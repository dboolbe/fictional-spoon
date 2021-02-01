package com.example.myapplication.models.bucket;

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
import com.example.myapplication.db.models.Bucket;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateUpdateBucketFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_create_update_bucket, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextName = view.findViewById(R.id.editTextName);

        if(getArguments() != null) {
            Log.d(CreateUpdateBucketFragment.class.toString(),""+getArguments().get("id"));
            Integer id = getArguments().getInt("id");
            if (id != null && id >= 0) {
                Bucket bucket = (Bucket) databaseHandler.getEntry(id, Bucket.class);

                Log.d(CreateUpdateBucketFragment.class.toString(), bucket.toString());

                editTextName.setText(bucket.getName());

                TextView textView = view.findViewById(R.id.title);
                textView.setText(R.string.update_bucket);

                Button button = view.findViewById(R.id.createEditButton);
                button.setText(R.string.update);
            }
        }

        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CreateUpdateBucketFragment.this)
                        .popBackStack();
            }
        });

        view.findViewById(R.id.createEditButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(CreateUpdateBucketFragment.class.toString(),"Edit name: " + editTextName.getText());

                Bucket bucket = new Bucket(editTextName.getText().toString());

                if(getArguments() != null) {
                    bucket.setId(getArguments().getInt("id"));
                    Log.d(CreateUpdateBucketFragment.class.toString(), "" + getArguments().get("id"));
                }

                boolean status = bucket.getId() != null ? databaseHandler.update(bucket) : databaseHandler.create(bucket);

                Log.d(CreateUpdateBucketFragment.class.toString(),"Status: " + status);
                Log.d(CreateUpdateBucketFragment.class.toString(),"Bucket: " + bucket);

                NavHostFragment.findNavController(CreateUpdateBucketFragment.this)
                        .popBackStack();
            }
        });
    }
}