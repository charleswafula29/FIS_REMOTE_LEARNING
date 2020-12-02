package com.fis.fis_remote_learning.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fis.fis_remote_learning.BottomPopupActivity;
import com.fis.fis_remote_learning.R;
import com.fis.fis_remote_learning.models.CredentialsModel;
import com.fis.fis_remote_learning.models.CredsRespModel;
import com.fis.fis_remote_learning.models.StudentModel;
import com.fis.fis_remote_learning.models.UploadStudentPicModel;
import com.fis.fis_remote_learning.utils.BaseUrl;
import com.fis.fis_remote_learning.utils.Utils;
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
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static com.fis.fis_remote_learning.utils.BaseUrl.IAAP;
import static com.fis.fis_remote_learning.utils.Utils.IAAP_Popup;
import static com.fis.fis_remote_learning.utils.Utils.ShowLongSnackBar;
import static com.fis.fis_remote_learning.utils.Utils.convertCapitalText;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {
    private Context context;
    private Gson gson = new Gson();
    private RecyclerView recyclerView;
    private ImageView ChoosePhoto;
    private CircleImageView profilePic;
    private ProgressBar progressBar;
    private StudentModel model;
    private static final int REQUEST_IMAGE = 100;
    private View root;
    private AlertDialog alertDialog;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_student_profile, container, false);

        Paper.init(context);
        String strtext = Paper.book().read("stdModel").toString();
        model = gson.fromJson(strtext, StudentModel.class);
        Log.e("Model", model.toString());

        alertDialog = Utils.ShowLoader(context);

        TextView Names = root.findViewById(R.id.UserNames);
        TextView AccountStatus = root.findViewById(R.id.AccountStatus);
        TextView SUID = root.findViewById(R.id.SUID);
        TextView AdmissionNo = root.findViewById(R.id.AdmissionNo);
        TextView CName = root.findViewById(R.id.ClassName);
        recyclerView = root.findViewById(R.id.Recycler);
        profilePic = root.findViewById(R.id.AccountProfile_image);
        ChoosePhoto = root.findViewById(R.id.ChoosePhoto);
        progressBar = root.findViewById(R.id.ProgressBar);

        ChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerSelect();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        getProfilePic(model.getSid(), model.getAdmNo(), model.getGender());

        ((ConstraintLayout)root.findViewById(R.id.Credentials)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCredentials(model.getSid());
            }
        });

        ((ConstraintLayout)root.findViewById(R.id.StopPayment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomPopupActivity.CancelSubscription(context, model.getStudentNames(),model.getSid());
            }
        });

        Names.setText(convertCapitalText(model.getStudentNames()));
        AccountStatus.setText(model.getStatus());
        SUID.setText(model.getSUID());
        AdmissionNo.setText((model.getAdmNo().isEmpty()) ? getString(R.string.not_set) : model.getAdmNo());
        CName.setText(model.getClassName());

        if (model.getStatus().equals(IAAP)) {
            AlertDialog dialog = IAAP_Popup(context, Utils.convertCapitalText(model.getStudentNames()));
            dialog.show();
        }

        return root;
    }

    private void triggerSelect() {
        Dexter.withActivity((Activity) context)
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

        UploadStudentPicModel studentPicModel =
                new UploadStudentPicModel(model.getSid(), encoded);

        String param = gson.toJson(studentPicModel);

        String Url = BaseUrl.getUploadStudentPic();
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Url, new JSONObject(param),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Resp_logo", response.toString());
                            progressBar.setVisibility(View.GONE);
                            try {
                                if (response.getString("Check").equals("Profile Picture Updated SuccessFully")) {
                                    ShowLongSnackBar(root.findViewById(R.id.AccountLayout), getString(R.string.upload_successful));
                                } else {
                                    ShowLongSnackBar(root.findViewById(R.id.AccountLayout), getString(R.string.upload_failed));
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
                    ShowLongSnackBar(root.findViewById(R.id.AccountLayout), message);
                    Log.e("UploadError", error.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    private Bitmap ConvertBase64(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void getProfilePic(Integer sid, String admNo,final String gender) {
        progressBar.setVisibility(View.VISIBLE);

        String Url = BaseUrl.getFetchStudentPic(sid);

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
                            if (gender.equals("Male")) {
                                profilePic.setImageResource(R.drawable.boy_icon);
                            } else {
                                profilePic.setImageResource(R.drawable.girl_icon);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (gender.equals("Male")) {
                    profilePic.setImageResource(R.drawable.boy_icon);
                } else {
                    profilePic.setImageResource(R.drawable.girl_icon);
                }
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

    private void getCredentials(Integer sid) {
        alertDialog.show();
        String Url = BaseUrl.getStudentCredentials(sid);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        alertDialog.dismiss();

                        Log.e("RESP",response.toString());
                        CredsRespModel respModel = gson.fromJson(response.toString(),CredsRespModel.class);
                        List<CredentialsModel> list = new ArrayList<>(respModel.getCredentials());
                        Log.e("RESP_MODEL",list.toString());
                        BottomPopupActivity.StudentCredentialsBottomSheet(context,list);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
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
                ShowLongSnackBar(root.findViewById(R.id.AccountLayout), message);
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

}
