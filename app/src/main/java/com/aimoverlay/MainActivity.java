package com.aimoverlay;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_CODE = 1001;
    private SeekBar seekSize;
    private SeekBar seekThickness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!KeyManager.isActivated(this)) {
            startActivity(new Intent(this, KeyActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Button btnPermission = findViewById(R.id.btnPermission);
        Button btnStart = findViewById(R.id.btnStart);
        Button btnStop = findViewById(R.id.btnStop);
        seekSize = findViewById(R.id.seekSize);
        seekThickness = findViewById(R.id.seekThickness);

        btnPermission.setOnClickListener(v -> requestOverlayPermission());

        btnStart.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Cần cấp quyền overlay trước", Toast.LENGTH_SHORT).show();
                requestOverlayPermission();
                return;
            }
            Intent intent = new Intent(this, OverlayService.class);
            intent.putExtra("size", seekSize.getProgress());
            intent.putExtra("thickness", seekThickness.getProgress());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            Toast.makeText(this, "Overlay đã bật", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
        });

        btnStop.setOnClickListener(v -> {
            stopService(new Intent(this, OverlayService.class));
            Toast.makeText(this, "Overlay đã tắt", Toast.LENGTH_SHORT).show();
        });
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Đã cấp quyền overlay", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Chưa cấp quyền overlay", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
