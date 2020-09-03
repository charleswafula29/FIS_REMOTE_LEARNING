package com.fairmontsintenational.fis_remote_learning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.fairmontsintenational.fis_remote_learning.utils.Utils.Mpesa_Popup;

public class StudentRegistrationComplete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration_complete);

        final Intent intent = getIntent();
        final String total = intent.getStringExtra("Total").toString();
        final String names = intent.getStringExtra("Names");
        final String SID = intent.getStringExtra("SID").toString();

        TextView Balance = findViewById(R.id.Balance);
        Button Skip = findViewById(R.id.Skip);
        Button Pay = findViewById(R.id.Pay);

        Balance.append(": KSH "+total);
        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mpesa_Popup(StudentRegistrationComplete.this,
                        names,Integer.parseInt(total),Integer.parseInt(SID));
            }
        });


        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
