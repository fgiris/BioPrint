package com.fizyoteknoterapi.muhendis.bioprint;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static int counter=0;
    Calendar calendar;
    TextView mResult,mBoy,mBoyTextInput,lateralResults,anteriorResults,posteriorResults,antSonucHeading,posSonucHeading,latSonucHeading;

    LinearLayout resultConstraintLayoutView;
    CoordinatorLayout mainLayout;
    ThreatmentImageView boyImageView,anteriorImageView,posteriorImageView,lateralImageView;
    EditText realHeight;
    final String EXTRA_MESSAGE="com.example.muhendis.bioprint";
    final String EXTRA_MESSAGE2="com.example.muhendis.bioprint2";
    int count=0,menuID=1;
    int[] numberOfPointsAnterior={5,2,2,6};
    int[] drawVerticalIndexAnterior={0,3};
    int[] drawHorizontalIndexAnterior={1,2};
    String[] pointsNameAnterior={"A","Ç","S","U","P","AC","AC","P","P","AP","AB","D","D","AB","AP"};
    int[] colorsNameAnterior={Color.BLUE, Color.GREEN,Color.MAGENTA,Color.RED};

    ScrollView mScrollView;


    int[] numberOfPointsPosterior={3,2,2,2,2,2};
    int[] drawVerticalIndexPosterior={0,4,5};
    int[] drawHorizontalIndexPosterior={1,2,3};
    String[] pointsNamePosterior={"C7","T5","P","AC","AC","P","P","D","D","AB","AT","AB","AT"};
    int[] colorsNamePosterior={Color.BLUE, Color.GREEN,Color.MAGENTA,Color.RED,Color.YELLOW,Color.CYAN};

    int[] numberOfPointsLateral={5,3};
    int[] drawVerticalIndexLateral={0};
    int[] drawHorizontalIndexLateral={1};
    String[] pointsNameLateral={"M","AC","P","D","AT","P","","P"};
    int[] colorsNameLateral={Color.BLUE, Color.GREEN};
    int radius=5;
    private int PICK_IMAGE_REQUEST = 1;
    boolean heightImageSelected=false;
    TextView exp;
    FloatingActionButton fab;
    FeedReaderContract.FeedEntry.FeedReaderDbHelper mDbHelper;

    //Zoom zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView mAppBarHeading = (TextView) findViewById(R.id.appbarheading);
        mScrollView= (ScrollView) findViewById(R.id.results_scroll_view);

        mAppBarHeading.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mAppBarHeading.setText("Fizyoprint");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        antSonucHeading = (TextView) findViewById(R.id.antSonucHeading);
        latSonucHeading = (TextView) findViewById(R.id.latSonucHeading);
        posSonucHeading = (TextView) findViewById(R.id.PostSonucHeading);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), PICK_IMAGE_REQUEST);
            }
        });
        //mText= (TextView) findViewById(R.id.content_text);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.denek_boy)
                    changeImageView(1);
                else if(item.getItemId()==R.id.denek_anterior)
                    changeImageView(2);
                else if(item.getItemId()==R.id.denek_posterior)
                    changeImageView(3);
                else if(item.getItemId()==R.id.denek_lateral)
                    changeImageView(4);
                else if(item.getItemId()==R.id.denek_sonuc)
                    changeImageView(5);

                return true;
            }
        });


        mainLayout=(CoordinatorLayout) findViewById(R.id.main_layout);
        exp =(TextView)findViewById(R.id.boy_aciklama);
        //resultConstraintLayoutView=getLayoutInflater().inflate(R.layout.content_results,null);

        //mainLayout.addView(resultConstraintLayoutView);
        lateralResults= (TextView) findViewById(R.id.lateral_sonuclar);
        anteriorResults= (TextView) findViewById(R.id.anterior_sonuclar);
        posteriorResults= (TextView) findViewById(R.id.posterior_sonuclar);

        resultConstraintLayoutView=(LinearLayout) findViewById(R.id.results_layout);
        mBoy=(TextView) findViewById(R.id.boy_aciklama);
        //realHeight= (EditText) findViewById(R.id.boy_input);
        boyImageView= (ThreatmentImageView) findViewById(R.id.boy_image_view);
        anteriorImageView= (ThreatmentImageView) findViewById(R.id.anterior_image_view);
        posteriorImageView= (ThreatmentImageView) findViewById(R.id.posterior_image_view);
        lateralImageView= (ThreatmentImageView) findViewById(R.id.lateral_image_view);
        mResult=(TextView) findViewById(R.id.result_text);

        resultConstraintLayoutView.setVisibility(View.INVISIBLE);
        anteriorImageView.setVisibility(View.INVISIBLE);
        posteriorImageView.setVisibility(View.INVISIBLE);
        lateralImageView.setVisibility(View.INVISIBLE);
        mResult.setVisibility(View.INVISIBLE);
        //mAngleText=(TextView) findViewById(R.id.angle_text);

        boyImageView.setHeightLines(this);
        boyImageView.invalidate();
        boyImageView.drawHeightLinesStart=true;


        boyImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("ONTOUCHLISTENER","!!!!!!!!ON TOUCH İÇERİSİNDE!!!!!!!");
                ThreatmentImageView drawView = (ThreatmentImageView) v;


                //drawView.setAllVariables(numberOfPoints.length,numberOfPoints,radius,drawVerticalIndex,drawHorizontalIndex);

                if(event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    float cX=event.getX();
                    float cY=event.getY();
                    //Point currentPoint=drawView.get_current_point(cX,cY);

                    //if(currentPoint!=null)

                        // draw
                        drawView.invalidate();
                    //drawView.drawHeightLines(cY);
                    drawView.cY=cY;
                    drawView.drawHeightLines = true;
                    drawView.drawHeightLinesStart=false;

                    //zoom.setImage(v.getContext(),drawView.currentBitmap);



                }

                //mAngleText.setText("Left:"+drawView.left+"Top:"+drawView.top+"Right:"+drawView.right+"Bottom:"+drawView.bottom);
                //float tanAngle=(drawView.bottom-drawView.top)/(drawView.right-drawView.left);
                //double angle=Math.toDegrees(Math.atan(tanAngle));
                //mAngleText.setText(String.valueOf(angle));



                return true;
            }
        });









        anteriorImageView.setAllVariables(numberOfPointsAnterior.length,numberOfPointsAnterior,radius,drawVerticalIndexAnterior,drawHorizontalIndexAnterior,this,colorsNameAnterior,pointsNameAnterior);
        anteriorImageView.invalidate();
        anteriorImageView.drawRect=true;


        anteriorImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("ONTOUCHLISTENER","!!!!!!!!ON TOUCH İÇERİSİNDE!!!!!!!");
                ThreatmentImageView drawView = (ThreatmentImageView) v;


                //drawView.setAllVariables(numberOfPoints.length,numberOfPoints,radius,drawVerticalIndex,drawHorizontalIndex);

                /*if(event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    float cX=event.getX();
                    float cY=event.getY();
                    Point currentPoint=drawView.get_current_point(cX,cY);

                    drawView.cX=cX;
                    drawView.cXY=cY;
                    //if(currentPoint!=null)

                    drawView.drawRect = true;
                        // draw
                        drawView.invalidate();


                    //zoom.setImage(v.getContext(),drawView.currentBitmap);



                }*/

                int action = event.getAction();

                float cX = event.getX();
                float cY = event.getY();
                Point currentPoint;


                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        currentPoint=drawView.get_current_point(cX,cY);
                        drawView.onMove=true;

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw


                        drawView.zooming = true;

                        drawView.invalidate();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        currentPoint=drawView.get_current_point(cX,cY);

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw


                        drawView.zooming = true;
                        //counter++;
                        //Log.d("counter",""+counter);
                        //if(counter%4==0)
                            //drawView.setDrawingCacheEnabled(true);
                        drawView.invalidate();
                        //if(counter%8==0)
                            //drawView.destroyDrawingCache();

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        drawView.onMove=false;
                        currentPoint=drawView.get_current_point(cX,cY);

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw
                        drawView.zooming = false;
                        //drawView.destroyDrawingCache();
                        drawView.invalidate();
                        break;

                    default:
                        break;
                }

                //mAngleText.setText("Left:"+drawView.left+"Top:"+drawView.top+"Right:"+drawView.right+"Bottom:"+drawView.bottom);
                //float tanAngle=(drawView.bottom-drawView.top)/(drawView.right-drawView.left);
                //double angle=Math.toDegrees(Math.atan(tanAngle));
                //mAngleText.setText(String.valueOf(angle));



                return true;
            }
        });


        posteriorImageView.setAllVariables(numberOfPointsPosterior.length,numberOfPointsPosterior,radius,drawVerticalIndexPosterior,drawHorizontalIndexPosterior,this,colorsNamePosterior,pointsNamePosterior);
        posteriorImageView.invalidate();
        posteriorImageView.drawRect=true;


        posteriorImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("ONTOUCHLISTENER","!!!!!!!!ON TOUCH İÇERİSİNDE!!!!!!!");
                ThreatmentImageView drawView = (ThreatmentImageView) v;


                //drawView.setAllVariables(numberOfPoints.length,numberOfPoints,radius,drawVerticalIndex,drawHorizontalIndex);

                /*if(event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    float cX=event.getX();
                    float cY=event.getY();
                    Point currentPoint=drawView.get_current_point(cX,cY);

                    drawView.cX=cX;
                    drawView.cXY=cY;
                    //if(currentPoint!=null)

                        // draw
                        drawView.invalidate();
                    drawView.drawRect = true;

                    //zoom.setImage(v.getContext(),drawView.currentBitmap);



                }*/
                int action = event.getAction();

                float cX = event.getX();
                float cY = event.getY();
                Point currentPoint;

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        currentPoint=drawView.get_current_point(cX,cY);

                        drawView.onMove=true;
                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw


                        drawView.zooming = true;
                        drawView.invalidate();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentPoint=drawView.get_current_point(cX,cY);

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw


                        drawView.zooming = true;
                        drawView.invalidate();

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        drawView.onMove=false;
                        currentPoint=drawView.get_current_point(cX,cY);

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw
                        drawView.zooming = false;
                        drawView.invalidate();
                        break;

                    default:
                        break;
                }

                //mAngleText.setText("Left:"+drawView.left+"Top:"+drawView.top+"Right:"+drawView.right+"Bottom:"+drawView.bottom);
                //float tanAngle=(drawView.bottom-drawView.top)/(drawView.right-drawView.left);
                //double angle=Math.toDegrees(Math.atan(tanAngle));
                //mAngleText.setText(String.valueOf(angle));



                return true;
            }
        });


        lateralImageView.setAllVariables(numberOfPointsLateral.length,numberOfPointsLateral,radius,drawVerticalIndexLateral,drawHorizontalIndexLateral,this,colorsNameLateral,pointsNameLateral);
        lateralImageView.invalidate();
        lateralImageView.drawRect=true;


        lateralImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("ONTOUCHLISTENER","!!!!!!!!ON TOUCH İÇERİSİNDE!!!!!!!");
                ThreatmentImageView drawView = (ThreatmentImageView) v;


                //drawView.setAllVariables(numberOfPoints.length,numberOfPoints,radius,drawVerticalIndex,drawHorizontalIndex);

                /*if(event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    float cX=event.getX();
                    float cY=event.getY();
                    Point currentPoint=drawView.get_current_point(cX,cY);

                    drawView.cX=cX;
                    drawView.cXY=cY;
                    //if(currentPoint!=null)

                        // draw
                        drawView.invalidate();
                    drawView.drawRect = true;

                    //zoom.setImage(v.getContext(),drawView.currentBitmap);



                }*/

                int action = event.getAction();

                float cX = event.getX();
                float cY = event.getY();
                Point currentPoint;

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        currentPoint=drawView.get_current_point(cX,cY);
                        drawView.onMove=true;

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw


                        drawView.zooming = true;
                        drawView.invalidate();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentPoint=drawView.get_current_point(cX,cY);

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw


                        drawView.zooming = true;
                        drawView.invalidate();

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        drawView.onMove=false;
                        currentPoint=drawView.get_current_point(cX,cY);

                        drawView.cX=cX;
                        drawView.cXY=cY;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)

                        drawView.drawRect = true;
                        // draw
                        drawView.zooming = false;
                        drawView.invalidate();
                        break;

                    default:
                        break;
                }

                //mAngleText.setText("Left:"+drawView.left+"Top:"+drawView.top+"Right:"+drawView.right+"Bottom:"+drawView.bottom);
                //float tanAngle=(drawView.bottom-drawView.top)/(drawView.right-drawView.left);
                //double angle=Math.toDegrees(Math.atan(tanAngle));
                //mAngleText.setText(String.valueOf(angle));



                return true;
            }
        });

        /*realHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Float realHeightText;
                if(s.length()>1)
                {
                    realHeightText=Float.parseFloat(s.toString());
                    boyImageView.setRatio(realHeightText);
                    anteriorImageView.ratio=posteriorImageView.ratio=lateralImageView.ratio=boyImageView.ratio;
                    anteriorImageView.ratio=posteriorImageView.ratio=lateralImageView.ratio=boyImageView.ratio;
                }

                //
                //anteriorImageView.ratio=posteriorImageView.ratio=lateralImageView.ratio=boyImageView.ratio;
                //Log.d("RATIO",""+boyImageView.ratio);
            }
        });*/

        Float realHeightText=Float.parseFloat(getIntent().getStringExtra(EXTRA_MESSAGE));
        Log.d("REAL HEIGHT",""+realHeightText);
        boyImageView.setRatio(realHeightText);
        anteriorImageView.ratio=posteriorImageView.ratio=lateralImageView.ratio=boyImageView.ratio;
        anteriorImageView.ratio=posteriorImageView.ratio=lateralImageView.ratio=boyImageView.ratio;
        Log.d("RATIO",""+anteriorImageView.ratio);



        TextView [] mTextViews={mResult,mBoy,lateralResults,anteriorResults,posteriorResults,exp};
        for(TextView mTextView:mTextViews)
        {
            mTextView.setTypeface(Typeface.createFromAsset(this.getAssets(), "pt_sans-web-wegular.ttf"));
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextSize(20);
        }

        TextView [] mTextViews2={antSonucHeading,posSonucHeading,latSonucHeading};

        for(TextView mTextView:mTextViews2)
        {
            mTextView.setTypeface(Typeface.createFromAsset(this.getAssets(), "pt_sans-web-wegular.ttf"),Typeface.BOLD);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextSize(26);

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

    public void changeImageView(int id)
    {
        if(id==1)
        {
            menuID=1;
            if(heightImageSelected)
                exp.setVisibility(View.INVISIBLE);
            else
                exp.setVisibility(View.VISIBLE);
            //mBoy.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            boyImageView.setVisibility(View.VISIBLE);
            anteriorImageView.setVisibility(View.INVISIBLE);
            posteriorImageView.setVisibility(View.INVISIBLE);
            lateralImageView.setVisibility(View.INVISIBLE);
            mResult.setVisibility(View.INVISIBLE);
            resultConstraintLayoutView.setVisibility(View.INVISIBLE);
            mScrollView.setVisibility(View.INVISIBLE);
        }
        else if(id==2)
        {
            menuID=2;
            fab.setVisibility(View.VISIBLE);
            mBoy.setVisibility(View.INVISIBLE);
            boyImageView.setVisibility(View.INVISIBLE);
            anteriorImageView.setVisibility(View.VISIBLE);
            posteriorImageView.setVisibility(View.INVISIBLE);
            lateralImageView.setVisibility(View.INVISIBLE);
            mResult.setVisibility(View.INVISIBLE);
            resultConstraintLayoutView.setVisibility(View.INVISIBLE);
            mScrollView.setVisibility(View.INVISIBLE);
        }
        else if(id==3)
        {
            menuID=3;
            fab.setVisibility(View.VISIBLE);
            mBoy.setVisibility(View.INVISIBLE);
            boyImageView.setVisibility(View.INVISIBLE);
            anteriorImageView.setVisibility(View.INVISIBLE);
            posteriorImageView.setVisibility(View.VISIBLE);
            lateralImageView.setVisibility(View.INVISIBLE);
            mResult.setVisibility(View.INVISIBLE);
            resultConstraintLayoutView.setVisibility(View.INVISIBLE);
            mScrollView.setVisibility(View.INVISIBLE);
        }
        else if(id==4)
        {
            menuID=4;
            fab.setVisibility(View.VISIBLE);
            mBoy.setVisibility(View.INVISIBLE);
            boyImageView.setVisibility(View.INVISIBLE);
            anteriorImageView.setVisibility(View.INVISIBLE);
            posteriorImageView.setVisibility(View.INVISIBLE);
            lateralImageView.setVisibility(View.VISIBLE);
            mResult.setVisibility(View.INVISIBLE);
            resultConstraintLayoutView.setVisibility(View.INVISIBLE);
            mScrollView.setVisibility(View.INVISIBLE);
        }
        else if(id==5)
        {
            Float realHeightText=Float.parseFloat(getIntent().getStringExtra(EXTRA_MESSAGE));
            Log.d("REAL HEIGHT",""+realHeightText);
            boyImageView.setRatio(realHeightText);
            anteriorImageView.ratio=posteriorImageView.ratio=lateralImageView.ratio=boyImageView.ratio;
            anteriorImageView.ratio=posteriorImageView.ratio=lateralImageView.ratio=boyImageView.ratio;
            Log.d("RATIO",""+anteriorImageView.ratio);
            
            menuID=5;
            fab.setVisibility(View.INVISIBLE);
            mBoy.setVisibility(View.INVISIBLE);
            boyImageView.setVisibility(View.INVISIBLE);
            anteriorImageView.setVisibility(View.INVISIBLE);
            posteriorImageView.setVisibility(View.INVISIBLE);
            lateralImageView.setVisibility(View.INVISIBLE);
            //mResult.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.VISIBLE);
            resultConstraintLayoutView.setVisibility(View.VISIBLE);

            anteriorImageView.calculateDegrees();
            anteriorImageView.calculateDistances();
            anteriorImageView.calculateVerticalDistances();

            posteriorImageView.calculateDegrees();
            posteriorImageView.calculateDistances();
            posteriorImageView.calculateVerticalDistances();

            lateralImageView.calculateDegrees();
            lateralImageView.calculateDistances();
            lateralImageView.calculateVerticalDistances();

            //Log.d("LAteralACI",""+lateralImageView.degreesBtwPoints.get(1).get(0));
            String lateralSonuclar="";
            /*lateralSonuclar+="How many degrees you are from vertical\n";
            lateralSonuclar+="Head-Shoulders: "+lateralImageView.degreesBtwPoints.get(0).get(0)+"\u00B0\n";
            lateralSonuclar+="Shoulders-Pelvis: "+lateralImageView.degreesBtwPoints.get(0).get(1)+"\u00B0\n";
            lateralSonuclar+="Hips-Knees: "+lateralImageView.degreesBtwPoints.get(0).get(2)+"\u00B0\n";
            lateralSonuclar+="Knees-Feet: "+lateralImageView.degreesBtwPoints.get(0).get(3)+"\u00B0\n\n";
            lateralSonuclar+="How many degrees you are from horizontal\n";
            lateralSonuclar+="Pelvis: "+lateralImageView.degreesBtwPoints.get(1).get(0)+"\u00B0\n\n";
            lateralSonuclar+="How far are you from vertical\n";
            lateralSonuclar+="Head: "+lateralImageView.verticalDistances.get(0).get(0)+"cm\n";
            lateralSonuclar+="Shoulder: "+lateralImageView.verticalDistances.get(0).get(1)+"cm\n";
            //lateralSonuclar+="Pelvis: "+lateralImageView.verticalDistances.get(1).get(1)+"cm\n";
            lateralSonuclar+="Hips: "+lateralImageView.verticalDistances.get(1).get(1)+"cm\n";
            lateralSonuclar+="Knees: "+lateralImageView.verticalDistances.get(0).get(2)+"cm\n";*/

            lateralSonuclar+="Vertical eksenle yapılan açı\n";
            lateralSonuclar+="Baş-Omuzlar: "+lateralImageView.degreesBtwPoints.get(0).get(0)+"\u00B0\n";
            lateralSonuclar+="Omuzlar-Pelvis: "+lateralImageView.degreesBtwPoints.get(0).get(1)+"\u00B0\n";
            lateralSonuclar+="Kalçalar-Dizler: "+lateralImageView.degreesBtwPoints.get(0).get(2)+"\u00B0\n";
            lateralSonuclar+="Dizler-Ayaklar: "+lateralImageView.degreesBtwPoints.get(0).get(3)+"\u00B0\n\n";
            lateralSonuclar+="Horizontal eksenle yapılan açı\n";
            lateralSonuclar+="Pelvis: "+lateralImageView.degreesBtwPoints.get(1).get(0)+"\u00B0\n\n";
            lateralSonuclar+="Vertical eksenle oluşan mesafe \n";
            lateralSonuclar+="Baş: "+lateralImageView.verticalDistances.get(0).get(0)+"cm\n";
            lateralSonuclar+="Omuzlar: "+lateralImageView.verticalDistances.get(0).get(1)+"cm\n";
            //lateralSonuclar+="Pelvis: "+lateralImageView.verticalDistances.get(1).get(1)+"cm\n";
            lateralSonuclar+="Kalçalar: "+lateralImageView.verticalDistances.get(1).get(1)+"cm\n";
            lateralSonuclar+="Dizler: "+lateralImageView.verticalDistances.get(0).get(2)+"cm\n";


            lateralResults.setText(lateralSonuclar);

            String anteriorSonuclar="";
            /*anteriorSonuclar+="How many degrees you are from horizontal\n";
            anteriorSonuclar+="Shoulders: "+anteriorImageView.degreesBtwPoints.get(1).get(0)+"\u00B0\n";
            anteriorSonuclar+="Pelvis: "+anteriorImageView.degreesBtwPoints.get(2).get(0)+"\u00B0\n";
            anteriorSonuclar+="Knees: "+anteriorImageView.degreesBtwPoints.get(3).get(2)+"\u00B0\n";
            anteriorSonuclar+="How far are you from vertical\n";
            anteriorSonuclar+="Forehead:"+anteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            anteriorSonuclar+="Shoulders:"+anteriorImageView.verticalDistances.get(0).get(2)+"cm\n";
            anteriorSonuclar+="Umbilicius:"+anteriorImageView.verticalDistances.get(0).get(3)+"cm\n";
            anteriorSonuclar+="Pelvis:"+anteriorImageView.verticalDistances.get(0).get(4)+"cm\n\n";
            //anteriorSonuclar+="Knees:"+lateralImageView.verticalDistances.get(0).get(0)+"cm\n";
            //anteriorSonuclar+="Toes:"+lateralImageView.verticalDistances.get(0).get(0)+"cm\n";
            anteriorSonuclar+="How many degrees your feet are rotated\n";
            anteriorSonuclar+="Left Foot: "+anteriorImageView.degreesBtwPoints.get(3).get(0)+"\u00B0\n";
            anteriorSonuclar+="Right Foot: "+anteriorImageView.degreesBtwPoints.get(3).get(4)+"\u00B0\n";*/

            anteriorSonuclar+="Horizontal eksenle yapılan açı\n";
            anteriorSonuclar+="Omuzlar: "+anteriorImageView.degreesBtwPoints.get(1).get(0)+"\u00B0\n";
            anteriorSonuclar+="Pelvis: "+anteriorImageView.degreesBtwPoints.get(2).get(0)+"\u00B0\n";
            anteriorSonuclar+="Dizler: "+anteriorImageView.degreesBtwPoints.get(3).get(2)+"\u00B0\n";
            anteriorSonuclar+="Vertical eksenle oluşan mesafe\n";
            anteriorSonuclar+="Baş:"+anteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            anteriorSonuclar+="Omuzlar:"+anteriorImageView.verticalDistances.get(0).get(2)+"cm\n";
            anteriorSonuclar+="Umblicus:"+anteriorImageView.verticalDistances.get(0).get(3)+"cm\n";
            anteriorSonuclar+="Pelvis:"+anteriorImageView.verticalDistances.get(0).get(4)+"cm\n\n";
            //anteriorSonuclar+="Knees:"+lateralImageView.verticalDistances.get(0).get(0)+"cm\n";
            //anteriorSonuclar+="Toes:"+lateralImageView.verticalDistances.get(0).get(0)+"cm\n";
            anteriorSonuclar+="Ayakların rotasyon açısı\n";
            anteriorSonuclar+="Sol Ayak: "+anteriorImageView.degreesBtwPoints.get(3).get(0)+"\u00B0\n";
            anteriorSonuclar+="Sağ Ayak: "+anteriorImageView.degreesBtwPoints.get(3).get(4)+"\u00B0\n";



            anteriorResults.setText(anteriorSonuclar);

            String posteriorSonuclar="";
            /*posteriorSonuclar+="How many degrees you are from horizontal\n";
            posteriorSonuclar+="Shoulders: "+posteriorImageView.degreesBtwPoints.get(1).get(0)+"\u00B0\n";
            posteriorSonuclar+="Pelvis: "+posteriorImageView.degreesBtwPoints.get(2).get(0)+"\u00B0\n";
            posteriorSonuclar+="Knees: "+posteriorImageView.degreesBtwPoints.get(3).get(0)+"\u00B0\n";
            posteriorSonuclar+="How far are you from vertical\n";
            //posteriorSonuclar+="Shoulders:"+posteriorImageView.verticalDistances.get(2).get(1)+"cm\n";
            posteriorSonuclar+="7th Cervical:"+posteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            posteriorSonuclar+="5th Thoracic:"+posteriorImageView.verticalDistances.get(0).get(1)+"cm\n";
            posteriorSonuclar+="Pelvis:"+posteriorImageView.verticalDistances.get(0).get(2)+"cm\n\n";
            //posteriorSonuclar+="Knees:"+posteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            //posteriorSonuclar+="Ankles:"+posteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            posteriorSonuclar+="How many degrees your feet are rotated\n";
            posteriorSonuclar+="Left Foot: "+posteriorImageView.degreesBtwPoints.get(4).get(0)+"\u00B0\n";
            posteriorSonuclar+="Right Foot: "+posteriorImageView.degreesBtwPoints.get(5).get(0)+"\u00B0\n";*/

            posteriorSonuclar+="Horizontal eksenle yapılan açı\n";
            posteriorSonuclar+="Omuzlar: "+posteriorImageView.degreesBtwPoints.get(1).get(0)+"\u00B0\n";
            posteriorSonuclar+="Pelvis: "+posteriorImageView.degreesBtwPoints.get(2).get(0)+"\u00B0\n";
            posteriorSonuclar+="Dizler: "+posteriorImageView.degreesBtwPoints.get(3).get(0)+"\u00B0\n";
            posteriorSonuclar+="Vertical eksenle oluşan mesafe\n";
            //posteriorSonuclar+="Shoulders:"+posteriorImageView.verticalDistances.get(2).get(1)+"cm\n";
            posteriorSonuclar+="7.Cervical Vertebra:"+posteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            posteriorSonuclar+="5.Thoracal Vertebra:"+posteriorImageView.verticalDistances.get(0).get(1)+"cm\n";
            posteriorSonuclar+="Pelvis:"+posteriorImageView.verticalDistances.get(0).get(2)+"cm\n\n";
            //posteriorSonuclar+="Knees:"+posteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            //posteriorSonuclar+="Ankles:"+posteriorImageView.verticalDistances.get(0).get(0)+"cm\n";
            posteriorSonuclar+="Ayakların rotasyon açısı\n";
            posteriorSonuclar+="Sol Ayak: "+posteriorImageView.degreesBtwPoints.get(4).get(0)+"\u00B0\n";
            posteriorSonuclar+="Sağ Ayak: "+posteriorImageView.degreesBtwPoints.get(5).get(0)+"\u00B0\n";



            posteriorResults.setText(posteriorSonuclar);

            String res="Anterior Sonuçlar\n\n"+anteriorSonuclar+"\n\n"+"Posterior Sonuçlar\n\n"+posteriorSonuclar+"\n\nLateral Sonuçlar\n\n"+lateralSonuclar;
            int rowID=Integer.parseInt(getIntent().getStringExtra(EXTRA_MESSAGE2));
            calendar= Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy/HH:mm");
            final String formattedDate = df.format(calendar.getTime());
            //UPDATE DB
            //DB connection
            mDbHelper = new FeedReaderContract.FeedEntry.FeedReaderDbHelper(getApplicationContext());

            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // New value for one column
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_RESULT, res);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_COMPLETED, 1);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TEST_DATE, formattedDate);

            // Which row to update, based on the title
            String selection = "_id="+rowID;
            //String[] selectionArgs = { "MyTitle" };

            int count = db.update(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    null);

            //Log.d("dbdbdbdbdbdbdb count",""+count);


        }
    }



    protected void onActivityResultBackup(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri uri = data.getData();

                try {
                    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();;
                    display.getMetrics(metrics);
                    float logicalDensity = metrics.density;

                    float screenWidth = (float)display.getWidth();
                    float screenHeight = (float)display.getHeight();


                    Log.d("MAINACTIVITY","----LOGICAL DENSITY: "+logicalDensity);

                    Log.d("MAINACTIVITY","SCREENWIDTH: "+screenWidth);
                    Log.d("MAINACTIVITY","SCREENHEIGHT: "+(float)display.getHeight());

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    int nh = (int) ( bitmapImage.getHeight() * (screenWidth / bitmapImage.getWidth()) );
                    int nw = (int) ( bitmapImage.getWidth() * ((530*logicalDensity) / bitmapImage.getHeight()) );

                    float fazlalik=(530*logicalDensity)-nh;
                    Bitmap scaled;
                    if(fazlalik<0)
                        scaled = Bitmap.createScaledBitmap(bitmapImage, nw, (int)(530*logicalDensity), true);
                    else
                        scaled = Bitmap.createScaledBitmap(bitmapImage, (int)screenWidth, nh, true);


                    Log.d("MAINACTIVITY","BITMAPWIDTH: "+bitmapImage.getWidth());
                    Log.d("MAINACTIVITY","BITMAPHEIGHT: "+bitmapImage.getHeight());
                    Log.d("MAINACTIVITY","SCALEDWIDTH: "+nw);
                    Log.d("MAINACTIVITY","SCALEDHEIGHT: "+nh);
                    Log.d("MAINACTIVITY","----FAZLALIK: "+fazlalik);


                    // Log.d(TAG, String.valueOf(bitmap));


                    //bitmap.setHeight(100);

                    ThreatmentImageView imageView;

                    switch(menuID)
                    {
                        case 1:
                            imageView = (ThreatmentImageView) findViewById(R.id.boy_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                            exp.setVisibility(View.INVISIBLE);
                            heightImageSelected=true;
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());
                            break;
                        case 2:
                            imageView = (ThreatmentImageView) findViewById(R.id.anterior_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                        /*imageView.imagePicked=true;
                        imageView.imageBitmap=bitmap;
                        imageView.invalidate();*/
                            //imageView.setBackground(BitmapDrawable.createFromPath(uri.getPath()));
                            //imageView.setImageResource(R.drawable.denek);
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());
                            break;
                        case 3:
                            imageView = (ThreatmentImageView) findViewById(R.id.posterior_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());

                            break;
                        case 4:
                            imageView = (ThreatmentImageView) findViewById(R.id.lateral_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());

                            break;

                    }


                } catch (IOException e) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getApplicationContext());
                    }
                    builder.setTitle("ERROR")
                            .setMessage("Message= "+e.getMessage()+"\n---Error String="+e.toString())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        }
        catch (Exception e)
        {
            String message;
            if(data==null)
                message = "Message= "+e.getMessage()+"\n---Error String="+e.toString()+"\nRequest Code: "+requestCode+"\nResult Code: "+resultCode+"\nData: null";
            else
                message = "Message= "+e.getMessage()+"\n---Error String="+e.toString()+"\nRequest Code: "+requestCode+"\nResult Code: "+resultCode+"\nData: "+data;
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getApplicationContext());
            }
            builder.setTitle("ERROR")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri uri = data.getData();

                try {
                    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();;
                    display.getMetrics(metrics);
                    float logicalDensity = metrics.density;

                    float screenWidth = (float)display.getWidth();
                    float screenHeight = (float)display.getHeight()*3/4;


                    Log.d("MAINACTIVITY","----LOGICAL DENSITY: "+logicalDensity);

                    Log.d("MAINACTIVITY","SCREENWIDTH: "+screenWidth);
                    Log.d("MAINACTIVITY","SCREENHEIGHT: "+(float)display.getHeight());

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    int bitmapWidthHeight;
                    if(bitmapImage.getHeight()<bitmapImage.getWidth())
                        bitmapWidthHeight = 0;
                    else if(bitmapImage.getHeight()>bitmapImage.getWidth())
                        bitmapWidthHeight = 2;
                    else
                        bitmapWidthHeight = 1;

                    int nh = (int) ( bitmapImage.getHeight() * (screenWidth / bitmapImage.getWidth()) );
                    int nw = (int) ( bitmapImage.getWidth() * (screenHeight / bitmapImage.getHeight()) );

                    float fazlalik=(530*logicalDensity)-nh;
                    Bitmap scaled;
                    if(bitmapWidthHeight==0)
                    {
                        scaled = Bitmap.createScaledBitmap(bitmapImage, (int)screenWidth, nh, true);

                    }
                    else
                        scaled = Bitmap.createScaledBitmap(bitmapImage, nw, (int)screenHeight, true);


                    Log.d("MAINACTIVITY","BITMAPWIDTH: "+bitmapImage.getWidth());
                    Log.d("MAINACTIVITY","BITMAPHEIGHT: "+bitmapImage.getHeight());
                    Log.d("MAINACTIVITY","SCALEDWIDTH: "+nw);
                    Log.d("MAINACTIVITY","SCALEDHEIGHT: "+nh);
                    Log.d("MAINACTIVITY","----FAZLALIK: "+fazlalik);


                    // Log.d(TAG, String.valueOf(bitmap));


                    //bitmap.setHeight(100);

                    ThreatmentImageView imageView;

                    switch(menuID)
                    {
                        case 1:
                            imageView = (ThreatmentImageView) findViewById(R.id.boy_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                            exp.setVisibility(View.INVISIBLE);
                            heightImageSelected=true;
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());
                            break;
                        case 2:
                            imageView = (ThreatmentImageView) findViewById(R.id.anterior_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                        /*imageView.imagePicked=true;
                        imageView.imageBitmap=bitmap;
                        imageView.invalidate();*/
                            //imageView.setBackground(BitmapDrawable.createFromPath(uri.getPath()));
                            //imageView.setImageResource(R.drawable.denek);
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());
                            break;
                        case 3:
                            imageView = (ThreatmentImageView) findViewById(R.id.posterior_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());

                            break;
                        case 4:
                            imageView = (ThreatmentImageView) findViewById(R.id.lateral_image_view);
                            imageView.cacheBitmap = scaled;
                            imageView.fazlalik = fazlalik;
                            //imageView.setImageBitmap(bitmap);
                            imageView.setImageBitmap(scaled);
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Log.d("MAINACTIVITY","WRAPPED HEIGHT: "+imageView.getHeight());

                            break;

                    }


                } catch (IOException e) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getApplicationContext());
                    }
                    builder.setTitle("ERROR")
                            .setMessage("Message= "+e.getMessage()+"\n---Error String="+e.toString())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        }
        catch (Exception e)
        {
            String message;
            if(data==null)
                message = "Message= "+e.getMessage()+"\n---Error String="+e.toString()+"\nRequest Code: "+requestCode+"\nResult Code: "+resultCode+"\nData: null";
            else
                message = "Message= "+e.getMessage()+"\n---Error String="+e.toString()+"\nRequest Code: "+requestCode+"\nResult Code: "+resultCode+"\nData: "+data;
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getApplicationContext());
            }
            builder.setTitle("ERROR")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }


   /* @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }*/
}
