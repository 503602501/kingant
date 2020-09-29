package com.crawler.handler.open.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;

public class TechnicalUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(TechnicalUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 String img = null;
		 for (Product product : storage.getInputUrlQueues()) {
			 if(StringUtils.isEmpty(product.getUrl())){
				 continue ;
			 }
			 product.setUrl("https://www.amazon.com/dp/"+product.getUrl().trim() ) ;
		}  
		
	}

	 
}
