package com.example.root.chatapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        if (ContextCompat.checkSelfPermission(CallLogActivity.this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                    Manifest.permission.READ_CALL_LOG)) {

                ActivityCompat.requestPermissions(CallLogActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            } else {
                ActivityCompat.requestPermissions(CallLogActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }

        } else {
            TextView textView = findViewById(R.id.textView);
            textView.setText(getCallDetails());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(CallLogActivity.this,
                            Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(CallLogActivity.this, " Permission Granted",
                                Toast.LENGTH_LONG).show();

                        TextView textView = findViewById(R.id.textView);
                        textView.setText(getCallDetails());
                    }

                } else {
                    Toast.makeText(CallLogActivity.this, " No Permission Granted",
                            Toast.LENGTH_LONG).show();
                }

                return;
            }
        }
    }

    private String getCallDetails() {
        StringBuffer stringBuffer = new StringBuffer();
        Uri allCalls = Uri.parse("content://call_log/calls");
        Cursor mangeCursor  = managedQuery(allCalls, null, null, null, null);

        int name = mangeCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = mangeCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = mangeCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = mangeCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = mangeCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (mangeCursor.moveToNext()){
            String phname = mangeCursor.getString(name);
            String phnumber = mangeCursor.getString(number);
            String phtype = mangeCursor.getString(type);
            String phdate = mangeCursor.getString(date);
            String phduration = mangeCursor.getString(duration);
            String dir = null;
            int dirCode = Integer.parseInt(phtype);

            Date calldateTime = new Date(Long.valueOf(phdate));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
            String callDate = simpleDateFormat.format(calldateTime);

            switch (dirCode){
                case  CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                case  CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case  CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            stringBuffer.append("\nName :  " +phname + "\nPhone Number : " +phnumber + "\nCall Type : "
                        +dir + "\nDate :  " +callDate + "\nDuration :  " + phduration + "\n..................");
        }

        mangeCursor.close();
        return stringBuffer.toString();
    }
}
