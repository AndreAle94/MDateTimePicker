package it.andreale.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import it.andreale.mdatepicker.date.DatePickerDialog;
import it.andreale.mdatepicker.date.OnDateSetListener;

public class MainActivity extends AppCompatActivity implements OnDateSetListener {

    private final static String DATE_PICKER_TAG = "datePickerTag";

    DatePickerDialog datePickerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve dialog instance from fragment manager
        datePickerDialog = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATE_PICKER_TAG);

        // if date picker is null, initialize it
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog.Builder(this)
                    .selectedDate(23, Calendar.MARCH, 2015)
                    .darkMode(false)
                    .yearRange(1980, 2050)
                    .build();
        }

        findViewById(R.id.date_picker_show).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show date picker
                datePickerDialog.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(this, "Date: " + dayOfMonth + "/" + monthOfYear + "/" + year, Toast.LENGTH_LONG).show();
    }
}