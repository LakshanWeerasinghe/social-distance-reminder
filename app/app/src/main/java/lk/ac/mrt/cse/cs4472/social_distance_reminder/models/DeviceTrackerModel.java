package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeviceTrackerModel implements Serializable {

    private String deviceUUID;
    private List<String> dates;
    private Integer riskLevel;

   public DeviceTrackerModel(){
       this.dates = new ArrayList<>();
       this.riskLevel = 5;
   }

   public DeviceTrackerModel(String deviceUUID){
       this.deviceUUID = deviceUUID;
       this.dates = new ArrayList<>();
       this.riskLevel = 5;
   }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public List<String> getDates() {
        return dates;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = Math.min(this.riskLevel, riskLevel);
    }

    public void addDate(String date){
       if(!this.dates.contains(date)){
          this.dates.add(date);
       }
    }

    @Override
    public String toString() {
        return "DeviceTrackerModel{" +
                "deviceUUID='" + deviceUUID + '\'' +
                ", dates=" + dates +
                ", riskLevel=" + riskLevel +
                '}';
    }
}
