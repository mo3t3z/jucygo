package com.example.jucygo.controller;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jucygo.R;
import com.example.jucygo.model.Product;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter for RecyclerView to display products in a list.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProduct;
        private TextView textViewProductName;
        private TextView textViewProductPrice;
        private TextView textViewProductQuantity;
        private TextView textViewProductDescription;

        ProductViewHolder(View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            textViewProductDescription = itemView.findViewById(R.id.textViewProductDescription);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onProductClick(productList.get(position));
                }
            });
        }

        void bind(Product product) {
            textViewProductName.setText(product.getName());
            
            DecimalFormat priceFormat = new DecimalFormat("DT #,##0.00");
            textViewProductPrice.setText(priceFormat.format(product.getPrice()));
            
            textViewProductQuantity.setText(String.valueOf(product.getQuantity()));
            
            String description = product.getDescription();
            if (description == null || description.trim().isEmpty()) {
                textViewProductDescription.setText("No description");
            } else {
                textViewProductDescription.setText(description);
            }

            // Set product image - use custom image if available, otherwise use placeholder
            if (product.hasImage()) {
                File imageFile = new File(product.getImagePath());
                if (imageFile.exists()) {
                    imageViewProduct.setImageURI(Uri.fromFile(imageFile));
                    imageViewProduct.clearColorFilter();
                    imageViewProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageViewProduct.setPadding(0, 0, 0, 0);
                } else {
                    setProductPlaceholder(product.getName().toLowerCase());
                }
            } else {
                setProductPlaceholder(product.getName().toLowerCase());
            }
        }

        private void setProductPlaceholder(String productName) {
            // Set placeholder image
            imageViewProduct.setImageResource(R.drawable.ic_product_placeholder);
            imageViewProduct.setScaleType(ImageView.ScaleType.CENTER);
            imageViewProduct.setPadding(12, 12, 12, 12);
            
            // Add color tint based on product name for visual variety
            if (productName.contains("orange") || productName.contains("mango") || productName.contains("peach")) {
                imageViewProduct.setColorFilter(itemView.getContext().getColor(R.color.orange_juice));
            } else if (productName.contains("apple") || productName.contains("green")) {
                imageViewProduct.setColorFilter(itemView.getContext().getColor(R.color.green_juice));
            } else if (productName.contains("strawberry") || productName.contains("berry") || productName.contains("grape")) {
                imageViewProduct.setColorFilter(itemView.getContext().getColor(R.color.purple_500));
            } else {
                imageViewProduct.setColorFilter(itemView.getContext().getColor(R.color.teal_500));
            }
        }
    }
}

