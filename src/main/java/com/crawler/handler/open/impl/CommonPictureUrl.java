package com.crawler.handler.open.impl;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.ImageUtil;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

public class CommonPictureUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(CommonPictureUrl.class);
	
	/*
	 * 通用的图片链接采集
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		//驱动队列中提取浏览器
//		String url = product.getUrl()
		if(storage.getStoreData().getList().isEmpty()){
			NumberUtil.resetNumber();
		}
		 for (Product product : storage.getInputUrlQueues()) {
			/* if(product.getUrl().indexOf("alipay")==-1){  //不存在，就不下载
				 return ;
			 }*/
			 if(!StringUtils.isEmpty(product.getUrl())){
				 storage.getStoreData().addText(NumberUtil.autoNumber(), "图片名称编号",storage);
				 storage.getStoreData().addText(product.getUrl(), "链接",storage);
			 }
		 }
		storage.getInputUrlQueues().clear();
	}
 
}
