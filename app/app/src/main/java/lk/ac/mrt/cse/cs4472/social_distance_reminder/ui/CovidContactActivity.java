package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.DeviceContactTracker;

public class CovidContactActivity extends AppCompatActivity {

    private static final String TAG = "CovidContactActivity";

    Button btn_date_picker;
    TextView text_selected_date;
    TextInputEditText descriptionEditText;
    Button btn_submit;

    final String tag = "DATE_TAG";
    String selectedDate;
    String description;

    FirebaseFirestore database;
    FirebaseUser currentUser;
    private SQLiteRepository sqLiteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_covid_contact_activity);

        btn_date_picker = findViewById(R.id.date_pick_button);
        text_selected_date = findViewById(R.id.selected_date_text);
        descriptionEditText = findViewById(R.id.covid_description_text_input);
        btn_submit = findViewById(R.id.covid_positive_button);

        database = FirebaseFirestore.getInstance();
        sqLiteRepository = DBHelper.getInstance(this);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Choose a Date");
        builder.setCalendarConstraints(limitFuture().build());

        MaterialDatePicker materialDatePicker = builder.build();

        btn_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), tag);
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        text_selected_date.setText(materialDatePicker.getHeaderText());
                        selectedDate = Objects.requireNonNull(
                                materialDatePicker.getSelection()).toString();
                    }
                });
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description = descriptionEditText.getText().toString();
                Map<String, DeviceContactTracker> closeContactMap =
                        sqLiteRepository.getCloseContactList(selectedDate);

                List<JSONObject> contactDetails = new ArrayList<>();
                for (DeviceContactTracker deviceContactTracker: closeContactMap.values()){
                    contactDetails.add(deviceContactTracker.toJSONObject());
                }

//                addCovidPositiveFirebase(contacts);
            }
        });

    }

    private CalendarConstraints.Builder limitFuture() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now());
        return constraintsBuilder;
    }
    
    private void addCovidPositiveFirebase(ArrayList<Map> contacts) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserUid = currentUser.getUid();

        Map<String, Object> covidContactDetails = new HashMap<>();
        covidContactDetails.put("user_id", currentUserUid);
        covidContactDetails.put("message", description);
        covidContactDetails.put("covid_positive_date", selectedDate);
        covidContactDetails.put("contacts", contacts);

        database.collection("CovidContact").add(covidContactDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.d("contacts", "successful");
                    Toast.makeText(CovidContactActivity.this, "Successfully Added Covid Positive Details", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("contacts", "unsuccessful");
                    Toast.makeText(CovidContactActivity.this, "Couldn't Add Covid Positive Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent nextActivity = new Intent(CovidContactActivity.this, HomeActivity.class);
        nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(nextActivity);
    }
}