# Jucygo - Juice Sales Management
## User Manual

### Version 1.0
**Platform:** Android 10 and above  
**Offline Mode:** Fully functional without internet connection

---

## Table of Contents
1. [Installation](#installation)
2. [Getting Started](#getting-started)
3. [Product Management](#product-management)
4. [Sales Management](#sales-management)
5. [Sales History](#sales-history)
6. [Tips & Best Practices](#tips--best-practices)

---

## Installation

### Requirements
- Android device running Android 10 (API level 29) or higher
- At least 50 MB of free storage space

### Installation Steps
1. Locate the `jucygo.apk` file on your device or download it
2. Enable "Install from Unknown Sources" in your device settings (if needed)
3. Tap the APK file to begin installation
4. Follow the on-screen prompts to complete installation
5. Open the app from your app drawer

---

## Getting Started

### First Launch
When you first open Jucygo, you'll see the **Product List** screen. To get started:

1. **Add Your First Product**
   - Tap "Add New Product"
   - Fill in product details (name, price, quantity, description)
   - Tap "Add Product"

2. **Start Recording Sales**
   - Once you have products, tap "Record Sale"
   - Select a product and enter quantity
   - Confirm the sale

---

## Product Management

### Adding a New Product
1. From the Product List, tap **"Add New Product"**
2. Fill in the required fields:
   - **Product Name** (required)
   - **Price** (required, e.g., 5.99)
   - **Quantity** (required, initial stock)
   - **Description** (optional)
3. Tap **"Add Product"** to save

### Viewing Products
- The Product List shows all your products
- Each card displays: Name, Price, Quantity, and Description
- Tap any product card to edit or delete it

### Editing a Product
1. From the Product List, tap on the product you want to edit
2. Modify any fields as needed
3. Tap **"Update Product"** to save changes
4. Or tap **"Delete Product"** to remove it (confirmation required)

---

## Sales Management

### Recording a Sale
1. From the Product List, tap **"Record Sale"**
2. **Select a Product** from the dropdown
   - Current stock and unit price will display automatically
3. **Enter Quantity** to sell
   - Total amount calculates automatically as you type
4. Tap **"Confirm Sale"** to complete

### Important Notes
- Sales are only allowed if sufficient stock is available
- Stock is automatically updated after each sale
- If stock is insufficient, you'll see an error message with available quantity

### Example Sale Flow
```
Product: "Orange Juice"
Stock Available: 50 units
Unit Price: $3.50
Quantity to Sell: 10 units
Total: $35.00

After confirmation:
- Sale is recorded
- Stock updated to 40 units
- Success message displayed
```

---

## Sales History

### Viewing Sales History
1. From the Product List, tap **"Sales History"**
2. All recorded sales are displayed in reverse chronological order (newest first)

### Search & Filter Features

#### Search by Product Name
- Type any part of the product name in the search bar
- Results filter in real-time

#### Search by Date
- Type date in format: **YYYY-MM-DD** (e.g., 2024-01-15)
- Shows all sales on that date

### Sale Information Displayed
Each sale card shows:
- **Product Name**
- **Total Amount** (highlighted)
- **Quantity Sold**
- **Unit Price**
- **Date & Time** (formatted: "Jan 15, 2024 at 14:30")

---

## Tips & Best Practices

### Product Management
- ✅ Keep product names clear and consistent
- ✅ Update stock quantities when you receive new inventory
- ✅ Set accurate prices for proper sales calculations
- ✅ Use descriptions to add notes or specifications

### Sales Recording
- ✅ Always verify stock before selling
- ✅ Check the calculated total before confirming
- ✅ Record sales immediately after transactions

### Stock Management
- ⚠️ Stock decreases automatically with each sale
- ⚠️ You cannot sell more than available stock
- ⚠️ Update product quantities manually if you add inventory

### Data Management
- 💾 All data is stored locally on your device
- 💾 No internet connection required
- 💾 Data persists between app sessions
- ⚠️ Uninstalling the app will delete all data

---

## Troubleshooting

### Issue: "No products available"
**Solution:** Add at least one product before recording sales.

### Issue: "Insufficient stock" error
**Solution:** Check available stock and reduce quantity, or add more inventory.

### Issue: Search not working
**Solution:** 
- For date search, use format: YYYY-MM-DD (e.g., 2024-01-15)
- For product search, type any part of the product name

### Issue: App crashes or freezes
**Solution:**
1. Close the app completely
2. Restart your device
3. If problem persists, reinstall the app (⚠️ data will be lost)

---

## Navigation Map

```
Main Screen (Product List)
├── Add New Product → AddProductActivity
├── Record Sale → AddSaleActivity
├── Sales History → SalesHistoryActivity
└── Click Product → EditProductActivity
```

---

## Support

For technical issues or feature requests, please contact the development team.

**Version:** 1.0  
**Last Updated:** 2024

---

## Quick Reference Card

| Action | Location | Button/Feature |
|--------|----------|----------------|
| Add Product | Product List | "Add New Product" |
| Edit Product | Product List | Tap product card |
| Record Sale | Product List | "Record Sale" |
| View History | Product List | "Sales History" |
| Search Sales | Sales History | Search bar |
| Back to Products | Any screen | "Back" or "Cancel" |

---

*Thank you for using Jucygo! We hope this application helps you manage your juice sales efficiently.*

