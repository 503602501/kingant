package com.crawler.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.crawler.entity.OpenUnit;
import com.crawler.entity.Storage;
import com.crawler.entity.StoreData;

/*
 * 每条线程的具体实现，浏览器处理类
 */
public class WebBrower implements IWebBrower{

	private static final Logger logger =  Logger.getLogger(WebBrower.class);
	private Storage storage;
	private OpenUnit openUnit ;
	
	public WebBrower(OpenUnit openUnit,Storage storage) throws Exception {
		this.storage = storage;
		this.openUnit =openUnit; // new AnalyseXpath(xpathConfig).parseConfig();  //解析配置
	}
	
	/*
	 * 启动运行,运行完了之后不关闭浏览器驱动，等关闭的页面才退出驱动
	 */
	public void start(){
		try {
			openUnit.runSpider(storage);
		} catch (Exception e) {
			logger.error("启动异常："+e.getMessage(),e);
			e.printStackTrace();
		}
	
	}
	
	
	public Storage getStorage() {
		return storage;
	}
	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	public OpenUnit getOpenUnit() {
		return openUnit;
	}
	public void setOpenUnit(OpenUnit openUnit) {
		this.openUnit = openUnit;
	}

	/* 仓库中存放数据的list重新清理一下
	 * 页面的分页清空
	 */
	
	@Override
	public void clearStoreData() {
		getStorage().clearPageCount();
		getStorage().getStoreData().initList();
		getStorage().getErrorList().clear();
	}
	
	@Override
	public void addRow(List row) {
		getStorage().getStoreData().getList().add(row);
	}
	
	@Override
	public List<List> getStoreDataList() {
		return getStorage().getStoreData().getList() ; 
		
	}

	@Override
	public StoreData getStoreData() {
		return getStorage().getStoreData();
	}

	@Override
	public void clearWidgets() {
		getStorage().getEnv().clearTableArea();
		getStorage().getEnv().clearLogArea();
	}

	public Object clone() {   
        WebBrower o = null;   
        try {   
            o = (WebBrower) super.clone();   
        } catch (CloneNotSupportedException e) {   
            e.printStackTrace();   
        }   
        return o;   
    }   
}
