package com.callor.iolist.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import com.callor.iolist.config.DBConnection;
import com.callor.iolist.config.HelpMessage;
import com.callor.iolist.config.Line;
import com.callor.iolist.models.BuyerDto;
import com.callor.iolist.models.IolistDto;
import com.callor.iolist.models.ProductDto;
import com.callor.iolist.persistance.BuyerDao;
import com.callor.iolist.persistance.IolistDao;
import com.callor.iolist.persistance.ProductDao;
import com.callor.iolist.service.BuyerService;
import com.callor.iolist.service.IolistService;
import com.callor.iolist.service.ProductService;

public class IolistServiceImplV1 implements IolistService {

	/*
	 * Iolist 와 Buyer, Product 기능을 서로 연결하는것 " 의존성 주입 (의존성 연결) "
	 */
	protected final Scanner scan;

	protected final IolistDao iolistDao;
	protected final BuyerDao buyerDao;
	protected final ProductDao productDao;
	protected final BuyerService buyerService;
	protected final ProductService productService;

	protected final String DATE = "DATE";
	protected final String TIME = "TIME";

	public IolistServiceImplV1() {

		scan = new Scanner(System.in);

		SqlSession sqlSession = DBConnection.getSessionFactory().openSession(true);

		iolistDao = sqlSession.getMapper(IolistDao.class);
		buyerDao = sqlSession.getMapper(BuyerDao.class);
		productDao = sqlSession.getMapper(ProductDao.class);

		buyerService = new BuyerServiceImplV2();
		productService = new ProductServiceImplV1();
	}

	@Override
	public void printIolist(IolistDto iolist) {
		System.out.printf("%d\t", iolist.ioSEQ);
		System.out.printf("%s\t", iolist.ioDate);
		System.out.printf("%s\t", iolist.ioTime);

		System.out.printf("%s\t", iolist.ioBuId);
		System.out.printf("%s\t", buyerDao.findBuId(iolist.ioBuId).buName);

		System.out.printf("%s\t", iolist.ioPCode);
		System.out.printf("%s\t", productDao.findById(iolist.ioPCode).pName);

		System.out.printf("%d\t", iolist.getIoPrice());
		System.out.printf("%d\t", iolist.getIoQuan());
		System.out.printf("%d\n", iolist.ioTotal);
	}

	@Override
	public void printList() {
		List<IolistDto> iolists = iolistDao.selectAll();
		this.printList(iolists);
	}

	@Override
	public void printList(List<IolistDto> iolists) {
		System.out.println(Line.sLine(100));
		System.out.println("SEQ\t거래일자\t거래시각\t고객ID\t고객명\t상품코드\t상품명\t판매단가\t판매합계");
		System.out.println(Line.sLine(100));
		// for 문 이용해서 List출력
		for (IolistDto dto : iolists) {
			this.printIolist(dto);
		}
	}

	protected Map<String, String> getDateTime() {

		// 현재 날짜와 시각을 getter 하기
		// Java 1.8 이후에 사용하는 새로운 날짜 객체
		LocalDateTime localDateTime = LocalDateTime.now();

		// 날짜와 시간데이터를 문자열로 변환 하기 위한 도구
		DateTimeFormatter strDate = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		DateTimeFormatter strTime = DateTimeFormatter.ofPattern("HH-mm-ss");

		String curDate = strDate.format(localDateTime);
		String curTime = strDate.format(localDateTime);

		/*
		 * Java의 Map<?,?> class 를 사용하여 임시 Dto 객체 만들기 Map class 이용한 객체는 key, value 가 쌍으로
		 * 이루어진 데이터 객체
		 */
		// String type key, String type 의 value 를 갖는 임시 Dto 객체
		/*
		 * JS의 JSON 객체와 유사한 구조 dateTime = { DATE : curDate, TIME : curTime }
		 */
		Map<String, String> dateTime = new HashMap<>();
		dateTime.put(DATE, curDate);
		dateTime.put(TIME, curTime);

		return dateTime;
	}

