package com.example.jucygo.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jucygo.R;
import com.example.jucygo.model.Sale;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for RecyclerView to display sales history.
 */
public class SalesHistoryAdapter extends RecyclerView.Adapter<SalesHistoryAdapter.SaleViewHolder> {

    private List<Sale> saleList;
    private DecimalFormat priceFormat;
    private SimpleDateFormat inputFormat;
    private SimpleDateFormat outputFormat;

    public SalesHistoryAdapter(List<Sale> saleList) {
        this.saleList = saleList;
        this.priceFormat = new DecimalFormat("$#,##0.00");
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sale, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        Sale sale = saleList.get(position);
        holder.bind(sale);
    }

    @Override
    public int getItemCount() {
        return saleList != null ? saleList.size() : 0;
    }

    public void updateSaleList(List<Sale> newSaleList) {
        this.saleList = newSaleList;
        notifyDataSetChanged();
    }

    class SaleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private TextView textViewTotalAmount;
        private TextView textViewQuantitySold;
        private TextView textViewUnitPrice;
        private TextView textViewSaleDate;

        SaleViewHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewTotalAmount = itemView.findViewById(R.id.textViewTotalAmount);
            textViewQuantitySold = itemView.findViewById(R.id.textViewQuantitySold);
            textViewUnitPrice = itemView.findViewById(R.id.textViewUnitPrice);
            textViewSaleDate = itemView.findViewById(R.id.textViewSaleDate);
        }

        void bind(Sale sale) {
            textViewProductName.setText(sale.getProductName());
            textViewTotalAmount.setText(priceFormat.format(sale.getTotalAmount()));
            textViewQuantitySold.setText(String.valueOf(sale.getQuantitySold()));
            textViewUnitPrice.setText(priceFormat.format(sale.getUnitPrice()));
            
            // Format date
            try {
                Date date = inputFormat.parse(sale.getDate());
                if (date != null) {
                    textViewSaleDate.setText(outputFormat.format(date));
                } else {
                    textViewSaleDate.setText(sale.getDate());
                }
            } catch (ParseException e) {
                textViewSaleDate.setText(sale.getDate());
            }
        }
    }
}

