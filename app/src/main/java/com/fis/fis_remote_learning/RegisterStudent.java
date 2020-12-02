package com.fis.fis_remote_learning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.fis.fis_remote_learning.models.LevelsModel;
import com.fis.fis_remote_learning.models.LevelsResponseModel;
import com.fis.fis_remote_learning.models.LoginDataModel;
import com.fis.fis_remote_learning.models.RegStudentRespModel;
import com.fis.fis_remote_learning.models.RegisterStudentModel;
import com.fis.fis_remote_learning.utils.BaseUrl;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

import static com.fis.fis_remote_learning.utils.Utils.ShowLoader;
import static com.fis.fis_remote_learning.utils.Utils.ShowLongSnackBar;

public class RegisterStudent extends AppCompatActivity {

    private List<String> levels;
    private List<LevelsModel> LevelsArray;
    private ProgressBar progressBar;
    private MaterialSpinner LevelSpinner;
    private EditText Fname,Lname;
    private String SelectedGender = "none";
    private EditText DOB,MiddleName;
    private LoginDataModel dataModel;
    private View view;
    private AlertDialog progressDialog;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        progressDialog= ShowLoader(this);
        Paper.init(this);

        String parentData = Paper.book().read("ParentData").toString();
        dataModel = gson.fromJson(parentData, LoginDataModel.class);

        Button next = findViewById(R.id.Next);
        progressBar = findViewById(R.id.ProgressBar);
        LevelSpinner = findViewById(R.id.StudentPackage);
        DOB = findViewById(R.id.Register_Age);
        RadioGroup gender = findViewById(R.id.Register_Gender);
        Fname = findViewById(R.id.Register_Fname);
        Lname = findViewById(R.id.Register_Lname);
        view = findViewById(R.id.RegisterLayout);
        MiddleName = findViewById(R.id.Register_MiddleName);


        getLevels();

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                SelectedGender = radioButton.getText().toString();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePageOne();
            }
        });
    }

    private void getLevels() {
        progressBar.setVisibility(View.VISIBLE);
        LevelsArray = new ArrayList<>();
        levels = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUrl.getStudentLevels(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        try {
                            final LevelsResponseModel model = gson.fromJson(response.toString(), LevelsResponseModel.class);
                            LevelsArray = model.getClasses();
                            for(LevelsModel levelsModel: LevelsArray){
                                levels.add(levelsModel.getClassName());
                            }
                            LevelSpinner.setItems(levels);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                    message = error.toString();
                }
                ShowLongSnackBar(findViewById(R.id.RegisterLayout),message);
                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(RegisterStudent.this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    private void validatePageOne() {
        if(Fname.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.first_name));
            Fname.requestFocus();
        }
        else if(Lname.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.last_name));
            Lname.requestFocus();
        }
        else if(MiddleName.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.middle_name));
            MiddleName.requestFocus();
        }
        else if(DOB.getText().toString().isEmpty()){
            ShowSnackBar(getString(R.string.age));
            DOB.requestFocus();
        }
        else if(SelectedGender.equals("none")){
            ShowSnackBar(getString(R.string.gender));
        }else{
            registerStudent(Fname.getText().toString().trim(),MiddleName.getText().toString(),Lname.getText().toString(),DOB.getText().toString());
        }
    }

    private void registerStudent(final String fname, String middleName, final String lname, String date) {
        progressDialog.show();
        String selectedLevel = LevelSpinner.getText().toString();
        Integer ClassID = null;

        for(LevelsModel model: LevelsArray){
            if(model.getClassName().equals(selectedLevel)){
                ClassID = model.getClassID();
            }
        }
        RegisterStudentModel studentModel = new RegisterStudentModel(null,
                fname,middleName,lname,SelectedGender,ClassID,date,Integer.parseInt(dataModel.getParentID()));

        final Gson gson = new Gson();
        final String param = gson.toJson(studentModel);
        Log.e("RegisterStudentModel",param);

        try {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BaseUrl.getRegisterStudent(), new JSONObject(param),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            RegStudentRespModel respModel = gson.fromJson(response.toString(),RegStudentRespModel.class);
                            if(respModel.getStatus().getCode().equals("1")){
                                //ShowLongSnackBar(view,getString(R.string.student_registered));
                                Intent intent = new Intent(RegisterStudent.this,SelectPackage.class);
                                intent.putExtra("SUID",respModel.getStatus().getSuId().replace("SID",""));
                                intent.putExtra("SNames",fname+" "+lname);
                                startActivity(intent);
                                finish();
                            }else{
                                ShowLongSnackBar(view,getString(R.string.student_register_failed)+": "+respModel.getStatus().getMessage());
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
                    ShowLongSnackBar(view,message);
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(RegisterStudent.this);
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void ShowSnackBar(String field){
        Snackbar.make(findViewById(R.id.RegisterLayout),field+" "+getString(R.string.required),Snackbar.LENGTH_LONG).show();
    }
}