	@Override
	public void input() {
		while (true) {
			// 장바구니 객체 시작
			IolistDto iolistDto = new IolistDto();

			// 현재 날짜와 시각을 세팅
			Map<String, String> dateTime = this.getDateTime();
			iolistDto.ioDate = dateTime.get(DATE);
			iolistDto.ioTime = dateTime.get(TIME);

			System.out.println(Line.dLine(70));
			System.out.println("장바구니 담기 - v1");
			System.out.println(Line.dLine(70));
			BuyerDto buyerDto = null;
			while (true) {
				// 고객의 이름을 찾고 리스트 보여줌
				buyerDto = buyerService.findByBuName();
				if (buyerDto == null) {
					HelpMessage.CONFIRM("장바구니 담기를 종료 할까요?", "Y:종료 >> ");
					String yesNo = scan.nextLine();
					if (yesNo.equalsIgnoreCase("Y"))
						return;
					else
						continue;
				}
				break;
			} // 고객 정보 조회

			HelpMessage.ALERT("조회한 고객 정보 : " + buyerDto.toString());

			iolistDto.ioBuId = buyerDto.buId;
			String message = String.format("조회한 고객코드(%s), 고객이름(%s) ", iolistDto.ioBuId, buyerDto.buName);
			HelpMessage.ALERT(message);

			ProductDto productDto = null;
			while (true) {
				productDto = productService.fintByPName();
				if (productDto == null) {
					HelpMessage.CONFIRM("장바구니 담기를 종료 할까요?", "Y:종료 >> ");
					String yesNo = scan.nextLine();
					if (yesNo.equalsIgnoreCase("Y"))
						return;
					else
						continue;
				}
				break;
			} // 상품정보 조회 end..

			HelpMessage.ALERT("조회한 상품 정보 : " + productDto.toString());

			iolistDto.ioPCode = productDto.pCode;
			message = String.format("검색한 상품코드 : %s, 상품이름 : %s", iolistDto.ioPCode, productDto.pName);
			HelpMessage.ALERT(message);

			/*
			 * 판매 단가 입력 판매단가는 기본적으로 상품정보에 등록 되어 있다. 보통은 상품정보에 등록된 판매단가를 그대로 사용하고 필요애 따라
			 * 판매단가를 변경하여 사용한다.
			 */
			while (true) {
				System.out.println(Line.sLine(70));
				System.out.println("판매단가를 입력해 주세요. QUIT : 종료");
				System.out.println("판매단가를 그대로 사용하려면 Enter");
				System.out.printf("판매단가 (%s) >> ", productDto.pOPrice);

				String strPrice = scan.nextLine();
				if (strPrice.equals("QUIT"))
					return;
				try {
					int intPrice = Integer.valueOf(strPrice);
					iolistDto.setIoPrice(intPrice);
				} catch (Exception e) {
					// 그냥 엔터나 숫자외의 문자가 눌리면 입샙션 발생
					if (strPrice.isBlank()) {
						iolistDto.setIoPrice(productDto.pOPrice);
					} else {
						HelpMessage.ERROR(String.format("판매단가는 정수로 입력하세요 (%s) ", strPrice));
						continue;
					}
				} // end try
				break;
			}
			HelpMessage.ALERT("입력한 판매단가 : " + iolistDto.getIoPrice());

			while (true) {
				System.out.println("상품 판매 수량을 입력 하세요. QUIT : 종료");
				System.out.println("수량 >> ");
				String strQuan = scan.nextLine();
				if (strQuan.equals("QUIT"))
					return;
				else if (strQuan.isBlank()) {
					HelpMessage.ERROR("수량을 입력 해야 합니다.");
					continue;
				}
				try {
					int intQuan = Integer.valueOf(strQuan);
					iolistDto.setIoQuan(intQuan);
				} catch (Exception e) {
					HelpMessage.ERROR(String.format("수량은 정수로 입력 하세요(%s)", strQuan));
					continue;
				}
				break;
			}
			HelpMessage.ALERT("입력한 수량 : " + iolistDto.getIoQuan());
			HelpMessage.ALERT("계산된 합계 : " + iolistDto.ioTotal);

			int result = iolistDao.insert(iolistDto);
			if (result > 0) {
				HelpMessage.OK("장바구니 추가 OK");
			} else {
				HelpMessage.ERROR("장바구니 추가 실패 !!");
			}

		}
	}

	
	protected String inputDate() {
		while(true) {
			System.out.println("날짜는 (YYYY-MM-DD)형식으로 입력 하세요. OUIT : 종료");
			System.out.print("날짜 입력 >> ");
			String strDate = scan.nextLine();
			if(strDate.equals("QUIT")) return null;
			else if(strDate.isBlank()) {
				HelpMessage.ERROR("날짜를 입력해 주세요");
				continue;
			}
			// 현재 날짜 확인
			SimpleDateFormat checkDate = new SimpleDateFormat("yyyy-MM-dd");
			
			Date date = new Date(System.currentTimeMillis());
			// 날짜형 데이터 문자열형 데이터로 변환
			// 2023-06-21 로 생성
			String curDate = checkDate.format(date);
			/*
			 * 날짜 형식을 갖춘 문자열형 데이터를 날짜형의 데이터로 변환
			 * ' parse() '
			 * "2023-06-21" >> 2023-06-21 로 바꿈 
			 * 이 method 는 SimpleDateFormat 에 설정된 형식 문자열(YYYY-MM-dd)에 일치하지 않으면 입샙션.
			 */
			try {
				// 문자열 -> 날짜 type 
				date = checkDate.parse(strDate);
				// 날짜 type -> 문자열로
				String result = checkDate.format(date);
				
				if(result.equals(strDate)) {
					return strDate;
				} else {
					HelpMessage.ERROR(String.format("날짜가 유효범위를 벗어났습니다.", strDate));
				}
			} catch (ParseException e) {
				HelpMessage.ERROR(String.format("날짜 입력 형식 오류(yyyy-MM-DD) : %s", strDate));
			
			}
		}
	}
	/*
	 * 기간별 거래 리스트
	 * 조회시작일자, 조회종료일자를 입력 받고 해당기간의 리스트를 출력하기.
	 * 
	 */
	@Override
	public void selectBetwenDate() {
		
		while(true) {
			System.out.println("기간별 리스트를 출력하기 위하여 날짜 입력.");
			System.out.println("조회 시작일자를 입력하세요.");
			String sDate = this.inputDate();
			if(sDate == null) return;
			
			System.out.println("조회 종료 일자를 입력하세요.");
			String eDate = this.inputDate();
			if(eDate == null) return;
			
			List<IolistDto> iolists = iolistDao.selectBetwenDate(sDate, eDate);
			this.printList(iolists);
		}
	}

