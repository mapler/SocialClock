package cn.socialclock;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * @author mapler
 * 点击通知栏后的处理
 */

public class NotificationTouchAction extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("socialalarmlog", "NotificationAction: onCreate");
		
		GetUpAction.doAction(NotificationTouchAction.this);
		
		this.finish();
	}
}
