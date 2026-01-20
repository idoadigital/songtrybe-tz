#!/bin/bash

# Build script for Songtrybe TV without Android Studio
# This script builds the APK using command line tools

echo "🚀 Building Songtrybe TV APK..."
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed${NC}"
    echo "Please install Java 17 or higher:"
    echo "  macOS: brew install openjdk@17"
    echo "  Linux: sudo apt install openjdk-17-jdk"
    echo "  Windows: Download from https://adoptium.net/"
    exit 1
fi

echo -e "${GREEN}✓ Java found${NC}"

# Check if ANDROID_HOME is set (optional)
if [ -z "$ANDROID_HOME" ]; then
    echo -e "${YELLOW}⚠ ANDROID_HOME not set, will download dependencies${NC}"
fi

# Make gradlew executable
chmod +x gradlew

# Clean previous builds
echo ""
echo "Cleaning previous builds..."
./gradlew clean

# Build Debug APK
echo ""
echo "Building Debug APK..."
if ./gradlew assembleDebug; then
    echo -e "${GREEN}✅ Debug APK built successfully!${NC}"
    echo ""
    echo "📦 APK location:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    
    # Get APK size
    APK_SIZE=$(ls -lh app/build/outputs/apk/debug/app-debug.apk | awk '{print $5}')
    echo "   Size: $APK_SIZE"
else
    echo -e "${RED}❌ Build failed${NC}"
    echo "Check the error messages above"
    exit 1
fi

echo ""
echo "================================================================"
echo ""
echo "📱 INSTALLATION OPTIONS:"
echo ""
echo "1. USB/Network Install (if ADB is available):"
echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
echo ""
echo "2. Copy to USB drive and install on TV"
echo ""
echo "3. Upload to Google Drive and download on TV"
echo ""
echo "4. Use Send Files to TV app"
echo ""
echo "================================================================"

# Optional: Install if device is connected
if command -v adb &> /dev/null; then
    echo ""
    echo "Checking for connected devices..."
    DEVICES=$(adb devices | grep -v "List" | grep device)
    
    if [ ! -z "$DEVICES" ]; then
        echo -e "${GREEN}✓ Device found${NC}"
        read -p "Install on connected device? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            adb install -r app/build/outputs/apk/debug/app-debug.apk
            echo -e "${GREEN}✅ App installed!${NC}"
            
            # Launch app
            read -p "Launch app? (y/n): " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                adb shell am start -n com.songtrybe.tv/.MainActivity
                echo -e "${GREEN}✅ App launched!${NC}"
            fi
        fi
    else
        echo "No devices connected. Connect your Android TV via USB or network:"
        echo "  Network: adb connect TV_IP_ADDRESS:5555"
    fi
fi