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

public class SingleVehicleActivity extends AppCompatActivity{

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, SingleVehicleActivity.class);
    }

    private final ApiService apiService = ApiService.Builder.getInstance();

    private ProgressBar loaderVehicle;

    private TextView vehicleNamePreciseTextView;
    private TextView vehicleModelPreciseTextView;
    private TextView vehicleManufacturerPreciseTextView;
    private TextView vehicleLenghtPreciseTextView;
    private TextView vehicleMaxAtmospheringSpeedPreciseTextView;
    private TextView vehicleCrewPreciseTextView;
    private TextView vehicleCargoCapacityPreciseTextView;
    private TextView vehicleConsumablesPreciseTextView;
    private TextView vehicleClassPreciseTextView;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_vehicle);

        initUi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initUi() {
        loaderVehicle = findViewById(R.id.loaderVehicles);
        loaderVehicle.setVisibility(View.VISIBLE);

        vehicleNamePreciseTextView = findViewById(R.id.vehicleNamePrecise);
        vehicleModelPreciseTextView = findViewById(R.id.vehicleModelPrecise);
        vehicleManufacturerPreciseTextView = findViewById(R.id.vehicleManufacturerPrecise);
        vehicleLenghtPreciseTextView = findViewById(R.id.vehicleLenghtPrecise);
        vehicleMaxAtmospheringSpeedPreciseTextView = findViewById(R.id.vehicleMaxAtmospheringSpeedPrecise);
        vehicleCrewPreciseTextView = findViewById(R.id.vehicleCrewPrecise);
        vehicleCargoCapacityPreciseTextView = findViewById(R.id.vehicleCargoCapacityPrecise);
        vehicleConsumablesPreciseTextView = findViewById(R.id.vehicleConsumablesPrecise);
        vehicleClassPreciseTextView = findViewById(R.id.vehicleClassPrecise);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            String index = String.valueOf(Currents.getInstance().getCurrentIndexVehicle());
            Toast.makeText(SingleVehicleActivity.this,index, Toast.LENGTH_SHORT).show();
            apiService.readVehicle(Currents.getInstance().getCurrentIndexVehicle()).enqueue(new Callback<Vehicle>() {
                @Override
                public void onResponse(@NonNull final Call<Vehicle> call, @NonNull final Response<Vehicle> response) {
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
                public void onFailure(@NonNull final Call<Vehicle> call, @NonNull final Throwable t) {
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
        }
    }

    private void handleResponse(final Response<Vehicle> response) {
        if (response.isSuccessful()) {
            //Toast.makeText(CharacterActivity.this, response.body().name, Toast.LENGTH_SHORT).show();
            Vehicle vehicle = response.body();

            loaderVehicle.setVisibility(View.GONE);

            vehicleNamePreciseTextView.setText(" " + vehicle.getName());
            vehicleModelPreciseTextView.setText(" " + vehicle.getModel());
            vehicleManufacturerPreciseTextView.setText(" " + vehicle.getManufacturer());
            vehicleLenghtPreciseTextView.setText(" " + vehicle.getLength());
            vehicleMaxAtmospheringSpeedPreciseTextView.setText(" " + vehicle.getMaxAtmospheringSpeed());
            vehicleCrewPreciseTextView.setText(" " + vehicle.getCrew());
            vehicleCargoCapacityPreciseTextView.setText(" " + vehicle.getCargoCapacity());
            vehicleConsumablesPreciseTextView.setText(" " + vehicle.getConsumables());
            vehicleClassPreciseTextView.setText(" " + vehicle.getVehicleClass());



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
