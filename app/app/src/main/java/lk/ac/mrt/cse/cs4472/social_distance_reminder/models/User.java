package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import java.io.Serializable;

public class User implements Serializable {

    private Integer id;
    private String userUUID;
    private String fcmToken;
    private String mobileNumber;
    private Boolean verifiedUser;
    private Boolean userEnabled;
    private String deviceUUID;

    public User(){

    }

    public User(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getVerifiedUser() {
        return verifiedUser;
    }

    public void setVerifiedUser(Boolean verifiedUser) {
        this.verifiedUser = verifiedUser;
    }

    public Boolean getUserEnabled() {
        return userEnabled;
    }

    public void setUserEnabled(Boolean userEnabled) {
        this.userEnabled = userEnabled;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userUUID='" + userUUID + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", verifiedUser=" + verifiedUser +
                ", userEnabled=" + userEnabled +
                ", deviceUUID='" + deviceUUID + '\'' +
                '}';
    }
}
