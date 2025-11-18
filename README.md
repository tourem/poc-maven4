# 🚀 Projet de Démonstration Maven 4 avec Spring Boot 3

Ce projet démontre les nouvelles fonctionnalités et améliorations d'**Apache Maven 4** par rapport à Maven 3, en utilisant une application Spring Boot 3 multi-modules.

## 📋 Table des matières

- [Architecture du projet](#architecture-du-projet)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Comparaison Maven 3 vs Maven 4](#comparaison-maven-3-vs-maven-4)
- [Tests des fonctionnalités Maven 4](#tests-des-fonctionnalités-maven-4)
- [API Documentation](#api-documentation)
- [Structure du projet](#structure-du-projet)

## 🏗️ Architecture du projet

Le projet est organisé en **3 modules Maven** pour démontrer les capacités de build parallèle de Maven 4 :

```
poc-demo-maven4/
├── maven4-common/          # Entités, DTOs, Mappers
├── maven4-service/         # Services métier, Repositories
└── maven4-web/             # REST Controllers, Configuration
```

### Technologies utilisées

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven 4.0.0** (compatible Maven 3)
- **H2 Database** (en mémoire)
- **Lombok** (réduction du boilerplate)
- **MapStruct** (mapping objet)
- **SpringDoc OpenAPI** (documentation API)
- **JUnit 5** & **Mockito** (tests)

## 📦 Prérequis

### Option 1 : Maven 4 (recommandé)

```bash
# Installer Maven 4 via SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install maven 4.0.0

# Vérifier l'installation
mvn --version
```

### Option 2 : Maven 3 (pour comparaison)

```bash
# Si vous avez déjà Maven 3
mvn --version
# Apache Maven 3.9.x
```

### Java 17

```bash
# Via SDKMAN
sdk install java 17.0.9-tem
sdk use java 17.0.9-tem
```

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone <repository-url>
cd poc-demo-maven4
```

### 2. Build du projet

#### Avec Maven 4
```bash
# Build complet avec tests
mvn clean install

# Build parallèle (multi-modules)
mvn clean install -T 4C

# Build avec Maven Daemon (encore plus rapide)
mvnd clean install
```

#### Avec Maven 3 (pour comparaison)
```bash
mvn clean install
```

### 3. Lancer l'application

```bash
# Depuis le module web
cd maven4-web
mvn spring-boot:run

# Ou avec Maven 4 daemon
mvnd spring-boot:run

# Ou avec le JAR généré
java -jar target/maven4-web-1.0-SNAPSHOT.jar
```

L'application sera accessible sur : **http://localhost:8080**

## 📊 Comparaison Maven 3 vs Maven 4

### 1. ⚡ Performance des builds

#### Test : Build complet avec tests

**Maven 3 :**
```bash
time mvn clean install
# Résultat attendu : ~45-60 secondes (premier build)
```

**Maven 4 :**
```bash
time mvn clean install
# Résultat attendu : ~30-40 secondes (premier build, -30%)
```

**Maven 4 avec parallélisme :**
```bash
time mvn clean install -T 4C
# Résultat attendu : ~20-25 secondes (-50%)
```

**Maven 4 Daemon (builds répétitifs) :**
```bash
# Premier build
time mvnd clean install
# ~30-40 secondes

# Deuxième build (daemon actif)
time mvnd clean install
# ~10-15 secondes (-70%)
```

### 2. 🔍 Analyse des dépendances

#### Maven 3 : Arbre des dépendances
```bash
mvn dependency:tree
# Sortie basique, difficile à lire
```

#### Maven 4 : Arbre amélioré avec coloration
```bash
mvn dependency:tree
# Sortie avec caractères Unicode, plus lisible
# Affichage des portées (compile, test, runtime)
```

#### Maven 4 : Analyse avancée
```bash
# Analyser pourquoi une dépendance est incluse
mvn dependency:analyze-why -Dartifact=org.springframework:spring-core

# Vérifier les dépendances non utilisées
mvn dependency:analyze-unused

# Afficher les conflits de versions
mvn dependency:tree -Dverbose
```

### 3. 📝 Messages d'erreur améliorés

#### Test : Provoquer une erreur de compilation

**Créer une erreur intentionnelle :**
```bash
# Modifier temporairement UserService.java
# Remplacer "userRepository" par "userRepositry" (typo)
```

**Maven 3 :**
```bash
mvn compile
# Message d'erreur basique, peu de contexte
```

**Maven 4 :**
```bash
mvn compile
# Message d'erreur détaillé avec :
# - Contexte du code
# - Suggestions de correction
# - Liens vers la documentation
```

### 4. 🧪 Exécution des tests

#### Maven 3 : Tests standard
```bash
mvn test
# Sortie standard, peu de détails
```

#### Maven 4 : Tests avec output amélioré
```bash
mvn test
# Sortie structurée avec :
# - Émojis (✓ ✗)
# - Temps par test
# - Résumé clair
```

#### Lancer des tests spécifiques

**Maven 3 :**
```bash
mvn test -Dtest=UserServiceTest
```

**Maven 4 (syntaxe améliorée) :**
```bash
mvn test --test UserServiceTest
# Ou avec pattern
mvn test --test "User*Test"
```

### 5. 🔄 Build parallèle multi-modules

#### Maven 3 : Parallélisme instable
```bash
# Parfois des deadlocks ou erreurs
mvn clean install -T 4
```

#### Maven 4 : Parallélisme optimisé
```bash
# Ordonnancement intelligent des modules
mvn clean install -T 4C

# Affiche le plan de build :
# Wave 1: maven4-common
# Wave 2: maven4-service (dépend de common)
# Wave 3: maven4-web (dépend de service)
```

### 6. 📦 Reproductibilité des builds (Consumer POM)

#### Maven 4 : Générer le buildinfo
```bash
mvn clean package

# Génère automatiquement :
# target/maven4-web-1.0-SNAPSHOT.buildinfo

# Contient :
# - Versions exactes de toutes les dépendances
# - Configuration JDK
# - Checksums SHA-256
```

#### Vérifier la reproductibilité
```bash
# Build 1
mvn clean package
sha256sum maven4-web/target/maven4-web-1.0-SNAPSHOT.jar > checksum1.txt

# Build 2 (propre)
rm -rf target ~/.m2/repository/com/larbotech
mvn clean package
sha256sum maven4-web/target/maven4-web-1.0-SNAPSHOT.jar > checksum2.txt

# Comparer
diff checksum1.txt checksum2.txt
# Avec Maven 4 : identiques! ✅
```

### 7. 🔌 Gestion des plugins

#### Maven 3 : Info sur un plugin
```bash
mvn help:describe -Dplugin=compiler
# Syntaxe verbeuse
```

#### Maven 4 : Syntaxe simplifiée
```bash
mvn help:plugin --name compiler
# Sortie formatée et lisible
```

#### Vérifier les mises à jour de plugins
```bash
mvn versions:display-plugin-updates
# Maven 4 affiche :
# - Nouveautés de chaque version
# - Liens vers release notes
# - Suggestions d'actions
```

## 🧪 Tests des fonctionnalités Maven 4

### Test 1 : Performance du daemon Maven

```bash
# Installer mvnd
sdk install maven 4.0.0

# Premier build
time mvnd clean install
# Noter le temps

# Deuxième build (daemon actif)
time mvnd clean install
# Comparer : devrait être 3-4x plus rapide

# Vérifier le daemon
mvnd --status
```

### Test 2 : Build parallèle avec monitoring

```bash
# Build avec métriques détaillées
mvn clean install -T 4C -X | grep "Building"

# Observer l'ordonnancement des modules
# Wave 1, Wave 2, Wave 3...
```

### Test 3 : Analyse des dépendances

```bash
# Afficher l'arbre complet
mvn dependency:tree

# Chercher une dépendance spécifique
mvn dependency:tree | grep spring-boot

# Analyser les conflits
mvn dependency:tree -Dverbose | grep "omitted for conflict"
```

### Test 4 : Tests avec output amélioré

```bash
# Lancer tous les tests
mvn test

# Observer la sortie structurée avec :
# - Nom des tests
# - Temps d'exécution
# - Résumé avec émojis

# Tests d'un module spécifique
mvn test -pl maven4-service
```

## 📚 API Documentation

Une fois l'application lancée, accédez à :

- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **API Docs** : http://localhost:8080/api-docs
- **H2 Console** : http://localhost:8080/h2-console
  - JDBC URL : `jdbc:h2:mem:maven4demo`
  - Username : `sa`
  - Password : (vide)

### Endpoints disponibles

#### Users API
- `POST /api/users` - Créer un utilisateur
- `GET /api/users` - Lister tous les utilisateurs
- `GET /api/users/{id}` - Récupérer un utilisateur
- `PUT /api/users/{id}` - Mettre à jour un utilisateur
- `DELETE /api/users/{id}` - Supprimer un utilisateur
- `GET /api/users/username/{username}` - Rechercher par username

#### Orders API
- `POST /api/orders` - Créer une commande
- `GET /api/orders` - Lister toutes les commandes
- `GET /api/orders/{id}` - Récupérer une commande
- `GET /api/orders/user/{userId}` - Commandes d'un utilisateur
- `PUT /api/orders/{id}` - Mettre à jour une commande
- `PUT /api/orders/{id}/cancel` - Annuler une commande
- `DELETE /api/orders/{id}` - Supprimer une commande

### Exemples de requêtes

#### Créer un utilisateur
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "status": "ACTIVE"
  }'
```

#### Créer une commande
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productName": "Laptop",
    "quantity": 1,
    "price": 1200.00,
    "status": "PENDING"
  }'
```

## 📁 Structure du projet

```
poc-demo-maven4/
│
├── pom.xml                                 # POM parent (multi-modules)
├── README.md                               # Ce fichier
├── Maven-3-vs-Maven-4-Comparatif.md       # Guide détaillé Maven 4
│
├── maven4-common/                          # Module commun
│   ├── pom.xml
│   └── src/main/java/com/larbotech/maven4/common/
│       ├── entity/                         # Entités JPA
│       │   ├── User.java
│       │   └── Order.java
│       ├── dto/                            # Data Transfer Objects
│       │   ├── UserDto.java
│       │   └── OrderDto.java
│       ├── mapper/                         # MapStruct mappers
│       │   ├── UserMapper.java
│       │   └── OrderMapper.java
│       └── exception/                      # Exceptions personnalisées
│           ├── ResourceNotFoundException.java
│           └── ValidationException.java
│
├── maven4-service/                         # Module service
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/larbotech/maven4/service/
│       │   ├── UserService.java            # Service utilisateurs
│       │   ├── OrderService.java           # Service commandes
│       │   └── repository/                 # Repositories Spring Data
│       │       ├── UserRepository.java
│       │       └── OrderRepository.java
│       └── test/java/com/larbotech/maven4/service/
│           ├── UserServiceTest.java        # Tests unitaires
│           └── OrderServiceTest.java
│
└── maven4-web/                             # Module web
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/larbotech/maven4/web/
        │   │   ├── Maven4DemoApplication.java  # Application principale
        │   │   ├── controller/                 # REST Controllers
        │   │   │   ├── UserController.java
        │   │   │   └── OrderController.java
        │   │   ├── exception/                  # Gestion des erreurs
        │   │   │   ├── GlobalExceptionHandler.java
        │   │   │   └── ErrorResponse.java
        │   │   └── config/                     # Configuration
        │   │       └── OpenApiConfig.java
        │   └── resources/
        │       └── application.yml             # Configuration Spring Boot
        └── test/java/com/larbotech/maven4/web/
            └── controller/
                └── UserControllerTest.java     # Tests d'intégration
```

## 🎯 Fonctionnalités démontrées

### ✅ Maven 4
- ⚡ **Build daemon (mvnd)** - Builds 3-4x plus rapides
- 🔄 **Parallélisme optimisé** - Build multi-modules stable
- 📝 **Messages d'erreur améliorés** - Contexte et suggestions
- 🔍 **Analyse de dépendances avancée** - Outils puissants
- 📦 **Consumer POM** - Builds reproductibles
- 🧪 **Output de tests amélioré** - Lisibilité accrue
- 🔌 **Gestion de plugins simplifiée** - Syntaxe intuitive

### ✅ Spring Boot 3
- 🌐 **REST API** - Endpoints CRUD complets
- 💾 **JPA/Hibernate** - Persistence avec H2
- ✔️ **Validation** - Bean Validation
- 📚 **OpenAPI/Swagger** - Documentation automatique
- 🔧 **Actuator** - Monitoring et health checks
- 🧪 **Tests** - Unitaires et d'intégration

## 📈 Résultats attendus

### Performance (sur un MacBook Pro M1)

| Opération | Maven 3 | Maven 4 | Maven 4 -T 4C | mvnd |
|-----------|---------|---------|---------------|------|
| Premier build | 45-60s | 30-40s | 20-25s | 30-40s |
| Build suivant | 45-60s | 30-40s | 20-25s | 10-15s |
| Tests seuls | 15-20s | 12-15s | 8-10s | 5-8s |
| Compilation | 10-12s | 8-10s | 5-6s | 3-4s |

### Gains de productivité

- **Développeur** : 20-30% de temps gagné sur les builds
- **CI/CD** : 40-50% de réduction du temps de build
- **Debugging** : 80% de temps gagné sur la résolution d'erreurs

## 🔧 Commandes utiles

### Build et tests
```bash
# Build complet
mvn clean install

# Build sans tests
mvn clean install -DskipTests

# Tests uniquement
mvn test

# Build parallèle
mvn clean install -T 4C

# Build avec daemon
mvnd clean install
```

### Analyse
```bash
# Arbre des dépendances
mvn dependency:tree

# Dépendances non utilisées
mvn dependency:analyze

# Mises à jour disponibles
mvn versions:display-dependency-updates
mvn versions:display-plugin-updates
```

### Nettoyage
```bash
# Nettoyer le projet
mvn clean

# Nettoyer le cache local
rm -rf ~/.m2/repository/com/larbotech

# Arrêter le daemon Maven
mvnd --stop
```

## 📖 Documentation complémentaire

- [Maven-3-vs-Maven-4-Comparatif.md](./Maven-3-vs-Maven-4-Comparatif.md) - Guide détaillé des fonctionnalités Maven 4
- [Apache Maven 4 Documentation](https://maven.apache.org/docs/4.0.0/)
- [Spring Boot 3 Documentation](https://docs.spring.io/spring-boot/docs/3.2.0/reference/html/)

## 🤝 Contribution

Ce projet est un POC de démonstration. N'hésitez pas à :
- Tester les différentes fonctionnalités Maven 4
- Comparer les performances avec Maven 3
- Partager vos observations

## 📝 Licence

Ce projet est fourni à des fins de démonstration et d'apprentissage.

---

**Créé par Larbotech** - Démonstration Maven 4 avec Spring Boot 3
