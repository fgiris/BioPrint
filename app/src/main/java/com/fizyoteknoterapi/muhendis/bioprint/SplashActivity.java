package com.fizyoteknoterapi.muhendis.bioprint;

/**
 * Created by muhendis on 28.10.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        startActivity(new Intent(SplashActivity.this, MainScreen.class));
        // close splash activity
        finish();
    }
}
