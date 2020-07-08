package com.fairmontsintenational.fis_remote_learning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import params.com.stepview.StatusViewScroller;

public class Register extends AppCompatActivity {

    ScrollView PageOne,PageTwo;
    TextView Next,Back,Register;
    StatusViewScroller statusViewScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        PageOne = findViewById(R.id.PageOne);
        PageTwo = findViewById(R.id.PageTwo);
        Next = findViewById(R.id.Next);
        Back = findViewById(R.id.Back);
        Register = findViewById(R.id.Register);
        statusViewScroller = findViewById(R.id.StepView);

        ((LinearLayout) findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePageOne();
                PageOne.setVisibility(View.GONE);
                PageTwo.setVisibility(View.VISIBLE);
                Next.setVisibility(View.GONE);
                Back.setVisibility(View.VISIBLE);
                Register.setVisibility(View.VISIBLE);
                statusViewScroller.getStatusView().setCurrentCount(2);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageOne.setVisibility(View.VISIBLE);
                PageTwo.setVisibility(View.GONE);
                Next.setVisibility(View.VISIBLE);
                Back.setVisibility(View.GONE);
                Register.setVisibility(View.GONE);
                statusViewScroller.getStatusView().setCurrentCount(1);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePageTwo();
            }
        });


    }

    private void validatePageTwo() {
        startActivity(new Intent(Register.this,Homepage.class));
        finish();
    }

    private void validatePageOne() {
    }
}
