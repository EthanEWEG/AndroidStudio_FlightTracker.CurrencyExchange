package com.example.flighttracker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flighttracker.databinding.ActivitySavedBinding;

import java.util.Objects;

/**
 * Activity to show saved flights
 */
public class SavedActivity extends AppCompatActivity {
    private ActivitySavedBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init toolbar
        setSupportActionBar(binding.toolbarSavedActivity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.saved_flight);

    }


    /**
     * Handle toolbar menu click
     *
     * @param item MenuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //back
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}