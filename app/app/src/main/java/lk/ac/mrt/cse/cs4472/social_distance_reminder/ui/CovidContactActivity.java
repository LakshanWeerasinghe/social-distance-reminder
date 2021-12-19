package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;

public class CovidContactActivity extends AppCompatActivity {

    Button btn_date_picker;
    TextView text_selected_date;

    final String tag = "DATE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_covid_contact_activity);

        btn_date_picker = findViewById(R.id.date_pick_button);
        text_selected_date = findViewById(R.id.selected_date_text);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Choose a Date");

        MaterialDatePicker materialDatePicker = builder.build();

        btn_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), tag);
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        text_selected_date.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });

    }
}