# How to Add App Icon for Jucygo

## ✅ Current Status

Your app already has a **custom juice-themed icon** installed! The icon features:
- 🎨 Gradient background (Purple to Teal) matching your app colors
- 🥤 Juice glass with orange liquid and bubbles
- 🥬 Green straw with leaf decoration
- 💧 Juice splash effects

## 📁 Icon Files Location

The app icon is configured using **Adaptive Icons** located in:
- `app/src/main/res/mipmap-anydpi/ic_launcher.xml` - Main icon configuration
- `app/src/main/res/mipmap-anydpi/ic_launcher_round.xml` - Round icon configuration
- `app/src/main/res/drawable/ic_launcher_background.xml` - Background layer
- `app/src/main/res/drawable/ic_launcher_foreground.xml` - Foreground icon layer

## 🎨 Using Your Own Custom Icon

### Method 1: Using Android Studio Image Asset Studio (Recommended)

1. **Open Image Asset Studio:**
   - Right-click on `app/src/main/res` folder
   - Select **New > Image Asset**
   - Or go to: **Tools > Resource Manager > + > Image Asset**

2. **Configure the Icon:**
   - **Icon Type:** Select "Launcher Icons (Adaptive and Legacy)"
   - **Foreground Layer:**
     - Choose "Image" tab
     - Click folder icon and select your image
     - Recommended size: 1024x1024px (square)
     - Adjust padding if needed
   - **Background Layer:**
     - Choose "Color" tab
     - Set to your brand color (e.g., `#9C27B0` purple or `#009688` teal)
     - Or choose "Image" for a custom background

3. **Generate Icons:**
   - Click **Next**
   - Click **Finish**
   - Android Studio will generate all required sizes automatically

### Method 2: Manual Vector Drawable (Current Method)

1. **Edit Background:**
   - Open `app/src/main/res/drawable/ic_launcher_background.xml`
   - Modify the colors, gradients, or shapes

2. **Edit Foreground:**
   - Open `app/src/main/res/drawable/ic_launcher_foreground.xml`
   - Draw your icon using SVG paths or shapes
   - Keep it centered in a 108x108dp viewport

3. **Test the Icon:**
   - Build and run the app
   - Check the launcher icon on your device

### Method 3: Using PNG Images

1. **Prepare Images:**
   - Create icons in these sizes:
     - 48x48px (mdpi)
     - 72x72px (hdpi)
     - 96x96px (xhdpi)
     - 144x144px (xxhdpi)
     - 192x192px (xxxhdpi)

2. **Place Images:**
   - Copy images to respective folders:
     - `app/src/main/res/mipmap-mdpi/ic_launcher.png`
     - `app/src/main/res/mipmap-hdpi/ic_launcher.png`
     - `app/src/main/res/mipmap-xhdpi/ic_launcher.png`
     - `app/src/main/res/mipmap-xxhdpi/ic_launcher.png`
     - `app/src/main/res/mipmap-xxxhdpi/ic_launcher.png`

3. **Update Manifest:**
   - Already configured: `android:icon="@mipmap/ic_launcher"`

## 🎯 Icon Design Tips

### Best Practices:
- ✅ Use simple, recognizable shapes
- ✅ Keep important content in the center 66% (safe zone)
- ✅ Use high contrast colors
- ✅ Test on different backgrounds
- ✅ Ensure visibility at small sizes (24dp)

### Safe Zone:
- Keep important content within **36dp** from each edge
- Android may crop edges for different device shapes

### Colors:
- Use your brand colors: Purple (`#9C27B0`) and Teal (`#009688`)
- Ensure contrast for visibility

## 🔧 Quick Customization

To change icon colors quickly:

**Background Color:**
Edit `ic_launcher_background.xml` - change gradient colors

**Foreground Color:**
Edit `ic_launcher_foreground.xml` - modify juice color or add new elements

## 📱 Testing Your Icon

1. **Build the app:** `Build > Make Project`
2. **Install on device:** Run the app
3. **Check launcher:** Look at your device's app drawer
4. **Test different shapes:** Adaptive icons change shape on different devices

## 🎨 Current Icon Description

Your current icon shows:
- **Background:** Purple-to-teal gradient with decorative circles
- **Foreground:** White glass with orange juice, green straw with leaf, bubbles, and splash effects
- **Theme:** Fresh, modern, and fitting for a juice sales app

## 🆘 Troubleshooting

**Icon not updating?**
- Clean and rebuild: `Build > Clean Project` then `Build > Rebuild Project`
- Uninstall and reinstall the app
- Clear app data

**Icon looks pixelated?**
- Use vector drawables (XML) instead of PNG
- Or ensure PNG images are high resolution (at least 192x192px for xxxhdpi)

**Icon doesn't show in launcher?**
- Check `AndroidManifest.xml` has `android:icon="@mipmap/ic_launcher"`
- Ensure files exist in `mipmap-anydpi/` folder

## 📚 Resources

- [Adaptive Icons Guide](https://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive)
- [Material Design Icons](https://fonts.google.com/icons)
- [Icon Generator Tools](https://icon.kitchen/)

---

**Your app icon is ready!** The custom juice-themed icon is already installed and will appear when you build and install the app.

