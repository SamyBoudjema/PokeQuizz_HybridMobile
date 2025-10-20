# Usage: ./run-maestro-tests.sh [android|ios|web]

PLATFORM=${1:-android}
MAESTRO_DIR=".maestro"
BUNDLE_ID="com.mastercyber.tp1"

echo "🎮 PokéApp - Tests Maestro"
echo "=========================="
echo "Plateforme: $PLATFORM"
echo ""

if ! command -v maestro &> /dev/null; then
    echo "❌ Maestro n'est pas installé ou pas dans le PATH"
    echo "Ajoutez cette ligne à votre .zshrc:"
    echo 'export PATH="$PATH":"$HOME/.maestro/bin"'
    exit 1
fi

echo "✅ Maestro version: $(maestro --version 2>&1 | tail -1)"
echo ""

if [ "$PLATFORM" = "android" ]; then
    if command -v adb &> /dev/null; then
        APP_INSTALLED=$(adb shell pm list packages | grep com.mastercyber.tp1)
        if [ -z "$APP_INSTALLED" ]; then
            echo "⚠️  L'application n'est pas installée sur l'émulateur Android"
            echo "Voulez-vous la compiler et l'installer ? (o/n)"
            read -r response
            if [ "$response" = "o" ]; then
                echo "📦 Compilation de l'application..."
                ./gradlew :composeApp:assembleDebug
                echo "📲 Installation sur l'émulateur..."
                adb install -r composeApp/build/outputs/apk/debug/composeApp-debug.apk
            else
                echo "❌ Installation annulée"
                exit 1
            fi
        fi
    else
        echo "⚠️  adb non trouvé - vérification de l'installation ignorée"
        echo "   Assurez-vous que l'application est installée sur l'émulateur"
    fi
elif [ "$PLATFORM" = "ios" ]; then
    BOOTED_DEVICE=$(xcrun simctl list devices | grep "Booted")
    if [ -z "$BOOTED_DEVICE" ]; then
        echo "❌ Aucun simulateur iOS n'est démarré"
        exit 1
    fi
    echo "✅ Simulateur détecté: $BOOTED_DEVICE"
    
    APP_INSTALLED=$(xcrun simctl get_app_container booted "$BUNDLE_ID" 2>/dev/null)
    if [ -z "$APP_INSTALLED" ]; then
        echo "⚠️  L'application n'est pas installée sur le simulateur"
        echo "Veuillez lancer l'app depuis Xcode d'abord"
        exit 1
    fi
fi

echo ""
echo "🧪 Lancement des tests..."
echo ""

if [ "$PLATFORM" = "web" ]; then
    maestro test --platform web "$MAESTRO_DIR/"
elif [ "$PLATFORM" = "ios" ]; then
    maestro test --platform ios "$MAESTRO_DIR/"
else
    maestro test "$MAESTRO_DIR/"
fi

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Tous les tests sont passés avec succès !"
else
    echo ""
    echo "❌ Certains tests ont échoué"
    exit 1
fi