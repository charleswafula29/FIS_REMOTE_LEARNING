package com.fairmontsintenational.fis_remote_learning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Introduction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        ((Button)findViewById(R.id.Register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Introduction.this,Register.class));
                finish();
            }
        });

        ((ConstraintLayout)findViewById(R.id.Login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Introduction.this,Login.class));
                finish();
            }
        });

//        addSlide(AppIntroFragment.newInstance("Welcome To FIS Remote Learning",
//                "We have introduced our remote learning classes for all our school sections. This service is available to all our students together with other students from other schools in Kenya. We have competent teachers who underwent training and are able to conduct the Virtual Learning to our students.",
//                R.drawable.kid_learning,
//                Color.parseColor("#FFFFFF"),
//                Color.parseColor("#FFFFFF"),
//                Color.parseColor("#FFFFFF"),
//                R.font.avenir_regular,
//                R.font.avenirltstd_light,
//                R.drawable.bachground_gradient));

//        addSlide(AppIntroFragment.newInstance("Manage your child(ren)'s profile",
//                "Through this platform, you will be able to create profiles and keep track of your child's progress with us. You will also be able to track your monthly payments easily.",
//                R.drawable.girl_child,
//                Color.parseColor("#FFFFFF"),
//                Color.parseColor("#FFFFFF"),
//                Color.parseColor("#FFFFFF"),
//                R.font.avenir_regular,
//                R.font.avenirltstd_light,
//                R.drawable.bachground_gradient));

//        addSlide(AppIntroFragment.newInstance("Let's Get Started",
//                "Let's create an account for you! Don't worry, you can also create and account for your spouse (this is optional). Click the button on the bottom right to proceed.",
//                R.drawable.new_online_classes,
//                Color.parseColor("#FFFFFF"),
//                Color.parseColor("#FFFFFF"),
//                Color.parseColor("#FFFFFF"),
//                R.font.avenir_regular,
//                R.font.avenirltstd_light,
//                R.drawable.bachground_gradient));
//
//        setTransformer(new AppIntroPageTransformerType.Parallax(
//                1.0, -1.0, 2.0));
//
//        isColorTransitionsEnabled();
    }

}
