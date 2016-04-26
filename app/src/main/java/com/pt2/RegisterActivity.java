package com.pt2;

/**
 * Created by DeniCahya on 2/21/2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputfirst_name, inputlast_name, inputusername,inputkontak,inputalamat;
    private EditText inputEmail;
    private EditText inputPassword, inputRepassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private RadioGroup  radioGroup;
    private RadioButton radioButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputfirst_name= (EditText) findViewById(R.id.first_name);
        inputlast_name = (EditText) findViewById(R.id.last_name);
        inputusername= (EditText) findViewById(R.id.username);
        inputkontak= (EditText) findViewById(R.id.kontak);
        inputalamat= (EditText) findViewById(R.id.alamat);
        inputRepassword= (EditText) findViewById(R.id.re_password);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        radioGroup = (RadioGroup) findViewById(R.id.radioGrup);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    DokterActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String first_name = inputfirst_name.getText().toString().trim();
                String last_name = inputlast_name.getText().toString().trim();
                String username = inputusername.getText().toString().trim();
                String kontak = inputkontak.getText().toString().trim();
                String alamat = inputalamat.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String re_password = inputRepassword.getText().toString().trim();
                String status;
                int selectId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectId);
               status = radioButton.getText().toString();
                if (!first_name.isEmpty() && !last_name.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty()
                        && !re_password.isEmpty()) {
                    if(password.equals(re_password)) {
                        registerUser(first_name,last_name,username,kontak,alamat, email, password, status);
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Password tidak cocok", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Data wajib tidak boleh kosong", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String first_name,final String last_name,final String username,
                              final String kontak,final String alamat,final String  email,final String  password,
                              final String status) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String id = jObj.getString("id");

                        JSONObject user = jObj.getJSONObject("user");
                        String first_name = user.getString("first_name");
                        String last_name = user.getString("last_name");
                        String username = user.getString("username");
                        String kontak = user.getString("kontak");
                        String alamat = user.getString("alamat");
                        String email = user.getString("email");
                        String status= user.getString("status");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(id, first_name, last_name,username,
                                kontak,alamat, email, status, created_at);

                        Toast.makeText(getApplicationContext(), "User berhasi didaftarkan. Coba login sekarang!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("username", username);
                params.put("kontak", kontak);
                params.put("alamat", alamat);
                params.put("email", email);
                params.put("password", password);
                params.put("status", status);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
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