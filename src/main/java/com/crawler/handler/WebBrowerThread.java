package com.crawler.handler;

import org.apache.log4j.Logger;

import com.crawler.entity.WebBrowerWrap;

/*
 * 浏览器定时采集
 */
public class WebBrowerThread implements Runnable{

	private WebBrowerWrap webBrowerWrap;
	private static final Logger logger =  Logger.getLogger(WebBrowerThread.class);
 
	public WebBrowerThread(WebBrowerWrap webBrowerWrap) {
		this.webBrowerWrap=webBrowerWrap;
	}

	@Override
	public void run() {
		logger.info("*************************************************************定时运行*************************************************************");
		webBrowerWrap.start();
	}
	
}
