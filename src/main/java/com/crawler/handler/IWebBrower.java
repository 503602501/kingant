package com.crawler.handler;

import java.util.List;

import com.crawler.entity.OpenUnit;
import com.crawler.entity.Storage;
import com.crawler.entity.StoreData;

public interface IWebBrower {
	
	public Storage getStorage();
	
	public void start() ;
	
	public OpenUnit getOpenUnit();
	
	public void clearStoreData();
	
	public void clearWidgets();
	
	public void addRow(List row);
	
	public List<List> getStoreDataList();
	
	public StoreData getStoreData();
	
}
