package com.callor.iolist.config;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;



public class DBConnection {

	/*
	 * MyBatis 를 통해 DBMS 에 접속하는 helper class Middle Ware 라고도 한다.
	 * 
	 * Java -> JDBC -> DBMS Java -> MyBatis -> JDBC -> DBMS
	 * 
	 * MyBatis 에서 DBMS에 연결할때 Connection Pool 통해 연결한다. 미리 DBMS에 연결할 통로(Session)를 만들어
	 * 주고 필요에 따라 통로를 배정받아 SQL Query 를 보내고 데이터를 받는다.
	 */
	private static SqlSessionFactory sqlSessionFactory;
	/*
	 * < static block, static 생성자 > static 으로 선언된 변수, 객체 등을 초기화 하기 위한 코드를 작성하는 블럭.
	 * 
	 */
	static {
		
		/*
		 * 1. Data Source를 설정 : DBMS 에 Connection 하는 도구 
		 * 2. DBMS 에 연결하기 위한 환경 설정 . 
		 * 3. 설정된 환경과 Data Source 등을 연결 
		 * 4. Mapper interface(Dao) 를 설정. 
		 * 5. SessionFactory 만들기
		 */

		// PooledDataSource() method
		// 10개 정도의 DB 연결 Connection 을 만들어 pool 에 보관.
		DataSource dataSource = new PooledDataSource(DBContract.JDBC_DRIVER, DBContract.URL, DBContract.USER_NAME,
				DBContract.PASSWORD);
		// DBMS
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		
		// DBMS 연결위한 환경설정.
		Environment environment = new Environment("dev", transactionFactory, dataSource);
		
		// DataSource + TransactionFactory + 환경설정 묶어 DBMS 연결 초기화 데이터로 만들기.
		Configuration configuration = new Configuration(environment);
		// Dao interface(객체)를 MyBatis 에게 알리기
		// Dao interface 가 저장된 package 를 등록한다.
		// 개별적으로 등록 할수 있으나, 여기에서는 package를 통째로 등록한다.
		configuration.addMappers("com.callor.iolist.persistance");
		
		// SessionFactory 생성하기
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}// static block end
	
	public static SqlSessionFactory getSessionFactory() {
		return sqlSessionFactory;
		
	}
}
