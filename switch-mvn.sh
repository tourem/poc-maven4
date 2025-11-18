###
# MAVEN VERSION SWITCHER
# Ajouter dans ~/.bashrc ou ~/.zshrc
###

# Emplacements Maven 3 et Maven 4
export MAVEN3_HOME="$HOME/tools/apache-maven-3.9.9"        # <-- adapte selon ton chemin
export MAVEN4_HOME="$HOME/tools/apache-maven-4.0.0-beta-3" # <-- adapte selon ton chemin

# Fonction : utiliser Maven 3
use_maven3() {
    if [ ! -d "$MAVEN3_HOME" ]; then
        echo "❌ MAVEN3_HOME n'existe pas : $MAVEN3_HOME"
        return 1
    fi
    export MAVEN_HOME="$MAVEN3_HOME"
    export PATH="$MAVEN_HOME/bin:$(echo $PATH | tr ':' '\n' | grep -v 'apache-maven' | paste -sd ':' -)"
    echo "✔ Maven 3 activé : $(mvn -version | head -n 1)"
}

# Fonction : utiliser Maven 4
use_maven4() {
    if [ ! -d "$MAVEN4_HOME" ]; then
        echo "❌ MAVEN4_HOME n'existe pas : $MAVEN4_HOME"
        return 1
    fi
    export MAVEN_HOME="$MAVEN4_HOME"
    export PATH="$MAVEN_HOME/bin:$(echo $PATH | tr ':' '\n' | grep -v 'apache-maven' | paste -sd ':' -)"
    echo "✔ Maven 4 activé : $(mvn -version | head -n 1)"
}

# Fonction : afficher la version Maven active
maven_active() {
    echo "➡ Maven utilisé :"
    mvn -version
}
