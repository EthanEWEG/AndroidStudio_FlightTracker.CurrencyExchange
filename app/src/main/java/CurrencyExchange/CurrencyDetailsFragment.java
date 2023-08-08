package CurrencyExchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.CurrencyDetailsLayoutBinding;

public class CurrencyDetailsFragment extends Fragment{

    Currency selected;

    public CurrencyDetailsFragment(Currency c) { selected = c; }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        CurrencyDetailsLayoutBinding binding = CurrencyDetailsLayoutBinding.inflate(inflater);

        binding.CurFrom.setText( selected.from );
        binding.CurTo.setText( selected.to );
        binding.CurFromAmt.setText( String.valueOf(selected.amountFrom) );
        binding.CurToAmt.setText( String.valueOf(selected.amountTo) );
        binding.date.setText( String.valueOf(selected.date) );


        return binding.getRoot();
    }

}
