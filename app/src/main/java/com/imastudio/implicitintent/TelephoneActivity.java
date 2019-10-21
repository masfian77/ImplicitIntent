package com.imastudio.implicitintent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TelephoneActivity extends AppCompatActivity {

    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.btn_call)
    Button btnCall;
    @BindView(R.id.btn_dial_phone)
    Button btnDialPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone);

        ButterKnife.bind(this);


    }

    @OnClick({R.id.edt_phone, R.id.btn_call, R.id.btn_dial_phone})
    public void onViewClicked(View view) {
        String noTelpon = edtPhone.getText().toString().trim();

        switch (view.getId()) {
            case R.id.edt_phone:

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 100);

                break;
            case R.id.btn_call:

                if (TextUtils.isEmpty(noTelpon)) {
                    Toast.makeText(this, "Fill required!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentCall = new Intent(Intent.ACTION_CALL);
                    intentCall.setData(Uri.parse("tel:" + noTelpon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                            return;
                        }
                    }
                    startActivity(intentCall);
                }

                break;
            case R.id.btn_dial_phone:

                if (TextUtils.isEmpty(noTelpon)) {
                    Toast.makeText(this, "Fill required!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intentDial = new Intent(Intent.ACTION_DIAL);
                    intentDial.setData(Uri.parse("tel:" + noTelpon));
                    startActivity(intentDial);
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
                    edtPhone.setText(nomerTelepon);
                }

            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
