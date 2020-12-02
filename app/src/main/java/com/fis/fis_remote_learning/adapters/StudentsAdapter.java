package com.fis.fis_remote_learning.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fis.fis_remote_learning.R;
import com.fis.fis_remote_learning.RegisterStudent;
import com.fis.fis_remote_learning.SelectPackage;
import com.fis.fis_remote_learning.SingleStudentProfile;
import com.fis.fis_remote_learning.classes.Constants;
import com.fis.fis_remote_learning.models.StudentModel;
import com.fis.fis_remote_learning.utils.BaseUrl;
import com.fis.fis_remote_learning.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import io.paperdb.Paper;

import static com.fis.fis_remote_learning.utils.BaseUrl.Active;
import static com.fis.fis_remote_learning.utils.BaseUrl.DEAC;
import static com.fis.fis_remote_learning.utils.BaseUrl.HNS;
import static com.fis.fis_remote_learning.utils.BaseUrl.IAAP;
import static com.fis.fis_remote_learning.utils.BaseUrl.PSA;
import static com.fis.fis_remote_learning.utils.BaseUrl.SAP;
import static com.fis.fis_remote_learning.utils.BaseUrl.SCBP;
import static com.fis.fis_remote_learning.utils.Utils.DEAC_Popup;
import static com.fis.fis_remote_learning.utils.Utils.PSA_Popup;
import static com.fis.fis_remote_learning.utils.Utils.SAP_Popup;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder>{
    private List<StudentModel> list;
    private Context context;

    public StudentsAdapter(List<StudentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final StudentModel model = list.get(position);

        if(model.getType().equals(Constants.ADD.toString())){
            holder.StudentImage.setImageResource(R.drawable.ic_add_user);
            holder.StudentImage.setBackground(context.getDrawable(R.drawable.yellow_circle));
            holder.StudentName.setText(context.getString(R.string.add_profile));
        }else{

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
                                    if(model.getGender().equals("Male")){
                                        holder.StudentImage.setImageResource(R.drawable.boy_icon);
                                    }else{
                                        holder.StudentImage.setImageResource(R.drawable.girl_icon);
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.progressBar.setVisibility(View.GONE);
                        if(model.getGender().equals("Male")){
                            holder.StudentImage.setImageResource(R.drawable.boy_icon);
                        }else{
                            holder.StudentImage.setImageResource(R.drawable.girl_icon);
                        }
                        Log.e("Error", error.toString());
                    }
                });
                RequestQueue queue = Volley.newRequestQueue(context);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjectRequest);

            }catch (Exception e){
                e.printStackTrace();
                if(model.getGender().equals("Male")){
                    holder.StudentImage.setImageResource(R.drawable.boy_icon);
                }else{
                    holder.StudentImage.setImageResource(R.drawable.girl_icon);
                }
            }

            holder.StudentName.setText(Utils.pickFirstName(Utils.convertCapitalText(model.getStudentNames())));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String stdModel = gson.toJson(model);
                Paper.book().write("stdModel",stdModel);

                if(model.getType().equals(Constants.ADD.toString())){
                    context.startActivity(new Intent(context, RegisterStudent.class));
                }else{
                    if(model.getStatus().equals(PSA)){
                        AlertDialog dialog = PSA_Popup(context,model.getStudentNames());
                        dialog.show();
                    }
                    else if(model.getStatus().equals(HNS)){
                        Intent intent = new Intent(context, SelectPackage.class);
                        intent.putExtra("SUID",String.valueOf(model.getSid()));
                        intent.putExtra("SNames",model.getStudentNames());
                        context.startActivity(intent);
                    }
                    else if(model.getStatus().equals(IAAP)){
                        context.startActivity(new Intent(context, SingleStudentProfile.class));
                    }
                    else if(model.getStatus().equals(SAP)){
                        AlertDialog dialog = SAP_Popup(context,model.getStudentNames(),model.getBalances(),model.getSid());
                        dialog.show();
                    }
                    else if(model.getStatus().equals(DEAC) ||model.getStatus().equals(SCBP)){
                        AlertDialog dialog = DEAC_Popup(context,model.getStudentNames());
                        dialog.show();
                    }
                    else if(model.getStatus().equals(Active)){
                        context.startActivity(new Intent(context, SingleStudentProfile.class));
                    }
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView StudentImage;
        TextView StudentName;
        ProgressBar progressBar;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            StudentImage = itemView.findViewById(R.id.StudentImage);
            StudentName = itemView.findViewById(R.id.StudentName);
            progressBar = itemView.findViewById(R.id.Loader);

        }
    }
}
