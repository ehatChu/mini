package com.kh.app.time;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.print.attribute.IntegerSyntax;

import com.kh.app.main.JdbcTemplate;
import com.kh.app.main.Main;

public final class PurchaseTime {

	public static int loginMemNum=3; //실제로는 로그인 시 받아올 숫자임. 해당 메소드를 실행시키기위한 임시 번호 부여
	
	
	public void showTimeTable(){
		try {
			Connection conn = JdbcTemplate.getConnection();
			System.out.println("=========피시방 요금===========");
		
		
			String sql = "SELECT * FROM TIME";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			//시간권 목록 보여주기
			while(rs.next()) {
				System.out.println(rs.getString("T_NUM")+".가격:"+rs.getString("T_PRICE")+"원,시간:"+rs.getString("T_TIME")+"분");
			}
			System.out.println("9.이전화면으로 돌아가기");
		
			//클로즈
			conn.close();
			
			inputFee();
		}catch(SQLException se) {
			System.out.println("DB에러 발생. DB관리자에 문의하세요.");
			System.out.println("오류상세 정보"+se.toString());
		}catch(Exception e) {
			System.out.println("커넥션 오류 발생.");
			System.out.println(e.toString());
		}
}
	
	//요금제 입력받기
	//입력받은 요금제로 데이터 변수에 저장하기
	//feeNum: 사용자로부터 입력받은 요금제 고유번호
	//timeAddMin: 요금제 번호에 맞는 요금제 시간, 추가시간같은 개념
	//timePrice: 요금제 번호에 맞는 요금제 가격
	//timeMin: 	회원테이블에서 받아온 적립시간
	public void inputFee() {
		try {
			TimeData data = new TimeData();
			System.out.print("요금제를 선택하세요.:");
			String feeInput = Main.SC.nextLine().trim();
			int inputNum = Integer.parseInt(feeInput);
			data.setFeeNum(inputNum);
				
		
			//feeNum을 받는 동시에 timeAddMin구하기 가능함
			//timeMin도 당장 받아올수있음. 세게의 변수를 한꺼번에 받아서 data 로 묶자
		
			Connection conn = JdbcTemplate.getConnection();
			//timeAddMin구하기 : 요금 결제시 고객이 선택한 추가시간임.
			String sql1 = "SELECT T_TIME, T_PRICE FROM TIME WHERE T_NUM = ?";
			PreparedStatement pstmt1 = conn.prepareStatement(sql1); //se
			pstmt1.setInt(1,data.getFeeNum()); //se
			ResultSet rs = pstmt1.executeQuery(); //se
			rs.next(); //se
			data.setTimeAddMin(rs.getInt("T_TIME"));//se
			//int timeAddMin = rs.getInt("T_TIME");
			data.setTimePrice(rs.getInt("T_PRICE")); //se
			//int timePrice = rs.getInt("T_PRICE");
	
			//timeMin받아오기 : 회원테이블의 남은 시간
			String sql2 = "SELECT MEM_TIME FROM MEMBER WHERE MEM_NUM= ?";
			PreparedStatement pstmt2 = conn.prepareStatement(sql2); //se
			pstmt2.setInt(1, loginMemNum); //se
			ResultSet rs2 = pstmt2.executeQuery(); //se
			if(rs2.next()) { //se
				data.setTimeMin(rs2.getInt("MEM_TIME")); //se
			//int timeMin = rs2.getInt("MEM_TIME");
			}
				
			
			conn.close(); //se
			//다음단계로 넘어가기
			getInfo(data);
		}catch(SQLException se) {
			System.out.println("값이 잘못 입력되었는지 확인해주세.");
			System.out.println("상세오류를 확인합니다. 관리자에게 보여주세요.");
			System.out.println(se.toString());
			System.out.println("이전 단계로 돌아갑니다.");
			showTimeTable();
			
			
		}catch(Exception e) {
			System.out.println("알수없는 오류. 관리자를 호출하세요.");
			System.out.println("상세오류를 확인합니다. 관리자에게 보여주세요.");
			System.out.println(e.toString());
			System.out.println("이전단계로 돌아갑니다.");
			//이전단계
			showTimeTable();
		}
		
		
		//timeAddMin과timeMin과feeNum을 한꺼번에 데이터로 관리하기
	
		
		
	}	
	
