package com.example.flighttracker;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.flighttracker.data_model.FlightResponse;
import com.example.flighttracker.data_model.FlightTrack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

/**
 * View model for managing data in main activity
 */
public class MainViewModel extends ViewModel {
    /**
     * The constant LIMIT.
     */
//The amount of data returned by the server each time
    public static final int LIMIT = 20;
    //data offset
    private int offset = 0;
    /**
     * The constant API_URL.
     */
//URL of API
    public static final String API_URL = "http://api.aviationstack.com/v1/flights?" +
            "access_key=%s&dep_iata=%s&limit=%d&offset=%d";
    /**
     * The constant ACCESS_KEY.
     */
//access key of api
    public static final String ACCESS_KEY = "1d0b71d0b2f5cd881dc852bc985e87a6";
    //request queue of volley
    private RequestQueue requestQueue;
    private final MutableLiveData<ArrayList<FlightTrack>> flightTrackLiveData;
    //show loading
    private final MutableLiveData<Boolean> showLoading;
    //show error message
    private final MutableLiveData<String> showErrorMsg;

    private String lastAirportCode;

    //data pagination

    /**
     * Instantiates a new Main view model.
     */
    public MainViewModel() {
        flightTrackLiveData = new MutableLiveData<>(new ArrayList<>());
        showLoading = new MutableLiveData<>(false);
        showErrorMsg = new MutableLiveData<>("");
    }

    /**
     * Init request queue of volley
     *
     * @param requestQueue request queue of volley
     */
    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * set last airport code saved in sharedPreferences when activity start
     *
     * @param lastAirportCode String
     */
    public void setLastAirportCode(String lastAirportCode) {
        this.lastAirportCode = lastAirportCode;
    }

    /**
     * clear error message
     */
    public void clearErrorMsg() {
        showErrorMsg.setValue("");
    }

    /**
     * get last airport code saved in sharedPreferences
     *
     * @return the last airport code
     */
    public String getLastAirportCode() {
        return lastAirportCode;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Cancel all requests in the request queue when activity is destroyed
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }

    /**
     * refresh flight list
     *
     * @param flightTrackList ArrayList<FlightTrack> new list of flight response by server
     */
    public void setFlightTrackList(ArrayList<FlightTrack> flightTrackList) {
        flightTrackLiveData.postValue(flightTrackList);
    }

    /**
     * get current flight list
     *
     * @return ArrayList<FlightTrack>  flight track list
     */
    public ArrayList<FlightTrack> getFlightTrackList() {
        return flightTrackLiveData.getValue();
    }

    /**
     * set data offset
     *
     * @param offset the offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * get data offset
     *
     * @return offset offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * observe flight list change
     *
     * @return MutableLiveData<ArrayList < FlightTrack>  > current list of flight
     */
    public LiveData<ArrayList<FlightTrack>> observeFlightList() {
        return flightTrackLiveData;
    }

    /**
     * observe loading state change
     *
     * @return LiveData<Boolean>  live data
     */
    public LiveData<Boolean> observeShowLoading() {
        return showLoading;
    }

    /**
     * observe if there error message need to show
     *
     * @return LiveData<String>  live data
     */
    public LiveData<String> observeShowErrorMsg() {
        return showErrorMsg;
    }

    /**
     * search flight
     *
     * @param airportCode iata code of airport
     * @param context     the context
     * @param offset      the offset
     */
    public void search(String airportCode, Context context, int offset) {
        showLoading.postValue(true);
        String url = String.format(Locale.US, API_URL, ACCESS_KEY, airportCode, LIMIT, offset);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Request successful callback
                    // Handle the response data
                    Gson gson = new Gson();
                    FlightResponse res = gson.fromJson(response, FlightResponse.class);
                    ArrayList<FlightTrack> currentFlightList = flightTrackLiveData.getValue();
                    assert currentFlightList != null;
                    currentFlightList.addAll(res.getData());
                    flightTrackLiveData.setValue(currentFlightList);
                    //save pagination
                    //hide loading
                    showLoading.postValue(false);
                }, error -> {
            // Request error callback
            Log.e("Error", error.toString());
            //hide loading
            showLoading.postValue(false);
            //show network error
            showErrorMsg.postValue(context.getString(R.string.network_error));
        });
        // Add the request to the request queue
        requestQueue.add(stringRequest);
    }

    /**
     * request more data from server
     *
     * @param airportCode iata code of airport
     * @param context     context
     */
    public void loadMoreData(String airportCode, Context context) {
        offset += LIMIT;
        search(airportCode, context, offset);
    }
}
