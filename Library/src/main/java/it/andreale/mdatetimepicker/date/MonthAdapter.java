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

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AndreAle94
 */
public class MonthAdapter extends PagerAdapter implements MonthView.MonthController {

    public final static int MONTH_IN_YEAR = 12;

    private final String[] months;
    private final AdapterController mController;

    public MonthAdapter(AdapterController controller) {
        mController = controller;
        months = new DateFormatSymbols(Locale.getDefault()).getMonths();
    }

    @Override
    public int getCount() {
        int startYear = mController.getStartYear();
        int endYear = mController.getEndYear() + 1;
        return (endYear - startYear) * MONTH_IN_YEAR;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        MonthView monthView = new MonthView(collection.getContext());
        monthView.registerController(position, this);
        collection.addView(monthView);
        return monthView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        MonthView monthView = (MonthView) view;
        monthView.unregisterController();
        collection.removeView(monthView);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        return months[getMonth(position)] + " " + String.valueOf(getYear(position));
    }

    @Override
    public int getFirstDayOfWeek() {
        return mController.getFirstDayOfWeek();
    }

    @Override
    public int getYear(int position) {
        return mController.getStartYear() + position / MONTH_IN_YEAR;
    }

    @Override
    public int getMonth(int position) {
        return position % MONTH_IN_YEAR;
    }

    @Override
    public Date getSelectedDate() {
        return mController.getSelectedDate();
    }

    @Override
    public int getWeekDayColor() {
        int textColor = mController.getTextColor();
        return Color.argb(127, Color.red(textColor), Color.green(textColor), Color.blue(textColor));
    }

    @Override
    public int getTodayTextColor() {
        return mController.getTodayColor();
    }

    @Override
    public int getSelectionColor() {
        return mController.getSelectionColor();
    }

    @Override
    public int getSelectedTextColor() {
        return Color.WHITE;
    }

    @Override
    public int getStandardTextColor() {
        return mController.getTextColor();
    }

    @Override
    public void onDayClicked(int day, int month, int year) {
        mController.onDayClicked(day, month, year);
    }

    public interface AdapterController {

        int getStartYear();

        int getEndYear();

        int getFirstDayOfWeek();

        int getSelectionColor();

        int getTextColor();

        int getTodayColor();

        Date getSelectedDate();

        void onDayClicked(int day, int month, int year);
    }
}