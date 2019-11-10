package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;


public class DaTaoKeShop implements IText {

	private static final Logger logger =  Logger.getLogger(DaTaoKeShop.class);
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text =text.replace("店铺：", "");
		
		if(!StringUtils.isEmpty(text)){
			 return text;
		}

	 	List<String>  row = storage.getLastRow(); //获取最后一行
		String id = row.get(5);
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("data_tk_zs_id", id));
		
		String content = "";
		for (int i = 0; i <5; i++) {
			try {
				content = HttpUtil.postHtmlContent("http://www.dataoke.com/get_team_name",urlParameters);
			}catch (Exception e) {
				logger.error("店铺名称采集失败:"+id);
				continue;
			}
			
			if(StringUtils.isEmpty(content)){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.error("为空"+id);
			}else{
				break;
			}
		}
		
		if(StringUtils.isEmpty(content) || content.indexOf("Permission")!=-1 ){
			logger.error(content+"店铺名名称为空:"+id);
			return "";
		}
		
		JSONObject jsonObject = JSONObject.parseObject(content);
		String shopName= (String) jsonObject.get("shop_name");
		shopName =shopName.replace("店铺：", "");
		return shopName;
	}

}
