package com.accuvally.hdtui.model;

import java.io.Serializable;

/**
 * Created by Andy Liu on 2017/5/17.
 */
public class QrCodeInfo implements Serializable {

    public int type;
    public String value;

    @Override
    public String toString() {
        return "QrCodeInfo{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
