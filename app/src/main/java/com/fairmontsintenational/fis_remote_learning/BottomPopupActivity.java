package com.fairmontsintenational.fis_remote_learning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fairmontsintenational.fis_remote_learning.classes.SendMail;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import static androidx.core.content.FileProvider.getUriForFile;

public class BottomPopupActivity extends AppCompatActivity {

    private static final String TAG = "IMAGE_PICKER_CLASS";
    public static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    public static final String INTENT_ASPECT_RATIO_X = "aspect_ratio_x";
    public static final String INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y";
    public static final String INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio";
    public static final String INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality";
    public static final String INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height";
    public static final String INTENT_BITMAP_MAX_WIDTH = "max_width";
    public static final String INTENT_BITMAP_MAX_HEIGHT = "max_height";


    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;

    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static String fileName;
    public static int setType = 1;

    public interface onClickOptionsListener{
        void logoutClicked();
    }

    public interface PickerOptionListener {
        void onTakeCameraSelected();

        void onChooseGallerySelected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_image_intent_null), Toast.LENGTH_LONG).show();
            return;
        }

        ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X);
        ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y);
        IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, IMAGE_COMPRESSION);
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false);
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth);
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight);

        int requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            takeCameraImage();
        } else {
            chooseImageFromGallery();
        }
    }

    public static void ChangeUserPasswordBottomSheet(final Context context,final String phone_no){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                context, R.style.BottomSheetDialogTheme);
        final View view = LayoutInflater
                .from(context).inflate(R.layout.bottom_reset_password, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        final Button submit;
        final ProgressBar progressBar;
        final ImageView showPass,close;

        final EditText oldpassword = view.findViewById(R.id.UserOldPassword);
        final EditText newPass = view.findViewById(R.id.UserNewPassword);
        final EditText ConfirmPass = view.findViewById(R.id.UserConfirmPassword);
        submit = view.findViewById(R.id.UserChangePass);
        progressBar = view.findViewById(R.id.Loader);
        showPass = view.findViewById(R.id.UserShowOldPassword);
        close = view.findViewById(R.id.Close);
        final ConstraintLayout details = view.findViewById(R.id.Details);
        final ConstraintLayout found = view.findViewById(R.id.Found);

        details.setVisibility(View.VISIBLE);
        found.setVisibility(View.GONE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setType = 1;
                bottomSheetDialog.dismiss();
            }
        });

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setType==1) {
                    setType = 0;
                    oldpassword.setTransformationMethod(null);
                    if (oldpassword.getText().length() > 0)
                        oldpassword.setSelection(oldpassword.getText().length());
                }
                else{
                    setType=1;
                    oldpassword.setTransformationMethod(new PasswordTransformationMethod());
                    if(oldpassword.getText().length() > 0)
                        oldpassword.setSelection(oldpassword.getText().length());
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_pass=oldpassword.getText().toString().trim();
                String new_pass=newPass.getText().toString().trim();
                String confirm_pass=ConfirmPass.getText().toString().trim();

                if(old_pass.isEmpty()){
                    oldpassword.setError("Kindly enter your current password");
                }else if(new_pass.isEmpty()){
                    newPass.setError("Kindly enter your new password");
                }else if(confirm_pass.isEmpty()){
                    ConfirmPass.setError("Kindly enter your new password");
                }else if(!new_pass.equals(confirm_pass)){
                    ConfirmPass.setError("Passwords don't match!");
                }
                else{
//                    String URL= BaseUrl.updatePassword(phone_no,new_pass,old_pass);
//                    progressBar.setVisibility(View.VISIBLE);
//                    submit.setEnabled(false);
//                    close.setEnabled(false);
//
//                    StringRequest request=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            progressBar.setVisibility(View.GONE);
//                            submit.setEnabled(true);
//                            close.setEnabled(true);
//
//                            Log.e("RESPONSE",response);
//
//                            details.setVisibility(View.GONE);
//                            found.setVisibility(View.VISIBLE);
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            progressBar.setVisibility(View.GONE);
//                            submit.setEnabled(true);
//                            close.setEnabled(true);
//
//                            String message = null;
//                            if (error instanceof NetworkError) {
//                                message = "Cannot connect to Internet...Please check your connection!";
//                            } else if (error instanceof ServerError) {
//                                message = "The server could not be found. Please try again after some time!!";
//                            } else if (error instanceof AuthFailureError) {
//                                message = "Cannot connect to Internet...Please check your connection!";
//                            } else if (error instanceof ParseError) {
//                                message = "Parsing error! Please try again after some time!!";
//                            } else if (error instanceof TimeoutError) {
//                                message = "Connection TimeOut! Please check your internet connection.";
//                            }else{
//                                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
//                            }
//
//                            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    RequestQueue requestQueue= Volley.newRequestQueue(context);
//                    requestQueue.add(request);
                    Toast.makeText(context, "Reset password", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public static void ParentHomeMenuBottomSheet(final Context context, final String names, final String number, final onClickOptionsListener listener){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                context, R.style.BottomSheetDialogTheme);
        final View Bottomview = LayoutInflater
                .from(context).inflate(R.layout.bottom_user_menu, null);
        bottomSheetDialog.setContentView(Bottomview);

        TextView profile = Bottomview.findViewById(R.id.MyProfile);
        TextView Report = Bottomview.findViewById(R.id.Report);
        TextView Logout = Bottomview.findViewById(R.id.Logout);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UserProfile.class);
                intent.putExtra("parent_name",names);
                intent.putExtra("phoneNo", number);
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SendMail.class);
                intent.putExtra("parent_name",names);
                intent.putExtra("phoneNo", number);
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.logoutClicked();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    public static void showImagePickerOptions(Context context, final PickerOptionListener listener) {
        // setup the alert builder
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                context, R.style.BottomSheetDialogTheme);
        View Bottomview = LayoutInflater
                .from(context).inflate(R.layout.profile_bottom_sheet_options, null);
        bottomSheetDialog.setContentView(Bottomview);

        ConstraintLayout openGallery = Bottomview.findViewById(R.id.Gallery);
        ConstraintLayout Photo = Bottomview.findViewById(R.id.Camera);

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                listener.onChooseGallerySelected();
                bottomSheetDialog.dismiss();
            }
        });

        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //take camera
                listener.onTakeCameraSelected();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void takeCameraImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            fileName = System.currentTimeMillis() + ".jpg";
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void chooseImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    cropImage(getCacheImagePath(fileName));
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    cropImage(imageUri);
                } else {
                    setResultCancelled();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    handleUCropResult(data);
                } else {
                    setResultCancelled();
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                Log.e(TAG, "Crop error: " + cropError);
                setResultCancelled();
                break;
            default:
                setResultCancelled();
        }
    }

    private void cropImage(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private void handleUCropResult(Intent data) {
        if (data == null) {
            setResultCancelled();
            return;
        }
        final Uri resultUri = UCrop.getOutput(data);
        setResultOk(resultUri);
    }

    private void setResultOk(Uri imagePath) {
        Intent intent = new Intent();
        intent.putExtra("path", imagePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(BottomPopupActivity.this, getPackageName() + ".provider", image);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    /**
     * Calling this will delete the images from cache directory
     * useful to clear some memory
     */
    public static void clearCache(Context context) {
        File path = new File(context.getExternalCacheDir(), "camera");
        if (path.exists() && path.isDirectory()) {
            for (File child : path.listFiles()) {
                child.delete();
            }
        }
    }
}