package cn.socialclock.ui;


import cn.socialclock.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AnalyticsActivity extends Activity implements OnClickListener {
    private Button btnTabMain;
    private Button btnTabSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("socialalarmlog", "AnalyticsActivity: onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.analytics);

        btnTabMain = (Button) findViewById(R.id.btn_tabMain);
        btnTabMain.setOnClickListener(this);
        btnTabSettings = (Button) findViewById(R.id.btn_tabSettings);
        btnTabSettings.setOnClickListener(this);

        Toast.makeText(AnalyticsActivity.this, "in development", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tabMain: {
                Intent switchTabMain = new Intent(AnalyticsActivity.this,
                        MainActivity.class);
                AnalyticsActivity.this.startActivity(switchTabMain);
                break;
            }
            case R.id.btn_tabSettings: {
                Intent switchTabMain = new Intent(AnalyticsActivity.this,
                        SettingsActivity.class);
                AnalyticsActivity.this.startActivity(switchTabMain);
                break;
            }
            // more buttons
        }
    }
}
