package com.crawler.handler.open.impl;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;

public class WinTimeUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(WinTimeUrl.class);
	
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		for( String s : storage.getStoreData().getHeader()){
			storage.getStoreData().addText("", s,storage);
		}
	}

}
