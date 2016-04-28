package com.accuvally.hdtui.utils.pay;import android.text.TextUtils;public class AuthResult {	private String resultStatus;	private String result;	private String memo;	private String resultCode;	private String authCode;	private String alipayOpenId;	public AuthResult(String rawResult) {		if (TextUtils.isEmpty(rawResult))			return;		String[] resultParams = rawResult.split(";");		for (String resultParam : resultParams) {			if (resultParam.startsWith("resultStatus")) {				resultStatus = getResultParam(resultParam, "resultStatus");			}			if (resultParam.startsWith("result")) {				result = getResultParam(resultParam, "result");			}			if (resultParam.startsWith("memo")) {				memo = getResultParam(resultParam, "memo");			}		}		String[] resultValue = result.split("&");		for (String value : resultValue) {			if (value.startsWith("alipay_open_id")) {				alipayOpenId = getValue(value);			}			if (value.startsWith("auth_code")) {				authCode = getValue(value);			}			if (value.startsWith("result_code")) {				resultCode = getValue(value);			}		}	}	@Override	public String toString() {		return "resultStatus={" + resultStatus + "};memo={" + memo				+ "};result={" + result + "}";	}	private String getResultParam(String content, String key) {		String prefix = key + "={";		return content.substring(content.indexOf(prefix) + prefix.length(),				content.lastIndexOf("}"));	}	private String getValue(String data) {		String[] content = data.split("=\"");		String value = content[1];		return value.substring(0, value.lastIndexOf("\""));	}	/**	 * @return the resultStatus	 */	public String getResultStatus() {		return resultStatus;	}	/**	 * @return the memo	 */	public String getMemo() {		return memo;	}	/**	 * @return the result	 */	public String getResult() {		return result;	}	/**	 * @return the resultCode	 */	public String getResultCode() {		return resultCode;	}	/**	 * @return the authCode	 */	public String getAuthCode() {		return authCode;	}	/**	 * @return the alipayOpenId	 */	public String getAlipayOpenId() {		return alipayOpenId;	}}