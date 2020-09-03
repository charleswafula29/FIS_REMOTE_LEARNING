package com.fairmontsintenational.fis_remote_learning.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.fairmontsintenational.fis_remote_learning.FeeStatement;
import com.fairmontsintenational.fis_remote_learning.Login;
import com.fairmontsintenational.fis_remote_learning.R;
import com.fairmontsintenational.fis_remote_learning.Register;
import com.fairmontsintenational.fis_remote_learning.adapters.FeeInvoiceAdapter;
import com.fairmontsintenational.fis_remote_learning.models.FeesDataModel;
import com.fairmontsintenational.fis_remote_learning.models.InvoiceFee;
import com.fairmontsintenational.fis_remote_learning.models.StatusModel;
import com.fairmontsintenational.fis_remote_learning.models.StudentModel;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

import static com.fairmontsintenational.fis_remote_learning.utils.Utils.Mpesa_Popup;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.RegistrationSuccessPopup;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.ShowLoader;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.ShowLongSnackBar;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.formatNumber;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FeeInvoiceAdapter adapter;
    private List<InvoiceFee> feesClassList;
    private TextView balance, total_invoiced, total_paid;
    String names;
    private ConstraintLayout Layout;
    Button Mpesa, Statement;
    private Context mContext;
    private SwipeRefreshLayout refreshLayout;
    private Gson gson = new Gson();
    private TextView NotFound;
    private float total = 0;
    private float paid = 0;

    public FeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fees, container, false);
        Paper.init(mContext);
        String strtext = Paper.book().read("stdModel").toString();
        final StudentModel model = gson.fromJson(strtext,StudentModel.class);


        balance= root.findViewById(R.id.Fees_balance);
        total_invoiced= root.findViewById(R.id.Fees_total_invoiced);
        total_paid= root.findViewById(R.id.Fees_total_paid);
        Mpesa = root.findViewById(R.id.Fees_Mpesa);
        Statement = root.findViewById(R.id.Fees_Statement);
        refreshLayout = root.findViewById(R.id.Refresh);
        Layout = root.findViewById(R.id.root_view);
        NotFound = root.findViewById(R.id.NoRecordsFound);

        recyclerView= root.findViewById(R.id.fees_logs_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        fetchfeebalance(String.valueOf(model.getSid()));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchfeebalance(String.valueOf(model.getSid()));
            }
        });

        Statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FeeStatement.class);
                intent.putExtra("SID",model.getSUID());
                startActivity(intent);
            }
        });

        Mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mpesa_Popup(mContext,model.getStudentNames(),(int)(total-paid),model.getSid());
            }
        });

        return root;
    }

    private void fetchfeebalance(String sid) {
        total = 0;
        paid = 0;
        refreshLayout.setRefreshing(true);
        feesClassList = new ArrayList<>();
        String url = BaseUrl.getFeeStatement(sid);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {
                                refreshLayout.setRefreshing(false);

                                try {
                                    FeesDataModel model = gson.fromJson(response.toString(),FeesDataModel.class);
                                    feesClassList.addAll(model.getReport());

                                    if(feesClassList.size()==0){
                                        NotFound.setVisibility(View.VISIBLE);
                                    }else{
                                        NotFound.setVisibility(View.GONE);
                                    }
                                    for(InvoiceFee fee : model.getReport()){
                                        total = total+fee.getDebit();
                                        paid = paid+fee.getPayments();
                                    }
                                    balance.setText(getString(R.string.ksh)+" "+formatNumber(String.valueOf((total-paid))));
                                    total_invoiced.setText(getString(R.string.ksh)+" "+formatNumber(String.valueOf(total)));
                                    total_paid.setText(getString(R.string.ksh)+" "+formatNumber(String.valueOf(paid)));

                                    adapter = new FeeInvoiceAdapter(mContext, feesClassList);
                                    recyclerView.setAdapter(adapter);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        refreshLayout.setRefreshing(false);
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
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
