package com.shufan.usercenter.test;

import haiyan.common.config.PathUtil;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.orm.database.DBRecord;

import java.io.File;
import java.util.Properties;

import com.shufan.usercenter.common.ContextListener;
import com.shufan.usercenter.dao.UCDao;
import com.shufan.usercenter.dao.impl.UCDaoImpl;

public class TestUCDao {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		String s = System.getProperty("user.dir");
		Properties p = PathUtil.getEnvVars();
		p.setProperty("HAIYAN_HOME", s+File.separator+"WebContent");
		ContextListener.init(s+File.separator+"WebContent"+File.separator+"WEB-INF");
		ContextListener.USE_ES=true; 
		
		testBill();
		
		System.exit(0);
	}

	private static void testUser() {
		UCDao dao = new UCDaoImpl(null);
		IDBRecord userRecord = new DBRecord();
		userRecord.set("WXID", "123");
		userRecord.set("NAME", "呵呵");
		userRecord.set("MOBILE", "243125415");
		userRecord = dao.addUser(userRecord);
		System.out.println(userRecord.get("ID")+" name: "+userRecord.get("NAME"));
		userRecord.set("NAME", "嘿嘿");
		userRecord = dao.updateUser(userRecord);
		System.out.println(userRecord.get("ID")+" name: "+userRecord.get("NAME"));
		userRecord = dao.selectUserByWXId("123");
		System.out.println(userRecord.get("ID")+" name: "+userRecord.get("NAME"));
		userRecord = dao.selectUserById("aaaaab");
		System.out.println(userRecord.get("ID")+" name: "+userRecord.get("NAME"));
	}
	private static void testAddr() {
		UCDao dao = new UCDaoImpl(null);
		IDBRecord addrRecord = new DBRecord();
		addrRecord.set("USERWXID", "123");
		addrRecord.set("NAME", "呵呵");
		addrRecord.set("MOBILE", "123456");
		addrRecord.set("USERID", "aaaaab");
		addrRecord.set("PROVINCE", "上海");
		addrRecord.set("CITY", "上海");
		addrRecord.set("COUNTY", "普陀区");
		addrRecord.set("ADDRESS", "中江路天洁大厦6楼");
		
		addrRecord = dao.addAddr(addrRecord);
		System.out.println(addrRecord.get("ID")+" MOBILE : "+addrRecord.get("MOBILE"));
		
		addrRecord.set("MOBILE", "654321");
		addrRecord = dao.updateAddr(addrRecord);
		System.out.println(addrRecord.get("ID")+" MOBILE: "+addrRecord.get("MOBILE"));
		
		IDBResultSet result = dao.selectAddrByWXId("123",20,1);
		if(result.getRecordCount()>0){
			System.out.println("数量： " + result.getRecordCount() + " ID: " + result.getRecord(0).get("ID"));
		}else{
			System.out.println("没有找到地址信息");
		}
	}
	private static void testBill() {
		UCDao dao = new UCDaoImpl(null);
		IDBBill bill = dao.selectUserBillById("aaaaab");
		System.out.println("BILLID : "+bill.getBillID());
		IDBResultSet userSet = bill.getResultSet(0);
		IDBResultSet addrSet = bill.getResultSet(1);
		IDBResultSet pointsSet = bill.getResultSet(2);
		if(userSet.getRecordCount()>0){
			System.out.println("数量： " + userSet.getRecordCount() + " USERNAME: " + userSet.getRecord(0).get("NAME"));
		}else{
			System.out.println("没有找到用户信息");
		}
		if(addrSet.getRecordCount()>0){
			System.out.println("数量： " + addrSet.getRecordCount() + " NAME: " + addrSet.getRecord(0).get("NAME"));
		}else{
			System.out.println("没有找到地址信息");
		}
		if(pointsSet.getRecordCount()>0){
			System.out.println("数量： " + pointsSet.getRecordCount() + " POINTS: " + pointsSet.getRecord(0).get("POINTS"));
		}else{
			System.out.println("没有找到积分信息");
		}
	}

}
