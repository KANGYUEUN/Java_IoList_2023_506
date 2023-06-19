package com.callor.iolist.exec;

import com.callor.iolist.service.BuyerService;
import com.callor.iolist.service.impl.BuyerServiceImplV1;

public class ExecB {

	public static void main(String[] args) {
		BuyerService buyerService = new BuyerServiceImplV1();
		buyerService.printList();
		
		//buyerService.findByBuId();
		//buyerService.findByBuName();
		//String newId = buyerService.newBuId(); // 새로운 고객 ID 추가 
		//System.out.println(newId);
		
		buyerService.insert();
	}
}
