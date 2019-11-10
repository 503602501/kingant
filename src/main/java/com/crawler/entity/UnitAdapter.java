package com.crawler.entity;


import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public abstract class UnitAdapter extends Unit{
	
	private static final Logger logger =  Logger.getLogger(UnitAdapter.class);
	
	public UnitAdapter(String type) {
		super(type);
	}
	

	/*
	 * 具体子类的处理逻辑
	 */
	abstract void handler( WebDriver webDriver , String xpath,Storage storage)throws Exception;
	
	/*
	 * 上层抽象处理
	 */
	public void adapterHandler( WebDriver webDriver , String xpath,Storage storage) throws Exception {
		if(storage.getEnv().isStop() || !storage.isContinue()){
//			logger.info("continue:"+storage.isContinue());
			return ;
		}
		storage.getDriverEntity().setTime(System.currentTimeMillis());
		handler(webDriver, xpath, storage);
	}
	
}
