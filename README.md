# 🎮 PokéQuizz - Kotlin Multiplatform

Application multiplateforme (Android, iOS, Web) de quiz Pokémon développée avec Kotlin Multiplatform et Compose Multiplatform.

## 📱 Fonctionnalités

### 🔍 Mode Révision
- Affichage aléatoire de Pokémon avec images couleur
- Détails complets : nom, types, statistiques
- Navigation entre Pokémon

### 🏆 Mode Quiz
- 10 questions à choix multiples
- Images en noir et blanc
- Système de score et classement
- Sauvegarde persistante (Android)

### 📊 Classement
- Top des meilleurs scores
- Nom, score et horodatage

## 🚀 Technologies

- **Kotlin Multiplatform** - Code partagé multi-plateformes
- **Compose Multiplatform** - UI déclarative
- **Ktor** - Client HTTP
- **Kotlinx Serialization** - JSON
- **Material 3** - Design moderne
- **SharedPreferences** - Persistance Android

## 🏗️ Architecture

```
composeApp/src/
├── commonMain/          # Code partagé
│   ├── App.kt           # UI principale
│   ├── Greeting.kt      # Logique métier
│   ├── models/          # Modèles
│   └── data/            # Données
├── androidMain/         # Android
├── iosMain/             # iOS
└── jsMain/              # Web
```

## � Installation

### Prérequis
- JDK 17+
- Android Studio Hedgehog+
- Xcode 14+ (iOS, macOS)
- Node.js 16+ (Web)

### 🤖 Android
```shell
./gradlew :composeApp:assembleDebug
```

### 🍎 iOS
Ouvrir `iosApp/iosApp.xcodeproj` dans Xcode et lancer.

### 🌐 Web
```shell
./gradlew :composeApp:jsBrowserDevelopmentRun
# Accessible sur http://localhost:8080
```

## � API

**Tyradex API**: `https://tyradex.vercel.app/api/v1/pokemon/`
- Données Pokémon en français
- Images haute qualité
- Accès public

## 📝 Améliorations

- ✅ Gestion robuste des erreurs réseau
- ✅ Messages d'erreur utilisateur
- ✅ Persistance des scores (Android)
- ✅ Traitement d'images multiplateforme

## 👤 Auteur

**Samy Boudjema**  
Master Cybersécurité - TP2

---

*Développé avec Kotlin Multiplatform*