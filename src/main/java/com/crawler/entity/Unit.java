package com.crawler.entity;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;


/**
 * @author zhonghc
 *
 */
public class Unit {

	private String xpath ; 
	private Integer wait ;  //执行前等待时间
	private Integer timeout ;  //超时时间
	private String name ;
	private String type ; //page,list,url,click
	private Integer level  ; 
	private String script  ;	
	private String find  ;	
	private String attribute ; 
	private Integer count ; 
	private String accurate  ;  //如果是精确，出现异常，数据进行重新采集
	private List<Unit>  childUnit = new ArrayList<Unit>();
	
	public Unit(String type) {
		this.type = type;
	}
 
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getAccurate() {
		return accurate;
	}

	public void setAccurate(String accurate) {
		this.accurate = accurate;
	}

	public String getFind() {
		return find;
	}

	public void setFind(String find) {
		this.find = find;
	}


	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public List<Unit> getChildUnit() {
		return childUnit;
	}

	public void addChildUnit(Unit unit) {
		childUnit.add(unit);
	}

	
	public void setType(String type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getType() {
		return type;
	}
	public String getXpath() {
		return xpath;
	}
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	
	public Integer getWait() {
		return wait;
	}

	public void setWait(Integer wait) {
		this.wait = wait;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	} 
	

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}


	public void setChildUnit(List<Unit> childUnit) {
		this.childUnit = childUnit;
	}
 
	

	/*public void handler( WebDriver webDriver , String xpath,Storage storage) throws Exception{
		throw new Exception("未实现没有具体的处理行为");
	}*/
	
	/*
	 * 初始化自己的业务逻辑
	 */
	public void init() throws Exception{
		
	}

}
