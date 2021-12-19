package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String userId;
    private String fcmToken;
    private String mobileNumber;
    private boolean verifiedUser;
    private boolean userEnabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean isVerifiedUser() {
        return verifiedUser;
    }

    public void setVerifiedUser(boolean verifiedUser) {
        this.verifiedUser = verifiedUser;
    }

    public boolean isUserEnabled() {
        return userEnabled;
    }

    public void setUserEnabled(boolean userEnabled) {
        this.userEnabled = userEnabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", verifiedUser=" + verifiedUser +
                ", userEnabled=" + userEnabled +
                '}';
    }
}
