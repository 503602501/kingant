package com.crawler.handler.open.impl;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.entity.StoreData;
import com.crawler.entity.Vcard;
import com.crawler.handler.open.IOpen;
import com.crawler.util.ExcelUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.StringUtils;

/*
 * 手机号码转换vcard处理类
 */
public class TianmaoListUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(TianmaoListUrl.class);

	/*
	 * 人工天猫源码采集
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		Product product = storage.getInputUrlQueues().poll();
		String path = product.getUrl();  //excel的路径
		
		File[] files =  FolderUtil.getFolderFiles(new File(path));
		
		//处理每个html文件的所有链接
		String html = "";
		for (File file : files) {
			html = FolderUtil.readFileContent(file);
			String[] s = html.split("https://detail.tmall.com/item.htm");
			for (String string : s) {
				if(string.startsWith("?id=")){
					string = string.substring(0,string.indexOf("&amp;"));
					storage.getStoreData().addText("https://detail.tmall.com/item.htm"+string, "链接",storage);
				}
			}
		}
	}
 
}
