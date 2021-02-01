package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView manageBucketButton = findViewById(R.id.manageBucketButton);
        manageBucketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                NavOptions navOption = new NavOptions.Builder().setPopUpTo(R.id.readBucketFragment, false).build();
                NavHostFragment.findNavController(fragment).navigate(R.id.readBucketFragment,null,navOption);
            }
        });
        TextView manageAccountButton = findViewById(R.id.manageAccountButton);
        manageAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                NavHostFragment.findNavController(fragment).popBackStack(fragment.getId(),true);
                NavHostFragment.findNavController(fragment).navigate(R.id.readAccountFragment);
            }
        });
        TextView manageTransactionButton = findViewById(R.id.manageTransactionButton);
        manageTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                NavHostFragment.findNavController(fragment).popBackStack(fragment.getId(),true);
                NavHostFragment.findNavController(fragment).navigate(R.id.readTransactionFragment);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_buckets) {
//            getSupportFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out)
//                    .replace(R.id.nav_host_fragment,new ReadBucketFragment())
//                    .addToBackStack(null)
//                    .commit();
            Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
            NavHostFragment.findNavController(fragment).navigate(R.id.readBucketFragment,null, getNavOptions());
            return true;
        } else if (id == R.id.action_accounts) {
            Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
            NavHostFragment.findNavController(fragment).navigate(R.id.readAccountFragment,null, getNavOptions());
            return true;
        } else if (id == R.id.action_transactions) {
            Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
            NavHostFragment.findNavController(fragment).navigate(R.id.readTransactionFragment,null, getNavOptions());
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public NavOptions getNavOptions() {
        NavOptions navOptions = new NavOptions.Builder().setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.slide_in).setPopEnterAnim(R.anim.fade_out)
                .setPopExitAnim(R.anim.slide_out).build();

        return navOptions;
    }

    public static int NO_SECTION = -1;
    public static int MANAGE_BUCKET_SECTION = 0;
    public static int MANAGE_ACCOUNT_SECTION = 1;
    public static int MANAGE_TRANSACTION_SECTION = 2;

    private int currentSession = NO_SECTION;

    public int setCurrentSession(int currentSession) {
        this.currentSession = currentSession;
        TextView[] textViews = new TextView[3];
        textViews[MANAGE_BUCKET_SECTION] = findViewById(R.id.manageBucketButton);
        textViews[MANAGE_ACCOUNT_SECTION] = findViewById(R.id.manageAccountButton);
        textViews[MANAGE_TRANSACTION_SECTION] = findViewById(R.id.manageTransactionButton);

        for(int i = 0; i < textViews.length; i++) {
            TextView textView = textViews[i];
            if(currentSession == i) {
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setBackgroundResource(R.drawable.border_selected);
            } else {
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.drawable.border);
            }
        }

        return currentSession;
    }

    public int getCurrentSession() {
        return currentSession;
    }
}