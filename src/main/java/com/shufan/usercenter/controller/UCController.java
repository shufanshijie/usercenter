package com.shufan.usercenter.controller;

import haiyan.common.CloseUtil;
import haiyan.common.StringUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.common.intf.web.IWebContext;
import haiyan.orm.database.DBRecord;
import haiyan.web.orm.RequestRecord;
import haiyan.web.session.WebContextFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
		try {
			PrintWriter pw = res.getWriter();
			pw.write(record.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO 返回json数据
		return new ModelAndView("userCenter.vm");
	}
	/**
	 * 根据用户微信Id更新用户资料
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "wx/addAddress/{userWXID}", method = RequestMethod.POST)
	public ModelAndView updateUserAddressByWXId(HttpServletRequest req, HttpServletResponse res,@PathVariable("userWXID")String userWXID){
		IWebContext context = null;
		BufferedReader br=null;
		try {
			context = WebContextFactory.createDBContext(req, res);
			UCDao dao = new UCDaoImpl(context);
			br = new BufferedReader(new InputStreamReader(req.getInputStream()));
			String line = null;
	        StringBuilder sb = new StringBuilder();
	        while((line = br.readLine())!=null){
	            sb.append(line);
	        }
	        JSONObject json = new JSONObject();
	        json.fromObject(sb.toString());
	        IDBResultSet result =  dao.selectAddrByWXId(userWXID,20,1);
	        IDBRecord addrRecord ;
	        if(result==null||result.getRecordCount()==0){
	        	addrRecord = new DBRecord();
	        	addrRecord.set("NAME", json.getString("NAME")+"");
	        	addrRecord.set("MOBILE", json.getLong("MOBILE")+"");
	        	addrRecord.set("COUNTY", json.getString("COUNTY")+"");
	        	addrRecord.set("ADDRESS", json.getString("ADDRESS")+"'");
	        	IDBRecord savedAddress = dao.addAddr(addrRecord);
	        	IDBRecord userRecord =dao.selectUserByWXId(userWXID);
	        	userRecord.set("DEFAULTADDRID", savedAddress.get("ID"));
	        	dao.updateUser(userRecord);
	        }
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			CloseUtil.close(context);
			CloseUtil.close(br);
		}
		return new ModelAndView("redirect:/wx/"+userWXID);
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
		IDBResultSet list = dao.selectAddrByUserId(userId, maxPageSize, pageIndex);
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
	 * 获取默认地址
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "addr/wx/{userWXID}", method = RequestMethod.GET)
	public void getAddrByWXId(HttpServletRequest req, HttpServletResponse res,@PathVariable("userWXID")String userWXID){
		IWebContext context = null;
		IDBRecord record = null;
		JSONObject json = new JSONObject();
		Writer writer = null;
		try {
			context = WebContextFactory.createDBContext(req, res);
			UCDao dao = new UCDaoImpl(context);
			record = dao.selectUserByWXId(userWXID);
			if(record==null || StringUtil.isBlankOrNull(record.get("DEFAULTADDRID"))){
				json.put("userWXID",userWXID);
			}else{
				json.put("userWXID",userWXID);
				IDBRecord addr = dao.selectAddrById((String)record.get("DEFAULTADDRID"));
				JSONObject addrJson = new JSONObject();
				addrJson.putAll(addr.getDataMap());
				json.put("addr", addrJson);
			}
			writer = res.getWriter();
			writer.write(json.toString());
			writer.flush();
		} catch (Throwable e) {
			throw new Warning(500,e);
		}finally{
			CloseUtil.close(writer);
			CloseUtil.close(context);
		}
		
	}
	/**
	 * 根据用户微信Id获取用户收货地址列表
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "addr/edit", method = RequestMethod.GET)
	public ModelAndView editAddressByUserWXId(HttpServletRequest req, HttpServletResponse res){
		ModelMap model = new ModelMap();
		return new ModelAndView("addressEdit.vm",model);
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
