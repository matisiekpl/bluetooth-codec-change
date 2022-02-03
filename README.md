# Bluetooth Codec Change
Mobile app that changes Bluetooth Device Audio Codec to SBC. Created with a little bit of Reverse Engineering.

This app uses hidden Android APIs like `android.bluetooth.BluetoothA2dp`. App can be compiled when these APIs are exposed. You can replace `android.jar` file in SDK directory in order to use "unlocked" Android SDK: https://github.com/anggrayudi/android-hidden-api

Following code changes Bluetooth Audio Codec to SBC. Method names was extracted by doing some RE on official Settings app: https://github.com/aosp-mirror/platform_packages_apps_settings/tree/master/src/com/android/settings 
```java
private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
    public void onServiceConnected(int profile, BluetoothProfile proxy) {
        if (profile == BluetoothProfile.A2DP) {
            a2dp = (BluetoothA2dp) proxy;
            BluetoothDevice device = a2dp.getActiveDevice();
            BluetoothCodecStatus status = a2dp.getCodecStatus(device);
            BluetoothCodecConfig config = status.getCodecConfig();
            int codec = config.getCodecType();
            BluetoothCodecConfig newConfig = new BluetoothCodecConfig(0,
                    1000 * 1000,
                    SAMPLE_RATE_44100,
                    BITS_PER_SAMPLE_16,
                    CHANNEL_MODE_STEREO,
                    32771,
                    49152,
                    0,
                    0);
            a2dp.setCodecConfigPreference(device, newConfig);
            BluetoothCodecStatus updatedStatus = a2dp.getCodecStatus(device);
            BluetoothCodecConfig updatedConfig = updatedStatus.getCodecConfig();
            runOnUiThread(() -> codecNameTextView.setText("Codec2:" + updatedConfig.toString()));
            MainActivity.this.finish();
            System.exit(0);
        }
    }
```
