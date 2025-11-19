#!/bin/bash

# Script pour générer un rapport PDF à partir du rapport HTML
# Nécessite wkhtmltopdf ou Chrome/Chromium

echo "🔄 Génération du rapport PDF..."

# Vérifier si wkhtmltopdf est installé
if command -v wkhtmltopdf &> /dev/null; then
    echo "✓ Utilisation de wkhtmltopdf"
    wkhtmltopdf \
        --enable-local-file-access \
        --page-size A4 \
        --margin-top 10mm \
        --margin-bottom 10mm \
        --margin-left 10mm \
        --margin-right 10mm \
        maven-comparison-report.html \
        maven-comparison-report.pdf
    echo "✓ Rapport PDF généré: maven-comparison-report.pdf"
    open maven-comparison-report.pdf
elif command -v google-chrome &> /dev/null; then
    echo "✓ Utilisation de Google Chrome"
    google-chrome --headless --disable-gpu --print-to-pdf=maven-comparison-report.pdf maven-comparison-report.html
    echo "✓ Rapport PDF généré: maven-comparison-report.pdf"
    open maven-comparison-report.pdf
elif command -v chromium &> /dev/null; then
    echo "✓ Utilisation de Chromium"
    chromium --headless --disable-gpu --print-to-pdf=maven-comparison-report.pdf maven-comparison-report.html
    echo "✓ Rapport PDF généré: maven-comparison-report.pdf"
    open maven-comparison-report.pdf
else
    echo "❌ Aucun outil de conversion PDF trouvé"
    echo "   Installez wkhtmltopdf, Google Chrome ou Chromium"
    echo ""
    echo "   Installation sur macOS:"
    echo "   brew install wkhtmltopdf"
    echo ""
    echo "   Ou utilisez l'impression du navigateur:"
    echo "   1. Ouvrez maven-comparison-report.html dans votre navigateur"
    echo "   2. Utilisez Cmd+P (ou Ctrl+P)"
    echo "   3. Sélectionnez 'Enregistrer au format PDF'"
    exit 1
fi

