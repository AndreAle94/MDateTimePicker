package it.andreale.mdatepicker.time;

import java.util.Calendar;

/**
 * Created by Andrea on 29/11/2015.
 */
public interface TimePickerController {

    int getDefaultMode();

    void onPickerModeChange(int pickerMode);

    boolean is24HourMode();

    Calendar getSelectedTime();

    int getCircleBackgroundColor();

    int getDefaultTextColor();

    int getSelectedTextColor();

    int getSelectorColor();

    void onTimeChanged();
}
