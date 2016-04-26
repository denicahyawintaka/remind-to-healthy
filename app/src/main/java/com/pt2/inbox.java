package com.pt2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class inbox extends AppCompatActivity {
    private static final String TAG = inbox.class.getSimpleName();
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private TextView namaPengirim1,namaPengirim2,namaPengirim3,namaPengirim4,namaPengirim5,tanggal1,tanggal2,tanggal3,tanggal4,tanggal5;
    private TextView Konsulsi1,Konsulsi2,Konsulsi3,Konsulsi4,Konsulsi5;
    private Button yes1,yes2,yes3,yes4,yes5;
    private Button no1,no2,no3,no4,no5;
    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5;
    final static int RQS_1 = 1;
    Uri uriAlarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        linearLayout1 = (LinearLayout) findViewById(R.id.inbok1);
        linearLayout2 = (LinearLayout) findViewById(R.id.inbok2);
        linearLayout3 = (LinearLayout) findViewById(R.id.inbok3);
        linearLayout4 = (LinearLayout) findViewById(R.id.inbok4);
        linearLayout5 = (LinearLayout) findViewById(R.id.inbok5);

        namaPengirim1 = (TextView) findViewById(R.id.namaPengirim1);
        namaPengirim2 = (TextView) findViewById(R.id.namaPengirim2);
        namaPengirim3 = (TextView) findViewById(R.id.namaPengirim3);
        namaPengirim4 = (TextView) findViewById(R.id.namaPengirim4);
        namaPengirim5 = (TextView) findViewById(R.id.namaPengirim5);


        Konsulsi1 = (TextView) findViewById(R.id.noKonsultasi1);
        Konsulsi2 = (TextView) findViewById(R.id.noKonsultasi2);
        Konsulsi3 = (TextView) findViewById(R.id.noKonsultasi3);
        Konsulsi4 = (TextView) findViewById(R.id.noKonsultasi4);
        Konsulsi5 = (TextView) findViewById(R.id.noKonsultasi5);

        tanggal1 = (TextView) findViewById(R.id.tanggalKonsultasi1);
        tanggal2 = (TextView) findViewById(R.id.tanggalKonsultasi2);
        tanggal3 = (TextView) findViewById(R.id.tanggalKonsultasi3);
        tanggal4 = (TextView) findViewById(R.id.tanggalKonsultasi4);
        tanggal5 = (TextView) findViewById(R.id.tanggalKonsultasi5);

        yes1 = (Button) findViewById(R.id.yes1);
        yes2 = (Button) findViewById(R.id.yes2);
        yes3 = (Button) findViewById(R.id.yes3);
        yes4 = (Button) findViewById(R.id.yes4);
        yes5 = (Button) findViewById(R.id.yes5);

        no1 = (Button) findViewById(R.id.no1);
        no2 = (Button) findViewById(R.id.no2);
        no3 = (Button) findViewById(R.id.no3);
        no4 = (Button) findViewById(R.id.no4);
        no5 = (Button) findViewById(R.id.no5);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String pasien = user.get("id");
        chekInbok(pasien);
    }

    private void chekInbok(final String pasien) {
        // Tag used to cancel the request
        String tag_string_req = "req_lihatKonsultasi";

        pDialog.setMessage("TUNGGU BEBERAPA SAAT...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lihatInbox, new Response.Listener<String>() {

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
                        JSONObject konsultasi = jObj.getJSONObject("cek");
                        JSONArray konsultasiList = konsultasi.getJSONArray("konsultasi");
                        for(int i=0;i<konsultasiList.length();i++){
                            switch (i){
                                case 0 :
                                    linearLayout1.setVisibility(View.VISIBLE);
                                    namaPengirim1.setText(konsultasiList.getJSONObject(i).getString("first_name"));
                                    tanggal1.setText(konsultasiList.getJSONObject(i).getString("tanggal"));

                                    final String id1= konsultasiList.getJSONObject(i).getString("idKonsultasi");
                                    Konsulsi1.setText("NOMOR KONSULTASI : "+ id1);

                                    final String namaPengirim1 =konsultasiList.getJSONObject(i).getString("first_name");
                                    yes1.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            db.deleteKonsultasidanObat();
                                            addObat(id1);
                                            db.addKonsultasi(id1,namaPengirim1);
                                            linearLayout1.setVisibility(View.GONE);
                                        }
                                    });

                                    no1.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            hapusKonsultasi(id1);
                                            linearLayout1.setVisibility(View.GONE);
                                        }
                                    });

                                    break;
                                case 1:
                                    linearLayout2.setVisibility(View.VISIBLE);
                                    namaPengirim2.setText(konsultasiList.getJSONObject(i).getString("first_name"));
                                    tanggal2.setText(konsultasiList.getJSONObject(i).getString("tanggal"));

                                    final String id2= konsultasiList.getJSONObject(i).getString("idKonsultasi");
                                    Konsulsi2.setText("NOMOR KONSULTASI : "+ id2);

                                    final String namaPengirim2 =konsultasiList.getJSONObject(i).getString("first_name");
                                    yes2.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            db.deleteKonsultasidanObat();
                                            addObat(id2);
                                            db.addKonsultasi(id2, namaPengirim2);
                                            linearLayout2.setVisibility(View.GONE);
                                        }
                                    });

                                    no2.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            hapusKonsultasi(id2);
                                            linearLayout2.setVisibility(View.GONE);
                                        }
                                    });
                                    break;
                                case 2 :
                                    linearLayout3.setVisibility(View.VISIBLE);
                                    namaPengirim3.setText(konsultasiList.getJSONObject(i).getString("first_name"));
                                    tanggal3.setText(konsultasiList.getJSONObject(i).getString("tanggal"));

                                    final String id3= konsultasiList.getJSONObject(i).getString("idKonsultasi");
                                    Konsulsi3.setText("NOMOR KONSULTASI : "+ id3);

                                    final String namaPengirim3 =konsultasiList.getJSONObject(i).getString("first_name");
                                    yes3.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            db.deleteKonsultasidanObat();
                                            addObat(id3);
                                            db.addKonsultasi(id3,namaPengirim3);
                                            linearLayout1.setVisibility(View.GONE);
                                        }
                                    });

                                    no3.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            hapusKonsultasi(id3);
                                            linearLayout3.setVisibility(View.GONE);
                                        }
                                    });
                                    break;
                                case 3 :
                                    linearLayout4.setVisibility(View.VISIBLE);
                                    namaPengirim4.setText(konsultasiList.getJSONObject(i).getString("first_name"));
                                    tanggal4.setText(konsultasiList.getJSONObject(i).getString("tanggal"));

                                    final String id4= konsultasiList.getJSONObject(i).getString("idKonsultasi");
                                    Konsulsi4.setText("NOMOR KONSULTASI : "+ id4);

                                    final String namaPengirim4 =konsultasiList.getJSONObject(i).getString("first_name");
                                    yes4.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            db.deleteKonsultasidanObat();
                                            addObat(id4);
                                            db.addKonsultasi(id4,namaPengirim4);
                                            linearLayout4.setVisibility(View.GONE);
                                        }
                                    });

                                    no4.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            hapusKonsultasi(id4);
                                            linearLayout4.setVisibility(View.GONE);
                                        }
                                    });
                                    break;
                                case 4 :
                                    linearLayout5.setVisibility(View.VISIBLE);
                                    namaPengirim5.setText(konsultasiList.getJSONObject(i).getString("first_name"));
                                    tanggal5.setText(konsultasiList.getJSONObject(i).getString("tanggal"));

                                    final String id5= konsultasiList.getJSONObject(i).getString("idKonsultasi");
                                    Konsulsi5.setText("NOMOR KONSULTASI : "+ id5);

                                    final String namaPengirim5 =konsultasiList.getJSONObject(i).getString("first_name");
                                    yes5.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            db.deleteKonsultasidanObat();
                                            addObat(id5);
                                            db.addKonsultasi(id5,namaPengirim5);
                                            linearLayout5.setVisibility(View.GONE);
                                        }
                                    });

                                    no5.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            hapusKonsultasi(id5);
                                            linearLayout5.setVisibility(View.GONE);
                                        }
                                    });

                                    break;
                            }
                        }



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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }


    private void addObat(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_addObat";
        System.out.println(id);
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
                        JSONObject obat = jObj.getJSONObject("obat");
                        String idObat = obat.getString("idObat");
                        String idKonsultasi = obat.getString("idKonsultasi");
                        String namaObat = obat.getString("namaObat");
                        String frekuensi = obat.getString("frekuensi");
                        String interval = obat.getString("interval");
                        String dosis = obat.getString("interval");
                        String penggunaan = obat.getString("penggunaan");
                        db.addObat(idObat, idKonsultasi, namaObat, frekuensi, interval,dosis, penggunaan);

                        setAlarm(uriAlarm);

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
                params.put("idKonsultasi", id);

                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void hapusKonsultasi(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_hapus";
        System.out.println(id);
        pDialog.setMessage("TUNGGU BEBERAPA SAAT...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_hapusKonsultasi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Hapus Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Now store the user in SQLite

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
                params.put("idKonsultasi", id);

                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private void setAlarm(Uri passuri) {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();

        System.out.println(now.getYear());
        System.out.println(now.getMonth());
        System.out.println(now.getDate());
        System.out.println(now.getHours());
        System.out.println(now.getMinutes());

        cal.set(2016,
                now.getMonth(),
                now.getDate(),
                now.getHours(),
                now.getMinutes(),
                00);



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
        db.updateFrekuensi();
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