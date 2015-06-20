package com.shufan.usercenter.dao;

import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.config.castorgen.Table;

/**
 * 用户中心数据访问对象接口
 * @author 商杰
 *
 */
public interface UCDao {
	
	public abstract IDBRecord addUser(IDBRecord userRecord);
	public abstract IDBRecord updateUser(IDBRecord userRecord);
	public abstract IDBRecord selectUserById(String userId);
	public abstract IDBRecord selectUserByWXId(String userWXId);
	public abstract boolean deleteUser(String[] userIds);
	
	public abstract IDBRecord selectAddrById(String addrId);
	public abstract IDBRecord addAddr(IDBRecord addrRecord);
	public abstract IDBRecord updateAddr(IDBRecord addrRecord);
	public abstract IDBResultSet selectAddrByUserId(String userId,int maxPageSize,int page);
	public abstract IDBResultSet selectAddrByWXId(String userWXId,int maxPageSize,int page);
	public abstract boolean deleteAddrs(String[] userIds);
	
	public abstract IDBBill selectUserBillById(String userId);
	public abstract IDBBill selectUserBillByWXId(String userWXId);
	
	public abstract Table getUserTable();
	public abstract Table getAddrTable();
}
