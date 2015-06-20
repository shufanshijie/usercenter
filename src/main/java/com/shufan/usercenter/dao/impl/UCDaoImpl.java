package com.shufan.usercenter.dao.impl;

import haiyan.bill.database.BillDBContextFactory;
import haiyan.bill.database.IBillDBManager;
import haiyan.bill.database.sql.IBillDBContext;
import haiyan.common.CloseUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.config.IBillConfig;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.IDBFilter;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.common.intf.session.IContext;
import haiyan.config.castorgen.Table;
import haiyan.config.util.ConfigUtil;
import haiyan.orm.database.TableDBContextFactory;
import haiyan.orm.database.sql.SQLDBFilter;
import haiyan.orm.intf.database.ITableDBManager;
import haiyan.orm.intf.session.ITableDBContext;

import java.util.ArrayList;
import java.util.List;

import com.shufan.usercenter.dao.UCDao;

public class UCDaoImpl implements UCDao {
	
	protected IContext parentContext;
	protected UCDaoImpl() {
	}
	public UCDaoImpl(IContext parentContext) {
		this.parentContext = parentContext;
	}
	public IContext getParentContext() {
		return parentContext;
	}
	private static Table userTable;
	public Table getUserTable() {
		if (userTable==null)
			synchronized(UCDaoImpl.class) {
				if (userTable==null)
					userTable = ConfigUtil.getTable("SYSOPERATOR");
			}
		return userTable;
	}
	private static Table addrTable;
	public Table getAddrTable() {
		if (addrTable==null)
			synchronized(UCDaoImpl.class) {
				if (addrTable==null)
					addrTable = ConfigUtil.getTable("T_UC_ADDRESS");
			}
		return addrTable;
	}
	private static IBillConfig userBill;
	public IBillConfig getUserBill(){
		if (userBill==null)
			synchronized(UCDaoImpl.class) {
				if (userBill==null)
					userBill = ConfigUtil.getBill("B_UC_USER");
			}
		return userBill;
	}
	
	@Override
	public IDBRecord addUser(IDBRecord userRecord) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.insert(context, getUserTable(), userRecord);
			return record;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBRecord updateUser(IDBRecord userRecord) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.update(context, getUserTable(), userRecord);
			return record;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBRecord selectUserById(String userId) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord result = dbm.select(context, getUserTable(), userId);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}
	
	@Override
	public IDBRecord selectUserByWXId(String userWXId) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			record.set("WXID", userWXId);
			IDBResultSet result = dbm.select(context, getUserTable(), record,1,1);
			if(result == null)
				return null;
			return result.getRecord(0);
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public boolean deleteUser(String[] userIds) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			List<IDBRecord> records = new ArrayList<IDBRecord>();
			for(String userId : userIds){
				IDBRecord record = dbm.createRecord();
				record.set("ID", userId);
				record.set("USED", 2);
			}
			List<IDBRecord> result = dbm.update(context, getUserTable(), records);
			if(result.size() == userIds.length)
				return true;
			else 
				return false;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBRecord addAddr(IDBRecord addrRecord) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.insert(context, getAddrTable(), addrRecord);
			return record;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBRecord updateAddr(IDBRecord addrRecord) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.update(context, getAddrTable(), addrRecord);
			return record;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBResultSet selectAddrByUserId(String userId,int maxPageSize,int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			record.set("USERID", userId);
			IDBResultSet result = dbm.select(context, getAddrTable(), record,maxPageSize,page);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}
	
	@Override
	public IDBResultSet selectAddrByWXId(String userWXId,int maxPageSize,int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			record.set("USERWXID", userWXId);
			IDBResultSet result = dbm.select(context, getAddrTable(), record,maxPageSize,page);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public boolean deleteAddrs(String[] userIds) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			boolean result = dbm.delete(context, getAddrTable(), userIds);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBBill selectUserBillById(String userId) {
		IBillDBContext context = null;
		IBillDBManager bbm = null;
		try {
			context = BillDBContextFactory.createBillDBContext(parentContext, getUserBill());
			bbm = context.getBBM();
			IDBBill bill = bbm.createBill(context, getUserBill(), false);
			bill.setBillID(userId);
			bbm.loadBill(context, bill);
			return bill;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBBill selectUserBillByWXId(String userWXId) {
		IBillDBContext context = null;
		IBillDBManager bbm = null;
		try {
			context = BillDBContextFactory.createBillDBContext(parentContext, getUserBill());
			bbm = context.getBBM();
			IDBBill bill = bbm.createBill(context, getUserBill(), false);
			IDBFilter filter = new SQLDBFilter(" and WXID = ? ",new Object[]{userWXId});
			bill.setDBFilter(1,  filter);
			bbm.loadBill(context, bill);
			return bill;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBRecord selectAddrById(String addrId) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord result = dbm.select(context, getAddrTable(), addrId);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

}
