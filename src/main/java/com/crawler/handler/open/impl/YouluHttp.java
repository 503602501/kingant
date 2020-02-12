package com.crawler.handler.open.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.WebBrower;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;
import com.crawler.util.FilterUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;
import com.us.codecraft.Xsoup;

public class YouluHttp implements IOpen {

	private static final Logger logger =  Logger.getLogger(YouluHttp.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		WebDriver webDriver = driverEntity.getWebDriver();
//		boolean success =DriverUtil.navigateUrl(storage, "https://www.youlu.net/",openUnit.getTimeout());
//		if(!success){
//			return ;
//		}
//		
		String cookies = DriverUtil.getCookies(webDriver);
		if(cookies.indexOf("ShopSortType")==-1){
			storage.getEnv().showLogArea("\n错误提示:/点击价格排序!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/点击价格排序!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/点击价格排序!!!!!\n\n");
			return ;
		}
		
		//时间间隔
		Integer shouchang =StringUtils.isEmpty(storage.getEnv().getWidgets().getInputFields().get(0).getText())? 3 : Integer.parseInt(storage.getEnv().getWidgets().getInputFields().get(0).getText());
		shouchang =  shouchang*1000 ;
		
		for (Product product : storage.getInputUrlQueues()) {
			 
			 if(StringUtils.isEmpty(product.getUrl())){
				 return ;
			 }
				 
			String ss = "";
			try {
				String contents= HttpUtil.getHtmlContent( "https://www.youlu.net/search/result3/?isbn="+product.getUrl()+"&publisherName=&author=&bookName=");
				String s = FilterUtil.cutString(contents, "bookId=", "target");
				s = s.replace("\"", "").trim();
				String url = String.format("https://www.youlu.net/info3/shp.aspx?bookId=%s&rowCount=0&pageIndex=1", s) ;
				String data = HttpUtil.getHtmlContent(url,cookies+";ShopSortType=price");
				
				ss = StringEscapeUtils.unescapeHtml4(data);
				ss = ss.substring(12,ss.length()-2) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			Document doc = Jsoup.parse(ss);
			
			StringBuffer sb = new StringBuffer();
			Elements eles = Xsoup.select(doc, "//body/li").getElements();
			for (int i = 1; i <= eles.size(); i++) {
				 String shopName = Xsoup.select(doc, "//body/li["+i+"]/div/div[@class='yl-seller-name']/a/text()").get();
				 String price = Xsoup.select(doc, "//body/li["+i+"]/div[@class='t4']").get();
				 price =StringUtils.delHtml(price);
				 price = price.replace("¥", "");
				 price = StringEscapeUtils.unescapeJava(price).trim();
				 String num = Xsoup.select(doc, "//body/li["+i+"]/div/span[@class='yl-seller-store']/text()").get();
				 System.out.println(shopName+"|"+price+"|"+num);
				 sb.append(shopName+","+price+","+num+";");
				
			}
			Storage.addText("ISBN", product.getUrl(), storage);
			Storage.addText("店铺信息", sb.toString(), storage);
			
			Thread.sleep(shouchang);
		 }
		
		storage.getInputUrlQueues().clear();
	}
	
	
	
	
	 
}
