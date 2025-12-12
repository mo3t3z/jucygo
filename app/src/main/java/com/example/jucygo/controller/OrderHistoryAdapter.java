package com.example.jucygo.controller;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Order;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying orders in RecyclerView.
 * Shows order details with customer name, product, quantity, total, status, and date.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private DBHelper dbHelper;
    private Runnable onOrderUpdated;
    private DecimalFormat priceFormat;
    private SimpleDateFormat inputFormat;
    private SimpleDateFormat outputFormat;

    public OrderHistoryAdapter(List<Order> orderList, DBHelper dbHelper, Runnable onOrderUpdated) {
        this.orderList = orderList;
        this.dbHelper = dbHelper;
        this.onOrderUpdated = onOrderUpdated;
        this.priceFormat = new DecimalFormat("$#,##0.00");
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        
        holder.textViewCustomerName.setText(order.getCustomerName());
        holder.textViewProductName.setText(order.getProductName());
        holder.textViewQuantityOrdered.setText(String.valueOf(order.getQuantityOrdered()));
        holder.textViewTotalAmount.setText(priceFormat.format(order.getTotalAmount()));
        holder.textViewUnitPrice.setText(priceFormat.format(order.getUnitPrice()));
        
        // Format and display date
        try {
            Date date = inputFormat.parse(order.getDate());
            holder.textViewDate.setText(outputFormat.format(date));
        } catch (ParseException e) {
            holder.textViewDate.setText(order.getDate());
        }
        
        // Display status with appropriate styling
        String status = order.getStatus();
        holder.textViewStatus.setText(getStatusDisplayText(status));
        
        // Set status background color
        int statusColor;
        switch (status) {
            case Order.STATUS_COMPLETED:
                statusColor = holder.itemView.getContext().getColor(R.color.green_juice);
                break;
            case Order.STATUS_CANCELLED:
                statusColor = holder.itemView.getContext().getColor(R.color.red_error);
                break;
            default: // pending
                statusColor = holder.itemView.getContext().getColor(R.color.orange_juice);
                break;
        }
        holder.cardStatus.setCardBackgroundColor(statusColor);
        
        // Show/hide action buttons based on status
        if (order.isPending()) {
            holder.buttonComplete.setVisibility(View.VISIBLE);
            holder.buttonCancel.setVisibility(View.VISIBLE);
            
            holder.buttonComplete.setOnClickListener(v -> {
                showCompleteConfirmation(holder, order);
            });
            
            holder.buttonCancel.setOnClickListener(v -> {
                showCancelConfirmation(holder, order);
            });
        } else {
            holder.buttonComplete.setVisibility(View.GONE);
            holder.buttonCancel.setVisibility(View.GONE);
        }
    }

    private String getStatusDisplayText(String status) {
        switch (status) {
            case Order.STATUS_COMPLETED:
                return "✓ Completed";
            case Order.STATUS_CANCELLED:
                return "✗ Cancelled";
            default:
                return "⏳ Pending";
        }
    }

    private void showCompleteConfirmation(OrderViewHolder holder, Order order) {
        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle(R.string.complete_order_title)
                .setMessage(R.string.complete_order_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    int result = dbHelper.updateOrderStatus(order.getId(), Order.STATUS_COMPLETED);
                    if (result > 0) {
                        Toast.makeText(holder.itemView.getContext(), 
                                R.string.order_completed_success, Toast.LENGTH_SHORT).show();
                        if (onOrderUpdated != null) {
                            onOrderUpdated.run();
                        }
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void showCancelConfirmation(OrderViewHolder holder, Order order) {
        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle(R.string.cancel_order_title)
                .setMessage(R.string.cancel_order_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    // Cancel order and restore stock
                    int result = dbHelper.updateOrderStatus(order.getId(), Order.STATUS_CANCELLED);
                    if (result > 0) {
                        // Restore the stock that was deducted when order was placed
                        restoreStock(holder, order);
                        Toast.makeText(holder.itemView.getContext(), 
                                R.string.order_cancelled_success, Toast.LENGTH_SHORT).show();
                        if (onOrderUpdated != null) {
                            onOrderUpdated.run();
                        }
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void restoreStock(OrderViewHolder holder, Order order) {
        // Get the product and restore stock
        var product = dbHelper.getProductByName(order.getProductName());
        if (product != null) {
            int newQuantity = product.getQuantity() + order.getQuantityOrdered();
            product.setQuantity(newQuantity);
            dbHelper.updateProduct(product);
        }
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    /**
     * Update the order list and refresh the RecyclerView.
     */
    public void updateOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCustomerName;
        TextView textViewProductName;
        TextView textViewQuantityOrdered;
        TextView textViewTotalAmount;
        TextView textViewUnitPrice;
        TextView textViewDate;
        TextView textViewStatus;
        MaterialCardView cardStatus;
        MaterialButton buttonComplete;
        MaterialButton buttonCancel;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.textViewCustomerName);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewQuantityOrdered = itemView.findViewById(R.id.textViewQuantityOrdered);
            textViewTotalAmount = itemView.findViewById(R.id.textViewTotalAmount);
            textViewUnitPrice = itemView.findViewById(R.id.textViewUnitPrice);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            cardStatus = itemView.findViewById(R.id.cardStatus);
            buttonComplete = itemView.findViewById(R.id.buttonComplete);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
        }
    }
}
