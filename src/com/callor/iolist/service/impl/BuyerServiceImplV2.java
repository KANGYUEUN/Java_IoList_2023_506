package com.callor.iolist.service.impl;

import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import com.callor.iolist.config.DBConnection;
import com.callor.iolist.config.HelpMessage;
import com.callor.iolist.config.Line;
import com.callor.iolist.models.BuyerDto;
import com.callor.iolist.persistance.BuyerDao;

public class BuyerServiceImplV2 extends BuyerServiceImplV1 {

	// BuyerDao, Scanner 객체를 V1에서 상속받아 선언한 상태.
	// V2에서 객체를 초기화 하는 생성자를 만들어야 한다.
	// < 클래스를 상속받을때 >
	// public 또는 protected로 선언한 변수와 method 는 상속을 받는다.
	// 하지만, V1클래스의 생성자는 상속을 받지 않는다. = 초기화 시키는 코드가 따라오지 않는다
	// V2에서 생성자를 새롭게 만들어서 객체들을 초기화 하는 코드를 사용 해야 한다.
	public BuyerServiceImplV2() {

		SqlSession session = DBConnection.getSessionFactory().openSession(true);
		buyerDao = session.getMapper(BuyerDao.class);
		scan = new Scanner(System.in);
	}

	// V1에서 만들어진 method를 재정의 하는것. = 다시 만들어 쓰겠다.
	@Override
	public BuyerDto findByBuId() {
		while (true) {
			System.out.println("고객ID 를 정수로 입력하세요. QUIT: 종료");
			System.out.println("고객 ID >> ");
			String strBuId = scan.nextLine();
			if (strBuId.isBlank()) {
				HelpMessage.ERROR("고객을 찾으려면 ID 를 입력해 주세요");
				continue;
			}
			try {
				// 입력받은 정수를 10자리의 고객 ID 형식으로 변환하기.
				int intBuId = Integer.valueOf(strBuId);
				strBuId = String.format("%010d", intBuId);
			} catch (Exception e) {
				HelpMessage.ERROR(String.format("고객 ID 는 정수로만 입력해야 합니다.(%s)", strBuId));
				continue;
			}
			BuyerDto buyerDto = buyerDao.findBuId(strBuId);
			if (buyerDto == null) {
				HelpMessage.ERROR(String.format("입력한 ID의 고객 정보가 없습니다.(%s)", strBuId));
				continue;
			}
			return buyerDto;
		}
	}// findByBuId() end..

	protected String inputBuId() {
		while (true) {
			System.out.println(Line.dLine(70));
			System.out.println("고객 정보를 확인합니다.");
			System.out.println("고객 ID 를 입력해 주세요. QUIT : 종료");
			System.out.print(" 고객 ID >>");

			String strBuId = scan.nextLine();
			if (strBuId.equals("QUIT"))
				return strBuId;
			if (strBuId.isBlank()) {
				HelpMessage.CONFIRM("고객 ID 가 없습니다.", "고객 ID 를 새로 생성 할까요? (Y: 생성)");
				String yesNo = scan.nextLine();
				if (yesNo.equalsIgnoreCase("Y"))
					return this.newBuId();
				else {
					HelpMessage.ERROR("고객 ID 생성 취소 ");
					continue;
				}
			}
			try {
				int intBuId = Integer.valueOf(strBuId);
				strBuId = String.format("%010d", intBuId);
				return strBuId;
			} catch (Exception e) {
				HelpMessage.ERROR(String.format("고객 ID 는 정수로만 입력해야 합니다.(%s)", strBuId));
				continue;
			}
		}
	}

