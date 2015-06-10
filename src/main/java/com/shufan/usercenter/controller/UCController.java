package com.shufan.usercenter.controller;

import haiyan.common.exception.Warning;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.common.intf.web.IWebContext;
import haiyan.web.orm.RequestRecord;
import haiyan.web.session.WebContextFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.shufan.usercenter.dao.UCDao;
import com.shufan.usercenter.dao.impl.UCDaoImpl;

@Controller
public class UCController {
	/**
	 * 根据用户Id获取用户资料
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "user/{userID}", method = RequestMethod.GET)
	public ModelAndView getUserCenterById(HttpServletRequest req, HttpServletResponse res,@PathVariable("userID")String userId){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBRecord record = dao.selectUserById(userId);
		ModelMap model = new ModelMap();
		model.putAll(record.getDataMap());
		return new ModelAndView("userCenter.vm",model);
	}
	/**
	 * 根据用户微信Id获取用户资料
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "wx/{userWXID}", method = RequestMethod.GET)
	public ModelAndView getUserCenterByWXId(HttpServletRequest req, HttpServletResponse res,@PathVariable("userWXID")String userWXID){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBRecord record = dao.selectUserByWXId(userWXID);
		ModelMap model = new ModelMap();
		model.putAll(record.getDataMap());
		return new ModelAndView("userCenter.vm",model);
	}
	/**
	 * 根据用户Id获取用户收货地址列表
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "addrs/user/{userID}/{pageIndex}", method = RequestMethod.GET)
	public ModelAndView getAddressListByUserId(HttpServletRequest req, HttpServletResponse res
			,@PathVariable("userID")String userId,@PathVariable("pageIndex")int pageIndex){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		String sPageSize = req.getParameter("maxPageSize");
		int maxPageSize = sPageSize == null ? 20 : Integer.parseInt(sPageSize);
		UCDao dao = new UCDaoImpl(context);
		IDBResultSet list = dao.selectAddrById(userId, maxPageSize, pageIndex);
		ModelMap model = new ModelMap();
		model.put("list", list.getRecords());
		return new ModelAndView("addrList.vm",model);
	}
	/**
	 * 根据用户微信Id获取用户收货地址列表
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "addrs/wx/{userWXID}/{pageIndex}", method = RequestMethod.GET)
	public ModelAndView getAddressListByUserWXId(HttpServletRequest req, HttpServletResponse res
			,@PathVariable("userWXID")String userWXID,@PathVariable("pageIndex")int pageIndex){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		String sPageSize = req.getParameter("maxPageSize");
		int maxPageSize = sPageSize == null ? 20 : Integer.parseInt(sPageSize);
		UCDao dao = new UCDaoImpl(context);
		IDBResultSet list = dao.selectAddrByWXId(userWXID, maxPageSize, pageIndex);
		ModelMap model = new ModelMap();
		model.put("list", list.getRecords());
		return new ModelAndView("addrList.vm",model);
	}
	/**
	 * 添加用户信息
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "user/add", method = RequestMethod.POST)
	public ModelMap addUser(HttpServletRequest req, HttpServletResponse res){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBRecord record = null;
		try {
			record = new RequestRecord(req, res, dao.getAddrTable());
			record = dao.addUser(record);
		} catch (Throwable e) {
			throw new Warning(500,e);
		}
		ModelMap model = new ModelMap();
		model.putAll(record.getDataMap());
		return model;
	}
	/**
	 * 添加用户信息
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "user/update", method = RequestMethod.POST)
	public ModelMap updateUser(HttpServletRequest req, HttpServletResponse res){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBRecord record = null;
		try {
			record = new RequestRecord(req, res, dao.getAddrTable());
			record = dao.updateUser(record);
		} catch (Throwable e) {
			throw new Warning(500,e);
		}
		ModelMap model = new ModelMap();
		model.putAll(record.getDataMap());
		return model;
	}
	/**
	 * 添加用户信息
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "user/delete", method = RequestMethod.POST)
	public ModelMap deleteUser(HttpServletRequest req, HttpServletResponse res){
		String ids = req.getParameter("IDS");
		boolean success = false;
		if(ids != null){
			IWebContext context = WebContextFactory.createDBContext(req, res);
			UCDao dao = new UCDaoImpl(context);
			success = dao.deleteUser(ids.split(","));
		}
		ModelMap model = new ModelMap();
		model.put("success",success);
		return model;
	}
	/**
	 * 添加地址信息
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "addr/add", method = RequestMethod.POST)
	public ModelMap addAddr(HttpServletRequest req, HttpServletResponse res){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBRecord record = null;
		try {
			record = new RequestRecord(req, res, dao.getAddrTable());
			record = dao.addAddr(record);
		} catch (Throwable e) {
			throw new Warning(500,e);
		}
		ModelMap model = new ModelMap();
		model.putAll(record.getDataMap());
		return model;
	}
	/**
	 * 修改地址信息
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "addr/update", method = RequestMethod.POST)
	public ModelMap updateAddr(HttpServletRequest req, HttpServletResponse res){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBRecord record = null;
		try {
			record = new RequestRecord(req, res, dao.getAddrTable());
			record = dao.updateAddr(record);
		} catch (Throwable e) {
			throw new Warning(500,e);
		}
		ModelMap model = new ModelMap();
		model.putAll(record.getDataMap());
		return model;
	}
	/**
	 * 根据地址id删除地址
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "addr/delete", method = RequestMethod.POST)
	public ModelMap deleteAddr(HttpServletRequest req, HttpServletResponse res){
		String ids = req.getParameter("ADDRIDS");
		boolean success = false;
		if(ids != null){
			IWebContext context = WebContextFactory.createDBContext(req, res);
			UCDao dao = new UCDaoImpl(context);
			success = dao.deleteAddrs(ids.split(","));
		}
		ModelMap model = new ModelMap();
		model.put("success",success);
		return model;
	}
	/**
	 * 下单获取用户信息ByUserId
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "userForm/user/{userId}", method = RequestMethod.POST)
	public void getUserBill(HttpServletRequest req, HttpServletResponse res,@PathVariable("userId")String userId){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBBill bill = dao.selectUserBillById(userId);
		//TODO 
	}
	/**
	 * 下单获取用户信息By微信Id
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "userForm/wx/{userWXId}", method = RequestMethod.POST)
	public void getUserBillWX(HttpServletRequest req, HttpServletResponse res,@PathVariable("userWXID")String userWXID){
		IWebContext context = WebContextFactory.createDBContext(req, res);
		UCDao dao = new UCDaoImpl(context);
		IDBBill bill = dao.selectUserBillByWXId(userWXID);
		//TODO 
	}
}
