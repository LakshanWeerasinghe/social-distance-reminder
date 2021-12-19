package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.w3c.dom.Text;

import java.util.Map;
import java.util.Objects;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ApplicationConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.CovidContactActivity;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActionInterface;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private SQLiteRepository sqLiteRepository;

    private SwitchMaterial mEnableBeaconServiceSwitch;

    private TextView highRiskVal;
    private TextView mildRiskVal;
    private TextView lowRiskVal;

    private MaterialButton mExposeToCovidBtn;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "begin onCreateView method execution");

        sqLiteRepository = DBHelper.getInstance(getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sdc_dashboard_fragment, container, false);

        mEnableBeaconServiceSwitch = view.findViewById(R.id.enable_beacon_service_switch);
        // TODO : set the enabled disabled setting and changed the background color accordingly

        highRiskVal = view.findViewById(R.id.highRiskVal);
        mildRiskVal = view.findViewById(R.id.mildRiskVal);
        lowRiskVal = view.findViewById(R.id.lowRiskVal);
        updateRiskLevelDetails();

        mExposeToCovidBtn = view.findViewById(R.id.expose_to_covid);

        mExposeToCovidBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent((Context) getActivity(), CovidContactActivity.class);
                startActivity(intent);
            }
        });

        mEnableBeaconServiceSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean enable) {
                Log.i(TAG, "user enabled beacon monitoring service " + enable);

                // TODO : change the color of the background if user disable the option

                ((HomeActionInterface) requireActivity()).changeBeaconServiceState(enable);
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void updateRiskLevelDetails(){
        Map<Integer, Integer> riskLevelDetails =
                sqLiteRepository.getNumberOfContactsForEachRiskLevel();

        lowRiskVal.setText(Objects.requireNonNull(
                riskLevelDetails.get(ApplicationConstants.LOW_RISK)).toString());
        mildRiskVal.setText(Objects.requireNonNull(
                riskLevelDetails.get(ApplicationConstants.MILD_RISK)).toString());
        highRiskVal.setText(Objects.requireNonNull(
                riskLevelDetails.get(ApplicationConstants.HIGH_RISK)).toString());
    }

}
