package lk.ac.mrt.cse.cs4472.social_distance_reminder.models;

public class NotificationModel {

    private String message;
    private String date;

    public NotificationModel() { }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