	@Override
	public void insert() {
		// BuyerDto 선언만 하기
		BuyerDto buyerDto = null;

		while (true) {
			String strBuId = this.inputBuId();
			if (strBuId.equals("QUIT"))
				return;

			// 입력한 고객 ID로 고객 정보 조회하기
			buyerDto = buyerDao.findBuId(strBuId);

			// 새로운 고객 입력하기
			if (buyerDto == null) {
				buyerDto = new BuyerDto();
				buyerDto.buId = strBuId;
				
				// 이미 저장된 고객일때.
			} else if (buyerDto != null) {
				String message = String.format("등록된 고객ID 입니다.\n\t" 
							+ "고객명 : (%s)\n\t" + " 전화번호 : (%s)", buyerDto.buName,
						buyerDto.buTel);
				
				String prompt = "고객정보를 수정할까요? (Y : 수정)";
				HelpMessage.CONFIRM(message, prompt);
				
				String yesNo = scan.nextLine();
				if (!yesNo.equalsIgnoreCase("Y"))
					continue;
			}
			break;
		} // while end ..
		HelpMessage.ALERT("고객 ID 확인 : " + buyerDto.buId);

		// 고객 이름 입력하기
		while (true) {
			String prompt = buyerDto == null || buyerDto.buName == null ? "새로운 고객 이름 "
					: String.format("고객명 수정(%s): 유지하려면 Enter ", buyerDto.buName);
			System.out.println(prompt);
			String strName = scan.nextLine();

			if (strName.equals("QUIT"))
				return;
			if (buyerDto.buName == null && strName.isEmpty()) {
				HelpMessage.ERROR("고객 이름은 반드시 입력 해야 합니다.");
				continue;
			} else if (!strName.isEmpty()) {
				buyerDto.buName = strName;
			}
			break;
		} // while end..
		HelpMessage.ALERT("입력한 고객이름 확인 : " + buyerDto.buName);

		// 고객 전화번호 입력하기
		while (true) {
			String prompt = buyerDto == null || buyerDto.buTel == null ? "새로운 고객 전화번호 "
					: String.format("전화번호 수정(%s): 유지하려면 Enter ", buyerDto.buTel);
			System.out.println(prompt);
			String strTel = scan.nextLine();

			if (strTel.equals("QUIT"))
				return;
			if (buyerDto.buTel == null && strTel.isEmpty()) {
				HelpMessage.ERROR("고객 전화번호는 반드시 입력 해야 합니다.");
				continue;
			} else if (!strTel.isEmpty()) {
				buyerDto.buTel = strTel;
			}
			break;
		} // while end..
		HelpMessage.ALERT("입력한 고객 전화번호 확인 : " + buyerDto.buTel);

		// 고객 주소 입력하기
		while (true) {
			String prompt = buyerDto == null || buyerDto.buAddr == null ? "새로운 고객 주소 "
					: String.format("주소 수정(%s): 유지하려면 Enter ", buyerDto.buAddr);
			System.out.println(prompt);
			String strAddr = scan.nextLine();

			if (strAddr.equals("QUIT"))
				return;
			if (buyerDto.buTel == null && strAddr.isEmpty()) {
				HelpMessage.ERROR("고객 주소는 반드시 입력 해야 합니다.");
				continue;
			} else if (!strAddr.isEmpty()) {
				buyerDto.buTel = strAddr;
			}
			break;
		} // while end..
		HelpMessage.ALERT("입력한 고객 주소 확인 : " + buyerDto.buAddr);

		
		String okMessage = String.format("고객 ID : %s\n" 
									+ "고객이름 : %s\n" 
									+ "전화번호 : %s\n" 
									+ "주소 : %s\n", 
									buyerDto.buId,
									buyerDto.buName, 	
									buyerDto.buTel, 
									buyerDto.buAddr);
		HelpMessage.ALERT(okMessage);
		
		// insert를 하면 새로운 고객 정보는 잘되나 기존에 있는정보는 오류가 뜸
		// 이를 위해 try catch 문 사용 
		int result = 0;
		try {
			result = buyerDao.insert(buyerDto);
			if(result>0) HelpMessage.OK("데이터 추가 OK");
		} catch (Exception e) {
			result = buyerDao.update(buyerDto);
			if(result > 0 ) HelpMessage.OK("데이터 수정 OK");
		}
		if(result < 1) {
			HelpMessage.ERROR("데이터 추가, 수정 실패!");
		}
	}

}
