package com.crawler.handler.open.impl;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;

public class HouzzUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(HouzzUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 
	  /*	Iterator<Product> iterator = storage.getInputUrlQueues().iterator();
		Product p = null;
		storage.getInputUrlQueues().clear();
		
		for (int i = 0; i < 1174; i++) {
			 p = new Product() ;
			 p.setUrl("https://www.houzz.com/photos/straight-staircase-ideas-phbr1-bp~t_745~a_13-453?fi="+i*18);
			 storage.getInputUrlQueues().add(p);
		}  */
	}

}
