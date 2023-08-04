package CurrencyExchange;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import algonquin.cst2335.finalproject.MainScreen;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.CurrencyScreenBinding;

public class MainCurrency extends AppCompatActivity {

    CurrencyScreenBinding binding;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home){

            Intent intent = new Intent(this, algonquin.cst2335.finalproject.MainScreen.class);
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
        else if (itemId == R.id.help){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help Window")
                    .setMessage("This program will convert currencies for you." +
                            "\n" +
                            "Enter a currency and amount below to get started.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Positive button click action (if needed)
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Negative button click action (if needed)
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return super.onOptionsItemSelected(item);

        }
        else {
            Toast.makeText(this, "Already on currency screen", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CurrencyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Currency Exchange");
    }
}
