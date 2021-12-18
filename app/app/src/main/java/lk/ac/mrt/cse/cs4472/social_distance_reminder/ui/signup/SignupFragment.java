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

import lk.ac.mrt.cse.cs4472.social_distance_reminder.LauncherActivity;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActivity;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.NavigationHost;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.SignupActivity;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.VerifyPhoneActivity;

public class SignupFragment extends Fragment {

    String phoneNumber;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sdc_signup_fragment, container, false);

        final TextInputEditText phoneNumberEditText = view.findViewById(R.id.phone_number);
        MaterialButton signupButton = view.findViewById(R.id.signup_button);

        // Set an error if the phonenumber is less than 10 characters.
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPhoneNumberValid(phoneNumberEditText.getText())) {
                    phoneNumberEditText.setError(getString(R.string.error_phone_number));
                } else {
                    phoneNumberEditText.setError(null); // Clear the error
                    phoneNumber = phoneNumberEditText.getText().toString().trim();

//                    Bundle bundle = new Bundle();
//                    bundle.putString("phoneNumber", phoneNumber);
//                    VerifyPhoneFragment verifyPhoneFragment = new VerifyPhoneFragment();
//                    verifyPhoneFragment.setArguments(bundle);
//
//                    ((NavigationHost) getActivity()).navigateTo(verifyPhoneFragment, false); // Navigate to the next Fragment

                    Intent intent = new Intent((Context) getActivity(), VerifyPhoneActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                }
            }
        });
        return view;

    }

    private boolean isPhoneNumberValid(@Nullable Editable text) {
        return text != null && text.length() >= 10;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("phone", "any number");
        super.onSaveInstanceState(outState);
    }
}
