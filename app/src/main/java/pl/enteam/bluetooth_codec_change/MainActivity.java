package pl.enteam.bluetooth_codec_change;

import static android.bluetooth.BluetoothCodecConfig.BITS_PER_SAMPLE_16;
import static android.bluetooth.BluetoothCodecConfig.CHANNEL_MODE_STEREO;
import static android.bluetooth.BluetoothCodecConfig.SAMPLE_RATE_44100;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BluetoothA2dp a2dp;
    TextView codecNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codecNameTextView = findViewById(R.id.codec_name_textview);

        BluetoothAdapter.getDefaultAdapter().getProfileProxy(getApplicationContext(), mProfileListener, BluetoothProfile.A2DP);
    }

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

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.A2DP) {
                a2dp = null;
            }
        }
    };

}