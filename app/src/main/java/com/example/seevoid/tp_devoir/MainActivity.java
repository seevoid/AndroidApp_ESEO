package com.example.seevoid.tp_devoir;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AnimationDrawable swapiAnimation;
    private Button goButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goButton = findViewById(R.id.goButton);
        goButton.setOnClickListener(onGoWikiButtonClicked);
        Typeface font = Typeface.createFromAsset(getAssets(), "SF_Distant_Galaxy_AltOutline.ttf");
        goButton.setTypeface(font);

        ImageView swapiImg = (ImageView) findViewById(R.id.swapiImg);
        swapiImg.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.animation_img));
        swapiAnimation = (AnimationDrawable)swapiImg.getDrawable();



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            swapiAnimation.start();
        }
    }

    /**
     * Go to wiki activity
     */
    private final View.OnClickListener onGoWikiButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            startActivity(MenuActivity.getStartIntent(MainActivity.this)); // start wiki activity

        }
    };

}
