package com.crawler.handler.text.impl;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;
import com.crawler.util.ImageUtil;
import com.crawler.util.JsonUtil;
import com.crawler.util.NumberUtil;


public class AliexpressHttpImage implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String url =webDriver.getCurrentUrl();
		String content=null;
		String path =null;
		try {
			content = HttpUtil.getHtmlContent(url,"");
		
			content = content.substring(content.indexOf(" data: "),content.indexOf("glo\"}}")+6);
			content = content.replace("data:", "");
			JSONArray zhutu = JsonUtil.getJSONArray("imageModule->imagePathList", content);
			for (Object object : zhutu) {
				String price = storage.getLastRow().get(2);
				price = price.replace("US $", "").replace(" ", "");
				path  =storage.getEnv().getDownloadPath();
				path = path+"\\"+storage.getLastRow().get(1)+"\\主图"+price ; // \\"+NumberUtil.autoNumber()+".jpg"  ;
				File file= new File(path);
				if(!file.exists()){
					file.mkdirs();
				}
				for (int i = 0; i < 3; i++) {
					try {
						ImageUtil.uploadImage(object+"", path+"\\"+NumberUtil.autoNumber()+".jpg");
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//颜色图片下载,如下
		JSONObject json =null;
		JSONArray prices = JsonUtil.getJSONArray("skuModule->skuPriceList", content);
		List<String> pricesList = new LinkedList<String>();
		for (int i = 0; i < prices.size(); i++) {
			json = (JSONObject) prices.get(i) ;
			json = (JSONObject) json.get("skuVal") ;
			pricesList.add(""+json.get("actSkuMultiCurrencyDisplayPrice")  );
		}
		/*
		HashMap<String, String> map= new HashMap<>();
		for (int i = 0; i < prices.size(); i++) {
			json = (JSONObject) prices.get(i) ;
			json = (JSONObject) json.get("skuVal") ;
			map.put(json.get("skuPropertyTips")+"", json.get("actSkuMultiCurrencyDisplayPrice") +"");
		}
		*/
		
		JSONArray color = JsonUtil.getJSONArray("skuModule->productSKUPropertyList", content);
		//颜色图片
		if(color!=null){
			
			json =  (JSONObject) color.get(0);
			JSONArray colorarray =(JSONArray) json.get("skuPropertyValues");
			System.out.println("********颜色图片**********");
			for (int i = 0; i < colorarray.size(); i++) {
				json = (JSONObject) colorarray.get(i) ;
				System.out.println(pricesList.get(i)+"$"+json.get("propertyValueName")+"$"+json.get("skuPropertyImagePath"));
				
				path  =storage.getEnv().getDownloadPath();
				String price = storage.getLastRow().get(2);
				
				path = path+"\\"+storage.getLastRow().get(1)+"\\颜色"+price.replace("US $", "").replace(" ", "") ; // \\"+NumberUtil.autoNumber()+".jpg"  ;
				File file= new File(path);
				if(!file.exists()){
					file.mkdirs();
				}
				
				for (int j = 0; j < 3; j++) {
					try {
						ImageUtil.uploadImage(json.get("skuPropertyImagePath")+"", path+"\\"+pricesList.get(i)+"-"+json.get("propertyValueName")+".jpg");
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		
		}
		
		StringBuffer sb = new StringBuffer();
		JSONArray array = JsonUtil.getJSONArray("specsModule->props",content);
		for (Object object : array) {
			json = (JSONObject) object;
			sb.append(json.get("attrName")+":"+json.get("attrValue")+"\n");
		}
		
		
		return sb.toString();
	}

}
