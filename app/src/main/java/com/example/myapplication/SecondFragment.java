package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.db.DatabaseHandler;
import com.example.myapplication.db.models.Account;
import com.example.myapplication.db.models.BaseModel;
import com.example.myapplication.db.models.Bucket;
import com.example.myapplication.db.models.FrequencyType;
import com.example.myapplication.db.models.TransactionType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SecondFragment extends Fragment {

    DatabaseHandler databaseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databaseHandler = new DatabaseHandler(getContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFrequencyTypesComponent(view);
        initializeTransactionTypesComponent(view);

        EditText editTextAmount = view.findViewById(R.id.editTextAmount);
        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatedMonthlyTotal();
                updatedYearlyTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initializeBucketComponent(view);

        EditText amountMonthly = view.findViewById(R.id.amountMonthly);
        EditText amountYearly = view.findViewById(R.id.amountYearly);

        initializeAccountsComponent(view);

        EditText editTextComment = view.findViewById(R.id.editTextComment);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        ((MainActivity) getActivity()).setCurrentSession(MainActivity.NO_SECTION);
    }

    private void initializeAccountsComponent(@NonNull View view) {
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

        EditText editTextFromAccount = view.findViewById(R.id.editTextFromAccount);
        EditText editTextToAccount = view.findViewById(R.id.editTextToAccount);

        // add listeners for the editText object to behave like a spinner
        editTextFromAccount.setOnClickListener(new EditTextSpinnerClickListener(accountFromSpinner));
        editTextToAccount.setOnClickListener(new EditTextSpinnerClickListener(accountToSpinner));
        accountFromSpinner.setOnItemSelectedListener(new SpinnerEditTextItemSelectedListener(editTextFromAccount));
        accountToSpinner.setOnItemSelectedListener(new SpinnerEditTextItemSelectedListener(editTextToAccount));
    }

    private void initializeBucketComponent(@NonNull View view) {
        // Initialize the Bucket spinner
        Spinner bucketSpinner = (Spinner) view.findViewById(R.id.spinnerBucket);
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

        EditText editTextBucket = (EditText) view.findViewById(R.id.editTextBucket);

        // add listeners for the editText object to behave like a spinner
        editTextBucket.setOnClickListener(new EditTextSpinnerClickListener(bucketSpinner));
        bucketSpinner.setOnItemSelectedListener(new SpinnerEditTextItemSelectedListener(editTextBucket));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initializeTransactionTypesComponent(@NonNull View view) {
        // Initialize the Transaction Types radio group
        RadioGroup transactionTypeRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroupTransactionType);

        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);

        ArrayList<String> transactionList = new ArrayList<>();
        for(BaseModel transactionType : transactionTypeEntries) {
            int resourceId = getResources().getIdentifier(((TransactionType) transactionType).getResourceId(),"string", getContext().getPackageName());
            transactionList.add(getResources().getString(resourceId));

            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(resourceId);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginEnd((int) getResources().getDimension(R.dimen.text_margin));
            radioButton.setLayoutParams(layoutParams);
            transactionTypeRadioGroup.addView(radioButton);
        }
        transactionTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updatedMonthlyTotal();
                updatedYearlyTotal();
            }
        });
    }

    private void initializeFrequencyTypesComponent(@NonNull View view) {
        // Initialize the Frequency Types spinner
        Spinner frequencyTypeSpinner = (Spinner) view.findViewById(R.id.spinnerFrequency);

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

        EditText editTextFrequency = (EditText) view.findViewById(R.id.editTextFrequency);

        // add listeners for the editText object to behave like a spinner
        editTextFrequency.setOnClickListener(new EditTextSpinnerClickListener(frequencyTypeSpinner));
        frequencyTypeSpinner.setOnItemSelectedListener(new SpinnerEditTextItemSelectedListener(editTextFrequency));
    }

    private static class EditTextSpinnerClickListener implements View.OnClickListener {

        private final Spinner spinner;

        EditTextSpinnerClickListener(Spinner spinner) {
            this.spinner = spinner;
        }

        @Override
        public void onClick(View v) {
            spinner.performClick();
        }
    }

    private class SpinnerEditTextItemSelectedListener implements AdapterView.OnItemSelectedListener {

        private final EditText editText;

        SpinnerEditTextItemSelectedListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String spinnerString = (String) parent.getAdapter().getItem(position);

            if(parent.getId() == R.id.spinnerFrequency) {
                updatedMonthlyTotal();
                updatedYearlyTotal();
            }

            editText.setText(spinnerString);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // not currently being used
        }
    }

    enum TimePeriod {
        MONTHLY,
        YEARLY
    }

    public double calculateTotal(double amount, TimePeriod timePeriod) {
        Spinner frequencyTypeSpinner = (Spinner) getActivity().findViewById(R.id.spinnerFrequency);
        List<? extends BaseModel> frequencyTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(FrequencyType.class);
        FrequencyType frequencyType = (FrequencyType) frequencyTypeEntries.get(frequencyTypeSpinner.getSelectedItemPosition());

        RadioGroup transactionTypeRadioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroupTransactionType);
        List<? extends BaseModel> transactionTypeEntries = (List<? extends BaseModel>) databaseHandler.getAllEntries(TransactionType.class);
        @SuppressLint("ResourceType") TransactionType transactionType = (TransactionType) transactionTypeEntries.get(transactionTypeRadioGroup.getCheckedRadioButtonId() - 1);

        double total = (transactionType.getName().equals("debit")) ? -1.0 : 1.0;
        double ratio = (timePeriod == TimePeriod.MONTHLY) ? frequencyType.getMonthlyRatio() : frequencyType.getYearlyRatio();
        return total * amount * ratio;
    }

    public double calculateMonthlyTotal(double amount) {
        return calculateTotal(amount, TimePeriod.MONTHLY);
    }

    public double calculateYearlyTotal(double amount) {
        return  calculateTotal(amount, TimePeriod.YEARLY);
    }

    public void updatedMonthlyTotal() {
        RadioGroup transactionTypeRadioGroup = getActivity().findViewById(R.id.radioGroupTransactionType);
        EditText editTextAmount = getActivity().findViewById(R.id.editTextAmount);
        EditText amountMonthly = getActivity().findViewById(R.id.amountMonthly);

        if(transactionTypeRadioGroup != null &&
                transactionTypeRadioGroup.getCheckedRadioButtonId() != -1 && editTextAmount != null
                && amountMonthly != null && !editTextAmount.getText().toString().isEmpty()) {
            double amount = Double.parseDouble(editTextAmount.getText().toString());

            Locale locale = getResources().getConfiguration().locale;
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            amountMonthly.setText(numberFormat.format(calculateMonthlyTotal(amount)));
        }
    }

    public void updatedYearlyTotal() {
        RadioGroup transactionTypeRadioGroup = getActivity().findViewById(R.id.radioGroupTransactionType);
        EditText editTextAmount = getActivity().findViewById(R.id.editTextAmount);
        EditText amountYearly = getActivity().findViewById(R.id.amountYearly);

        if(transactionTypeRadioGroup != null &&
                transactionTypeRadioGroup.getCheckedRadioButtonId() != -1 && editTextAmount != null
                && amountYearly != null && !editTextAmount.getText().toString().isEmpty()) {
            double amount = Double.parseDouble(editTextAmount.getText().toString());

            Locale locale = getResources().getConfiguration().locale;
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            amountYearly.setText(numberFormat.format(calculateYearlyTotal(amount)));
        }
    }
}