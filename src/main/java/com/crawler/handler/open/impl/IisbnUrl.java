package com.crawler.handler.open.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.HttpUtil;
import com.crawler.util.JsonUtil;

public class IisbnUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(IisbnUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 
			Iterator<Product> iterator = storage.getInputUrlQueues().iterator();
			while ( iterator.hasNext()) {
				Product p = iterator.next();
				p.setUrl("http://www.iisbn.com/Search.aspx?keywords="+p.getUrl());
			}
	}

}
