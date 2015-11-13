package abt.androidblacktiger;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Provides methods for notifications for the Android Black Tiger App.
 * Author: Diarmuid
 */
public class NewLocationNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewLocation";

    /**
     * Displays a notification to show a new location that has been found.
     * When the notification is clicked, it opens Maps Activity with the same extras as the args
     * passed to the notification.
     * @param context A context for the app displaying the notification
     * @param word The english word to be displayed
     * @param translation The translated word to be displayed
     * @param location A string representation of the lat/lon coordinates to be passed to MapsActivity.
     * Author: Diarmuid
     */
    public static void notify(final Context context, final String word, final String translation, final String location) {
        final Resources res = context.getResources();
        // This image is used as the notification's large icon (thumbnail).
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.mao);
        final String title = res.getString(R.string.new_location_notification_title_template, word);
        final String text = res.getString(
                R.string.new_location_notification_placeholder_text_template, word, translation);
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(context.getString(R.string.word_intent_word), word);
        intent.putExtra(context.getString(R.string.word_intent_translation), translation);
        intent.putExtra(context.getString(R.string.word_intent_location), location);
        Intent settingsIntent = new Intent(context,SettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, settingsIntent,Intent.FILL_IN_ACTION);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)
                        // Set required fields, including the small icon, the
                        // notification title, and text.
                .setSmallIcon(R.drawable.mao_notif)
                .setContentTitle(title)
                .setContentText(text)
                        // All fields below this line are optional. -----------------------------
                        // Use a default priority (recognized on devices running Android
                        // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Provide a large icon, shown with the notification in the
                        // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                        // Set ticker text (preview) information for this notification.
                .setTicker(word)

                        // Set the pending intent to be initiated when the user touches
                        // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                        // Show expanded text content on devices running Android 4.1 or
                        // later.
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText("You have a new word to learn!"))

                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true)
                .addAction(R.drawable.mao_notif, "Stop Notifications", pendingIntent);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, String, String)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
