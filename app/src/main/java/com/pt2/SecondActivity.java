package com.pt2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.pt2.fragment_alarm.alarmListener;

public class SecondActivity  extends FragmentActivity implements alarmListener {
    private static final String TAG = SecondActivity.class.getSimpleName();
    TextView secInfo;
    private SQLiteHandler db;
    Button btnStop, btnLihat;

    Ringtone ringTone;
    Uri uriAlarm;


    final static int RQS_1 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        secInfo = (TextView) findViewById(R.id.secinfo);
        btnStop = (Button) findViewById(R.id.belum);
        btnLihat = (Button) findViewById(R.id.lihatObat);
        db = new SQLiteHandler(getApplicationContext());

        String stringUri = getIntent().getStringExtra("SEC_RINGTONE_URI");
        Uri uri = Uri.parse(stringUri);
        //secInfo.setText("uri: " + uri + "\n");

        ringTone = RingtoneManager
                .getRingtone(getApplicationContext(), uri);

        secInfo.append(ringTone.getTitle(SecondActivity.this));

        ringTone.play();
        btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragment_alarm inputNameDialog = new fragment_alarm();
                inputNameDialog.setCancelable(false);
                inputNameDialog.setDialogTitle("Daftar Obat");
                inputNameDialog.show(fragmentManager, "Obat");
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringTone != null) {
                    ringTone.stop();
                    ringTone = null;

                    HashMap<String, String> obat = db.getObatDetail();
                    if (!obat.get("frekuensi").equals("0")) {
                        Calendar cal = Calendar.getInstance();
                        Date now = new Date();

                        System.out.println(now.getYear());
                        System.out.println(now.getMonth());
                        System.out.println(now.getDate());
                        System.out.println(now.getHours());
                        System.out.println(now.getMinutes());
                        int interval = Integer.parseInt(obat.get("interval").toString());
                        System.out.println(interval);
                        cal.set(2016,
                                now.getMonth(),
                                now.getDate(),
                                now.getHours(),
                                now.getMinutes() + interval,
                                00);
                        uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                        String passuriAlarm = uriAlarm.toString();
                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                        intent.putExtra("KEY_TONE_URL", passuriAlarm);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                getBaseContext(),
                                RQS_1,
                                intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                        db.updateFrekuensi();
                        System.out.println(obat.get("frekuensi"));
                    }

                    startActivity(new Intent(SecondActivity.this, menu_pasien.class));

                }
            }
        });

    }

    @Override
    public ArrayList<HashMap> getObat() {

        ArrayList<HashMap> obatList = db.getObatArray();
        return obatList;
    }
}