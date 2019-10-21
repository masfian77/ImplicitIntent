package com.imastudio.implicitintent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmsActivity extends AppCompatActivity {

    @BindView(R.id.edt_to)
    EditText edtTo;
    @BindView(R.id.edt_body_sms)
    EditText edtBodySms;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_send_intent)
    Button btnSendIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ButterKnife.bind(this);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        10);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @OnClick({R.id.edt_to, R.id.btn_send, R.id.btn_send_intent})
    public void onViewClicked(View view) {

        String noTelp = edtTo.getText().toString().trim();
        String bodySms = edtBodySms.getText().toString().trim();

        switch (view.getId()) {
            case R.id.edt_to:

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 100);

                break;
            case R.id.btn_send:

                if (TextUtils.isEmpty(noTelp) || TextUtils.isEmpty(bodySms)) {
                    Toast.makeText(this, "Fill required!", Toast.LENGTH_SHORT).show();
                }else {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(noTelp, null, bodySms, null, null);
                    Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_send_intent:

                if (TextUtils.isEmpty(noTelp) || TextUtils.isEmpty(bodySms)) {
                    Toast.makeText(this, "Fill required!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intentSms = new Intent(Intent.ACTION_SENDTO);
                    intentSms.setData(Uri.parse("smsto:" + Uri.encode(noTelp)));
                    intentSms.putExtra("sms_body", bodySms);
                    startActivity(intentSms);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                Cursor cursor = null;
                Uri uri = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null, null);
                }

                if (cursor != null && cursor.moveToNext()){
                    String nomerTelepon = cursor.getString(0);
                    edtTo.setText(nomerTelepon);
                }

            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }

    }
}