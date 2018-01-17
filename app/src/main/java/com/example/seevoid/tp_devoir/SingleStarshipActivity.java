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
import com.example.seevoid.tp_devoir.communication.models.Starship;
import com.example.seevoid.tp_devoir.communication.remote.ApiService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seevoid on 16/01/18.
 */

public class SingleStarshipActivity extends AppCompatActivity{

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, SingleStarshipActivity.class);
    }

    private final ApiService apiService = ApiService.Builder.getInstance();

    private ProgressBar loaderStarship;

    private TextView starshipNamePreciseTextView;
    private TextView starshipModelPreciseTextView;
    private TextView starshipManufacturerPreciseTextView;
    private TextView starshipLenghtPreciseTextView;
    private TextView starshipMaxAtmospheringSpeedPreciseTextView;
    private TextView starshipCrewPreciseTextView;
    private TextView starshipCargoCapacityPreciseTextView;
    private TextView starshipConsumablesPreciseTextView;
    private TextView starshipClassPreciseTextView;

    private TextView starshipMGLTPreciseTextView;
    private TextView starshipCostInCreditsPreciseTextView;
    private TextView starshipHyperdriveRatingPreciseTextView;
    private TextView starshipPassengersPreciseTextView;
    private TextView starshipPilotsUrlsPreciseTextView;
    private TextView starshipStarshipClassPreciseTextView;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_starship);

        initUi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initUi() {
        loaderStarship = findViewById(R.id.loaderStarships);
        loaderStarship.setVisibility(View.VISIBLE);

        starshipNamePreciseTextView = findViewById(R.id.starshipNamePrecise);
        starshipModelPreciseTextView = findViewById(R.id.starshipModelPrecise);
        starshipManufacturerPreciseTextView = findViewById(R.id.starshipManufacturerPrecise);
        starshipLenghtPreciseTextView = findViewById(R.id.starshipLenghtPrecise);
        starshipMaxAtmospheringSpeedPreciseTextView = findViewById(R.id.starshipMaxAtmospheringSpeedPrecise);
        starshipCrewPreciseTextView = findViewById(R.id.starshipCrewPrecise);
        starshipCargoCapacityPreciseTextView = findViewById(R.id.starshipCargoCapacityPrecise);
        starshipConsumablesPreciseTextView = findViewById(R.id.starshipConsumablesPrecise);
        starshipClassPreciseTextView = findViewById(R.id.starshipClassPrecise);
        starshipMGLTPreciseTextView = findViewById(R.id.starshipMGLTPrecise);
        starshipCostInCreditsPreciseTextView = findViewById(R.id.starshipCostInCreditsPrecise);
        starshipHyperdriveRatingPreciseTextView = findViewById(R.id.starshipHyperdriveRatingPrecise);
        starshipPassengersPreciseTextView = findViewById(R.id.starshipPassengersPrecise);
        starshipPilotsUrlsPreciseTextView = findViewById(R.id.starshipPilotsPrecise);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            String index = String.valueOf(Currents.getInstance().getCurrentIndexStarship());
            Toast.makeText(SingleStarshipActivity.this,index, Toast.LENGTH_SHORT).show();
            apiService.readStarship(Currents.getInstance().getCurrentIndexStarship()).enqueue(new Callback<Starship>() {
                @Override
                public void onResponse(@NonNull final Call<Starship> call, @NonNull final Response<Starship> response) {
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
                public void onFailure(@NonNull final Call<Starship> call, @NonNull final Throwable t) {
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

    private void handleResponse(final Response<Starship> response) {
        if (response.isSuccessful()) {
            //Toast.makeText(CharacterActivity.this, response.body().name, Toast.LENGTH_SHORT).show();
            Starship starship = response.body();

            loaderStarship.setVisibility(View.GONE);

            starshipNamePreciseTextView.setText(" " + starship.getName());
            starshipModelPreciseTextView.setText(" " + starship.getModel());
            starshipManufacturerPreciseTextView.setText(" " + starship.getManufacturer());
            starshipLenghtPreciseTextView.setText(" " + starship.getLength());
            starshipMaxAtmospheringSpeedPreciseTextView.setText(" " + starship.getMaxAtmospheringSpeed());
            starshipCrewPreciseTextView.setText(" " + starship.getCrew());
            starshipCargoCapacityPreciseTextView.setText(" " + starship.getCargoCapacity());
            starshipConsumablesPreciseTextView.setText(" " + starship.getConsumables());
            starshipClassPreciseTextView.setText(" " + starship.getStarshipClass());
            starshipMGLTPreciseTextView.setText(" " + starship.getMglt());
            starshipCostInCreditsPreciseTextView.setText(" " + starship.getCostInCredits());
            starshipHyperdriveRatingPreciseTextView.setText(" " + starship.getHyperdriveRating());
            starshipPassengersPreciseTextView.setText(" " + starship.getPassengers());
            starshipPilotsUrlsPreciseTextView.setText(" " + starship.getPilotsUrls());




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
