package it.andreale.mdatepicker.date;

import java.util.Calendar;

/**
 * Created by Andrea on 25/11/2015.
 */
public interface DatePickerController {

    int getDefaultMode();

    void onPickerModeChange(int pickerMode);

    int getStartYear();

    int getEndYear();

    Calendar getSelectedDate();

    void onYearChanged(int year);

    boolean isDarkMode();

    int getSelectionColor();

    int getTodayColor();

    String getHeaderDateFormat();

    int getFirstDayOfWeek();

    void onDayClicked(int day, int month, int year);

    int getDirectionalButtonColor();

    int getMonthHeaderTextColor();
}
