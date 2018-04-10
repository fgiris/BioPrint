package com.fizyoteknoterapi.muhendis.bioprint;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserInfoScreen extends AppCompatActivity {

    Spinner mBirthDayInput,mBirthMonthInput,mBirthYearInput;
    String [] day,month,year;
    EditText mName,mSurname,mAge,mHeight,mWeight;
    TextView infoBirthYear,infoBirthMonth,infoBirthDay,infoWeight,infoHeight,infoAge,infoGender,infoName,infoSurname,mBirthDateText;
    RadioGroup mGenderRadioGroup;
    RadioButton mMan,mWoman;
    Button mInfoContinue;
    final String EXTRA_MESSAGE="com.example.muhendis.bioprint";
    final String EXTRA_MESSAGE2="com.example.muhendis.bioprint2";
    FeedReaderContract.FeedEntry.FeedReaderDbHelper mDbHelper;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_screen);

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView mAppBarHeading = (TextView) findViewById(R.id.appbarheading);

        mAppBarHeading.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mAppBarHeading.setText("HASTA BİLGİLERİ");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mBirthDateText= (TextView)findViewById(R.id.textView3);
        infoBirthYear= (TextView)findViewById(R.id.infoBirthYear);
        infoBirthMonth= (TextView)findViewById(R.id.infoBirthMonth);
        infoBirthDay= (TextView)findViewById(R.id.infoBirthDay);
        infoWeight= (TextView)findViewById(R.id.infoWeight);
        infoHeight= (TextView)findViewById(R.id.infoHeight);
        infoAge= (TextView)findViewById(R.id.infoAge);
        infoGender= (TextView)findViewById(R.id.infoGender);
        infoName= (TextView)findViewById(R.id.infoName);
        infoSurname= (TextView)findViewById(R.id.infoSurname);
        mBirthDayInput = (Spinner) findViewById(R.id.infoBirthDayInput);
        mBirthMonthInput = (Spinner) findViewById(R.id.infoBirthMonthInput);
        mBirthYearInput = (Spinner) findViewById(R.id.infoBirthYearInput);
        mName=(EditText)findViewById(R.id.infoNameInput);
        mSurname=(EditText)findViewById(R.id.infoSurnameInput);
        mAge=(EditText)findViewById(R.id.infoAgeInput);
        mHeight=(EditText)findViewById(R.id.infoHeightInput);
        mWeight=(EditText)findViewById(R.id.infoWeightInput);
        mMan=(RadioButton)findViewById(R.id.infoGenderInputMan);
        mWoman=(RadioButton)findViewById(R.id.infoGenderInputWoman);
        mInfoContinue=(Button)findViewById(R.id.infoContinueButton);
        calendar=Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy/HH:mm");
        final String formattedDate = df.format(calendar.getTime());

        mInfoContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CHECK IF ALL REQUIRED FIELDS ARE FILLED
                if(mName.getText().toString().matches("") ||
                        mSurname.getText().toString().matches("") ||
                        mAge.getText().toString().matches("") ||
                        mHeight.getText().toString().matches("") ||
                        mWeight.getText().toString().matches("") ||
                        mBirthDayInput.getSelectedItemPosition()==0 ||
                        mBirthYearInput.getSelectedItemPosition()==0 ||
                        mBirthMonthInput.getSelectedItemPosition()==0)
                {
                    Toast.makeText(UserInfoScreen.this, "Lütfen bütün alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //DB connection
                mDbHelper = new FeedReaderContract.FeedEntry.FeedReaderDbHelper(getApplicationContext());

                // Gets the data repository in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, mName.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME, mSurname.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GENDER, (mMan.isSelected())?mMan.getText().toString():mWoman.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AGE, mAge.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_HEIGHT, mHeight.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT, mWeight.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_DAY, mBirthDayInput.getSelectedItem().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_MONTH, mBirthMonthInput.getSelectedItem().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BIRTH_YEAR, mBirthYearInput.getSelectedItem().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_DATE, formattedDate);
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_RESULT, "HENÜZ TEST YAPILMADI");

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                //Log.d("dbdbdbdbdb",""+newRowId);
                //START TEST SCREEN
                Intent intent=new Intent(UserInfoScreen.this,MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE2,String.valueOf(newRowId));
                intent.putExtra(EXTRA_MESSAGE,mHeight.getText().toString());
                startActivity(intent);
            }
        });


        //fill date arrays to list in the Spinners
        day=new String[32];
        month=new String[13];
        year=new String[102];
        day[0]="[Gün Seçiniz]";
        month[0]="[Ay Seçiniz]";
        year[0]="[Yıl Seçiniz]";
        fillArrayWithNumbersBetween(1,31,day);
        fillArrayWithNumbersBetween(1,13,month);
        fillArrayWithNumbersBetween(1920,2020,year);

        //set Spinners drop down menu to appropriate arrays
        setAdapterToSpinner(day,mBirthDayInput);
        setAdapterToSpinner(month,mBirthMonthInput);
        setAdapterToSpinner(year,mBirthYearInput);



        setupUI(findViewById(R.id.user_info_main_layout));
        setTypeFaces();



    }

    private void setTypeFaces()
    {
        EditText [] mEditTexts={mName,mSurname,mAge,mHeight,mWeight};
        for(TextView mTextView:mEditTexts)
        {
            mTextView.setTypeface(Typeface.createFromAsset(this.getAssets(), "sairaextracondensed-regular.ttf"));
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextSize(20);
        }

        mInfoContinue.setTypeface(Typeface.createFromAsset(this.getAssets(), "sairaextracondensed-bold.ttf"));

        TextView [] mTextViews = {infoBirthYear,infoBirthMonth,infoBirthDay,infoWeight,infoHeight,infoAge,infoGender,infoName,infoSurname};

        for(TextView mTextView:mTextViews)
        {
            mTextView.setTypeface(Typeface.createFromAsset(this.getAssets(), "sairaextracondensed-regular.ttf"));
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextSize(30);
        }

        mBirthDateText.setTypeface(Typeface.createFromAsset(this.getAssets(), "sairaextracondensed-bold.ttf"));
        mBirthDateText.setGravity(Gravity.CENTER);
        mBirthDateText.setTextSize(30);

        RadioButton[] mRadioButtons = {mMan,mWoman};

        for(RadioButton mTextView:mRadioButtons)
        {
            mTextView.setTypeface(Typeface.createFromAsset(this.getAssets(), "sairaextracondensed-regular.ttf"));
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextSize(20);
        }
    }

    private void fillArrayWithNumbersBetween(int start,int end,String [] array)
    {
        for(int i=1;i<array.length;i++)
            array[i]=Integer.toString(start+i-1);
    }

    private void setAdapterToSpinner(String[] array,Spinner spinner)
    {
        MySpinnerAdapter adapter = new MySpinnerAdapter(this,
        R.layout.spinner_item,array);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    /*@Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }*/


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(UserInfoScreen.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "sairaextracondensed-light.ttf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, String[] items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
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


