package com.fizyoteknoterapi.muhendis.bioprint;

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

public class RecordsDetails extends AppCompatActivity {

    FeedReaderContract.FeedEntry.FeedReaderDbHelper mDbHelper;
    final String EXTRA_MESSAGE="com.example.muhendis.bioprint";
    final String EXTRA_MESSAGE2="com.example.muhendis.bioprint2";
    String mHeight;
    SQLiteDatabase db;
    TextView mAppBarHeading;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_details);

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        mAppBarHeading = (TextView) findViewById(R.id.appbarheading);

        mAppBarHeading.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mAppBarHeading.setText("DETAYLAR");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayout layout = (LinearLayout) findViewById(R.id.recordsDetailsLinearLayout);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //lp.gravity= Gravity.CENTER;
        lp.setMargins(15,15,15,15);



        int rowID=Integer.parseInt(getIntent().getStringExtra(EXTRA_MESSAGE));
        Log.d("ROWID",""+rowID);
        //DB connection
        mDbHelper = new FeedReaderContract.FeedEntry.FeedReaderDbHelper(getApplicationContext());

        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_GENDER,
                FeedReaderContract.FeedEntry.COLUMN_NAME_AGE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_HEIGHT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_DAY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_MONTH,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_YEAR,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_DATE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_COMPLETED,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_RESULT

        };

        // Filter results WHERE "title" = 'My Title'
        String selection = "_id="+rowID;;
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
        //FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );



        while(cursor.moveToNext()) {
            String testDate="TEST TARİHİ:   "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_DATE))+"\n\n";
            String name="AD: "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME))+"\n";
            String surname="SOYAD: "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME))+"\n";
            String age="YAŞ: "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_AGE))+"\n";
            String gender="CİNSİYET: "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_GENDER))+"\n";
            String weight="KİLO: "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT))+" kg\n";
            String height="BOY: "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_HEIGHT))+" cm\n";
            String birthDay="DOĞUM TARİHİ: "+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_DAY));
            String birthMonth=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_MONTH));
            String birthYear=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_YEAR))+"\n\n\n";
            String testResults="TEST SONUÇLARI:\n\n\n"+cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_RESULT))+"\n";

            String data=testDate+name+surname+gender+age+height+weight+birthDay+"/"+birthMonth+"/"+birthYear+testResults;
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textView=new TextView(this);
            textView.setLayoutParams(lp);
            textView.setTypeface(Typeface.createFromAsset(this.getAssets(), "pt_sans-web-wegular.ttf"));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18);
            textView.setText(data);

            row.addView(textView);

            if(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_COMPLETED))==0)
            {
                Button continueTestBtn=new Button(this);
                mHeight=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT));
                continueTestBtn.setLayoutParams(lp);
                continueTestBtn.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
                continueTestBtn.setText("TESTE DEVAM ET");
                continueTestBtn.setTextSize(20);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    continueTestBtn.setBackground(getDrawable(R.drawable.mybutton));
                }
                continueTestBtn.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
                continueTestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long newRowId = v.getId();
                        //START TEST SCREEN
                        Intent intent=new Intent(RecordsDetails.this,MainActivity.class);
                        intent.putExtra(EXTRA_MESSAGE2,String.valueOf(newRowId));
                        intent.putExtra(EXTRA_MESSAGE,mHeight);
                        startActivity(intent);
                    }
                });
                row.addView(continueTestBtn);
            }

            Button deleteRecordBtn=new Button(this);

            deleteRecordBtn.setLayoutParams(lp);
            deleteRecordBtn.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                deleteRecordBtn.setBackground(getDrawable(R.drawable.mybutton));
            }
            deleteRecordBtn.setText("KAYDI SİL");
            deleteRecordBtn.setTextSize(20);

            deleteRecordBtn.setTypeface(Typeface.DEFAULT_BOLD);
            deleteRecordBtn.setTextColor(Color.RED);
            deleteRecordBtn.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
            deleteRecordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Define 'where' part of query.
                    String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = { Integer.toString(v.getId()) };
                    // Issue SQL statement.
                    db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

                    Intent returnIntent = getIntent();
                    setResult(RESULT_CANCELED, returnIntent);
                    finish();

                }
            });
            row.addView(deleteRecordBtn);


            layout.addView(row);

        }
        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_menu, menu);
        return true;
    }

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
