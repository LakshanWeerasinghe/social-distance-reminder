package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.VerifyPhoneActivity;

public class SignupFragment extends Fragment {

    String phoneNumber;
    TextInputEditText phoneNumberEditText;
    MaterialButton signupButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sdr_signup_fragment, container, false);

        phoneNumberEditText = view.findViewById(R.id.phone_number_text_input);
        signupButton = view.findViewById(R.id.signup_button);

        // Set an error if the phone number is less than 10 characters.
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPhoneNumberValid(phoneNumberEditText.getText())) {
                    phoneNumberEditText.setError(getString(R.string.error_phone_number));
                } else {
                    phoneNumberEditText.setError(null); // Clear the error
                    phoneNumber = phoneNumberEditText.getText().toString().trim();
                    int id = requireActivity().getIntent().getIntExtra("id", -1);

                    Intent intent = new Intent((Context) getActivity(), VerifyPhoneActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        });
        return view;

    }

    private boolean isPhoneNumberValid(@Nullable Editable text) {
        return text != null && text.length() == 12;
    }

}
