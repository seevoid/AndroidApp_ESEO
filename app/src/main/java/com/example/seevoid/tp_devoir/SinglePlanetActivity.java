package com.example.seevoid.tp_devoir;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seevoid.tp_devoir.communication.http.HttpError;
import com.example.seevoid.tp_devoir.communication.models.People;
import com.example.seevoid.tp_devoir.communication.models.Planet;
import com.example.seevoid.tp_devoir.communication.models.Vehicle;
import com.example.seevoid.tp_devoir.communication.remote.ApiService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seevoid on 16/01/18.
 */

public class SinglePlanetActivity extends AppCompatActivity {

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, SinglePlanetActivity.class);
    }

    private final ApiService apiService = ApiService.Builder.getInstance();

    private boolean firstLaunch = true;

    private ProgressBar loaderPlanet;

    private TextView planetNamePreciseTextView;
    private TextView planetClimatePreciseTextView;
    private TextView planetCreatedPreciseTextView;
    private TextView planetDiameterPreciseTextView;
    private TextView planetEditedPreciseTextView;
    private TextView planetGravityPreciseTextView;
    private TextView planetOrbitalPeriodPreciseTextView;
    private TextView planetPopulationPreciseTextView;
    private TextView planetRotationPeriodPreciseTextView;
    private TextView planetSurfaceWaterPreciseTextView;
    private TextView planetTerrainPreciseTextView;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_planet);

        initUi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initUi() {
        loaderPlanet = findViewById(R.id.loaderPlanet);
        loaderPlanet.setVisibility(View.VISIBLE);

        planetNamePreciseTextView = findViewById(R.id.planetNamePrecise);
        planetClimatePreciseTextView = findViewById(R.id.planetClimatePrecise);
        planetCreatedPreciseTextView = findViewById(R.id.planetCreatedPrecise);
        planetDiameterPreciseTextView = findViewById(R.id.planetDiameterPrecise);
        planetEditedPreciseTextView = findViewById(R.id.planetEditedPrecise);
        planetGravityPreciseTextView = findViewById(R.id.planetGravityPrecise);
        planetOrbitalPeriodPreciseTextView = findViewById(R.id.planetOrbitalPeriodPrecise);
        planetPopulationPreciseTextView = findViewById(R.id.planetPopulationPrecise);
        planetRotationPeriodPreciseTextView = findViewById(R.id.planetRotationPeriodPrecise);
        planetSurfaceWaterPreciseTextView = findViewById(R.id.planetSurfacePrecise);
        planetTerrainPreciseTextView = findViewById(R.id.planetTerrainPrecise);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus && firstLaunch) {

            apiService.readPlanet(Currents.getInstance().getCurrentIndexPlanet()).enqueue(new Callback<Planet>() {
                @Override
                public void onResponse(@NonNull final Call<Planet> call, @NonNull final Response<Planet> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(WikiActivity.this, response.body().name, Toast.LENGTH_LONG).show();
                            handleResponse(response);
                            //refreshButton.setEnabled(true);
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull final Call<Planet> call, @NonNull final Throwable t) {
                    t.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //refreshButton.setEnabled(true);
                            //Toast.makeText(WikiActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            firstLaunch = false;
        }
    }

    private void handleResponse(final Response<Planet> response) {
        if (response.isSuccessful()) {
            //Toast.makeText(CharacterActivity.this, response.body().name, Toast.LENGTH_SHORT).show();
            Planet planet = response.body();

            loaderPlanet.setVisibility(View.GONE);

            planetNamePreciseTextView.setText(" " + planet.getName());
            planetClimatePreciseTextView.setText(" " + planet.getClimate());
            planetCreatedPreciseTextView.setText(" " + planet.getCreated());
            planetDiameterPreciseTextView.setText(" " + planet.getDiameter());
            planetEditedPreciseTextView.setText(" " + planet.getEdited());
            planetGravityPreciseTextView.setText(" " + planet.getGravity());
            planetOrbitalPeriodPreciseTextView.setText(" " + planet.getOrbitalPeriod());
            planetPopulationPreciseTextView.setText(" " + planet.getPopulation());
            planetRotationPeriodPreciseTextView.setText(" " + planet.getRotationPeriod());
            planetSurfaceWaterPreciseTextView.setText(" " + planet.getSurfaceWater());
            planetTerrainPreciseTextView.setText(" " + planet.getTerrain());


        } else { // error HTTP
            try {
                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                //Toast.makeText(CharacterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (final IOException e) {
                e.printStackTrace();
                //Toast.makeText(CharacterActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
