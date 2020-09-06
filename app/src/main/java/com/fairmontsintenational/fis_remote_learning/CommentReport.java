package com.fairmontsintenational.fis_remote_learning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.fairmontsintenational.fis_remote_learning.models.ReportModel;
import com.fairmontsintenational.fis_remote_learning.models.StatusModel;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;
import com.fairmontsintenational.fis_remote_learning.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import static com.fairmontsintenational.fis_remote_learning.utils.Utils.RegistrationSuccessPopup;

public class CommentReport extends AppCompatActivity {
    private Gson gson = new Gson();
    private ReportModel model;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_report);

        Intent intent = getIntent();
        String ReportModelString = intent.getStringExtra("ReportModel");
        model = gson.fromJson(ReportModelString,ReportModel.class);

        dialog = Utils.ShowLoader(CommentReport.this);

        ((TextView)findViewById(R.id.StudentName)).setText(Utils.convertCapitalText(model.getName()));
        try {
            ((TextView)findViewById(R.id.Date)).setText(Utils.convertDate(model.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
            ((TextView)findViewById(R.id.Date)).setText(model.getDate().replace("T00:00:00",""));
        }
        ((TextView)findViewById(R.id.Teacher)).setText(Utils.convertCapitalText(model.getTeacher()));

        ((TextView)findViewById(R.id.DayEntry)).setText(model.getDayEntry());
        ((TextView)findViewById(R.id.TeacherComment)).setText(model.getTeacherComment());

        final EditText comment = findViewById(R.id.Comment_text);
        comment.setText(model.getParenyComment());

        ((Button)findViewById(R.id.Comment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment.getText().toString().isEmpty()){
                    comment.setError("Enter your comments first!");
                }else if(comment.getText().toString().equals(model.getParenyComment())){
                    comment.setError("No changes made to the previous comment!");
                }
                else{
                    PostComment(comment.getText().toString());
                }
            }
        });


    }

    private void PostComment(String comment) {
        dialog.show();
        String url = BaseUrl.getCommentStudentReports(model.getDiaryId(),Utils.getCurrentDate(),comment);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                dialog.dismiss();
                                Toast.makeText(CommentReport.this, "Comments updated!", Toast.LENGTH_LONG).show();
                                finish();
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
                        Utils.ShowLongSnackBar((findViewById(R.id.LayoutView)),message);
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(CommentReport.this);
        queue.add(jsonObjectRequest);
    }
}
