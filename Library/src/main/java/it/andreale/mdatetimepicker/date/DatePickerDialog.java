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
package it.andreale.mdatetimepicker.date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

import it.andreale.mdatetimepicker.DialogUtils;
import it.andreale.mdatetimepicker.R;

/**
 * Created by AndreAle94
 */
public class DatePickerDialog extends DialogFragment implements DatePickerController, View.OnClickListener {

    private final static int DEFAULT_ACCENT_COLOR = Color.parseColor("#009688");
    private final static int DEFAULT_START_YEAR = 1900;
    private final static int DEFAULT_END_YEAR = 2100;
    private final static int DEFAULT_PICKER_MODE = DateHeaderView.MODE_DAY_PICKER;
    private final static int DEFAULT_FIRST_DAY_OF_WEEK = Calendar.SUNDAY;
    private final static String DEFAULT_HEADER_DATE_FORMAT = "EEE, MMM dd";
    private final static int BACKGROUND_LIGHT = Color.parseColor("#FAFAFA");
    private final static int BACKGROUND_DARK = Color.parseColor("#424242");

    private final static String SAVED_HEADER_COLOR = "dpd:headerColor";
    private final static String SAVED_SELECTION_COLOR = "dpd:selectionColor";
    private final static String SAVED_POSITIVE_COLOR = "dpd:positiveColor";
    private final static String SAVED_NEGATIVE_COLOR = "dpd:negativeColor";
    private final static String SAVED_POSITIVE_TEXT = "dpd:positiveText";
    private final static String SAVED_NEGATIVE_TEXT = "dpd:negativeText";
    private final static String SAVED_TODAY_COLOR = "dpd:todayColor";
    private final static String SAVED_DARK_MODE = "dpd:darkMode";
    private final static String SAVED_DEFAULT_MODE = "dpd:defaultMode";
    private final static String SAVED_PICKER_MODE = "dpd:pickerMode";
    private final static String SAVED_START_YEAR = "dpd:startYear";
    private final static String SAVED_END_YEAR = "dpd:endYear";
    private final static String SAVED_CALENDAR = "dpd:internalCalendar";
    private final static String SAVED_HEADER_DATE_FORMAT = "dpd:headerFormat";

    private int mHeaderColor;
    private int mSelectionColor;
    private int mPositiveColor;
    private int mNegativeColor;
    private boolean mDarkTheme;
    private int mTodayColor;

    private String mPositiveText;
    private String mNegativeText;
    private int mYearStart;
    private int mYearEnd;
    private Calendar mCalendar;
    private int mDefaultPickerMode;
    private String mHeaderDateFormat;
    private int mFirstDayOfWeek;
    private boolean mBuilderFlag;

    // components
    private DateHeaderView mHeaderView;
    private YearPickerView mYearPickerView;
    private DayPickerView mDayPickerView;

    private Button mPositiveButton;
    private Button mNegativeButton;

    // callback
    private OnDateSetListener mDateSetListener;

    public DatePickerDialog() {
        mBuilderFlag = false;
    }

