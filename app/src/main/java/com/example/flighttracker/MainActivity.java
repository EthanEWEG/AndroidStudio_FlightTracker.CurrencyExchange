package com.example.flighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.toolbox.Volley;
import com.example.flighttracker.data_model.FlightTrack;
import com.example.flighttracker.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The constant KEY_SHARED_PREF.
     */
//key of SharedPreferences
    public static final String KEY_SHARED_PREF = "my_preferences";
    /**
     * The constant KEY_SEARCH_TERM.
     */
//key for saving search term
    public static final String KEY_SEARCH_TERM = "search_term";
    /**
     * The constant KEY_LOCALE.
     */
    public static final String KEY_LOCALE = "locale";
    /**
     * The constant KEY_FLIGHT_LIST.
     */
    public static final String KEY_FLIGHT_LIST = "flight list";
    /**
     * The constant KEY_DATA_OFFSET.
     */
    public static final String KEY_DATA_OFFSET = "offset";

    // Time interval for double press to exit the app
    private static final int BACK_PRESS_INTERVAL = 2000;
    // Flag to track if back button has been pressed once
    private boolean doubleBackToExitPressedOnce = false;
    // Handler used to delay resetting the double press flag
    private Handler mHandler;


    /**
     * The Shared preferences.
     */
    SharedPreferences sharedPreferences;

    //view binding
    private ActivityMainBinding binding;

    /**
     * The constant ENGLISH.
     */
//language code
    public final static int ENGLISH = 0;
    /**
     * The constant ZH_CN.
     */
    public final static int ZH_CN = 1;

    //current language
    private int language = ENGLISH;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init toolbar
        setSupportActionBar(binding.toolbarMainActivity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Init Handler used to delay resetting the double press flag
        mHandler = new Handler();

        // Init main view model
        //view model hold by main activity
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //init request queue of volley
        mainViewModel.setRequestQueue(Volley.newRequestQueue(getApplicationContext()));

        //init sharedPreferences
        sharedPreferences = getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        //read last airport code saved in sharedPreferences
        mainViewModel.setLastAirportCode(sharedPreferences.getString(KEY_SEARCH_TERM, ""));

        //read language setting
        language = sharedPreferences.getInt(KEY_LOCALE, ENGLISH);
        changeLanguage(false);

        //reload flight list and data offset when activity restart
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<FlightTrack> flightTracks = (ArrayList<FlightTrack>) extras.getSerializable(KEY_FLIGHT_LIST);
            mainViewModel.setFlightTrackList(flightTracks);
            mainViewModel.setOffset(extras.getInt(KEY_DATA_OFFSET, 0));
        }
    }

    /**
     * show menu on toolbar
     *
     * @param menu Menu
     * @return true is shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle toolbar menu click
     *
     * @param item MenuItem
     * @return bool
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // exit app when back button pressed
            onBackPressed();
            return true;
        } else if (id == R.id.action_settings) {
            showSettingDialog();
            return true;
        } else if (id == R.id.action_help) {
            showHelpDialog();
            return true;
        } else if (id == R.id.action_view_saved) {
            // go to view saved flight
            Intent intent = new Intent(MainActivity.this, SavedActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment instanceof DetailFragment) {
            //if current show detail, just back to home fragment
            super.onBackPressed();
            return;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        // Set the flag to true indicating back button has been pressed once
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press_back_again_to_exit_the_app, Toast.LENGTH_SHORT).show();

        // Delay resetting the double press flag
        mHandler.postDelayed(() -> doubleBackToExitPressedOnce = false, BACK_PRESS_INTERVAL);
    }

    /**
     * save airport code in SharedPreferences
     *
     * @param airportCode the airport code
     */
    public void saveAirportCode(String airportCode) {
        //save search term
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SEARCH_TERM, airportCode);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save language setting
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LOCALE, language);
        editor.apply();
    }

    /**
     * show help dialog
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.help_content);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            //confirm
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * show setting dialog
     */
    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] options = new String[]{getString(R.string.english),
                getString(R.string.zhCN)};
        final int[] checkedItemIndex = {language};
        builder.setTitle(R.string.change_language)
                .setSingleChoiceItems(options, language, (dialog, which) -> checkedItemIndex[0] = which)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    language = checkedItemIndex[0];
                    //change language
                    changeLanguage(true);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * change language of application
     */
    private void changeLanguage(boolean byUser) {
        if (language == ENGLISH) {
            Locale localeEnglish = new Locale("en");
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.setLocale(localeEnglish);
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
        if (language == ZH_CN) {
            Locale localeEnglish = new Locale("zh");
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.setLocale(localeEnglish);
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
        //set title
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        //if switch by user, restart activity
        if (byUser) {
            ArrayList<FlightTrack> currentFlightList = mainViewModel.getFlightTrackList();
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_FLIGHT_LIST, currentFlightList);
            bundle.putInt(KEY_DATA_OFFSET, mainViewModel.getOffset());
            intent.putExtras(bundle);
            finish();
            startActivity(intent);
        }
    }
}