package com.fairmontsintenational.fis_remote_learning.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.Settings;
import com.androidstudy.daraja.util.TransactionType;
import com.fairmontsintenational.fis_remote_learning.R;

import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.BusinessShortCode;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.Passkey;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.REG_AMOUNT;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.callbackurl;
import static com.fairmontsintenational.fis_remote_learning.utils.BaseUrl.transctionDesc;

public class MpesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void validatePageTwo(Context context,String SUID) {

    }
}
