package com.accuvally.hdtui.utils.pay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.model.PayInfo;

public class PayUtils {

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(PayInfo info) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + Config.PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Config.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + info.getId() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + info.getProductTitle() + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + info.getProductTitle() + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + info.getAmount() + "\"";//
//		orderInfo += "&total_fee=" + "\"" + 0.01 + "\"";//

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + info.getNotifyUrl() + "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"http://180.150.178.179/pay/alipay_return\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return SignUtils.sign(content, Config.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
