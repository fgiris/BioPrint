package com.fizyoteknoterapi.muhendis.bioprint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by muhendis on 20.11.2017.
 */

public class GoynometreImageView extends android.support.v7.widget.AppCompatImageView {

    Paint currentPaint;
    PointF zoomPos = new PointF(0, 0);
    Boolean zooming=false,startPoints=true,onMove=false,drawPoints=false;
    int activePoint;
    ArrayList<Point> mPoints;
    Display mDisplay;
    public Bitmap cacheBitmap;
    public float fazlalik=0;

    public GoynometreImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        //buildDrawingCache();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();
        mPoints = new ArrayList<Point>(3);

    }

    @Override
    protected void onDraw(Canvas canvas) {


        setCurrentPaint(4, Color.GREEN);
        int sizeOfMagnifier = 70;



        super.onDraw(canvas);

        if(startPoints)
        {
            float disBtwPoints = mDisplay.getHeight()/10;
            float paddingLeft = mDisplay.getWidth()/10;
            float startY=disBtwPoints*3;

            for(int i=0;i<3;i++)
            {
                if(i==1)
                    setCurrentPaint(4,Color.RED);
                mPoints.add(new Point((int)paddingLeft,(int)(startY+i*disBtwPoints)));
                canvas.drawCircle(paddingLeft,(startY+i*disBtwPoints),6,currentPaint);
                setCurrentPaint(4,Color.GREEN);
                if(i!=2)
                {
                    canvas.drawLine(paddingLeft,startY+i*disBtwPoints,paddingLeft,startY+(i+1)*disBtwPoints,currentPaint);
                }
            }
            startPoints=false;

        }
        else if(drawPoints)
        {
            for(int i=0;i<3;i++)
            {
                if(i==1)
                    setCurrentPaint(4,Color.RED);
                canvas.drawCircle(mPoints.get(i).x,mPoints.get(i).y,6,currentPaint);
                setCurrentPaint(4,Color.GREEN);
                if(i!=2)
                {
                    canvas.drawLine(mPoints.get(i).x,mPoints.get(i).y,mPoints.get(i+1).x,mPoints.get(i+1).y,currentPaint);
                }
            }
            currentPaint.setTextSize(30);
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            canvas.drawText(df.format(getGonyometreAngle())+"°",(mPoints.get(0).x+mPoints.get(1).x+mPoints.get(2).x)/3,(mPoints.get(0).y+mPoints.get(1).y+mPoints.get(2).y)/3-130,currentPaint);
        }
        if(zooming)
        {
            buildDrawingCache();
            //Bitmap bitmap = getDrawingCache();
            Bitmap bitmap = cacheBitmap;


            //setDrawingCacheEnabled(false);
            //bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setShader(shader);
            Matrix matrix = new Matrix();
            matrix.reset();
            //if(fazlalik<=0)
                matrix.postScale(2f, 2f, zoomPos.x-150, zoomPos.y+50);
            //else
                //matrix.postScale(2f, 2f, zoomPos.x-150, zoomPos.y+50-fazlalik);
            paint.getShader().setLocalMatrix(matrix);
            canvas.drawCircle(zoomPos.x+150, zoomPos.y-50, sizeOfMagnifier, paint);

            canvas.drawCircle(zoomPos.x+150, zoomPos.y-50, 10, currentPaint);

        }

    }

    public void setCurrentPaint(int width,int color)
    {
        currentPaint = new Paint();
        currentPaint.setDither(true);
        currentPaint.setColor(color);  // alpha.r.g.b
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
        currentPaint.setStrokeWidth(width);
    }

    public int getCurrentPoint(float x, float y)
    {
        ArrayList<Integer> points=new ArrayList<Integer>();
        for(int i=0;i<mPoints.size();i++)
        {
            Point currentPoint=mPoints.get(i);
            if(x<currentPoint.x+25 && x>currentPoint.x-25 && y<currentPoint.y+25 && y>currentPoint.y-25)
            {
                points.add(i);
            }
        }

        if(points.size()>1)
        {
            double min=Math.hypot(mPoints.get(points.get(0)).x,mPoints.get(points.get(0)).y);
            int minIndex=0;
            int i=0;
            double distance;
            for(int p:points)
            {
                distance=Math.hypot(mPoints.get(p).x,mPoints.get(p).y);
                if(distance<min)
                {
                    min=distance;
                    minIndex=i;
                }
                i++;

            }

            //dragDropPoints.get(points.get(minIndex).x).get(points.get(minIndex).y).set((int)x,(int)y);
            if(!onMove)
            {
                activePoint=points.get(minIndex);
                mPoints.get(points.get(minIndex)).set((int)x,(int)y);

            }
            else
            {
                mPoints.get(activePoint).set((int)x,(int)y);
            }

            return minIndex;
        }

        else if(points.size()>0)
        {
            //dragDropPoints.get(points.get(0).x).get(points.get(0).y).set((int)x,(int)y);
            if(!onMove)
            {
                mPoints.get(points.get(0)).set((int)x,(int)y);
                activePoint=points.get(0);
            }

            else
            {
                mPoints.get(activePoint).set((int)x,(int)y);
            }

            return points.get(0);
        }


        else
        {
            //Log.d("GETCURRENTPOINT","Returned NULL !!!! NULL");
            //if(!onMove)
            //activePoint=null;
            return -1;
        }
    }

    public double getGonyometreAngle()
    {
        double a,b,c;
        a= Math.sqrt(Math.pow((mPoints.get(2).x-mPoints.get(0).x),2)+Math.pow((mPoints.get(2).y-mPoints.get(0).y),2));
        b= Math.sqrt(Math.pow((mPoints.get(2).x-mPoints.get(1).x),2)+Math.pow((mPoints.get(2).y-mPoints.get(1).y),2));
        c= Math.sqrt(Math.pow((mPoints.get(1).x-mPoints.get(0).x),2)+Math.pow((mPoints.get(1).y-mPoints.get(0).y),2));


        return Math.toDegrees(Math.acos((b*b+c*c-a*a)/(2*b*c)));
    }





}
