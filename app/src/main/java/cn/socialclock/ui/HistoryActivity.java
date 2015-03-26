package cn.socialclock.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import cn.socialclock.R;
import cn.socialclock.manager.SocialClockManager;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;

public class HistoryActivity extends Activity implements OnClickListener {

    private SocialClockManager socialClockManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SocialClockLogger.log("HistoryActivity: onCreate");

        super.onCreate(savedInstanceState);

        // alarm creator init
        socialClockManager = new SocialClockManager(this);

        // build ui
        buildInterface();
    }

    private void buildInterface() {
        // set screen orientation portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // set title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set view layout
        setContentView(R.layout.history);

        // build alarm event list
        buildListInterface();

        // build tab menu init
        buildTabMenuButton();

    }

    /** alarm event list */
    private void buildListInterface() {
        ListView listView = (ListView) findViewById(R.id.alarm_event_list);

        SimpleAdapter allAlarmEventAdapter = new SimpleAdapter(this,
                socialClockManager.getParsedAllFinishedAlarmEvents(), R.layout.alarmeventitem,
                new String[] {ConstantData.AdapterKey.ALARM_EVENT_USER_ID_KEY,
                        ConstantData.AdapterKey.ALARM_EVENT_START_AT_KEY,
                        ConstantData.AdapterKey.ALARM_EVENT_END_AT_KEY,
                        ConstantData.AdapterKey.ALARM_EVENT_SNOOZE_TIMES_KEY},
                new int[] {R.id.userId, R.id.startAt, R.id.endAt, R.id.snoozeTimes});

        listView.setAdapter(allAlarmEventAdapter);
    }

    /** tab menu init */
    private void buildTabMenuButton() {
        Button btnTabMain = (Button) findViewById(R.id.btn_tabMain);
        btnTabMain.setOnClickListener(this);
        Button btnTabSettings = (Button) findViewById(R.id.btn_tabSettings);
        btnTabSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tabMain: {
                Intent switchTabMain = new Intent(this, MainActivity.class);
                startActivity(switchTabMain);
                break;
            }
            case R.id.btn_tabSettings: {
                Intent switchTabMain = new Intent(this, SettingsActivity.class);
                startActivity(switchTabMain);
                break;
            }
            default:
                break;
        }
    }
}
