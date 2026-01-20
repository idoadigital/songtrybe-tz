# Running Songtrybe TV Without Android Studio

Several options to build and run the app without installing Android Studio:

## Option 1: Command Line Build (Recommended)

### Prerequisites
Install only the Android SDK command line tools (much smaller than Android Studio):

#### On macOS:
```bash
# Install via Homebrew
brew install --cask android-commandlinetools
```

#### On Windows/Linux:
1. Download [Android SDK Command-line Tools](https://developer.android.com/studio#command-tools)
2. Extract to a folder (e.g., `~/android-sdk`)
3. Add to PATH

### Setup SDK
```bash
# Set up Android SDK location
export ANDROID_HOME=~/Library/Android/sdk  # macOS
# export ANDROID_HOME=~/android-sdk        # Linux/Windows

# Install required SDK packages
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Accept licenses
yes | sdkmanager --licenses
```

### Build the APK
```bash
cd songtrybe-tv-app

# Make gradlew executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# The APK will be at:
# app/build/outputs/apk/debug/app-debug.apk
```

### Install on Device/Emulator
```bash
# For physical Android TV (connected via USB)
adb install app/build/outputs/apk/debug/app-debug.apk

# For Android TV emulator
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Option 2: GitHub Actions CI/CD (Easiest - No Local Setup)

Create a GitHub repository and use Actions to build automatically:

### `.github/workflows/build.yml`
```yaml
name: Build Android TV App

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Setup Android SDK
      uses: android-actions/setup-android@v2
    
    - name: Build APK
      run: |
        cd songtrybe-tv-app
        chmod +x gradlew
        ./gradlew assembleDebug
    
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: songtrybe-tv-app/app/build/outputs/apk/debug/app-debug.apk
```

Push your code to GitHub and the APK will be built automatically!

## Option 3: Use Online Build Services

### AppCenter (Microsoft)
1. Sign up at [appcenter.ms](https://appcenter.ms)
2. Connect your GitHub repo
3. Configure Android build
4. Download APK after build

### Bitrise
1. Sign up at [bitrise.io](https://www.bitrise.io)
2. Add Android TV project
3. Run build workflow
4. Download APK

## Option 4: Docker Build Environment

Use Docker to build without installing Android tools:

### `Dockerfile`
```dockerfile
FROM openjdk:17-jdk-slim

# Install Android SDK
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
RUN unzip commandlinetools-linux-9477386_latest.zip -d /android-sdk
ENV ANDROID_HOME=/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/bin

# Accept licenses
RUN yes | sdkmanager --licenses --sdk_root=$ANDROID_HOME
RUN sdkmanager --sdk_root=$ANDROID_HOME "platforms;android-34" "build-tools;34.0.0"

WORKDIR /app
COPY songtrybe-tv-app .
RUN chmod +x gradlew
RUN ./gradlew assembleDebug
```

Build with Docker:
```bash
docker build -t songtrybe-tv-build .
docker run -v $(pwd)/output:/output songtrybe-tv-build \
  cp app/build/outputs/apk/debug/app-debug.apk /output/
```

## Option 5: Use a Cloud IDE

### Gitpod
1. Add `.gitpod.yml` to your repo:
```yaml
tasks:
  - init: |
      sdk install java 17.0.9-tem
      sdk install gradle
      cd songtrybe-tv-app
      ./gradlew build
```

2. Open in Gitpod: `https://gitpod.io/#https://github.com/YOUR_REPO`

### GitHub Codespaces
1. Create a codespace in your repo
2. Install Java extension pack
3. Build using terminal

## Installing APK on Android TV

### Physical Android TV Device:
1. Enable Developer Options (Settings → About → Click Build 7 times)
2. Enable USB Debugging
3. Connect via USB or ADB over network:
```bash
# Connect over network (TV and computer on same WiFi)
adb connect TV_IP_ADDRESS:5555
adb install app-debug.apk
```

### Using a USB Drive:
1. Copy APK to USB drive
2. Install a file manager on Android TV (e.g., FX File Explorer)
3. Navigate to USB and install APK

### Upload to Google Drive:
1. Upload APK to Google Drive
2. Install "Send Files to TV" app on Android TV
3. Download and install APK

## Testing Without Android TV Device

### Option 1: Android TV Emulator (Without Studio)
```bash
# Install emulator
sdkmanager "emulator" "system-images;android-34;google_apis;x86_64"

# Create AVD
avdmanager create avd -n AndroidTV -k "system-images;android-34;google_apis;x86_64" -d "tv_1080p"

# Run emulator
emulator -avd AndroidTV
```

### Option 2: Use Online Android Emulators
- [Appetize.io](https://appetize.io) - Upload APK and test online
- [BrowserStack](https://www.browserstack.com) - Real device testing

### Option 3: Test on Android Phone/Tablet
While not ideal for TV UI, you can test basic functionality:
```bash
adb install app-debug.apk
```

## Pre-built APK Option

I can help you build the APK if you:
1. Commit your code to GitHub
2. Use GitHub Actions (Option 2 above)
3. Download the built APK from Actions artifacts

## Quick Cloud Build Script

Save this as `cloud-build.sh`:
```bash
#!/bin/bash

# Use Google Cloud Build (requires Google Cloud account)
gcloud builds submit \
  --tag gcr.io/PROJECT_ID/songtrybe-tv \
  --timeout=20m \
  songtrybe-tv-app/

# Download built APK
gsutil cp gs://PROJECT_ID_cloudbuild/app-debug.apk .
```

## Recommended Approach

1. **Fastest**: Use GitHub Actions (Option 2) - No local setup needed
2. **Most Control**: Command line build (Option 1) - Minimal installation
3. **For Testing**: Use online emulators or test on Android phone

Would you like me to set up GitHub Actions for automatic builds?