package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.models.Account;
import com.example.myapplication.db.models.BaseModel;

import java.util.List;

public class TableViewFirstColumnAdapter extends RecyclerView.Adapter<TableViewFirstColumnAdapter.RowViewHolder> implements StickHeaderItemDecoration.StickyHeaderInterface {

    private final List<? extends BaseModel> data;
    private final LayoutInflater layoutInflater;

    public TableViewFirstColumnAdapter(Context context, List<? extends BaseModel> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = layoutInflater.inflate(R.layout.table_header_column_list_item, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.table_column_list_item, parent, false);
        }

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {

        int rowPos = holder.getAdapterPosition();

        if(rowPos == 0) {
            Log.d(TableViewFirstColumnAdapter.class.toString(),"Placeholder test");

            holder.textViewId.setText(R.string.col_id);
        } else {
            Account account = (Account) data.get(rowPos - 1);

            holder.textViewId.setText(account.getId().toString());
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return itemPosition == 0;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.table_header_column_list_item;
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }
    @Override
    public void bindHeaderData(View header, int headerPosition) {
        // binding our header data here
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {
        protected TextView textViewId;

        public RowViewHolder(View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.text_view_id);
        }
    }
}
