package com.callor.iolist.service;

import java.util.List;

import com.callor.iolist.models.BuyerDto;

public interface BuyerService {

	// 키보드에서 고객 정보를 입력 받고 Dao 통해 Insert 하기 
	public void insert();
	
	// 키보드에서 고객 정보를 입력 받고 Dao 통해 Update 하기
	public void update();
	
	// Dao 통해 SelectAll() 한 리스트를 출력하기.
	public void printList();
	
	// 매개 변수를 통해 리스트 전달받아 출력하기.
	public void printList(List<BuyerDto>buyerList);
	
	// 한 사람의 고객정보 출력하기 
	// Dto 정보를 매개변수 통해 전달받아 출력하기.
	public void printBuyer(BuyerDto buyerDto);
	
	// 고객정보 조회하기
	// ID조회하기, 이름으로 조회하기, 전화번호로 조회하기
	// 출력은 어떻게?
	
	// 고객ID 를 입력 받고, 찾은 고객 정보를 return 하기
	public BuyerDto findByBuId();
	public BuyerDto findByBuId(String buId);
	
	/*
	 * 고객 이름을 입력받고 
	 * 리스트를 보여주고 
	 * 고객 ID 를 선택하고 
	 * 
	 * 찾은 고객 정보를 return하기
	 */
	public BuyerDto findByBuName();
	
	// 전화번호로 조회하기 
	public BuyerDto findByBuTel();
	
	// 고객 ID를 자동으로 새로 생성하기
	public String newBuId();
	
}
