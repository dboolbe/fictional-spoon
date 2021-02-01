package com.example.myapplication.models.transaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHandler;
import com.example.myapplication.db.models.BaseModel;
import com.example.myapplication.db.models.Transaction;
import com.example.myapplication.models.ListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ReadTransactionFragment extends Fragment implements ListAdapter.ItemClickListener {

    DatabaseHandler databaseHandler;
    ListAdapter adapter;
    List<? extends BaseModel> dbList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(getContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_transaction, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // fetch the detailed entries
        dbList = databaseHandler.getAllDetailedEntries(Transaction.class);

        for(BaseModel baseModel : dbList) {
            Transaction transaction = ((Transaction) baseModel);
            Log.d(ReadTransactionFragment.class.toString(), "new Transaction(\"" + transaction.getPayee() + "\", " + transaction.getTransactionType() + ", " + transaction.getAmount() + ", " + transaction.getBucket() + ", " + transaction.getFrequency() + ", " + transaction.getAccountFrom() + ", " + transaction.getAccountTo() + ", \"" + transaction.getComment() + "\");");
        }

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.transactionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListAdapter(getContext(), dbList, R.layout.list_item_transaction, R.id.listItemName);
        adapter.setClickListener(this);
        adapter.setListItemMonthlyAmountResourceId(R.id.listItemMonthlyAmount);
        adapter.setStringMonthlyAmountResourceId(R.string.read_bucket_monthly);
        adapter.setListItemYearlyAmountResourceId(R.id.listItemYearlyAmount);
        adapter.setStringYearlyAmountResourceId(R.string.read_bucket_yearly);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ReadTransactionFragment.this)
                        .navigate(R.id.action_readTransactionFragment_to_createUpdateTransactionFragment,null, ((MainActivity)getActivity()).getNavOptions());
            }
        });

        ((MainActivity) getActivity()).setCurrentSession(MainActivity.MANAGE_TRANSACTION_SECTION);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle args = new Bundle();
        args.putInt("id", adapter.getItem(position).getId());

        NavHostFragment.findNavController(ReadTransactionFragment.this)
                .navigate(R.id.action_readTransactionFragment_to_createUpdateTransactionFragment, args);

//        Fragment fragment = new CreateUpdateTransactionFragment();
//        getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out).replace(R.id.nav_host_fragment,fragment).addToBackStack(null).commit();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch(choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Integer id = databaseHandler.delete(adapter.getItem(position));
                        if(id != null) {
                            dbList.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(getActivity(), R.string.delete_transaction_confirmation_message, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.format(getResources().getString(R.string.confirm_transaction_deletion_label),adapter.getItem(position).getDisplayText()))
                .setIcon(android.R.drawable.ic_delete)
                .setMessage(R.string.confirm_transaction_deletion)
                .setPositiveButton(R.string.yes_label, dialogClickListener)
                .setNegativeButton(R.string.no_label, dialogClickListener).show();
    }
}