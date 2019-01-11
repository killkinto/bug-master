package com.killkinto.bugmaster.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.killkinto.bugmaster.QuizActivity;
import com.killkinto.bugmaster.R;
import com.killkinto.bugmaster.data.DatabaseManager;
import com.killkinto.bugmaster.data.Insect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ReminderService extends IntentService {

    private static final String TAG = ReminderService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    public ReminderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Quiz reminder event triggered");

        //Present a notification to the user
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create action intent
        Intent action = new Intent(this, QuizActivity.class);
        Cursor cursor = DatabaseManager.getInstance(this).queryAllInsects(null);
        List<Insect> insects = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            insects.add(new Insect(cursor));
        }
        cursor.close();
        Collections.shuffle(insects);
        insects = insects.subList(0, QuizActivity.ANSWER_COUNT);
        ArrayList<Insect> options = new ArrayList<>(insects);
        action.putParcelableArrayListExtra(QuizActivity.EXTRA_INSECTS, options);
        action.putExtra(QuizActivity.EXTRA_ANSWER, options.get(new Random().nextInt(QuizActivity.ANSWER_COUNT)));

        PendingIntent operation =
                PendingIntent.getActivity(this, 0, action, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_bug_empty)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);
    }
}
