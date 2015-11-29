package it.andreale.mdatepicker.time;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

import it.andreale.mdatepicker.DialogUtils;
import it.andreale.mdatepicker.R;

/**
 * Created by Andrea on 29/11/2015.
 */
public class TimePickerDialog extends DialogFragment implements TimePickerController, View.OnClickListener {

    private final static int DEFAULT_ACCENT_COLOR = Color.parseColor("#009688");
    private final static int BACKGROUND_LIGHT = Color.parseColor("#FAFAFA");
    private final static int BACKGROUND_DARK = Color.parseColor("#424242");

    private final static String SAVED_HEADER_COLOR = "tpd:headerColor";
    private final static String SAVED_POSITIVE_COLOR = "tpd:positiveColor";
    private final static String SAVED_NEGATIVE_COLOR = "tpd:negativeColor";
    private final static String SAVED_SELECTOR_COLOR = "tpd:selectorColor";
    private final static String SAVED_TEXT_COLOR = "tpd:textColor";
    private final static String SAVED_SELECTED_TEXT_COLOR = "tpd:selectedTextColor";
    private final static String SAVED_CIRCLE_COLOR = "tpd:circleColor";
    private final static String SAVED_POSITIVE_TEXT = "tpd:positiveText";
    private final static String SAVED_NEGATIVE_TEXT = "tpd:negativeText";
    private final static String SAVED_DARK_MODE = "tpd:darkMode";
    private final static String SAVED_HOUR_MODE = "tpd:hourMode";
    private final static String SAVED_CALENDAR = "tpd:internalCalendar";
    private final static String SAVED_PICKER_MODE = "tpd:pickerMode";

    private int mHeaderColor;
    private int mPositiveColor;
    private int mNegativeColor;
    private boolean mDarkTheme;
    private int mCircleColor;
    private int mDefaultTextColor;
    private int mSelectedTextColor;
    private int mSelectorColor;

    private String mPositiveText;
    private String mNegativeText;
    private Calendar mCalendar;
    private boolean m24HourMode;
    private boolean mBuilderFlag;

    // callback
    private OnTimeSetListener mTimeSetListener;

    // components
    private TimeHeaderView mHeaderView;
    private RadialSelectorView mSelectorView;

    private Button mPositiveButton;
    private Button mNegativeButton;

    public TimePickerDialog() {
        mBuilderFlag = false;
    }

