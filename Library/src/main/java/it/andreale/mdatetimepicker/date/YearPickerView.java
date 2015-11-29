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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;

import it.andreale.mdatetimepicker.R;

/**
 * Created by AndreAle94
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