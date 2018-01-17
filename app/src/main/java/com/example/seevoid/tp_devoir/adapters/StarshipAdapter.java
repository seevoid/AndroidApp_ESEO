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
import com.example.seevoid.tp_devoir.communication.models.Starship;
import com.example.seevoid.tp_devoir.communication.models.Starship;

import java.util.List;

/**
 * Created by seevoid on 16/01/18.
 */

public class StarshipAdapter extends ArrayAdapter<Starship> {

    /**
     * Declare an inner interface to listen click event on device items
     */
    public interface OnStarshipSelectedListener {
        void handle(final Starship starship);
    }

    private final StarshipAdapter.OnStarshipSelectedListener onStarshipSelectedListener;

    public StarshipAdapter(@NonNull final Context context, final List<Starship> devices, final StarshipAdapter.OnStarshipSelectedListener listener) {
        super(context, R.layout.starship_list_item, devices);
        onStarshipSelectedListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View holder = convertView;
        if (convertView == null) {
            final LayoutInflater vi = LayoutInflater.from(getContext());
            holder = vi.inflate(R.layout.starship_list_item, null);
        }

        final Starship starship = getItem(position);
        if (starship == null) {
            return holder;
        }

        // display the name
        final TextView starshipName = holder.findViewById(R.id.starshipName);
        if (starshipName != null) {
            starshipName.setText(starship.name);
        }

        // When this device item is clicked, trigger the listener
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onStarshipSelectedListener != null) {
                    onStarshipSelectedListener.handle(starship);
                }
            }
        });

        return holder;
    }
}
