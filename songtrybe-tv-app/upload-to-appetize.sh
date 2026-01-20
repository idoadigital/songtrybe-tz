#!/bin/bash

# Upload APK to Appetize.io for instant browser testing
# Usage: ./upload-to-appetize.sh [path-to-apk]

APK_PATH=${1:-"app/build/outputs/apk/debug/app-debug.apk"}
API_KEY="tok_w73vyuwk3ugwuff2q3bqb3grli"

echo "🌐 Uploading APK to Appetize.io..."
echo "APK: $APK_PATH"

# Check if APK exists
if [ ! -f "$APK_PATH" ]; then
    echo "❌ APK not found at: $APK_PATH"
    echo "Build the APK first with: ./gradlew assembleDebug"
    exit 1
fi

# Get APK size
APK_SIZE=$(ls -lh "$APK_PATH" | awk '{print $5}')
echo "📦 APK Size: $APK_SIZE"

# Upload to Appetize.io
echo "📤 Uploading..."
RESPONSE=$(curl -s -X POST \
  -F "file=@$APK_PATH" \
  -F "platform=android" \
  -F "note=Songtrybe TV - Local build $(date)" \
  -H "Authorization: Bearer $API_KEY" \
  https://api.appetize.io/v1/apps)

echo ""
echo "Response: $RESPONSE"
echo ""

# Extract app URL from response
APP_URL=$(echo $RESPONSE | grep -o '"publicURL":"[^"]*"' | cut -d'"' -f4)

if [ ! -z "$APP_URL" ]; then
    echo "✅ Successfully uploaded to Appetize.io!"
    echo ""
    echo "🔗 Live Demo URL: $APP_URL"
    echo ""
    echo "📱 You can now test your app in any browser!"
    echo "   - No Android device needed"
    echo "   - Runs instantly in browser"
    echo "   - Test D-pad navigation with arrow keys"
    echo ""
    
    # Try to open the URL (macOS)
    if command -v open &> /dev/null; then
        read -p "Open demo in browser now? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            open "$APP_URL"
        fi
    fi
else
    echo "❌ Failed to upload to Appetize.io"
    echo "Response was: $RESPONSE"
    exit 1
fi