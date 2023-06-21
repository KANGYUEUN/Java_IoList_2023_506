package com.callor.iolist.service.impl;

import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import com.callor.iolist.config.AnsiConsol;
import com.callor.iolist.config.DBConnection;
import com.callor.iolist.config.HelpMessage;
import com.callor.iolist.config.Line;
import com.callor.iolist.models.BuyerDto;
import com.callor.iolist.persistance.BuyerDao;
import com.callor.iolist.service.BuyerService;
/*
 * < 객체의 선언과 초기화 >
 * 1.객체의 선언 : class 를 사용하여 객체(변수)를 선언하는것
 * 				BuyerDao buyerDao;
 * 2.객체의 초기화 : 선언된 객체를 통하여 변수, method 에 접근 할수 있도록 하는 조치
 * 				BuyerDao buyerDao = new BuyerDao(); // 생성자로 객체 초기화
 * 3.객체의 생성 : 선언되고 초기화 까지 완성된 객체, 사용할 준비가 된 상태.
 */
public class BuyerServiceImplV1 implements BuyerService {

	// Service 에서 Dao 의 method를 사용하기 위한 객체 선언.
	// 클래스 영역에 선언된 객체는 final 키워드를 필수적으로 사용하자
	// final 키워드를 사용하여 선언된 객체는 만약 초기화가 되지 않으면
	// 문법오류를 보여 반드시 초기화 하도록 독려 한다. 
	// final로 선언된 객체는 반드시 현재 클래스의 생성자에서 초기화를 해주어야 한다.
	protected BuyerDao buyerDao;
	protected Scanner scan;
	public BuyerServiceImplV1() {
		
		// new 생성자를 사용하지 않고
		// SqlSession의 getMapper()를 통하여 BuyerDao 객체를 전달받아 초기화.
		SqlSession session = DBConnection.getSessionFactory().openSession(true);
		buyerDao = session.getMapper(BuyerDao.class); 
		scan = new Scanner(System.in);
	}

	// 고객 정보 추가 
	@Override
	public void insert() {
		BuyerDto buyerDto = null;
		while(true) {
			System.out.println(Line.dLine(70));
			System.out.println("추가할 고객정보를 확인합니다.");
			System.out.println("고객 ID 를 입력 하세요. QUIT :종료, NEW :ID 생성");
			System.out.print("고객 ID >> ");
			buyerDto = this.findByBuId();
			String strBuId = scan.nextLine();
			
			if(strBuId.equals("QUIT"))return;
			if(strBuId.equals("NEW")) {
				strBuId = this.newBuId();
				//System.out.println("새로 생성된 ID : " + strBuId);
			}
			// 고객 ID 가 strBuId 에 담겨 있늕데
			// 키보드로 입력한 ID 이거나, 새로 생성한 ID 일 것이다.
			// strBuId 에 담긴 고객 Id 를 findByBuId() 에게 전달한다.
			buyerDto = this.findByBuId(strBuId);
			
			// 조회된 고객 정보가 null이면
			// 새로운 고객 정보를 저장할 Dto 를 만들고 
			// 고객ID 를 setting
			if(buyerDto == null) {
				buyerDto = new BuyerDto();
				buyerDto.buId = strBuId;
			}
			break;
		}
		// 새로운 고객이면 buyerDto = null
		// 이미 등록된 고객이면 고객정보가 출력 된다. 
		System.out.println("고객정보" + buyerDto);
		while(true) {
			String prompt = buyerDto.buName == null
					 ? "새로운 고객명 >> "
					: String.format("고객명 수정 (%s) >>", buyerDto.buName);
			System.out.print(prompt);
			String strName = scan.nextLine();
			}
	}

	@Override
	public void update() {

	}

	// BuyerDao.selectAll() 실행해서 전체 Buyer List 출력하기
	@Override
	public void printList() {
		List<BuyerDto> buyerList = buyerDao.selectAll();
		System.out.println(Line.dLine(70));
		System.out.println("우리상사 ERP - 고객리스트");
		System.out.println(Line.sLine(70));
		System.out.println("고객ID\t\t고객이름\t전화번호\t주소");
		System.out.println(Line.dLine(70));
		this.printList(buyerList);
		System.out.println(Line.dLine(70));
	}

