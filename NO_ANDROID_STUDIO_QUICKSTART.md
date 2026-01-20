# 🚀 Run Songtrybe TV Without Android Studio

Build and run the app in 3 simple steps - no Android Studio needed!

## Prerequisites (One-time Setup)

### Install Java 17
```bash
# macOS
brew install openjdk@17

# Ubuntu/Debian
sudo apt install openjdk-17-jdk

# Windows
# Download from https://adoptium.net/
```

That's it! No Android Studio needed. ✅

## Step 1: Build the APK (2 minutes)

```bash
cd songtrybe-tv-app
./build-apk.sh
```

This creates: `app/build/outputs/apk/debug/app-debug.apk`

## Step 2: Install on Your TV

### Option A: Network Install (Easiest)
1. On your Android TV: Settings → Network → Note the IP address
2. On your TV: Settings → Developer Options → Enable Network Debugging
3. On your computer:
```bash
# Connect to TV (replace with your TV's IP)
adb connect 192.168.1.100:5555

# Install the app
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Option B: USB Drive
1. Copy `app-debug.apk` to a USB drive
2. Insert USB into your Android TV
3. Use a file manager app on TV to install

### Option C: Cloud Transfer
1. Upload `app-debug.apk` to Google Drive
2. Install "Send files to TV" app on your Android TV
3. Download and install the APK

## Step 3: Launch the App

Find "Songtrybe TV" in your Android TV apps and launch it!

---

## 🌐 Alternative: GitHub Auto-Build (No Local Build)

1. Push your code to GitHub
2. GitHub Actions will automatically build the APK
3. Download from Actions tab → Latest workflow → Artifacts

---

## 📱 Test on Android Phone (If No TV)

```bash
# Install on any Android device
adb install app/build/outputs/apk/debug/app-debug.apk
```

The UI is optimized for TV but will work on phones for testing.

---

## ⚡ Quick Commands Reference

```bash
# Build APK
cd songtrybe-tv-app && ./build-apk.sh

# Install via USB
adb install app/build/outputs/apk/debug/app-debug.apk

# Install via Network
adb connect TV_IP:5555
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.songtrybe.tv/.MainActivity

# View logs
adb logcat | grep songtrybe
```

---

## 🆘 Troubleshooting

### "gradlew: command not found"
```bash
chmod +x gradlew
```

### "SDK location not found"
The script will auto-download required dependencies.

### "Device not found"
1. Enable Developer Mode on TV (Settings → About → Click Build 7 times)
2. Enable USB/Network debugging
3. Try: `adb connect YOUR_TV_IP:5555`

### Build fails
Check you have Java 17:
```bash
java -version  # Should show version 17.x
```

---

## 🎉 That's It!

No Android Studio needed! Your APK is ready to install on any Android TV device.

Need help? The build script provides step-by-step guidance and error messages.