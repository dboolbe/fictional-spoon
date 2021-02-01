package com.example.myapplication.models.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHandler;
import com.example.myapplication.db.models.Account;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateUpdateAccountFragment extends Fragment {

    DatabaseHandler databaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler = new DatabaseHandler(getContext());

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_update_account, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextDescription = view.findViewById(R.id.editTextDescription);

        if(getArguments() != null) {
            Log.d(CreateUpdateAccountFragment.class.toString(),""+getArguments().get("id"));
            Integer id = getArguments().getInt("id");
            if (id != null && id >= 0) {
                Account account = (Account) databaseHandler.getEntry(id, Account.class);

                Log.d(CreateUpdateAccountFragment.class.toString(), account.toString());

                editTextName.setText(account.getName());
                editTextDescription.setText(account.getDescription());

                TextView textView = view.findViewById(R.id.title);
                textView.setText(R.string.update_account);

                Button button = view.findViewById(R.id.createEditButton);
                button.setText(R.string.update);
            }
        }

        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CreateUpdateAccountFragment.this)
                        .popBackStack();
            }
        });

        view.findViewById(R.id.createEditButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String description = editTextDescription.getText().toString();

                Account account = new Account(name, description);

                Log.d(CreateUpdateAccountFragment.class.toString(),"Account => " + account);

                if(getArguments() != null) {
                    account.setId(getArguments().getInt("id"));
                    Log.d(CreateUpdateAccountFragment.class.toString(), "" + getArguments().get("id"));
                }

                boolean status = account.getId() != null ? databaseHandler.update(account) : databaseHandler.create(account);

                Log.d(CreateUpdateAccountFragment.class.toString(),"Status: " + status);

                NavHostFragment.findNavController(CreateUpdateAccountFragment.this)
                        .popBackStack();
            }
        });
    }
}