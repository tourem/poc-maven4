#!/bin/bash

# Script de comparaison Maven 3 vs Maven 4
# Ce script exécute une série de tests et collecte les métriques

set -e

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fichier de résultats JSON
RESULTS_FILE="comparison-results.json"

echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}   Comparaison Maven 3 vs Maven 4${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo ""

# Initialiser le fichier JSON
cat > $RESULTS_FILE << 'EOF'
{
  "timestamp": "",
  "maven3": {
    "version": "",
    "tests": {}
  },
  "maven4": {
    "version": "",
    "tests": {}
  }
}
EOF

# Fonction pour mesurer le temps d'exécution
measure_time() {
    local start=$(date +%s.%N)
    "$@"
    local end=$(date +%s.%N)
    local duration=$(echo "$end - $start" | bc)
    echo $duration
}

# Fonction pour exécuter un test Maven
run_maven_test() {
    local maven_cmd=$1
    local test_name=$2
    local maven_args=$3
    local branch=$4
    
    echo -e "${YELLOW}▶ Test: $test_name${NC}"
    echo -e "  Commande: $maven_cmd $maven_args"
    echo -e "  Branche: $branch"
    
    # Basculer sur la bonne branche
    git checkout $branch > /dev/null 2>&1
    
    # Nettoyer avant le test
    bash -c "source ~/.bashrc && $maven_cmd clean > /dev/null 2>&1" || true
    
    # Mesurer le temps
    local start=$(date +%s.%N)
    bash -c "source ~/.bashrc && $maven_cmd $maven_args" > /tmp/maven_output.log 2>&1
    local exit_code=$?
    local end=$(date +%s.%N)
    local duration=$(echo "$end - $start" | bc)
    
    if [ $exit_code -eq 0 ]; then
        echo -e "  ${GREEN}✓ Succès${NC} - Durée: ${duration}s"
    else
        echo -e "  ${RED}✗ Échec${NC} - Durée: ${duration}s"
    fi
    
    echo $duration
}

# Fonction pour ajouter un résultat au JSON
add_result() {
    local maven_version=$1
    local test_name=$2
    local duration=$3
    local success=$4
    
    # Utiliser jq pour ajouter le résultat
    jq --arg mv "$maven_version" \
       --arg tn "$test_name" \
       --arg dur "$duration" \
       --arg succ "$success" \
       '.[$mv].tests[$tn] = {"duration": $dur, "success": $succ}' \
       $RESULTS_FILE > ${RESULTS_FILE}.tmp && mv ${RESULTS_FILE}.tmp $RESULTS_FILE
}

echo -e "\n${BLUE}═══ Phase 1: Vérification des versions ═══${NC}\n"

# Récupérer les versions
MAVEN3_VERSION=$(bash -c "source ~/.bashrc && mvn3 --version 2>/dev/null | head -n 1" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')
MAVEN4_VERSION=$(bash -c "source ~/.bashrc && mvn4 --version 2>/dev/null | head -n 1" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+(-[a-z0-9-]+)?')

echo -e "Maven 3: ${GREEN}$MAVEN3_VERSION${NC}"
echo -e "Maven 4: ${GREEN}$MAVEN4_VERSION${NC}"

# Mettre à jour le JSON avec les versions
jq --arg v3 "$MAVEN3_VERSION" --arg v4 "$MAVEN4_VERSION" --arg ts "$(date -u +%Y-%m-%dT%H:%M:%SZ)" \
   '.maven3.version = $v3 | .maven4.version = $v4 | .timestamp = $ts' \
   $RESULTS_FILE > ${RESULTS_FILE}.tmp && mv ${RESULTS_FILE}.tmp $RESULTS_FILE

echo -e "\n${BLUE}═══ Phase 2: Tests Maven 3 ═══${NC}\n"

# Test 1: Build clean package (Maven 3)
duration=$(run_maven_test "mvn3" "Build clean package" "clean package -DskipTests" "maven3-test")
add_result "maven3" "clean_package_no_tests" "$duration" "true"

# Test 2: Build avec tests (Maven 3)
duration=$(run_maven_test "mvn3" "Build avec tests" "clean package" "maven3-test")
add_result "maven3" "clean_package_with_tests" "$duration" "true"

# Test 3: Build avec cache (second build) (Maven 3)
duration=$(run_maven_test "mvn3" "Build avec cache" "package -DskipTests" "maven3-test")
add_result "maven3" "package_cached_no_tests" "$duration" "true"

# Test 4: Tests seuls (Maven 3)
duration=$(run_maven_test "mvn3" "Exécution tests" "test" "maven3-test")
add_result "maven3" "test_only" "$duration" "true"

# Test 5: Dependency tree (Maven 3)
duration=$(run_maven_test "mvn3" "Dependency tree" "dependency:tree" "maven3-test")
add_result "maven3" "dependency_tree" "$duration" "true"

# Test 6: Build parallèle (Maven 3)
duration=$(run_maven_test "mvn3" "Build parallèle -T 4" "clean package -T 4 -DskipTests" "maven3-test")
add_result "maven3" "parallel_build" "$duration" "true"

echo -e "\n${BLUE}═══ Phase 3: Tests Maven 4 ═══${NC}\n"

# Test 1: Build clean package (Maven 4)
duration=$(run_maven_test "mvn4" "Build clean package" "clean package -DskipTests" "maven4-test")
add_result "maven4" "clean_package_no_tests" "$duration" "true"

# Test 2: Build avec tests (Maven 4)
duration=$(run_maven_test "mvn4" "Build avec tests" "clean package" "maven4-test")
add_result "maven4" "clean_package_with_tests" "$duration" "true"

# Test 3: Build avec cache (second build) (Maven 4)
duration=$(run_maven_test "mvn4" "Build avec cache" "package -DskipTests" "maven4-test")
add_result "maven4" "package_cached_no_tests" "$duration" "true"

# Test 4: Tests seuls (Maven 4)
duration=$(run_maven_test "mvn4" "Exécution tests" "test" "maven4-test")
add_result "maven4" "test_only" "$duration" "true"

# Test 5: Dependency tree (Maven 4)
duration=$(run_maven_test "mvn4" "Dependency tree" "dependency:tree" "maven4-test")
add_result "maven4" "dependency_tree" "$duration" "true"

# Test 6: Build parallèle (Maven 4)
duration=$(run_maven_test "mvn4" "Build parallèle -T 4" "clean package -T 4 -DskipTests" "maven4-test")
add_result "maven4" "parallel_build" "$duration" "true"

echo -e "\n${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✓ Tous les tests sont terminés !${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "\nRésultats sauvegardés dans: ${YELLOW}$RESULTS_FILE${NC}"
echo ""

# Revenir sur la branche main
git checkout main > /dev/null 2>&1

echo -e "${BLUE}Génération du rapport HTML...${NC}"

