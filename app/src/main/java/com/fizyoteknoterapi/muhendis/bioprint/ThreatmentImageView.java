package com.fizyoteknoterapi.muhendis.bioprint;

/**
 * Created by muhendis on 6.04.2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;


public class ThreatmentImageView extends android.support.v7.widget.AppCompatImageView {
    private Paint currentPaint;
    public boolean drawRect = false;
    public boolean drawHeightLines=false;
    public boolean drawHeightLinesStart=false;
    public ArrayList<ArrayList<Point>> dragDropPoints;
    public ArrayList<ArrayList<Float>> degreesBtwPoints,distancesBtwPoints,verticalDistances;
    public float radius,lastStartX,lastStartY,middleLineX,lastCx;
    public Bitmap currentBitmap,imageBitmap;
    public Canvas canvas;
    public float recYtop,recYbottom,recWidth,cX,cXY,cY,lastTop,lastBottom,ratio;
    public Display mDisplay;
    public int[] colorOfPointsSet,verticalIndex;
    public String[] nameOfPointsSet;
    public boolean onMove=false;
    public Point activePoint;
    public Point activePointIndex;

    public PointF zoomPos;
    public boolean zooming = false,imagePicked=false;
    private Matrix matrix;
    private Paint paint;
    private Bitmap bitmap;
    private BitmapShader shader;
    private int sizeOfMagnifier = 70;
    public Bitmap cacheBitmap;
    public float fazlalik=0;



    public ThreatmentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        zoomPos = new PointF(0, 0);
        matrix = new Matrix();
        paint = new Paint();
        activePoint=new Point();
        activePointIndex=new Point();



    }
    public void setAllVariables( int numberOfLineSet,int[] numberOfPoints,int radius,int[] drawVerticalIndex,int[] drawHorizontalIndex,Context context,int[] colorOfPointsSet,String[] nameOfPointsSet) {

        setDrawingCacheEnabled(true);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        this.colorOfPointsSet=colorOfPointsSet;
        this.nameOfPointsSet=nameOfPointsSet;
        mDisplay=display;
        this.radius=radius;
        verticalIndex=drawVerticalIndex;
        setCurrentPaint(4,0xFF00CC00);
        lastStartX=50;
        lastStartY=50;
        middleLineX=50;
        lastCx=cX=display.getWidth()/2;
        setDragDropPoints(numberOfLineSet,numberOfPoints,drawVerticalIndex,drawHorizontalIndex);
        currentBitmap=Bitmap.createBitmap(display.getWidth(),display.getHeight(), Bitmap.Config.ARGB_8888);
        canvas=new Canvas(currentBitmap);
        //canvas.save();

    }

    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);






        if (drawRect)
        {

            //this.canvas.restore();
            this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            draw_points_circle();
            draw_line_between_points();
            drawVerticalLine(canvas);


            //this.canvas.save();
            canvas.drawBitmap(currentBitmap,0,0,currentPaint);

        }
        else if(drawHeightLines)
        {
            drawHeightLines(canvas,cY);
            //drawVerticalLine(canvas,cX);

            //canvas.drawBitmap(currentBitmap,0,0,currentPaint);
            //this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        else if(drawHeightLinesStart)
        {
            drawHeightLines(canvas);
            //drawVerticalLine(canvas,cX);
        }

        /*if(imagePicked)
        {

            canvas.drawBitmap(imageBitmap,new Matrix(),null);
        }*/


        if (!zooming) {



        } else {

            //setDrawingCacheEnabled(true);
            //buildDrawingCache();
            //bitmap = getDrawingCache();
            bitmap = cacheBitmap;


            //setDrawingCacheEnabled(false);
            //bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            paint = new Paint();
            paint.setShader(shader);
            matrix.reset();
            //if(fazlalik<=0)
                matrix.postScale(2f, 2f, zoomPos.x-150, zoomPos.y+50);
            //else
                //matrix.postScale(2f, 2f, zoomPos.x-150, zoomPos.y+50-fazlalik);
            paint.getShader().setLocalMatrix(matrix);
            canvas.drawCircle(zoomPos.x+150, zoomPos.y-50, sizeOfMagnifier, paint);

            canvas.drawCircle(zoomPos.x+150, zoomPos.y-50, 10, currentPaint);

            //destroyDrawingCache();
        }
    }

    public void setDragDropPoints(int numberOfLineSet,int[] numberOfPoints,int[] drawVerticalIndex,int[] drawHorizontalIndex)
    {
        dragDropPoints=new ArrayList<ArrayList<Point>>(numberOfLineSet);


        //create ArrayList objects
        for(int i=0;i<numberOfLineSet;i++)
        {
            ArrayList<Point> mList=new ArrayList<Point>(numberOfPoints[i]);
            dragDropPoints.add(mList);
            /*for(int j=0;j<numberOfPoints[i];j++)
                dragDropPoints.get(i).add(new Point());*/

            //Log.d("NUMBEROFPOINT",i+" SIZE IS "+numberOfPoints[i]);
            //Log.d("DRAGDROPPOINTS",i+" SIZE IS "+dragDropPoints.get(i).size());
        }
        //Log.d("DRAGDROPPOINTS-GNEL"," SIZE IS "+dragDropPoints.size());
        //initialize these objects
        for(int i=0;i<numberOfLineSet;i++)
        {
            //check if the index is in vertical indexes
            if(is_in_array(drawVerticalIndex,i))
                draw_vertical_start(i,numberOfPoints[i],middleLineX,lastStartY);
            else
                draw_horizontal_start(i,numberOfPoints[i],middleLineX-30,lastStartY);

        }

    }

    public void drawVerticalLine(Canvas canvas)
    {
        //Log.d("DRAWVERTICALLINE","ICINDE");
        if(cX<lastCx+70 && cX>lastCx-70 && (cXY<100 ||cXY>mDisplay.getHeight()-100) )
        {
            //Log.d("DRAWVERTICALLINE","IF    ICINDE");
            setCurrentPaint(4, Color.BLACK);
            canvas.drawLine(cX,0,cX,mDisplay.getHeight(),currentPaint);
            setCurrentPaint(4,0xFF00CC00);
            lastCx=cX;
        }
        else
        {
            setCurrentPaint(4, Color.BLACK);
            canvas.drawLine(lastCx,0,lastCx,mDisplay.getHeight(),currentPaint);
            setCurrentPaint(4,0xFF00CC00);
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


    public Point get_current_point(float x,float y)
    {
        int cX,cY;

        ArrayList<Point> points=new ArrayList<Point>();
        for(int i=0;i<dragDropPoints.size();i++)
        {
            for(int j=0;j<dragDropPoints.get(i).size();j++)
            {
                Point currentPoint=(Point)dragDropPoints.get(i).get(j);
                if(x<currentPoint.x+25 && x>currentPoint.x-25 && y<currentPoint.y+25 && y>currentPoint.y-25)
                {
                    //dragDropPoints.get(i).get(j).set((int)x,(int)y);
                    //dragDropPoints.get(i).get(j).x=(int)x;
                    //dragDropPoints.get(i).get(j).y=(int)y;
                    //print_points(dragDropPoints);
                    points.add(new Point(i,j));
                    //Log.d("GETCURRENTPOINT","POINTS LISTE EKLENDI SIZE"+points.size());

                }

            }
        }
        if(points.size()>1)
        {
            double min=Math.hypot(points.get(0).x,points.get(0).y);
            int minIndex=0;
            int i=0;
            double distance;
            for(Point p:points)
            {
                distance=Math.hypot(p.x,p.y);
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
                activePoint=new Point(points.get(minIndex).x,points.get(minIndex).y);
                dragDropPoints.get(points.get(minIndex).x).get(points.get(minIndex).y).set((int)x,(int)y);

            }
            else
            {
                dragDropPoints.get(activePoint.x).get(activePoint.y).set((int)x,(int)y);
            }

            return new Point(points.get(minIndex).x,points.get(minIndex).y);
        }
        else if(points.size()>0)
        {
            //dragDropPoints.get(points.get(0).x).get(points.get(0).y).set((int)x,(int)y);
            if(!onMove)
            {
                dragDropPoints.get(points.get(0).x).get(points.get(0).y).set((int)x,(int)y);
                activePoint=new Point(points.get(0).x,points.get(0).y);
            }

            else
            {
                dragDropPoints.get(activePoint.x).get(activePoint.y).set((int)x,(int)y);
            }

            return new Point(points.get(0).x,points.get(0).y);
        }


        else
        {
            //Log.d("GETCURRENTPOINT","Returned NULL !!!! NULL");
            //if(!onMove)
                //activePoint=null;
            return null;
        }

    }

    public void draw_points_circle()
    {

        float lastcx=0,lastcy=0;
        int counter=0;
        for(int i=0;i<dragDropPoints.size();i++)
        {
            setCurrentPaint(4,colorOfPointsSet[i]);
            for(int j=0;j<dragDropPoints.get(i).size();j++)
            {
                float cX=dragDropPoints.get(i).get(j).x;
                float cY=dragDropPoints.get(i).get(j).y;
                //Log.d(j+" DRAGDROPPOINTS-CX","SIZE IS "+cX);
                //Log.d(j+" DRAGDROPPOINTS-CY","SIZE IS "+cY);
                canvas.drawCircle(cX,cY,radius,currentPaint);
                lastcx=cX;
                lastcy=cY;
                setCurrentPaint(2,colorOfPointsSet[i]);
                currentPaint.setTextSize(30);
                if(is_in_array(verticalIndex,i))
                    canvas.drawText(nameOfPointsSet[counter],lastcx+100,lastcy,currentPaint);
                else
                    canvas.drawText(nameOfPointsSet[counter],lastcx,lastcy-20,currentPaint);
                counter++;
            }

        }


    }

    public void draw_line_between_points()
    {
        for(int i=0;i<dragDropPoints.size();i++)
        {
            float startX=dragDropPoints.get(i).get(0).x;
            float startY=dragDropPoints.get(i).get(0).y;

            setCurrentPaint(4,colorOfPointsSet[i]);
            for(int j=0;j<dragDropPoints.get(i).size();j++)
            {
                float stopX=dragDropPoints.get(i).get(j).x;
                float stopY=dragDropPoints.get(i).get(j).y;
                if(j!=0)
                {
                    canvas.drawLine(startX,startY,stopX,stopY,currentPaint);
                    startX=stopX;
                    startY=stopY;
                }

            }
        }
    }

    public void draw_line_between_intersection_points()
    {

    }

    public void draw_vertical_start(int index,int numberOfPoints,float startX,float startY)
    {
        for(int i=0;i<numberOfPoints;i++)
        {
            dragDropPoints.get(index).add(i,new Point((int)startX,(int)startY));

            startY+=70;
        }
        lastStartX=startX;
        lastStartY=startY+50;
        //Log.d("Vertical","Draw vertical içerisinde");

    }
    public void draw_horizontal_start(int index,int numberOfPoints,float startX,float startY)
    {
        for(int i=0;i<numberOfPoints;i++)
        {
            dragDropPoints.get(index).add(i,new Point((int)startX,(int)startY));

            startX+=100;
        }
        lastStartX=startX;
        lastStartY=startY+50;
        //Log.d("Horizontal","Draw Horizontal içerisinde");

    }

    public boolean is_in_array(int[] searchArray,int element)
    {
        for(int i:searchArray)
        {
            if(i==element)
                return true;
        }
        return false;
    }

    public void print_points(ArrayList<ArrayList<Point>> points)
    {
        for (int i=0;i<points.size();i++)
            for(int j=0;j<points.get(i).size();j++)
                System.out.println("Satır==="+i+"   Index==="+j+"     ElementX="+points.get(i).get(j).x+"    ElementY="+points.get(i).get(j).y);
    }

    public void setHeightLines(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        setCurrentPaint(4,0xFF00CC00);

        lastStartY=160;
        currentBitmap=Bitmap.createBitmap(display.getWidth(),display.getHeight(), Bitmap.Config.ARGB_8888);
        canvas=new Canvas(currentBitmap);
        lastTop=recYtop=lastStartY;
        lastBottom=recYbottom=lastStartY+250;
        recWidth=display.getWidth();
        //drawHeightLines();

    }

    public void drawHeightLines(Canvas canvas)
    {
        canvas.drawRect(0,recYtop,recWidth,recYbottom,currentPaint);

    }

    /*public void drawHeightLines(Canvas canvas,float cY)
    {
        if(Math.abs(cY-recYtop)<Math.abs(cY-recYbottom) && cY<recYbottom-20)
        {
            canvas.drawRect(0,cY,recWidth,lastBottom,currentPaint);

            lastTop=cY;
        }

        else if(Math.abs(cY-recYtop)>Math.abs(cY-recYbottom) && cY>recYtop+20)
        {
            canvas.drawRect(0,lastTop,recWidth,cY,currentPaint);
            lastBottom=cY;

        }

        else
        {
            canvas.drawRect(0,lastTop,recWidth,lastBottom,currentPaint);
        }



    }*/

    public void drawHeightLines(Canvas canvas,float cY)
    {
        if(Math.abs(cY-recYtop)<Math.abs(cY-recYbottom) && cY<recYbottom-20)
        {
            canvas.drawRect(0,cY,recWidth,recYbottom,currentPaint);

            recYtop=cY;
        }

        else if(Math.abs(cY-recYtop)>Math.abs(cY-recYbottom) && cY>recYtop+20)
        {
            canvas.drawRect(0,recYtop,recWidth,cY,currentPaint);
            recYbottom=cY;

        }

        else
        {
            canvas.drawRect(0,recYtop,recWidth,recYbottom,currentPaint);
        }



    }

    public void setRatio(float realHeight)
    {
        ratio=realHeight/getHeightFromImage();
    }

    /*public float getHeightFromImage()
    {
        return lastBottom-lastTop;
    }*/

    public float getHeightFromImage()
    {
        return Math.abs(recYbottom-recYtop);
    }

    public void calculateDegrees()
    {
        //Log.d("CALCULATEDEGREE","ICERDE");
        degreesBtwPoints=new ArrayList<ArrayList<Float>>(dragDropPoints.size());
        for(int i=0;i<dragDropPoints.size();i++)
        {
            degreesBtwPoints.add(new ArrayList<Float>(dragDropPoints.get(i).size()));

            for(int j=1;j<dragDropPoints.get(i).size();j++)
            {
                Point p1,p2;
                p1=dragDropPoints.get(i).get(j-1);
                p2=dragDropPoints.get(i).get(j);

                //double d=Math.abs((double)(p2.x-p1.x))/Math.abs((double)p2.y-p1.y);
                float d1=(float)Math.toDegrees(Math.atan(Math.abs((double)p2.x-p1.x)/Math.abs((double)p2.y-p1.y)));
                float d2=90-d1;
                float degree=((d1<d2)?d1:d2);

                degreesBtwPoints.get(i).add(new Float(degree));
            }
        }

    }

    public void calculateDistances()
    {
        distancesBtwPoints=new ArrayList<ArrayList<Float>>(dragDropPoints.size());
        for(int i=0;i<dragDropPoints.size();i++)
        {
            distancesBtwPoints.add(new ArrayList<Float>(dragDropPoints.get(i).size()));

            for(int j=1;j<dragDropPoints.get(i).size();j++)
            {
                Point p1,p2;
                p1=dragDropPoints.get(i).get(j-1);
                p2=dragDropPoints.get(i).get(j);
                float distance=(float)Math.hypot(Math.abs(p1.x-p2.x),Math.abs(p1.y-p2.y));


                distancesBtwPoints.get(i).add(new Float(distance*ratio));
            }
        }
    }

    public void calculateVerticalDistances()
    {
        verticalDistances=new ArrayList<ArrayList<Float>>(dragDropPoints.size());
        for(int i=0;i<dragDropPoints.size();i++)
        {
            verticalDistances.add(new ArrayList<Float>(dragDropPoints.get(i).size()));

            for(int j=0;j<dragDropPoints.get(i).size();j++)
            {
                Point p1;
                p1=dragDropPoints.get(i).get(j);
                float distance=(float)lastCx-p1.x;
                Log.d("POINT#"+i+j,""+distance);


                verticalDistances.get(i).add(new Float(distance*ratio));
            }
        }
    }



}
