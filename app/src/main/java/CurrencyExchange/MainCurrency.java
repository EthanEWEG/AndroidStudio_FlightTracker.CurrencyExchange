package CurrencyExchange;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicInteger;

import Aviation.MainAviation;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.CurrencyScreenBinding;
import algonquin.cst2335.finalproject.databinding.CurrencySavedBinding;

/**
 * This class represents the main currency conversion screen of the CurrencyExchange app.
 * It allows users to convert currencies, view conversion details, and manage saved conversions.
 */
public class MainCurrency extends AppCompatActivity {

    /**
     *     RecyclerView Adapter for currency conversions
      */
    private RecyclerView.Adapter myAdapter;
    /**
     *     // Binding object for the CurrencyScreen layout
     */
    CurrencyScreenBinding binding;

    /**
     *     // ViewModel for handling currency conversion data
     */
    CurrencyViewModel currencyModel;

    /**
     *     // List of currency conversion objects
     */
    private ArrayList<Currency> currencies;

    /**
     *     // Data Access Object for interacting with the Currency Room database
     */
    private CurrencyDAO cDAO;

    /**
     *     // Strings to hold current "From" and "To" currencies, and the amount for conversion
      */
    protected String curFrom;
    protected String curTo;
    protected String curAmt;

    /**
     *     // RequestQueue to handle API requests using Volley
     */
    protected RequestQueue queue = null;

    /**
     *     // Position of the selected currency in the RecyclerView
      */
    private int selectedPosition = RecyclerView.NO_POSITION;

    /**
     * Initialize the options menu and inflate it with the specified menu resource.
     *
     * @param menu The options menu in which the items are placed.
     * @return Return true for the menu to be displayed, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        menu.findItem(R.id.delete).setVisible(false);
        return true;
    }

    /**
     * Callback method to handle action bar item clicks.
     *
     * @param item The menu item that was selected.
     * @return Return true if the item selection was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            // Handle the click on the "Home" menu item
            Intent intent = new Intent(this, algonquin.cst2335.finalproject.MainScreen.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planeIcon) {
            // Handle the click on the "Plane Icon" menu item
            Intent intent = new Intent(this, MainAviation.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);
            return true;
        } else if (itemId == R.id.help) {
            // Handle the click on the "Help" menu item
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help Window")
                    .setMessage("This program will convert currencies for you." +
                            "\n" +
                            "Enter a currency and amount below to get started.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Positive button click action (if needed)
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return super.onOptionsItemSelected(item);
        } else {
            // Handle clicks on other menu items (if any)
            Toast.makeText(this, "Already on currency screen", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Key for the "From" currency used in SharedPreferences for storing the selected currency.
     */
    private static final String FROM_KEY = "FROM_KEY";

    /**
     * Key for the "To" currency used in SharedPreferences for storing the selected currency.
     */
    private static final String TO_KEY = "TO_KEY";

    /**
     * SharedPreferences object for managing shared preferences related to currency exchange.
     */
    private SharedPreferences sharedPreferences;

    /**
     * EditText field for entering the "From" currency value.
     */
    private EditText from;

    /**
     * EditText field for entering the "To" currency value.
     */
    private EditText to;

    /**
     * EditText field for entering the amount to be converted.
     */
    private EditText amt;

    /**
     * Key for the initial "From" currency used in SharedPreferences for storing the initially selected currency.
     */
    private static final String FROM2_KEY = "FROM2_KEY";

    /**
     * Key for the initial "To" currency used in SharedPreferences for storing the initially selected currency.
     */
    private static final String TO2_KEY = "TO2_KEY";

    /**
     * Key for the initial amount used in SharedPreferences for storing the initially entered amount.
     */
    private static final String AMT_KEY = "AMT_KEY";


