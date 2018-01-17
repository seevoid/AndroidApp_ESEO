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
import com.example.seevoid.tp_devoir.communication.models.Planet;
import com.example.seevoid.tp_devoir.communication.models.Vehicle;

import java.util.List;

/**
 * Created by seevoid on 16/01/18.
 */

public class PlanetAdapter extends ArrayAdapter<Planet> {

    /**
     * Declare an inner interface to listen click event on device items
     */
    public interface OnPlanetSelectedListener {
        void handle(final Planet planet);
    }

    private final PlanetAdapter.OnPlanetSelectedListener onPlanetSelectedListener;

    public PlanetAdapter(@NonNull final Context context, final List<Planet> devices, final PlanetAdapter.OnPlanetSelectedListener listener) {
        super(context, R.layout.planet_list_item, devices);
        onPlanetSelectedListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View holder = convertView;
        if (convertView == null) {
            final LayoutInflater vi = LayoutInflater.from(getContext());
            holder = vi.inflate(R.layout.planet_list_item, null);
        }

        final Planet planet = getItem(position);
        if (planet == null) {
            return holder;
        }

        // display the name
        final TextView planetName = holder.findViewById(R.id.planetName);
        if (planetName != null) {
            planetName.setText(planet.name);
        }

        // When this device item is clicked, trigger the listener
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onPlanetSelectedListener != null) {
                    onPlanetSelectedListener.handle(planet);
                }
            }
        });

        return holder;
    }
}
