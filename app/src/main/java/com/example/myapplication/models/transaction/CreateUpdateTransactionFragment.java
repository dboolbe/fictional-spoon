package com.example.myapplication.models.transaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHandler;
import com.example.myapplication.db.models.Account;
import com.example.myapplication.db.models.BaseModel;
import com.example.myapplication.db.models.Bucket;
import com.example.myapplication.db.models.FrequencyType;
import com.example.myapplication.db.models.Transaction;
import com.example.myapplication.db.models.TransactionType;
import com.example.myapplication.models.account.CreateUpdateAccountFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CreateUpdateTransactionFragment extends Fragment implements AdapterView.OnItemSelectedListener,TextWatcher {

    DatabaseHandler databaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler = new DatabaseHandler(getContext());

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if(fab != null) {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_update_transaction, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextPayee = view.findViewById(R.id.editTextPayee);

        // Initialize the Frequency Types spinner
        Spinner frequencyTypeSpinner = (Spinner) view.findViewById(R.id.editTextFrequency);

        List<? extends BaseModel> frequencyTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(FrequencyType.class);

        ArrayList<String> frequencyTypeList = new ArrayList<>();
        for(BaseModel frequencyType : frequencyTypeEntries) {
            int resourceId = getResources().getIdentifier(((FrequencyType) frequencyType).getResourceId(),"string", getContext().getPackageName());
            frequencyTypeList.add(getResources().getString(resourceId));
        }

        ArrayAdapter frequencyTypeAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, frequencyTypeList);

        // Specify the layout to use when the list of choices appears
        frequencyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        frequencyTypeSpinner.setAdapter(frequencyTypeAdapter);

        // Initialize the Transaction Types spinner
        Spinner transactionTypeSpinner = (Spinner) view.findViewById(R.id.editTextTransactionType);
        // Create an ArrayAdapter using the string array and a default spinner layout

        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);

        ArrayList<String> transactionList = new ArrayList<>();
        for(BaseModel transactionType : transactionTypeEntries) {
            int resourceId = getResources().getIdentifier(((TransactionType) transactionType).getResourceId(),"string", getContext().getPackageName());
            transactionList.add(getResources().getString(resourceId));
        }

        ArrayAdapter transactionAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, transactionList);

        // Specify the layout to use when the list of choices appears
        transactionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        transactionTypeSpinner.setAdapter(transactionAdapter);

        EditText editTextAmount = view.findViewById(R.id.editTextAmount);

        // Initialize the Bucket spinner
        Spinner bucketSpinner = (Spinner) view.findViewById(R.id.editTextBucket);
        // Create an ArrayAdapter using the string array and a default spinner layout

        List<? extends BaseModel> bucketEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(Bucket.class);

        ArrayList<String> bucketList = new ArrayList<>();
        for(BaseModel bucket : bucketEntries) {
            bucketList.add(bucket.getDisplayText());
        }

        ArrayAdapter bucketAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, bucketList);

        // Specify the layout to use when the list of choices appears
        bucketAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        bucketSpinner.setAdapter(bucketAdapter);

        EditText amountMonthly = view.findViewById(R.id.amountMonthly);
        EditText amountYearly = view.findViewById(R.id.amountYearly);

        // Initialize the Account spinner
        Spinner accountFromSpinner = (Spinner) view.findViewById(R.id.editFromAccount);
        Spinner accountToSpinner = (Spinner) view.findViewById(R.id.editToAccount);
        // Create an ArrayAdapter using the string array and a default spinner layout

        List<? extends BaseModel> accountEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(Account.class);

        ArrayList<String> accountList = new ArrayList<>();
        for(BaseModel account : accountEntries) {
            accountList.add(account.getDisplayText());
        }

        ArrayAdapter accountAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, accountList);

        // Specify the layout to use when the list of choices appears
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        accountFromSpinner.setAdapter(accountAdapter);
        accountToSpinner.setAdapter(accountAdapter);

        EditText editTextComment = view.findViewById(R.id.editTextComment);

        if(getArguments() != null) {
            Log.d(CreateUpdateAccountFragment.class.toString(),""+getArguments().get("id"));
            Integer id = getArguments().getInt("id");
            if (id != null && id >= 0) {
                Transaction transaction = (Transaction) databaseHandler.getEntry(id, Transaction.class);

                Log.d(CreateUpdateTransactionFragment.class.toString(), transaction.toString());

                editTextPayee.setText(transaction.getPayee());
                for(int i = 0; i < frequencyTypeEntries.size(); i++) {
                    if(frequencyTypeEntries.get(i).getId().equals(transaction.getFrequency())) {
                        frequencyTypeSpinner.setSelection(i);
                    }
                }
                for(int i = 0; i < transactionTypeEntries.size(); i++) {
                    if(transactionTypeEntries.get(i).getId().equals(transaction.getTransactionType())) {
                        transactionTypeSpinner.setSelection(i);
                    }
                }
                editTextAmount.setText(transaction.getAmount().toString());
                for(int i = 0; i < bucketEntries.size(); i++) {
                    if(bucketEntries.get(i).getId().equals(transaction.getBucket())) {
                        bucketSpinner.setSelection(i);
                    }
                }
                for(int i = 0; i < accountEntries.size(); i++) {
                    if(accountEntries.get(i).getId().equals(transaction.getAccountFrom())) {
                        accountFromSpinner.setSelection(i);
                    }
                    if(accountEntries.get(i).getId().equals(transaction.getAccountTo())) {
                        accountToSpinner.setSelection(i);
                    }
                }
                editTextComment.setText(transaction.getComment());

                TextView textView = view.findViewById(R.id.title);
                textView.setText(R.string.update_account);

                Button button = view.findViewById(R.id.createEditButton);
                button.setText(R.string.update);
            }
        }

        editTextAmount.addTextChangedListener(this);
        frequencyTypeSpinner.setOnItemSelectedListener(this);
        transactionTypeSpinner.setOnItemSelectedListener(this);

        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CreateUpdateTransactionFragment.this)
                        .popBackStack();
            }
        });

        view.findViewById(R.id.createEditButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(CreateUpdateAccountFragment.class.toString(), "Edit name: " + editTextPayee.getText());

                    Log.d(CreateUpdateAccountFragment.class.toString(), "Transaction value: " + transactionTypeEntries.get(transactionTypeSpinner.getSelectedItemPosition()));

                    Transaction transaction = new Transaction(editTextPayee.getText().toString(),
                            transactionTypeEntries.get(transactionTypeSpinner.getSelectedItemPosition()).getId(),
                            Double.parseDouble(editTextAmount.getText().toString()),
                            bucketEntries.get(bucketSpinner.getSelectedItemPosition()).getId(),
                            frequencyTypeEntries.get(frequencyTypeSpinner.getSelectedItemPosition()).getId(),
                            accountEntries.get(accountFromSpinner.getSelectedItemPosition()).getId(),
                            accountEntries.get(accountToSpinner.getSelectedItemPosition()).getId(),
                            editTextComment.getText().toString());

                    if (getArguments() != null) {
                        transaction.setId(getArguments().getInt("id"));
                        Log.d(CreateUpdateAccountFragment.class.toString(), "" + getArguments().get("id"));
                    }

                    boolean status = transaction.getId() != null ? databaseHandler.update(transaction) : databaseHandler.create(transaction);

                    Log.d(CreateUpdateAccountFragment.class.toString(),"Status: " + status);
                    Log.d(CreateUpdateAccountFragment.class.toString(), "Transaction: " + transaction);

                    NavHostFragment.findNavController(CreateUpdateTransactionFragment.this)
                            .popBackStack();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public double calculateMonthlyTotal(double amount) {
        Spinner frequencyTypeSpinner = (Spinner) getActivity().findViewById(R.id.editTextFrequency);
        List<? extends BaseModel> frequencyTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(FrequencyType.class);
//        List<? extends BaseModel> frequencyTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(FrequencyType.class);
        FrequencyType frequencyType = (FrequencyType) frequencyTypeEntries.get(frequencyTypeSpinner.getSelectedItemPosition());

        Spinner transactionTypeSpinner = (Spinner) getActivity().findViewById(R.id.editTextTransactionType);
        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);
//        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);
        TransactionType transactionType = (TransactionType) transactionTypeEntries.get(transactionTypeSpinner.getSelectedItemPosition());

        if(transactionType.getName().equals("debit")) {
            return -1.0 * amount * frequencyType.getMonthlyRatio();
        } else {
            return 1.0 * amount * frequencyType.getMonthlyRatio();
        }
    }

    public double calculateYearlyTotal(double amount) {
        Spinner frequencyTypeSpinner = (Spinner) getActivity().findViewById(R.id.editTextFrequency);
        List<? extends BaseModel> frequencyTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(FrequencyType.class);
//        List<? extends BaseModel> frequencyTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(FrequencyType.class);
        FrequencyType frequencyType = (FrequencyType) frequencyTypeEntries.get(frequencyTypeSpinner.getSelectedItemPosition());

        Spinner transactionTypeSpinner = (Spinner) getActivity().findViewById(R.id.editTextTransactionType);
        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);
