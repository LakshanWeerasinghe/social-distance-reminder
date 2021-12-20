package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import java.io.Serializable;

public class UserConfig implements Serializable {

    private Integer id;
    private Boolean enableBeaconService;
    private Boolean enableVibrating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEnableBeaconService() {
        return enableBeaconService;
    }

    public void setEnableBeaconService(Boolean enableBeaconService) {
        this.enableBeaconService = enableBeaconService;
    }

    public Boolean getEnableVibrating() {
        return enableVibrating;
    }

    public void setEnableVibrating(Boolean enableVibrating) {
        this.enableVibrating = enableVibrating;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "id=" + id +
                ", enableBeaconService=" + enableBeaconService +
                ", enableVibrating=" + enableVibrating +
                '}';
    }
}
