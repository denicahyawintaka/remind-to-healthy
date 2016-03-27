package com.pt2;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SecondActivity extends ActionBarActivity {

    TextView secInfo;
    Button btnStop;

    Ringtone ringTone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        secInfo = (TextView)findViewById(R.id.secinfo);
        btnStop = (Button)findViewById(R.id.stop);

        String stringUri = getIntent().getStringExtra("SEC_RINGTONE_URI");
        Uri uri = Uri.parse(stringUri);
        //secInfo.setText("uri: " + uri + "\n");

        ringTone = RingtoneManager
                .getRingtone(getApplicationContext(), uri);

        secInfo.append(ringTone.getTitle(SecondActivity.this));

        ringTone.play();

        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ringTone != null){
                    ringTone.stop();
                    ringTone = null;
                    startActivity(new Intent(SecondActivity.this, PasienActivity.class));
                }
            }
        });

    }

}
