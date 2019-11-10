package com.crawler.entity;

public class Item {
	private String name ;
	private Xpath xpath;
	
	public Item(Xpath xpath) {
		 this.xpath = xpath;
		this.name = xpath.getMainOpen().getName(); 
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Xpath getXpath() {
		return xpath;
	}
	public void setXpath(Xpath xpath) {
		this.xpath = xpath;
	}
}
