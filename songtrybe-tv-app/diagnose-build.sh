#!/bin/bash

# Build diagnostics script for Songtrybe TV
echo "🔍 Diagnosing build issues for Songtrybe TV..."
echo "================================================"

# Check Java version
echo "☕ Java Version:"
java -version
echo ""

# Check if gradlew exists and is executable
echo "🛠️ Gradle Wrapper:"
if [ -f gradlew ]; then
    echo "✅ gradlew found"
    if [ -x gradlew ]; then
        echo "✅ gradlew is executable"
    else
        echo "❌ gradlew is not executable"
        chmod +x gradlew
        echo "✅ Made gradlew executable"
    fi
else
    echo "❌ gradlew not found"
fi

# Check gradle wrapper jar
if [ -f gradle/wrapper/gradle-wrapper.jar ]; then
    echo "✅ gradle-wrapper.jar found"
else
    echo "❌ gradle-wrapper.jar missing"
    echo "Downloading..."
    ./download-gradle-wrapper.sh
fi

# Check google-services.json
if [ -f app/google-services.json ]; then
    echo "✅ google-services.json found"
else
    echo "❌ google-services.json missing"
    echo "This file is required for Firebase integration"
fi

# Check required directories
echo ""
echo "📁 Required directories:"
required_dirs=(
    "app/src/main/java/com/songtrybe/tv"
    "app/src/main/res/values"
    "app/src/main/res/drawable"
    "app/src/main/res/mipmap-hdpi"
)

for dir in "${required_dirs[@]}"; do
    if [ -d "$dir" ]; then
        echo "✅ $dir exists"
    else
        echo "❌ $dir missing"
        mkdir -p "$dir"
        echo "✅ Created $dir"
    fi
done

echo ""
echo "🔍 Attempting to check dependencies..."
if [ -x gradlew ]; then
    echo "Running: ./gradlew dependencies --configuration debugRuntimeClasspath | head -20"
    ./gradlew dependencies --configuration debugRuntimeClasspath | head -20
    
    echo ""
    echo "🧪 Running clean build..."
    ./gradlew clean assembleDebug --info --stacktrace
else
    echo "❌ Cannot run gradle checks - gradlew not available"
fi

echo ""
echo "🎯 Diagnosis complete!"
echo "If build still fails, check the error messages above."