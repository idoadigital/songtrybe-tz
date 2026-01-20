#!/bin/bash

# Download Gradle wrapper jar
echo "Downloading Gradle wrapper jar..."

mkdir -p gradle/wrapper

# Download gradle-wrapper.jar from official Gradle distribution
curl -L https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar \
     -o gradle/wrapper/gradle-wrapper.jar

if [ -f gradle/wrapper/gradle-wrapper.jar ]; then
    echo "✅ Gradle wrapper jar downloaded successfully"
    ls -lh gradle/wrapper/gradle-wrapper.jar
else
    echo "❌ Failed to download gradle wrapper jar"
    exit 1
fi