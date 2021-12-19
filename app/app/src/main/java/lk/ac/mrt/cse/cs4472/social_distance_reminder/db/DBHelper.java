package lk.ac.mrt.cse.cs4472.social_distance_reminder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ApplicationConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;

public class DBHelper extends SQLiteOpenHelper implements SQLiteRepository {

    private static final String TAG = "DBHelper";
    private static DBHelper dbHelper = null;

    private final static String DATABASE_NAME = "social.distance.reminder.db";
    private static final int DATABASE_VERSION = 1;
    private static final int update_time = 10;

    // user table
    private static final String USER_TABLE_NAME = "user";
    private static final String COL_UT_ID = "id";
    private static final String COL_UT_USER_ID = "user_id";
    private static final String COL_UT_TIMESTAMP = "timestamp";
    private static final String COL_UT_VERIFIED = "verified";
    private static final String COL_UT_FCM = "fcm";

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

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context){
        if(dbHelper == null){
            synchronized (DBHelper.class){
                if(dbHelper == null){
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user_table_query, contact_tracking_table_query, covid_contacted_table_query,
                covid_infected_table_query, user_config_table_query;

        user_table_query = "CREATE TABLE " + USER_TABLE_NAME + " ( " +
                            COL_UT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_UT_USER_ID + " TEXT, " +
                            COL_UT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            COL_UT_VERIFIED + " INTEGER DEFAULT 0, " +
                            COL_UT_FCM + " TEXT)";

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
                COL_UCT_ID + " TEXT, " +
                COL_UCT_ENABLE_BEACON_SERVICE + " INTEGER, " +
                COL_UCT_ENABLE_VIBRATE + " INTEGER) ";

        db.execSQL(user_table_query);
        db.execSQL(contact_tracking_table_query);
        db.execSQL(covid_contacted_table_query);
        db.execSQL(covid_infected_table_query);
        db.execSQL(user_config_table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TRACKING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COVID_CONTACTED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COVID_INFECTED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_CONFIG_TABLE_NAME);

        onCreate(db);
    }


    @Override
    public void saveSocialDistancingViolation(String contactedUserId, Integer riskLevel) {
        Log.d(TAG, "begin saving social distance violation for userId: " + contactedUserId
                + " riskLevel: " + riskLevel);

        // TODO : write query to save the value in db

        Log.d(TAG, "end saving social distancing violation");
    }

    @Override
    public User getUserDetails() {
        Log.d(TAG, "begin retrieve user details");

        User user = new User();

        Log.d(TAG, "end retrieving user details");
        return user;
    }

    @Override
    public Map<Integer, Integer> getNumberOfContactsForEachRiskLevel() {
        Log.d(TAG, "begin to retrieve  risk level details");
        Map<Integer, Integer> riskLevelDetails = new HashMap<>();
        riskLevelDetails.put(ApplicationConstants.HIGH_RISK, 0);
        riskLevelDetails.put(ApplicationConstants.MILD_RISK, 0);
        riskLevelDetails.put(ApplicationConstants.LOW_RISK, 0);



        Log.d(TAG, "end of retrieving risk level details");
        return riskLevelDetails;
    }

    @Override
    public void saveUserDetails() {

    }

    @Override
    public void saveCovidContactedNotificationDetails() {

    }

    @Override
    public void getPastContactsDetails() {

    }

    @Override
    public void updateBeaconEnableConfig(String userId, boolean value) {
        Log.d(TAG, "begin updating user beacon enabling config " + " value : " + value);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_UCT_ENABLE_BEACON_SERVICE, value ? 1: 0);
        sqLiteDatabase.update(USER_CONFIG_TABLE_NAME, contentValues, userId, null);

        Log.d(TAG, "end updating user beacon enabling config");
    }

    @Override
    public void initializeTables() {
        Log.d(TAG, "begin populating data for user and user config tables");


        Log.d(TAG, "end populating data for user and user config tables");
    }
}
