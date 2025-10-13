# TP2 - Kotlin Multiplatform - Application Pokémon

## 📋 Description du projet

Application multiplateforme développée avec Kotlin Multiplatform et Compose Multiplatform permettant d'afficher des informations sur des Pokémon aléatoires via l'API Tyradex.

## ✅ Fonctionnalités implémentées

### Partie 1 : Configuration du projet
- ✅ Projet initialisé avec Kotlin Multiplatform
- ✅ Support Android, iOS, et JS (Web)
- ✅ Configuration Gradle avec les dépendances nécessaires

### Partie 2 : Intégration API et affichage basique
- ✅ Configuration de Ktor (client HTTP Kotlin)
- ✅ Ajout de kotlinx-serialization pour la désérialisation JSON
- ✅ Création des modèles de données (Pokemon, Name, Sprites, Type, Stats, Resistance)
- ✅ Appel API vers https://tyradex.vercel.app/api/v1/pokemon/[id]
- ✅ Récupération et affichage du nom d'un Pokémon aléatoire
- ✅ Permissions Internet ajoutées dans AndroidManifest.xml

### Partie 3 : Fonctionnalités avancées
- ✅ Téléchargement de l'image du Pokémon (sprite regular)
- ✅ Conversion de l'image en ByteArray
- ✅ Affichage de l'image dans l'interface
- ✅ **Conversion en noir et blanc** (implémentation Android avec ColorMatrix)
- ✅ Bouton pour basculer entre couleur et noir et blanc
- ✅ Affichage des statistiques du Pokémon (PV, Attaque, Défense, etc.)
- ✅ Affichage des types du Pokémon
- ✅ Bouton pour charger un nouveau Pokémon aléatoire

## 🏗️ Architecture du projet

### Code partagé (commonMain)
- `Greeting.kt` : Classe principale avec les appels API
- `App.kt` : Interface utilisateur Compose Multiplatform
- `models/` : Modèles de données (Pokemon, Name, Sprites, Type, Stats, Resistance)

### Code spécifique Android (androidMain)
- `ImageConverter.android.kt` : Conversion des images avec support du noir et blanc
- `AndroidManifest.xml` : Permissions Internet

### Code spécifique iOS (iosMain)
- `ImageConverter.ios.kt` : Conversion des images pour iOS

### Code spécifique JS (jsMain)
- `ImageConverter.js.kt` : Conversion des images pour le Web

## 🔧 Technologies utilisées

- **Kotlin Multiplatform** : Partage du code entre plateformes
- **Compose Multiplatform** : UI partagée
- **Ktor 2.3.7** : Client HTTP multiplateforme
- **kotlinx.serialization** : Sérialisation/désérialisation JSON
- **Material3** : Design system pour l'interface

## 📱 Comment exécuter le projet

### Android
```bash
./gradlew :composeApp:assembleDebug
```
Ou ouvrir le projet dans Android Studio et lancer sur un émulateur/appareil Android.

### iOS
Ouvrir `iosApp/iosApp.xcodeproj` dans Xcode et lancer sur simulateur/appareil iOS.

## 🎨 Fonctionnalités de l'interface

1. **Bouton principal** : Affiche/masque le contenu
2. **Chargement** : Indicateur de progression pendant la récupération des données
3. **Carte Pokémon** :
   - Nom en français
   - Numéro Pokédex
   - Image (couleur ou noir et blanc)
   - Bouton pour basculer entre couleur et N&B
   - Types du Pokémon avec design en badges
   - Statistiques complètes (PV, Attaque, Défense, Att. Spé, Déf. Spé, Vitesse)
   - Bouton pour charger un autre Pokémon

## 📝 Notes techniques

- **WasmJS désactivé** : Ktor 2.3.7 ne supporte pas complètement WasmJS. Pour l'activer, mettre à jour vers Ktor 3.x
- **Conversion N&B Android** : Utilise ColorMatrix avec saturation à 0 pour convertir en niveaux de gris
- **Gestion d'erreurs** : Les erreurs de réseau sont gérées avec des try-catch
- **Architecture propre** : Séparation claire entre le code commun et le code spécifique à chaque plateforme

## 🐛 Résolution de problèmes

Si vous rencontrez des erreurs de compilation :
1. Synchroniser Gradle : `./gradlew --refresh-dependencies`
2. Nettoyer le build : `./gradlew clean`
3. Vérifier que les permissions Internet sont dans AndroidManifest.xml

## 🎓 Objectifs du TP atteints

- ✅ Initialisation du projet KMP
- ✅ Configuration de Git et création de branches
- ✅ Configuration de Ktor et des dépendances
- ✅ Appels API asynchrones avec coroutines
- ✅ Affichage d'images téléchargées
- ✅ Conversion d'images en noir et blanc
- ✅ Interface utilisateur complète et interactive
- ✅ Code partagé entre plateformes avec implémentations spécifiques

## 🚀 Améliorations possibles

- Ajouter une base de données locale pour sauvegarder les Pokémon favoris
- Implémenter un système de recherche par nom ou ID
- Ajouter plus d'effets visuels (sépia, flou, etc.)
- Implémenter le support complet de WasmJS avec Ktor 3.x
- Ajouter des animations entre les changements de Pokémon
- Implémenter un système de cache pour les images

---

**Auteur** : Projet réalisé dans le cadre du TP2 - Kotlin Multiplatform
**Date** : 2025

