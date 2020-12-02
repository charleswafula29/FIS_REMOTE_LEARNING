package com.fis.fis_remote_learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

public class UserProfile extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolBar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


}
