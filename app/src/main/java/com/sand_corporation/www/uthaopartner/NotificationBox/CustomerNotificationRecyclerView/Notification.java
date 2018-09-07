package com.sand_corporation.www.uthaopartner.NotificationBox.CustomerNotificationRecyclerView;

/**
 * Created by HP on 12/12/2017.
 */

public class Notification {

    private String date, title, message;
    private int notification_read_status;

    public Notification(String date, String title, String message, int notification_read_status) {
        this.date = date;
        this.title = title;
        this.message = message;
        this.notification_read_status = notification_read_status;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getNotification_read_status() {
        return notification_read_status;
    }
}
