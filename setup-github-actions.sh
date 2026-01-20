#!/bin/bash

# Setup script for GitHub Actions automatic APK builds
# This script helps you push your code to GitHub and set up secrets

echo "🚀 Setting up GitHub Actions for Songtrybe TV"
echo "=============================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if git is initialized
if [ ! -d .git ]; then
    echo "Initializing git repository..."
    git init
    git add .
    git commit -m "Initial commit: Songtrybe TV Android app"
    echo -e "${GREEN}✓ Git repository initialized${NC}"
else
    echo -e "${GREEN}✓ Git repository already initialized${NC}"
fi

# Check if GitHub CLI is installed
if command -v gh &> /dev/null; then
    echo -e "${GREEN}✓ GitHub CLI found${NC}"
    echo ""
    echo "Creating GitHub repository..."
    
    # Create repo using GitHub CLI
    gh repo create songtrybe-tv --public --source=. --remote=origin --push
    
    echo -e "${GREEN}✓ Repository created and code pushed${NC}"
else
    echo -e "${YELLOW}GitHub CLI not found. Please create repository manually:${NC}"
    echo ""
    echo -e "${BLUE}1. Go to: https://github.com/new${NC}"
    echo -e "${BLUE}2. Repository name: songtrybe-tv${NC}"
    echo -e "${BLUE}3. Make it public${NC}"
    echo -e "${BLUE}4. Don't initialize with README${NC}"
    echo -e "${BLUE}5. Click 'Create repository'${NC}"
    echo ""
    echo "Then run these commands:"
    echo ""
    echo "git remote add origin https://github.com/YOUR_USERNAME/songtrybe-tv.git"
    echo "git branch -M main"
    echo "git push -u origin main"
    echo ""
    read -p "Have you created the repository? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Please create the repository first, then run this script again"
        exit 1
    fi
    
    read -p "Enter your GitHub username: " GITHUB_USERNAME
    git remote add origin https://github.com/$GITHUB_USERNAME/songtrybe-tv.git
    git branch -M main
    git push -u origin main
fi

echo ""
echo "=============================================="
echo -e "${YELLOW}IMPORTANT: Set up the GOOGLE_SERVICES_JSON secret${NC}"
echo "=============================================="
echo ""
echo "You need to add your google-services.json as a secret:"
echo ""
echo "1. Get the base64 encoded content:"
echo ""
echo -e "${BLUE}Run this command:${NC}"
echo "base64 -i songtrybe-tv-app/app/google-services.json | pbcopy"
echo ""
echo "2. Go to your repository settings:"
if command -v gh &> /dev/null; then
    REPO_URL=$(gh repo view --json url -q .url)
    echo "$REPO_URL/settings/secrets/actions/new"
else
    echo "https://github.com/YOUR_USERNAME/songtrybe-tv/settings/secrets/actions/new"
fi
echo ""
echo "3. Create new secret:"
echo "   Name: GOOGLE_SERVICES_JSON"
echo "   Value: [Paste the base64 string]"
echo ""
echo "=============================================="
echo ""

# Offer to open browser
if command -v open &> /dev/null; then
    read -p "Open GitHub settings in browser? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if command -v gh &> /dev/null; then
            REPO_URL=$(gh repo view --json url -q .url)
            open "$REPO_URL/settings/secrets/actions/new"
        fi
    fi
fi

echo ""
echo -e "${GREEN}✅ GitHub Actions workflow is ready!${NC}"
echo ""
echo "Next steps:"
echo "1. Add the GOOGLE_SERVICES_JSON secret (instructions above)"
echo "2. Push any code change to trigger a build"
echo "3. Check the Actions tab for your APK"
echo ""
echo "Quick test - trigger a build now:"
echo "  git add ."
echo "  git commit -m 'Test GitHub Actions build'"
echo "  git push"
echo ""
echo -e "${GREEN}🎉 Setup complete!${NC}"