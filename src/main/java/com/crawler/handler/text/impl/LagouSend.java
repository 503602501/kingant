package com.crawler.handler.text.impl;

import java.util.HashMap;


import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.log4testng.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;

/**
 * 拉勾网，发送打招呼内容
 *
 */
public class LagouSend implements IText {

	
	private static final Logger logger =  Logger.getLogger(LagouSend.class);
	
//	private static Storage storage2 ;
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		String s = storage.getLastRow().get(1);
		if(!"打招呼".equals(s)){
			return "";
		}
		
		
		StringBuffer sb = new StringBuffer();
        for (Cookie ck : webDriver.manage().getCookies()) {
        	if(sb.length()!=0){
        		sb.append(";");
        	}
        	sb.append(ck.getName() + "=" + ck.getValue());
        }
		
		String userid = storage.getLastRow().get(0);
		
		String ss = send(sb.toString(), userid);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ss;
	}
	
	public String  send(String cookies , String userid) {
	
		try {
			String positionId = getPositionId(cookies);
			return sendContent(cookies, positionId, userid);
		} catch (Exception e) {
			logger.error(e);
			logger.error(e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
//		return "异常发送" ;
	}

	/**
	 * 获取拉勾网的发送模板id
	 */
	@SuppressWarnings("unused")
	public  String getPositionId(String cookies ) throws Exception {
		String content= HttpUtil.getHtmlContent("https://easy.lagou.com/position/multiChannel/default-invite.json",cookies);
		
//		storage2.getEnv().showLogArea("\n"+content);
		
		
		JSONObject jsonObject = JSONObject.parseObject(content);
		JSONObject cont = (JSONObject) jsonObject.get("content");
		JSONObject data =  (JSONObject) cont.get("data");
		JSONObject position =  (JSONObject) data.get("position");
		String positionId = ""+position.get("positionId");
		if("DEFAULT".equals(position.get("positionCreateType"))){
			return positionId;
		}
		return "";
	}
	
	
	@SuppressWarnings("unused")
	public static String sendContent(String cookies, String positionId,String userId) throws Exception {
		HashMap map = new HashMap<>();
		map.put("cUserIds", userId);
		String content = HttpUtil.postHtmlContent("https://easy.lagou.com/im/session/greetingList.json?", map, cookies);
		
//		storage2.getEnv().showLogArea("\n"+content);
		
		JSONObject res = JSONObject.parseObject(content);
		JSONObject con = (JSONObject) res.get("content");
		JSONObject data = (JSONObject) con.get("data");
		JSONArray greetingList = (JSONArray) data.get("greetingList");
		String sendContent = "";
		
		for (Object object : greetingList) {
			JSONObject json  = (JSONObject) object ; 
			if("true".equals(""+json.get("defaults"))){
				sendContent = ""+json.get("content");
				break;
			}
		}
		
//		storage2.getEnv().showLogArea("\n"+sendContent);
		
		System.out.println(sendContent);
		
		HashMap param = new HashMap<>();
		param.put("inviteDeliver", "true");
		param.put("positionId", positionId);
		param.put("greetingContent", sendContent); //sendContent
        
		content = HttpUtil.postHtmlContent("https://easy.lagou.com/im/session/batchCreate/"+userId+".json?", param, cookies);
		
//		storage2.getEnv().showLogArea("\n"+content);
		
		System.out.println(content);
		return content ;
	}
	
	
}
