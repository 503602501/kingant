package com.crawler.entity;

import java.util.List;

/*
 *主线程收集的产品信息
 */
public class Product {

	private String url ;
	private String param ;
	private List<String> list;  //行数据
	private List<String> header;  //头部
	private Integer threshold;
	
	private Integer downloadIndex ; //下载的位置
	
	public Product() {
		this.threshold=0;
	}
	
	public List<String> getHeader() {
		return header;
	}
	public void setHeader(List<String> header) {
		this.header = header;
	}


	public String getUrl() {
		if(url==null){
			return "";
		}
		return url.trim();
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setAddThreshold() {
		this.threshold +=1 ;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getDownloadIndex() {
		return downloadIndex;
	}

	public void setDownloadIndex(Integer downloadIndex) {
		this.downloadIndex = downloadIndex;
	} 
	
}
