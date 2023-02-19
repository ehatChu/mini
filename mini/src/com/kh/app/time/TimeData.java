package com.kh.app.time;

public class TimeData {
	private int feeNum;
	private int timeAddMin;
	private int timeMin;
	private int timePrice;
	
	public int getTimePrice() {
		return timePrice;
	}
	public void setTimePrice(int timePrice) {
		this.timePrice = timePrice;
	}
	public int getFeeNum() {
		return feeNum;
	}
	public void setFeeNum(int feeNum) {
		this.feeNum = feeNum;
	}
	public int getTimeAddMin() {
		return timeAddMin;
	}
	public void setTimeAddMin(int timeAddMin) {
		this.timeAddMin = timeAddMin;
	}
	public int getTimeMin() {
		return timeMin;
	}
	public void setTimeMin(int timeMin) {
		this.timeMin = timeMin;
	}
	public TimeData(int feeNum, int timeAddMin, int timeMin,int timePrice) {
		super();
		this.feeNum = feeNum;
		this.timeAddMin = timeAddMin;
		this.timeMin = timeMin;
		this.timePrice =timePrice;
	}
	public TimeData() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "TimeData [feeNum=" + feeNum + ", timeAddMin=" + timeAddMin + ", timeMin=" + timeMin +", timePrice"+ timePrice;
	}
	
	
}
