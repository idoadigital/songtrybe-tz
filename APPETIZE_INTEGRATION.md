# 🌐 Appetize.io Integration - Test Your App in Any Browser

Your GitHub Actions workflow now automatically uploads the APK to Appetize.io, allowing you to test the Android TV app directly in a web browser!

## ✨ What's New

Every time you push code, the workflow will:
1. ✅ Build your APK
2. 🌐 Upload to Appetize.io automatically
3. 📝 Provide a live demo link in the build summary
4. 📥 Still provide downloadable APK files

## 🎯 Benefits

- **No Device Needed**: Test without Android TV or emulator
- **Instant Access**: Demo link available immediately after build
- **Easy Sharing**: Send demo link to others for feedback
- **D-pad Testing**: Use arrow keys to simulate remote control
- **Always Updated**: Fresh demo with every code push

## 🚀 How to Use

### Automatic (GitHub Actions)

1. Push your code:
```bash
git add .
git commit -m "Your changes"
git push
```

2. Go to Actions → Latest build
3. Look for "📺 Songtrybe TV Build Report"
4. Click the **"Launch Songtrybe TV Demo"** link

### Manual Upload (Local Testing)

```bash
# Build APK locally
cd songtrybe-tv-app
./gradlew assembleDebug

# Upload to Appetize.io
./upload-to-appetize.sh
```

## 🎮 Testing in Browser

Once your app is uploaded:

### Navigation Controls:
- **Arrow Keys**: D-pad navigation (↑ ↓ ← →)
- **Enter/Return**: Select button
- **Escape**: Back button
- **Click**: Touch simulation (though app is D-pad optimized)

### What You Can Test:
- ✅ Navigate between home rails
- ✅ Browse musicians and details
- ✅ UI responsiveness and focus states
- ✅ Overall app flow and layout
- ❌ YouTube playback (launches in separate window)

## 🔗 Demo URLs

Every build creates a unique demo URL like:
`https://appetize.io/app/[unique-id]`

Demo sessions include:
- **Duration**: 100 seconds free (reloadable)
- **Device**: Simulated Android TV interface
- **Performance**: Real Android environment in cloud

## 📊 Build Reports

Your GitHub Actions now show:

```markdown
# 📺 Songtrybe TV Build Report

✅ **Debug APK:** Built successfully (Size: 15.2M)

## 📥 Download Options

### APK Files
Check the Artifacts section below to download APK files

### 🌐 Live Demo
**Try the app instantly in your browser:**

🔗 **[Launch Songtrybe TV Demo](https://appetize.io/app/xyz123)**

*No download required - runs directly in your browser!*

## 📱 Installation Instructions
1. Download the APK from Artifacts (above)
2. Transfer to your Android TV via USB or network
3. Install using a file manager on your TV
4. Launch **Songtrybe TV** from your apps
```

## 🛠️ Configuration

The integration uses your API key: `tok_w73vyuwk3ugwuff2q3bqb3grli`

### Features Enabled:
- ✅ Automatic upload on every debug build
- ✅ Build metadata (commit SHA, timestamp)
- ✅ Public demo URLs (no login required)
- ✅ Android platform targeting
- ✅ Error handling and fallbacks

## 🔐 Security Notes

- API key is embedded in workflow (consider moving to GitHub Secrets for production)
- Demo URLs are public but expire after inactivity
- No sensitive data is exposed in demos
- Builds only upload debug APKs (not release)

## 🎉 Example Workflow

```
You: git push
GitHub: Building... (3 minutes)
GitHub: ✅ Build complete!
GitHub: 📱 Demo ready at: https://appetize.io/app/abc123

You: Click link
Browser: Opens live Android TV simulator
You: Use arrow keys to navigate app
You: Test all features instantly!
```

## 🆘 Troubleshooting

### Demo not working?
- Check if APK built successfully in Actions
- Look for Appetize upload errors in build logs
- Verify the demo URL was generated

### Navigation issues?
- Use arrow keys, not mouse
- Press Enter to select items
- Press Escape to go back

### App crashes in demo?
- Check if Firebase is configured correctly
- Verify all dependencies are included in APK
- Review build logs for errors

## 💡 Next Steps

1. **Push your code** to see the integration in action
2. **Share demo links** with your team for feedback
3. **Test regularly** during development
4. **Use for presentations** - no setup required!

Your Android TV app is now instantly testable by anyone, anywhere! 🚀