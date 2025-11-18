# Configuration Maven 3

Cette branche est configurée pour **Apache Maven 3.9.9** (dernière version stable de Maven 3).

## Vérification de la version

```bash
mvn --version
# Devrait afficher: Apache Maven 3.9.9
```

## Fonctionnalités Maven 3 testées

### 1. Build standard
```bash
# Build classique Maven 3
mvn clean install

# Temps attendu: 6-8 secondes
```

### 2. Build parallèle (instable)
```bash
# Build avec parallélisme Maven 3
mvn clean install -T 4

# Note: Peut être instable avec des deadlocks
# Pas d'ordonnancement intelligent des modules
```

### 3. Messages d'erreur basiques
```bash
# En cas d'erreur, Maven 3 affiche :
# - Message d'erreur simple
# - Peu de contexte
# - Pas de suggestions
mvn compile
```

### 4. Analyse des dépendances standard
```bash
# Arbre des dépendances (sortie basique)
mvn dependency:tree

# Analyser les dépendances
mvn dependency:analyze

# Afficher les conflits (verbeux)
mvn dependency:tree -Dverbose
```

### 5. Output de tests standard
```bash
# Tests avec output classique
mvn test

# Syntaxe pour tests spécifiques
mvn test -Dtest=UserServiceTest
```

### 6. Gestion des plugins
```bash
# Info sur un plugin (syntaxe verbeuse)
mvn help:describe -Dplugin=compiler

# Vérifier les mises à jour
mvn versions:display-plugin-updates
```

## Benchmarks attendus

### Performance (MacBook Pro M1)
- **Build standard** : ~6-8 secondes
- **Build parallèle (-T 4)** : ~5-6 secondes (instable)
- **Tests** : ~2-3 secondes
- **Compilation** : ~2 secondes

### Limitations Maven 3
| Fonctionnalité | Maven 3 | Problème |
|----------------|---------|----------|
| Build parallèle | Instable | Deadlocks possibles |
| Messages erreur | Basiques | Peu de contexte |
| Daemon | Non disponible | Pas de réutilisation JVM |
| Dependency analysis | Limitée | Peu d'outils avancés |
| Reproductibilité | Partielle | Pas de buildinfo |

## Scripts de test

### test-maven3.sh
```bash
#!/bin/bash
echo "🔧 Test Maven 3 Features"
echo "========================"

# Test 1: Build standard
echo "Test 1: Build standard"
time mvn clean install

# Test 2: Build parallèle (peut être instable)
echo "Test 2: Build parallèle"
time mvn clean install -T 4

# Test 3: Analyse des dépendances
echo "Test 3: Analyse des dépendances"
mvn dependency:tree | head -30

# Test 4: Tests
echo "Test 4: Tests"
mvn test

echo "✅ Tests Maven 3 terminés"
```

## Configuration recommandée

### settings.xml (optionnel)
```xml
<settings>
  <profiles>
    <profile>
      <id>maven3</id>
      <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
      </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>maven3</activeProfile>
  </activeProfiles>
</settings>
```

## Comparaison avec Maven 4

### Ce qui manque dans Maven 3
- ❌ **Maven Daemon** - Pas de réutilisation de la JVM
- ❌ **Messages d'erreur détaillés** - Contexte limité
- ❌ **Build parallèle stable** - Deadlocks fréquents
- ❌ **Analyse avancée** - Outils limités
- ❌ **Buildinfo** - Pas de reproductibilité garantie
- ❌ **Output structuré** - Tests difficiles à lire

### Ce qui fonctionne bien
- ✅ **Stabilité** - Version mature et stable
- ✅ **Compatibilité** - Large écosystème
- ✅ **Documentation** - Bien documenté
- ✅ **Plugins** - Tous les plugins supportés

## Troubleshooting

### Problème : Maven 3 non détecté
```bash
# Vérifier la version
mvn --version

# Utiliser le wrapper Maven
./mvnw --version

# Installer Maven 3 via SDKMAN
sdk install maven 3.9.9
sdk use maven 3.9.9
```

### Problème : Build lent
```bash
# Essayer le parallélisme (peut être instable)
mvn clean install -T 4

# Ou skip les tests
mvn clean install -DskipTests
```

### Problème : Deadlock avec -T
```bash
# Réduire le nombre de threads
mvn clean install -T 2

# Ou revenir au build séquentiel
mvn clean install
```

## Notes importantes

- ✅ Version stable et mature
- ⚠️ Parallélisme instable
- ⚠️ Messages d'erreur basiques
- ⚠️ Pas de daemon disponible
- ✅ Tous les tests passent (22 tests)

## Migration vers Maven 4

Pour migrer vers Maven 4, basculez sur la branche `maven4-test` :

```bash
git checkout maven4-test
mvn clean install
```

Voir les différences :
```bash
git diff maven3-test maven4-test
```

---

**Branche** : maven3-test
**Version Maven** : 3.9.9
**Date** : 18 novembre 2025
