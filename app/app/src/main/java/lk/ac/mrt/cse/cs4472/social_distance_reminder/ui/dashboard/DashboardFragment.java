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

import java.util.Map;
import java.util.Objects;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ApplicationConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ArgumentConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.service.LocalizationService;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.CovidContactActivity;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.UserConfig;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActionInterface;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomePermissionHandlerInterface;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private SQLiteRepository sqLiteRepository;

    private SwitchMaterial mEnableBeaconServiceSwitch;

    private TextView highRiskVal;
    private TextView mildRiskVal;
    private TextView lowRiskVal;

    private MaterialButton mExposeToCovidBtn;
    private MaterialButton mToggleLang;
    Integer userConfigId;

    private String lang = "en";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        Bundle arguments = getArguments();
        Boolean beaconServiceEnabled = true;
        if(arguments != null){
            userConfigId = arguments.getInt(ArgumentConstants.BUNDLE_ARG_USER_CONFIG_ID);
            beaconServiceEnabled = arguments.getBoolean(
                    ArgumentConstants.BUNDLE_ARG_ENABLE_BEACON_SERVICE);
        }

        mEnableBeaconServiceSwitch.setChecked(beaconServiceEnabled);

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

        mToggleLang = view.findViewById(R.id.language_toggle);

        mToggleLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lang.equals("en")){
                    lang = "si";
                }else {
                    lang = "en";
                }
                Log.i(TAG, lang);
                LocalizationService.setLocale(getActivity(), lang);
            }
        });

        mEnableBeaconServiceSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean enable) {
                        Log.i(TAG, "user enabled beacon monitoring service " + enable);

                        // TODO : change the color of the background if user disable the option

                        if(enable){
                            boolean hasPermissions = ((HomePermissionHandlerInterface)
                                    requireActivity()).checkPermissionsAndRequest();
                            if(hasPermissions){
                                ((HomeActionInterface) requireActivity()).changeBeaconServiceState(
                                        userConfigId, true);
                            }
                            else {
                                mEnableBeaconServiceSwitch.setChecked(false);
                            }
                        }
                        if(!enable){
                            ((HomeActionInterface) requireActivity()).changeBeaconServiceState(
                                    userConfigId, false);
                        }

                    }
                });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("SetTextI18n")
    private void updateRiskLevelDetails() {
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
