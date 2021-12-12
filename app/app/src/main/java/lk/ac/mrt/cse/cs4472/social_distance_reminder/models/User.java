package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String userName;
    private String fcmToken;
    private String mobileNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
