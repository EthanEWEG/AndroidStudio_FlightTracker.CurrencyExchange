package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import CurrencyExchange.MainCurrency;
import algonquin.cst2335.finalproject.databinding.MainScreenBinding;

public class MainScreen extends AppCompatActivity {

    MainScreenBinding binding;

    /**
     * Adds menu items onto toolbar
     * @param menu my menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        menu.findItem(R.id.delete).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.currencyIcon){

            Intent intent = new Intent(this, CurrencyExchange.MainCurrency.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);

            return true;
        }
        else if (itemId == R.id.planeIcon){

            Intent intent = new Intent(this, Aviation.MainAviation.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);

            return true;
        }
        else {
            Toast.makeText(this, "Already on home screen", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Main Screen");

        binding.CurrencyButton.setOnClickListener(click -> {
            Intent intent = new Intent(this, CurrencyExchange.MainCurrency.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);
        });

        binding.PlaneButton.setOnClickListener(click -> {

            Intent intent = new Intent(this, Aviation.MainAviation.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);

        });

    }
}