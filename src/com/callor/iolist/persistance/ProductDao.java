package com.callor.iolist.persistance;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.callor.iolist.config.DBContract;
import com.callor.iolist.models.ProductDto;
import com.callor.iolist.persistance.sql.ProductSQL;

public interface ProductDao {
	
	@Select(" SELECT * FROM " 
			+ DBContract.TABLE.PRODUCT
			+ " ORDER BY pCode ")
	public List<ProductDto> selectAll();
	
	@Select(" SELECT * FROM " 
			+ DBContract.TABLE.PRODUCT
			+ " WHERE pCode = #{id}")
	public ProductDto findById(String id);
	
	@Insert(ProductSQL.INSERT)
	public int insert(ProductDto productDto);
	
	@Update(ProductSQL.UPDATE)
	public int update(ProductDto productDto);
	
	// 이름순으로 정렬해 보여주기 
	@Select(" SELECT * FROM " 
			+ DBContract.TABLE.PRODUCT
			+ " WHERE pName LIKE '%' || #{pName} || '%' "
			+ " ORDER BY pName ")
	public List<ProductDto> findByPName(String pName);
}
