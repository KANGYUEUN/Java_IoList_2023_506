package com.callor.iolist.exec;

import com.callor.iolist.service.BuyerService;
import com.callor.iolist.service.impl.BuyerServiceImplV2;

public class ExecC {
	
	public static void main(String[] args) {
		BuyerService buyerService = new BuyerServiceImplV2();
		// 고객 ID 정보 찾기 
		//buyerService.findByBuId();
		// 고객 ID 새로 생성하기 
		buyerService.insert();
		// 고객리스트 보여주기
		buyerService.printList();
	}
}
