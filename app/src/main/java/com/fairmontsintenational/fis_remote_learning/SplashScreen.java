package com.fairmontsintenational.fis_remote_learning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fairmontsintenational.fis_remote_learning.classes.Sessions;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    SharedPreferences prefs = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        prefs = getSharedPreferences("com.maputo.app", MODE_PRIVATE);
        Paper.init(this);
        fetchurl();

        TextView poweredBy = findViewById(R.id.ProweredBy);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        poweredBy.setText(getString(R.string.copyright)+year+". "+getString(R.string.powered_by_fis));

    }

    @Override
    protected void onResume() {
        super.onResume();
        int SPLASH_TIME_OUT = 3000;
        if (prefs.getBoolean("firstrun", true)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Paper.book().write("Session", Sessions.InActive.toString());
                    startActivity(new Intent(SplashScreen.this, Introduction.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
            prefs.edit().putBoolean("firstrun", false).apply();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkSession();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void checkSession() {
        String session = Paper.book().read("Session").toString();
        if(session.equals(Sessions.Active.toString())){
            startActivity(new Intent(SplashScreen.this, Homepage.class));
            finish();
        }else if(session.equals(Sessions.InActive.toString())){
            startActivity(new Intent(SplashScreen.this, Login.class));
            finish();
        }
    }

    private void fetchurl() {
        final JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, BaseUrl.GET_MAIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("User");
                            Paper.book().write("Main_url",jsonArray.get(0).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Invalid Credentials! Please try again!!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }else{
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}
