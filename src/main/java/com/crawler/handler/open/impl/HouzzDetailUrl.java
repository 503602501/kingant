package com.crawler.handler.open.impl;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.handler.text.impl.AutoCode;
import com.crawler.util.FilterUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

public class HouzzDetailUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(HouzzDetailUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 String img = null;
		 for (Product product : storage.getInputUrlQueues()) {
			 storage.getStoreData().addText( System.currentTimeMillis()+NumberUtil.autoNumber(), "名字",storage);
			 img = getUrl(product.getUrl());
			 img = img.replace("_4-", "_9-");
			 storage.getStoreData().addText(img, "下载图片",storage);
		}  
		
		storage.getInputUrlQueues().clear();
	}

	public String  getUrl(String url) {
		String contents = null;
		for (int i = 0; i < 3; i++) {
			try {
				contents = HttpUtil.getHtmlContent(url);
				contents = contents.substring(contents.lastIndexOf("view-photo-visitor__image-area"));
				contents = contents.substring(contents.indexOf("src="),contents.indexOf("jpg")+3);
				contents = contents.replace("src=\"", "");
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return contents ;
		
	}
}
