package Aviation;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import Aviation.data_model.FlightTrack;
import algonquin.cst2335.finalproject.R;

/**
 * Adapter for flight recyclerview
 */
public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.ViewHolder> {
    private final ArrayList<FlightTrack> flightList;
    private OnItemClickListener itemClickListener;


    /**
     * Instantiates a new Flight list adapter.
     */
    public FlightListAdapter() {
        flightList = new ArrayList<>();
    }

    /**
     * listener for item click
     */
    public interface OnItemClickListener {
        /**
         * On click.
         *
         * @param flightTrack the flight track
         */
        void onClick(FlightTrack flightTrack);
    }

    /**
     * set item click listener
     *
     * @param itemClickListener OnItemClickListener
     */
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * add new flights into list
     *
     * @param flightTracks ArrayList<FlightTrack> list of new flights
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setFlightList(ArrayList<FlightTrack> flightTracks) {
        flightList.clear();
        flightList.addAll(flightTracks);
        notifyDataSetChanged();
    }

    /**
     * export flight list
     *
     * @return ArrayList<FlightTrack>  flight list
     */
    public ArrayList<FlightTrack> getFlightList() {
        return flightList;
    }

    /**
     * clear data
     */
    public void clear() {
        flightList.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_flight_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlightTrack flightTrack = flightList.get(position);
        holder.bind(flightTrack);
        //set item click listener
        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.onClick(flightTrack);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    /**
     * View holder of item in recyclerview
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Tv date.
         */
//textview for show information of flight
        TextView tvDate, /**
         * The Tv airline.
         */
        tvAirline, /**
         * The Tv flight.
         */
        tvFlight, /**
         * The Tv aircraft.
         */
        tvAircraft, /**
         * The Tv status.
         */
        tvStatus;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAirline = itemView.findViewById(R.id.tv_airline);
            tvFlight = itemView.findViewById(R.id.tv_flight);
            tvAircraft = itemView.findViewById(R.id.tv_aircraft);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }

        /**
         * bind data with view
         *
         * @param flightTrack information of flight
         */
        public void bind(FlightTrack flightTrack) {
            if (flightTrack.getFlightDate() == null) {
                tvDate.setText(R.string.empty);
            } else {
                tvDate.setText(flightTrack.getFlightDate());
            }
            if (flightTrack.getAirline() == null) {
                tvAirline.setText(R.string.empty);
            } else {
                tvAirline.setText(flightTrack.getAirline().getName());
            }
            if (flightTrack.getFlight() == null) {
                tvFlight.setText(R.string.empty);
            } else {
                tvFlight.setText(flightTrack.getFlight().getNumber());
            }

            if (flightTrack.getAircraft() == null) {
                tvAircraft.setText(R.string.empty);
            } else {
                tvAircraft.setText(flightTrack.getAircraft().getIata());
            }
            if (flightTrack.getFlightStatus() == null) {
                tvStatus.setText(R.string.empty);
            } else {
                tvStatus.setText(flightTrack.getFlightStatus());
            }
        }
    }
}
