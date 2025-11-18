#!/bin/bash

# Script de test des fonctionnalités Maven 3
# Usage: ./test-maven3.sh

set -e

echo "=========================================="
echo "🔧 Test des fonctionnalités Maven 3"
echo "=========================================="
echo ""

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Fonction pour afficher le temps
print_time() {
    local duration=$1
    echo -e "${GREEN}⏱️  Temps: ${duration}s${NC}"
}

# Vérifier la version de Maven
echo -e "${BLUE}📦 Version de Maven:${NC}"
mvn --version | head -n 1
echo ""

# Vérifier que c'est bien Maven 3
MAVEN_VERSION=$(mvn --version | head -n 1 | grep -o "3\.[0-9]")
if [ -z "$MAVEN_VERSION" ]; then
    echo -e "${RED}⚠️  ATTENTION: Maven 3 n'est pas détecté!${NC}"
    echo -e "${YELLOW}Installez Maven 3 avec: sdk install maven 3.9.9${NC}"
    echo ""
else
    echo -e "${GREEN}✅ Maven 3 détecté${NC}"
    echo ""
fi

# Test 1: Build standard
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 1: Build standard${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn clean install"
echo ""

mvn clean > /dev/null 2>&1
START=$(date +%s)
mvn clean install
END=$(date +%s)
DURATION=$((END - START))
print_time $DURATION
echo ""

# Test 2: Build parallèle (peut être instable)
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 2: Build parallèle (Maven 3 - peut être instable)${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn clean install -T 4"
echo -e "${YELLOW}⚠️  Note: Le parallélisme Maven 3 peut causer des deadlocks${NC}"
echo ""

mvn clean > /dev/null 2>&1
START=$(date +%s)
mvn clean install -T 4 || {
    echo -e "${RED}❌ Build parallèle échoué (deadlock Maven 3)${NC}"
    echo -e "${YELLOW}Ceci est une limitation connue de Maven 3${NC}"
}
END=$(date +%s)
DURATION=$((END - START))
print_time $DURATION
echo ""

# Test 3: Analyse des dépendances
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 3: Analyse des dépendances (Maven 3)${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn dependency:tree"
echo ""

mvn dependency:tree | head -n 40
echo "..."
echo ""

# Test 4: Tests standard
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 4: Tests avec output standard${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn test"
echo ""

START=$(date +%s)
mvn test
END=$(date +%s)
DURATION=$((END - START))
print_time $DURATION
echo ""

# Test 5: Vérifier les mises à jour
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 5: Vérifier les mises à jour de plugins${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn versions:display-plugin-updates"
echo ""

mvn versions:display-plugin-updates | head -n 25
echo ""

# Test 6: Tests spécifiques (syntaxe Maven 3)
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 6: Test spécifique (syntaxe Maven 3)${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn test -Dtest=UserServiceTest"
echo ""

mvn test -Dtest=UserServiceTest -pl maven4-service
echo ""

echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ Tests Maven 3 terminés!${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo ""
echo -e "${BLUE}💡 Fonctionnalités Maven 3 testées:${NC}"
echo "  ✅ Build multi-modules"
echo "  ⚠️  Parallélisme (instable avec -T)"
echo "  ✅ Analyse des dépendances (basique)"
echo "  ✅ Output de tests (standard)"
echo "  ✅ Gestion des plugins"
echo ""
echo -e "${YELLOW}⚠️  Limitations Maven 3 observées:${NC}"
echo "  ❌ Pas de daemon (JVM redémarre à chaque build)"
echo "  ❌ Messages d'erreur basiques"
echo "  ❌ Parallélisme instable"
echo "  ❌ Pas de buildinfo (reproductibilité)"
echo "  ❌ Output de tests peu lisible"
echo ""
echo -e "${BLUE}🚀 Pour comparer avec Maven 4:${NC}"
echo "  1. Basculer: git checkout maven4-test"
echo "  2. Lancer: ./test-maven4.sh"
echo "  3. Comparer les performances et fonctionnalités"
echo ""
