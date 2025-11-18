#!/bin/bash

# Script de comparaison Maven 3 vs Maven 4
# Usage: ./test-maven-comparison.sh

set -e

echo "=========================================="
echo "🚀 Test de comparaison Maven 3 vs Maven 4"
echo "=========================================="
echo ""

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
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

# Test 1: Build complet
echo -e "${YELLOW}Test 1: Build complet avec tests${NC}"
echo "Commande: mvn clean install"
echo ""

# Nettoyer avant le test
mvn clean > /dev/null 2>&1

# Mesurer le temps
START=$(date +%s)
mvn clean install
END=$(date +%s)
DURATION=$((END - START))
print_time $DURATION
echo ""

# Test 2: Build parallèle (si Maven 4)
echo -e "${YELLOW}Test 2: Build parallèle (multi-modules)${NC}"
echo "Commande: mvn clean install -T 4C"
echo ""

mvn clean > /dev/null 2>&1
START=$(date +%s)
mvn clean install -T 4C
END=$(date +%s)
DURATION=$((END - START))
print_time $DURATION
echo ""

# Test 3: Tests uniquement
echo -e "${YELLOW}Test 3: Exécution des tests${NC}"
echo "Commande: mvn test"
echo ""

START=$(date +%s)
mvn test
END=$(date +%s)
DURATION=$((END - START))
print_time $DURATION
echo ""

# Test 4: Analyse des dépendances
echo -e "${YELLOW}Test 4: Analyse des dépendances${NC}"
echo "Commande: mvn dependency:tree"
echo ""

mvn dependency:tree | head -n 30
echo "..."
echo ""

# Test 5: Vérifier les mises à jour
echo -e "${YELLOW}Test 5: Vérifier les mises à jour de plugins${NC}"
echo "Commande: mvn versions:display-plugin-updates"
echo ""

mvn versions:display-plugin-updates | head -n 20
echo ""

echo "=========================================="
echo -e "${GREEN}✅ Tests terminés!${NC}"
echo "=========================================="
echo ""
echo "💡 Conseils pour Maven 4:"
echo "  - Utilisez 'mvnd' au lieu de 'mvn' pour des builds encore plus rapides"
echo "  - Utilisez '-T 4C' pour le parallélisme optimal"
echo "  - Consultez le fichier README.md pour plus de détails"
echo ""