    /**
     * Called when the activity is starting or being recreated. This is where most of the initialization
     * and setup of the activity should happen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CurrencyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currencyModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        currencies = currencyModel.conversions.getValue();

        CurrencyDB db = Room.databaseBuilder(getApplicationContext(), CurrencyDB.class, "CurrencyDatabase").build();
        cDAO = db.currencyDAO();

        queue = Volley.newRequestQueue(this);

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Currency Exchange");

        //SHARED PREFERENCES START
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        from = findViewById(R.id.editFrom);
        to = findViewById(R.id.editTo);
        amt = findViewById(R.id.editAmount);

        String savedFrom2 = sharedPreferences.getString(FROM2_KEY, "");
        String savedTo2 = sharedPreferences.getString(TO2_KEY, "");
        String savedAmt = sharedPreferences.getString(AMT_KEY, "");
        from.setText(savedFrom2);
        to.setText(savedTo2);
        amt.setText(savedAmt);
        ViewTreeObserver viewTreeObserver = binding.getRoot().getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to avoid multiple calls
                binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Perform the click action on the button after the view hierarchy is fully laid out
                binding.buttonC.performClick();
                String savedFrom = sharedPreferences.getString(FROM_KEY, "");
                String savedTo = sharedPreferences.getString(TO_KEY, "");
                from.setText(savedFrom);
                to.setText(savedTo);
                amt.setText("");
            }
        });

        // Add TextWatcher to the EditText fields
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Save the current value of "From" EditText
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(FROM_KEY, editable.toString());
                editor.apply();
            }
        });

        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Save the current value of "To" EditText
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TO_KEY, editable.toString());
                editor.apply();
            }
        });
        //END OF SHARED PREFS PART


        currencyModel.selectedConversion.observe(this, (newMessageValue) -> {
            CurrencyDetailsFragment chatFragment = new CurrencyDetailsFragment( newMessageValue );
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.add(R.id.fragmentLocation, chatFragment);
            tx.commit();
            tx.addToBackStack("");
        });

        if (currencies == null) {
            currencyModel.conversions.setValue(currencies = new ArrayList<>());
        }

        class MyRowHolder extends RecyclerView.ViewHolder {

            TextView dateText;
            TextView curFromText;
            TextView curToText;
            TextView amtFromText;
            TextView amtToText;
            TextView outline;

            public MyRowHolder(View itemView) {
                super(itemView);

                itemView.setOnClickListener(clk ->{
                    int position = getAbsoluteAdapterPosition();

                    Currency selected = currencies.get(position);
                    currencyModel.selectedConversion.postValue(selected);

                    // Update the selected position
                    selectedPosition = position;
                });

                dateText = itemView.findViewById(R.id.date);
                curFromText = itemView.findViewById(R.id.CurBefore);
                curToText = itemView.findViewById(R.id.CurResult);
                amtFromText = itemView.findViewById(R.id.AmtBefore);
                amtToText = itemView.findViewById(R.id.AmtResult);
            }
        }

        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @Override
            public MyRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                CurrencySavedBinding binding = CurrencySavedBinding.inflate(inflater, parent, false);
                return new MyRowHolder(binding.getRoot());

            }

            @Override
            public void onBindViewHolder(MyRowHolder holder, int position) {
                Currency obj = currencies.get(position);

                holder.dateText.setText(obj.getDate());
                holder.curFromText.setText(obj.getFrom());
                holder.curToText.setText(obj.getTo());
                holder.amtFromText.setText(String.valueOf(obj.getAmountFrom()));
                holder.amtToText.setText(String.valueOf(obj.getAmountTo()));

            }

            @Override
            public int getItemCount() {
                return currencies.size();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.buttonC.setOnClickListener(click -> {
            curFrom = binding.editFrom.getText().toString();
            curTo = binding.editTo.getText().toString();
            curAmt = binding.editAmount.getText().toString();
            if (!curAmt.isEmpty()) {
                double curAmount = Double.parseDouble(curAmt);
                if (curAmount > 0) {
                } else {
                    curAmt = "1";
                }
            } else {
                curAmt = "1";
            }
            String url = null;
            try {
                url = "https://api.getgeoapi.com/v2/currency/convert?api_key=ada17e7b791ab4e85a5b7bdffb5623e74a8c2cc6&from="
                        + URLEncoder.encode(curFrom,"UTF-8") + "&to="
                        + URLEncoder.encode(curTo,"UTF-8") + "&amount="
                        + URLEncoder.encode(curAmt,"UTF-8") + "&format=json";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        double amtTo;
                        String JcurTo;

                        String date;
                        try {
                            date = response.getString("updated_date");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String JcurFrom;
                        try {
                            JcurFrom = response.getString("base_currency_code");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        double Jamount;
                        try {
                            Jamount = response.getDouble("amount");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        JSONObject rates = null;
                        try {
                            rates = response.getJSONObject("rates");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Iterator<String> keys = rates.keys();

                        if (rates.has(curTo)) {
                            JcurTo = keys.next();
                            try {
                                amtTo = rates.getJSONObject(JcurTo).getDouble("rate_for_amount");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Unsuccesful Conversion", Toast.LENGTH_SHORT).show();
                            });
                            return;
                        }

                        runOnUiThread( (  )  -> {

                            currencies.add(new Currency(0, JcurFrom, JcurTo, Jamount, amtTo,date));

                            myAdapter.notifyItemInserted(currencies.size()-1);
                            /* this code will clear the input fields.
                            binding.amountText.setText("");
                            binding.editFrom.setText("");
                            binding.editTo.setText("");
                             */
                            myAdapter.notifyDataSetChanged();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(FROM2_KEY, JcurFrom);
                            editor.putString(TO2_KEY, JcurTo);
                            editor.putString(AMT_KEY, String.valueOf(Jamount));
                            editor.apply();