	//feeNum에 따른 금액 및 시간정보 받아오기
	//100분, 2000원 결제 하시겠습니까? 결제안전장치기능
	public void getInfo(TimeData data) {
		System.out.print(data.getTimeAddMin()+"분, "+data.getTimePrice()+"원 결제 하시겠습니까? (y/n)");
		String input = Main.SC.nextLine().trim();
		if(input.equalsIgnoreCase("y")) {
			System.out.println("결제가 완료되었습니다.");
			addPayList(data);
			
		}else if(input.equalsIgnoreCase("n")) {
			System.out.println("결제가 취소되었습니다. 이전 화면으로 돌아갑니다.");
			showTimeTable();
			//뒤로가기하면 시간권선택화면으로 돌아가기 
		}else {
			System.out.println("잘못입력하셨습니다. 이전 화면으로 돌아갑니다.");
			showTimeTable();
		}
		
	}
	
	
	
	//시간권 결제내역테이블에 결제 내역 추가하기
	public void addPayList(TimeData data) {
		try {
		//커넥션 받아오기
		Connection conn = JdbcTemplate.getConnection(); //Exception e 
		
		//SQL문작성 : 결제내역 인서트 
		//feeNum은 static 멤버변수로 선언하여 어디서든 이용가능하게끔 
		String sql= "INSERT INTO TIME_PAYMENT VALUES (TP_NUM.NEXTVAL,?,SYSDATE,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql); //SQLException e
		

		pstmt.setInt(1, data.getFeeNum()); //SQLException e
		
		pstmt.setInt(2, loginMemNum); //SQLException e
		
		int result = pstmt.executeUpdate(); //SQLException e
		
		if(result ==1) {
			updateTimeDate(data);
		}else {
			System.out.println("결제 실패. 전단계로 돌아갑니다.");
			showTimeTable();
			//돌아가는 코드 작성해야함.
		}
		
		//커넥션 종료
		conn.close(); //SQLException e
		}catch(SQLException se) {
			System.out.println("DB문제입니다. 이전 단계로 돌아갑니다.");
		}catch(Exception e) {
			System.out.println("커넥션오류입니다.");
		}finally { //오류와 상관없이 무조건 실행되는 구
			
		}
		
	}

	
	//추가시간 적립하기 
	//memNum이라는 값을 받아오게 만들었음. 
	public void updateTimeDate(TimeData data){
		try{
			Connection conn = JdbcTemplate.getConnection();//exception
		
		
			//SQL문 작성(update) : 적립시간에 결제시간 더한 값 회원정보에 업데이트하기 
			String sql02 ="UPDATE MEMBER SET MEM_TIME =MEM_TIME+ ? WHERE MEM_NUM = ?";
			PreparedStatement pstmt02 = conn.prepareStatement(sql02); //se
			pstmt02.setInt(1, data.getTimeAddMin()); //se
			pstmt02.setInt(2, loginMemNum); //se
			int result = pstmt02.executeUpdate(); //se
			if(result == 1) {
				System.out.println(data.getTimeAddMin()+"분이 추가되었습니다.");
				System.out.println("화면전환발생");
				//강분님께 화면 받아서 그 화면으로 돌아가기 
			}else {
				System.out.println("오류발생. 전단계로 돌아갑니다.");
				showTimeTable();
			}
			conn.close();
		
		}catch(SQLException se) {
			System.out.println("DB오류 발생. 전단계로 돌아갑니다.");
			System.out.println(se.toString());
			showTimeTable();
		}catch(Exception e) {
			System.out.println("커넥션오류발생. 전단계로 돌아갑니다.");
			System.out.println(e.toString());
			showTimeTable();
		}finally {
			
		}
	}
	
	
}