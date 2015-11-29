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
package it.andreale.mdatepicker.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.andreale.mdatepicker.DialogUtils;

/**
 * Created by AndreAle94
 */
public class MonthView extends View implements View.OnTouchListener {

    private final static int EMPTY_DAY = 0;
    private final static int DAY_OF_WEEK = 7;
    private final static int MAX_WEEK_IN_MONTH = 6;

    private String[] mDayLabel;
    private int[][] mDaysMatrix;

    private Paint mPaint;
    private Rect mRect;

    private Calendar mCalendar;
    private Date mTodayDate;

    private MonthController mController;
    private long mTouchDownTime;

    public MonthView(Context context) {
        super(context);
        initialize();
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mDayLabel = new String[DAY_OF_WEEK];
        mDaysMatrix = new int[DAY_OF_WEEK][MAX_WEEK_IN_MONTH];
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new Rect();
        mCalendar = Calendar.getInstance();
        DialogUtils.truncate(mCalendar);
        mTodayDate = mCalendar.getTime();
        setOnTouchListener(this);
    }

    public void registerController(int position, MonthController controller) {
        mController = controller;
        // initialize month view
        mCalendar.set(Calendar.YEAR, mController.getYear(position));
        mCalendar.set(Calendar.MONTH, mController.getMonth(position));
        // generate week day array
        int firstDayOfWeek = mController.getFirstDayOfWeek();
        int[] weekDays = new int[DAY_OF_WEEK];
        int fixedIndex = firstDayOfWeek;
        for (int i = 0; i < DAY_OF_WEEK; i++) {
            // calculate fixed index
            if (fixedIndex > Calendar.SATURDAY) {
                fixedIndex = 1;
            }
            weekDays[i] = fixedIndex;
            fixedIndex += 1;
        }
        // generate week day name array
        String[] weekdays = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
        for (int i = 0; i < DAY_OF_WEEK; i++) {
            mDayLabel[i] = weekdays[weekDays[i]].substring(0, 1).toUpperCase(Locale.getDefault());
        }
        // generate days matrix. find first day position
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int skip = 0;
        for (int i = 0; i < DAY_OF_WEEK; i++) {
            if (weekDays[i] != dayOfWeek) {
                skip += 1;
                continue;
            }
            break;
        }
        int lastDay = 0;
        for (int i = 0; i < MAX_WEEK_IN_MONTH; i++) {
            for (int j = 0; j < DAY_OF_WEEK; j++) {
                // check if should write empty day
                if (skip > 0) {
                    skip -= 1;
                    mDaysMatrix[j][i] = EMPTY_DAY;
                    continue;
                }
                if (lastDay >= dayOfMonth) {
                    mDaysMatrix[j][i] = EMPTY_DAY;
                    continue;
                }
                lastDay += 1;
                mDaysMatrix[j][i] = lastDay;
            }
        }
    }

    public void unregisterController() {
        mController = null;
    }

    private boolean isSelected(int day) {
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        return mCalendar.getTime().compareTo(mController.getSelectedDate()) == 0;
    }

    private boolean isToday(int day) {
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        return mCalendar.getTime().compareTo(mTodayDate) == 0;
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
        float cellHeight = height / 7;
        onDrawDayLabels(c, width, cellHeight);
        onDrawDaysMatrix(c, width, cellHeight * MAX_WEEK_IN_MONTH, cellHeight);
    }

    private float getTextWidth(String text) {
        return mPaint.measureText(text);
    }

    private float getTextHeight(String text) {
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        return mRect.height();
    }

    protected void onDrawDayLabels(Canvas c, float width, float height) {
        float cellWidth = width / DAY_OF_WEEK;
        mPaint.setColor(mController.getWeekDayColor());
        mPaint.setTextSize(height / 12 * 5);
        for (int i = 0; i < mDayLabel.length; i++) {
            // measure text
            // measureTextWidth(mDayLabel[i]);
            String text = mDayLabel[i];
            float x = (i * cellWidth) + (cellWidth / 2) - (getTextWidth(text) / 2);
            float y = (height + getTextHeight(text)) / 2;
            onDrawDayLabel(text, c, x, y);
        }
    }

    protected void onDrawDayLabel(String day, Canvas c, float x, float y) {
        c.drawText(day, x, y, mPaint);
    }

    protected void onDrawDaysMatrix(Canvas c, float width, float height, float topOffset) {
        float cellX = width / DAY_OF_WEEK;
        float cellY = height / MAX_WEEK_IN_MONTH;
        for (int i = 0; i < MAX_WEEK_IN_MONTH; i++) {
            for (int j = 0; j < DAY_OF_WEEK; j++) {
                int day = mDaysMatrix[j][i];
                if (day > 0) {
                    // calculate x and y
                    String dayText = String.valueOf(day);
                    // measureTextWidth(dayText);
                    float x = j * cellX;
                    float y = (i * cellY) + topOffset;
                    // check day type
                    boolean isToday = isToday(day);
                    boolean isSelected = isSelected(day);
                    if (isSelected) {
                        // draw selector
                        onDrawDaySelector(c, x, y, cellX, cellY);
                    }
                    // calculate text start point
                    x += (cellX - getTextWidth(dayText)) / 2;
                    y += (cellY + getTextHeight(dayText)) / 2;
                    // draw on canvas
                    onDrawDay(dayText, c, x, y, isToday, isSelected);
                }
            }
        }
    }

    protected void onDrawDaySelector(Canvas c, float cx, float cy, float width, float height) {
        mPaint.setColor(mController.getSelectionColor());
        float radius = Math.min(width, height) / 2;
        float x = cx + (width / 2);
        float y = cy + (height / 2);
        c.drawCircle(x, y, radius, mPaint);
    }

    protected void onDrawDay(String day, Canvas c, float x, float y, boolean isToday, boolean isSelected) {
        if (isSelected) {
            mPaint.setColor(mController.getSelectedTextColor());
        } else if (isToday) {
            mPaint.setColor(mController.getTodayTextColor());
        } else {
            mPaint.setColor(mController.getStandardTextColor());
        }
        c.drawText(day, x, y, mPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean handled = false;
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mTouchDownTime = SystemClock.elapsedRealtime();
                handled = true;
                break;
            case MotionEvent.ACTION_UP:
                if (SystemClock.elapsedRealtime() - mTouchDownTime <= 100){
                    float x = event.getX();
                    float y = event.getY();
                    handled = onClick(x, y);
                }
                break;
        }
        return handled;
    }

    private boolean onClick(float x, float y) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (Math.min(width, height) == 0) {
            // skip check
            return false;
        }
        if (mController == null) {
            // skip check
            return false;
        }
        // calculate cells size
        float cellWidth = width / DAY_OF_WEEK;
        float cellHeight = height / (MAX_WEEK_IN_MONTH + 1);
        // calculate click position
        int column = (int) (x / cellWidth);
        int row = (int) (y / cellHeight) - 1;
        // check if is a valid cell
        int day = mDaysMatrix[column][row];
        if (day > 0) {
            mController.onDayClicked(day, mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR));
        }
        return false;
    }

    public interface MonthController {

        int getFirstDayOfWeek();

        int getYear(int position);

        int getMonth(int position);

        Date getSelectedDate();

        int getWeekDayColor();

        int getTodayTextColor();

        int getSelectionColor();

        int getSelectedTextColor();

        int getStandardTextColor();

        void onDayClicked(int day, int month, int year);
    }
}