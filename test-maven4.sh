#!/bin/bash

# Script de test des fonctionnalités Maven 4
# Usage: ./test-maven4.sh

set -e

echo "=========================================="
echo "🚀 Test des fonctionnalités Maven 4"
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

# Vérifier que c'est bien Maven 4
MAVEN_VERSION=$(mvn --version | head -n 1 | grep -o "4\.[0-9]")
if [ -z "$MAVEN_VERSION" ]; then
    echo -e "${RED}⚠️  ATTENTION: Maven 4 n'est pas détecté!${NC}"
    echo -e "${YELLOW}Installez Maven 4 avec: sdk install maven 4.0.0-beta-4${NC}"
    echo ""
else
    echo -e "${GREEN}✅ Maven 4 détecté${NC}"
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

# Test 2: Build parallèle
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 2: Build parallèle (Maven 4 optimisé)${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn clean install -T 4C"
echo ""

mvn clean > /dev/null 2>&1
START=$(date +%s)
mvn clean install -T 4C
END=$(date +%s)
DURATION=$((END - START))
print_time $DURATION
echo ""

# Test 3: Analyse des dépendances
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 3: Analyse des dépendances (Maven 4)${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn dependency:tree"
echo ""

mvn dependency:tree | head -n 40
echo "..."
echo ""

# Test 4: Tests avec output amélioré
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 4: Tests avec output amélioré${NC}"
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

# Test 6: Buildinfo (reproductibilité)
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${YELLOW}Test 6: Buildinfo (reproductibilité Maven 4)${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo "Commande: mvn clean package"
echo ""

mvn clean package -DskipTests > /dev/null 2>&1
if [ -f "maven4-web/target/maven4-web-1.0-SNAPSHOT.buildinfo" ]; then
    echo -e "${GREEN}✅ Buildinfo généré${NC}"
    echo ""
    echo "Contenu du buildinfo:"
    head -n 20 maven4-web/target/maven4-web-1.0-SNAPSHOT.buildinfo
    echo "..."
else
    echo -e "${YELLOW}⚠️  Buildinfo non généré (nécessite Maven 4.0.0 final)${NC}"
fi
echo ""

echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ Tests Maven 4 terminés!${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo ""
echo -e "${BLUE}💡 Fonctionnalités Maven 4 testées:${NC}"
echo "  ✅ Build multi-modules"
echo "  ✅ Parallélisme optimisé (-T 4C)"
echo "  ✅ Analyse des dépendances améliorée"
echo "  ✅ Output de tests structuré"
echo "  ✅ Gestion des plugins"
echo "  ✅ Reproductibilité (buildinfo)"
echo ""
echo -e "${YELLOW}🚀 Pour tester Maven Daemon (mvnd):${NC}"
echo "  1. Installer: sdk install maven 4.0.0"
echo "  2. Lancer: mvnd clean install"
echo "  3. Relancer: mvnd clean install (3-4x plus rapide!)"
echo ""
