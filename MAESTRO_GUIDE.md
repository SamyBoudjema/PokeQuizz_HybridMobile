# 🎮 Guide de Tests Maestro - PokéApp

## 📋 Ce qui a été configuré

J'ai installé et configuré Maestro pour ton application PokéApp Kotlin Multiplatform. Voici ce qui a été mis en place :

### ✅ Installation
- Maestro 2.0.6 installé sur ton système macOS
- Ajouté automatiquement à ton PATH

### 📁 Structure des tests créés

**7 workflows de test** dans le dossier `.maestro/` :

1. **01-test-revision-pokemon.yaml** 
   - Teste l'ouverture du mode révision
   - Vérifie le chargement du Pokémon
   - Teste le toggle noir & blanc
   - Teste le rechargement d'un nouveau Pokémon

2. **02-test-quiz-pokemon.yaml**
   - Teste le flux complet du quiz
   - Réponses correctes et incorrectes
   - Navigation entre les 10 questions
   - Enregistrement du score

3. **03-test-classement.yaml**
   - Teste l'ouverture et fermeture du classement

4. **04-test-navigation-complete.yaml**
   - Teste la navigation entre tous les écrans
   - Vérifie les transitions

5. **05-test-quiz-complet.yaml**
   - Quiz complet avec annulation du score

6. **06-test-robustesse.yaml**
   - Tests de cas limites
   - Navigation rapide
   - Gestion d'erreurs

7. **07-test-performance.yaml**
   - Teste le chargement multiple de Pokémon
   - Vérifie la performance de l'app

### 🔧 Modifications du code

J'ai ajouté des `testTag` aux composants principaux pour que Maestro puisse les identifier :
- `pokemon-card` : La carte du Pokémon en mode révision
- `pokemon-image` : L'image du Pokémon
- `quiz-card` : La carte du quiz
- `quiz-image` : L'image du Pokémon à deviner
- `quiz-input` : Le champ de saisie de la réponse
- `quiz-submit-button` : Le bouton de validation
- `quiz-next-button` : Le bouton question suivante

## 🚀 Démarrage Rapide

### Option 1 : Script automatique

```bash
# Pour Android
./run-maestro-tests.sh android

# Pour iOS
./run-maestro-tests.sh ios

# Pour Web
./run-maestro-tests.sh web
```

### Option 2 : Manuel

1. **Préparer l'application**
   
   Pour Android :
   ```bash
   # Compiler l'app
   ./gradlew :composeApp:assembleDebug
   
   # Installer sur l'émulateur
   adb install composeApp/build/outputs/apk/debug/composeApp-debug.apk
   ```

2. **Lancer un test**
   ```bash
   # Test individuel
   maestro test .maestro/01-test-revision-pokemon.yaml
   
   # Tous les tests
   maestro test .maestro/
   
   # Avec analyse AI
   maestro test .maestro/ --analyze
   ```

3. **Mode interactif (recommandé pour déboguer)**
   ```bash
   maestro studio
   ```
   Puis ouvre un fichier .yaml pour voir l'exécution en temps réel

## 📱 Commandes Utiles

### Déboguer
```bash
# Mode studio (interface interactive)
maestro studio

# Enregistrer un nouveau test
maestro record .maestro/mon-nouveau-test.yaml

# Test avec logs détaillés
maestro test .maestro/01-test-revision-pokemon.yaml -v
```

### Rapports
```bash
# Générer un rapport JUnit
maestro test .maestro/ --format junit --output=test-results/

# Avec analyse AI
maestro test .maestro/01-test-revision-pokemon.yaml --analyze
```

### CI/CD
```bash
# Format adapté pour CI
maestro test .maestro/ --format junit --output=test-results/maestro/
```

## 🎯 Scénarios de test couverts

- ✅ Chargement de Pokémon aléatoires
- ✅ Basculement couleur / noir et blanc
- ✅ Quiz complet avec 10 questions
- ✅ Validation des réponses
- ✅ Enregistrement et affichage des scores
- ✅ Navigation entre écrans
- ✅ Gestion des états de chargement
- ✅ Tests de robustesse
- ✅ Tests de performance

## 🔍 Vérifier que tout fonctionne

```bash
# 1. Vérifier que Maestro est installé
maestro --version

# 2. Vérifier que l'app est installée (Android)
adb shell pm list packages | grep mastercyber

# 3. Lancer un test simple
maestro test .maestro/03-test-classement.yaml
```

## 📚 Prochaines étapes

1. **Lance les tests** avec la commande ci-dessus
2. **Explore Maestro Studio** pour voir l'UI de ton app
3. **Personnalise les workflows** selon tes besoins
4. **Ajoute de nouveaux scénarios** avec `maestro record`
5. **Intègre dans ton CI/CD** (GitHub Actions, GitLab CI, etc.)

## 🐛 Dépannage

**Problème : Maestro ne trouve pas l'app**
- Vérifie l'appId dans les fichiers YAML : `com.mastercyber.tp1`
- Vérifie que l'app est installée : `adb devices` puis `adb shell pm list packages`

**Problème : Timeouts**
- Augmente les timeouts dans les workflows
- Vérifie ta connexion internet (l'app charge des Pokémon)

**Problème : Éléments non trouvés**
- Utilise `maestro studio` pour inspecter l'UI
- Vérifie que les testTag sont présents

## 📖 Documentation

- README détaillé : `.maestro/README.md`
- Doc officielle : https://maestro.mobile.dev
- Exemples : https://maestro.mobile.dev/examples

Bon test ! 🎉

