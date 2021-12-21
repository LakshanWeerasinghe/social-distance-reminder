package lk.ac.mrt.cse.cs4472.social_distance_reminder.db;

import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.UserConfig;

public interface SQLiteRepository {

    void saveSocialDistancingViolation(String contactedUserId, Integer riskLevel);

    User getUserDetails();

    Map<Integer, Integer> getNumberOfContactsForEachRiskLevel();

    void saveCovidContactedNotificationDetails();

    List<Map<String, Object>> getCloseContactList(String dateOfPositive);

    void updateBeaconEnableConfig(String userId, boolean value);

    UserConfig getUserConfigs();

    void updateUserConfig(Integer id, Boolean enableBeaconService, Boolean enableVibrating);

    void initializeTables();

    void updateUserDetails(User user);
}
