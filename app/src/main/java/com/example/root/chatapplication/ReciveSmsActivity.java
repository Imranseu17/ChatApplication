package com.example.root.chatapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReciveSmsActivity extends AppCompatActivity {

    private static  ReciveSmsActivity instance;
    ArrayList<String> smsMessageList = new ArrayList<>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    Button compose;

    public  static  ReciveSmsActivity getInstance(){
        return  instance;
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive_sms);
        smsListView = findViewById(R.id.smsList);
        compose = findViewById(R.id.btnCompose);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,smsMessageList);
        smsListView.setAdapter(arrayAdapter);
        refreashSmsInbox();
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String[] smsMessage = smsMessageList.get(position).split("\n");
                    String address = smsMessage[0];
                    String smsMessages = "";
                    for (int i = 0 ; i < smsMessage.length ; i++){
                        smsMessages += smsMessage[i];
                    }

                    String smsMessagestr = address + "\n";
                    smsMessagestr += smsMessages;
                    Toast.makeText(ReciveSmsActivity.this,smsMessagestr,Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReciveSmsActivity.this, SendSmsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreashSmsInbox() {

        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null,null,null,null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int timeMillis = smsInboxCursor.getColumnIndex("date");
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        String dateText = format.format(date);
        if (indexBody < 0 || !smsInboxCursor.moveToFirst())return;
        arrayAdapter.clear();
        do{
            String str = smsInboxCursor.getString(indexAddress)+"at"+
                    "\n"+smsInboxCursor.getString(indexBody)+dateText+"\n";
            arrayAdapter.add(str);
        }while (smsInboxCursor.moveToNext());

    }

    public void  updateList(final String smsMassage){
        arrayAdapter.insert(smsMassage,0);
        arrayAdapter.notifyDataSetChanged();
    }


}
