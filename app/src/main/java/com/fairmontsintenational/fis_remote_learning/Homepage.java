package com.fairmontsintenational.fis_remote_learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fairmontsintenational.fis_remote_learning.adapters.StudentsAdapter;
import com.fairmontsintenational.fis_remote_learning.classes.Constants;
import com.fairmontsintenational.fis_remote_learning.models.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {

    RecyclerView ActiveRecycler,InactiveRecycler;
    List<StudentModel> ActiveStudents,InactiveStudents;
    StudentsAdapter ActiveAdapter,InactiveAdapter;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        ActiveRecycler = findViewById(R.id.ActiveRecycler);
        InactiveRecycler = findViewById(R.id.InactiveRecycler);
        ActiveRecycler.setHasFixedSize(true);
        ActiveRecycler.setLayoutManager(new LinearLayoutManager(Homepage.this,RecyclerView.HORIZONTAL,false));

        InactiveRecycler.setHasFixedSize(true);
        InactiveRecycler.setLayoutManager(new LinearLayoutManager(Homepage.this,RecyclerView.HORIZONTAL,false));

        getProfiles();

        ((TextView) findViewById(R.id.activeTitle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((TextView) findViewById(R.id.InactiveTitle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void getProfiles() {
        ActiveStudents = new ArrayList<>();
        InactiveStudents = new ArrayList<>();

        ActiveStudents.add(new StudentModel(null,
                "https://webneel.com/daily/sites/default/files/images/daily/08-2018/12-portrait-photography-beautiful-kid-sam.jpg",
                "Ruth Kabura",
                "PP2",
                "ruth@yopmail.com",
                "123456",
                "Verified",
                Constants.NORMAL.toString()));

        ActiveStudents.add(new StudentModel(null,
                "https://scontent.fnbo5-1.fna.fbcdn.net/v/t31.0-8/15025432_10208035684758749_3724918611520034123_o.jpg?_nc_cat=102&_nc_sid=09cbfe&_nc_eui2=AeEhWL0z9e-afmnQa-hR_IreTJNu8mChjIxMk27yYKGMjIrEDRL-KR-tbjOYm4_dokw2J6OmQtGNUDlTWYYhZM7a&_nc_ohc=jcqnLqob-jkAX-8-sxj&_nc_pt=5&_nc_ht=scontent.fnbo5-1.fna&oh=e4799b304647806498c1614dc745f5e3&oe=5F23DAAB",
                "Kaylor Moraa",
                "PP2",
                "Kaylor@yopmail.com",
                "123456",
                "Verified",
                Constants.NORMAL.toString()));

        //ADD button
        ActiveStudents.add(new StudentModel(null,"","","","","","",Constants.ADD.toString()));

        //Inactive ones

        InactiveStudents.add(new StudentModel(null,"https://images.theconversation.com/files/138670/original/image-20160921-21723-zvi9hu.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=926&fit=clip",
                "Miles Matoke",
                "PP2",
                "miles@yopmail.com",
                "123456",
                "Under Review",
                Constants.NORMAL.toString()));

        InactiveStudents.add(new StudentModel(null,"https://scontent.fnbo5-1.fna.fbcdn.net/v/t1.0-9/105997343_4686941331331940_6178530885906463367_n.jpg?_nc_cat=103&_nc_sid=730e14&_nc_eui2=AeEQivP5Rb5U_7YrlSiPxtpN78mLnRIbm7bvyYudEhubtpm1OD7krSPsMGKcIrB-SbU2GK0FEDPF0ozeIQY2p9wN&_nc_ohc=HwEPRpC803IAX9jCtL5&_nc_pt=5&_nc_ht=scontent.fnbo5-1.fna&oh=dc9abc3b4554f43d1f1235de2ff7d4b5&oe=5F256F76",
                "Zuberi Jaylani",
                "PP2",
                "zubby@yopmail.com",
                "123456",
                "Under Review",
                Constants.NORMAL.toString()));

        ActiveAdapter = new StudentsAdapter(ActiveStudents,Homepage.this);
        ActiveRecycler.setAdapter(ActiveAdapter);

        InactiveAdapter = new StudentsAdapter(InactiveStudents,Homepage.this);
        InactiveRecycler.setAdapter(InactiveAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfiles();
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.click_back_again), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
