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

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import it.andreale.mdatetimepicker.R;

/**
 * Created by AndreAle94
 */
public class YearAdapter extends BaseAdapter {

    private final Controller mController;
    private float mTextNormal;
    private float mTextSelected;

    public YearAdapter(Controller controller, Resources resources) {
        mController = controller;
        mTextNormal = resources.getDimension(R.dimen.year_text_size_normal);
        mTextSelected = resources.getDimension(R.dimen.year_text_size_selected);
    }

    @Override
    public int getCount() {
        int startYear = mController.getStartYear();
        int endYear = mController.getEndYear() + 1;
        return endYear - startYear;
    }

    @Override
    public Object getItem(int position) {
        int startYear = mController.getStartYear();
        int currentYear = startYear + position;
        return String.valueOf(currentYear);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isSelected(int position) {
        int selectedYear = mController.getSelectedYear();
        int startYear = mController.getStartYear();
        return (selectedYear - startYear) == position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // recycle view
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.md_date_picker_year_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) view;
            viewHolder.mTextView.setTextColor(createTextSelector());
            // set holder as tag to be retrieved later
            view.setTag(viewHolder);
        } else {
            // retrieve tag from view
            viewHolder = (ViewHolder) view.getTag();
        }
        // customize the view
        viewHolder.setText((String) getItem(position));
        viewHolder.setSelected(isSelected(position));
        // return created view
        return view;
    }

    private ColorStateList createTextSelector() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_selected},
                new int[]{}
        };
        int[] colors = new int[]{
                mController.getPressedColor(),
                mController.getSelectedColor(),
                mController.getTextColor()
        };
        return new ColorStateList(states, colors);
    }

    private class ViewHolder {
        // hold text view
        private TextView mTextView;

        private void setText(String text) {
            if (mTextView != null) {
                mTextView.setText(text);
            }
        }

        private void setSelected(boolean selected) {
            if (mTextView != null) {
                // mTextView.setSelected(selected);
                mTextView.setTextColor(selected ? mController.getSelectedColor() : mController.getTextColor());
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? mTextSelected : mTextNormal);
            }
        }
    }

    public interface Controller {

        int getStartYear();

        int getEndYear();

        int getSelectedYear();

        int getPressedColor();

        int getSelectedColor();

        int getTextColor();
    }
}