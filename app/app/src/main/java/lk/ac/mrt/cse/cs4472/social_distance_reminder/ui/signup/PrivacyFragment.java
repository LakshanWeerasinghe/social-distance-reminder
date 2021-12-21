package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.NavigationHost;

public class PrivacyFragment extends Fragment {

    MaterialButton backButton;
    MaterialButton nextButton;
    MaterialButton skipButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sdr_privacy_fragment, container, false);

        backButton = view.findViewById(R.id.privacy_back_button);
        nextButton = view.findViewById(R.id.privacy_next_button);
        skipButton = view.findViewById(R.id.privacy_skip_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new SignupFragment(), true);
            }
        });

        return view;

    }
}
