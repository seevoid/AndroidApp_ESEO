package com.example.seevoid.tp_devoir.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.seevoid.tp_devoir.R;
import com.example.seevoid.tp_devoir.communication.models.People;
import com.example.seevoid.tp_devoir.communication.models.Vehicle;

import java.util.List;

/**
 * Created by seevoid on 15/01/18.
 */

public class VehicleAdapter extends ArrayAdapter<Vehicle> {

    /**
     * Declare an inner interface to listen click event on device items
     */
    public interface OnVehicleSelectedListener {
        void handle(final Vehicle vehicle);
    }

    private final VehicleAdapter.OnVehicleSelectedListener onVehicleSelectedListener;

    public VehicleAdapter(@NonNull final Context context, final List<Vehicle> devices, final VehicleAdapter.OnVehicleSelectedListener listener) {
        super(context, R.layout.vehicle_list_item, devices);
        onVehicleSelectedListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View holder = convertView;
        if (convertView == null) {
            final LayoutInflater vi = LayoutInflater.from(getContext());
            holder = vi.inflate(R.layout.vehicle_list_item, null);
        }

        final Vehicle vehicle = getItem(position);
        if (vehicle == null) {
            return holder;
        }

        // display the name
        final TextView vehicleName = holder.findViewById(R.id.vehicleName);
        if (vehicleName != null) {
            vehicleName.setText(vehicle.name);
        }

        // When this device item is clicked, trigger the listener
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onVehicleSelectedListener != null) {
                    onVehicleSelectedListener.handle(vehicle);
                }
            }
        });

        return holder;
    }
}
