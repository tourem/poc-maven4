#!/bin/bash

# Script de comparaison Maven 3 vs Maven 4
set -e

# Chemins Maven
MAVEN3_BIN="/opt/homebrew/Cellar/maven/3.9.9/bin/mvn"
MAVEN4_BIN="/Users/mtoure/install/apache-maven-4.0.0-rc-5/bin/mvn"

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Fichier de résultats
RESULTS_FILE="comparison-results.json"

echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}        Comparaison Maven 3 vs Maven 4${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo ""

# Vérifier que les binaires Maven existent
if [ ! -f "$MAVEN3_BIN" ]; then
    echo -e "${RED}Erreur: Maven 3 non trouvé à $MAVEN3_BIN${NC}"
    exit 1
fi

if [ ! -f "$MAVEN4_BIN" ]; then
    echo -e "${RED}Erreur: Maven 4 non trouvé à $MAVEN4_BIN${NC}"
    exit 1
fi

# Initialiser le fichier JSON
cat > $RESULTS_FILE << 'EOF'
{
  "timestamp": "",
  "project": "poc-demo-maven4",
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

# Fonction pour mesurer le temps et exécuter une commande
run_test() {
    local maven_bin=$1
    local maven_name=$2
    local test_name=$3
    local maven_args=$4
    local branch=$5
    
    echo -e "\n${CYAN}▶ Test: ${test_name}${NC}"
    echo -e "  Maven: ${maven_name}"
    echo -e "  Commande: mvn ${maven_args}"
    echo -e "  Branche: ${branch}"
    
    # Basculer sur la bonne branche
    git checkout $branch > /dev/null 2>&1
    
    # Mesurer le temps
    local start=$(date +%s)
    $maven_bin $maven_args > /tmp/maven_test_output.log 2>&1
    local exit_code=$?
    local end=$(date +%s)
    local duration=$((end - start))
    
    if [ $exit_code -eq 0 ]; then
        echo -e "  ${GREEN}✓ Succès${NC} - Durée: ${YELLOW}${duration}s${NC}"
    else
        echo -e "  ${RED}✗ Échec${NC} - Durée: ${duration}s"
        echo -e "  ${RED}Voir les logs: /tmp/maven_test_output.log${NC}"
    fi
    
    echo $duration
}

# Récupérer les versions Maven
echo -e "${BLUE}═══ Vérification des versions ═══${NC}"
MAVEN3_VERSION=$($MAVEN3_BIN --version | head -n 1 | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')
MAVEN4_VERSION=$($MAVEN4_BIN --version | head -n 1 | grep -oE '[0-9]+\.[0-9]+\.[0-9]+(-[a-z0-9-]+)?')

echo -e "Maven 3: ${GREEN}${MAVEN3_VERSION}${NC}"
echo -e "Maven 4: ${GREEN}${MAVEN4_VERSION}${NC}"

# Mettre à jour le JSON avec les versions et timestamp
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
cat > $RESULTS_FILE << EOF
{
  "timestamp": "$TIMESTAMP",
  "project": "poc-demo-maven4",
  "maven3": {
    "version": "$MAVEN3_VERSION",
    "tests": {}
  },
  "maven4": {
    "version": "$MAVEN4_VERSION",
    "tests": {}
  }
}
EOF

echo -e "\n${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}        TESTS MAVEN 3 (branche: maven3-test)${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"

# Test 1: Clean package sans tests (Maven 3)
duration=$(run_test "$MAVEN3_BIN" "Maven 3" "Clean package (sans tests)" "clean package -DskipTests" "maven3-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test1_m3.json

# Test 2: Clean package avec tests (Maven 3)
duration=$(run_test "$MAVEN3_BIN" "Maven 3" "Clean package (avec tests)" "clean package" "maven3-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test2_m3.json

# Test 3: Package avec cache (Maven 3)
duration=$(run_test "$MAVEN3_BIN" "Maven 3" "Package avec cache (sans tests)" "package -DskipTests" "maven3-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test3_m3.json

# Test 4: Tests seuls (Maven 3)
duration=$(run_test "$MAVEN3_BIN" "Maven 3" "Exécution des tests" "test" "maven3-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test4_m3.json

# Test 5: Dependency tree (Maven 3)
duration=$(run_test "$MAVEN3_BIN" "Maven 3" "Dependency tree" "dependency:tree" "maven3-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test5_m3.json

# Test 6: Build parallèle (Maven 3)
duration=$(run_test "$MAVEN3_BIN" "Maven 3" "Build parallèle (-T 4)" "clean package -T 4 -DskipTests" "maven3-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test6_m3.json

echo -e "\n${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}        TESTS MAVEN 4 (branche: maven4-test)${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"

# Nettoyer le cache Maven pour Maven 4
rm -rf ~/.m2/repository/* 2>/dev/null || true

# Test 1: Clean package sans tests (Maven 4)
duration=$(run_test "$MAVEN4_BIN" "Maven 4" "Clean package (sans tests)" "clean package -DskipTests" "maven4-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test1_m4.json

# Test 2: Clean package avec tests (Maven 4)
duration=$(run_test "$MAVEN4_BIN" "Maven 4" "Clean package (avec tests)" "clean package" "maven4-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test2_m4.json

# Test 3: Package avec cache (Maven 4)
duration=$(run_test "$MAVEN4_BIN" "Maven 4" "Package avec cache (sans tests)" "package -DskipTests" "maven4-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test3_m4.json

# Test 4: Tests seuls (Maven 4)
duration=$(run_test "$MAVEN4_BIN" "Maven 4" "Exécution des tests" "test" "maven4-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test4_m4.json

# Test 5: Dependency tree (Maven 4)
duration=$(run_test "$MAVEN4_BIN" "Maven 4" "Dependency tree" "dependency:tree" "maven4-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test5_m4.json

# Test 6: Build parallèle (Maven 4)
duration=$(run_test "$MAVEN4_BIN" "Maven 4" "Build parallèle (-T 4)" "clean package -T 4 -DskipTests" "maven4-test")
echo "{\"duration\": $duration, \"success\": true}" > /tmp/test6_m4.json

echo -e "\n${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✓ Tous les tests sont terminés !${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"

# Construire le fichier JSON final avec tous les résultats
cat > $RESULTS_FILE << EOF
{
  "timestamp": "$TIMESTAMP",
  "project": "poc-demo-maven4",
  "maven3": {
    "version": "$MAVEN3_VERSION",
    "tests": {
      "clean_package_no_tests": $(cat /tmp/test1_m3.json),
      "clean_package_with_tests": $(cat /tmp/test2_m3.json),
      "package_cached_no_tests": $(cat /tmp/test3_m3.json),
      "test_only": $(cat /tmp/test4_m3.json),
      "dependency_tree": $(cat /tmp/test5_m3.json),
      "parallel_build": $(cat /tmp/test6_m3.json)
    }
  },
  "maven4": {
    "version": "$MAVEN4_VERSION",
    "tests": {
      "clean_package_no_tests": $(cat /tmp/test1_m4.json),
      "clean_package_with_tests": $(cat /tmp/test2_m4.json),
      "package_cached_no_tests": $(cat /tmp/test3_m4.json),
      "test_only": $(cat /tmp/test4_m4.json),
      "dependency_tree": $(cat /tmp/test5_m4.json),
      "parallel_build": $(cat /tmp/test6_m4.json)
    }
  }
}
EOF

echo -e "\nRésultats sauvegardés dans: ${YELLOW}$RESULTS_FILE${NC}"
cat $RESULTS_FILE

# Revenir sur la branche main
git checkout main > /dev/null 2>&1

echo -e "\n${GREEN}Prêt pour la génération du rapport HTML !${NC}"

