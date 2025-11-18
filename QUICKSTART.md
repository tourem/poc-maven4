# 🚀 Guide de démarrage rapide

## Installation en 5 minutes

### 1. Installer Maven 4

```bash
# Via SDKMAN (recommandé)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install maven 4.0.0

# Vérifier
mvn --version
```

### 2. Build du projet

```bash
# Cloner et builder
cd poc-demo-maven4
mvn clean install

# Ou avec parallélisme
mvn clean install -T 4C
```

### 3. Lancer l'application

```bash
cd maven4-web
mvn spring-boot:run
```

### 4. Tester l'API

Ouvrir dans le navigateur : http://localhost:8080/swagger-ui.html

## 🎯 Tests rapides Maven 4

### Test 1 : Performance du build

```bash
# Maven 3 ou 4 standard
time mvn clean install

# Maven 4 avec parallélisme
time mvn clean install -T 4C

# Maven 4 Daemon (installer mvnd d'abord)
time mvnd clean install
```

### Test 2 : Analyse des dépendances

```bash
# Arbre des dépendances
mvn dependency:tree

# Analyser les conflits
mvn dependency:tree -Dverbose

# Dépendances non utilisées
mvn dependency:analyze
```

### Test 3 : Tests avec output amélioré

```bash
# Tous les tests
mvn test

# Tests d'un module spécifique
mvn test -pl maven4-service

# Test spécifique (Maven 4)
mvn test --test UserServiceTest
```

## 📊 Comparaison rapide

| Fonctionnalité | Maven 3 | Maven 4 |
|----------------|---------|---------|
| Build standard | 45-60s | 30-40s |
| Build parallèle | Instable | 20-25s |
| Build daemon | N/A | 10-15s |
| Messages erreur | Basiques | Détaillés |
| Analyse deps | Limitée | Avancée |

## 🔗 Liens utiles

- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **H2 Console** : http://localhost:8080/h2-console
- **Actuator** : http://localhost:8080/actuator/health

## 📚 Documentation complète

Voir [README.md](./README.md) pour la documentation complète.

## 🆘 Problèmes courants

### Erreur : Java version

```bash
# Installer Java 17
sdk install java 17.0.9-tem
sdk use java 17.0.9-tem
```

### Erreur : Port 8080 déjà utilisé

```bash
# Changer le port dans application.yml
server:
  port: 8081
```

### Build lent

```bash
# Utiliser le parallélisme
mvn clean install -T 4C

# Ou installer mvnd
sdk install maven 4.0.0
mvnd clean install
```

## 🎓 Prochaines étapes

1. ✅ Tester les endpoints API via Swagger
2. ✅ Comparer les temps de build Maven 3 vs 4
3. ✅ Explorer les messages d'erreur améliorés
4. ✅ Tester le build parallèle
5. ✅ Essayer Maven Daemon (mvnd)

---

**Bon test ! 🚀**
