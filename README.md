# 🎮 PokéQuizz - Application Mobile Hybride

Une application de quiz Pokémon interactive développée avec Apache Cordova/PhoneGap où les utilisateurs devinent les noms des Pokémon à partir d'images en noir et blanc.

## 📱 Aperçu

PokéQuizz est une application mobile hybride qui propose :
- Quiz de 10 questions avec images Pokémon transformées en noir et blanc
- Système de scoring basé sur la précision et le temps
- Sauvegarde locale des scores avec classement
- Interface utilisateur moderne et responsive
- Architecture modulaire optimisée

## ✨ Fonctionnalités

- 🖼️ **Images dynamiques** : Conversion en temps réel des sprites Pokémon en noir et blanc
- ⏱️ **Timer interactif** : Chronomètre pour chaque question
- 💾 **Sauvegarde locale** : Persistance des scores via Cordova File API
- 🏆 **Système de classement** : Leaderboard avec statistiques détaillées
- 📱 **Design responsive** : Interface adaptée à tous les écrans mobiles
- 🎯 **Navigation fluide** : Système d'écrans avec transitions

## 🛠️ Technologies Utilisées

- **Framework** : Apache Cordova/PhoneGap
- **Frontend** : HTML5, CSS3, JavaScript ES6+
- **API** : Tyradex Pokemon API (https://tyradex.vercel.app/)
- **Stockage** : Cordova File API + localStorage (fallback)
- **Canvas** : Manipulation d'images en temps réel
- **CSS Grid/Flexbox** : Layout responsive moderne

## 📁 Architecture du Projet

```
pokequizz/
├── config.xml                 # Configuration Cordova
├── package.json               # Dépendances npm
├── platforms/                 # Plateformes mobiles générées
├── plugins/                   # Plugins Cordova installés
└── www/                       # Code source de l'application
    ├── index.html             # Point d'entrée HTML
    ├── css/
    │   └── index.css          # Styles CSS optimisés
    ├── js/                    # Modules JavaScript
    │   ├── app.js            # Orchestrateur principal (120 lignes)
    │   ├── api.js            # Gestion API et images (60 lignes)
    │   ├── storage.js        # Persistance des données (80 lignes)
    │   ├── quiz.js           # Logique du quiz (90 lignes)
    │   ├── leaderboard.js    # Système de classement (70 lignes)
    │   └── ui-manager.js     # Gestion de l'interface (90 lignes)
    └── img/
        └── logo.png          # Logo de l'application
```

### 🏗️ Architecture Modulaire

- **`app.js`** : Point d'entrée et coordination générale
- **`api.js`** : Requêtes API et traitement des images
- **`storage.js`** : Gestion de la persistance locale
- **`quiz.js`** : Logique métier du quiz et scoring
- **`leaderboard.js`** : Classements et statistiques
- **`ui-manager.js`** : Manipulation du DOM et navigation

## 🚀 Installation et Développement

### Prérequis
```bash
# Installer Node.js et npm
npm install -g cordova

# Pour iOS (macOS uniquement)
xcode-select --install

# Pour Android
# Installer Android Studio et configurer ANDROID_HOME
```

### Installation du Projet
```bash
# Cloner le repository
git clone [URL_DU_REPO]
cd pokequizz

# Installer les dépendances
npm install

# Ajouter les plateformes
cordova platform add ios
cordova platform add android

# Installer les plugins requis
cordova plugin add cordova-plugin-file
cordova plugin add cordova-plugin-whitelist
```

### Développement et Test
```bash
# Test dans le navigateur
cordova run browser

# Test sur simulateur iOS
cordova run ios

# Test sur émulateur Android
cordova run android

# Build pour production
cordova build --release
```

## 🎮 Utilisation

1. **Écran d'accueil** : Choisir entre "Nouveau Quiz" ou "Classement"
2. **Quiz** : Deviner 10 Pokémon à partir d'images en noir et blanc
3. **Scoring** : Système basé sur la précision et le temps de réponse
4. **Sauvegarde** : Enregistrer son score avec un nom personnalisé
5. **Classement** : Voir les meilleurs scores et statistiques globales

## 🔧 Configuration

### Plugins Cordova Requis
- `cordova-plugin-file` : Gestion des fichiers locaux
- `cordova-plugin-whitelist` : Sécurité et requêtes externes

### API Utilisée
- **Tyradex API** : `https://tyradex.vercel.app/api/v1/pokemon/[ID]`
- Pokémon ID aléatoire entre 1 et 1010
- Images des sprites converties en noir et blanc via Canvas HTML5

## 📊 Fonctionnement du Quiz

1. **Génération** : ID Pokémon aléatoire (1-1010)
2. **Récupération** : Données via API Tyradex
3. **Conversion** : Image couleur → noir et blanc (Canvas)
4. **Scoring** : Points basés sur rapidité + précision
5. **Sauvegarde** : Score local persistant

## 🏆 Système de Scoring

- **Base** : 100 points par bonne réponse
- **Bonus temps** : +50 points si < 5 secondes
- **Bonus vitesse** : +25 points si < 10 secondes
- **Total maximum** : 1500 points (10 × 150)

## 🔄 Persistance des Données

1. **Priorité** : Cordova File API (stockage natif)
2. **Fallback** : localStorage (navigateur)
3. **Format** : JSON avec horodatage
4. **Capacité** : Illimitée (selon espace disponible)

## 📱 Compatibilité

- **iOS** : 10.0+
- **Android** : 7.0+ (API 24+)
- **Navigateurs** : Chrome, Safari, Firefox (test uniquement)

## 🎨 Design Patterns

- **Module Pattern** : Séparation des responsabilités
- **Observer Pattern** : Communication entre modules
- **Factory Pattern** : Génération des éléments UI
- **Single Screen Navigation** : Affichage d'un écran à la fois

## 📈 Performances

- **Taille modules** : < 100 lignes chacun
- **Images** : Optimisation Canvas sans stockage
- **API** : Cache temporaire des données récentes
- **DOM** : Manipulation minimale et ciblée

## 🐛 Dépannage

### Problèmes Courants
```bash
# Plugin manquant
cordova plugin add cordova-plugin-file

# Permissions iOS
# Vérifier config.xml pour les autorisations réseau

# Erreur Android build
# Mettre à jour Android SDK et build-tools
```

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/amelioration`)
3. Commit les changements (`git commit -m 'Ajouter amelioration'`)
4. Push vers la branche (`git push origin feature/amelioration`)
5. Créer une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👨‍💻 Auteur

Développé avec ❤️ pour le Master 2 - Développement Mobile Hybride

---

*PokéQuizz - "Attrapez-les tous... si vous les reconnaissez !"* 🎯