package com.accuvally.hdtui.model;

import java.io.Serializable;

public class DetailsTicketInfo implements Serializable {

	public int SN;

	/**
	 * -- 票种状态，有5种：
       0 表示未开始购买
       1 表示购买时间已截止
       2 表示已暂停销售
       3 表示有效期已过
       4 表示可报名
	 */
	public int Status;

	public double Price;

	public String Currency;

	public String Title;

	public int Quantity;

	public int SoldNum;

	public int BookNum;

	public String BookStart;

	public String BookEnd;

	public int QuantityUnit;

	public int MinOrder;

	public int MaxOrder;

	public boolean NeedApply;

	public String EffectDate;

	public String ExpiredDate;

	public String Group;

	public boolean Enabled;

	public String StatusStr;

	public String PriceStr;

	public String Desc;
	public boolean HadReg;
	public String   Msg ;

//	public String Description;//原价1328元，20张免费票

	public boolean isHadReg() {
		return HadReg;
	}

	public void setHadReg(boolean hadReg) {
		HadReg = hadReg;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public int getSN() {
		return SN;
	}

	public void setSN(int sN) {
		SN = sN;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public int getSoldNum() {
		return SoldNum;
	}

	public void setSoldNum(int soldNum) {
		SoldNum = soldNum;
	}

	public int getBookNum() {
		return BookNum;
	}

	public void setBookNum(int bookNum) {
		BookNum = bookNum;
	}

	public String getBookStart() {
		return BookStart;
	}

	public void setBookStart(String bookStart) {
		BookStart = bookStart;
	}

	public String getBookEnd() {
		return BookEnd;
	}

	public void setBookEnd(String bookEnd) {
		BookEnd = bookEnd;
	}

	public int getQuantityUnit() {
		return QuantityUnit;
	}

	public void setQuantityUnit(int quantityUnit) {
		QuantityUnit = quantityUnit;
	}

	public int getMinOrder() {
		return MinOrder;
	}

	public void setMinOrder(int minOrder) {
		MinOrder = minOrder;
	}

	public int getMaxOrder() {
		return MaxOrder;
	}

	public void setMaxOrder(int maxOrder) {
		MaxOrder = maxOrder;
	}

	public boolean isNeedApply() {
		return NeedApply;
	}

	public void setNeedApply(boolean needApply) {
		NeedApply = needApply;
	}

	public String getEffectDate() {
		return EffectDate;
	}

	public void setEffectDate(String effectDate) {
		EffectDate = effectDate;
	}

	public String getExpiredDate() {
		return ExpiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		ExpiredDate = expiredDate;
	}

	public String getGroup() {
		return Group;
	}

	public void setGroup(String group) {
		Group = group;
	}

	public boolean isEnabled() {
		return Enabled;
	}

	public void setEnabled(boolean enabled) {
		Enabled = enabled;
	}

	public String getStatusStr() {
		return StatusStr;
	}

	public void setStatusStr(String statusStr) {
		StatusStr = statusStr;
	}

	public String getPriceStr() {
		return PriceStr;
	}

	public void setPriceStr(String priceStr) {
		PriceStr = priceStr;
	}

}
