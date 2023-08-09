package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import Aviation.MainAviation;
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

            Intent intent = new Intent(this, MainAviation.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);

            return true;
        }
        else if (itemId == R.id.help){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help Window")
                    .setMessage("Chose one of the programs below to get started")
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

            Intent intent = new Intent(this, MainAviation.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);

        });

    }
}