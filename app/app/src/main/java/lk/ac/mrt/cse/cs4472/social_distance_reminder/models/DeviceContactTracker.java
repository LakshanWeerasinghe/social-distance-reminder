package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceContactTracker implements Serializable {

    private String contactedDeviceUUID;
    private final Set<Pair<String, Integer>> contactedDatesAndRiskLevel;

    public DeviceContactTracker(String contactedDeviceUUID){
        this.contactedDeviceUUID = contactedDeviceUUID;
        this.contactedDatesAndRiskLevel = new HashSet<>();
    }

    public String getContactedDeviceUUID() {
        return contactedDeviceUUID;
    }

    public void setContactedDeviceUUID(String contactedDeviceUUID) {
        this.contactedDeviceUUID = contactedDeviceUUID;
    }

    public Set<Pair<String, Integer>> getContactedDatesAndRiskLevel() {
        return contactedDatesAndRiskLevel;
    }

    public void addContactedDateAndRiskLevel(Pair<String, Integer> value){
        contactedDatesAndRiskLevel.add(value);
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceUUID", contactedDeviceUUID);
            List<JSONObject> contactedDates = new ArrayList<>();
            for(Pair<String, Integer> pair: contactedDatesAndRiskLevel){
                JSONObject dateRiskObj = new JSONObject();
                dateRiskObj.put("date", pair.first);
                dateRiskObj.put("riskLevel", pair.second);
                contactedDates.add(dateRiskObj);
            }
            jsonObject.put("contactedDates", contactedDates);
            return jsonObject;
        }catch (JSONException exception){
            return null;
        }
    }

    @Override
    public String toString() {
        return "UserContactTracker{" +
                "contactedUserUUID='" + contactedDeviceUUID + '\'' +
                ", contactedDatesAndRiskLevel=" + contactedDatesAndRiskLevel +
                '}';
    }
}
