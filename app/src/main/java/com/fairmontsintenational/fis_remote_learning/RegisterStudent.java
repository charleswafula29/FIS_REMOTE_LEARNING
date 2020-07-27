package com.fairmontsintenational.fis_remote_learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import params.com.stepview.StatusViewScroller;

public class RegisterStudent extends AppCompatActivity {

    private ConstraintLayout PageOne,PageTwo;
    private TextView Next,Back,Register;
    private StatusViewScroller statusViewScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolBar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PageOne = findViewById(R.id.PageOne);
        PageTwo = findViewById(R.id.PageTwo);
        Next = findViewById(R.id.Next);
        Back = findViewById(R.id.Back);
        Register = findViewById(R.id.Register);
        statusViewScroller = findViewById(R.id.StepView);
        MaterialSpinner LevelSpinner = findViewById(R.id.StudentLevel_Spinner);

        List<String> levels = new ArrayList<>();
        levels.add("Kindergarten");
        levels.add("Pre-primary 1");
        levels.add("Pre-primary 2");
        levels.add("Grade 1-2");
        levels.add("Grade 2-4");
        LevelSpinner.setItems(levels);


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
        finish();
    }

    private void validatePageOne() {
    }
}
