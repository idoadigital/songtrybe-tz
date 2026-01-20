# 🚀 GitHub Actions - Quick Setup (3 Steps)

Get automatic APK builds in 3 minutes!

## Step 1: Run Setup Script

```bash
./setup-github-actions.sh
```

This script will:
- Initialize git
- Create GitHub repository
- Push your code

## Step 2: Add Google Services Secret

The script will give you a command to copy. Run it:

```bash
# Copy your google-services.json as base64
base64 -i songtrybe-tv-app/app/google-services.json | pbcopy  # macOS
```

Then:
1. Go to your GitHub repo → Settings → Secrets → Actions
2. Click "New repository secret"
3. Name: `GOOGLE_SERVICES_JSON`
4. Value: Paste (Cmd+V)
5. Click "Add secret"

## Step 3: Trigger Your First Build

```bash
git add .
git commit -m "Test automatic build"
git push
```

## ✅ That's It!

Go to your GitHub repo → **Actions** tab → Watch it build!

After ~5 minutes, download your APK from the Artifacts section.

---

## 📱 How to Use

### Every time you push code:
```bash
git add .
git commit -m "Your changes"
git push
```
→ APK automatically builds → Download from Actions tab

### Manual build:
1. Go to Actions tab
2. Click "Build Songtrybe TV App"
3. Click "Run workflow"
4. Choose build type (debug/release)

### Create a release:
```bash
git tag v1.0.0
git push --tags
```
→ Creates GitHub Release with APKs attached

---

## 🔗 Quick Links

After setup, your links will be:
- **Repository**: `https://github.com/YOUR_USERNAME/songtrybe-tv`
- **Actions**: `https://github.com/YOUR_USERNAME/songtrybe-tv/actions`
- **Latest Build**: Click Actions → Latest workflow → Artifacts

---

## ❓ FAQ

**Q: Where do I download the APK?**
A: Actions tab → Click latest build → Scroll to Artifacts → Download

**Q: How long does build take?**
A: Usually 3-5 minutes

**Q: Can I build without pushing?**
A: Yes! Actions tab → Run workflow → Manual trigger

**Q: Build failed?**
A: Check you added the GOOGLE_SERVICES_JSON secret correctly

---

## 🎉 No Android Studio Needed!

Just code → push → download APK. That's it!