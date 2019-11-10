package com.crawler.handler.text.impl;

import java.net.URLEncoder;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;
import com.google.gson.JsonObject;

public class TaoBaoMobileDetailHttp implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		 
		String crrenturl = webDriver.getCurrentUrl();
		String id =crrenturl.substring(crrenturl.indexOf("id=")+3);
		
		String sign = storage.getLabelByIndex(0); //sign签名，手工输入,目前还没破解他啊
		if(StringUtils.isEmpty(sign) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入sign签名!!!!!\n\n");
			return "错误提示:/请输入sign签名"; 
		}
		
		
		StringBuffer sb = new StringBuffer();
		JSONObject jsonObject =null;
		try {
			
			for (int i = 0; i <100; i++) {
				String url = getTaoBaoProductUrl(id,sign);
				String content = HttpUtil.getHtmlContent(url);
				jsonObject = JSONObject.parseObject(content);
				String succ = ""+jsonObject.get("ret");
				if(succ.indexOf("SUCCESS")==-1){
					storage.getEnv().showLogArea("\n错误提示:/sign签名请求失败,请输入新的sign签名!!!!!\n\n");
					Thread.sleep(10000);
					sign = storage.getLabelByIndex(0); //sign签名，手工输入,目前还没破解他啊
				}else{
					break;
				}
			}
			
			/*************************数据解析过程****************************/
			JSONObject data = (JSONObject) jsonObject.get("data");
			JSONObject skuBase = (JSONObject) data.get("skuBase");
			JSONArray jsonArray = (JSONArray) skuBase.get("props");
			JSONObject size = (JSONObject) jsonArray.get(0);
			JSONArray sizeArray  = (JSONArray) size.get("values");
			for (Object object : sizeArray) {
				sb.append( ((JSONObject)object).get("name")+",");
			}
			
			sb.append("|");
			JSONObject color = (JSONObject) jsonArray.get(1);
			JSONArray colorArray  = (JSONArray)  color.get("values");
			for (Object object : colorArray) {
				sb.append(((JSONObject)object).get("name")+",");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return sb.toString();
		
	}

	public static void main(String[] args) {
		String url = "https://h5.m.taobao.com/awp/core/detail.htm?spm=a1z10.5-c-s.w4002-21086417433.55.22fb5e45EIqmcr&id=596530967262";
		String id =url.substring(url.indexOf("id=")+3);
		System.out.println(id);
	}
	
	public static  String getTaoBaoProductUrl(String id,String sign){
		JsonObject json = new JsonObject();  //586451114773
		json.addProperty ("id", id);
		json.addProperty("itemNumId", id);
		json.addProperty("exParams", String.format("{\"id\":\"%s\"}",id));
		return "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&sign="+sign+"&data="+URLEncoder.encode(json.toString());
	}
	
}
