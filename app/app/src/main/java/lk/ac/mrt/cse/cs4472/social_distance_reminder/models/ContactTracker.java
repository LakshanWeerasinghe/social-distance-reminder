package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import java.io.Serializable;

public class ContactTracker implements Serializable {

    private Integer id;
    private String contactedUserId;
    private Integer riskLevel;
    private String timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactedUserId() {
        return contactedUserId;
    }

    public void setContactedUserId(String contactedUserId) {
        this.contactedUserId = contactedUserId;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ContactTracker{" +
                "id=" + id +
                ", contactedUserId='" + contactedUserId + '\'' +
                ", riskLevel=" + riskLevel +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
