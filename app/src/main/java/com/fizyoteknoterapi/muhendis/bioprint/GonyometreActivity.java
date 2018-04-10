package com.fizyoteknoterapi.muhendis.bioprint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;

public class GonyometreActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    GoynometreImageView mGoynometreImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonyometre);


        mGoynometreImage = (GoynometreImageView) findViewById(R.id.gonyometreImageView);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView mAppBarHeading = (TextView) findViewById(R.id.appbarheading);

        mAppBarHeading.setTypeface(Typeface.createFromAsset(this.getAssets(), "bebas_neue_bold.ttf"));
        mAppBarHeading.setText("GONYOMETRE");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        mGoynometreImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                GoynometreImageView drawView = (GoynometreImageView) v;

                int action = event.getAction();

                float cX = event.getX();
                float cY = event.getY();
                Point currentPoint;

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        drawView.getCurrentPoint(cX,cY);
                        drawView.onMove=true;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        drawView.drawPoints=true;


                        drawView.zooming = true;
                        drawView.invalidate();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        drawView.getCurrentPoint(cX,cY);
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;

                        drawView.zooming = true;
                        drawView.invalidate();

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        drawView.onMove=false;
                        drawView.zoomPos.x=cX;
                        drawView.zoomPos.y=cY;
                        //if(currentPoint!=null)


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
    }


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
                        scaled = Bitmap.createScaledBitmap(bitmapImage, (int)screenWidth, nh, true);
                    else
                        scaled = Bitmap.createScaledBitmap(bitmapImage, nw, (int)screenHeight, true);
                    mGoynometreImage.setImageBitmap(scaled);
                    mGoynometreImage.cacheBitmap = scaled;
                    mGoynometreImage.fazlalik = fazlalik;
                    mGoynometreImage.setDrawingCacheEnabled(true);
                    mGoynometreImage.buildDrawingCache();

                } catch (IOException e) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getApplicationContext());
                    }
                    builder.setTitle("ERROR")
                            .setMessage("Message= "+e.getMessage()+"\n---Error String="+e.toString()+"\nRequest Code: "+requestCode+"\nResult Code: "+resultCode+"\nData: "+data)
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
