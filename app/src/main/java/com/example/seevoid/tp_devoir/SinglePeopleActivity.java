package com.example.seevoid.tp_devoir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seevoid.tp_devoir.communication.http.HttpError;
import com.example.seevoid.tp_devoir.communication.models.People;
import com.example.seevoid.tp_devoir.communication.remote.ApiService;
import com.google.gson.Gson;
import android.widget.ProgressBar;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seevoid on 14/01/18.
 */

public class SinglePeopleActivity extends AppCompatActivity {

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, SinglePeopleActivity.class);
    }

    private final ApiService apiService = ApiService.Builder.getInstance();

    private ProgressBar loaderCharacter;

    private TextView characterNamePreciseTextView;
    private TextView characterBirthYearPreciseTextView;
    private TextView characterEyeColorPreciseTextView;
    private TextView characterGenderPreciseTextView;
    private TextView characterHairColorPreciseTextView;
    private TextView characterHeightPreciseTextView;
    private TextView characterMassPreciseTextView;
    private TextView characterSkinColorPreciseTextView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_people);

        initUi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initUi() {
        loaderCharacter = findViewById(R.id.loaderCharacter);
        loaderCharacter.setVisibility(View.VISIBLE);

        characterNamePreciseTextView = findViewById(R.id.characterNamePrecise);
        characterBirthYearPreciseTextView = findViewById(R.id.characterBirthYearPrecise);
        characterEyeColorPreciseTextView = findViewById(R.id.characterEyeColorPrecise);
        characterGenderPreciseTextView = findViewById(R.id.characterGenderPrecise);
        characterHairColorPreciseTextView = findViewById(R.id.characterHairColorPrecise);
        characterHeightPreciseTextView = findViewById(R.id.characterHeightPrecise);
        characterMassPreciseTextView = findViewById(R.id.characterMassPrecise);
        characterSkinColorPreciseTextView = findViewById(R.id.characterSkinColorPrecise);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            String index = String.valueOf(Currents.getInstance().getCurrentIndexPeople());
            Toast.makeText(SinglePeopleActivity.this,index, Toast.LENGTH_SHORT).show();
            apiService.readCharacter(Currents.getInstance().getCurrentIndexPeople()).enqueue(new Callback<People>() {
                @Override
                public void onResponse(@NonNull final Call<People> call, @NonNull final Response<People> response) {
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
                public void onFailure(@NonNull final Call<People> call, @NonNull final Throwable t) {
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

    private void handleResponse(final Response<People> response) {
        if (response.isSuccessful()) {
            //Toast.makeText(CharacterActivity.this, response.body().name, Toast.LENGTH_SHORT).show();
            People people = response.body();

            loaderCharacter.setVisibility(View.GONE);

            characterNamePreciseTextView.setText(" " + people.getName());
            characterBirthYearPreciseTextView.setText(" " + people.getBirthYear());
            characterEyeColorPreciseTextView.setText(" " + people.getEyeColor());
            characterGenderPreciseTextView.setText(" " + people.getGender());
            characterHairColorPreciseTextView.setText(" " + people.getHairColor());
            characterHeightPreciseTextView.setText(" " + people.getHeight());
            characterMassPreciseTextView.setText(" " + people.getMass());
            characterSkinColorPreciseTextView.setText(" " + people.getSkinColor());


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