//        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);
        TransactionType transactionType = (TransactionType) transactionTypeEntries.get(transactionTypeSpinner.getSelectedItemPosition());

        if(transactionType.getName().equals("debit")) {
            return -1.0 * amount * frequencyType.getYearlyRatio();
        } else {
            return 1.0 * amount * frequencyType.getYearlyRatio();
        }
    }

    public void updatedMonthlyTotal() {
        EditText editTextAmount = getActivity().findViewById(R.id.editTextAmount);
        EditText amountMonthly = getActivity().findViewById(R.id.amountMonthly);

        if(editTextAmount != null && amountMonthly != null && !editTextAmount.getText().toString().isEmpty()) {
            amountMonthly.setText(String.format(getResources().getString(R.string.format_money), calculateMonthlyTotal(Double.parseDouble(editTextAmount.getText().toString()))));
        }
    }

    public void updatedYearlyTotal() {
        EditText editTextAmount = getActivity().findViewById(R.id.editTextAmount);
        EditText amountYearly = getActivity().findViewById(R.id.amountYearly);

        if(editTextAmount != null && amountYearly != null && !editTextAmount.getText().toString().isEmpty()) {
            amountYearly.setText(String.format(getResources().getString(R.string.format_money), calculateYearlyTotal(Double.parseDouble(editTextAmount.getText().toString()))));
        }
    }

    public void updateAccountLabels() {
        Spinner transactionTypeSpinner = (Spinner) getActivity().findViewById(R.id.editTextTransactionType);
        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);
//        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);
        TransactionType transactionType = (TransactionType) transactionTypeEntries.get(transactionTypeSpinner.getSelectedItemPosition());

        TextView fromAccountLabel = getActivity().findViewById(R.id.fromAccountLabel);
        TextView toAccountLabel = getActivity().findViewById(R.id.toAccountLabel);

        if(fromAccountLabel != null && toAccountLabel != null) {
            if(transactionType.getName().equals("debit")) {
                toAccountLabel.setText(R.string.account_funds_going_to);
                fromAccountLabel.setText(R.string.account_funds_coming_from);
            } else {
                toAccountLabel.setText(R.string.account_funds_coming_from);
                fromAccountLabel.setText(R.string.account_funds_going_to);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updatedMonthlyTotal();
        updatedYearlyTotal();
        updateAccountLabels();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        updatedMonthlyTotal();
        updatedYearlyTotal();
        updateAccountLabels();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatedMonthlyTotal();
        updatedYearlyTotal();
        updateAccountLabels();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}