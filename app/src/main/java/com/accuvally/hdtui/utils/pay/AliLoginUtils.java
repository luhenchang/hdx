package com.accuvally.hdtui.utils.pay;

import android.app.Activity;

import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.utils.eventbus.ChangeAliLoginEventBus;
import com.alipay.sdk.app.AuthTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;

//阿里授权登录  invisible
public class AliLoginUtils {

	private static final int SDK_AUTH_FLAG = 1;

	public void auth(final Activity activity, String app_id, String pid, String target_id) {
		// 授权信息
		String info = getAuthInfo(app_id, pid, target_id);
		// 对授权信息做签名
		String sign = sign(info);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝授权规范的参数信息
		final String authInfo = info + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable authRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造AuthTask 对象
				AuthTask authTask = new AuthTask(activity);
				// 调用授权接口，获取授权结果
				String result = authTask.auth(authInfo);

				EventBus.getDefault().post(new ChangeAliLoginEventBus(SDK_AUTH_FLAG, result));
			}
		};

		Thread authThread = new Thread(authRunnable);
		authThread.start();
	}

	public String getAuthInfo(String app_id, String pid, String target_id) {
		// 服务接口名称， 固定值
		String authInfo = "apiname=\"com.alipay.account.auth\"";

		// 商户签约拿到的app_id，如：2013081700024223
		authInfo += "&app_id=" + "\"" + app_id + "\"";

		// 商户类型标识， 固定值
		authInfo += "&app_name=\"mc\"";

		// 授权类型，授权常量值为"AUTHACCOUNT", 登录常量值为"LOGIN"
		authInfo += "&auth_type=\"AUTHACCOUNT\"";

		// 业务类型， 固定值
		authInfo += "&biz_type=\"openservice\"";

		// 商户签约拿到的pid，如：2088102123816631
		authInfo += "&pid=" + "\"" + pid + "\"";

		// 产品码， 固定值
		authInfo += "&product_id=\"WAP_FAST_LOGIN\"";

		// 授权范围， 固定值
		authInfo += "&scope=\"kuaijie\"";

		// 商户标识该次用户授权请求的ID，该值在商户端应保持唯一，如：kkkkk091125
		authInfo += "&target_id=" + "\"" + target_id + "\"";

		// 签名时间戳
		authInfo += "&sign_date=" + "\"" + getSignDate() + "\"";

		return authInfo;

	}

	public String sign(String content) {
		return SignUtils.sign(content, Config.RSA_PRIVATE);
	}

	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	public String getSignDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return format.format(new Date());
	}
}
