#!/bin/bash

# Deploy script for Songtrybe TV Cloud Functions
# This sets the YouTube API key and deploys functions

echo "🚀 Deploying Songtrybe TV Cloud Functions..."

# Set the YouTube API key in Firebase config
echo "Setting YouTube API key..."
firebase functions:config:set youtube.api_key="AIzaSyDi78Z-7WAYPC_qVNwdOpxZQe5CPE7BFgg"

# Build TypeScript
echo "Building TypeScript..."
npm run build

# Deploy functions
echo "Deploying to Firebase..."
firebase deploy --only functions

echo "✅ Deployment complete!"
echo ""
echo "Your Cloud Functions are now live with YouTube metadata enrichment enabled."
echo "The YouTube API key has been securely stored in Firebase config."