	@Override
	public void selectByBuyer() {

		BuyerDto buyerDto = buyerService.findByBuName();
		if (buyerDto == null) {
			HelpMessage.ERROR("고객정보가 없습니다.");
		} else {
			List<IolistDto> iolists = iolistDao.selectByBuyer(buyerDto.buId);
			this.printList(iolists);
		}

	}

	@Override
	public void selectByProduct() {

		ProductDto productDto = productService.fintByPName();
		if (productDto == null) {
			HelpMessage.ERROR("상품 정보를 찾을수 없습니다.");
		} else {
			List<IolistDto> iolists = iolistDao.selectByProduct(productDto.pCode);
			this.printList(iolists);
		}

	}
	
	@Override
	public void selectByBuyerBetweenDate() {
		
		while(true) {
			System.out.println("데이터 조회");
			BuyerDto buyerDto = buyerService.findByBuName();
			if(buyerDto == null) return;
			System.out.println("거래 기간을 입력해 주세요");
			System.out.print("조회 시작 일자 >> ");
			String sDate = inputDate();
			if(sDate == null ) return;
			
			System.out.print("조회 종료 일자 >> ");
			String eDate = inputDate();
			if(eDate == null ) return;
			
			List<IolistDto> iolists = iolistDao.selectByBuyerBetweenDate(
										buyerDto.buId, sDate, eDate);
			this.printList(iolists);
		}
	}

	@Override
	public void selectByProductBetweenDate() {
		while(true) {
			System.out.println("데이터 조회");
			ProductDto productDto = productService.fintByPName();
			if(productDto == null) return;
			System.out.println("거래 기간을 입력해 주세요");
			System.out.print("조회 시작 일자 >> ");
			String sDate = inputDate();
			if(sDate == null ) return;
			
			System.out.print("조회 종료 일자 >> ");
			String eDate = inputDate();
			if(eDate == null ) return;
			
			List<IolistDto> iolists = iolistDao.selectByProductBetweenDate(
										productDto.pCode, sDate, eDate);
			this.printList(iolists);
		}
	}
}