package com.example.root.chatapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SendSmsActivity extends AppCompatActivity {
    Button sendSmsBtn,inbox,pick;
    EditText tophoneNumber;
    EditText smsMessageET;
    EditText editName;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        sendSmsBtn = findViewById(R.id.btnsms);
        tophoneNumber = findViewById(R.id.edittextPhoneNo);
        smsMessageET = findViewById(R.id.edittextsms);
        editName = findViewById(R.id.edittextName);
        inbox = findViewById(R.id.btInbox);
        pick = findViewById(R.id.pick);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);
            }
        });
        sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tophoneNumber.getText().toString().length() != 0
                        && smsMessageET.getText().toString().length() !=0
                        && editName.getText().toString().length() != 0){
                    if (Patterns.PHONE.matcher(tophoneNumber.getText().toString()).matches())
                        sendSms();
                    tophoneNumber.setText("");
                    smsMessageET.setText("");
                    editName.setText("");
                }
                else {
                    Toast.makeText(SendSmsActivity.this,"frist valid input the name, number and sms",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendSmsActivity.this,ReciveSmsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendSms() {
        String tophone = tophoneNumber.getText().toString();
        String smsMessage = smsMessageET.getText().toString();
        String Name = editName.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(tophone,Name,smsMessage,null,null);
            Toast.makeText(this,"SMS sent",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case (7):
                if (resultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = data.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                editName.setText(TempNameHolder);

                                tophoneNumber.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;




        }

    }

    private  void sendsmswithInternet(){
        try {
            // Construct data
            String apiKey = "apikey=" + "fGcGlS2VABM-CaFeNcMK5ytSeLPAELhsq6mOUVW4FE";
            String message = "&message=" + smsMessageET.getText().toString();
            String numbers = "&numbers=" + tophoneNumber.getText().toString();
            String sender = "&sender=" + editName.getText().toString();

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
                Toast.makeText(SendSmsActivity.this,"Massage Send",Toast.LENGTH_LONG).show();
            }
            rd.close();


        } catch (Exception e) {
            Toast.makeText(SendSmsActivity.this,"Error: "+e,Toast.LENGTH_LONG).show();

        }
    }






}
