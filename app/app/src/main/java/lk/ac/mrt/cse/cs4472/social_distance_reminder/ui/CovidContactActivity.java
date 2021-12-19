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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;

public class CovidContactActivity extends AppCompatActivity {

    Button btn_date_picker;
    TextView text_selected_date;
    TextInputEditText descriptionEditText;
    Button btn_submit;

    final String tag = "DATE_TAG";
    String selectedDate;
    String description;

    FirebaseFirestore database;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_covid_contact_activity);

        btn_date_picker = findViewById(R.id.date_pick_button);
        text_selected_date = findViewById(R.id.selected_date_text);
        descriptionEditText = findViewById(R.id.covid_description_text_input);
        btn_submit = findViewById(R.id.covid_positive_button);

        database = FirebaseFirestore.getInstance();

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
                        selectedDate = materialDatePicker.getSelection().toString();
                    }
                });
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description = descriptionEditText.getText().toString();
                // TODO: get the list of covid contacts from Sqlite
                // [{uid="111", risk_level="High", covid_contact_date="34876876487"},{uid="222", risk_level="Medium", covid_contact_date="3423423424"}]

                Map<String, Object> contact1 = new HashMap<>();
                contact1.put("uid", "111");
                contact1.put("risk_level", "High");
                contact1.put("covid_contact_date", "8979898669");

                Map<String, Object> contact2 = new HashMap<>();
                contact2.put("uid", "222");
                contact2.put("risk_level", "Medium");
                contact2.put("covid_contact_date", "23425435479");

                ArrayList<Map> contacts = new ArrayList<Map>();
                contacts.add(contact1);
                contacts.add(contact2);

                addCovidPositiveFirebase(contacts);
            }
        });

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