package com.crawler.handler.open.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.WebBrower;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;

public class ShopeeUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(ShopeeUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		
	 
		Iterator<Product> iterator = storage.getInputUrlQueues().iterator();
		String ids = null;
		String name = null;
		String image = null;
		while ( iterator.hasNext()) {
			Product p = iterator.next();
			ids =p.getUrl().substring( p.getUrl().lastIndexOf("-i.")+3);
			name = p.getUrl().substring(p.getUrl().lastIndexOf("/")+1,p.getUrl().indexOf("-i."));
			String content = HttpUtil.getHtmlContent("https://shopee.ph/api/v2/item/get?itemid="+ids.split("\\.")[1]+"&shopid="+ids.split("\\.")[0],"utf-8");
			JSONObject jsonObject = (JSONObject) JSONObject.parseObject(content).get("item");
			JSONArray array = (JSONArray) jsonObject.get("images");
			for (int i = 0; i < array.size(); i++) {
				 image= "https://cf.shopee.ph/file/"+array.get(i);
				 storage.getStoreData().addText(name, "文件夹名-商品名",storage);
				 storage.getStoreData().addText(System.currentTimeMillis()+i+"", "文件名",storage);
				 storage.getStoreData().addText(image, "图片链接",storage);
				 
				 //****************************//
				 
				/* storage.getStoreData().addText(name, "文件夹名-商品名",storage);
				 storage.getStoreData().addText(System.currentTimeMillis()+i*2+"", "文件名",storage);
				 storage.getStoreData().addText(image.replace("_tn", ""), "图片链接",storage);*/
			}
		}
			
		storage.getInputUrlQueues().clear();	
		 
	}


	private String  getPageUrl(String url,Integer page ) {
		
		if(url.indexOf("page=")==-1){
			url= url+"&page=1&1=1";
		}
		
		String s  =url.substring(url.indexOf("page="));
		s = s.substring(0,s.indexOf("&"));
		url = url.replace(s, "page="+page);
		return url;
	}
	 
	public static void main(String[] args) {
		String url ="https://shopee.ph/YAZI-2116-Korean-Deer-Shell-Bag-Sling-Bag-2-kinds-Fabric.-i.3256461.304929873";
		
		System.out.println( url .substring(url.lastIndexOf("/")+1,url.indexOf(".-i.")) );
	}

}
