package cn.socialclock;

import java.sql.Time;

import cn.socialclock.db.AlarmTransaction;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.Utils;

import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class GetUpAction {
	static void doAction(Context context) {
		Log.d("socialalarmlog", "GetUpAction: doAction()");
		AlarmCreator alarmCreator = new AlarmCreator(context);
		alarmCreator.createNormalAlarm();
		AlarmTransaction thisTransaction = AlarmPop.getThisTransaction();
		/* 清除消息栏 */
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(ConstantData.AlarmType.ALARM_SNOOZE);

		/* 将起床时间写入数据库 */
		Time getupTime = new java.sql.Time(System.currentTimeMillis());
		thisTransaction.setGetup_time(getupTime);
		thisTransaction.setIsgetup(1);
		thisTransaction.insertToDb(context);
		
		long timepass = (thisTransaction.getGetup_time().getTime() - thisTransaction
				.getAlarm_time().getTime()) / 1000;
		String timepassS = ""
				+ (timepass / 60 > 0 ? (timepass / 60 + "分") : "") + timepass
				% 60 + "秒";

		String textGetupWeibo = "(这是测试)"
				+ "我本来定的"
				+ thisTransaction.getAlarm_time().getHours()
				+ "点"
				+ Utils.timeFormat(thisTransaction.getAlarm_time()
						.getMinutes())
				+ "分的闹钟，结果"
				+ thisTransaction.getGetup_time().getHours()
				+ "点"
				+ Utils.timeFormat(thisTransaction.getGetup_time()
						.getMinutes()) + "分才起来。按掉"
				+ thisTransaction.getSnooze_times() + "次闹铃，赖床" + timepassS
				+ "...我果然是个废物。(" + thisTransaction.getAlarm_date().toString()
				+ ")";
		Log.d("socialalarmlog", textGetupWeibo);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(OAuthConstant.getInstance().getToken(),
					OAuthConstant.getInstance().getTokenSecret());
			Status status = weibo.updateStatus(textGetupWeibo);
			Toast.makeText(context, "已上传微博", Toast.LENGTH_LONG).show();
			System.out.println(status.getId() + " : " + status.getText() + "  "
					+ status.getCreatedAt());

		} catch (WeiboException e) {
			e.printStackTrace();
			Toast.makeText(context, "上传微博失败", Toast.LENGTH_LONG).show();
		}

	}
}
