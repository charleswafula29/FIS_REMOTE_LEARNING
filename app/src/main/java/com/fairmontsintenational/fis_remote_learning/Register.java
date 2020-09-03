package com.fairmontsintenational.fis_remote_learning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import com.fairmontsintenational.fis_remote_learning.classes.Constants;
import com.fairmontsintenational.fis_remote_learning.classes.Sessions;
import com.fairmontsintenational.fis_remote_learning.models.LoginModel;
import com.fairmontsintenational.fis_remote_learning.models.RegistrationModel;
import com.fairmontsintenational.fis_remote_learning.models.StatusModel;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;
import params.com.stepview.StatusViewScroller;

import static com.fairmontsintenational.fis_remote_learning.utils.Utils.RegistrationSuccessPopup;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.convertCapitalText;

public class Register extends AppCompatActivity {

    private ScrollView PageOne,PageTwo;
    private TextView Next,Back,Register;
    private StatusViewScroller statusViewScroller;
    String gender = null;
    private int setType = 1;
    private int setType2 = 1;
    private EditText Fname,Lname,Email,Phone,Password,
            Spouse_Fname,Spouse_Lname,Spouse_Email,Spouse_Phone,Spouse_Password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage(getString(R.string.registering));

        Paper.init(this);

        PageOne = findViewById(R.id.PageOne);
        PageTwo = findViewById(R.id.PageTwo);
        Next = findViewById(R.id.Next);
        Back = findViewById(R.id.Back);
        Register = findViewById(R.id.Register);
        statusViewScroller = findViewById(R.id.StepView);
        Fname= findViewById(R.id.Register_Fname);
        Lname= findViewById(R.id.Register_Lname);
        Email= findViewById(R.id.Register_Email);
        Phone= findViewById(R.id.Register_PhoneNo);
        Password= findViewById(R.id.Register_Password);
        Spouse_Fname= findViewById(R.id.SpouseRegister_Fname);
        Spouse_Lname= findViewById(R.id.SpouseRegister_Lname);
        Spouse_Email= findViewById(R.id.SpouseRegister_Email);
        Spouse_Phone= findViewById(R.id.SpouseRegister_PhoneNo);
        Spouse_Password= findViewById(R.id.SpouseRegister_Password);
        RadioGroup genderGroup = findViewById(R.id.Register_Gender);
        final ImageView showPass = findViewById(R.id.ShowPass);
        final ImageView spouseShowPass = findViewById(R.id.SpouseShowPass);

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setType==1) {
                    setType = 0;
                    Password.setTransformationMethod(null);
                    if (Password.getText().length() > 0){
                        Password.setSelection(Password.getText().length());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            showPass.setImageDrawable(getDrawable(R.drawable.ic_hide_pass));
                        }else{
                            showPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide_pass));
                        }
                    }
                }
                else{
                    setType=1;
                    Password.setTransformationMethod(new PasswordTransformationMethod());
                    if(Password.getText().length() > 0){
                        Password.setSelection(Password.getText().length());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            showPass.setImageDrawable(getDrawable(R.drawable.ic_show_pass));
                        }else {
                            showPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_show_pass));
                        }
                    }
                }
            }
        });

        spouseShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setType2==1) {
                    setType2 = 0;
                    Spouse_Password.setTransformationMethod(null);
                    if (Spouse_Password.getText().length() > 0){
                        Spouse_Password.setSelection(Password.getText().length());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            spouseShowPass.setImageDrawable(getDrawable(R.drawable.ic_hide_pass));
                        }else{
                            spouseShowPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide_pass));
                        }
                    }
                }
                else{
                    setType2=1;
                    Spouse_Password.setTransformationMethod(new PasswordTransformationMethod());
                    if(Spouse_Password.getText().length() > 0){
                        Spouse_Password.setSelection(Password.getText().length());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            spouseShowPass.setImageDrawable(getDrawable(R.drawable.ic_show_pass));
                        }else {
                            spouseShowPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_show_pass));
                        }
                    }
                }
            }
        });

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                gender = radioButton.getText().toString();
            }
        });

        ((LinearLayout) findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePageOne();
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageOne.setVisibility(View.VISIBLE);
                PageTwo.setVisibility(View.GONE);
                Next.setVisibility(View.VISIBLE);
                Back.setVisibility(View.GONE);
                Register.setVisibility(View.GONE);
                statusViewScroller.getStatusView().setCurrentCount(1);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePageTwo();
            }
        });


    }

    private void validatePageTwo() {
        progressDialog.show();
        RegistrationModel model =
                new RegistrationModel(null,
                        convertCapitalText(Fname.getText().toString()),
                        convertCapitalText(Lname.getText().toString()),
                        Phone.getText().toString(),
                        Email.getText().toString(),
                        gender,
                        Password.getText().toString(),
                        (Spouse_Fname.getText().toString().isEmpty()? null:convertCapitalText(Spouse_Fname.getText().toString())),
                        (Spouse_Lname.getText().toString().isEmpty()? null:convertCapitalText(Spouse_Lname.getText().toString())),
                        (Spouse_Phone.getText().toString().isEmpty()? null:Spouse_Phone.getText().toString()),
                        (Spouse_Fname.getText().toString().isEmpty()? null:((gender.equals(Constants.Male.toString()))?Constants.Female.toString():Constants.Male.toString())),
                        (Spouse_Password.getText().toString().isEmpty()? null:Spouse_Password.getText().toString()),
                        (Spouse_Email.getText().toString().isEmpty()? null:Spouse_Email.getText().toString()),
                        null);

        final Gson gson = new Gson();
        final String param = gson.toJson(model);
        Log.e("RegisterModel",param);

        try {
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.POST, BaseUrl.getRegister(), new JSONObject(param),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Resp",response.toString());
                                    try {
                                        StatusModel statusModel = gson.fromJson(response.getJSONObject("status").toString(), StatusModel.class);
                                        Log.e("Resp_Next",statusModel.toString());
                                        if(statusModel.getCode().equals("1")){
                                            AlertDialog dialog = RegistrationSuccessPopup(com.fairmontsintenational.fis_remote_learning.Register.this);
                                            startActivity(new Intent(Register.this,Login.class));
                                            dialog.show();

//                                            Toast.makeText(Register.this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
//                                            login(Phone.getText().toString(),Password.getText().toString());
                                        }else{
                                            progressDialog.dismiss();
                                            ShowSnackBarError(statusModel.getMessage());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
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
                                message = error.toString();
                            }
                            ShowSnackBarError(message);
                        }
                    });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(Register.this);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void login(String number, String pass) {
//
//        String url = BaseUrl.getLogin(number, pass);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
//                        Gson gson = new Gson();
//                        try {
//                            final LoginModel model = gson.fromJson(response.getJSONObject("Response").toString(), LoginModel.class);
//                            if(model.getStatus().getCode().equals("0")){
//                                ShowSnackBarError(model.getStatus().getMessage());
//
//                            }else{
//                                Paper.book().write("Session", Sessions.Active.toString());
//                                String parentModel = gson.toJson(model.getData());
//                                Paper.book().write("ParentData",parentModel);
//                                startActivity(new Intent(Register.this, Homepage.class));
//                                finish();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                String message = null;
//                if (error instanceof NetworkError) {
//                    message = getString(R.string.network_error);
//                } else if (error instanceof ServerError) {
//                    message = getString(R.string.server_error);
//                } else if (error instanceof AuthFailureError) {
//                    message = getString(R.string.auth_error);
//                } else if (error instanceof ParseError) {
//                    message = getString(R.string.parse_error);
//                } else if (error instanceof TimeoutError) {
//                    message = getString(R.string.timeout_error);
//                } else {
//                    ShowSnackBarError(error.toString());
//                }
//                ShowSnackBarError(message);
//            }
//        });
//
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                -1,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue queue = Volley.newRequestQueue(Register.this);
//        queue.add(jsonObjectRequest);
//
//    }

    private void validatePageOne() {
        if(Fname.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.first_name));
            Fname.requestFocus();
        }
        else if(Lname.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.last_name));
            Lname.requestFocus();
        }
        else if(Email.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.email_address));
            Email.requestFocus();
        }
        else if(Phone.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.email_address));
            Phone.requestFocus();
        }
        else if(Password.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.password));
            Password.requestFocus();
        }
        else if(gender==null){
            ShowSnackBar(getString(R.string.gender));
        }else {
            PageOne.setVisibility(View.GONE);
            PageTwo.setVisibility(View.VISIBLE);
            Next.setVisibility(View.GONE);
            Back.setVisibility(View.VISIBLE);
            Register.setVisibility(View.VISIBLE);
            statusViewScroller.getStatusView().setCurrentCount(2);
        }
    }

    private void ShowSnackBar(String field){
        Snackbar.make(findViewById(R.id.RegisterLayout),field+" "+getString(R.string.required),Snackbar.LENGTH_LONG).show();
    }

    private void ShowSnackBarError(String message){
        Snackbar.make(findViewById(R.id.RegisterLayout),message,Snackbar.LENGTH_LONG).show();
    }

}
