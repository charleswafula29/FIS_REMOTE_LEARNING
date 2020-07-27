package com.fairmontsintenational.fis_remote_learning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fairmontsintenational.fis_remote_learning.classes.Constants;
import com.fairmontsintenational.fis_remote_learning.models.StudentModel;
import com.fairmontsintenational.fis_remote_learning.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfiles extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<StudentModel> list;


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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentProfiles.this));

        Intent intent = getIntent();
        final String type = intent.getStringExtra("Type");

        assert type != null;
        if(type.equals(Constants.ACTIVE_STUDENTS.toString())){
            title.setText(getString(R.string.active_student_profiles));
            getActiveStudents();
        }
        else if (type.equals(Constants.INACTIVE_STUDENTS.toString())){
            title.setText(getString(R.string.in_active_student_profiles));
            getInactiveStudents();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(type.equals(Constants.ACTIVE_STUDENTS.toString())){
                    getActiveStudents();
                }
                else if (type.equals(Constants.INACTIVE_STUDENTS.toString())){
                    getInactiveStudents();
                }
            }
        });

    }

    private void getInactiveStudents() {
        swipeRefreshLayout.setRefreshing(true);
        list = new ArrayList<>();
        list.add(new StudentModel(null,"https://images.theconversation.com/files/138670/original/image-20160921-21723-zvi9hu.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=926&fit=clip",
                "Miles Matoke",
                "PP2",
                "miles@yopmail.com",
                "123456",
                "Under Review",
                Constants.NORMAL.toString()));

        list.add(new StudentModel(null,"https://scontent.fnbo5-1.fna.fbcdn.net/v/t1.0-9/105997343_4686941331331940_6178530885906463367_n.jpg?_nc_cat=103&_nc_sid=730e14&_nc_eui2=AeEQivP5Rb5U_7YrlSiPxtpN78mLnRIbm7bvyYudEhubtpm1OD7krSPsMGKcIrB-SbU2GK0FEDPF0ozeIQY2p9wN&_nc_ohc=HwEPRpC803IAX9jCtL5&_nc_pt=5&_nc_ht=scontent.fnbo5-1.fna&oh=dc9abc3b4554f43d1f1235de2ff7d4b5&oe=5F256F76",
                "Zuberi Jaylani",
                "PP2",
                "zubby@yopmail.com",
                "123456",
                "Under Review",
                Constants.NORMAL.toString()));

        InnerAdapter adapter = new InnerAdapter(list);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    private void getActiveStudents() {
        swipeRefreshLayout.setRefreshing(true);
        list = new ArrayList<>();
        list.add(new StudentModel(null,
                "https://webneel.com/daily/sites/default/files/images/daily/08-2018/12-portrait-photography-beautiful-kid-sam.jpg",
                "Jane Doe",
                "PP2",
                "rose@yopmail.com",
                "123456",
                "Verified",
                Constants.NORMAL.toString()));

        list.add(new StudentModel(null,
                "https://scontent.fnbo5-1.fna.fbcdn.net/v/t31.0-8/15025432_10208035684758749_3724918611520034123_o.jpg?_nc_cat=102&_nc_sid=09cbfe&_nc_eui2=AeEhWL0z9e-afmnQa-hR_IreTJNu8mChjIxMk27yYKGMjIrEDRL-KR-tbjOYm4_dokw2J6OmQtGNUDlTWYYhZM7a&_nc_ohc=jcqnLqob-jkAX-8-sxj&_nc_pt=5&_nc_ht=scontent.fnbo5-1.fna&oh=e4799b304647806498c1614dc745f5e3&oe=5F23DAAB",
                "Kaylor Moraa",
                "PP2",
                "Kaylor@yopmail.com",
                "123456",
                "Verified",
                Constants.NORMAL.toString()));

        InnerAdapter adapter = new InnerAdapter(list);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.ViewHolder>{
        private List<StudentModel> list;

        public InnerAdapter(List<StudentModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(StudentProfiles.this);
            View view = inflater.inflate(R.layout.students_vertical_list,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            StudentModel model = list.get(position);

            Glide.with(StudentProfiles.this)
                    .load(model.getImageUri())
                    .apply(new RequestOptions().placeholder(R.drawable.fis_user).error(R.drawable.fis_user))
                    .into(holder.studentImage);

            holder.StudentName.setText(Utils.convertCapitalText(model.getStudentNames()));
            holder.StudentLevel.setText(Utils.convertCapitalText(model.getLevel()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StudentProfiles.this, SingleStudentProfile.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            CircleImageView studentImage;
            TextView StudentName,StudentLevel;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                studentImage = itemView.findViewById(R.id.ChildImage);
                StudentName = itemView.findViewById(R.id.Student_Name);
                StudentLevel = itemView.findViewById(R.id.profile_studentGrade);
            }
        }
    }
}
