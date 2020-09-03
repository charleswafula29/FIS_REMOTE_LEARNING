package com.fairmontsintenational.fis_remote_learning.fragments;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fairmontsintenational.fis_remote_learning.Homepage;
import com.fairmontsintenational.fis_remote_learning.Login;
import com.fairmontsintenational.fis_remote_learning.R;
import com.fairmontsintenational.fis_remote_learning.StudentProfiles;
import com.fairmontsintenational.fis_remote_learning.adapters.StudentsAdapter;
import com.fairmontsintenational.fis_remote_learning.classes.Constants;
import com.fairmontsintenational.fis_remote_learning.classes.Sessions;
import com.fairmontsintenational.fis_remote_learning.models.LoginDataModel;
import com.fairmontsintenational.fis_remote_learning.models.LoginModel;
import com.fairmontsintenational.fis_remote_learning.models.Profile;
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
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.IAAP;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.ShowLongSnackBar;
import static com.fairmontsintenational.fis_remote_learning.utils.Utils.getTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentHomeFragment extends Fragment {

    private Context context;
    private RecyclerView ActiveRecycler, InactiveRecycler;
    private List<StudentModel> ActiveStudents, InactiveStudents;
    private StudentsAdapter ActiveAdapter, InactiveAdapter;
    private LoginDataModel dataModel;
    private View root;
    private ProgressBar progressBar;
    private CircleImageView profilePic;
    private Profile profile = new Profile();
    private Gson gson = new Gson();

    public ParentHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_parent_home, container, false);

        Paper.init(context);
        String parentData = Paper.book().read("ParentData").toString();
        final Gson gson = new Gson();
        Log.d("HERE", parentData);
        dataModel = gson.fromJson(parentData, LoginDataModel.class);

        TextView names = root.findViewById(R.id.UserNames);
        TextView time = root.findViewById(R.id.Time);
        LinearLayout background = root.findViewById(R.id.linearLayout3);
        names.setText((dataModel.getFirstName()==null)?"Not set":Utils.pickFirstName(dataModel.getFirstName()).toUpperCase());

        int hours = getTime();
        if (hours>=1 && hours <= 12) {
            time.setText(getString(R.string.good_morning));
            background.setBackground(ContextCompat.getDrawable(context, R.drawable.morning));
        } else if (hours>=13 && hours <= 17) {
            time.setText(getString(R.string.good_afternoon));
            background.setBackground(ContextCompat.getDrawable(context, R.drawable.morning));
        }
        else if(hours >= 18 && hours <= 24){
            time.setText(getString(R.string.good_evening));
            background.setBackground(ContextCompat.getDrawable(context, R.drawable.evening));
        }

        profilePic = root.findViewById(R.id.DefaultPagePic);
        progressBar = root.findViewById(R.id.ProgressBar);
        ActiveRecycler = root.findViewById(R.id.ActiveRecycler);
        InactiveRecycler = root.findViewById(R.id.InactiveRecycler);
        ActiveRecycler.setHasFixedSize(true);
        ActiveRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        InactiveRecycler.setHasFixedSize(true);
        InactiveRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        loadProfileDefault(dataModel.getParentID());

        ((TextView) root.findViewById(R.id.activeTitle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentProfiles.class);
                intent.putExtra("Type", Constants.ACTIVE_STUDENTS.toString());
                //Paper.book().write("Data",ActiveStudents);
                startActivity(intent);
            }
        });

        ((TextView) root.findViewById(R.id.InactiveTitle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentProfiles.class);
                intent.putExtra("Type", Constants.INACTIVE_STUDENTS.toString());
                //Paper.book().write("Data",InactiveStudents);
                startActivity(intent);
            }
        });

        return root;
    }

    private void loadProfileDefault(String parentID) {
        progressBar.setVisibility(View.VISIBLE);

        String Url = BaseUrl.getParentProfPic(parentID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getString("Check").equals("Failed")){
                                Bitmap bitmap = ConvertBase64(response.getString("Check"));
                                profilePic.setImageBitmap(bitmap);
                                progressBar.setVisibility(View.GONE);
                            }else{
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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

    private void getProfiles() {
        ActiveStudents = new ArrayList<>();
        InactiveStudents = new ArrayList<>();
        final AlertDialog dialog = Utils.ShowLoader(context);
        dialog.show();
        String url = BaseUrl.getLogin(Paper.book().read("Phone").toString(),Paper.book().read("Pass").toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            final LoginModel model = gson.fromJson(response.getJSONObject("Response").toString(), LoginModel.class);
                            if(model.getStatus().getCode().equals("0")){
                                progressBar.setVisibility(View.GONE);
                                Snackbar.make(root,model.getStatus().getMessage(),Snackbar.LENGTH_LONG).show();
                            }else{
                                for(StudentsRespModel respModel: model.getData().getStudents()){
                                    if(respModel.getStatus().equals(Active) || respModel.getStatus().equals(IAAP)){
                                        ActiveStudents.add(new StudentModel(respModel.getSId(),
                                                respModel.getName(),respModel.getSEX(),respModel.getCName(),respModel.getAdmno(),
                                                respModel.getStatus(),
                                                Constants.NORMAL.toString(),
                                                Integer.valueOf(respModel.getBalances()),
                                                respModel.getSUID()));
                                    }else{
                                        InactiveStudents.add(new StudentModel(respModel.getSId(),
                                                respModel.getName(),respModel.getSEX(),respModel.getCName(),respModel.getAdmno(),
                                                respModel.getStatus(),
                                                Constants.NORMAL.toString(),
                                                Integer.valueOf(respModel.getBalances()),
                                                respModel.getSUID()));
                                    }
                                }

                                ActiveStudents.add(new StudentModel(null, null, null, null, null, null, Constants.ADD.toString(),null,null));

                                ActiveAdapter = new StudentsAdapter(ActiveStudents, context);
                                ActiveRecycler.setAdapter(ActiveAdapter);

                                InactiveAdapter = new StudentsAdapter(InactiveStudents, context);
                                InactiveRecycler.setAdapter(InactiveAdapter);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                    message = error.toString();
                }
                ShowLongSnackBar(root,message);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);

    }

    private Bitmap ConvertBase64(String base64String){
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfiles();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }
}
