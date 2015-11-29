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
package it.andreale.mdatepicker.time;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

import it.andreale.mdatepicker.DialogUtils;
import it.andreale.mdatepicker.R;

/**
 * Created by AndreAle94
 */
public class TimeHeaderView extends RelativeLayout implements View.OnClickListener {

    public static final int MODE_UNINITIALIZED = -1;
    public static final int MODE_HOUR_PICKER = 0;
    public static final int MODE_MINUTE_PICKER = 1;

    private final static int HOURS_HALF_DAY = 12;
    private final static int TRANSPARENT_WHITE = DialogUtils.getTransparentColor(Color.WHITE);

    private int mPickerMode;
    private TimePickerController mController;
    private DecimalFormat mMinuteFormatter;

    private LinearLayout mModeLayout;
    private TextView mHourTextView;
    private TextView mMinuteTextView;
    private TextView mModeAM;
    private TextView mModePM;

    public TimeHeaderView(Context context) {
        super(context);
        initialize();
    }

    public TimeHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TimeHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mPickerMode = MODE_UNINITIALIZED;
        mController = null;
        mMinuteFormatter = new DecimalFormat("00");
        // inflate and setup
        inflate(getContext(), R.layout.md_time_picker_header_layout, this);
        mModeLayout = (LinearLayout) findViewById(R.id.time_mode_layout);
        mHourTextView = (TextView) findViewById(R.id.header_hour);
        mMinuteTextView = (TextView) findViewById(R.id.header_minute);
        mModeAM = (TextView) findViewById(R.id.time_mode_am);
        mModePM = (TextView) findViewById(R.id.time_mode_pm);
        mHourTextView.setOnClickListener(this);
        mMinuteTextView.setOnClickListener(this);
        mModeAM.setOnClickListener(this);
        mModePM.setOnClickListener(this);
    }

    public void setPickerMode(int pickerMode) {
        mPickerMode = pickerMode;
    }

    public int getPickerMode() {
        return mPickerMode;
    }

    public void registerController(TimePickerController controller) {
        mController = controller;
        if (mController != null) {
            if (mPickerMode == MODE_UNINITIALIZED) {
                notifyController(mController.getDefaultMode(), false);
            } else {
                notifyController(mPickerMode, true);
            }
            updateTime();
        }
    }

    private void notifyController(int pickerMode, boolean forced) {
        if (mPickerMode != pickerMode || forced) {
            mPickerMode = pickerMode;
            if (mController != null) {
                mController.onPickerModeChange(mPickerMode);
                // change text color
                if (pickerMode == MODE_HOUR_PICKER) {
                    mHourTextView.setTextColor(Color.WHITE);
                    mMinuteTextView.setTextColor(TRANSPARENT_WHITE);
                } else if (pickerMode == MODE_MINUTE_PICKER) {
                    mHourTextView.setTextColor(TRANSPARENT_WHITE);
                    mMinuteTextView.setTextColor(Color.WHITE);
                }
            }
        }
    }

    public void updateTime() {
        boolean is24HourMode = mController.is24HourMode();
        DialogUtils.setVisible(mModeLayout, !is24HourMode);
        Calendar calendar = mController.getSelectedTime();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (!is24HourMode) {
            switch (calendar.get(Calendar.AM_PM)) {
                case Calendar.AM:
                    mModeAM.setTextColor(Color.WHITE);
                    mModePM.setTextColor(TRANSPARENT_WHITE);
                    break;
                case Calendar.PM:
                    mModeAM.setTextColor(TRANSPARENT_WHITE);
                    mModePM.setTextColor(Color.WHITE);
                    break;
            }
            if (hour > HOURS_HALF_DAY) {
                hour -= HOURS_HALF_DAY;
            }
        }
        mHourTextView.setText(String.valueOf(hour));
        mMinuteTextView.setText(mMinuteFormatter.format(minute));
    }

    @Override
    public void onClick(View v) {
        if (v == mHourTextView) {
            notifyController(MODE_HOUR_PICKER, false);
        } else if (v == mMinuteTextView) {
            notifyController(MODE_MINUTE_PICKER, false);
        } else if (v == mModeAM) {
            Calendar calendar = mController.getSelectedTime();
            if (calendar.get(Calendar.AM_PM) != Calendar.AM) {
                calendar.set(Calendar.AM_PM, Calendar.AM);
            }
            updateTime();
        } else if (v == mModePM) {
            Calendar calendar = mController.getSelectedTime();
            if (calendar.get(Calendar.AM_PM) != Calendar.PM) {
                calendar.set(Calendar.AM_PM, Calendar.PM);
            }
            updateTime();
        }
    }
}