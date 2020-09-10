package com.fairmontsintenational.fis_remote_learning.forgotPassword;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import com.fairmontsintenational.fis_remote_learning.R;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;
import com.fairmontsintenational.fis_remote_learning.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText phoneNo = findViewById(R.id.PhoneNo);

        findViewById(R.id.Proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNo.getText().toString().isEmpty()){
                    phoneNo.setError(getString(R.string.phone_required));
                    phoneNo.requestFocus();
                }else if(phoneNo.getText().toString().length() < 10){
                    phoneNo.setError(getString(R.string.phone_invalid_format));
                    phoneNo.requestFocus();
                }else{
                    final AlertDialog dialog = Utils.ShowLoader(ForgotPassword.this);
                    dialog.show();
                    String url = BaseUrl.getRequestResetCode(phoneNo.getText().toString().trim());

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    dialog.dismiss();
                                    try {
                                        Intent intent = new Intent(ForgotPassword.this,VerificationCode.class);
                                        intent.putExtra("PhoneNo",phoneNo.getText().toString());
                                        intent.putExtra("Code",response.getString("Check"));
                                        startActivity(intent);
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ShowSnackBarError("Failed, please contact our help desk or try again later.");
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
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
                    RequestQueue queue = Volley.newRequestQueue(ForgotPassword.this);
                    queue.add(jsonObjectRequest);

                }
            }
        });
    }

    private void ShowSnackBarError(String message){
        Snackbar.make(findViewById(R.id.ForgotLayout),message,Snackbar.LENGTH_SHORT).show();
    }
}
