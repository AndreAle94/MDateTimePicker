package it.andreale.mdatepicker.date;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.andreale.mdatepicker.R;

/**
 * Created by Andrea on 23/11/2015.
 */
public class DateHeaderView extends LinearLayout implements View.OnClickListener {

    public static final int MODE_UNINITIALIZED = -1;
    public static final int MODE_DAY_PICKER = 0;
    public static final int MODE_YEAR_PICKER = 1;

    private final static int TRANSPARENT_WHITE = Color.argb(127, 255, 255, 255);

    private int mPickerMode;
    private DatePickerController mController;

    private TextView mYearText;
    private TextView mDayText;

    public DateHeaderView(Context context) {
        super(context);
        initialize();
    }

    public DateHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DateHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mPickerMode = MODE_UNINITIALIZED;
        mController = null;
        // inflate and setup
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.md_date_picker_header_layout, this);
        mYearText = (TextView) findViewById(R.id.header_year);
        mDayText = (TextView) findViewById(R.id.header_day);
        mYearText.setOnClickListener(this);
        mDayText.setOnClickListener(this);
    }

    public void setPickerMode(int pickerMode) {
        notifyController(pickerMode, false);
    }

    public int getPickerMode() {
        return mPickerMode;
    }

    public void registerController(DatePickerController controller) {
        mController = controller;
        if (mController != null) {
            if (mPickerMode == MODE_UNINITIALIZED) {
                notifyController(mController.getDefaultMode(), false);
            } else {
                notifyController(mPickerMode, true);
            }
            updateDate();
        }
    }

    private void notifyController(int pickerMode, boolean forced) {
        if (mPickerMode != pickerMode || forced) {
            mPickerMode = pickerMode;
            if (mController != null) {
                mController.onPickerModeChange(mPickerMode);
                // change text color
                if (pickerMode == MODE_DAY_PICKER) {
                    mDayText.setTextColor(Color.WHITE);
                    mYearText.setTextColor(TRANSPARENT_WHITE);
                } else if (pickerMode == MODE_YEAR_PICKER) {
                    mDayText.setTextColor(TRANSPARENT_WHITE);
                    mYearText.setTextColor(Color.WHITE);
                }
            }
        }
    }

    public void updateDate() {
        Calendar calendar = mController.getSelectedDate();
        mYearText.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        SimpleDateFormat format = new SimpleDateFormat(mController.getHeaderDateFormat(), Locale.getDefault());
        mDayText.setText(format.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v == mYearText) {
            notifyController(MODE_YEAR_PICKER, false);
        } else if (v == mDayText) {
            notifyController(MODE_DAY_PICKER, false);
        }
    }
}