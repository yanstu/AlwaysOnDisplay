package com.luckyx.display;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    public static final String SCREEN_OFF_TIMEOUT = "screen_off_timeout";
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = (Switch) findViewById(R.id.vSwitch);
        Context context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                int time = 0;
                try {
                    time = Settings.System.getInt(getContentResolver(), SCREEN_OFF_TIMEOUT);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                if ((time / 1000 / 60) != 30) {
                    aSwitch.setChecked(false);
                } else {
                    aSwitch.setChecked(true);
                }
                aSwitch.setSwitchTextAppearance(this, R.style.s_off);
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        aSwitch.setSwitchTextAppearance(MainActivity.this, b ? R.style.s_on : R.style.s_off);
                        if (b) {
                            Settings.System.putInt(getContentResolver(), SCREEN_OFF_TIMEOUT, 1000 * 60 * 30);
                        }else{
                            Settings.System.putInt(getContentResolver(), SCREEN_OFF_TIMEOUT, 1000 * 60 * 2);
                        }
                    }
                });
            }
        }
    }
}