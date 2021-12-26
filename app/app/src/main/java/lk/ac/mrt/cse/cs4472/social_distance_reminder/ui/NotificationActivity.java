package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.adapter.NotificationListAdapter;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.NotificationModel;

public class NotificationActivity extends AppCompatActivity {

    private ListView listView;
    private List<NotificationModel> notificationList;
    private SQLiteRepository sqLiteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_notification_activity);

        sqLiteRepository = DBHelper.getInstance(this);

        listView = findViewById(R.id.notifications_list);

        notificationList = sqLiteRepository.getCovidContactedNotificationList();

        inflateListView(notificationList);
    }

    private void inflateListView(List<NotificationModel> notificationList) {
        NotificationListAdapter adapter = new NotificationListAdapter(this, notificationList);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setDividerHeight(5);
    }
}