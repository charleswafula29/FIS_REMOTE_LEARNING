package com.fis.fis_remote_learning.forgotPassword;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.fis.fis_remote_learning.R;
import com.fis.fis_remote_learning.utils.BaseUrl;
import com.fis.fis_remote_learning.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.fis.fis_remote_learning.utils.Utils.ShowFailedPopup;

public class VerificationCode extends AppCompatActivity {
    private AlertDialog dialog;
    private EditText Code,Password;
    private int setType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog = Utils.ShowLoader(VerificationCode.this);

        Intent intent = getIntent();
        final String phoneNo = intent.getStringExtra("PhoneNo");
        final String SentCode = intent.getStringExtra("Code").toString();

        Code = findViewById(R.id.Code);
        Password = findViewById(R.id.Password);
        final ImageView ShowPass =findViewById(R.id.ShowPass);

        ShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


        findViewById(R.id.Proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Code.getText().toString().isEmpty()){
                    Code.setError(getString(R.string.code_required));
                    Code.requestFocus();
                }else if(Password.getText().toString().isEmpty()){
                    Password.setError(getString(R.string.pass_required));
                    Password.requestFocus();
                }else if(Password.getText().toString().length()<6){
                    Password.setError(getString(R.string.pass_too_short));
                    Password.requestFocus();
                }else{
                    resetPassword(SentCode,Code.getText().toString().trim(),phoneNo,Password.getText().toString().trim());
                }
            }
        });
    }

    private void resetPassword(String SentCode,String code, String phoneNo, String NewPass) {
        dialog.show();
        String url = BaseUrl.getChangePassword(phoneNo,NewPass);

        if(!SentCode.equals(code)){
            dialog.dismiss();
            ShowFailedPopup(VerificationCode.this,"Incorrect Code",getString(R.string.code_mismatch_failed));
        }else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            try {
                                if(response.getString("Check").equals("Updated")){
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(VerificationCode.this);
                                    LayoutInflater inflater = LayoutInflater.from(VerificationCode.this);
                                    View view = inflater.inflate(R.layout.success_layout, null);
                                    builder.setView(view);
                                    final AlertDialog SuccessDialog = builder.create();
                                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    SuccessDialog.setCancelable(false);

                                    ((TextView) view.findViewById(R.id.title)).setText("Operation successful");
                                    ((TextView) view.findViewById(R.id.Message)).setText("Your password has been successfully updated. Login to access your homepage!");
                                    view.findViewById(R.id.Close).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            SuccessDialog.dismiss();
                                            finish();
                                        }
                                    });
                                    SuccessDialog.show();
                                }else{
                                    ShowFailedPopup(VerificationCode.this,"Operation failed",response.getString("Check"));
                                }
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
            RequestQueue queue = Volley.newRequestQueue(VerificationCode.this);
            queue.add(jsonObjectRequest);

        }
    }

    private void ShowSnackBarError(String message){
        Snackbar.make(findViewById(R.id.ForgotLayout),message,Snackbar.LENGTH_SHORT).show();
    }
}
