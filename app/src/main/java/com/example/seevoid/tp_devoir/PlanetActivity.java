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

import com.example.seevoid.tp_devoir.adapters.PlanetAdapter;
import com.example.seevoid.tp_devoir.adapters.VehicleAdapter;
import com.example.seevoid.tp_devoir.communication.http.HttpError;
import com.example.seevoid.tp_devoir.communication.models.Planet;
import com.example.seevoid.tp_devoir.communication.models.SWModelList;
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

public class PlanetActivity extends AppCompatActivity {

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, PlanetActivity.class);
    }

    ApiService apiService = ApiService.Builder.getInstance();

    private ProgressBar loaderPlanets;

    private PlanetAdapter planetAdapter;

    private final List<Planet> listOfPlanets = new ArrayList<>();

    private ListView planetsListView;

    private boolean firstLaunched = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);

        loaderPlanets = findViewById(R.id.loaderPlanet);
        loaderPlanets.setVisibility(View.VISIBLE);

        planetsListView = findViewById(R.id.planetListView);

        planetAdapter = new PlanetAdapter(this, listOfPlanets, planetSelectedListener);

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
            apiService.getAllPlanets().enqueue(new Callback<SWModelList<Planet>>() {
                @Override
                public void onResponse(@NonNull final Call<SWModelList<Planet>> call, @NonNull final Response<SWModelList<Planet>> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleResponse(response);
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull final Call<SWModelList<Planet>> call, @NonNull final Throwable t) {
                    t.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlanetActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            firstLaunched = false;
        }
    }

    private void handleResponse(final Response<SWModelList<Planet>> response) {
        if (response.isSuccessful()) {

            //Toast.makeText(VehicleActivity.this, String.valueOf(response.body().size()), Toast.LENGTH_SHORT).show();

            for (int i=1; i<response.body().size()+1; i++)
                createUIPlanet(i);


            loaderPlanets.setVisibility(View.GONE);
        } else { // error HTTP
            try {
                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                Toast.makeText(PlanetActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (final IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUIPlanet(final int index) {
        apiService.readPlanet(index).enqueue(new Callback<Planet>() {
            @Override
            public void onResponse(@NonNull final Call<Planet> call, @NonNull final Response<Planet> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            Planet planet = new Planet(response.body().name, index);
                            //planetAdapter.add(planet);
                            planetsListView.setAdapter(planetAdapter);
                            listOfPlanets.add(planet);

                        } else { // error HTTP
                            try {
                                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                                Toast.makeText(PlanetActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (final IOException e) {
                                e.printStackTrace();
                                //Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }

            @Override
            public void onFailure(@NonNull final Call<Planet> call, @NonNull final Throwable t) {
                t.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(VehicleActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private final PlanetAdapter.OnPlanetSelectedListener planetSelectedListener = new PlanetAdapter.OnPlanetSelectedListener() {
        @Override
        public void handle(final Planet planet) {
            Currents.getInstance().setCurrentIndexPlanet(planet.identifier);
            startActivity(SinglePlanetActivity.getStartIntent(PlanetActivity.this));
        }
    };
}
