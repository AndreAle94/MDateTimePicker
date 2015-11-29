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
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;

import it.andreale.mdatepicker.DialogUtils;
import it.andreale.mdatepicker.R;

/**
 * Created by AndreAle94
 */
public class DayPickerView extends LinearLayout implements MonthAdapter.AdapterController, View.OnClickListener, MonthPageAdapter.Controller {

    private DatePickerController mController;

    private MonthAdapter mAdapter;
    private ImageButton mButtonBack;
    private ImageButton mButtonForward;
    private MonthPageAdapter mPageAdapter;
    private ViewPager mViewPager;

    public DayPickerView(Context context) {
        super(context);
        initialize();
    }

    public DayPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.md_date_picker_month_layout, this);
        mButtonBack = (ImageButton) findViewById(R.id.arrow_back);
        mButtonForward = (ImageButton) findViewById(R.id.arrow_forward);
        mPageAdapter = (MonthPageAdapter) findViewById(R.id.month_page_adapter);
        mViewPager = (ViewPager) findViewById(R.id.month_view_pager);
        // setup
        mButtonBack.setOnClickListener(this);
        mButtonForward.setOnClickListener(this);
    }

    public void registerController(DatePickerController controller) {
        mController = controller;
        onDateChanged();
    }

    public void onDateChanged() {
        if (mController != null) {
            createAdapter();
            moveToSelection();
        }
    }

    private void moveToSelection() {
        // calculate selection position
        int startYear = mController.getStartYear();
        int year = mController.getSelectedDate().get(Calendar.YEAR);
        int position = (year - startYear) * MonthAdapter.MONTH_IN_YEAR;
        position += mController.getSelectedDate().get(Calendar.MONTH);
        // move view pager
        mViewPager.setCurrentItem(position);
        refreshDirectionalButtons();
    }

    private void createAdapter() {
        mAdapter = new MonthAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mPageAdapter.setViewPager(this, mViewPager);
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
    public int getFirstDayOfWeek() {
        return mController.getFirstDayOfWeek();
    }

    @Override
    public int getSelectionColor() {
        return mController.getSelectionColor();
    }

    @Override
    public void onPageChanged() {
        refreshDirectionalButtons();
    }

    @Override
    public int getMonthHeaderTextColor() {
        return mController.getMonthHeaderTextColor();
    }

    @Override
    public int getTextColor() {
        return mController.isDarkMode() ? Color.WHITE : Color.BLACK;
    }

    @Override
    public int getTodayColor() {
        return mController.getTodayColor();
    }

    @Override
    public Date getSelectedDate() {
        return mController.getSelectedDate().getTime();
    }

    @Override
    public void onDayClicked(int day, int month, int year) {
        mController.onDayClicked(day, month, year);
    }

    @Override
    public void onClick(View v) {
        int position = mViewPager.getCurrentItem();
        if (v == mButtonBack) {
            if (canScrollBack()) {
                position -= 1;
            }
        } else if (v == mButtonForward) {
            if (canScrollForward()) {
                position += 1;
            }
        }
        mViewPager.setCurrentItem(position, true);
    }

    private void refreshDirectionalButtons() {
        int color = mController.getDirectionalButtonColor();
        int disabledColor = DialogUtils.getTransparentColor(color);
        mButtonBack.setColorFilter(canScrollBack() ? color : disabledColor);
        mButtonForward.setColorFilter(canScrollForward() ? color : disabledColor);
    }

    private boolean canScrollBack() {
        return mViewPager.getCurrentItem() > 0;
    }

    private boolean canScrollForward() {
        return (mViewPager.getCurrentItem() + 1) < mAdapter.getCount();
    }
}