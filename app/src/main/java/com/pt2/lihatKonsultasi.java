package com.pt2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class lihatKonsultasi extends AppCompatActivity {
    private static final String TAG = lihatKonsultasi.class.getSimpleName();
    private TextView username;
    private TextView namaPenyakitTxt;
    private TextView saranDokterTxt;
    private TextView obat;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    Ringtone ringTone;

    Uri uriAlarm;

    final static int RQS_RINGTONEPICKER = 1;

    final static int RQS_1 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_konsultasi);
        username = (TextView) findViewById(R.id.username);
        namaPenyakitTxt = (TextView) findViewById(R.id.namaPenyakit);
        saranDokterTxt = (TextView) findViewById(R.id.saranDokter);
        uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String pasien = user.get("id");
        checkKonsultasi(pasien);
    }


    private void checkKonsultasi(final String pasien) {
        // Tag used to cancel the request
        String tag_string_req = "req_lihatKonsultasi";

        pDialog.setMessage("TUNGGU BEBERAPA SAAT...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lihatKosultasi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Konsultasi Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in

                        // Now store the user in SQLite
                        JSONObject konsultasi = jObj.getJSONObject("konsultasi");
                        String idDokter = konsultasi.getString("idDokter");
                        String namaPenyakit = konsultasi.getString("namaPenyakit");
                        String saranDokter = konsultasi.getString("saranDokter");


                        username.setText(idDokter);
                        namaPenyakitTxt.setText(namaPenyakit);
                        saranDokterTxt.setText(saranDokter);
                        setAlarm(uriAlarm);

                        // Inserting row in users table
                        //db.addUser(name, email, id, created_at);
                        // Launch main activity

                            /*Intent intent = new Intent(lihatKonsultasi.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();*/
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pasien", pasien);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void setAlarm(Uri passuri) {

            Calendar cal = Calendar.getInstance();

          cal.set(1,
                1,
                1,
                1,
                1,
                1);

            String passString = passuri.toString();


            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            intent.putExtra("KEY_TONE_URL", passString);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getBaseContext(),
                    RQS_1,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}