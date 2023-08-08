package CurrencyExchange;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class for managing currency conversions and selected conversion details.
 */
public class CurrencyViewModel extends ViewModel {

    /**
     * LiveData holding the list of currency conversions.
     */
    public MutableLiveData<ArrayList<Currency>> conversions = new MutableLiveData<>();

    /**
     * LiveData holding the selected currency conversion details.
     */
    public MutableLiveData<Currency> selectedConversion = new MutableLiveData<>();

}