	@Override
	public void printList(List<BuyerDto> buyerList) {
		for (BuyerDto dto : buyerList) {
			this.printBuyer(dto);
		}
	}

	@Override
	public void printBuyer(BuyerDto buyerDto) {
		System.out.printf("%s\t", buyerDto.buId);
		System.out.printf("%s\t", buyerDto.buName);
		System.out.printf("%s\t", buyerDto.buTel);
		System.out.printf("%s\n", buyerDto.buAddr);
	}

	public BuyerDto findByBuId() {
		while(true) {
			System.out.println(Line.dLine(70));
			System.out.println("고객정보조회");
			System.out.println("고객ID를 정수로 입력하세요. QUIT : 종료 ");
			System.out.print("고객ID >> ");
			String strBuId = scan.nextLine();
			if(strBuId.equals("QUIT")) return null;
			
			return this.findByBuId(strBuId);
			
		}
	}
	
	@Override
	public BuyerDto findByBuId(String strBuId) {
			try {
				int intBuId = Integer.valueOf(strBuId);
				strBuId = String.format("%010d", intBuId);
				BuyerDto buyerDto = buyerDao.findBuId(strBuId);
				if(buyerDto == null) {
					System.out.printf("없는 고객 정보 입니다.(%s)\n", strBuId);
				} else {
					System.out.println(AnsiConsol.CYAN(Line.sLine(70)));
					this.printBuyer(buyerDto);
					System.out.println(AnsiConsol.CYAN(Line.sLine(70)));
					return buyerDto;
				}
			} catch (Exception e) {
				HelpMessage.ERROR("고객ID는 정수로 입력하세요" , 
						String.format(" 입력한 고객 ID : %s", strBuId));
			} 
		return null;
	}

	@Override
	public BuyerDto findByBuName() {
		while(true) {
			System.out.println(Line.dLine(70));
			System.out.println("고객정보조회");
			System.out.println("고객의 이름을 입력 하세요. (QUIT : 종료)");
			System.out.println("고객이름 >> ");
			String strBuName = scan.nextLine();
			if(strBuName.equals("QUIT")) break;
			if(strBuName.isBlank()) {
				HelpMessage.ERROR("고객이름은 반드시 입력해야 합니다.");
				continue;
			}
			// return 한 결과는 empty 이거나 1개 이상이다.
			List<BuyerDto> buyerList = buyerDao.findByName(strBuName);
			if(buyerList == null || buyerList.isEmpty()) {
				HelpMessage.ERROR("찾는 고객이름이 없습니다.");
				continue;
			} else if(buyerList.size() < 2) {
				// 2 < : 리스트가 하나 뿐이라면.
				// 이름으로 검색한 고객의 데이터가 1개 뿐일 경우
				// ID 를 검색하는 절차가 필요 없다.
				// 검색한 1개의 데이터를 return 한다.
				return buyerList.get(0);
			}
			
			// 리스트 출력
			System.out.println(AnsiConsol.CYAN(Line.sLine(70)));
			this.printList(buyerList);
			System.out.println(AnsiConsol.CYAN(Line.sLine(70)));
			
			// 고객ID를 입력하게 하기 
			BuyerDto buyerDto = this.findByBuId();
			if(buyerDto == null) {
				HelpMessage.ERROR("고객ID를 확인하세요");
				System.out.println("고객찾기를 그만 두시겠습니까?(Y/N)");
				String yesNo = scan.nextLine();
				if(yesNo.equals("Y")) return null;
			} else {
				return buyerDto;
			}
		}
		return null;
	}

	@Override
	public BuyerDto findByBuTel() {
		return null;
	}
	
	// 자동으로 새로운 고객 ID 생성하기 
	@Override
	public String newBuId() {
		String maxId = buyerDao.getMaxId();
		if(maxId == null || maxId.isBlank()) 
			return String.format("%010d", 1);
		
		int intBuId = Integer.valueOf(maxId);
		maxId = String.format("%010d", intBuId + 1);
		return maxId;
	}
}
