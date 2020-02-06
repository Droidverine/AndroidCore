package com.droidverine.androidcore;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class MainActivity extends AppCompatActivity {
    String CHANNEL_ID="Androidcoreexample";
    Switch switchcharging,switchdata;
    static int JOB_ID=111;
    static  int datast;
    JobScheduler mScheduler;
    static String option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        switchcharging=findViewById(R.id.switch1);
        switchdata=findViewById(R.id.switch2);
        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());


        if(switchdata.isChecked())
        {
            datast=0;

        }else {
            datast=1;
        }
        //Jobscheduler


        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(datast)
                .setRequiresCharging(switchdata.isChecked())
                ;
        JobInfo myJobInfo = builder.build();
        mScheduler.schedule(myJobInfo);
        createNotificationChannel();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Toast
                Toast.makeText(getApplicationContext(),"Toast here",Toast.LENGTH_LONG).show();

                //Notifications

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                PendingIntent contentIntent= PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("My notification")
                        .setContentText("Much longer text that cannot fit one line...")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Much longer text that cannot fit one line dvvdfvdfvdvdfvdfvdvdvdv..."))
                        .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent)
                        .setColor(Color.BLUE)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        ;
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(11, builder.build());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
