package com.example.myapplication.models.account;

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
import com.example.myapplication.db.models.Account;
import com.example.myapplication.db.models.BaseModel;
import com.example.myapplication.models.ListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ReadAccountFragment extends Fragment implements ListAdapter.ItemClickListener {

    DatabaseHandler databaseHandler;
    ListAdapter adapter;
    List<? extends BaseModel> dbList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(getContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_account, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavHostFragment.findNavController(this).popBackStack(R.id.readBucketFragment,true);

        dbList = databaseHandler.getAllDetailedEntries(Account.class);

        for(BaseModel baseModel : dbList) {
            Account account = ((Account) baseModel);
            Log.d(ReadAccountFragment.class.toString(), "new Account(\"" + account.getName() + "\",\"" + account.getDescription() + "\");");
        }

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.accountList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListAdapter(getContext(), dbList, R.layout.list_item_account, R.id.listItemName);
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
                NavHostFragment.findNavController(ReadAccountFragment.this)
                        .navigate(R.id.action_readAccountFragment_to_createUpdateAccountFragment);
            }
        });

        ((MainActivity) getActivity()).setCurrentSession(MainActivity.MANAGE_ACCOUNT_SECTION);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle args = new Bundle();
        args.putInt("id", adapter.getItem(position).getId());

        NavHostFragment.findNavController(ReadAccountFragment.this)
                .navigate(R.id.action_readAccountFragment_to_createUpdateAccountFragment, args);
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
                            Toast.makeText(getActivity(), R.string.delete_account_confirmation_message, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.format(getResources().getString(R.string.confirm_account_deletion_label),adapter.getItem(position).getDisplayText()))
                .setIcon(android.R.drawable.ic_delete)
                .setMessage(R.string.confirm_account_deletion)
                .setPositiveButton(R.string.yes_label, dialogClickListener)
                .setNegativeButton(R.string.no_label, dialogClickListener).show();
    }
}