package com.darkspacelab.kassssprzak;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GlownaActivity extends AppCompatActivity {
    private SilnikRenderujacy mContentView;
    private View mControlsView;
    private Button mLewy;
    private Button mPrawy;
    private TextView mInfo;

    private int mSampleDurationTime = 1000; // 1 sec
    private boolean continueToRun = true;

    Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {

        //...
        public void run() {

            // do your stuff here, like update
            // this block of code you going to reach every  second
            mContentView.aktualizacja();

            if(mContentView.czyPrzegrana() == false){
                mHandler.postDelayed(mRunnable, mSampleDurationTime);
                mInfo.setText("Wynik: " + mContentView.ilePuntkow());
            } else {
                mInfo.setText("PRZEGRANA!!! " + mInfo.getText());
            }
            mInfo.invalidate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_glowna);

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (SilnikRenderujacy) findViewById(R.id.fullscreen_content);
        mPrawy = (Button) findViewById(R.id.prawy);
        mLewy = (Button) findViewById(R.id.lewy);
        mInfo = (TextView) findViewById(R.id.timer);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        //        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);




        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentView.resetujGra();
                mHandler.postDelayed(mRunnable, mSampleDurationTime);
            }
        });
        // Set up the user interaction to manually show or hide the system UI.
        mPrawy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentView.wPrawo();
            }
        });
        // Set up the user interaction to manually show or hide the system UI.
        mLewy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentView.wLewo();
            }
        });
    }
}