    private static DatePickerDialog newInstance() {
        return new DatePickerDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_HEADER_COLOR, mHeaderColor);
        outState.putInt(SAVED_SELECTION_COLOR, mSelectionColor);
        outState.putInt(SAVED_POSITIVE_COLOR, mPositiveColor);
        outState.putInt(SAVED_NEGATIVE_COLOR, mNegativeColor);
        outState.putInt(SAVED_TODAY_COLOR, mTodayColor);
        outState.putBoolean(SAVED_DARK_MODE, mDarkTheme);
        outState.putString(SAVED_HEADER_DATE_FORMAT, mHeaderDateFormat);
        outState.putInt(SAVED_PICKER_MODE, mHeaderView.getPickerMode());
        outState.putInt(SAVED_START_YEAR, mYearStart);
        outState.putInt(SAVED_END_YEAR, mYearEnd);
        outState.putString(SAVED_POSITIVE_TEXT, mPositiveText);
        outState.putString(SAVED_NEGATIVE_TEXT, mNegativeText);
        outState.putSerializable(SAVED_CALENDAR, mCalendar);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnDateSetListener) {
            mDateSetListener = (OnDateSetListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDateSetListener = null;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // restore old state if not null
        if (savedInstanceState != null) {
            mHeaderColor = savedInstanceState.getInt(SAVED_HEADER_COLOR, DEFAULT_ACCENT_COLOR);
            mSelectionColor = savedInstanceState.getInt(SAVED_SELECTION_COLOR, DEFAULT_ACCENT_COLOR);
            mPositiveColor = savedInstanceState.getInt(SAVED_POSITIVE_COLOR, DEFAULT_ACCENT_COLOR);
            mNegativeColor = savedInstanceState.getInt(SAVED_NEGATIVE_COLOR, DEFAULT_ACCENT_COLOR);
            mTodayColor = savedInstanceState.getInt(SAVED_TODAY_COLOR, DEFAULT_ACCENT_COLOR);
            mDarkTheme = savedInstanceState.getBoolean(SAVED_DARK_MODE, false);
            mDefaultPickerMode = savedInstanceState.getInt(SAVED_DEFAULT_MODE, DEFAULT_PICKER_MODE);
            mYearStart = savedInstanceState.getInt(SAVED_START_YEAR, DEFAULT_START_YEAR);
            mYearEnd = savedInstanceState.getInt(SAVED_END_YEAR, DEFAULT_END_YEAR);
            mCalendar = (Calendar) savedInstanceState.getSerializable(SAVED_CALENDAR);
            mHeaderDateFormat = savedInstanceState.getString(SAVED_HEADER_DATE_FORMAT, DEFAULT_HEADER_DATE_FORMAT);
            mPositiveText = savedInstanceState.getString(SAVED_POSITIVE_TEXT, getString(android.R.string.ok));
            mNegativeText = savedInstanceState.getString(SAVED_NEGATIVE_TEXT, getString(android.R.string.cancel));
            mBuilderFlag = true;
            if (mCalendar != null) {
                mFirstDayOfWeek = mCalendar.getFirstDayOfWeek();
            } else {
                mFirstDayOfWeek = DEFAULT_FIRST_DAY_OF_WEEK;
            }
        } else {
            mDefaultPickerMode = DEFAULT_PICKER_MODE;
            mCalendar.setFirstDayOfWeek(mFirstDayOfWeek);
            // check builder flag
            if (!mBuilderFlag) {
                throw new IllegalStateException("You must initialize DatePickerDialog throw Builder class");
            }
        }
        // inflate view
        View view = inflatePickerView(savedInstanceState);
        // style views
        mHeaderView.setBackgroundColor(mHeaderColor);
        view.setBackgroundColor(mDarkTheme ? BACKGROUND_DARK : BACKGROUND_LIGHT);
        // create dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        return dialog;
    }

    protected View inflatePickerView(Bundle savedInstanceState) {
        // inflate view
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.md_date_picker_dialog, null);
        mHeaderView = (DateHeaderView) view.findViewById(R.id.date_header_view);
        mYearPickerView = (YearPickerView) view.findViewById(R.id.date_year_picker_view);
        mDayPickerView = (DayPickerView) view.findViewById(R.id.date_day_picker_view);
        // buttons
        mPositiveButton = (Button) view.findViewById(R.id.positive_button);
        mNegativeButton = (Button) view.findViewById(R.id.negative_button);
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);
        mPositiveButton.setTextColor(mPositiveColor);
        mNegativeButton.setTextColor(mNegativeColor);
        mPositiveButton.setText(mPositiveText);
        mNegativeButton.setText(mNegativeText);
        // configure views
        if (savedInstanceState != null) {
            mHeaderView.setPickerMode(savedInstanceState.getInt(SAVED_PICKER_MODE));
        }
        mHeaderView.registerController(this);
        mDayPickerView.registerController(this);
        mYearPickerView.resisterController(this);
        return view;
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        mDateSetListener = listener;
    }

    @Override
    public int getDefaultMode() {
        return mDefaultPickerMode;
    }

    @Override
    public void onPickerModeChange(int pickerMode) {
        switch (pickerMode) {
            case DateHeaderView.MODE_DAY_PICKER:
                DialogUtils.setVisible(mDayPickerView, true);
                DialogUtils.setVisible(mYearPickerView, false);
                break;
            case DateHeaderView.MODE_YEAR_PICKER:
                mYearPickerView.onPreShow();
                DialogUtils.setVisible(mDayPickerView, false);
                DialogUtils.setVisible(mYearPickerView, true);
                break;
        }
    }

    @Override
    public int getStartYear() {
        return mYearStart;
    }

    @Override
    public int getEndYear() {
        return mYearEnd;
    }

    @Override
    public Calendar getSelectedDate() {
        return mCalendar;
    }

    @Override
    public void onYearChanged(int year) {
        mCalendar.set(Calendar.YEAR, year);
        mYearPickerView.notifyDateChanged();
        mHeaderView.updateDate();
        mDayPickerView.onDateChanged();
        mHeaderView.setPickerMode(DateHeaderView.MODE_DAY_PICKER);
    }

    @Override
    public boolean isDarkMode() {
        return mDarkTheme;
    }

    @Override
    public int getSelectionColor() {
        return mSelectionColor;
    }

    @Override
    public int getTodayColor() {
        return mTodayColor;
    }

    @Override
    public String getHeaderDateFormat() {
        return mHeaderDateFormat;
    }

    @Override
    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    @Override
    public void onDayClicked(int day, int month, int year) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mHeaderView.updateDate();
        mDayPickerView.onDateChanged();
        mYearPickerView.notifyDateChanged();
    }

    @Override
    public int getDirectionalButtonColor() {
        return mDarkTheme ? Color.WHITE : Color.BLACK;
    }

    @Override
    public int getMonthHeaderTextColor() {
        return mDarkTheme ? Color.WHITE : Color.BLACK;
    }

    @Override
    public void onClick(View v) {
        if (v == mPositiveButton) {
            if (mDateSetListener != null) {
                mDateSetListener.onDateSet(
                        this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
            }
            dismiss();
        } else if (v == mNegativeButton) {
            dismiss();
        }
    }

    public static class Builder {

        private Context mContext;
        private int mAccentColor;
        private boolean mDarkMode;
        private int mPositiveColor;
        private int mNegativeColor;
        private int mHeaderColor;
        private int mSelectionColor;
        private int mTodayColor;
        private String mHeaderDateFormat;
        private Calendar mCalendar;
        private int mStartYear;
        private int mEndYear;
        private int mFirstDayOfWeek;
        private String mPositiveText;
        private String mNegativeText;

        private boolean mHeaderSet;

        public Builder(Context context) {
            mContext = context;
            mAccentColor = DialogUtils.resolveColor(context, R.attr.colorAccent, DEFAULT_ACCENT_COLOR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAccentColor = DialogUtils.resolveColor(context, android.R.attr.colorAccent, mAccentColor);
            }
            mDarkMode = DialogUtils.resolveBoolean(context, R.attr.mdp_darkMode, false);
            mPositiveColor = DialogUtils.resolveColor(context, R.attr.mdp_positiveColor, mAccentColor);
            mNegativeColor = DialogUtils.resolveColor(context, R.attr.mdp_negativeColor, mAccentColor);
            mTodayColor = DialogUtils.resolveColor(context, R.attr.mdp_todayTextColor, mAccentColor);
            mHeaderColor = DialogUtils.resolveColor(context, R.attr.mdp_headerBackgroundColor, mAccentColor);
            mHeaderSet = mHeaderColor != mAccentColor;
            mSelectionColor = DialogUtils.resolveColor(context, R.attr.mdp_selectedCircleColor, mAccentColor);
            mCalendar = Calendar.getInstance();
            DialogUtils.truncate(mCalendar);
            mStartYear = DEFAULT_START_YEAR;
            mEndYear = DEFAULT_END_YEAR;
            mFirstDayOfWeek = DEFAULT_FIRST_DAY_OF_WEEK;
            mHeaderDateFormat = DEFAULT_HEADER_DATE_FORMAT;
        }

        public Builder darkMode(boolean darkMode) {
            mDarkMode = darkMode;
            return this;
        }

        public Builder positiveColor(int color) {
            mPositiveColor = color;
            return this;
        }

        public Builder negativeColor(int color) {
            mNegativeColor = color;
            return this;
        }

        public Builder todayTextColor(int color) {
            mTodayColor = color;
            return this;
        }

        public Builder headerBackgroundColor(int color) {
            mHeaderColor = color;
            mHeaderSet = true;
            return this;
        }

        public Builder selectedCircleColor(int color) {
            mSelectionColor = color;
            return this;
        }

        public Builder selectedDate(Calendar calendar) {
            return selectedDate(
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
            );
        }

        public Builder selectedDate(Date date) {
            mCalendar.setTime(date);
            DialogUtils.truncate(mCalendar);
            return this;
        }

        public Builder selectedDate(int day, int month, int year) {
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.YEAR, year);
            return this;
        }

        public Builder yearRange(int startYear, int endYear) {
            if (startYear > endYear) {
                throw new IllegalArgumentException("Start year can be later than end year!");
            }
            mStartYear = startYear;
            mEndYear = endYear;
            return this;
        }

        public Builder firstDayOfWeek(int firstDayOfWeek) {
            if (firstDayOfWeek < Calendar.SUNDAY || firstDayOfWeek > Calendar.SATURDAY) {
                throw new IllegalArgumentException("You must provide a valid day of week. See Calendar class");
            }
            mFirstDayOfWeek = firstDayOfWeek;
            return this;
        }

        public Builder headerDateFormat(String format) {
            mHeaderDateFormat = format;
            return this;
        }

        public Builder positiveText(String text) {
            mPositiveText = text;
            return this;
        }

        public Builder positiveText(int text) {
            mPositiveText = mContext.getString(text);
            return this;
        }

        public Builder negativeText(String text) {
            mNegativeText = text;
            return this;
        }

        public Builder negativeText(int text) {
            mNegativeText = mContext.getString(text);
            return this;
        }

        public DatePickerDialog build() {
            if (mPositiveText == null) {
                mPositiveText = mContext.getString(android.R.string.ok);
            }
            if (mNegativeText == null) {
                mNegativeText = mContext.getString(android.R.string.cancel);
            }
            if (!mHeaderSet && mDarkMode) {
                mHeaderColor = Color.parseColor("#555555");
            }
            DatePickerDialog dialog = DatePickerDialog.newInstance();
            dialog.mCalendar = mCalendar;
            dialog.mFirstDayOfWeek = mFirstDayOfWeek;
            dialog.mDarkTheme = mDarkMode;
            dialog.mPositiveColor = mPositiveColor;
            dialog.mNegativeColor = mNegativeColor;
            dialog.mYearStart = mStartYear;
            dialog.mYearEnd = mEndYear;
            dialog.mHeaderDateFormat = mHeaderDateFormat;
            dialog.mHeaderColor = mHeaderColor;
            dialog.mSelectionColor = mSelectionColor;
            dialog.mTodayColor = mTodayColor;
            dialog.mPositiveText = mPositiveText;
            dialog.mNegativeText = mNegativeText;
            dialog.mBuilderFlag = true;
            return dialog;
        }

        public void show(FragmentManager fragmentManager, String tag) {
            DatePickerDialog dialog = build();
            dialog.show(fragmentManager, tag);
        }
    }
}