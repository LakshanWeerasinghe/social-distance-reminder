package lk.ac.mrt.cse.cs4472.social_distance_reminder.db;

import java.util.Map;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.UserConfig;

public interface SQLiteRepository {

    void saveSocialDistancingViolation(String contactedUserId, Integer riskLevel);

    User getUserDetails();

    Map<Integer, Integer> getNumberOfContactsForEachRiskLevel();

    void saveUserDetails();

    void saveCovidContactedNotificationDetails();

    void getPastContactsDetails();

    void updateBeaconEnableConfig(String userId, boolean value);

    UserConfig getUserConfigs();

    void updateUserConfig(Integer id, Boolean enableBeaconService, Boolean enableVibrating);

    void initializeTables();

    void updateUserDetails(Integer id, String mobileNumber, Boolean isUserVerified,
                           Boolean isUserEnabled);
}
