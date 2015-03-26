package cn.socialclock.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import cn.socialclock.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.settings);

        Button btn_tabMain = (Button) findViewById(R.id.btn_tabMain);
        btn_tabMain.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent switchTabMain = new Intent(SettingsActivity.this,
                        MainActivity.class);
                SettingsActivity.this.startActivity(switchTabMain);
            }
        });
        Button btn_tabAnalytics = (Button) findViewById(R.id.btn_tabAnalytics);
        btn_tabAnalytics.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent switchTabMain = new Intent(SettingsActivity.this,
                        HistoryActivity.class);
                SettingsActivity.this.startActivity(switchTabMain);
            }
        });
        Toast.makeText(SettingsActivity.this, "in development", Toast.LENGTH_SHORT).show();
    }
}
