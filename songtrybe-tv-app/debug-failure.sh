#!/bin/bash

echo "🔍 Debugging the specific build failure..."
echo "============================================"

cd songtrybe-tv-app

# Test each build task individually to isolate the failure
echo "Step 1: Testing Gradle wrapper..."
./gradlew --version --no-daemon

echo ""
echo "Step 2: Testing dependencies resolution..."
./gradlew app:dependencies --no-daemon --configuration debugCompileClasspath | head -20

echo ""
echo "Step 3: Testing resource processing..."
./gradlew app:processDebugResources --no-daemon --stacktrace

echo ""
echo "Step 4: Testing compilation..."
./gradlew app:compileDebugKotlin --no-daemon --stacktrace

echo ""
echo "Step 5: Testing APK assembly..."
./gradlew app:assembleDebug --no-daemon --stacktrace --info | tail -50

echo ""
echo "🎯 If any step fails above, that's our culprit!"