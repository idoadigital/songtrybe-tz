#!/bin/bash

echo "🔧 Emergency Build Fix for Songtrybe TV"
echo "======================================"

# Remove problematic files
echo "Cleaning up problematic files..."
rm -rf .gradle/
rm -rf app/build/
rm -rf build/

# Ensure gradlew is executable
chmod +x gradlew

# Download gradle wrapper if missing
if [ ! -f gradle/wrapper/gradle-wrapper.jar ]; then
    echo "Downloading gradle wrapper..."
    ./download-gradle-wrapper.sh
fi

echo "🧪 Testing minimal build..."
echo "Running: ./gradlew clean"
./gradlew clean

echo ""
echo "Running: ./gradlew assembleDebug --stacktrace --info"
./gradlew assembleDebug --stacktrace --info

echo ""
echo "🎯 Build attempt complete!"
echo "If this fails, the issue is with the environment or basic Android setup."