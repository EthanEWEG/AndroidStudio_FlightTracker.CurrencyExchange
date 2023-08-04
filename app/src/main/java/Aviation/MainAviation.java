package Aviation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.CurrencyScreenBinding;

public class MainAviation extends AppCompatActivity {

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
        else if (itemId == R.id.CurrencyButton){

            Intent intent = new Intent(this, CurrencyExchange.MainCurrency.class);
            // Add any extras if needed
            //intent.putExtra("message", "Hello");
            startActivity(intent);

            return true;
        }
        else {
            Toast.makeText(this, "Already on Aviation screen", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CurrencyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Aviation");
    }
}
