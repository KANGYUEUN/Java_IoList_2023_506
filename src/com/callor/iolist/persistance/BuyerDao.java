package com.callor.iolist.persistance;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.callor.iolist.config.DBContract;
import com.callor.iolist.models.BuyerDto;
import com.callor.iolist.persistance.sql.BuyerSQL;

/*
 * DBMS Query 를 보내고, 데이터를 받을 method 선언.
 * interface 에 Query method를 선언만 해두면
 * MyBatis 의 SessionFactory 가 실제 사용될 코드를 자동으로 작성한다. 
 */
public interface BuyerDao {

	@Select("SELECT * FROM" + DBContract.TABLE.BUYER)
	public List<BuyerDto> selectAll();

	// PK 를 기준으로 select 하기
	@Select("SELECT * FROM" + DBContract.TABLE.BUYER + "WHERE buId = #{id}")
	public BuyerDto findBuId(String id);

	@Insert(BuyerSQL.INSERT)
	public int insert(BuyerDto dto);
	@Insert(BuyerSQL.UPDATE)
	public int update(BuyerDto dto);

	@Delete("DELETE FROM " + DBContract.TABLE.BUYER + "WHERE buId = #{id}")
	public int delete(String id);

	/*
	 * 고객 정보 관리 추가 기능 
	 * 1. 고객 이름으로 조회하기 
	 * 2. 고객 전화번호로 조회하기
	 */
	
	@Select("SELECT * FROM" + DBContract.TABLE.BUYER + "WHERE buName LIKE '%' || #{name} || '%' ")
	public List<BuyerDto> findByName(String name);
	@Select("SELECT * FROM" + DBContract.TABLE.BUYER + "WHERE buTel LIKE '%' || #{tel} || '%' ")
	public List<BuyerDto> findByTel(String tel);
	
	@Select("SELECT max(buid) FROM " + DBContract.TABLE.BUYER)
	public String getMaxId();
	/*
	 * < select(조회) method 를 만들때 주의 사항 >
	 * 조회대상(where 절에서 사용)이 PK 일때는 조회되는 데이터가 없거나(null) 1개뿐
	 * 이때 method 의 return type 은 Dto 로 설정.
	 * SELECT * FROM tbl_buyer WHERE buId = '000001'
	 * 
	 *  조회할 대상이 PK 가 아닌경우 조회되는 데이터는 없거나(empty) 1개이상이다.
	 *  이때는 method 의 return type 은 List<Dto> 로 설정 해야 한다.
	 * 
	 */

}
