# 🎮 PokéApp - Kotlin Multiplatform

Application multiplateforme (Android, iOS, Web) pour découvrir et réviser les Pokémon, développée avec Kotlin Multiplatform et Compose Multiplatform.

## 📱 Fonctionnalités

### Mode Révision
- Affichage aléatoire de Pokémon avec toutes leurs informations
- Visualisation en couleur ou noir et blanc
- Détails complets : nom, numéro, types, statistiques
- Bouton pour charger un nouveau Pokémon aléatoire

### Mode Quiz 🎯
- Quiz interactif pour deviner le nom des Pokémon
- Affichage de l'image du Pokémon
- Saisie du nom et validation
- Feedback visuel (vert pour succès, rouge pour échec)
- Enchaînement automatique avec le bouton "Suivant"

## 🏗️ Architecture du Projet

```
composeApp/src/commonMain/kotlin/com/mastercyber/tp1/
├── App.kt                          # Point d'entrée principal
├── Greeting.kt                     # Service API Pokémon
├── models/                         # Modèles de données
│   └── Pokemon.kt
├── ui/
│   ├── screens/                    # Écrans de l'application
│   │   ├── PokemonScreen.kt        # Mode révision
│   │   └── QuizScreen.kt           # Mode quiz
│   └── components/                 # Composants réutilisables
│       ├── LoadingState.kt
│       ├── PokemonCard.kt
│       └── QuizResultCard.kt
└── utils/                          # Utilitaires
```

## 🚀 Technologies

- **Kotlin Multiplatform** - Partage de code entre plateformes
- **Compose Multiplatform** - UI déclarative multiplateforme
- **Ktor Client** - Requêtes HTTP
- **Kotlinx Serialization** - Sérialisation JSON
- **Material 3** - Design moderne

## 🔧 Build et Exécution

### Android Application

```shell
./gradlew :composeApp:assembleDebug
```

### Web Application (Wasm)

```shell
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### Web Application (JS)

```shell
./gradlew :composeApp:jsBrowserDevelopmentRun
```

### iOS Application

Ouvrez `iosApp/iosApp.xcodeproj` dans Xcode et lancez l'application.

## 📚 API Utilisée

- [Tyradex API](https://tyradex.vercel.app/) - API Pokémon en français

## 👨‍💻 Développement

### Structure modulaire
Le projet suit une architecture propre et modulaire :
- Séparation des écrans et des composants
- Composants réutilisables
- Code maintenable et testable

### Prérequis
- JDK 17 ou supérieur
- Android Studio ou IntelliJ IDEA
- Xcode (pour iOS)
- Node.js (pour le target Web)

## 📝 Notes

Projet réalisé dans le cadre du Master 2 Hybrid Mobile.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).