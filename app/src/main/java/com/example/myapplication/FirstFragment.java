package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.DatabaseHandler;
import com.example.myapplication.db.models.Account;
import com.example.myapplication.db.models.BaseModel;
import com.example.myapplication.models.ListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FirstFragment extends Fragment {

    DatabaseHandler databaseHandler;
    LayoutInflater inflater;
    ViewGroup container;
    TableViewAdapter adapter;
    List<? extends BaseModel> dbList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databaseHandler = new DatabaseHandler(getContext());
        this.inflater = inflater;
        this.container = container;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbList = databaseHandler.getAllDetailedEntries(Account.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewDeliveryProductList);

        TableViewAdapter tableViewAdapter = new TableViewAdapter(getContext(), dbList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        StickHeaderItemDecoration stickHeaderDecoration = new StickHeaderItemDecoration(tableViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tableViewAdapter);
        recyclerView.addItemDecoration(stickHeaderDecoration);

        RecyclerView recyclerViewId = view.findViewById(R.id.recyclerViewColumnId);

        TableViewFirstColumnAdapter tableViewAdapterId = new TableViewFirstColumnAdapter(getContext(), dbList);

        LinearLayoutManager linearLayoutManagerId = new LinearLayoutManager(getContext());
        StickHeaderItemDecoration stickHeaderDecorationId = new StickHeaderItemDecoration(tableViewAdapterId);
        recyclerViewId.setLayoutManager(linearLayoutManagerId);
        recyclerViewId.setAdapter(tableViewAdapterId);
        recyclerViewId.addItemDecoration(stickHeaderDecorationId);


        final RecyclerView.OnScrollListener[] scrollListeners = new RecyclerView.OnScrollListener[2];
        scrollListeners[0] = new RecyclerView.OnScrollListener( )
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                recyclerViewId.removeOnScrollListener(scrollListeners[1]);
                recyclerViewId.scrollBy(dx, dy);
                recyclerViewId.addOnScrollListener(scrollListeners[1]);
            }
        };
        scrollListeners[1] = new RecyclerView.OnScrollListener( )
        {
            @Override
            public void onScrolled(RecyclerView recyclerViewBy, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.removeOnScrollListener(scrollListeners[0]);
                recyclerView.scrollBy(dx, dy);
                recyclerView.addOnScrollListener(scrollListeners[0]);
            }
        };
        recyclerView.addOnScrollListener(scrollListeners[0]);
        recyclerViewId.addOnScrollListener(scrollListeners[1]);


        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        ((MainActivity) getActivity()).setCurrentSession(MainActivity.NO_SECTION);
    }

    private String formatAmount(double amount, boolean isDebit) {
        String formattedString;
        if(isDebit) {
            formattedString = String.format(getResources().getString(R.string.format_money), amount);
        } else {
            formattedString = String.format(getResources().getString(R.string.format_money), amount);
        }
        return formattedString;
    }
}