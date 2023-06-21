package com.callor.iolist.exec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class IsDate {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("날짜 >> ");
		String strDate = scan.nextLine();

		SimpleDateFormat check = new SimpleDateFormat("yyyy-MM-dd");
		try {
			/*
			 * SimpleDateFormat 에 설정한 형식에 벗어나면,
			 * exception 이 발생되고 실제 상용되는 날짜가 아니면 엉뚱한 날짜를 생성한다.
			 * 1. exception 처리에서 날짜 format(형식)을 검사한다.
			 * 2. equals() 사용하여 정상적인 상용날짜인지 검사한다.
			 * 
			 */
			Date date = check.parse(strDate);
			// 역변환
			String checkDate = check.format(date);
			
			if(!strDate.equals(checkDate)) {
				System.out.println("날짜를 정확히 입력하세요.");
			}
			System.out.println(date.toString());
		} catch (ParseException e) {
			System.out.println("날짜 형식이 잘못 되었습니다.");

		}
	}
}
