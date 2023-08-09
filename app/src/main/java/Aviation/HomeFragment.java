package Aviation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.FragmentHomeBinding;

/**
 * Home fragment
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;


    //adapter for flight list view
    private FlightListAdapter flightListAdapter;

    private MainViewModel mainViewModel;

    /**
     * Instantiates a new Home fragment.
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * New instance home fragment.
     *
     * @return the home fragment
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get view model hold by activity
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.bind(root);
        binding.editTextCode.setText(mainViewModel.getLastAirportCode());

        //Set click listener for search button
        binding.btnSearch.setOnClickListener(view -> {
            search();
        });
        //hide loading
        showLoading(false);

        //init and set adapter for flight list
        flightListAdapter = new FlightListAdapter();
        binding.flightList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.flightList.setAdapter(flightListAdapter);

        //when item click,open detail fragment
        flightListAdapter.setItemClickListener(flightTrack -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            DetailFragment detailFragment = DetailFragment.newInstance(flightTrack, false);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack("detail");
            transaction.commit();
        });

        //observe data change when user perform a search
        mainViewModel.observeFlightList().observe(getViewLifecycleOwner(), flightTracks -> {
            flightListAdapter.setFlightList(flightTracks);
        });
        //observe loading state change
        mainViewModel.observeShowLoading().observe(getViewLifecycleOwner(), this::showLoading);
        //observe error message
        mainViewModel.observeShowErrorMsg().observe(getViewLifecycleOwner(), s -> {
            if (s.length() > 0) {
                showError(s);
            }
        });

        //refresh data when list is pull-down
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            search();
        });
        // Add a pull-up to load more listeners
        binding.flightList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                // check if reach bottom
                if (lastVisibleItemPosition == (totalItemCount - 1)) {
                    //read current iata code of airport
                    String currentInput = binding.editTextCode.getText().toString();
                    if (checkInput(currentInput)) {
                        //search if airport code is valid
                        String airportCode = binding.editTextCode.getText().toString();
                        mainViewModel.loadMoreData(airportCode, getContext());
                    }

                }
            }
        });

        return binding.getRoot();
    }

    /**
     * search by airport iata code
     */
    private void search() {
        //clear current data
        flightListAdapter.clear();
        //read current iata code of airport
        String currentInput = binding.editTextCode.getText().toString();
        if (checkInput(currentInput)) {
            //search if airport code is valid
            String airportCode = binding.editTextCode.getText().toString();
            mainViewModel.search(airportCode, getContext(), 0);
        } else {
            //show warn message
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.hint);
            builder.setMessage(R.string.invalid_airport_code_please_3_letter_code);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                // ok button clicked,dismiss
                dialog.dismiss();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * show or hide loading
     *
     * @param show true is shown
     */
    private void showLoading(boolean show) {
        if (show) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.flightList.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.flightList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * check if user input is a valid airport code
     *
     * @param airportCode String
     * @return true is valid
     */
    private boolean checkInput(String airportCode) {
        String IATA_REGEX = "^[A-Z]{3}$";
        Pattern pattern = Pattern.compile(IATA_REGEX);
        return pattern.matcher(airportCode).matches();
    }

    /**
     * show error message
     */
    private void showError(String errorMsg) {
        // Show the request error
        if (errorMsg.length() > 0) {
            Snackbar.make(binding.editTextCode, errorMsg, Snackbar.LENGTH_SHORT).show();
        }
        mainViewModel.clearErrorMsg();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //save airport code when this fragment exit
        MainAviation mainActivity = (MainAviation) requireActivity();
        mainActivity.saveAirportCode(binding.editTextCode.getText().toString());
    }
}