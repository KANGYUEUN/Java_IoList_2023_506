package com.callor.iolist.exec;

public class IsEmptyAndIsBlank {

	public static void main(String[] args) {
		String nation1 = "";
		
		// 다른 문자열이 없이 스페이스만 있는 문자열 : " 화이트 스페이스 "
		String nation2 = "   ";
		
		System.out.println(nation1.equals("")); // true
		System.out.println(nation1.isEmpty()); // true
		
		System.out.println(nation1.isBlank()); // true
		
		System.out.println(nation2.isEmpty()); // false
		
		// java 11 에서는 isBlank 를 지원하지만
		// 이전버전은 사용 불가.
		// 이때는 문자열을 trim() 을 한번 통과시켜 화이트 스페이스 등을 제거한다.
		System.out.println(nation2.isBlank()); // true 11~
		System.out.println(nation2.trim().isEmpty()); // true 11이전
		
	}
}
