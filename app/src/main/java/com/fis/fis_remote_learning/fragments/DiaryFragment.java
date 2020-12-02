package com.fis.fis_remote_learning.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.fis.fis_remote_learning.CommentReport;
import com.fis.fis_remote_learning.R;
import com.fis.fis_remote_learning.models.ReportModel;
import com.fis.fis_remote_learning.models.ReportRespModel;
import com.fis.fis_remote_learning.models.StudentModel;
import com.fis.fis_remote_learning.utils.BaseUrl;
import com.fis.fis_remote_learning.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.paperdb.Paper;

import static com.fis.fis_remote_learning.utils.Utils.convertDate;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
public class DiaryFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LottieAnimationView NotFound;
    private TextView NotFoundText;
    private Context context;
    private Gson gson = new Gson();
    private StudentModel model;
    private InnerAdapter adapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public DiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_diary, container, false);

        Paper.init(context);
        String strtext = Paper.book().read("stdModel").toString();
        model = gson.fromJson(strtext, StudentModel.class);

        swipeRefreshLayout = root.findViewById(R.id.Refresh);
        recyclerView = root.findViewById(R.id.Recycler);
        NotFound = root.findViewById(R.id.imageView3);
        NotFoundText = root.findViewById(R.id.NoText);
        EditText SearchField  = root.findViewById(R.id.SearchField);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fetchReports(model.getSid());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchReports(model.getSid());
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchReports(model.getSid());
    }

    private void fetchReports(Integer sid) {
        String url = BaseUrl.getStudentReports(sid);
        swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefreshLayout.setRefreshing(false);
                        ReportRespModel reportRespModel = gson.fromJson(response.toString(),ReportRespModel.class);
                        List<ReportModel> list = new ArrayList<>(reportRespModel.getReport());

                        if(list.size()==0){
                            NotFound.setVisibility(View.VISIBLE);
                            NotFoundText.setVisibility(View.VISIBLE);
                        }else {
                            NotFound.setVisibility(View.GONE);
                            NotFoundText.setVisibility(View.GONE);
                        }

                        adapter = new InnerAdapter(list);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                NotFound.setVisibility(View.VISIBLE);
                NotFoundText.setVisibility(View.VISIBLE);
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
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                Log.e("Error", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);


    }

    private class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.viewHolder> implements Filterable{
        private List<ReportModel> list;
        private List<ReportModel> All_list;

        InnerAdapter(List<ReportModel> list) {
            this.list = list;
            this.All_list = new ArrayList<>(list);
        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new viewHolder(inflater.inflate(R.layout.reports_list,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            final ReportModel model = list.get(position);
            try {
                holder.Date.setText(context.getString(R.string.date_paid)+" "+convertDate(model.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(model.getParenyComment() == null){
                holder.Status.setText(context.getString(R.string.not_commented));
            }else{
                holder.Status.setText(context.getString(R.string.commented));
            }
            holder.PreviewTitle.setText(Utils.pickPreviewText(model.getDayEntry()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentReport.class);
                    String modelString = gson.toJson(model);
                    intent.putExtra("ReportModel",modelString);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ReportModel> filteredList = new ArrayList<>();
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(All_list);
                } else {
                    for (ReportModel activity : All_list) {
                        try {
                            if (Utils.convertDate(activity.getDate()).contains(constraint.toString()) ||
                                    activity.getDayEntry().contains(constraint.toString())
                            || context.getString(R.string.commented).contains(constraint.toString())
                            || context.getString(R.string.not_commented).contains(constraint.toString())) {
                                filteredList.add(activity);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list.clear();
                list.addAll((Collection<? extends ReportModel>) results.values);
                notifyDataSetChanged();
            }
        };


        private class viewHolder extends RecyclerView.ViewHolder{

            TextView Date,PreviewTitle,Status;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                Date = itemView.findViewById(R.id.Date);
                PreviewTitle = itemView.findViewById(R.id.PreviewTitle);
                Status = itemView.findViewById(R.id.Status);
            }
        }
    }
}
