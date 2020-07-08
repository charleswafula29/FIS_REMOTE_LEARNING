package com.fairmontsintenational.fis_remote_learning.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fairmontsintenational.fis_remote_learning.R;
import com.fairmontsintenational.fis_remote_learning.SingleStudentProfile;
import com.fairmontsintenational.fis_remote_learning.classes.Constants;
import com.fairmontsintenational.fis_remote_learning.fragments.StudentProfileFragment;
import com.fairmontsintenational.fis_remote_learning.models.StudentModel;
import com.fairmontsintenational.fis_remote_learning.utils.Utils;

import java.util.List;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StudentModel model = list.get(position);

        if(model.getType().equals(Constants.ADD.toString())){
            holder.StudentImage.setImageResource(R.drawable.ic_add_user);
            holder.StudentImage.setBackground(context.getDrawable(R.drawable.yellow_circle));
            holder.StudentName.setText(context.getString(R.string.add_profile));
        }else{
            Glide.with(context)
                    .load(model.getImageUri())
                    .apply(new RequestOptions().placeholder(R.drawable.fis_user).error(R.drawable.fis_user))
                    .into(holder.StudentImage);

            holder.StudentName.setText(Utils.pickFirstName(model.getStudentNames()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("StdNames", model.getStudentNames());
//                bundle.putString("StdEmail", model.getEmail());
//                bundle.putString("StdEmailPass", model.getEmailpassword());
//                bundle.putString("StdStatus", model.getStatus());
//
//                StudentProfileFragment fragobj = new StudentProfileFragment();
//                fragobj.setArguments(bundle);

                if(model.getType().equals(Constants.ADD.toString())){
                    Toast.makeText(context, "Add new profile", Toast.LENGTH_SHORT).show();
                }else{
                    context.startActivity(new Intent(context, SingleStudentProfile.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView StudentImage;
        TextView StudentName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            StudentImage = itemView.findViewById(R.id.StudentImage);
            StudentName = itemView.findViewById(R.id.StudentName);

        }
    }
}
