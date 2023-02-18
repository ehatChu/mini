package com.kh.app.main;

import java.util.Scanner;

public class Main {
	public static final Scanner SC = new Scanner(System.in);
	public static void main(String[] args) throws Exception {
		Time t = new Time();
		t.showTimeTable();
		t.inputFee();
		//int memNum =2;
		t.getInfo();
		t.getMemberTime();
		t.earnTimePoint();
		
	}

}