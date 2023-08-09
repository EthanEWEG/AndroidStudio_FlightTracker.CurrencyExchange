package Aviation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import Aviation.data_model.FlightTrack;
import algonquin.cst2335.finalproject.MyApplication;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.FragmentShowSavedBinding;

/**
 * Fragment to show saved flight
 */
public class ShowSavedFragment extends Fragment {
    private FragmentShowSavedBinding binding;
    //adapter for flight list view
    private FlightListAdapter flightListAdapter;

    /**
     * Instantiates a new Show saved fragment.
     */
    public ShowSavedFragment() {
        // Required empty public constructor
    }

    /**
     * New instance show saved fragment.
     *
     * @return the show saved fragment
     */
    public static ShowSavedFragment newInstance() {
        return new ShowSavedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_show_saved, container, false);
        binding = FragmentShowSavedBinding.bind(root);
        //init and set adapter for flight list
        flightListAdapter = new FlightListAdapter();
        binding.savedFlightList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.savedFlightList.setAdapter(flightListAdapter);
        return binding.getRoot();
    }

    /**
     * load saved flight from database
     */
    private void loadSavedFlights() {
        Collection collection = MyApplication.getCollection();
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.collection(collection));
        try (ResultSet resultSet = query.execute()) {
            ArrayList<FlightTrack> savedFlights = new ArrayList<>();
            Gson gson = new Gson();
            //load flight
            for (Result result : resultSet.allResults()) {
                Dictionary dictionary = result.getDictionary(collection.getName());
                assert dictionary != null;
                String data = dictionary.getString(MyApplication.KEY_DATA);
                if (!Objects.equals(data, null)) {
                    FlightTrack flightTrack = gson.fromJson(data, FlightTrack.class);
                    savedFlights.add(flightTrack);
                }
            }
            //add to recyclerview
            flightListAdapter.setFlightList(savedFlights);
            //when item click,open detail fragment
            flightListAdapter.setItemClickListener(flightTrack -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                DetailFragment detailFragment = DetailFragment.newInstance(flightTrack, true);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container_saved_activity, detailFragment);
                transaction.addToBackStack("detail");
                transaction.commit();
            });
        } catch (CouchbaseLiteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //refresh data
        loadSavedFlights();
    }
}