package it.andreale.mdatepicker.date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;

import it.andreale.mdatepicker.R;

/**
 * Created by Andrea on 23/11/2015.
 */
public class YearPickerView extends ListView implements YearAdapter.Controller, AdapterView.OnItemClickListener {

    private YearAdapter mAdapter;
    private DatePickerController mController;

    private int mPickerHeight;

    public YearPickerView(Context context) {
        super(context, null, 0);
        initialize();
    }

    public YearPickerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initialize();
    }

    public YearPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setSelector(new StateListDrawable());
        setDividerHeight(0);
        setItemsCanFocus(true);
        mPickerHeight = getResources().getDimensionPixelOffset(R.dimen.date_picker_body_height);
    }

    public void resisterController(DatePickerController controller) {
        mController = controller;
        // initialize adapter
        mAdapter = new YearAdapter(this, getResources());
        setOnItemClickListener(this);
        setAdapter(mAdapter);
    }

    public void notifyDateChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onPreShow() {
        notifyDateChanged();
        // move list to center current year
        Resources res = getResources();
        int position = getSelectedYear() - getStartYear();
        int padding = res.getDimensionPixelSize(R.dimen.year_picker_padding);
        int textSelected = res.getDimensionPixelSize(R.dimen.year_text_size_selected);
        int selectedViewHeight = 2 * padding + textSelected;
        int offset = (mPickerHeight - selectedViewHeight) / 2;
        setSelectionFromTop(position, offset);
    }

    @Override
    public int getStartYear() {
        return mController.getStartYear();
    }

    @Override
    public int getEndYear() {
        return mController.getEndYear();
    }

    @Override
    public int getSelectedYear() {
        return mController.getSelectedDate().get(Calendar.YEAR);
    }

    @Override
    public int getPressedColor() {
        return mController.getSelectionColor();
    }

    @Override
    public int getSelectedColor() {
        return mController.getSelectionColor();
    }

    @Override
    public int getTextColor() {
        boolean isDarkMode = mController.isDarkMode();
        return isDarkMode ? Color.WHITE : Color.BLACK;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // calculate selected year
        int startYear = mController.getStartYear();
        int selectedYear = startYear + position;
        // notify controller
        mController.onYearChanged(selectedYear);
    }
}