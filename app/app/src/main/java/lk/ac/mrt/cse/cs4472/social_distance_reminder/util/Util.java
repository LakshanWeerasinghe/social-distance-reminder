package lk.ac.mrt.cse.cs4472.social_distance_reminder.util;

import android.app.Activity;
import android.widget.Toast;

import java.util.Random;
import java.util.UUID;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ApplicationConstants;

public class Util {

    private static final Random notificationIdGenerator = new Random();
    private static final Random foregroundServiceIdGenerator = new Random();

    public static int getNotificationId() {
        return notificationIdGenerator.nextInt();
    }

    public static int getForegroundServiceId() {
        return foregroundServiceIdGenerator.nextInt();
    }

    public static UUID generateRandomUUID() {
        return UUID.randomUUID();
    }

    public static int getRiskLevel(double distance) {
        if (1 >= distance) {
            return ApplicationConstants.HIGH_RISK;
        }
        if (3 >= distance) {
            return ApplicationConstants.MILD_RISK;
        }
        if (5 >= distance) {
            return ApplicationConstants.LOW_RISK;
        }
        return 0;
    }

    public static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
}
