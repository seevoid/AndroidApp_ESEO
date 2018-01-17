package com.example.seevoid.tp_devoir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by seevoid on 15/01/18.
 */

public class MenuActivity extends AppCompatActivity {

    public static Intent getStartIntent(final Context ctx) {
        return new Intent(ctx, MenuActivity.class);
    }

    ImageButton vehiclesImageButton;
    ImageButton charactersImageButton;
    ImageButton planetsImageButton;
    ImageButton starshipsImageButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initUI();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initUI() {
        vehiclesImageButton = findViewById(R.id.vehiclesImageButton);
        vehiclesImageButton.setOnClickListener(onVehiclesImageButtonClicked);

        charactersImageButton = findViewById(R.id.charactersImageButton);
        charactersImageButton.setOnClickListener(onCharactersImageButtonClicked);

        planetsImageButton = findViewById(R.id.planetsImageButton);
        planetsImageButton.setOnClickListener(onPlanetsImageButtonClicked);

        starshipsImageButton = findViewById(R.id.starshipsImageButton);
        starshipsImageButton.setOnClickListener(onStarshipsImageButtonClicked);
    }

    private final View.OnClickListener onVehiclesImageButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            startActivity(VehicleActivity.getStartIntent(MenuActivity.this)); // start wiki activity

        }
    };

    private final View.OnClickListener onCharactersImageButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            startActivity(PeopleActivity.getStartIntent(MenuActivity.this)); // start wiki activity

        }
    };

    private final View.OnClickListener onPlanetsImageButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            startActivity(PlanetActivity.getStartIntent(MenuActivity.this)); // start wiki activity

        }
    };

    private final View.OnClickListener onStarshipsImageButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            startActivity(StarshipActivity.getStartIntent(MenuActivity.this)); // start wiki activity

        }
    };
}
