package com.fizyoteknoterapi.muhendis.bioprint;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AppDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        TextView aboutTextView = (TextView) findViewById(R.id.about_text_view);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView mAppBarHeading = (TextView) findViewById(R.id.appbarheading);

        mAppBarHeading.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mAppBarHeading.setText("KULLANMA TALİMATLARI");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aboutTextView.setTypeface(Typeface.createFromAsset(this.getAssets(), "pt_sans-web-wegular.ttf"));
        aboutTextView.setText(Html.fromHtml(getString(R.string.about_text)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.home:
                Intent launchNewIntent = new Intent(this,MainScreen.class);
                startActivityForResult(launchNewIntent, 0);
                return true;
        }
        return false;
    }

}
