package ru.geekbrains.sms_manager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10; // Этот код будет возворащаться, когда пользователь согласится
    private static final String INPUT_MESSAGE = "Входящие";
    private static final String OUTPUT_MESSAGE = "Исходящие";
    private String phoneNumber;
    private String message;
    private EditText editTextPhoneNumber;
    private EditText editTextMessage;
    private Button btnSend;
    private AdapterSmsManager adapterSmsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initGui();
        initOnClickEvent();
        Realm.init(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            String from = (String) getIntent().getSerializableExtra("addressFrom");
            String message = (String) getIntent().getSerializableExtra("message");
            if (from.length() > 1 && message.length() > 1){
                addElement(from, message, INPUT_MESSAGE);
                getIntent().removeExtra("addressFrom");
                getIntent().removeExtra("message");
            }
        }

    }

    private void initOnClickEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMessage.getText().length() > 1 && editTextPhoneNumber.getText().length() > 1){

                    phoneNumber = editTextPhoneNumber.getText().toString();
                    message = editTextMessage.getText().toString();

                    makeSms(phoneNumber, message);

                    Snackbar.make(v, "Сообщение отправлено!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(v, "Произошла ошибка при отправке!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                }
        });
    }

    private void initGui() {
        editTextPhoneNumber = findViewById(R.id.edit_phone_number);
        editTextMessage = findViewById(R.id.edit_text_message);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        btnSend = findViewById(R.id.btn_send);

        adapterSmsManager = new AdapterSmsManager();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSmsManager);
    }


    // Realm function
    private void addElement(String from, String message, String status){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(new SMSTable(from, message, status));
        realm.commitTransaction();
        realm.close();
        adapterSmsManager.adapterNotifyDataSetChange();
    }

    public void deleteAllFromRealm(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(SMSTable.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        adapterSmsManager.adapterNotifyDataSetChange();
    }




    // Запрос пермиссии для СМС
    public void requestForSMSPermission() {
        // Можем ли мы запрашивать пермиссии, если нет, то и смысла нет запрашивать
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            // Запрашиваем пермиссии у пользователя
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }

    // Это результат запроса у пользователя пермиссии
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Все препоны пройдены и пермиссия дана, можно делать СМС
                    makeSms(phoneNumber, message);
                }
            default:
        }
    }

    void makeSms(String phoneNumber, String message){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
            addElement(phoneNumber, message, OUTPUT_MESSAGE);
        } else {
// Если пермиссии нет, то запросим у пользователя
            requestForSMSPermission();
        }
    }



    // настройки меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {
            deleteAllFromRealm();
        }

        return super.onOptionsItemSelected(item);
    }
}
