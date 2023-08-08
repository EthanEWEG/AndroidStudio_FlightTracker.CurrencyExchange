package CurrencyExchange;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CurrencyViewModel extends ViewModel{

    public MutableLiveData<ArrayList<Currency>> conversions = new MutableLiveData<>();
    public MutableLiveData<Currency> selectedConversion = new MutableLiveData<>();

}
