package com.example.root.chatapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 3/10/18.
 */

public class SmsBroadcastReciver extends BroadcastReceiver {

    public  static  final String SMS_BUNDLE = "puds";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if(intentExtras != null){
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessagestr = "";
            for(int i = 0; i < sms.length; i++){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])sms[i]);
                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                long timeMillis = smsMessage.getTimestampMillis();

                Date date = new Date(timeMillis);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                String dateText =simpleDateFormat.format(date);

                smsMessagestr += address +"at "+"\t"+dateText+"\n";
                smsMessagestr += smsBody+"\n";

            }
            Toast.makeText(context,smsMessagestr,Toast.LENGTH_LONG).show();
            ReciveSmsActivity instance = ReciveSmsActivity.getInstance();
            if (instance != null)
                instance.updateList(smsMessagestr);
        }

    }
}
