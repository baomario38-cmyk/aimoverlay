package com.aimoverlay;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KeyManager {

    private static final String PREF_NAME = "aim_overlay_pref";
    private static final String KEY_ACTIVATED = "activated";
    private static final String KEY_USED = "used_key";
    private static final String KEY_DEVICE = "device_uuid";

    private static final Set<String> VALID_KEYS = new HashSet<>(Arrays.asList(
            "AIM-7X9K-2M4P-QW8E",
            "AIM-3F6H-9J2L-ZX5C",
            "AIM-8N1B-4V7R-TY3U",
            "AIM-5D2G-6K9M-AS4D",
            "AIM-1Q8W-3E6R-FG7H",
            "AIM-9T4Y-2U5I-OP1A",
            "AIM-6S3D-7F8G-HJ2K",
            "AIM-4L5Z-1X2C-VB3N",
            "AIM-2M7N-8B9V-CX4Z",
            "AIM-0P1O-5I6U-YT7R"
    ));

    public static String getDeviceUUID(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        if (androidId == null) androidId = "unknown_device";
        return sha256(androidId);
    }

    public static boolean isActivated(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(KEY_ACTIVATED, false)) return false;
        String savedDevice = pref.getString(KEY_DEVICE, "");
        String currentDevice = getDeviceUUID(context);
        return savedDevice.equals(currentDevice);
    }

    public static int validateKey(Context context, String key) {
        if (key == null || key.trim().isEmpty()) return 0;
        String normalized = key.trim().toUpperCase();
        if (!VALID_KEYS.contains(normalized)) return 1;

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String usedKey = pref.getString(KEY_USED, "");
        String currentDevice = getDeviceUUID(context);

        if (!usedKey.isEmpty()) {
            String usedDevice = pref.getString(KEY_DEVICE, "");
            if (usedKey.equals(normalized) && usedDevice.equals(currentDevice)) {
                return 2;
            }
            if (usedKey.equals(normalized) && !usedDevice.equals(currentDevice)) {
                return 3;
            }
            return 3;
        }

        pref.edit()
                .putBoolean(KEY_ACTIVATED, true)
                .putString(KEY_USED, normalized)
                .putString(KEY_DEVICE, currentDevice)
                .apply();
        return 2;
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            return input;
        }
    }
}
