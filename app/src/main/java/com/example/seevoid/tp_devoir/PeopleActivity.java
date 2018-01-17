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

import com.example.seevoid.tp_devoir.adapters.CharacterAdapter;
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

public class PeopleActivity extends AppCompatActivity{

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, PeopleActivity.class);
    }

    ApiService apiService = ApiService.Builder.getInstance();

    private ProgressBar loaderCharacters;

    private CharacterAdapter characterAdapter;

    private final List<People> listOfCharacters = new ArrayList<>();

    private ListView characterListView;

    private boolean firstLaunched = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        loaderCharacters = findViewById(R.id.loaderCharacters);
        loaderCharacters.setVisibility(View.VISIBLE);

        characterListView = findViewById(R.id.characterListView);

        characterAdapter = new CharacterAdapter(this, listOfCharacters, characterSelectedListener);

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
            apiService.getAllPeople().enqueue(new Callback<SWModelList<People>>() {
                @Override
                public void onResponse(@NonNull final Call<SWModelList<People>> call, @NonNull final Response<SWModelList<People>> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleResponse(response);
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull final Call<SWModelList<People>> call, @NonNull final Throwable t) {
                    t.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PeopleActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            firstLaunched = false;
        }
    }

    private void handleResponse(final Response<SWModelList<People>> response) {
        if (response.isSuccessful()) {

            for (int i=1; i<response.body().size()+1; i++)
                createUIPeople(i);
            loaderCharacters.setVisibility(View.GONE);
        } else { // error HTTP
            try {
                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                Toast.makeText(PeopleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (final IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUIPeople(final int index) {
        apiService.readCharacter(index).enqueue(new Callback<People>() {
            @Override
            public void onResponse(@NonNull final Call<People> call, @NonNull final Response<People> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            People people = new People(response.body().name, index);
                            //characterAdapter.add(people);
                            characterListView.setAdapter(characterAdapter);
                            listOfCharacters.add(people);

                        } else { // error HTTP
                            try {
                                final HttpError error = new Gson().fromJson(response.errorBody().string(), HttpError.class);
                                Toast.makeText(PeopleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (final IOException e) {
                                e.printStackTrace();
                                //Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NonNull final Call<People> call, @NonNull final Throwable t) {
                t.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeopleActivity.this, R.string.status_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private final CharacterAdapter.OnCharacterSelectedListener characterSelectedListener = new CharacterAdapter.OnCharacterSelectedListener() {
        @Override
        public void handle(final People people) {
            Currents.getInstance().setCurrentIndexPeople(people.identifier);
            startActivity(SinglePeopleActivity.getStartIntent(PeopleActivity.this));
        }
    };



}
