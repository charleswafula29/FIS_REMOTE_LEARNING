package com.fis.fis_remote_learning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.fis.fis_remote_learning.models.FeesDataModel;
import com.fis.fis_remote_learning.models.InvoiceFee;
import com.fis.fis_remote_learning.utils.BaseUrl;
import com.fis.fis_remote_learning.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.fis.fis_remote_learning.utils.Utils.ShowLongSnackBar;

public class FeeStatement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<InvoiceFee> List;
    private Gson gson = new Gson();
    private LinearLayout Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_statement);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String sid = intent.getStringExtra("SID");

        swipeRefreshLayout = findViewById(R.id.Refresh);
        recyclerView = findViewById(R.id.Recycler);
        Layout = findViewById(R.id.root_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchfeebalance(String.valueOf(sid));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchfeebalance(String.valueOf(sid));
            }
        });
    }

    private void fetchfeebalance(String sid) {
        swipeRefreshLayout.setRefreshing(true);
        List = new ArrayList<>();
        String url = BaseUrl.getFeeStatement(sid);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {
                                swipeRefreshLayout.setRefreshing(false);

                                try {
                                    FeesDataModel model = gson.fromJson(response.toString(),FeesDataModel.class);
                                    List.addAll(model.getReport());

                                    InnerAdapter adapter = new InnerAdapter(List);
                                    recyclerView.setAdapter(adapter);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
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
        RequestQueue queue = Volley.newRequestQueue(FeeStatement.this);
        queue.add(jsonObjectRequest);
    }



    private class InnerAdapter extends RecyclerView.Adapter<FeeStatement.InnerAdapter.ViewHolder>{
        List<InvoiceFee> list;

        public InnerAdapter(List<InvoiceFee> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(FeeStatement.this);
            View view = inflater.inflate(R.layout.statement_list,parent,false);
            return new ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            InvoiceFee fee = list.get(position);
            try {
                holder.StatementDate.setText(Utils.convertDate(fee.getTxnDate()));
            }
            catch (ParseException e) {
                holder.StatementDate.setText(fee.getTxnDate().replace("T00:00:00",""));
                e.printStackTrace();
            }
            holder.StatementBalance.setText(getString(R.string.ksh)+" "+Utils.formatNumber(String.valueOf(fee.getRunningBalance())));
            holder.StatemntDesc.setText(fee.getDescription());
            holder.StatementCredit.setText(getString(R.string.ksh)+" "+Utils.formatNumber(String.valueOf(fee.getPayments())));
            holder.StatementDebit.setText(getString(R.string.ksh)+" "+Utils.formatNumber(String.valueOf(fee.getDebit())));

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView StatementDate, StatemntDesc, StatementDebit, StatementCredit, StatementBalance;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                StatementDate = itemView.findViewById(R.id.sttDate);
                StatemntDesc  = itemView.findViewById(R.id.sttDesc);
                StatementDebit = itemView.findViewById(R.id.sttDebit);
                StatementCredit  = itemView.findViewById(R.id.sttCredit);
                StatementBalance  = itemView.findViewById(R.id.sttBalance);
            }
        }
    }
}
