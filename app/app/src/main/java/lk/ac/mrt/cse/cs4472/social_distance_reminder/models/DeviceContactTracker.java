package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import android.util.Pair;

import java.io.Serializable;
import java.util.HashSet;
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

    @Override
    public String toString() {
        return "UserContactTracker{" +
                "contactedUserUUID='" + contactedDeviceUUID + '\'' +
                ", contactedDatesAndRiskLevel=" + contactedDatesAndRiskLevel +
                '}';
    }
}
