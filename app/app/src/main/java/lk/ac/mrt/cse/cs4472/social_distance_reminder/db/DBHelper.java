package lk.ac.mrt.cse.cs4472.social_distance_reminder.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ApplicationConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.UserConfig;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.DeviceContactTracker;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.util.CalendarUtil;

public class DBHelper extends SQLiteOpenHelper implements SQLiteRepository {

    private static final String TAG = "DBHelper";
    private final static String DATABASE_NAME = "social.distance.reminder.db";
    private static final int DATABASE_VERSION = 1;

    // user table
    private static final String USER_TABLE_NAME = "user";
    private static final String COL_UT_ID = "id";                   // 1
    private static final String COL_UT_USER_ID = "user_id";         // UUID
    private static final String COL_UT_TIMESTAMP = "timestamp";     // TIMESTAMP
    private static final String COL_UT_VERIFIED = "verified";       //
    private static final String COL_UT_FCM = "fcm";
    private static final String COL_UT_MOBILE_NUMBER = "mobile_number";
    private static final String COl_UT_USER_ENABLE = "user_enable";
    private static final String COL_UT_DEVICE_UUID = "device_uuid";
    // contact tracking table
    private static final String CONTACT_TRACKING_TABLE_NAME = "contact_tracking";
    private static final String COL_CTT_ID = "id";
    private static final String COL_CTT_CONTACTED_USER_ID = "contacted_user_id";
    private static final String COL_CTT_TIMESTAMP = "timestamp";
    private static final String COL_CTT_CLASS = "class";
    // covid contacted table
    private static final String COVID_CONTACTED_TABLE_NAME = "covid_contacted";
    private static final String COL_CCT_ID = "id";
    private static final String COL_CCT_DATES = "date";
    private static final String COL_CCT_CLASS = "class";
    private static final String COL_CCT_READ_NOTIFICATION = "read_notification";
    // covid infected table
    private static final String COVID_INFECTED_TABLE_NAME = "covid_infected";
    private static final String COL_CIT_ID = "id";
    private static final String COL_CIT_DATE = "date";
    // user configurations
    private static final String USER_CONFIG_TABLE_NAME = "user_configuration";
    private static final String COL_UCT_ID = "id";
    private static final String COL_UCT_ENABLE_BEACON_SERVICE = "enable_beacon_service";
    private static final String COL_UCT_ENABLE_VIBRATE = "enable_vibrate";
    private static DBHelper dbHelper = null;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "begin creating db tables");

        String user_table_query, contact_tracking_table_query, covid_contacted_table_query,
                covid_infected_table_query, user_config_table_query;

        user_table_query = "CREATE TABLE " + USER_TABLE_NAME + " ( " +
                COL_UT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_UT_USER_ID + " TEXT, " +
                COL_UT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                COL_UT_VERIFIED + " INTEGER DEFAULT 0, " +
                COL_UT_FCM + " TEXT, " +
                COL_UT_MOBILE_NUMBER + " TEXT, " +
                COL_UT_DEVICE_UUID + " TEXT, " +
                COl_UT_USER_ENABLE + " INETEGER)";

        // contact tracking table
        contact_tracking_table_query = "CREATE TABLE " + CONTACT_TRACKING_TABLE_NAME + " ( " +
                COL_CTT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CTT_CONTACTED_USER_ID + " TEXT, " +
                COL_CTT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                COL_CTT_CLASS + " INTEGER )";

        // covid contacted table
        covid_contacted_table_query = "CREATE TABLE " + COVID_CONTACTED_TABLE_NAME + " ( " +
                COL_CCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CCT_DATES + " DATE, " +
                COL_CCT_CLASS + " INTEGER, " +
                COL_CCT_READ_NOTIFICATION + " INTEGER)";

        // covid infected table
        covid_infected_table_query = "CREATE TABLE " + COVID_INFECTED_TABLE_NAME + " ( " +
                COL_CIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CIT_DATE + " DATE)";

        // user configurations
        user_config_table_query = "CREATE TABLE " + USER_CONFIG_TABLE_NAME + " ( " +
                COL_UCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_UCT_ENABLE_BEACON_SERVICE + " INTEGER, " +
                COL_UCT_ENABLE_VIBRATE + " INTEGER) ";

        db.execSQL(user_table_query);
        db.execSQL(contact_tracking_table_query);
        db.execSQL(covid_contacted_table_query);
        db.execSQL(covid_infected_table_query);
        db.execSQL(user_config_table_query);

        Log.d(TAG, "end creating db tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "begin updating table structures");

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TRACKING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COVID_CONTACTED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COVID_INFECTED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_CONFIG_TABLE_NAME);

        onCreate(db);
        Log.d(TAG, "end updating table structures");
    }


    @Override
    public void saveSocialDistancingViolation(String contactedUserId, Integer riskLevel) {
        Log.d(TAG, "begin saving social distance violation for userId: " + contactedUserId
                + " riskLevel: " + riskLevel);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CTT_CONTACTED_USER_ID, contactedUserId);
        contentValues.put(COL_CTT_CLASS, riskLevel);
        contentValues.put(COL_CTT_TIMESTAMP, CalendarUtil.getCurrentDateTimeStr());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(CONTACT_TRACKING_TABLE_NAME, null, contentValues);

        Log.d(TAG, "end saving social distancing violation");
    }

    @SuppressLint("Range")
    @Override
    public User getUserDetails() {
        Log.d(TAG, "begin retrieve user details");
        User user = null;

        String get_user_details = "SELECT * FROM " + USER_TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDatabase.rawQuery(get_user_details, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COL_UT_ID)));
            user.setUserUUID(cursor.getString(cursor.getColumnIndex(COL_UT_USER_ID)));
            user.setVerifiedUser(cursor.getInt(cursor.getColumnIndex(COL_UT_VERIFIED)) == 1);
            user.setUserEnabled(cursor.getInt(cursor.getColumnIndex(COl_UT_USER_ENABLE)) == 1);
            user.setMobileNumber(cursor.getString(cursor.getColumnIndex(COL_UT_MOBILE_NUMBER)));
            user.setDeviceUUID(cursor.getString(cursor.getColumnIndex(COL_UT_DEVICE_UUID)));
        }
        cursor.close();

        Log.d(TAG, "end retrieving user details");
        return user;
    }


    @SuppressLint("Range")
    @Override
    public Map<Integer, Integer> getNumberOfContactsForEachRiskLevel() {
        Log.d(TAG, "begin to retrieve  risk level details");
        Map<Integer, Integer> riskLevelDetails = new HashMap<>();
        riskLevelDetails.put(ApplicationConstants.HIGH_RISK, 0);
        riskLevelDetails.put(ApplicationConstants.MILD_RISK, 0);
        riskLevelDetails.put(ApplicationConstants.LOW_RISK, 0);

        String contact_tracking_select_query = "SELECT COUNT(" + COL_CTT_ID + ") AS CCT_COUNT, "
                + COL_CTT_CLASS + " FROM " + CONTACT_TRACKING_TABLE_NAME +
                " GROUP BY " + COL_CTT_CLASS;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDatabase.rawQuery(contact_tracking_select_query, null);

        if (cursor.moveToFirst()) {
            do {
                riskLevelDetails.put(
                        cursor.getInt(cursor.getColumnIndex(COL_CTT_CLASS)),
                        cursor.getInt(cursor.getColumnIndex("CCT_COUNT"))
                );
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        Log.d(TAG, "end of retrieving risk level details");
        return riskLevelDetails;
    }

    @Override
    public void saveCovidContactedNotificationDetails() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Map<String, DeviceContactTracker> getCloseContactList(String dateOfPositive) {
        Log.d(TAG, "begin retrieve close contact list");
        Timestamp dateOfPositiveTimeStamp = new Timestamp(Long.parseLong(dateOfPositive));

        String beginDateTimestamp = CalendarUtil.getTimestampBeforeNoOfDays(
                dateOfPositiveTimeStamp.toString(), 5);
        String endDateTimestamp = CalendarUtil.getTimestampAfterNoOfDays(
                dateOfPositiveTimeStamp.toString(), 5);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String contacts_selecting_query = "SELECT * FROM " + CONTACT_TRACKING_TABLE_NAME +
                " WHERE " + COL_CTT_TIMESTAMP
                + " BETWEEN ? AND ? ";

        Cursor cursor = sqLiteDatabase.rawQuery(contacts_selecting_query,
                new String[]{beginDateTimestamp, endDateTimestamp});

        Map<String, DeviceContactTracker> deviceContactTrackerMap = new HashMap<>();

        while (cursor.moveToNext()){
            @SuppressLint("Range")
            String contactedDeviceUUID = cursor.getString(
                    cursor.getColumnIndex(COL_CTT_CONTACTED_USER_ID));
            @SuppressLint("Range")
            String contactedDate = cursor.getString(cursor.getColumnIndex(COL_CTT_TIMESTAMP));
            contactedDate = CalendarUtil.getDateStrFromTimeStamp(contactedDate);
            @SuppressLint("Range")
            Integer riskLevel = cursor.getInt(cursor.getColumnIndex(COL_CTT_CLASS));

            DeviceContactTracker deviceContactTracker
                    = deviceContactTrackerMap.get(contactedDeviceUUID);
            if(deviceContactTracker != null){
                deviceContactTracker.addContactedDateAndRiskLevel(
                        Pair.create(contactedDate, riskLevel)
                );

            }else {
                deviceContactTracker =  new DeviceContactTracker(contactedDeviceUUID);
                deviceContactTracker.addContactedDateAndRiskLevel(
                        Pair.create(contactedDate, riskLevel)
                );
                deviceContactTrackerMap.put(contactedDeviceUUID,
                        deviceContactTracker);
            }
        }

        Log.d(TAG, "end retrieve close contact list");
        return deviceContactTrackerMap;
    }

    @Override
    public void updateBeaconEnableConfig(String userId, boolean value) {
        Log.d(TAG, "begin updating user beacon enabling config " + " value : " + value);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_UCT_ENABLE_BEACON_SERVICE, value ? 1 : 0);
        sqLiteDatabase.update(USER_CONFIG_TABLE_NAME, contentValues,
                "id = ?", new String[]{"1"});

        Log.d(TAG, "end updating user beacon enabling config");
    }

    @SuppressLint("Range")
    @Override
    public UserConfig getUserConfigs() {
        Log.d(TAG, "begin retrieve user configs");

        UserConfig userConfig = null;

        String get_user_config_details = "SELECT * FROM " + USER_CONFIG_TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDatabase.rawQuery(get_user_config_details, null);

        if (cursor.moveToFirst()) {
            userConfig = new UserConfig();
            userConfig.setId(cursor.getInt(cursor.getColumnIndex(COL_UCT_ID)));
            userConfig.setEnableBeaconService(cursor.getInt(
                    cursor.getColumnIndex(COL_UCT_ENABLE_BEACON_SERVICE)) == 1);
            userConfig.setEnableVibrating(
                    cursor.getInt(cursor.getColumnIndex(COL_UCT_ENABLE_VIBRATE)) == 1);
        }
        cursor.close();

        Log.d(TAG, userConfig.toString());

        Log.d(TAG, "end retrieving user configs");
        return userConfig;
    }

    @Override
    public void updateUserConfig(Integer id, Boolean enableBeaconService, Boolean enableVibrating) {
        Log.d(TAG, "begin updating user config details");

        if (id != null) {
            ContentValues contentValues = new ContentValues();
            if (enableBeaconService != null) {
                contentValues.put(COL_UCT_ENABLE_BEACON_SERVICE, enableBeaconService ? 1 : 0);
            }
            if (enableVibrating != null) {
                contentValues.put(COL_UCT_ENABLE_VIBRATE, enableVibrating ? 1 : 0);
            }

            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.update(USER_CONFIG_TABLE_NAME, contentValues,
                    " id = ? ", new String[]{id.toString()});
        }

        Log.d(TAG, "end updating user config details");
    }

    @Override
    public void initializeTables() {
        Log.d(TAG, "begin populating data for user and user config tables");

        ContentValues userDetailsCV = new ContentValues();
        userDetailsCV.put(COL_UT_VERIFIED, 0);
        userDetailsCV.put(COl_UT_USER_ENABLE, 0);

        ContentValues userConfigDetailsCV = new ContentValues();
        userConfigDetailsCV.put(COL_UCT_ENABLE_BEACON_SERVICE, 0);
        userConfigDetailsCV.put(COL_UCT_ENABLE_VIBRATE, 0);


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(USER_TABLE_NAME, null, null);
        sqLiteDatabase.delete(USER_CONFIG_TABLE_NAME, null, null);
        sqLiteDatabase.insert(USER_TABLE_NAME, null, userDetailsCV);
        sqLiteDatabase.insert(USER_CONFIG_TABLE_NAME, null, userConfigDetailsCV);

        Log.d(TAG, "end populating data for user and user config tables");
    }

    @Override
    public void updateUserDetails(User user) {
        Log.d(TAG, "begin updating user details");
        if (user.getId() != -1) {
            ContentValues contentValues = new ContentValues();

            if(user.getUserUUID() != null){
                contentValues.put(COL_UT_USER_ID, user.getUserUUID());
            }
            if (user.getMobileNumber() != null) {
                contentValues.put(COL_UT_MOBILE_NUMBER, user.getMobileNumber());
            }
            if (user.getVerifiedUser() != null) {
                contentValues.put(COL_UT_VERIFIED, user.getVerifiedUser() ? 1 : 0);
            }
            if (user.getUserEnabled() != null) {
                contentValues.put(COl_UT_USER_ENABLE, user.getUserEnabled() ? 1 : 0);
            }
            if(user.getFcmToken() != null){
                contentValues.put(COL_UT_FCM, user.getFcmToken());
            }
            if(user.getDeviceUUID() != null){
                contentValues.put(COL_UT_DEVICE_UUID, user.getDeviceUUID());
            }

            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Integer val = sqLiteDatabase.update(USER_TABLE_NAME, contentValues,
                    " id = ?", new String[]{user.getId().toString()});
        }

        Log.d(TAG, "end updating user details");
    }

}
