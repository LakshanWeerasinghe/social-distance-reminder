package lk.ac.mrt.cse.cs4472.social_distance_reminder.db;

import android.util.Pair;

import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;

public interface SQLiteRepository {

    void saveSocialDistancingViolation(String contactedUserId, Integer riskLevel);

    User getUserDetails();

    Map<Integer, Integer> getNumberOfContactsForEachRiskLevel();

    void saveUserDetails();

    void saveCovidContactedNotificationDetails();

    void getPastContactsDetails();

    void updateBeaconEnableConfig(String userId, boolean value);

    void initializeTables();
}
