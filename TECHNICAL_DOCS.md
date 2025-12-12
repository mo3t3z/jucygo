# Jucygo - Juice Sales Management
## Technical Documentation

### Version 1.0
**Development Environment:** Android Studio  
**Language:** Java  
**Minimum SDK:** Android 10 (API 29)  
**Target SDK:** Android 14 (API 36)  
**Architecture:** MVC (Model-View-Controller)

---

## Table of Contents
1. [Project Structure](#project-structure)
2. [Database Schema](#database-schema)
3. [Class Diagram](#class-diagram)
4. [Navigation Map](#navigation-map)
5. [Key Components](#key-components)
6. [API Reference](#api-reference)
7. [Build Instructions](#build-instructions)

---

## Project Structure

```
app/src/main/
├── java/com/example/jucygo/
│   ├── model/
│   │   ├── Product.java          # Product model class
│   │   ├── Sale.java             # Sale model class
│   │   └── DBHelper.java         # SQLite database helper
│   ├── controller/
│   │   ├── MainActivity.java                    # Entry point
│   │   ├── ProductListActivity.java             # Product list display
│   │   ├── AddProductActivity.java              # Add new product
│   │   ├── EditProductActivity.java             # Edit/delete product
│   │   ├── AddSaleActivity.java                 # Record new sale
│   │   ├── SalesHistoryActivity.java            # Sales history view
│   │   ├── ProductAdapter.java                  # RecyclerView adapter for products
│   │   └── SalesHistoryAdapter.java             # RecyclerView adapter for sales
│   └── view/ (implied through XML layouts)
├── res/
│   ├── layout/
│   │   ├── activity_product_list.xml
│   │   ├── activity_add_product.xml
│   │   ├── activity_edit_product.xml
│   │   ├── activity_add_sale.xml
│   │   ├── activity_sales_history.xml
│   │   ├── item_product.xml
│   │   └── item_sale.xml
│   ├── values/
│   │   ├── strings.xml
│   │   ├── colors.xml
│   │   └── themes.xml
│   └── ...
└── AndroidManifest.xml
```

---

## Database Schema

### Database Information
- **Database Name:** `JucygoDB`
- **Version:** 2
- **Location:** `/data/data/com.example.jucygo/databases/JucygoDB`

### Tables

#### 1. Products Table
```sql
CREATE TABLE products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL,
    quantity INTEGER NOT NULL,
    description TEXT
);
```

**Columns:**
- `id`: Primary key, auto-increment
- `name`: Product name (required)
- `price`: Unit price (required)
- `quantity`: Stock quantity (required)
- `description`: Optional product description

#### 2. Sales Table
```sql
CREATE TABLE sales (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    productName TEXT NOT NULL,
    quantitySold INTEGER NOT NULL,
    unitPrice REAL NOT NULL,
    totalAmount REAL NOT NULL,
    date TEXT NOT NULL
);
```

**Columns:**
- `id`: Primary key, auto-increment
- `productName`: Name of the product sold
- `quantitySold`: Number of units sold
- `unitPrice`: Price per unit at time of sale
- `totalAmount`: Total sale amount (unitPrice × quantitySold)
- `date`: Timestamp in format "yyyy-MM-dd HH:mm:ss"

---

## Class Diagram

### Model Classes

```
┌─────────────────┐
│    Product      │
├─────────────────┤
│ - id: int       │
│ - name: String  │
│ - price: double │
│ - quantity: int │
│ - description:  │
│   String        │
├─────────────────┤
│ + getters/setter│
└─────────────────┘

┌─────────────────┐
│     Sale        │
├─────────────────┤
│ - id: int       │
│ - productName:  │
│   String        │
│ - quantitySold: │
│   int           │
│ - unitPrice:    │
│   double        │
│ - totalAmount:  │
│   double        │
│ - date: String  │
├─────────────────┤
│ + getters/setter│
│ + generateDate()│
└─────────────────┘
```

### Database Helper

```
┌──────────────────────────────┐
│        DBHelper               │
│  extends SQLiteOpenHelper     │
├──────────────────────────────┤
│ - DATABASE_NAME               │
│ - DATABASE_VERSION            │
│ - TABLE_PRODUCTS              │
│ - TABLE_SALES                 │
├──────────────────────────────┤
│ + onCreate()                  │
│ + onUpgrade()                 │
│                              │
│ Products:                     │
│ + addProduct()                │
│ + getAllProducts()            │
│ + getProductById()            │
│ + getProductByName()          │
│ + updateProduct()             │
│ + deleteProduct()             │
│                              │
│ Sales:                        │
│ + addSale()                   │
│ + getAllSales()               │
│ + searchSalesByProductName()  │
│ + searchSalesByDate()         │
│ + getTotalSalesByDate()       │
│                              │
│ Stock:                        │
│ + isStockSufficient()         │
│ + updateStockAfterSale()      │
└──────────────────────────────┘
```

### Controller Classes

```
Activities:
- MainActivity (entry point)
- ProductListActivity (main screen)
- AddProductActivity
- EditProductActivity
- AddSaleActivity
- SalesHistoryActivity

Adapters:
- ProductAdapter
- SalesHistoryAdapter
```

---

## Navigation Map

```
┌─────────────────────┐
│   MainActivity      │ (auto-navigates)
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ ProductListActivity │ ◄────────┐
│ (Main Hub)          │          │
└─────┬───────────────┘          │
      │                           │
      ├─► AddProductActivity      │
      │                           │
      ├─► EditProductActivity     │
      │                           │
      ├─► AddSaleActivity ───────┘
      │                           │
      └─► SalesHistoryActivity ───┘
```

**Navigation Flow:**
1. `MainActivity` → Automatically navigates to `ProductListActivity`
2. `ProductListActivity` → Central hub with navigation to all modules
3. All activities have back navigation or cancel buttons
4. Parent activities set in `AndroidManifest.xml`

---

## Key Components

### 1. Model Layer (`model/`)

**Product.java**
- Represents a juice product
- Constructors for new/existing products
- Standard getters and setters

**Sale.java**
- Represents a sales transaction
- Auto-generates date on creation
- Stores immutable sale data

**DBHelper.java**
- Extends `SQLiteOpenHelper`
- Manages all database operations
- Handles version upgrades
- Provides CRUD operations for products and sales
- Includes stock validation and update methods

### 2. View Layer (XML Layouts)

**Material Design Components:**
- MaterialCardView for items
- MaterialButton for actions
- TextInputLayout for forms
- SearchView for filtering

**Consistent UI:**
- Color scheme: Purple and Teal
- Typography: 24sp headings, 16sp body
- Spacing: 16dp margins, 8dp padding
- Cards with elevation and corner radius

### 3. Controller Layer (`controller/`)

**Activities:**
- All activities extend `AppCompatActivity`
- Follow lifecycle best practices
- Use `onResume()` for data refresh
- Proper intent passing between activities

**Adapters:**
- Extend `RecyclerView.Adapter`
- ViewHolder pattern for performance
- Support for data updates via `notifyDataSetChanged()`

---

## API Reference

### DBHelper Methods

#### Product Operations

```java
// Add a new product
long addProduct(Product product)

// Get all products
List<Product> getAllProducts()

// Get product by ID
Product getProductById(int id)

// Get product by name
Product getProductByName(String productName)

// Update product
int updateProduct(Product product)

// Delete product
int deleteProduct(int id)
```

#### Sale Operations

```java
// Add a new sale
long addSale(Sale sale)

// Get all sales (sorted DESC by ID)
List<Sale> getAllSales()

// Search sales by product name
List<Sale> searchSalesByProductName(String query)

// Search sales by date
List<Sale> searchSalesByDate(String dateQuery)

// Get total sales amount for a date
double getTotalSalesByDate(String date)
```

#### Stock Operations

```java
// Check if stock is sufficient
boolean isStockSufficient(String productName, int quantityRequested)

// Update stock after sale
int updateStockAfterSale(String productName, int quantitySold)
```

---

## Build Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK (API 29-36)

### Build Steps

1. **Open Project**
   ```bash
   Open Android Studio → File → Open → Select project folder
   ```

2. **Sync Gradle**
   - Click "Sync Now" when prompted
   - Or: File → Sync Project with Gradle Files

3. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

4. **Locate APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### Gradle Configuration

**Key Dependencies:**
- `androidx.appcompat:appcompat`
- `com.google.android.material:material`
- `androidx.recyclerview:recyclerview`

**Build Configuration:**
- Min SDK: 29 (Android 10)
- Target SDK: 36
- Compile SDK: 36
- Java Version: 11

---

## Architecture Patterns

### MVC (Model-View-Controller)

**Model (`model/`):**
- Data classes: `Product`, `Sale`
- Database: `DBHelper`
- Business logic and data persistence

**View (XML Layouts):**
- UI definitions in `res/layout/`
- No logic, only presentation
- Material Design components

**Controller (`controller/`):**
- Activities handle user interaction
- Adapters manage RecyclerView data binding
- Bridge between Model and View

### Data Flow

```
User Action → Activity → DBHelper → SQLite
                    ↓
                 Model Object
                    ↓
              Adapter → RecyclerView → User
```

---

## Database Migration

**Version History:**
- Version 1: Products table only
- Version 2: Added Sales table

**Migration Logic:**
```java
if (oldVersion < 2) {
    // Create sales table
    db.execSQL(CREATE_TABLE_SALES);
}
```

**Future Migrations:**
- Increment `DATABASE_VERSION`
- Add migration logic in `onUpgrade()`
- Test with existing data

---

## Performance Considerations

### Optimizations Implemented
- ✅ RecyclerView for efficient list rendering
- ✅ ViewHolder pattern in adapters
- ✅ Database queries use indexed columns
- ✅ Proper cursor closing
- ✅ Database connection management

### Best Practices
- Close database connections after use
- Use transactions for multiple operations
- Limit data loaded at once
- Recycle views in RecyclerView

---

## Testing Recommendations

### Manual Testing Checklist
- [ ] Add/Edit/Delete products
- [ ] Record sales with various quantities
- [ ] Test stock validation
- [ ] Search sales by product name
- [ ] Search sales by date
- [ ] Verify data persistence after app restart
- [ ] Test navigation flow
- [ ] Test with empty states (no products/sales)

### Unit Testing (Future)
- Database operations
- Model classes
- Validation logic

### Integration Testing (Future)
- Full user workflows
- Data consistency
- Stock management accuracy

---

## Security Considerations

- ✅ No external API calls (offline only)
- ✅ Data stored locally (SQLite)
- ✅ Input validation on all forms
- ⚠️ No encryption (local data only)
- ⚠️ No backup mechanism (user responsibility)

---

## Future Enhancements

### Potential Features
- Export sales data to CSV/PDF
- Product categories
- Discount management
- Multi-user support
- Data backup/restore
- Reports and analytics
- Barcode scanning

### Technical Improvements
- Unit tests
- Dependency injection
- Room Database migration
- Kotlin conversion
- Jetpack Compose UI

---

## Version History

### Version 1.0 (Current)
- Product CRUD operations
- Sales recording with stock management
- Sales history with search/filter
- Complete MVC architecture
- Material Design UI

---

## Contact & Support

For technical inquiries or contributions:
- Review code documentation in Java files
- Check this technical documentation
- Refer to USER_MANUAL.md for user guidance

---

**Document Version:** 1.0  
**Last Updated:** 2024  
**Maintained By:** Development Team

