package cn.socialclock;

import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

	Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.settings);
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);
		AccessToken accessToken;
		TextView tx = (TextView) findViewById(R.id.textoauth);
		uri = this.getIntent().getData();
		tx.setText("得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"
				+ OAuthConstant.getInstance().getToken()
				+ "\n Access token secret:\n"
				+ OAuthConstant.getInstance().getTokenSecret());
		if (uri != null) {
			try {
				RequestToken requestToken = OAuthConstant.getInstance()
						.getRequestToken();
				accessToken = requestToken.getAccessToken(uri
						.getQueryParameter("oauth_verifier"));
				OAuthConstant.getInstance().setAccessToken(accessToken);
				Log.d("socialalarmlog",
						"得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"
								+ accessToken.getToken()
								+ "\n Access token secret:\n"
								+ accessToken.getTokenSecret()
								+ "\nrequestToken.getToken()\n"
								+ requestToken.getToken()
								+ "\nrequestToken.getTokenSecret()\n"
								+ requestToken.getTokenSecret());

				tx.setText("得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"
						+ accessToken.getToken()
						+ "\n Access token secret:\n"
						+ accessToken.getTokenSecret());
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if (OAuthConstant.getInstance().getAccessToken()!=null){
			TextView tx1 = (TextView)findViewById(R.id.isOauthWeibo);
			tx1.setText("已验证");
			tx1.setBackgroundColor(R.color.themeblue);
		}

		Button btnOAuthWeibo = (Button) findViewById(R.id.btn_oauthWeibo);
		btnOAuthWeibo.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Weibo weibo = new Weibo();
				RequestToken requestToken;
				try {
					requestToken = weibo
							.getOAuthRequestToken("weibo4android://OAuthActivity");
					OAuthConstant.getInstance().setRequestToken(requestToken);
					Uri uri = Uri.parse(requestToken.getAuthenticationURL()
							+ "&display=mobile");
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		Button btnTestWeibo = (Button) findViewById(R.id.btn_testWeibo);
		btnTestWeibo.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {
					Weibo weibo = new Weibo();
					weibo.setToken(OAuthConstant.getInstance().getToken(),
							OAuthConstant.getInstance().getTokenSecret());
					Status status = weibo.updateStatus("testing..");
					System.out.println(status.getId() + " : "
							+ status.getText() + "  " + status.getCreatedAt());

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(Settings.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});

		Button btn_tabMain = (Button) findViewById(R.id.btn_tabMain);
		btn_tabMain.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent switchTabMain = new Intent(Settings.this,
						MainActivity.class);
				Settings.this.startActivity(switchTabMain);
			}
		});
		Button btn_tabAnalytics = (Button) findViewById(R.id.btn_tabAnalytics);
		btn_tabAnalytics.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent switchTabMain = new Intent(Settings.this,
						AnalyticsActivity.class);
				Settings.this.startActivity(switchTabMain);
			}
		});
	}
}
