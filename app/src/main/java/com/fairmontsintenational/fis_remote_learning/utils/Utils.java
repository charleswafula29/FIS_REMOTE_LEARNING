package com.fairmontsintenational.fis_remote_learning.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.Settings;
import com.androidstudy.daraja.util.TransactionType;
import com.fairmontsintenational.fis_remote_learning.Login;
import com.fairmontsintenational.fis_remote_learning.R;
import com.fairmontsintenational.fis_remote_learning.Register;
import com.fairmontsintenational.fis_remote_learning.classes.Sessions;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.BusinessShortCode;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.Passkey;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.REG_AMOUNT;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.callbackurl;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.transctionDesc;

public class Utils {

    public static String pickPreviewText(String text){
        int length = text.length();
        String preview;
        if(length>30){
            preview =text.substring(0,30)+"....";
        }else {
            preview = text;
        }
        return preview;
    }

    public static String pickFirstName(String names){
        List<String> list = new ArrayList<String>(Arrays.asList(names.split(" ")));
        return list.get(0);
    }

    public static String formatNumber(String amount) {
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        Double d = Double.valueOf(amount);
        return myFormat.format(d);
    }

    public static String convertDate(String parsedate) throws ParseException {
        String new_date = parsedate.replace("T00:00:00", "");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());
        Date date = sdf.parse(parsedate);
        assert date != null;
        return new SimpleDateFormat("EE MMM dd",java.util.Locale.getDefault()).format(date);
    }

    public static String convertCapitalText(String text){
        String str = text.toLowerCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static int getTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        int minutes = calendar.get(Calendar.MINUTE);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public static String getCurrentDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());
        Date date = new Date();
        return formatter.format(date);
    }

    public static void ShowLongSnackBar(View view,String Message){
        Snackbar.make(view,Message,Snackbar.LENGTH_LONG).show();
    }
    public static ProgressDialog ShowProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.registering));
        return progressDialog;
    }
    public static AlertDialog ShowLoader(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loadingdialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        //dialog.show();
        return dialog;
    }

    public static void ShowSuccessPopup(Context context,String Title,String Message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.success_layout, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView) view.findViewById(R.id.title)).setText(Title);
        ((TextView) view.findViewById(R.id.Message)).setText(Message);
        view.findViewById(R.id.Close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static AlertDialog LockedAccountPopup(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.locked_account, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //dialog.show();
        return dialog;
    }

    public static AlertDialog RegistrationSuccessPopup(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.registration_successful, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //dialog.show();
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    public static AlertDialog PSA_Popup(Context context, String Snames){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.registration_successful, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView Title = view.findViewById(R.id.title);
        TextView Text = view.findViewById(R.id.textView5);
        Title.setText(context.getString(R.string.psa_title));
        Text.setText(Snames+context.getString(R.string.psa_text));
        //dialog.show();
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    public static AlertDialog IAAP_Popup(Context context, String Snames){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.iaap_popup, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView Title = view.findViewById(R.id.title);
        TextView Text = view.findViewById(R.id.textView5);
        Text.setText(Snames+context.getString(R.string.hello_your_account_iaap));
        //dialog.show();
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    public static AlertDialog DEAC_Popup(Context context, String Snames){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.deac_popup, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView Text = view.findViewById(R.id.textView5);
        Text.setText(Snames+context.getString(R.string.hello_your_account_deac));
        //dialog.show();
        return dialog;
    }
    @SuppressLint("SetTextI18n")
    public static AlertDialog SAP_Popup(final Context context, final String Snames, final Integer amount, final Integer SID){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sap_popup, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView Balance = view.findViewById(R.id.Total);
        TextView Text = view.findViewById(R.id.textView5);
        Button Pay = view.findViewById(R.id.Pay);
        Balance.append(": KSH "+amount);
        Text.setText(Snames+context.getString(R.string.hello_your_account_sap));

        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Mpesa_Popup(context,Snames,amount,SID);
            }
        });
        //dialog.show();
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    public static AlertDialog StudentRegistered_Popup(final Context context, final String Snames, final Integer amount, final Integer SID){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.student_registered_popup, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ((Activity)context).finish();
            }
        });

        TextView Balance = view.findViewById(R.id.Balance);
        Button Pay = view.findViewById(R.id.Pay);
        Balance.append(": KSH "+amount);

        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Mpesa_Popup(context,Snames,amount,SID);
            }
        });
        //dialog.show();
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    public static AlertDialog Mpesa_Popup(final Context context, String Snames, Integer amount, final Integer SUID){
        final String timestamp = Settings.generateTimestamp();
        final String password = Settings.generatePassword(BusinessShortCode, Passkey, timestamp);

        final Daraja mydaraja = Daraja.with("DLBNaGlClN7Dl78MWv8JgNCtA2jWBVr1", "zY9E3xniVsLIpKee", Env.PRODUCTION, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.e("Mpesa Token", accessToken.getAccess_token());
            }

            @Override
            public void onError(String error) {
                Log.e("Mpesa Token Error", error);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lipa_na_mpesa_popup, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        ((ImageView) view.findViewById(R.id.Close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView Balance = view.findViewById(R.id.Balance);
        final EditText PhoneNo = view.findViewById(R.id.phone_number);
        final EditText Amount = view.findViewById(R.id.Amount);
        final ProgressBar progressBar = view.findViewById(R.id.ProgressBar);
        final Button Pay = view.findViewById(R.id.Pay);
        Balance.append(": KSH "+formatNumber(String.valueOf(amount)));

        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = PhoneNo.getText().toString().trim();
                String cash = Amount.getText().toString().trim();
                if(phone.isEmpty()){
                    PhoneNo.setError(context.getString(R.string.phone_empty));
                    PhoneNo.requestFocus();
                }else if(phone.length()!=10){
                    PhoneNo.setError(context.getString(R.string.phone_format));
                    PhoneNo.requestFocus();
                }else if(cash.isEmpty()){
                    Amount.setError(context.getString(R.string.amount_empty));
                    Amount.requestFocus();
                }else if(Integer.parseInt(cash)<=0){
                    Amount.setError(context.getString(R.string.amount_format));
                    Amount.requestFocus();
                }
                else {
                    Pay.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    LNMExpress lnmExpress = new LNMExpress(
                            BusinessShortCode,
                            Passkey,
                            TransactionType.CustomerPayBillOnline,
                            cash,
                            phone,
                            BusinessShortCode,
                            phone,
                            callbackurl,
                            "SID"+SUID,
                            transctionDesc
                    );

                    mydaraja.requestMPESAExpress(lnmExpress,
                            new DarajaListener<LNMResult>() {
                                @Override
                                public void onResult(@NonNull LNMResult lnmResult) {
                                    progressBar.setVisibility(View.GONE);
                                    //((Activity)context).finish();
                                    Log.e("MPESA_RESP", lnmResult.ResponseDescription);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(String error) {
                                    dialog.dismiss();
                                    //((Activity)context).finish();
                                    Toast.makeText(context, "Failed to trigger payment!", Toast.LENGTH_LONG).show();
                                    Log.e("MPESA_FAILED", error.toString());
                                    Log.e("MPESA_FAILED_TIMESTAMP", timestamp);
                                    Log.e("MPESA_FAILED_PASS", password);
                                }
                            });
                }
            }
        });

        return dialog;
    }


}
