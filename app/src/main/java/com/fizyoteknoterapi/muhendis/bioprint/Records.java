package com.fizyoteknoterapi.muhendis.bioprint;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Records extends AppCompatActivity {

    FeedReaderContract.FeedEntry.FeedReaderDbHelper mDbHelper;
    final String EXTRA_MESSAGE="com.example.muhendis.bioprint";
    TextView mAppBarHeading;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

       /* if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        mAppBarHeading = (TextView) findViewById(R.id.appbarheading);

        mAppBarHeading.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mAppBarHeading.setText("KAYITLAR");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //final Drawable upArrow = getResources().getDrawable(R.drawable.back_arrow2);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //getSupportActionBar().show();


        setContent();


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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setContent()
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.recordsLinearLayout);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity= Gravity.CENTER;
        lp.setMargins(15,15,15,15);

       /* for (int i = 0; i < 10; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //for (int j = 0; j < 4; j++ ){
                Button btnTag = new Button(this);
                btnTag.setLayoutParams(lp);
                btnTag.setText("Button " + (i));
                btnTag.setId(i);
                row.addView(btnTag);
            //}

            layout.addView(row);
        }*/


        //DB connection
        mDbHelper = new FeedReaderContract.FeedEntry.FeedReaderDbHelper(getApplicationContext());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
        //FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_DATE+ " DESC"                                 // The sort order
        );



        while(cursor.moveToNext()) {
            String name=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME));
            String surname=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME));
            String data=name+" "+surname;
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //for (int j = 0; j < 4; j++ ){
            Button btnTag = new Button(this);
            btnTag.setLayoutParams(lp);
            btnTag.setText(data);
            btnTag.setBackground(getDrawable(R.drawable.mybutton2));
            btnTag.setTextColor(Color.WHITE);
            btnTag.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
            btnTag.setTextSize(24);
            final int rowID=cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID);
            btnTag.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("BTN ID",""+v.getId());
                    Intent intent=new Intent(Records.this,RecordsDetails.class);
                    intent.putExtra(EXTRA_MESSAGE, (Integer.toString(v.getId())));
                    startActivityForResult(intent,1);
                }
            });
            row.addView(btnTag);
            //}

            layout.addView(row);


        }
        cursor.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("ACTIVITY FINISH","bitti");
        finish();
        startActivity(getIntent());
        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                finish();
                startActivity(getIntent());
            }
            if (resultCode == RESULT_CANCELED) {

                finish();
                startActivity(getIntent());
            }
        }
    }//onActivityResult





}
