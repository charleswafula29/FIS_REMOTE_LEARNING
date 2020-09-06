package com.fairmontsintenational.fis_remote_learning;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.fairmontsintenational.fis_remote_learning.models.LoginModel;
import com.fairmontsintenational.fis_remote_learning.models.StudentModel;
import com.fairmontsintenational.fis_remote_learning.models.StudentsRespModel;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;
import com.fairmontsintenational.fis_remote_learning.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.Active;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.DEAC;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.HNS;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.IAAP;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.PSA;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.SAP;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.SCBP;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.DEAC_Popup;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.PSA_Popup;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.SAP_Popup;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.ShowLongSnackBar;

public class StudentProfiles extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ConstraintLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profiles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolBar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = findViewById(R.id.Title);
        swipeRefreshLayout = findViewById(R.id.Refresh);
        recyclerView = findViewById(R.id.Recycler);
        root = findViewById(R.id.root_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentProfiles.this));

        Paper.init(this);
        Intent intent = getIntent();
        final String type = intent.getStringExtra("Type").toString();
        //final List<StudentModel> Students = Paper.book().read("Data");

        if (type.equals(Constants.ACTIVE_STUDENTS.toString())) {
            title.setText(getString(R.string.active_student_profiles));
        } else if (type.equals(Constants.INACTIVE_STUDENTS.toString())) {
            title.setText(getString(R.string.in_active_student_profiles));
        }

        getStudents(type);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStudents(type);
            }
        });

    }

    private void getStudents(final String type) {
        swipeRefreshLayout.setRefreshing(true);
        final List<StudentModel> list = new ArrayList<>();

        String url = BaseUrl.getLogin(Paper.book().read("Phone").toString(), Paper.book().read("Pass").toString());
        Log.e("URL",url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            final LoginModel model = gson.fromJson(response.getJSONObject("Response").toString(), LoginModel.class);
                            if (model.getStatus().getCode().equals("0")) {
                                Snackbar.make(root, model.getStatus().getMessage(), Snackbar.LENGTH_LONG).show();
                            }else {
                                for (StudentsRespModel respModel : model.getData().getStudents()) {
                                    if (type.equals(Constants.ACTIVE_STUDENTS.toString())) {
                                        if (respModel.getStatus().equals(Active) || respModel.getStatus().equals(IAAP)) {
                                            list.add(new StudentModel(respModel.getSId(),
                                                    respModel.getName(), respModel.getSEX(), respModel.getCName(), respModel.getAdmno(),
                                                    respModel.getStatus(),
                                                    Constants.NORMAL.toString(),
                                                    Integer.valueOf(respModel.getBalances()),
                                                    respModel.getSUID()));
                                        }
                                    } else if (type.equals(Constants.INACTIVE_STUDENTS.toString())) {
                                        if(respModel.getStatus().equals(PSA) || respModel.getStatus().equals(HNS)
                                        || respModel.getStatus().equals(SAP) || respModel.getStatus().equals(SCBP)
                                                || respModel.getStatus().equals(DEAC)){
                                            list.add(new StudentModel(respModel.getSId(),
                                                    respModel.getName(), respModel.getSEX(), respModel.getCName(), respModel.getAdmno(),
                                                    respModel.getStatus(),
                                                    Constants.NORMAL.toString(),
                                                    Integer.valueOf(respModel.getBalances()),
                                                    respModel.getSUID()));
                                        }
                                    }
                                }

                                InnerAdapter adapter = new InnerAdapter(list);
                                recyclerView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            swipeRefreshLayout.setRefreshing(false);
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
                ShowLongSnackBar(root, message);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(StudentProfiles.this);
        queue.add(jsonObjectRequest);

    }

    private class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.ViewHolder> {
        private List<StudentModel> list;

        public InnerAdapter(List<StudentModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(StudentProfiles.this);
            View view = inflater.inflate(R.layout.students_vertical_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final StudentModel model = list.get(position);
            holder.StudentName.setText(Utils.convertCapitalText(model.getStudentNames()));
            holder.StudentLevel.setText(Utils.convertCapitalText(model.getClassName()));
            holder.profile_status.setText(model.getStatus());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StudentProfiles.this, SingleStudentProfile.class));
                }
            });

            try{

                holder.progressBar.setVisibility(View.VISIBLE);

                String Url = BaseUrl.getFetchStudentPic(model.getSid());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Bitmap bitmap = ConvertBase64(response.getString("ByteArray"));
                                    holder.StudentImage.setImageBitmap(bitmap);
                                    holder.progressBar.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    holder.progressBar.setVisibility(View.GONE);
                                    if (model.getGender().equals("Male")) {
                                        holder.StudentImage.setImageResource(R.drawable.boy_icon);
                                    } else {
                                        holder.StudentImage.setImageResource(R.drawable.girl_icon);
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.progressBar.setVisibility(View.GONE);
                        if (model.getGender().equals("Male")) {
                            holder.StudentImage.setImageResource(R.drawable.boy_icon);
                        } else {
                            holder.StudentImage.setImageResource(R.drawable.girl_icon);
                        }
                        Log.e("Error", error.toString());
                    }
                });
                RequestQueue queue = Volley.newRequestQueue(StudentProfiles.this);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjectRequest);

            }catch (Exception e){
                e.printStackTrace();
                if (model.getGender().equals("Male")) {
                    holder.StudentImage.setImageResource(R.drawable.boy_icon);
                } else {
                    holder.StudentImage.setImageResource(R.drawable.girl_icon);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String stdModel = gson.toJson(model);
                    Paper.book().write("stdModel", stdModel);

                    if (model.getStatus().equals(PSA)) {
                        AlertDialog dialog = PSA_Popup(StudentProfiles.this, model.getStudentNames());
                        dialog.show();
                    }
                    else if (model.getStatus().equals(HNS)) {
                        Intent intent = new Intent(StudentProfiles.this, SelectPackage.class);
                        intent.putExtra("SUID", String.valueOf(model.getSid()));
                        intent.putExtra("SNames", model.getStudentNames());
                        startActivity(intent);
                    }
                    else if (model.getStatus().equals(IAAP)) {
                        startActivity(new Intent(StudentProfiles.this, SingleStudentProfile.class));
                    }
                    else if (model.getStatus().equals(SAP)) {
                        AlertDialog dialog = SAP_Popup(StudentProfiles.this, model.getStudentNames(), model.getBalances(), model.getSid());
                        dialog.show();
                    }
                    else if (model.getStatus().equals(DEAC) || model.getStatus().equals(SCBP)) {
                        AlertDialog dialog = DEAC_Popup(StudentProfiles.this, model.getStudentNames());
                        dialog.show();
                    }
                    else if (model.getStatus().equals(Active)) {
                        startActivity(new Intent(StudentProfiles.this, SingleStudentProfile.class));
                    }
                }
            });

        }

        private Bitmap ConvertBase64(String base64String) {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView StudentImage;
            TextView StudentName, StudentLevel, profile_status;
            ProgressBar progressBar;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                StudentImage = itemView.findViewById(R.id.Image);
                StudentName = itemView.findViewById(R.id.Student_Name);
                profile_status = itemView.findViewById(R.id.profile_status);
                StudentLevel = itemView.findViewById(R.id.profile_studentGrade);
                progressBar = itemView.findViewById(R.id.Loader);
            }
        }
    }
}