                            Toast.makeText(this, "Succesful Conversion", Toast.LENGTH_SHORT).show();

                        });

                    },
                    (error) -> { runOnUiThread( (  )  -> {Toast.makeText(this, "Unsuccesful Conversion", Toast.LENGTH_SHORT).show();}); });
            queue.add(request);
        });

        /**
         * Sets an OnClickListener on the "Add" button in the CurrencyScreenBinding.
         * When the "Add" button is clicked, this method checks if a currency item is selected in the RecyclerView.
         * If a currency item is selected, it inserts the selected currency into the database using CurrencyDAO.
         * A toast message is displayed to indicate that the conversion has been added.
         */
        binding.Add.setOnClickListener(clk -> {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> {
                    Currency selectedCurrency = currencies.get(selectedPosition);
                    cDAO.insertMessage(new Currency(0, selectedCurrency.getFrom(), selectedCurrency.getTo(), selectedCurrency.getAmountFrom(), selectedCurrency.getAmountTo(), selectedCurrency.getDate()));
                });
                Toast.makeText(this, "You added the conversion", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Sets an OnClickListener on the "Delete" button in the CurrencyScreenBinding.
         * When the "Delete" button is clicked, this method checks if a currency item is selected in the RecyclerView.
         * If a currency item is selected, it deletes the selected currency from the database using CurrencyDAO.
         * The currency item is also removed from the currencies list, and the adapter is notified about the change.
         * A Snackbar message is displayed to indicate that the conversion has been deleted, and an "Undo" action is provided.
         * If the "Undo" action is clicked, the deleted conversion is reinserted into the database and added back to the currencies list.
         * The adapter is notified about the new item insertion.
         */
        binding.Delete.setOnClickListener(clk -> {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                Currency selectedCurrency = currencyModel.selectedConversion.getValue();

                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> {
                    cDAO.deleteMessage(selectedCurrency);
                    runOnUiThread(() -> {
                        currencies.remove(selectedCurrency);
                        myAdapter.notifyDataSetChanged();

                        Snackbar.make(binding.getRoot(), "You deleted conversion #"+ selectedCurrency.getId() , Snackbar.LENGTH_LONG )
                                .setAction("Undo", c -> {

                                    thread.execute(() -> {
                                        cDAO.insertMessage(new Currency(selectedCurrency.getId(), selectedCurrency.getFrom(), selectedCurrency.getTo(), selectedCurrency.getAmountFrom(), selectedCurrency.getAmountTo(), selectedCurrency.getDate()));
                                    });
                                    currencies.add(new Currency(selectedCurrency.getId(), selectedCurrency.getFrom(), selectedCurrency.getTo(), selectedCurrency.getAmountFrom(), selectedCurrency.getAmountTo(), selectedCurrency.getDate()));
                                    myAdapter.notifyItemInserted(currencies.size()-1);
                                    myAdapter.notifyDataSetChanged();
                                })
                                .show();
                    });
                });
                selectedPosition = RecyclerView.NO_POSITION;
            }
        });

        /**
         * Sets an OnClickListener on the "Update" button in the CurrencyScreenBinding.
         * When the "Update" button is clicked, this method fetches all currency items from the database using CurrencyDAO.
         * For each currency item fetched, it constructs a URL for API call to get updated conversion rates.
         * It makes a GET request to the API and updates the currency item with the received data, such as converted currency and amount.
         * The currency item is updated in the database and the currencies list with the new data.
         * If all the requests are completed, it clears the existing data in the currencies list and adds the updated data from the database.
         * The adapter is then notified about the changes, and a toast message is displayed to indicate that the conversions have been shown/updated.
         */
        binding.Update.setOnClickListener(clk -> {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<Currency> dbCurrencies = cDAO.getAllMessages();
                int numRequests = dbCurrencies.size();
                AtomicInteger completedRequests = new AtomicInteger(0);

                for (Currency currency : dbCurrencies) {
                    String curFrom2 = currency.getFrom();
                    String curTo2 = currency.getTo();
                    String curAmt2 = String.valueOf(currency.getAmountFrom());
                    String url = null;
                    try {
                        url = "https://api.getgeoapi.com/v2/currency/convert?api_key=ada17e7b791ab4e85a5b7bdffb5623e74a8c2cc6&from="
                                + URLEncoder.encode(curFrom2,"UTF-8") + "&to="
                                + URLEncoder.encode(curTo2,"UTF-8") + "&amount="
                                + URLEncoder.encode(curAmt2,"UTF-8") + "&format=json";
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }


                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response) -> {
                                double amtTo;
                                String JcurTo;

                                String date;
                                try {
                                    date = response.getString("updated_date");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String JcurFrom;
                                try {
                                    JcurFrom = response.getString("base_currency_code");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                double Jamount;
                                try {
                                    Jamount = response.getDouble("amount");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                JSONObject rates = null;
                                try {
                                    rates = response.getJSONObject("rates");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Iterator<String> keys = rates.keys();

                                if (rates.has(curTo2)) {
                                    JcurTo = keys.next();
                                    try {
                                        amtTo = rates.getJSONObject(JcurTo).getDouble("rate_for_amount");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(this, "Unsuccesful Conversion.", Toast.LENGTH_SHORT).show();
                                    });
                                    return;
                                }

                                runOnUiThread( (  )  -> {

                                    currency.setFrom(JcurFrom);
                                    currency.setTo(JcurTo);
                                    currency.setAmountFrom(Jamount);
                                    currency.setAmountTo(amtTo);
                                    currency.setDate(date);

                                    if (completedRequests.incrementAndGet() == numRequests) {
                                        // All requests are completed, update UI
                                        currencies.clear(); // Clear the existing data
                                        currencies.addAll(dbCurrencies); // Add the updated data from the database
                                        myAdapter.notifyDataSetChanged(); // Notify the adapter about the changes
                                        Toast.makeText(this, "Saved Conversions Shown/Updated", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            },
                            (error) -> {Toast.makeText(this, "Unsuccesful Conversion", Toast.LENGTH_SHORT).show();});
                    queue.add(request);
                }
            });
            Toast.makeText(this, "Saved Conversions Shown/Updated", Toast.LENGTH_SHORT).show();
        });
    }
}
