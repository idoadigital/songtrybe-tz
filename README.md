# Songtrybe TV - Android TV / Google TV App

A production-ready Android TV application for Songtrybe, featuring music video playback via YouTube, artist discovery, and personalized library features.

## Features

- 🎵 Browse musicians with TV-optimized rails (Home screen)
- 🎬 Music video playback via YouTube app (no scraping/downloading)
- 🔍 Discover artists by genre and categories
- ⭐ Favorites and Recently Played tracking
- 📺 Full D-pad navigation support (remote-first)
- 🎨 10-foot UI design for TV viewing distance
- 🔄 YouTube metadata enrichment with 30-day TTL cache
- 🔐 Firebase Auth integration for user data

## Architecture

### Android TV App (Kotlin + Jetpack Compose)
- **MVVM Architecture** with Repository pattern
- **Jetpack Compose for TV** for UI
- **Navigation Compose** for screen navigation
- **Coroutines + Flow** for async operations
- **Firebase Firestore** for data persistence
- **Coil** for image loading

### Cloud Functions (TypeScript)
- YouTube Data API v3 integration
- Metadata caching with 29-day TTL
- Batch processing (up to 50 videos per API call)
- Scheduled cache refresh for popular content

## Setup Instructions

### Prerequisites

1. Android Studio Arctic Fox or later
2. Node.js 18+ and npm
3. Firebase CLI (`npm install -g firebase-tools`)
4. YouTube Data API v3 key

### 1. Firebase Setup

1. Create a Firebase project at https://console.firebase.google.com
2. Enable Firestore Database
3. Enable Authentication (Email/Password or Google Sign-In)
4. Download `google-services.json` and place in `songtrybe-tv-app/app/`

### 2. Android TV App Setup

```bash
cd songtrybe-tv-app

# Add google-services.json to app/ directory
# Open project in Android Studio
# Sync Gradle files
# Build and run on Android TV emulator or device
```

### 3. Cloud Functions Setup

```bash
cd songtrybe-tv-functions
npm install

# Set YouTube API key
firebase functions:config:set youtube.api_key="YOUR_API_KEY"

# Deploy functions
npm run deploy
```

### 4. Firestore Indexes

Create the following composite indexes in Firestore Console:

```
Collection: musicians
- Field: isHighlighted (Ascending)
- Field: upvoteCount (Descending)

Collection: musicians  
- Field: isVerified (Ascending)
- Field: upvoteCount (Descending)

Collection: musicians
- Field: genre (Ascending)
- Field: upvoteCount (Descending)
```

## YouTube Compliance

This app strictly complies with YouTube Terms of Service:

- ✅ Uses official YouTube app for playback (no iframe/WebView embeds)
- ✅ No scraping, downloading, or direct stream URL extraction
- ✅ YouTube Data API responses cached with 29-day TTL
- ✅ Provides user data deletion mechanism
- ✅ Displays "Plays via YouTube" attribution
- ✅ Links to YouTube Terms of Service in Settings

## Firestore Schema

### Collections

#### `musicians`
Existing collection with fields:
- name, imageUrl, genre, location, bio
- youtubeUrls (array of YouTube watch URLs)
- isHighlighted, isVerified, upvoteCount
- Social links: instagram, youtubeChannel, spotify, appleMusic

#### `youtubeVideoCache`
YouTube metadata cache:
- videoId, title, channelTitle
- durationSeconds, thumbnails
- lastFetchedAt (server timestamp)
- stats (optional): viewCount, likeCount

#### `users/{uid}/favorites`
User's favorite musicians

#### `users/{uid}/recentlyPlayed`
User's playback history

## Key Files Structure

```
songtrybe-tv/
├── songtrybe-tv-app/           # Android TV application
│   ├── app/
│   │   ├── src/main/java/com/songtrybe/tv/
│   │   │   ├── data/
│   │   │   │   ├── firebase/   # Firebase schema constants
│   │   │   │   ├── model/      # Data models
│   │   │   │   ├── mapper/     # Firestore mappers
│   │   │   │   └── repository/ # Data repositories
│   │   │   ├── ui/
│   │   │   │   ├── components/ # Reusable UI components
│   │   │   │   ├── screens/    # Screen composables
│   │   │   │   └── theme/      # App theme
│   │   │   ├── navigation/     # Navigation setup
│   │   │   └── util/           # Utilities (YouTube, time)
│   │   └── AndroidManifest.xml # TV launcher configuration
│   └── build.gradle.kts
│
└── songtrybe-tv-functions/     # Firebase Cloud Functions
    ├── src/
    │   └── index.ts            # YouTube metadata functions
    ├── package.json
    └── tsconfig.json
```

## Development

### Running Locally

1. **Android TV Emulator**:
   - Create TV AVD in Android Studio
   - API Level 21+ (Android 5.0)
   - Run app with Shift+F10

2. **Physical Android TV**:
   - Enable Developer Options
   - Enable USB Debugging
   - Connect via USB or ADB over network

3. **Cloud Functions Emulator**:
   ```bash
   cd songtrybe-tv-functions
   npm run serve
   ```

### Testing D-pad Navigation

Use arrow keys in emulator or physical remote:
- ↑↓←→ for navigation
- Enter/OK for selection
- Back for navigation back
- Home to exit app

## TV Banner Asset

Create a 320x180px banner image and place at:
`app/src/main/res/drawable/tv_banner.png`

This appears in the Android TV launcher.

## Deployment

### Android TV App

1. Generate signed APK/AAB in Android Studio
2. Upload to Google Play Console
3. Complete TV-specific requirements:
   - TV screenshots (1920x1080)
   - TV banner (320x180)
   - Leanback launcher intent

### Cloud Functions

```bash
cd songtrybe-tv-functions
npm run deploy
```

## Performance Optimizations

- Lazy loading with `LazyRow` and `LazyColumn`
- Stable keys for list items
- Image caching with Coil
- Firestore offline persistence
- YouTube metadata pre-caching for popular content

## Security

- API keys stored in Cloud Functions config
- Rate limiting on callable functions
- Firebase App Check (optional)
- User authentication required for favorites/history

## TODO / Future Enhancements

The following screens need full implementation:
- [ ] Discover Screen - Genre and category grids
- [ ] Search Screen - TV keyboard integration
- [ ] Library Screen - Favorites and Recently Played lists
- [ ] Settings Screen - Auth, legal links, data deletion

## Support

For issues or questions, please file an issue in the repository.

## License

[Your License Here]

## Acknowledgments

- Built for Songtrybe
- Powered by Firebase and YouTube Data API
- UI components from Jetpack Compose for TV