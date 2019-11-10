package com.crawler.handler.open.impl;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;

public class Open1688Sku implements IOpen {

	private static final Logger logger =  Logger.getLogger(Open1688Sku.class);
	/*
	 * 特殊处理1688的行数据
	 * * 特殊处理1688的链接，pc端的地址为：https://detail.1688.com/offer/534566792341.html?spm=b26110380.sw1688.mof001.243.M6kaRa
     * 转成手机端的地址：https://m.1688.com/offer/534566792341.html?spm=b26110380.sw1688.mof001.243.M6kaRa
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		//驱动队列中提取浏览器
		 for (Product product : storage.getInputUrlQueues()) {
			 product.setUrl(product.getUrl().replace("//detail.1688.com/", "//m.1688.com/"));
		 }
	}
}
