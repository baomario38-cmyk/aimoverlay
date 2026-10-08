package com.aimoverlay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KeyActivity extends AppCompatActivity {

    private EditText edtKey;
    private TextView txtDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (KeyManager.isActivated(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_key);

        edtKey = findViewById(R.id.edtKey);
        txtDevice = findViewById(R.id.txtDevice);
        Button btnActivate = findViewById(R.id.btnActivate);

        String uuid = KeyManager.getDeviceUUID(this);
        txtDevice.setText("Device: " + uuid.substring(0, 16) + "...");

        btnActivate.setOnClickListener(v -> {
            String key = edtKey.getText().toString().trim();
            int result = KeyManager.validateKey(this, key);

            switch (result) {
                case 0:
                    Toast.makeText(this, R.string.key_error_empty, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, R.string.key_error_invalid, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, R.string.key_error_used, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(this, R.string.key_success, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    break;
            }
        });
    }
}