    private static TimePickerDialog newInstance() {
        return new TimePickerDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_HEADER_COLOR, mHeaderColor);
        outState.putInt(SAVED_POSITIVE_COLOR, mPositiveColor);
        outState.putInt(SAVED_NEGATIVE_COLOR, mNegativeColor);
        outState.putInt(SAVED_CIRCLE_COLOR, mCircleColor);
        outState.putInt(SAVED_SELECTOR_COLOR, mSelectorColor);
        outState.putInt(SAVED_TEXT_COLOR, mDefaultTextColor);
        outState.putInt(SAVED_SELECTED_TEXT_COLOR, mSelectedTextColor);
        outState.putBoolean(SAVED_DARK_MODE, mDarkTheme);
        outState.putBoolean(SAVED_HOUR_MODE, m24HourMode);
        outState.putInt(SAVED_PICKER_MODE, mHeaderView.getPickerMode());
        outState.putString(SAVED_POSITIVE_TEXT, mPositiveText);
        outState.putString(SAVED_NEGATIVE_TEXT, mNegativeText);
        outState.putSerializable(SAVED_CALENDAR, mCalendar);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnTimeSetListener) {
            mTimeSetListener = (OnTimeSetListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTimeSetListener = null;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // restore old state if not null
        if (savedInstanceState != null) {
            mHeaderColor = savedInstanceState.getInt(SAVED_HEADER_COLOR, DEFAULT_ACCENT_COLOR);
            mPositiveColor = savedInstanceState.getInt(SAVED_POSITIVE_COLOR, DEFAULT_ACCENT_COLOR);
            mNegativeColor = savedInstanceState.getInt(SAVED_NEGATIVE_COLOR, DEFAULT_ACCENT_COLOR);
            mDarkTheme = savedInstanceState.getBoolean(SAVED_DARK_MODE, false);
            mCircleColor = savedInstanceState.getInt(SAVED_CIRCLE_COLOR, mDarkTheme ? Color.parseColor("#555555") : Color.parseColor("#eeeeee"));
            mSelectorColor = savedInstanceState.getInt(SAVED_SELECTOR_COLOR, DEFAULT_ACCENT_COLOR);
            mDefaultTextColor = savedInstanceState.getInt(SAVED_TEXT_COLOR, mDarkTheme ? Color.BLACK : Color.WHITE);
            mSelectedTextColor = savedInstanceState.getInt(SAVED_SELECTED_TEXT_COLOR, mDarkTheme ? Color.WHITE : Color.BLACK);
            mCalendar = (Calendar) savedInstanceState.getSerializable(SAVED_CALENDAR);
            mPositiveText = savedInstanceState.getString(SAVED_POSITIVE_TEXT, getString(android.R.string.ok));
            mNegativeText = savedInstanceState.getString(SAVED_NEGATIVE_TEXT, getString(android.R.string.cancel));
            m24HourMode = savedInstanceState.getBoolean(SAVED_HOUR_MODE, false);
            mBuilderFlag = true;
        } else {
            // check builder flag
            if (!mBuilderFlag) {
                throw new IllegalStateException("You must initialize TimePickerDialog throw Builder class");
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
        View view = inflater.inflate(R.layout.md_time_picker_dialog, null);
        mHeaderView = (TimeHeaderView) view.findViewById(R.id.time_header_view);
        mSelectorView = (RadialSelectorView) view.findViewById(R.id.time_selector_view);
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
        mSelectorView.registerController(this);
        mHeaderView.registerController(this);
        return view;
    }

    public void setOnTimeSetListener(OnTimeSetListener listener) {
        mTimeSetListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == mPositiveButton) {
            if (mTimeSetListener != null) {
                mTimeSetListener.onTimeSet(
                        this,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE)
                );
            }
            dismiss();
        } else if (v == mNegativeButton) {
            dismiss();
        }
    }

    @Override
    public int getDefaultMode() {
        return TimeHeaderView.MODE_HOUR_PICKER;
    }

    @Override
    public void onPickerModeChange(int pickerMode) {
        mSelectorView.notifyPickerModeChanged(pickerMode);
    }

    @Override
    public boolean is24HourMode() {
        return m24HourMode;
    }

    @Override
    public Calendar getSelectedTime() {
        return mCalendar;
    }

    @Override
    public int getCircleBackgroundColor() {
        return mCircleColor;
    }

    @Override
    public int getDefaultTextColor() {
        return mDefaultTextColor;
    }

    @Override
    public int getSelectedTextColor() {
        return mSelectedTextColor;
    }

    @Override
    public int getSelectorColor() {
        return mSelectorColor;
    }

    @Override
    public void onTimeChanged() {
        mHeaderView.updateTime();
        mSelectorView.invalidate();
    }

    public static class Builder {

        private Context mContext;
        private int mAccentColor;
        private boolean mDarkMode;
        private int mPositiveColor;
        private int mNegativeColor;
        private int mHeaderColor;
        private int mCircleColor;
        private boolean mCustomCircleColor;
        private Calendar mCalendar;
        private int mDefaultTextColor;
        private boolean mCustomTextColor;
        private int mSelectedTextColor;
        private boolean mCustomSelectedTextColor;
        private int mSelectorColor;
        private boolean m24HourMode;
        private String mPositiveText;
        private String mNegativeText;

        private boolean mHeaderSet;

        public Builder(Context context) {
            mContext = context;
            mAccentColor = DialogUtils.resolveColor(context, R.attr.colorAccent, DEFAULT_ACCENT_COLOR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAccentColor = DialogUtils.resolveColor(context, android.R.attr.colorAccent, mAccentColor);
            }
            mDarkMode = DialogUtils.resolveBoolean(context, R.attr.mdt_darkMode, false);
            m24HourMode = DialogUtils.resolveBoolean(context, R.attr.mdt_24HourMode, false);
            mPositiveColor = DialogUtils.resolveColor(context, R.attr.mdt_positiveColor, mAccentColor);
            mNegativeColor = DialogUtils.resolveColor(context, R.attr.mdt_negativeColor, mAccentColor);
            mHeaderColor = DialogUtils.resolveColor(context, R.attr.mdt_headerBackgroundColor, mAccentColor);
            mSelectorColor = DialogUtils.resolveColor(context, R.attr.mdt_selectorColor, mAccentColor);
            if (DialogUtils.canResolve(context, R.attr.mdt_circleBackgroundColor)) {
                mCircleColor = DialogUtils.resolveColor(context, R.attr.mdt_circleBackgroundColor, Color.GRAY);
                mCustomCircleColor = true;
            }
            mCalendar = Calendar.getInstance();
        }

        public Builder darkMode(boolean darkMode) {
            mDarkMode = darkMode;
            return this;
        }

        public Builder mode24Hour(boolean mode24Hour) {
            m24HourMode = mode24Hour;
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

        public Builder headerBackgroundColor(int color) {
            mHeaderColor = color;
            mHeaderSet = true;
            return this;
        }

        public Builder circleBackgroundColor(int color) {
            mCircleColor = color;
            mCustomCircleColor = true;
            return this;
        }

        public Builder selectorColor(int color) {
            mSelectorColor = color;
            return this;
        }

        public Builder textColor(int color) {
            mDefaultTextColor = color;
            mCustomTextColor = true;
            return this;
        }

        public Builder selectedTextColor(int color) {
            mSelectedTextColor = color;
            mCustomSelectedTextColor = true;
            return this;
        }

        public Builder selectedTime(Calendar calendar) {
            return selectedTime(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
            );
        }

        public Builder selectedTime(int hour, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);
            return this;
        }

        public Builder selectedTime(Date date) {
            mCalendar.setTime(date);
            return this;
        }

        public TimePickerDialog build() {
            if (mPositiveText == null) {
                mPositiveText = mContext.getString(android.R.string.ok);
            }
            if (mNegativeText == null) {
                mNegativeText = mContext.getString(android.R.string.cancel);
            }
            if (!mHeaderSet && mDarkMode) {
                mHeaderColor = Color.parseColor("#555555");
            }
            if (!mCustomCircleColor) {
                mCircleColor = mDarkMode ? Color.parseColor("#555555") : Color.parseColor("#eeeeee");
            }
            if (!mCustomTextColor) {
                mDefaultTextColor = mDarkMode ? Color.WHITE : Color.BLACK;
            }
            if (!mCustomSelectedTextColor) {
                mSelectedTextColor = mDarkMode ? Color.BLACK : Color.WHITE;
            }
            TimePickerDialog dialog = TimePickerDialog.newInstance();
            dialog.mCalendar = mCalendar;
            dialog.m24HourMode = m24HourMode;
            dialog.mDarkTheme = mDarkMode;
            dialog.mPositiveColor = mPositiveColor;
            dialog.mNegativeColor = mNegativeColor;
            dialog.mHeaderColor = mHeaderColor;
            dialog.mPositiveText = mPositiveText;
            dialog.mNegativeText = mNegativeText;
            dialog.mCircleColor = mCircleColor;
            dialog.mSelectorColor = mSelectorColor;
            dialog.mDefaultTextColor = mDefaultTextColor;
            dialog.mSelectedTextColor = mSelectedTextColor;
            dialog.mBuilderFlag = true;
            return dialog;
        }
    }
}