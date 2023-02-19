package com.kh.app.main;

import java.util.Scanner;

import com.kh.app.time.PurchaseTime;
import com.kh.app.time.TimeData;

public class Main {
	public static final Scanner SC = new Scanner(System.in);
	public static void main(String[] args) throws Exception {
		PurchaseTime pt = new PurchaseTime();
		pt.showTimeTable();
		
	}

}