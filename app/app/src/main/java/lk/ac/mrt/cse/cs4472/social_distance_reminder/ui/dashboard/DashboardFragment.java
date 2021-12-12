package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.dashboard;

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

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActionInterface;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private SwitchMaterial mEnableBeaconServiceSwitch;

    private TextView mLowRiskDetailsTV;
    private TextView mMildRiskDetailsTV;
    private Text mHighRiskDetailsTV;

    private MaterialButton mExposeToCovidBtn;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "begin onCreateView method execution");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sdc_dashboard_fragment, container, false);

        mEnableBeaconServiceSwitch = view.findViewById(R.id.enable_beacon_service_switch);
        // TODO : set the enabled disabled setting and changed the background color accordingly

        // TODO: complete the below method and initialize risk level texts
        updateRiskLevelDetails();

        mExposeToCovidBtn = view.findViewById(R.id.expose_to_covid);

        mExposeToCovidBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: go to the Covid Expose Fragment @Nilmani
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

    private void updateRiskLevelDetails(){
        // TODO : extract the information from the database and show update the cards
    }

}
