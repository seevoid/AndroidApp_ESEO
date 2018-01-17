package com.example.seevoid.tp_devoir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.seevoid.tp_devoir.adapters.StarshipAdapter;
import com.example.seevoid.tp_devoir.communication.http.HttpError;
import com.example.seevoid.tp_devoir.communication.models.SWModelList;
import com.example.seevoid.tp_devoir.communication.models.Starship;

import com.example.seevoid.tp_devoir.communication.models.Vehicle;
import com.example.seevoid.tp_devoir.communication.remote.ApiService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seevoid on 16/01/18.
 */

public class StarshipActivity extends AppCompatActivity {

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, StarshipActivity.class);
    }

    ApiService apiService = ApiService.Builder.getInstance();

    private ProgressBar loaderStarships;

    private StarshipAdapter starshipAdapter;

    private final List<Starship> listOfStarships = new ArrayList<>();

    private ListView starshipsListView;

    private boolean firstLaunched = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starship);

        loaderStarships = findViewById(R.id.loaderStarships);
        loaderStarships.setVisibility(View.VISIBLE);

        starshipsListView = findViewById(R.id.starshipListView);

        starshipAdapter = new StarshipAdapter(this, listOfStarships, starshipSelectedListener);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus && firstLaunched) {
            apiService.getAllStarships().enqueue(new Callback<SWModelList<Starship>>() {
                @Override
                public void onResponse(@NonNull final Call<SWModelList<Starship>> call, @NonNull final Response<SWModelList<Starship>> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleResponse(response);
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull final Call<SWModelList<Starship>> call, @NonNull final Throwable t) {
                    t.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StarshipActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            firstLaunched = false;
        }
    }

    private void handleResponse(final Response<SWModelList<Starship>> response) {
        if (response.isSuccessful()) {

            //Toast.makeText(StarshipActivity.this, String.valueOf(response.body().size()), Toast.LENGTH_SHORT).show();

            for (int i=1; i<response.body().size()+1; i++)
                createUIStarship(i);


            loaderStarships.setVisibility(View.GONE);
        } else { // error HTTP
            try {
                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                Toast.makeText(StarshipActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (final IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUIStarship(final int index) {
        apiService.readStarship(index).enqueue(new Callback<Starship>() {
            @Override
            public void onResponse(@NonNull final Call<Starship> call, @NonNull final Response<Starship> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            Starship starship = new Starship(response.body().name, index);
                            //starshipAdapter.add(starship);
                            starshipsListView.setAdapter(starshipAdapter);
                            listOfStarships.add(starship);

                        } else { // error HTTP
                            HttpError error = null;
                            try {
                                error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(StarshipActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            @Override
            public void onFailure(@NonNull final Call<Starship> call, @NonNull final Throwable t) {
                t.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(StarshipActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private final StarshipAdapter.OnStarshipSelectedListener starshipSelectedListener = new StarshipAdapter.OnStarshipSelectedListener() {
        @Override
        public void handle(final Starship starship) {
            Currents.getInstance().setCurrentIndexStarship(starship.identifier);
            startActivity(SingleStarshipActivity.getStartIntent(StarshipActivity.this));
        }
    };
}
