package com.fairmontsintenational.fis_remote_learning.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fairmontsintenational.fis_remote_learning.BottomPopupActivity;
import com.fairmontsintenational.fis_remote_learning.Login;
import com.fairmontsintenational.fis_remote_learning.R;
import com.fairmontsintenational.fis_remote_learning.classes.Sessions;
import com.fairmontsintenational.fis_remote_learning.forgotPassword.ForgotPassword;
import com.fairmontsintenational.fis_remote_learning.forgotPassword.VerificationCode;
import com.fairmontsintenational.fis_remote_learning.models.LoginDataModel;
import com.fairmontsintenational.fis_remote_learning.utils.BaseUrl;
import com.fairmontsintenational.fis_remote_learning.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static com.fairmontsintenational.fis_remote_learning.utils.Utils.ShowLongSnackBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentAccountFragment extends Fragment {

    private Context context;
    private String phone_no;
    private CircleImageView profilePic;
    private ProgressBar progressBar;
    private static final int REQUEST_IMAGE = 100;
    private LoginDataModel dataModel;
    private View root;

    public ParentAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_parent_account, container, false);

        Paper.init(context);

        String parentData = Paper.book().read("ParentData").toString();
        Gson gson = new Gson();
        dataModel = gson.fromJson(parentData,LoginDataModel.class);

        TextView parentName = root.findViewById(R.id.Fname);
        TextView parentPhone = root.findViewById(R.id.PhoneNo);
        TextView parentFullNames = root.findViewById(R.id.AccountProfile_names);
        profilePic = root.findViewById(R.id.AccountProfile_image);
        ImageView choosePhoto = root.findViewById(R.id.ChoosePhoto);
        progressBar = root.findViewById(R.id.ProgressBar);
        TextView resetPassword = root.findViewById(R.id.ResetPassword);

        loadProfileDefault(dataModel.getParentID());

        (root.findViewById(R.id.Logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });

        parentName.setText(dataModel.getFirstName());
        parentPhone.setText(Paper.book().read("Phone").toString());
        parentFullNames.setText(Utils.convertCapitalText(dataModel.getFirstName()));

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(Paper.book().read("Phone").toString());
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerSelect();
            }
        });

        BottomPopupActivity.clearCache(context);

        return root;
    }

    private void changePassword(final String parentPhone) {
        final AlertDialog dialog = Utils.ShowLoader(context);
        dialog.show();
        String url = BaseUrl.getRequestResetCode(parentPhone);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent(context, VerificationCode.class);
                            intent.putExtra("PhoneNo",parentPhone);
                            intent.putExtra("Code",response.getString("Check"));
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowSnackBarError("Failed, please contact our help desk or try again later.");
                        }
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
                    ShowSnackBarError(error.toString());
                }
                ShowSnackBarError(message);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

    private void triggerSelect() {
        Dexter.withActivity((Activity)context)
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
        BottomPopupActivity.showImagePickerOptions(context, new BottomPopupActivity.PickerOptionListener() {
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
        Intent intent = new Intent(context, BottomPopupActivity.class);
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
        Intent intent = new Intent(context, BottomPopupActivity.class);
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    // loading profile image from local cache
                    assert uri != null;
                    loadProfile(uri.toString(), bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, getString(R.string.failed_picture), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void loadProfile(String url, Bitmap bitmap) {
        Log.d("IMAGE PATH", "Image cache path: " + url);

        Glide.with(this).load(url)
                .into(profilePic);
        profilePic.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));

        UploadPic(bitmap);
    }

    private void UploadPic(Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);

        assert bitmap != null;
        String encoded = convertBitmap(bitmap);

        byte[] image = encoded.getBytes();

        LinkedHashMap<String,String> param = new LinkedHashMap<>();
        param.put("ParentID",dataModel.getParentID());
        param.put("ImageinBase64",encoded);

        Log.e("POST",new JSONObject(param).toString());

        String Url = BaseUrl.getUploadParentPic();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Url, new JSONObject(param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Resp_logo",response.toString());
                        progressBar.setVisibility(View.GONE);
                        try {
                            if (!response.getString("Check").equals("Failed")) {
                                ShowLongSnackBar(root.findViewById(R.id.AccountLayout),getString(R.string.upload_successful));
                            }else{
                                ShowLongSnackBar(root.findViewById(R.id.AccountLayout),getString(R.string.upload_failed));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
                ShowLongSnackBar(root.findViewById(R.id.AccountLayout),message);
                Log.e("UploadError",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }

    private void loadProfileDefault(String parentID) {
        progressBar.setVisibility(View.VISIBLE);

        String Url = BaseUrl.getParentProfPic(parentID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Bitmap bitmap = ConvertBase64(response.getString("ByteArray"));
                            profilePic.setImageBitmap(bitmap);
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }

    private String convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap ConvertBase64(String base64String){
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void LogOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.confirmation_popup, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button proceed = view.findViewById(R.id.ConfirmationOk);

        (view.findViewById(R.id.ConfirmationCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("Session", Sessions.InActive.toString());
                startActivity(new Intent(context, Login.class));
                ((Activity)context).finish();
            }
        });

        dialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    private void ShowSnackBarError(String message){
        Snackbar.make(root.findViewById(R.id.AccountLayout),message,Snackbar.LENGTH_SHORT).show();
    }
}
