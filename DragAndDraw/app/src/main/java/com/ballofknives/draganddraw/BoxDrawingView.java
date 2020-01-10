package com.ballofknives.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    public static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    //constructor for instantiating view from java
    public BoxDrawingView(Context context){
        this(context, null);
        setSaveEnabled(true);
    }

    //constructor for instantiating view from xml
    public BoxDrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
        setSaveEnabled(true);
    }

    public boolean onTouchEvent(MotionEvent event){
        PointF curr = new PointF(event.getX(), event.getY());

        Log.i(TAG, "Received event at (" + curr.x + "," + curr.y + ")");
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION DOWN!");
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE!");
                if(mCurrentBox != null){
                    mCurrentBox.setCurrent(curr);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP!");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_CANCEL!");
                mCurrentBox = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes){
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Log.d(TAG,"Saving Instance State");
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mBoxList = mBoxes;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        Log.d(TAG,"Restoring Instance State");
        mBoxes = ss.mBoxList;
    }

    private static class SavedState extends BaseSavedState {
        private ArrayList<Box> mBoxList;

        public SavedState(Parcelable superState) {
            super(superState);
            Log.d(TAG, "Saving parcelable");
        }
    }

}
