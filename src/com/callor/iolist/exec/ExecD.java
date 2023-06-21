package com.callor.iolist.exec;

import com.callor.iolist.service.IolistService;
import com.callor.iolist.service.impl.IolistServiceImplV1;

public class ExecD {

	public static void main(String[] args) {
		IolistService iolistService = new IolistServiceImplV1();
		iolistService.input();
		iolistService.printList();
	}
}
