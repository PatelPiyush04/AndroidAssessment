package com.theherdonline.ui.notification.normal;

import com.theherdonline.db.entity.HerdNotification;

public interface NotificationListener {
    void onClickNotification(HerdNotification notification);
}
