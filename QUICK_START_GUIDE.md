# 🚀 Guide de démarrage rapide - Maven 3 vs Maven 4

## ⚡ En 3 minutes

### 1️⃣ Cloner le projet
```bash
git clone https://github.com/tourem/poc-maven4.git
cd poc-maven4
```

### 2️⃣ Choisir une branche

#### Option A : Tester Maven 3
```bash
git checkout maven3-test
sdk install maven 3.9.9
sdk use maven 3.9.9
./test-maven3.sh
```

#### Option B : Tester Maven 4
```bash
git checkout maven4-test
sdk install maven 4.0.0-beta-4
sdk use maven 4.0.0-beta-4
./test-maven4.sh
```

### 3️⃣ Comparer les résultats
```bash
# Noter les temps de build
# Observer les différences de fonctionnalités
# Lire MAVEN_VERSION.md sur chaque branche
```

---

## 📊 Résultats attendus

### Maven 3 (maven3-test)
- ⏱️ Build: **6-8 secondes**
- ⚠️ Parallélisme instable
- ❌ Pas de daemon

### Maven 4 (maven4-test)
- ⏱️ Build: **4-5 secondes** (-30%)
- ⏱️ Build parallèle: **2-3 secondes** (-50%)
- ⏱️ Build daemon: **1-2 secondes** (-70%)
- ✅ Parallélisme stable
- ✅ Messages d'erreur détaillés

---

## 🔗 Liens utiles

- **Repository**: https://github.com/tourem/poc-maven4
- **Documentation complète**: [README.md](README.md)
- **Guide des branches**: [BRANCHES.md](BRANCHES.md)

---

**Prêt en 3 minutes ! 🎉**
