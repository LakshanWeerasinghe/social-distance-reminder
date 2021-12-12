package lk.ac.mrt.cse.cs4472.social_distance_reminder.util;

import java.util.Random;
import java.util.UUID;

public class Util {

    private static final Random notificationIdGenerator = new Random();
    private static final Random foregroundServiceIdGenerator = new Random();

    public static int getNotificationId(){
        return notificationIdGenerator.nextInt();
    }

    public static int getForegroundServiceId(){
        return foregroundServiceIdGenerator.nextInt();
    }

    public static UUID generateRandomUUID(){
        return UUID.randomUUID();
    }
}
