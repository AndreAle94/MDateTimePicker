/*
 * ******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 AndreAle94
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * *****************************************************************************
 */
package it.andreale.mdatetimepicker.time;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * Created by AndreAle94
 */
public class RadialSelectorView extends View implements View.OnTouchListener {

    private final static int DEGREES_FOR_MINUTE = 6;
    private final static int DEGREES_FOR_HOUR = 15;

    private TimePickerController mController;

    private Paint mPaint;
    private Rect mRect;
    private int mPickerMode;

    public RadialSelectorView(Context context) {
        super(context);
        initialize();
    }

    public RadialSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public RadialSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new Rect();
        mPickerMode = TimeHeaderView.MODE_UNINITIALIZED;
        setOnTouchListener(this);
    }

    public void registerController(TimePickerController controller) {
        mController = controller;
    }

    public void notifyPickerModeChanged(int pickerMode) {
        mPickerMode = pickerMode;
        invalidate();
    }

    private float getTextWidth(String text) {
        return mPaint.measureText(text);
    }

    private float getTextHeight(String text) {
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        return mRect.height();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (Math.min(width, height) == 0) {
            // skip drawing
            return;
        }
        if (mController == null) {
            // skip drawing
            return;
        }
        if (mPickerMode == TimeHeaderView.MODE_UNINITIALIZED) {
            // skip drawing
            return;
        }
        // calculate real size
        float drawableWidth = width - (getPaddingRight() + getPaddingLeft());
        float drawableHeight = height - (getPaddingTop() + getPaddingBottom());
        // calculate appropriate radius
        float circleRadius = Math.min(drawableWidth, drawableHeight) / 2;
        // calculate circle center coordinates
        float centerX = getPaddingLeft() + (drawableWidth / 2);
        float centerY = getPaddingTop() + (drawableHeight / 2);
        // draw background circle
        onDrawBackgroundCircle(c, centerX, centerY, circleRadius);
        onDrawSelector(c, centerX, centerY, circleRadius);
        onDrawNumbers(c, centerX, centerY, circleRadius);
    }

    protected void onDrawBackgroundCircle(Canvas c, float cx, float cy, float radius) {
        mPaint.setColor(mController.getCircleBackgroundColor());
        c.drawCircle(cx, cy, radius, mPaint);
    }

    protected void onDrawSelector(Canvas c, float cx, float cy, float radius) {
        mPaint.setColor(mController.getSelectorColor());
        // draw center circle
        c.drawCircle(cx, cy, radius / 40, mPaint);
        // calculate center of secondary circle
        int degrees = 0;
        float fixedRadius = radius * 0.8f;
        if (mPickerMode == TimeHeaderView.MODE_MINUTE_PICKER) {
            int minute = mController.getSelectedTime().get(Calendar.MINUTE);
            degrees = DEGREES_FOR_MINUTE * minute;
        } else if (mPickerMode == TimeHeaderView.MODE_HOUR_PICKER) {
            Calendar calendar = mController.getSelectedTime();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            degrees = DEGREES_FOR_HOUR * 2 * hour;
            if (mController.is24HourMode()) {
                if (hour > 0 && hour < 13) {
                    fixedRadius = radius * 0.6f;
                }
            }
        }
        double fixedAngle = Math.toRadians(90 - degrees);
        float centerX = (float) (cx + fixedRadius * Math.cos(fixedAngle));
        float centerY = (float) (cy - fixedRadius * Math.sin(fixedAngle));
        c.drawCircle(centerX, centerY, radius * 0.15f, mPaint);
        // draw line
        mPaint.setStrokeWidth(radius / 80);
        c.drawLine(cx, cy, centerX, centerY, mPaint);
    }

    protected void onDrawNumbers(Canvas c, float cx, float cy, float radius) {
        mPaint.setTextSize(radius * 0.1f);
        if (mPickerMode == TimeHeaderView.MODE_MINUTE_PICKER) {
            int minute = mController.getSelectedTime().get(Calendar.MINUTE);
            for (int i = 0; i < 60; i += 5) {
                // calculate coordinates
                int degrees = DEGREES_FOR_MINUTE * i;
                double fixedAngle = Math.toRadians(90 - degrees);
                float centerX = (float) (cx + radius * 0.8f * Math.cos(fixedAngle));
                float centerY = (float) (cy - radius * 0.8f * Math.sin(fixedAngle));
                String number = String.valueOf(i);
                if (i < 10) {
                    number = "0" + number;
                }
                onDrawNumber(c, centerX, centerY, number, i == minute);
            }
        } else if (mPickerMode == TimeHeaderView.MODE_HOUR_PICKER) {
            Calendar calendar = mController.getSelectedTime();
            if (mController.is24HourMode()) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                for (int i = 0; i <= 23; i++) {
                    int degrees = DEGREES_FOR_HOUR * 2 * i;
                    double fixedAngle = Math.toRadians(90 - degrees);
                    float offset = (i == 0 || i > 12) ? 0.8f : 0.6f;
                    float centerX = (float) (cx + radius * offset * Math.cos(fixedAngle));
                    float centerY = (float) (cy - radius * offset * Math.sin(fixedAngle));
                    String number = String.valueOf(i);
                    onDrawNumber(c, centerX, centerY, number, i == hour);
                }
            } else {
                int hour = calendar.get(Calendar.HOUR);
                for (int i = 1; i <= 12; i++) {
                    int degrees = DEGREES_FOR_HOUR * 2 * i;
                    double fixedAngle = Math.toRadians(90 - degrees);
                    float centerX = (float) (cx + radius * 0.8f * Math.cos(fixedAngle));
                    float centerY = (float) (cy - radius * 0.8f * Math.sin(fixedAngle));
                    String number = String.valueOf(i);
                    onDrawNumber(c, centerX, centerY, number, i % 12 == hour);
                }
            }
        }
    }

    protected void onDrawNumber(Canvas c, float cx, float cy, String number, boolean selected) {
        mPaint.setColor(selected ? mController.getSelectedTextColor() : mController.getDefaultTextColor());
        // measure text to get bottom-left coordinates
        float x = cx - (getTextWidth(number) / 2);
        float y = cy + (getTextHeight(number) / 2);
        c.drawText(number, x, y, mPaint);
    }

    private boolean mTracking;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTracking = true;
                onMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTracking) {
                    onMove(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTracking = false;
                break;
        }
        return true;
    }

    private void onMove(float x, float y) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (Math.min(width, height) == 0) {
            // skip check
            return;
        }
        if (mController == null) {
            // skip check
            return;
        }
        // calculate real size
        float drawableWidth = width - (getPaddingRight() + getPaddingLeft());
        float drawableHeight = height - (getPaddingTop() + getPaddingBottom());
        // calculate appropriate radius
        float circleRadius = Math.min(drawableWidth, drawableHeight) / 2;
        // calculate circle center coordinates
        float centerX = getPaddingLeft() + (drawableWidth / 2);
        float centerY = getPaddingTop() + (drawableHeight / 2);
        // calculate point distance
        float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        // check picker mode
        if (mPickerMode == TimeHeaderView.MODE_HOUR_PICKER) {
            if (mController.is24HourMode()) {
                if (distance > circleRadius * 0.45f && distance < circleRadius) {
                    // calculate angle
                    double degrees = Math.toDegrees(Math.atan2(centerY - y, x - centerX));
                    float fixedAngle = 450 - (float) degrees + (DEGREES_FOR_HOUR);
                    int hour = ((int) fixedAngle / (DEGREES_FOR_HOUR * 2)) % 12;
                    if (distance > circleRadius * 0.7f) {
                        if (hour != 0) {
                            hour += 12;
                        }
                    } else {
                        if (hour == 0) {
                            hour = 12;
                        }
                    }
                    Calendar calendar = mController.getSelectedTime();
                    if (hour != calendar.get(Calendar.HOUR_OF_DAY)) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        mController.onTimeChanged();
                    }
                }
            } else {
                if (distance > circleRadius * 0.45f && distance < circleRadius) {
                    // calculate angle
                    double degrees = Math.toDegrees(Math.atan2(centerY - y, x - centerX));
                    float fixedAngle = 450 - (float) degrees + (DEGREES_FOR_HOUR);
                    int hour = ((int) fixedAngle / (DEGREES_FOR_HOUR * 2)) % 12;
                    Calendar calendar = mController.getSelectedTime();
                    if (hour != calendar.get(Calendar.HOUR)) {
                        calendar.set(Calendar.HOUR, hour);
                        mController.onTimeChanged();
                    }
                }
            }
        } else if (mPickerMode == TimeHeaderView.MODE_MINUTE_PICKER) {
            if (distance > circleRadius * 0.45f && distance < circleRadius) {
                // calculate angle
                double degrees = Math.toDegrees(Math.atan2(centerY - y, x - centerX));
                float fixedAngle = 450 - (float) degrees;
                int minute = ((int) fixedAngle / DEGREES_FOR_MINUTE) % 60;
                Calendar calendar = mController.getSelectedTime();
                if (minute != calendar.get(Calendar.MINUTE)) {
                    calendar.set(Calendar.MINUTE, minute);
                    mController.onTimeChanged();
                }
            }
        }
    }
}