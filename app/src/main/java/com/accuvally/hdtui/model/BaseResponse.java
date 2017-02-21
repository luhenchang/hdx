package com.accuvally.hdtui.model;

import com.accuvally.hdtui.config.Config;

public class BaseResponse {
	public int code;
	public String msg;
	public String result;

	public boolean isSuccess() {
		return code == Config.RESULT_CODE_SUCCESS;
	}
	
	public String getMsg(){
		return msg;
	}

	public String getResult() {
		return result;
	}

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
