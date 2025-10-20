#!/bin/bash
# Script pour installer l'application et lancer les tests

echo "🔧 Installation et Tests PokéApp"
echo "================================="
echo ""

# Trouver adb
ADB_PATH=""
if command -v adb &> /dev/null; then
    ADB_PATH="adb"
elif [ -f "$HOME/Library/Android/sdk/platform-tools/adb" ]; then
    ADB_PATH="$HOME/Library/Android/sdk/platform-tools/adb"
else
    echo "❌ adb non trouvé. Veuillez installer Android SDK Platform Tools"
    exit 1
fi

echo "✅ adb trouvé: $ADB_PATH"

# Compiler l'application
echo ""
echo "📦 Compilation de l'application..."
./gradlew :composeApp:assembleDebug

if [ $? -ne 0 ]; then
    echo "❌ Échec de la compilation"
    exit 1
fi

# Installer l'APK
echo ""
echo "📲 Installation de l'APK..."
APK_PATH="composeApp/build/outputs/apk/debug/composeApp-debug.apk"

if [ ! -f "$APK_PATH" ]; then
    echo "❌ APK non trouvé: $APK_PATH"
    exit 1
fi

$ADB_PATH install -r "$APK_PATH"

if [ $? -ne 0 ]; then
    echo "❌ Échec de l'installation"
    exit 1
fi

echo "✅ Application installée avec succès"

# Lancer l'application
echo ""
echo "🚀 Lancement de l'application..."
$ADB_PATH shell am start -n com.mastercyber.tp1/com.mastercyber.tp1.MainActivity

sleep 3

# Lancer les tests
echo ""
echo "🧪 Lancement des tests Maestro..."
echo ""
./run-maestro-tests.sh


