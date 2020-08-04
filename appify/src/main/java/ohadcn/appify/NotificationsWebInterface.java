package ohadcn.appify;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.widget.RemoteViews;

import static android.content.Context.NOTIFICATION_SERVICE;

class NotificationsWebInterface extends BaseWebInterface {

    @JavascriptInterface
    public void notification(final String title, final String content) {
        Notification notification = null;
        int channelCode = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(mContext)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(android.R.drawable.star_on)
                    .setAutoCancel(false)
                    .build();
        }

        NotificationManager mNotificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);

        mNotificationManager.notify("appify", channelCode, notification);
//        NotificationManagerCompat.from(mContext).notify(channelCode, notification);

    }

}
