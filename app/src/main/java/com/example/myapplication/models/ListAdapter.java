package com.example.myapplication.models;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.models.BaseModel;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ViewHolder> {

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private final List<? extends BaseModel> data;
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private final int listItemLayoutResourceId; // used to identify the component to attach listener to
    private final int listItemComponentResourceId; // used to show how the list item should be displayed

    private Integer listItemMonthlyAmountResourceId = null;
    private Integer stringMonthlyAmountResourceId = null;
    private Integer listItemYearlyAmountResourceId = null;
    private Integer stringYearlyAmountResourceId = null;

    // data is passed into the constructor
    public ListAdapter(Context context, List<? extends BaseModel> data, int listItemLayoutResourceId,
                       int listItemComponentResourceId) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.listItemLayoutResourceId = listItemLayoutResourceId;
        this.listItemComponentResourceId = listItemComponentResourceId;
    }

    public void setListItemMonthlyAmountResourceId(Integer listItemMonthlyAmountResourceId) {
        this.listItemMonthlyAmountResourceId = listItemMonthlyAmountResourceId;
    }

    public void setStringMonthlyAmountResourceId(Integer stringMonthlyAmountResourceId) {
        this.stringMonthlyAmountResourceId = stringMonthlyAmountResourceId;
    }

    public void setListItemYearlyAmountResourceId(Integer listItemYearlyAmountResourceId) {
        this.listItemYearlyAmountResourceId = listItemYearlyAmountResourceId;
    }

    public void setStringYearlyAmountResourceId(Integer stringYearlyAmountResourceId) {
        this.stringYearlyAmountResourceId = stringYearlyAmountResourceId;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(listItemLayoutResourceId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, listItemComponentResourceId, itemClickListener);
        if(listItemMonthlyAmountResourceId != null) viewHolder.setListItemMonthlyAmountResourceId(listItemMonthlyAmountResourceId);
        if(listItemYearlyAmountResourceId != null) viewHolder.setListItemYearlyAmountResourceId(listItemYearlyAmountResourceId);

        return viewHolder;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String displayText = data.get(position).getDisplayText();
        TextView textView = holder.getTextView();
        textView.setText(displayText);

        if(data.get(position).getMonthlyAmount() > 0) {
            textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
        } else if(data.get(position).getMonthlyAmount() < 0) {
            textView.setTypeface(textView.getTypeface(),Typeface.ITALIC);
        }

        textView = holder.getListItemMonthlyAmountView();
        if(textView != null && stringMonthlyAmountResourceId != null) {
            textView.setText(String.format(
                    holder.itemView.getResources().getString(stringMonthlyAmountResourceId),
                    data.get(position).getMonthlyAmount()));

            if(data.get(position).getMonthlyAmount() > 0) {
                textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
            } else if(data.get(position).getMonthlyAmount() < 0) {
                textView.setTypeface(textView.getTypeface(),Typeface.ITALIC);
            }
        }

        textView = holder.getListItemYearlyAmountView();
        if(textView != null && stringYearlyAmountResourceId != null) {
            textView.setText(String.format(
                    holder.itemView.getResources().getString(stringYearlyAmountResourceId),
                    data.get(position).getYearlyAmount()));

            if(data.get(position).getMonthlyAmount() > 0) {
                textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
            } else if(data.get(position).getMonthlyAmount() < 0) {
                textView.setTypeface(textView.getTypeface(),Typeface.ITALIC);
            }
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }

    // convenience method for getting data at click position
    public BaseModel getItem(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
