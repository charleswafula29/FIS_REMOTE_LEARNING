package com.fis.fis_remote_learning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.fis.fis_remote_learning.models.ChargeModel;
import com.fis.fis_remote_learning.models.ChargeStudentModel;
import com.fis.fis_remote_learning.models.ChargesRespModel;
import com.fis.fis_remote_learning.models.PostChargeStudent;
import com.fis.fis_remote_learning.models.StatusModel;
import com.fis.fis_remote_learning.utils.BaseUrl;
import com.fis.fis_remote_learning.utils.Utils;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.fis.fis_remote_learning.utils.BaseUrl.OnlinePackage;
import static com.fis.fis_remote_learning.utils.BaseUrl.RegistrationCharges;
import static com.fis.fis_remote_learning.utils.Utils.ShowLoader;
import static com.fis.fis_remote_learning.utils.Utils.ShowLongSnackBar;
import static com.fis.fis_remote_learning.utils.Utils.convertCapitalText;

public class SelectPackage extends AppCompatActivity {
    private Gson gson = new Gson();
    private ProgressBar bar1,bar2;
    private List<ChargeModel> RegistrationChargesList,OnlinePackagesList;
    private MaterialSpinner RegSpinner,PkgSpinner;
    private ConstraintLayout Layout;
    private TextView Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package);

        Intent intent = getIntent();
        final String names = intent.getStringExtra("SNames").toString();
        final String SUID = intent.getStringExtra("SUID").toString();
        ((TextView) findViewById(R.id.Note)).setText(getString(R.string.choose_subsc).replace("std_name", convertCapitalText(names)));
        bar1 = findViewById(R.id.ProgressBar);
        bar2 = findViewById(R.id.ProgressBar2);
        RegSpinner = findViewById(R.id.RegistrationPackage);
        PkgSpinner = findViewById(R.id.StudentPackage);
        Layout = findViewById(R.id.layout);
        Total = findViewById(R.id.Total);

        getPaymentPackages();

        RegSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                calculateSum();
            }
        });

        PkgSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                calculateSum();
            }
        });

        ((Button) findViewById(R.id.Pay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TriggerPayment(SUID,names);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void calculateSum() {
        String RegType = RegSpinner.getText().toString();
        String Pkg = PkgSpinner.getText().toString();
        Integer sum = 0;
        if(!RegType.isEmpty()){
            for(ChargeModel model: RegistrationChargesList){
                if(Utils.convertCapitalText(model.getPackageName()).equals(RegType)){
                    sum = sum+model.getCharges();
                }
            }
        }
        if(!Pkg.isEmpty()){
            for(ChargeModel model: OnlinePackagesList){
                if(Utils.convertCapitalText(model.getPackageName()).equals(Pkg)){
                    sum = sum+model.getCharges();
                }
            }
        }

        Total.setText(getString(R.string.total)+" "+sum);
    }

    private void TriggerPayment(final String suid, final String names) {
        final AlertDialog dialog = ShowLoader(SelectPackage.this);
        dialog.show();
        String RegType = RegSpinner.getText().toString();
        String Pkg = PkgSpinner.getText().toString();
        List<ChargeStudentModel> charges = new ArrayList<>();

        if(!RegType.isEmpty()){
            for(ChargeModel model: RegistrationChargesList){
                if(Utils.convertCapitalText(model.getPackageName()).equals(RegType)){
                    charges.add(new ChargeStudentModel(model.getPackageId(),model.getPackageType(),model.getPackageName(),1,model.getCharges()));
                }
            }
        }
        if(!Pkg.isEmpty()){
            for(ChargeModel model: OnlinePackagesList){
                if(Utils.convertCapitalText(model.getPackageName()).equals(Pkg)){
                    charges.add(new ChargeStudentModel(model.getPackageId(),model.getPackageType(),model.getPackageName(),1,model.getCharges()));
                }
            }
        }

        PostChargeStudent postChargeStudent = new PostChargeStudent(suid,charges);
        String model = gson.toJson(postChargeStudent);
        Log.e("ADD_CHARGE",model);

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BaseUrl.getChargeStudent(), new JSONObject(model),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("RESPONSE",response.toString());
                            try {
                                StatusModel statusModel = gson.fromJson(response.getJSONObject("status").toString(),StatusModel.class);
                                Log.e("RESPONSE_MODEL",statusModel.toString());
                                if(statusModel.getCode().equals("1")){
                                    Intent intent = new Intent(SelectPackage.this, StudentRegistrationComplete.class);
                                    intent.putExtra("Names",names);
                                    intent.putExtra("Total",Total.getText().toString().replace("Total : KSH ","").trim());
                                    intent.putExtra("SID",suid.replace("SID",""));
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Utils.ShowLongSnackBar(Layout,statusModel.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
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
                        message = error.toString();
                    }
                    ShowLongSnackBar(Layout,message);
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(SelectPackage.this);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getPaymentPackages() {
        RegistrationChargesList = new ArrayList<>();
        OnlinePackagesList = new ArrayList<>();
        final List<String> RegCharges = new ArrayList<>();
        final List<String> Pkgs = new ArrayList<>();
        bar1.setVisibility(View.VISIBLE);
        bar2.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUrl.getStudentCharges(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ChargesRespModel model = gson.fromJson(response.toString(),ChargesRespModel.class);
                        for(ChargeModel chargeModel: model.getCharges()){
                            if(chargeModel.getPackageType().equals(RegistrationCharges)){
                                RegistrationChargesList.add(chargeModel);
                            }else if(chargeModel.getPackageType().equals(OnlinePackage)){
                                OnlinePackagesList.add(chargeModel);
                            }
                        }

                        for(ChargeModel chargeModel: RegistrationChargesList){
                            RegCharges.add(convertCapitalText(chargeModel.getPackageName()));
                        }
                        RegSpinner.setItems(RegCharges);

                        for(ChargeModel chargeModel: OnlinePackagesList){
                            Pkgs.add(convertCapitalText(chargeModel.getPackageName()));
                        }
                        PkgSpinner.setItems(Pkgs);

                        bar1.setVisibility(View.GONE);
                        bar2.setVisibility(View.GONE);
                        calculateSum();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(SelectPackage.this);
        queue.add(jsonObjectRequest);
    }


}
