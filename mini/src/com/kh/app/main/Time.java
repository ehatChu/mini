package com.kh.app.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Time {
	public static int feeNum; //어디서든 접근 가능한 요금제 입력받은 번호임.
	public static int timeMin; //적립시간(분기준)
	public static int loginMemNum=3; //실제로는 로그인 시 받아올 숫자임. 해당 메소드를 실행시키기위한 임시 번호 부여
	public void showTimeTable() throws Exception {
		Connection conn = JdbcTemplate.getConnection();
		System.out.println("=========피시방 요금===========");
		
		
		String sql = "SELECT * FROM TIME";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			System.out.println(rs.getString("T_NUM")+".가격:"+rs.getString("T_PRICE")+"원,시간:"+rs.getString("T_TIME")+"분");
		}
		
		
		//클로즈
		conn.close();
	}
	
	//요금제 입력받기
	public void inputFee(){
		System.out.print("요금제를 선택하세요.:");
		String feeInput = Main.SC.nextLine();
		feeNum = Integer.parseInt(feeInput);
	}	
	
	//feeNum에 따른 금액 및 시간정보 받아오기
	//100분, 2000원 결제 하시겠습니까? 결제안전장치기능
	public void getInfo() throws Exception {
		//커넥션 받아오기
		Connection conn = JdbcTemplate.getConnection();
		
		String sql = "SELECT * FROM TIME WHERE T_NUM = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, feeNum);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		
		int timeMin = rs.getInt("T_TIME");
		int timePrice = rs.getInt("T_PRICE");
		
		System.out.print(timeMin+"분, "+timePrice+"원 결제 하시겠습니까? (y/n)");
		String input = Main.SC.nextLine();
		if(input.equalsIgnoreCase("y")) {
			addPayList();
		}else if(input.equalsIgnoreCase("n")) {
			System.out.println("결제가 취소되었습니다. 이전 화면으로 돌아갑니다.");
			//로그인 시 보여지는 메뉴화면으로 돌아가는 메소드 호출. or 시간권 보여주는 화면 
		}else {
			System.out.println("잘못입력하셨습니다. 이전 화면으로 돌아갑니다.");
			//메뉴화면 or 시간권화면 둘중 어느 단계로 돌아가는게 맞을까요?
			
		}
		
	}
	
	
	
	//시간권 결제내역테이블에 결제 내역 추가하기
	public void addPayList() throws Exception {
		//커넥션 받아오기
		Connection conn = JdbcTemplate.getConnection();
		//SQL문작성 : 결제내역 인서트 
		//feeNum은 static 멤버변수로 선언하여 어디서든 이용가능하게끔 
		String sql= "INSERT INTO TIME_PAYMENT VALUES (TP_NUM.NEXTVAL,?,SYSDATE,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, feeNum);
		pstmt.setInt(2, loginMemNum);
		
		int result = pstmt.executeUpdate();
		
		if(result ==1) {
			System.out.println("결제가 완료되었습니다.");
		}else {
			System.out.println("결제 실패. 전단계로 돌아갑니다.");
			//돌아가는 코드 작성해야함.
		}
		
		//커넥션 종료
		conn.close();
	}
	//남은시간 가져오기
	public void getMemberTime() throws Exception {
		//SQL문 작성 : 회원정보에서 적립시간 가져오기
		String sql = "SELECT MEM_TIME FROM MEMBER WHERE MEM_NUM = ? ";
		
		//커넥션 연결
		Connection conn = JdbcTemplate.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, loginMemNum);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		timeMin = rs.getInt("MEM_TIME");
		
  		 conn.close();
	}
	
	//추가시간 적립하기 
	//memNum이라는 값을 받아오게 만들었음. 
	public void earnTimePoint() throws Exception {
		Connection conn = JdbcTemplate.getConnection();
		
		//SQL 결제시간 DB에서 가져오기 TIME테이블의 T_PRICE가져오기
		String sql01 = "SELECT T_TIME FROM TIME WHERE T_NUM =?";
		PreparedStatement pstmt01 =conn.prepareStatement(sql01);
		pstmt01.setInt(1, feeNum);
		ResultSet rs01 = pstmt01.executeQuery();
		rs01.next();
		int resultMin = rs01.getInt("T_TIME");
		timeMin+=resultMin;
		
		//SQL문 작성(update) : 적립시간에 결제시간 더한 값 회원정보에 업데이트하기 
		String sql02 ="UPDATE MEMBER SET MEM_TIME = ? WHERE MEM_NUM = ?";
		PreparedStatement pstmt02 = conn.prepareStatement(sql02);
		pstmt02.setInt(1, resultMin);
		pstmt02.setInt(2, loginMemNum);
		int result = pstmt02.executeUpdate();
		if(result == 1) {
			System.out.println(resultMin+"분이 추가되었습니다.");
		}else {
			System.out.println("오류발생"+result);
		}
		
		
		conn.close();
	}
	
	
}