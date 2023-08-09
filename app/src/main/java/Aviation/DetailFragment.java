package Aviation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import Aviation.data_model.FlightTrack;
import algonquin.cst2335.finalproject.MyApplication;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.FragmentDetailBinding;

/**
 * fragment for show detail of flight
 */
public class DetailFragment extends Fragment {

    /**
     * The constant KEY_FLIGHT.
     */
// data key of flight information
    public static final String KEY_FLIGHT = "flight";
    /**
     * The constant KEY_SAVED.
     */
//data key for indicating if this flight is saved
    public static final String KEY_SAVED = "saved";

    //current flight in view
    private FlightTrack currentFlightTrack;
    //flag indicate if view saved flight
    private boolean viewSaved = false;

    //view binding
    private FragmentDetailBinding binding;

    /**
     * Instantiates a new Detail fragment.
     */
    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * create instance of  DetailFragment
     *
     * @param flightTrack flight to show
     * @param saved       true means this flight has saved in database
     * @return DetailFragment detail fragment
     */
    public static DetailFragment newInstance(FlightTrack flightTrack, boolean saved) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_FLIGHT, flightTrack);
        args.putBoolean(KEY_SAVED, saved);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //read current flight
            currentFlightTrack = (FlightTrack) getArguments().getSerializable(KEY_FLIGHT);
            viewSaved = getArguments().getBoolean(KEY_SAVED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        binding = FragmentDetailBinding.bind(root);
        // Inflate the layout for this fragment
        showData(currentFlightTrack);

        if (viewSaved) {
            //already saved, hide save button
            binding.btnSave.setBackgroundColor(getResources().getColor(R.color.red,
                    requireActivity().getTheme()));
            binding.btnSave.setText(R.string.delete);
            binding.btnSave.setTextColor(getResources().getColor(R.color.white,
                    requireActivity().getTheme()));
            //click listener for delete flight
            binding.btnSave.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
                builder.setTitle(R.string.delete_flight);
                builder.setMessage(R.string.do_you_want_to_delete_this_flight_information);
                builder.setPositiveButton(R.string.delete, (dialog, which) -> {
                    //confirm
                    deleteFlight();
                });
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        } else {
            //not saved, hide save button
            binding.btnSave.setText(R.string.save);
            //set click listen for button
            binding.btnSave.setOnClickListener(view -> saveFlight());
        }
        //if already saved and not view saved, disable save button
        if (!viewSaved) {
            binding.btnSave.setEnabled(isSaved());
        }
        return binding.getRoot();
    }

    /**
     * delete current flight
     */
    private void deleteFlight() {
        try {
            Collection collection = MyApplication.getCollection();
            Query query = QueryBuilder.select(SelectResult.all())
                    .from(DataSource.collection(collection))
                    .where(Expression.property(MyApplication.KEY_ID)
                            .equalTo(Expression.string(getCurrentFlightID())))
                    .limit(Expression.number(1));
            ResultSet resultSet = query.execute();
            for (Result result : resultSet) {
                Dictionary dictionary = result.getDictionary(collection.getName());
                assert dictionary != null;
                String docID = dictionary.getString(MyApplication.KEY_DOC_ID);
                assert docID != null;
                Document document = collection.getDocument(docID);
                assert document != null;
                collection.delete(document);
            }
            resultSet.close();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        } finally {
            //back
            requireActivity().onBackPressed();
        }
    }

    /**
     * check if this flight is already saved
     *
     * @return true is saved
     */
    private boolean isSaved() {
        try {
            Collection collection = MyApplication.getCollection();
            Query query = QueryBuilder.select(SelectResult.all())
                    .from(DataSource.collection(collection))
                    .where(Expression.property(MyApplication.KEY_ID)
                            .equalTo(Expression.string(getCurrentFlightID())))
                    .limit(Expression.number(1));
            ResultSet resultSet = query.execute();
            boolean isSaved = resultSet.allResults().size() > 0;
            //close query
            resultSet.close();
            return !isSaved;
        } catch (CouchbaseLiteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * save flight to database
     */
    private void saveFlight() {
        if (isSaved()) {
            try {
                Collection collection = MyApplication.getCollection();
                //If not saved to database,save the flight
                MutableDocument mutableDocument = new MutableDocument();
                Gson gson = new Gson();
                String flightJson = gson.toJson(currentFlightTrack);
                mutableDocument.setString(MyApplication.KEY_DATA, flightJson);
                mutableDocument.setString(MyApplication.KEY_ID, getCurrentFlightID());
                collection.save(mutableDocument);
                String docID = mutableDocument.getId();
                //save doc id
                mutableDocument.setString(MyApplication.KEY_DOC_ID, docID);
                collection.save(mutableDocument);
                //save into database, show hint message and disable save button
                Snackbar.make(binding.getRoot().getContext(), binding.getRoot(),
                        getString(R.string.already_saved), Snackbar.LENGTH_SHORT).show();
                binding.btnSave.setEnabled(false);
            } catch (CouchbaseLiteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * get current flight id
     *
     * @return String
     */
    private String getCurrentFlightID() {
        return String.format("%s_%s", currentFlightTrack.getAirline().getName(),
                currentFlightTrack.getFlight().getNumber());
    }

    /**
     * fill data to view
     *
     * @param flightTrack FlightTrack
     */
    private void showData(FlightTrack flightTrack) {
        binding.tvDestination.setText(flightTrack.getArrival().getAirport());
        binding.tvTerminal.setText(flightTrack.getDeparture().getTerminal());
        binding.tvGate.setText(flightTrack.getDeparture().getGate());
        binding.tvDelay.setText(String.valueOf(flightTrack.getDeparture().getDelay()));
        if (flightTrack.getFlightDate() == null) {
            binding.tvDate.setText("");
        } else {
            binding.tvDate.setText(flightTrack.getFlightDate());
        }
        if (flightTrack.getAirline() == null) {
            binding.tvAirline.setText("");
        } else {
            binding.tvAirline.setText(flightTrack.getAirline().getName());
        }
        if (flightTrack.getFlight() == null) {
            binding.tvFlight.setText("");

        } else {
            binding.tvFlight.setText(flightTrack.getFlight().getNumber());
        }

        if (flightTrack.getAircraft() == null) {
            binding.tvAircraft.setText("");
        } else {
            binding.tvAircraft.setText(flightTrack.getAircraft().getIata());
        }
        if (flightTrack.getFlightStatus() == null) {
            binding.tvStatus.setText("");
        } else {
            binding.tvStatus.setText(flightTrack.getFlightStatus());
        }
    }
}