package CurrencyExchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.CurrencyDetailsLayoutBinding;

/**
 * A fragment that displays the details of a currency conversion.
 */
public class CurrencyDetailsFragment extends Fragment {

    private Currency selected;

    /**
     * Constructor to create a new instance of the CurrencyDetailsFragment with the provided Currency object.
     *
     * @param c The Currency object containing the details of the currency conversion to be displayed.
     */
    public CurrencyDetailsFragment(Currency c) {
        selected = c;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        CurrencyDetailsLayoutBinding binding = CurrencyDetailsLayoutBinding.inflate(inflater);

        // Set the currency conversion details to the respective TextViews
        binding.CurFrom.setText(selected.from);
        binding.CurTo.setText(selected.to);
        binding.CurFromAmt.setText(String.valueOf(selected.amountFrom));
        binding.CurToAmt.setText(String.valueOf(selected.amountTo));
        binding.date.setText(selected.date);

        return binding.getRoot();
    }

}