package lk.ac.mrt.cse.cs4472.social_distance_reminder.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ApplicationConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;

public class HTTPService {

    private static final String TAG = "HTTPService";
    private static SQLiteRepository sqLiteRepository;
    private static HTTPService httpService = null;
    private final RequestQueue queue;

    private HTTPService(Context context) {
        sqLiteRepository = DBHelper.getInstance(context);
        queue = Volley.newRequestQueue(context);
    }

    public static synchronized HTTPService getInstance(Context context) {
        if (httpService == null) {
            httpService = new HTTPService(context);
        }
        return httpService;
    }


    /**
     * Send HTTP Request to save FCMToken and receive deviceUUID
     *
     * @param endPoint REST API endpoint suffix
     * @param body     json body
     * @param params   User : { id, userUUID }
     */
    public void postFCMToken(String endPoint, Map<String, String> body, String... params) {
        Log.d(TAG, "begin sending http request to store fcmToken");


        String finalUrl = ApplicationConstants.FIREBASE_REST_API_BASE_URL + endPoint;

        try {
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.POST, finalUrl, new JSONObject(body),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(TAG, "fcmToken saving successful");
                                    try {
                                        User user = new User(Integer.valueOf(params[0]));
                                        user.setUserUUID(params[1]);
                                        user.setDeviceUUID(response.getString("deviceUUID"));
                                        Log.i(TAG, user.toString());

                                        sqLiteRepository.updateUserDetails(user);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());

                        }
                    });

            this.queue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "end sending http request to store fcmToken");
    }

}
