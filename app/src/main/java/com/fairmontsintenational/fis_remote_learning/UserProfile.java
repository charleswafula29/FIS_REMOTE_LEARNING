package com.fairmontsintenational.fis_remote_learning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    TextView ParentName,ParentFullNames, ParentPhone,ResetPassword;
    String phone_no;
    CircleImageView profilePic;
    ImageView ChoosePhoto;
    ProgressBar progressBar;
    private static final int REQUEST_IMAGE = 100;

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

        ParentName = findViewById(R.id.Fname);
        ParentPhone = findViewById(R.id.PhoneNo);
        ParentFullNames = findViewById(R.id.AccountProfile_names);
        profilePic = findViewById(R.id.AccountProfile_image);
        ChoosePhoto = findViewById(R.id.ChoosePhoto);
        progressBar = findViewById(R.id.ProgressBar);
        ResetPassword = findViewById(R.id.ResetPassword);

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomPopupActivity.ChangeUserPasswordBottomSheet(UserProfile.this,phone_no);
            }
        });

        ChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerSelect();
            }
        });

        BottomPopupActivity.clearCache(UserProfile.this);

    }

    private void triggerSelect() {
        Dexter.withActivity(UserProfile.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        BottomPopupActivity.showImagePickerOptions(UserProfile.this, new BottomPopupActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(UserProfile.this, BottomPopupActivity.class);
        intent.putExtra(BottomPopupActivity.INTENT_IMAGE_PICKER_OPTION, BottomPopupActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(BottomPopupActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(BottomPopupActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(BottomPopupActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(BottomPopupActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(BottomPopupActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(BottomPopupActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(UserProfile.this, BottomPopupActivity.class);
        intent.putExtra(BottomPopupActivity.INTENT_IMAGE_PICKER_OPTION, BottomPopupActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(BottomPopupActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(BottomPopupActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(BottomPopupActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // loading profile image from local cache
                    loadProfile(uri.toString(), bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UserProfile.this, getString(R.string.failed_picture), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void loadProfile(String url, Bitmap bitmap) {
        Log.d("IMAGE PATH", "Image cache path: " + url);

        Glide.with(this).load(url)
                .into(profilePic);
        profilePic.setColorFilter(ContextCompat.getColor(UserProfile.this, android.R.color.transparent));

        //UploadPic(bitmap);
    }

    private void UploadPic(Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);

        assert bitmap != null;
        String encoded = convertBitmap(bitmap);

        byte[] image = encoded.getBytes();

        //String Url = BaseUrl.getUploadImage();

//        CustomerLogoModel model = new CustomerLogoModel(userID,11, encoded);
//        Gson gson = new Gson();
//        final String param = gson.toJson(model);
//        Log.d("LOGO",param);
//
//
//        try {
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                    Url, new JSONObject(param),
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.e("Resp_logo",response.toString());
//                            progressBar.setVisibility(View.GONE);
//                            try {
//                                if (response.getJSONObject("status").getInt("code") == 0) {
//                                    Toast.makeText(MyProfile.this, getString(R.string.upload_successful), Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(MyProfile.this, response.getJSONObject("status").getString("message"), Toast.LENGTH_LONG).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressBar.setVisibility(View.GONE);
//                    String message = null;
//                    if (error instanceof NetworkError) {
//                        message = getString(R.string.network_error);
//                    } else if (error instanceof ServerError) {
//                        message = getString(R.string.server_error);
//                    } else if (error instanceof AuthFailureError) {
//                        message = getString(R.string.auth_error);
//                    } else if (error instanceof ParseError) {
//                        message = getString(R.string.parse_error);
//                    } else if (error instanceof TimeoutError) {
//                        message = getString(R.string.timeout_error);
//                    } else {
//                        Toast.makeText(MyProfile.this, error.toString(), Toast.LENGTH_LONG).show();
//                    }
//
//                    assert message != null;
//                    Log.e("Upload_failed",message);
//                    Toast.makeText(MyProfile.this, getString(R.string.upload_failed), Toast.LENGTH_LONG).show();
//                }
//            }){
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("Content-Type", "application/json");
//                    params.put("Authorization", "Bearer " + ACCESS_TOKEN);
//                    params.put("App-Key", BaseUrl.AppKey);
//                    return params;
//                }
//
//            };
//
//            RequestQueue queue = Volley.newRequestQueue(MyProfile.this);
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    0,
//                    -1,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(jsonObjectRequest);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        //network call
    }

    private void loadProfileDefault(String userID) {
        progressBar.setVisibility(View.VISIBLE);

        //String Url = BaseUrl.getUserDetails(userID);

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressBar.setVisibility(View.GONE);
//                        try {
//                            if (response.getJSONObject("status").getInt("code") == 0) {
//
//                                if(!response.getJSONObject("data").isNull("imageDownloadLink")){
//                                    Glide.with(MyProfile.this).load(BaseUrl.mainUrl2 + response.getJSONObject("data").getString("imageDownloadLink"))
//                                            .apply(new RequestOptions().placeholder(R.drawable.new_user).error(R.drawable.new_user))
//                                            .into(profilePic);
//                                }
//
//                            }else{
//                                Toast.makeText(MyProfile.this, response.getJSONObject("status").getString("message"), Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//                Log.e("Error", error.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + ACCESS_TOKEN);
//                params.put("App-Key", BaseUrl.AppKey);
//                return params;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(MyProfile.this);
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                -1,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(jsonObjectRequest);

    }

    private String convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
