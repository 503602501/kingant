package com.crawler.entity;

public class Xpath {
	private String id ;     //唯一标识
	private String name ;   //下拉框名称
	private OpenUnit mainOpen ;     //主配置初始化后的信息
	private OpenUnit detailOpen ;   //子配置初始化后的信息
	private String setting ;//模块对应设置的配置文件存储信息
	
	public Xpath(OpenUnit openUnit) {
		this.mainOpen = openUnit;
		this.id = openUnit.getId();
		this.name=openUnit.getName();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
 
	public String getSetting() {
		return setting;
	}
	public void setSetting(String setting) {
		this.setting = setting;
	}
	public OpenUnit getMainOpen() {
		return mainOpen;
	}
	public void setMainOpen(OpenUnit mainOpen) {
		this.mainOpen = mainOpen;
	}
	public OpenUnit getDetailOpen() {
		return detailOpen;
	}
	public void setDetailOpen(OpenUnit detailOpen) {
		this.detailOpen = detailOpen;
	}
}
