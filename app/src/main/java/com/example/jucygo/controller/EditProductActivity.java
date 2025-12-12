package com.example.jucygo.controller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity to edit or delete an existing product.
 * Validates input fields before updating.
 */
public class EditProductActivity extends AppCompatActivity {

    private TextInputEditText editTextProductName;
    private TextInputEditText editTextProductPrice;
    private TextInputEditText editTextProductQuantity;
    private TextInputEditText editTextProductDescription;
    private ImageView imageViewProduct;
    private MaterialButton buttonSelectImage;
    private MaterialButton buttonUpdateProduct;
    private MaterialButton buttonDeleteProduct;
    private MaterialButton buttonCancel;
    private DBHelper dbHelper;
    private int productId;
    private Product currentProduct;
    
    private String currentImagePath = "";
    private Uri photoUri;
    
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        dbHelper = new DBHelper(this);
        productId = getIntent().getIntExtra("product_id", -1);

        if (productId == -1) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupActivityResultLaunchers();
        initializeViews();
        loadProduct();
        setupButtons();
    }
    
    private void setupActivityResultLaunchers() {
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        currentImagePath = copyImageToAppStorage(selectedImageUri);
                        if (!currentImagePath.isEmpty()) {
                            imageViewProduct.setImageURI(Uri.parse(currentImagePath));
                            imageViewProduct.setPadding(0, 0, 0, 0);
                        }
                    }
                }
            }
        );
        
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success && photoUri != null) {
                    currentImagePath = photoUri.toString();
                    imageViewProduct.setImageURI(photoUri);
                    imageViewProduct.setPadding(0, 0, 0, 0);
                }
            }
        );
        
        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
                }
            }
        );
    }
    
    private void initializeViews() {
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductQuantity = findViewById(R.id.editTextProductQuantity);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonUpdateProduct = findViewById(R.id.buttonUpdateProduct);
        buttonDeleteProduct = findViewById(R.id.buttonDeleteProduct);
        buttonCancel = findViewById(R.id.buttonCancel);
    }
    
    private void setupButtons() {
        buttonSelectImage.setOnClickListener(v -> showImagePickerDialog());
        buttonUpdateProduct.setOnClickListener(v -> updateProduct());
        buttonDeleteProduct.setOnClickListener(v -> showDeleteConfirmationDialog());
        buttonCancel.setOnClickListener(v -> finish());
    }
    
    private void showImagePickerDialog() {
        String[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery)};
        
        new AlertDialog.Builder(this)
            .setTitle(R.string.select_image)
            .setItems(options, (dialog, which) -> {
                if (which == 0) {
                    checkCameraPermissionAndOpen();
                } else {
                    openGallery();
                }
            })
            .show();
    }
    
    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
    
    private void openCamera() {
        try {
            File photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(this, 
                    getPackageName() + ".fileprovider", photoFile);
            cameraLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_creating_image, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }
    
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PRODUCT_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentImagePath = image.getAbsolutePath();
        return image;
    }
    
    private String copyImageToAppStorage(Uri sourceUri) {
        try {
            File destFile = createImageFile();
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            OutputStream outputStream = new FileOutputStream(destFile);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            
            inputStream.close();
            outputStream.close();
            
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_saving_image, Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    /**
     * Loads the product data from database and populates the form fields.
     */
    private void loadProduct() {
        currentProduct = dbHelper.getProductById(productId);

        if (currentProduct == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextProductName.setText(currentProduct.getName());
        editTextProductPrice.setText(String.valueOf(currentProduct.getPrice()));
        editTextProductQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        editTextProductDescription.setText(currentProduct.getDescription());
        
        // Load existing image
        currentImagePath = currentProduct.getImagePath();
        if (currentProduct.hasImage()) {
            File imageFile = new File(currentImagePath);
            if (imageFile.exists()) {
                imageViewProduct.setImageURI(Uri.fromFile(imageFile));
                imageViewProduct.setPadding(0, 0, 0, 0);
            }
        }
    }

    /**
     * Validates and updates the product in the database.
     */
    private void updateProduct() {
        String name = editTextProductName.getText().toString().trim();
        String priceStr = editTextProductPrice.getText().toString().trim();
        String quantityStr = editTextProductQuantity.getText().toString().trim();
        String description = editTextProductDescription.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            editTextProductName.setError("Product name is required");
            editTextProductName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            editTextProductPrice.setError("Product price is required");
            editTextProductPrice.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                editTextProductPrice.setError("Price must be positive");
                editTextProductPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextProductPrice.setError("Invalid price format");
            editTextProductPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            editTextProductQuantity.setError("Product quantity is required");
            editTextProductQuantity.requestFocus();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                editTextProductQuantity.setError("Quantity must be positive");
                editTextProductQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextProductQuantity.setError("Invalid quantity format");
            editTextProductQuantity.requestFocus();
            return;
        }

        // Update product in database
        Product updatedProduct = new Product(productId, name, price, quantity, description, currentImagePath);
        int result = dbHelper.updateProduct(updatedProduct);

        if (result > 0) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Shows a confirmation dialog before deleting the product.
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete \"" + currentProduct.getName() + "\"?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Deletes the product from the database.
     */
    private void deleteProduct() {
        // Delete associated image file
        if (currentProduct.hasImage()) {
            File imageFile = new File(currentProduct.getImagePath());
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
        
        int result = dbHelper.deleteProduct(productId);

        if (result > 0) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
        }
    }
}

