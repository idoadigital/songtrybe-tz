# Firebase Setup for Songtrybe TV

Your Firebase project `cowork-1ff61` has been configured for the Songtrybe TV app.

## Configuration Applied

- **Project ID**: cowork-1ff61
- **Auth Domain**: cowork-1ff61.firebaseapp.com
- **Database URL**: https://cowork-1ff61-default-rtdb.firebaseio.com
- **Storage Bucket**: cowork-1ff61.firebasestorage.app

## Next Steps

### 1. Firebase Console Setup

Go to [Firebase Console](https://console.firebase.google.com/project/cowork-1ff61) and:

#### Enable Required Services:
1. **Firestore Database**
   - Navigate to Firestore Database
   - Click "Create Database"
   - Choose production mode
   - Select your preferred location

2. **Authentication**
   - Go to Authentication > Sign-in method
   - Enable Email/Password or Google Sign-In

3. **Cloud Functions**
   - May require billing account upgrade (Blaze plan)
   - Needed for YouTube metadata enrichment

### 2. Android App Registration

1. Go to Project Settings > General
2. Under "Your apps", click "Add app" > Android
3. Register app with package name: `com.songtrybe.tv`
4. Download the updated `google-services.json` if needed

### 3. Firestore Security Rules

Add these rules in Firestore > Rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Musicians collection - read-only for all users
    match /musicians/{document=**} {
      allow read: if true;
      allow write: if false;
    }
    
    // YouTube cache - read-only for all users
    match /youtubeVideoCache/{document=**} {
      allow read: if true;
      allow write: if false;
    }
    
    // User data - only authenticated users can access their own data
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### 4. Deploy Cloud Functions

```bash
cd songtrybe-tv-functions

# Login to Firebase
firebase login

# Set YouTube API key (get from Google Cloud Console)
firebase functions:config:set youtube.api_key="YOUR_YOUTUBE_API_KEY"

# Deploy functions
npm run deploy
```

### 5. Create Firestore Indexes

In Firestore > Indexes, create:

```
Collection: musicians
Fields: isHighlighted (Ascending), upvoteCount (Descending)

Collection: musicians  
Fields: isVerified (Ascending), upvoteCount (Descending)

Collection: musicians
Fields: genre (Ascending), upvoteCount (Descending)
```

### 6. YouTube Data API Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Select your Firebase project (cowork-1ff61)
3. Enable YouTube Data API v3
4. Create API credentials (API Key)
5. Restrict key to YouTube Data API v3
6. Use this key in Cloud Functions config (step 4)

### 7. Sample Data Structure

Create sample musician documents in Firestore:

```json
{
  "name": "Artist Name",
  "imageUrl": "https://example.com/image.jpg",
  "genre": "Hip-Hop/Rap",
  "location": "New York, NY",
  "bio": "Artist biography...",
  "youtubeUrls": [
    "https://www.youtube.com/watch?v=VIDEO_ID_1",
    "https://www.youtube.com/watch?v=VIDEO_ID_2"
  ],
  "isHighlighted": true,
  "isVerified": false,
  "upvoteCount": 100,
  "categories": ["urban", "trending"],
  "instagram": "artisthandle",
  "youtubeChannel": "https://youtube.com/c/channelname",
  "spotify": "https://open.spotify.com/artist/...",
  "updatedAt": "2024-01-20T10:00:00Z"
}
```

## Testing

1. Build and run the Android TV app:
```bash
cd songtrybe-tv-app
# Open in Android Studio
# Build > Make Project
# Run on Android TV emulator
```

2. Test Cloud Functions locally:
```bash
cd songtrybe-tv-functions
npm run serve
```

## Important Notes

- The `google-services.json` file has been created with your configuration
- Make sure to keep your API keys secure
- Enable Firebase Analytics and Crashlytics for production monitoring
- Consider implementing Firebase App Check for additional security

## Troubleshooting

If you encounter issues:

1. **Auth errors**: Verify Authentication is enabled in Firebase Console
2. **Firestore errors**: Check security rules and indexes
3. **Functions errors**: Ensure billing is enabled and API key is set
4. **App crashes**: Check logcat for detailed error messages

For support, check the [Firebase Documentation](https://firebase.google.com/docs) or file an issue in the repository.