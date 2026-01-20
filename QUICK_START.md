# 🚀 Songtrybe TV - Quick Start Guide

Get the Songtrybe TV app running in 5 minutes!

## Prerequisites
- Android Studio installed
- Node.js 18+ installed
- Firebase CLI (`npm install -g firebase-tools`)

## Step 1: Setup Firebase (2 min)

1. Go to [Firebase Console](https://console.firebase.google.com/project/cowork-1ff61)
2. Enable **Firestore Database** (Create Database → Production mode)
3. Enable **Authentication** (Sign-in method → Email/Password)

## Step 2: Deploy Cloud Functions (2 min)

```bash
cd songtrybe-tv-functions
npm install
firebase login

# Deploy with YouTube API key already configured
./deploy-functions.sh
```

Your YouTube API key is already configured in the deploy script! ✅

## Step 3: Add Sample Data to Firestore (1 min)

In Firebase Console → Firestore, create a `musicians` collection with this sample document:

```json
{
  "name": "Sample Artist",
  "imageUrl": "https://picsum.photos/400/400",
  "genre": "Hip-Hop",
  "location": "New York, NY",
  "bio": "An amazing artist with great music.",
  "youtubeUrls": [
    "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    "https://www.youtube.com/watch?v=9bZkp7q19f0"
  ],
  "isHighlighted": true,
  "isVerified": true,
  "upvoteCount": 100,
  "instagram": "artist",
  "youtubeChannel": "https://youtube.com/c/artist"
}
```

## Step 4: Run the Android TV App (2 min)

1. Open `songtrybe-tv-app` folder in Android Studio
2. Wait for Gradle sync to complete
3. Create Android TV emulator (or connect physical device)
4. Click **Run** ▶️

## 🎉 That's it!

The app should now be running with:
- ✅ Musicians displayed on home screen
- ✅ YouTube metadata automatically fetched
- ✅ Click videos to launch YouTube app
- ✅ D-pad navigation working

## Testing Features

### Navigate with D-pad:
- **Arrow Keys**: Navigate between items
- **Enter/OK**: Select item
- **Back**: Go back

### Test Playback:
1. Navigate to any artist
2. Select a video
3. YouTube app will launch (or browser if not installed)

## Troubleshooting

### If no musicians appear:
- Check Firestore has documents in `musicians` collection
- Verify Firestore is enabled in Firebase Console

### If videos don't show metadata:
- Check Cloud Functions are deployed: `firebase list functions`
- View function logs: `firebase functions:log`

### If app crashes:
- Check Android Studio logcat for errors
- Ensure `google-services.json` is in `app/` folder

## Next Steps

- Add more musicians to Firestore
- Customize the UI theme in `Theme.kt`
- Implement remaining screens (Search, Library, Settings)
- Test on real Android TV device

## Support

Need help? Check the main [README.md](README.md) for detailed documentation.