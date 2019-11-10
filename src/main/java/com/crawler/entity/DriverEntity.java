package com.crawler.entity;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.service.DriverService;

import com.crawler.util.DriverUtil;

public class DriverEntity {
	
	private String brower ; //chrome 或者phantomjs 
	private Long time ; //驱动最后运行的时间
	private WebDriver webDriver ; 
	private DriverService driverService;
	private static final Logger logger =  Logger.getLogger(DriverEntity.class);
	
	public DriverEntity(String brower) {
		this.time = System.currentTimeMillis();
		this.brower = brower;
	}
	
	public WebDriver getWebDriver() {
		return webDriver;
	}
	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	public DriverService getDriverService() {
		return driverService;
	}
	public void setDriverService(DriverService driverService) {
		this.driverService = driverService;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
 
	public boolean isChrome() {
		if(this.brower.indexOf("chrome")!=-1){
			return true;
		}
		return false;
	}
	
	
	//移除当前的驱动信息
	public void removeDirverEntity() {
		try {
			webDriver.quit();
		} catch (Exception e) {
			logger.error("驱动销毁异常："+e.getMessage());
		}
		
		try {
			driverService.stop();
		} catch (Exception e) {
			logger.error("服务销毁异常："+e.getMessage());
		}
	}
		
}
