# Tests Maestro pour PokéApp

## Installation de Maestro

Maestro est déjà installé sur votre système. Si vous devez le réinstaller :

```bash
curl -Ls "https://get.maestro.mobile.dev" | bash
```

Puis ajoutez à votre PATH :
```bash
export PATH="$PATH":"$HOME/.maestro/bin"
```

## Structure des tests

Les workflows de test se trouvent dans le dossier `.maestro/` :

- **01-test-revision-pokemon.yaml** : Test du mode révision Pokémon
  - Ouvre le mode révision
  - Vérifie le chargement du Pokémon
  - Teste le basculement noir & blanc
  - Teste le rechargement d'un nouveau Pokémon

- **02-test-quiz-pokemon.yaml** : Test du quiz complet
  - Démarre un quiz
  - Teste les réponses incorrectes et correctes
  - Parcourt les 10 questions
  - Enregistre le score

- **03-test-classement.yaml** : Test de l'écran de classement
  - Ouvre et ferme le classement

- **04-test-navigation-complete.yaml** : Test de navigation entre écrans
  - Navigue entre tous les écrans
  - Vérifie que les basculements fonctionnent correctement

- **05-test-quiz-complet.yaml** : Test d'un quiz complet avec annulation
  - Répond aux 10 questions
  - Teste l'annulation de l'enregistrement du score

## Exécuter les tests

### Pré-requis
Votre application doit être lancée sur un émulateur Android ou un simulateur iOS.

### Pour Android

1. Lancez votre émulateur Android
2. Compilez et installez l'application :
   ```bash
   ./gradlew :composeApp:assembleDebug
   adb install composeApp/build/outputs/apk/debug/composeApp-debug.apk
   ```

3. Exécutez un test spécifique :
   ```bash
   maestro test .maestro/01-test-revision-pokemon.yaml
   ```

4. Exécutez tous les tests :
   ```bash
   maestro test .maestro/
   ```

### Pour iOS

1. Lancez votre simulateur iOS
2. Compilez et installez l'application depuis Xcode
3. Exécutez les tests avec la même commande :
   ```bash
   maestro test .maestro/01-test-revision-pokemon.yaml
   ```

### Pour le Web

Maestro supporte également les applications web via navigateur :
```bash
maestro test --platform web .maestro/
```

## Commandes utiles

### Test avec analyse AI
```bash
maestro test .maestro/01-test-revision-pokemon.yaml --analyze
```

### Test avec rapport détaillé
```bash
maestro test .maestro/ --format junit --output=test-results/
```

### Mode interactif (Studio)
```bash
maestro studio
```
Puis ouvrez un fichier de test pour le déboguer de manière interactive.

### Enregistrer un nouveau workflow
```bash
maestro record .maestro/nouveau-test.yaml
```

## Dépannage

### L'application ne se lance pas
- Vérifiez que l'`appId` dans les fichiers YAML correspond à votre package : `com.mastercyber.tp1`
- Vérifiez que l'application est bien installée : `adb shell pm list packages | grep mastercyber`

### Les éléments ne sont pas trouvés
- Utilisez `maestro studio` pour inspecter les éléments de l'UI
- Vérifiez que les `testTag` sont bien présents dans le code Compose

### Timeouts
- Augmentez le `timeout` dans les commandes `extendedWaitUntil`
- Vérifiez votre connexion réseau (l'app charge des Pokémon depuis une API)

## Conseils

- Exécutez les tests dans l'ordre pour une meilleure cohérence
- Les tests du quiz peuvent prendre du temps car ils chargent 10 Pokémon
- Assurez-vous que votre appareil a une connexion internet pour charger les images
- Pour CI/CD, utilisez Maestro Cloud : https://cloud.mobile.dev

## Ressources

- Documentation officielle : https://maestro.mobile.dev
- Exemples : https://maestro.mobile.dev/examples
- Discord communautaire : https://discord.gg/mobile-dev-tools

