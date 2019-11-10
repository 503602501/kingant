package com.crawler.handler.open.impl;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.HttpUtil;

public class AmazonCompatibilityUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(AmazonCompatibilityUrl.class);
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		
		ConcurrentLinkedQueue<Product> inputUrlQueues = new ConcurrentLinkedQueue<>();
		String s ="https://www.amazon.com/gp/product/compatibility-chart/%s?ie=UTF8&i=%s";
		for (Product p : storage.getInputUrlQueues()) {
			
			String content = HttpUtil.getHtmlContent("https://www.amazon.com/gp/product/compatibility-chart/"+p.getUrl());
			content =content.substring(content.indexOf("numberofresults"),content.indexOf("Fitments"));
			
			Integer count = new Integer (content.substring(content.lastIndexOf("of")+2).trim());  //数据总量
			
			for (int i = 0; i < Math.ceil( new Double(count/25.0)); i++) {
				Product product = new Product();
				product.setUrl(String.format(s, p.getUrl(),25*i));
				inputUrlQueues.add(product);
			}
		}
		storage.setInputUrlQueues(inputUrlQueues);
		
		for (Product p : storage.getInputUrlQueues()) {
			System.out.println(p.getUrl());
		}
		System.out.println("asdf");
	}
  
}
