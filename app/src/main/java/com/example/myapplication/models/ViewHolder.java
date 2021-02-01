package com.example.myapplication.models;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private final TextView textView;
    private final ListAdapter.ItemClickListener itemClickListener;

    private Integer listItemMonthlyAmountResourceId;
    private Integer listItemYearlyAmountResourceId;

    public ViewHolder(View itemView, int resourceId, ListAdapter.ItemClickListener itemClickListener) {
        super(itemView);
        textView = itemView.findViewById(resourceId);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.itemClickListener = itemClickListener;
    }

    public ViewHolder(View itemView, int resourceId, ListAdapter.ItemClickListener itemClickListener,
                      int alphaId, int betaId) {
        super(itemView);
        textView = itemView.findViewById(resourceId);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.itemClickListener = itemClickListener;
    }

    public void setListItemMonthlyAmountResourceId(Integer listItemMonthlyAmountResourceId) {
        this.listItemMonthlyAmountResourceId = listItemMonthlyAmountResourceId;
    }

    public TextView getListItemMonthlyAmountView() {
        TextView textView = null;
        if(listItemMonthlyAmountResourceId != null) {
            textView = this.itemView.findViewById(listItemMonthlyAmountResourceId);
        }
        return textView;
    }

    public void setListItemYearlyAmountResourceId(Integer listItemYearlyAmountResourceId) {
        this.listItemYearlyAmountResourceId = listItemYearlyAmountResourceId;
    }

    public TextView getListItemYearlyAmountView() {
        TextView textView = null;
        if(listItemYearlyAmountResourceId != null) {
            textView = this.itemView.findViewById(listItemYearlyAmountResourceId);
        }
        return textView;
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener != null) itemClickListener.onItemClick(textView, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        if(itemClickListener != null) itemClickListener.onItemLongClick(textView, getAdapterPosition());
        return true;
    }

    public TextView getTextView() {
        return textView;
    }
}
