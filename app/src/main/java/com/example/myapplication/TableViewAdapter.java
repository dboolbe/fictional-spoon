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

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TableViewAdapter extends RecyclerView.Adapter<TableViewAdapter.RowViewHolder> implements StickHeaderItemDecoration.StickyHeaderInterface {

    private final List<? extends BaseModel> data;
    private final LayoutInflater layoutInflater;

    public TableViewAdapter(Context context, List<? extends BaseModel> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = layoutInflater.inflate(R.layout.table_header_list_item, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.table_list_item, parent, false);

            if(viewType % 2 == 0) {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            } else {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.light_grey));
            }
        }

        Log.d(TableViewAdapter.class.toString(), "****************************************************************");
        Log.d(TableViewAdapter.class.toString(), "viewType: " + viewType);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {

        int rowPos = holder.getAdapterPosition();

        if(rowPos == 0) {
            Log.d(TableViewAdapter.class.toString(),"Placeholder test");

//            holder.textViewId.setText(R.string.col_id);
            holder.textViewName.setText(R.string.col_name);
            holder.textViewMonthlyAmount.setText(R.string.col_monthly_amount);
            holder.textViewYearlyAmount.setText(R.string.col_yearly_amount);
            holder.textViewMonthlyExpenseAmount.setText(R.string.col_monthly_expense_amount);
            holder.textViewMonthlyIncomeAmount.setText(R.string.col_monthly_income_amount);
            holder.textViewYearlyExpenseAmount.setText(R.string.col_yearly_expense_amount);
            holder.textViewYearlyIncomeAmount.setText(R.string.col_yearly_income_amount);
        } else {
            Locale locale = holder.itemView.getResources().getConfiguration().locale;
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
//            if(numberFormat instanceof DecimalFormat) {
//                DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
//                decimalFormat.setMinimumIntegerDigits(7);
//            }

            Account account = (Account) data.get(rowPos - 1);

//            holder.textViewId.setText(account.getId().toString());
            holder.textViewName.setText(account.getName());
            holder.textViewMonthlyAmount.setText(numberFormat.format(account.getMonthlyAmount()));
            holder.textViewYearlyAmount.setText(numberFormat.format(account.getYearlyAmount()));
            holder.textViewMonthlyExpenseAmount.setText(numberFormat.format(account.getMonthlyExpenseAmount()));
            holder.textViewMonthlyIncomeAmount.setText(numberFormat.format(account.getMonthlyIncomeAmount()));
            holder.textViewYearlyExpenseAmount.setText(numberFormat.format(account.getYearlyExpenseAmount()));
            holder.textViewYearlyIncomeAmount.setText(numberFormat.format(account.getYearlyIncomeAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
//        return (position == 0) ? 0 : 1;
        return position;
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return itemPosition == 0;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.table_header_list_item;
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
//        protected TextView textViewId;
        protected TextView textViewName;
        protected TextView textViewMonthlyAmount;
        protected TextView textViewYearlyAmount;
        protected TextView textViewMonthlyExpenseAmount;
        protected TextView textViewMonthlyIncomeAmount;
        protected TextView textViewYearlyExpenseAmount;
        protected TextView textViewYearlyIncomeAmount;

        public RowViewHolder(View itemView) {
            super(itemView);

//            textViewId = itemView.findViewById(R.id.text_view_id);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewMonthlyAmount = itemView.findViewById(R.id.text_view_monthly_amount);
            textViewYearlyAmount = itemView.findViewById(R.id.text_view_yearly_amount);
            textViewMonthlyExpenseAmount = itemView.findViewById(R.id.text_view_monthly_expense_amount);
            textViewMonthlyIncomeAmount = itemView.findViewById(R.id.text_view_monthly_income_amount);
            textViewYearlyExpenseAmount = itemView.findViewById(R.id.text_view_yearly_expense_amount);
            textViewYearlyIncomeAmount = itemView.findViewById(R.id.text_view_yearly_income_amount);
        }
    }
}
