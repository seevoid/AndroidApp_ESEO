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

import com.example.seevoid.tp_devoir.adapters.VehicleAdapter;
import com.example.seevoid.tp_devoir.communication.http.HttpError;
import com.example.seevoid.tp_devoir.communication.models.People;
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
 * Created by seevoid on 15/01/18.
 */

public class VehicleActivity extends AppCompatActivity {

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, VehicleActivity.class);
    }

    ApiService apiService = ApiService.Builder.getInstance();

    private ProgressBar loaderVehicles;

    private VehicleAdapter vehicleAdapter;

    private final List<Vehicle> listOfVehicles = new ArrayList<>();

    private ListView vehiclesListView;

    private boolean firstLaunched = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        loaderVehicles = findViewById(R.id.loaderVehicles);
        loaderVehicles.setVisibility(View.VISIBLE);

        vehiclesListView = findViewById(R.id.vehicleListView);

        vehicleAdapter = new VehicleAdapter(this, listOfVehicles, vehicleSelectedListener);
        //vehiclesListView.setAdapter(vehicleAdapter);

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
            apiService.getAllVehicles().enqueue(new Callback<SWModelList<Vehicle>>() {
                @Override
                public void onResponse(@NonNull final Call<SWModelList<Vehicle>> call, @NonNull final Response<SWModelList<Vehicle>> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleResponse(response);
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull final Call<SWModelList<Vehicle>> call, @NonNull final Throwable t) {
                    t.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VehicleActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            firstLaunched = false;
        }
    }

    private void handleResponse(final Response<SWModelList<Vehicle>> response) {
        if (response.isSuccessful()) {

            //Toast.makeText(VehicleActivity.this, String.valueOf(response.body().size()), Toast.LENGTH_SHORT).show();

            for (int i=1; i<response.body().size()+1; i++)
                createUIVehicle(i);


            loaderVehicles.setVisibility(View.GONE);
        } else { // error HTTP
            try {
                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                Toast.makeText(VehicleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (final IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUIVehicle(final int index) {
        apiService.readVehicle(index).enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(@NonNull final Call<Vehicle> call, @NonNull final Response<Vehicle> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            Vehicle vehicle = new Vehicle(response.body().name, index);
                            //vehicleAdapter.add(vehicle);
                            vehiclesListView.setAdapter(vehicleAdapter);
                            listOfVehicles.add(vehicle);

                        } else { // error HTTP
                            try {
                                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                                Toast.makeText(VehicleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (final IOException e) {
                                e.printStackTrace();
                                //Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }

            @Override
            public void onFailure(@NonNull final Call<Vehicle> call, @NonNull final Throwable t) {
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

    private final VehicleAdapter.OnVehicleSelectedListener vehicleSelectedListener = new VehicleAdapter.OnVehicleSelectedListener() {
        @Override
        public void handle(final Vehicle vehicle) {
            Currents.getInstance().setCurrentIndexVehicle(vehicle.identifier);
            startActivity(SingleVehicleActivity.getStartIntent(VehicleActivity.this));
        }
    };


}
