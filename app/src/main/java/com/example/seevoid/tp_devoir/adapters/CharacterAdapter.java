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

import java.util.List;

/**
 * Created by seevoid on 13/01/18.
 */

public class CharacterAdapter extends ArrayAdapter<People> {

    /**
     * Declare an inner interface to listen click event on device items
     */
    public interface OnCharacterSelectedListener {
        void handle(final People people);
    }

    private final OnCharacterSelectedListener onCharacterSelectedListener;

    public CharacterAdapter(@NonNull final Context context, final List<People> devices, final OnCharacterSelectedListener listener) {
        super(context, R.layout.people_list_item, devices);
        onCharacterSelectedListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View holder = convertView;
        if (convertView == null) {
            final LayoutInflater vi = LayoutInflater.from(getContext());
            holder = vi.inflate(R.layout.people_list_item, null);
        }

        final People people = getItem(position);
        if (people == null) {
            return holder;
        }

        // display the name
        final TextView characterName = holder.findViewById(R.id.characterName);
        if (characterName != null) {
            characterName.setText(people.name);
        }

        // When this device item is clicked, trigger the listener
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onCharacterSelectedListener != null) {
                    onCharacterSelectedListener.handle(people);
                }
            }
        });

        return holder;
    }


}
