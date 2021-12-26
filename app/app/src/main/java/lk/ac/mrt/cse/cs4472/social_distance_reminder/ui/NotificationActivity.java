package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.adapter.NotificationListAdapter;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.Notification;

public class NotificationActivity extends AppCompatActivity {

    private ListView listView;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_notification_activity);

        listView = findViewById(R.id.notifications_list);

        Notification notification1 = new Notification();
        notification1.setMessage("Contacted Covid");

        Notification notification2 = new Notification();
        notification2.setMessage("Delta Variant");

        Notification notification3 = new Notification();
        notification3.setMessage("Omicrone");

        Notification notification4 = new Notification();
        notification4.setMessage("Very Serious");

        Notification notification5 = new Notification();
        notification5.setMessage("Contacted Covid");

        Notification notification6 = new Notification();
        notification6.setMessage("Delta Variant");

        Notification notification7 = new Notification();
        notification7.setMessage("Omicrone");

        Notification notification8 = new Notification();
        notification8.setMessage("Very Serious");

        Notification notification9 = new Notification();
        notification9.setMessage("Contacted Covid");

        Notification notification10 = new Notification();
        notification10.setMessage("Delta Variant");

        Notification notification11 = new Notification();
        notification11.setMessage("Omicrone");

        Notification notification12 = new Notification();
        notification12.setMessage("Very Serious");

        notificationList = new ArrayList<>();
        notificationList.add(notification1);
        notificationList.add(notification2);
        notificationList.add(notification3);
        notificationList.add(notification4);
        notificationList.add(notification5);
        notificationList.add(notification6);
        notificationList.add(notification7);
        notificationList.add(notification8);
        notificationList.add(notification9);
        notificationList.add(notification10);
        notificationList.add(notification11);
        notificationList.add(notification12);

        inflateListView(notificationList);
    }

    private void inflateListView(List<Notification> notificationList) {
        NotificationListAdapter adapter = new NotificationListAdapter(this, notificationList);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setDividerHeight(5);
    }
}