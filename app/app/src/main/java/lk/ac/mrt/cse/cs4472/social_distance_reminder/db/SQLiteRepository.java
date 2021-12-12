package lk.ac.mrt.cse.cs4472.social_distance_reminder.db;

public interface SQLiteRepository {

    void saveReceivedBeaconSignals();

    void getUserDetails();

    void getNumberOfContactsForEachRiskLevel();

    void saveUserDetails();

    void saveCovidContactedNotificationDetails();

    void getPastContactsDetails();
}
