package com.fairmontsintenational.fis_remote_learning;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.fairmontsintenational.fis_remote_learning.classes.Constants;
import com.fairmontsintenational.fis_remote_learning.classes.Sessions;
import com.fairmontsintenational.fis_remote_learning.models.LoginModel;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

import static com.fairmontsintenational.fis_remote_learning.utils.Utils.LockedAccountPopup;

public class Login extends AppCompatActivity {
    private Button BtnLogin;
    private TextView SignUp;
    private EditText phoneNo,Password;
    private ProgressBar progressBar;
    private ImageView ShowPass;
    private int setType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BtnLogin = findViewById(R.id.Login);
        SignUp = findViewById(R.id.SignUp);
        phoneNo = findViewById(R.id.PhoneNo);
        Password = findViewById(R.id.Password);
        progressBar = findViewById(R.id.Loader);
        ShowPass = findViewById(R.id.ShowPass);
        Paper.init(this);

        ShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setType==1) {
                    setType = 0;
                    Password.setTransformationMethod(null);
                    if (Password.getText().length() > 0){
                        Password.setSelection(Password.getText().length());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ShowPass.setImageDrawable(getDrawable(R.drawable.ic_hide_pass));
                        }else{
                            ShowPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide_pass));
                        }
                    }
                }
                else{
                    setType=1;
                    Password.setTransformationMethod(new PasswordTransformationMethod());
                    if(Password.getText().length() > 0){
                        Password.setSelection(Password.getText().length());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ShowPass.setImageDrawable(getDrawable(R.drawable.ic_show_pass));
                        }else {
                            ShowPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_show_pass));
                        }
                    }
                }
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNo.getText() == null){
                    phoneNo.setError(getString(R.string.phone_required));
                    phoneNo.requestFocus();
                }else if(phoneNo.getText().toString().length() < 10){
                    phoneNo.setError(getString(R.string.phone_invalid_format));
                    phoneNo.requestFocus();
                }
                else if(Password.getText() == null){
                    Password.setError(getString(R.string.pass_required));
                    Password.requestFocus();
                }else{
                    String number = phoneNo.getText().toString().trim();
                    String pass = Password.getText().toString().trim();
                    login(number,pass);
                }
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });
    }

    private void login(final String number, final String pass) {
        BtnLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        String url = BaseUrl.getLogin(number, pass);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BtnLogin.setEnabled(true);
                        Gson gson = new Gson();
                        try {
                            Log.e("resp",response.toString());
                            final LoginModel model = gson.fromJson(response.getJSONObject("Response").toString(), LoginModel.class);
                            if(model.getStatus().getCode().equals("0")){
                                progressBar.setVisibility(View.GONE);
                                Snackbar.make(findViewById(R.id.LoginLayout),model.getStatus().getMessage(),Snackbar.LENGTH_LONG).show();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                if(model.getData().getAccountStatus()==null){
                                    Paper.book().write("Session", Sessions.Active.toString());
                                    String parentModel = gson.toJson(model.getData());
                                    Paper.book().write("ParentData",parentModel);
                                    Paper.book().write("Phone",number);
                                    Paper.book().write("Pass",pass);
                                    startActivity(new Intent(Login.this, Homepage.class));
                                    finish();
                                }else{
                                    if(model.getData().getAccountStatus().equals("Pending")){
                                        AlertDialog alertDialog = LockedAccountPopup(Login.this);
                                        alertDialog.show();
                                    }else{
                                        Paper.book().write("Session", Sessions.Active.toString());
                                        String parentModel = gson.toJson(model.getData());
                                        Paper.book().write("ParentData",parentModel);
                                        Paper.book().write("Phone",number);
                                        Paper.book().write("Pass",pass);
                                        startActivity(new Intent(Login.this, Homepage.class));
                                        finish();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BtnLogin.setEnabled(true);
                progressBar.setVisibility(View.GONE);

                String message = null;
                if (error instanceof NetworkError) {
                    message = getString(R.string.network_error);
                } else if (error instanceof ServerError) {
                    message = getString(R.string.server_error);
                } else if (error instanceof AuthFailureError) {
                    message = getString(R.string.auth_error);
                } else if (error instanceof ParseError) {
                    message = getString(R.string.parse_error);
                } else if (error instanceof TimeoutError) {
                    message = getString(R.string.timeout_error);
                } else {
                    ShowSnackBarError(error.toString());
                }
                ShowSnackBarError(message);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(jsonObjectRequest);

    }

    private void ShowSnackBarError(String message){
        Snackbar.make(findViewById(R.id.LoginLayout),message,Snackbar.LENGTH_LONG).show();
    }
}
