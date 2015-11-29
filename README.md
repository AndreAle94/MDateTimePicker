# MDateTimePicker

This library is a simple implementation of the new DatePicker and TimePicker as shown in [the Material Design spec](http://www.google.com/design/spec/components/pickers.html).

## Screenshots
- DatePicker

Portrait | Landscape
---- | ----
![Portrait](https://github.com/AndreAle94/MDatePicker/blob/master/Images/dp_portrait.png) | ![Landscape](https://github.com/AndreAle94/MDatePicker/blob/master/Images/dp_landscape.png)

- TimePicker

Portrait | Landscape
---- | ----
![Portrait](https://github.com/AndreAle94/MDatePicker/blob/master/Images/tp_portrait.png) | ![Landscape](https://github.com/AndreAle94/MDatePicker/blob/master/Images/tp_landscape.png)

## How it works?
1. [Simple implementation](#simple-implementation)
2. [Builder options](#builder-options)
3. [Advanced options](#advanced-options)
4. [License](#license)

## Simple implementation
- You have not to worry about screen orientation changes becouse it will automatically look for your activity whenever the dialog is attached.
```java
public class MainActivity extends AppCompatActivity implements OnDateSetListener, OnTimeSetListener {

    private final static String DATE_PICKER_TAG = "datePickerTag";
    private final static String TIME_PICKER_TAG = "timePickerTag";

    private DatePickerDialog datePickerDialog = null;
    private TimePickerDialog timePickerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve datepicker dialog instance from fragment manager
        datePickerDialog = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATE_PICKER_TAG);
        
        // retrieve timepicker dialog instance from fragment manager
        timePickerDialog = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIME_PICKER_TAG);

        // if date picker is null, initialize it
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog.Builder(this)
                    .selectedDate(23, 3, 2015)
                    .darkMode(false)
                    .yearRange(1980, 2050)
                    .build();
        }

        // if time picker is null, initialize it
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog.Builder(this)
                    .darkMode(false)
                    .mode24Hour(false)
                    .build();
        }

        findViewById(R.id.date_picker_show).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show date picker
                datePickerDialog.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });

        findViewById(R.id.time_picker_show).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show time picker
                timePickerDialog.show(getSupportFragmentManager(), TIME_PICKER_TAG);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(this, "Date: " + dayOfMonth + "/" + monthOfYear + "/" + year, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeSet(TimePickerDialog dialog, int hourOfDay, int minute) {
        Toast.makeText(this, "Time: " + hourOfDay + " : " + minute, Toast.LENGTH_LONG).show();
    }
}
```
- If you want to provide your own listener outside activity you have to manage screen orientation changes
```java
public class MainActivity extends AppCompatActivity {

    private final static String DATE_PICKER_TAG = "datePickerTag";
    private final static String TIME_PICKER_TAG = "timePickerTag";

    private DatePickerDialog datePickerDialog = null;
    private TimePickerDialog timePickerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve datepicker dialog instance from fragment manager
        datePickerDialog = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATE_PICKER_TAG);
        
        // retrieve timepicker dialog instance from fragment manager
        timePickerDialog = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIME_PICKER_TAG);

        // if date picker is null, initialize it
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog.Builder(this)
                    .selectedDate(23, 3, 2015)
                    .darkMode(false)
                    .yearRange(1980, 2050)
                    .build();
        }

        // if time picker is null, initialize it
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog.Builder(this)
                    .darkMode(false)
                    .mode24Hour(false)
                    .build();
        }
        
        // create listener
        datePickerDialog.setOnDateSetListener(new OnDateSetListener() {
            
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(this, "Date: " + dayOfMonth + "/" + monthOfYear + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
        
        // create listener
        timePickerDialog.setOnTimeSetListener(new OnTimeSetListener() {
        
            @Override
            public void onTimeSet(TimePickerDialog dialog, int hourOfDay, int minute) {
                Toast.makeText(this, "Time: " + hourOfDay + " : " + minute, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.date_picker_show).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show date picker
                datePickerDialog.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });

        findViewById(R.id.time_picker_show).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show time picker
                timePickerDialog.show(getSupportFragmentManager(), TIME_PICKER_TAG);
            }
        });
    }
}
```
## Builder options
- DatePicker
```java 
new DatePickerDialog.Builder(this)
    .firstDayOfWeek(Calendar.SUNDAY)          // DEFAULT: Calendar.SUNDAY
    .darkMode(false)                          // DEFAULT: false
    .headerBackgroundColor(Color.RED)         // DEFAULT: colorAccent of your app theme
    .positiveColor(Color.BLUE)                // DEFAULT: colorAccent of your app theme
    .positiveText("Positive")                 // DEFAULT: android.R.string.ok
    .negativeColor(Color.RED)                 // DEFAULT: colorAccent of your app theme
    .negativeText("Negative")                 // DEFAULT: android.R.string.cancel
    .selectedCircleColor(Color.MAGENTA)       // DEFAULT: colorAccent of your app theme
    .todayTextColor(Color.MAGENTA)            // DEFAULT: colorAccent of your app theme
    .selectedDate(23, Calendar.MARCH, 2015)   // DEFAULT: current device date (months in java are 0 - 11)
    .yearRange(1980, 2050)                    // DEFAULT: from 1900 to 2100
    .build();                                 // create dialog instance
    // if you call .show(getSupportFragmentManager(), DATE_PICKER_TAG) it will auto build it.
```
- TimePicker
```java 
new TimePickerDialog.Builder(this)
    .darkMode(false)                          // DEFAULT: false
    .headerBackgroundColor(Color.RED)         // DEFAULT: colorAccent of your app theme
    .positiveColor(Color.BLUE)                // DEFAULT: colorAccent of your app theme
    .positiveText("Positive")                 // DEFAULT: android.R.string.ok
    .negativeColor(Color.RED)                 // DEFAULT: colorAccent of your app theme
    .negativeText("Negative")                 // DEFAULT: android.R.string.cancel
    .circleBackgroundColor(Color.MAGENTA)     // DEFAULT: appropriate color for picker theme
    .selectedTextColor(Color.MAGENTA)         // DEFAULT: appropriate color for picker theme
    .textColor(Color.MAGENTA)                 // DEFAULT: appropriate color for picker theme
    .selectorColor(Color.MAGENTA)             // DEFAULT: colorAccent of your app theme
    ..selectedTime(14, 47)                    // DEFAULT: current device time
    .build();                                 // create dialog instance
    // if you call .show(getSupportFragmentManager(), TIME_PICKER_TAG) it will auto build it.
```
## Advanced options
You can specify some of the builder params directly in your styles.xml file. (You have not to set them every time).
```xml
<resources>
    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <!-- Customize date picker here. -->
        <item name="mdp_darkMode">false</item>
        <item name="mdp_positiveColor">@color/colorAccent</item>
        <item name="mdp_negativeColor">@color/colorAccent</item>
        <item name="mdp_headerBackgroundColor">@color/colorAccent</item>
        <item name="mdp_todayTextColor">@color/colorAccent</item>
        <item name="mdp_selectedCircleColor">@color/colorAccent</item>
        <!-- Customize time picker here. -->
        <item name="mtp_darkMode">false</item>
        <item name="mtp_24HourMode">false</item>
        <item name="mtp_positiveColor">@color/colorAccent</item>
        <item name="mtp_negativeColor">@color/colorAccent</item>
        <item name="mtp_headerBackgroundColor">@color/colorAccent</item>
        <item name="mtp_circleBackgroundColor">@color/gray</item>
        <item name="mtp_selectorColor">@color/colorAccent</item>
    </style>
</resources>
```
- If you set a param in your builder it will override the param specified in app's theme!
For example: if you set in your style.xml the theme mode:
```xml
<item name="mdp_darkMode">false</item>
```
and than in your builder you set:
```java 
new DatePickerDialog.Builder(this)
    .darkMode(true)
    .build();
```
it will create a dark picker.

- If you don't specify any color, it will automatically wrap accentColor from your app theme.

## License
    The MIT License (MIT)
    
    Copyright (c) 2015 AndreAle94
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
