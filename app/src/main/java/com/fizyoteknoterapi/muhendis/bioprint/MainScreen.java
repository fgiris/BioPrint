package com.fizyoteknoterapi.muhendis.bioprint;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    Button mTestButton,mInstructionsButton,mRecordsButton,mGonyometreButton;
    TextView mAppBarHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        mTestButton=(Button)findViewById(R.id.testButton);
        mInstructionsButton=(Button)findViewById(R.id.instructionsButton);
        mRecordsButton=(Button)findViewById(R.id.recordsButton);
        mAppBarHeading = (TextView) findViewById(R.id.appbarheading);
        mGonyometreButton= (Button)findViewById(R.id.gonyometreButton);

        mTestButton.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mInstructionsButton.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mRecordsButton.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mAppBarHeading.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mGonyometreButton.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));


        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainScreen.this,UserInfoScreen.class);
                startActivity(intent);
            }
        });

        mInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainScreen.this,AppDetails.class);
                startActivity(intent);
            }
        });

        mRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainScreen.this,Records.class);
                startActivity(intent);
            }
        });

        mGonyometreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainScreen.this,GonyometreActivity.class);
                startActivity(intent);
            }
        });


    }
}
