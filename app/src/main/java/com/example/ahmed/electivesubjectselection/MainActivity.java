package com.example.ahmed.electivesubjectselection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hololo.tutorial.library.TutorialActivity;

public class MainActivity extends TutorialActivity {
    private static int SPLASH_TIME_OUT = 3500;
    Animation frombottom,fromtop;
    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.textView3);
        imageView = (ImageView)findViewById(R.id.imageView);
        fromtop =AnimationUtils.loadAnimation(this,R.anim.fromtop);
        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        tv.setAnimation(frombottom);
        imageView.setAnimation(fromtop);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                    startActivity(new Intent(MainActivity.this, OnBoardActivity.class));

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
