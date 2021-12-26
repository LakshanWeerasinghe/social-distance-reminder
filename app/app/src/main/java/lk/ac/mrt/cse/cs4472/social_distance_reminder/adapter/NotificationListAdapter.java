package lk.ac.mrt.cse.cs4472.social_distance_reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.NotificationModel;

public class NotificationListAdapter extends ArrayAdapter<NotificationModel> {
    public NotificationListAdapter(Context context, List<NotificationModel> notificationList) {
        super(context, 0, notificationList);
    }

    // called for each item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationModel notification = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sdr_notification_card_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.notification_list_item_text_view);
        textView.setText(notification.getMessage());

        TextView covidPositiveDateTextView = convertView.findViewById(
                R.id.notification_covid_positive_date_text_view);
        covidPositiveDateTextView.setText(notification.getDate());

        return convertView;
    }
}
