package com.crawler.entity;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.crawler.util.DriverUtil;



public class SelectUnit extends UnitAdapter {
	private String range  ;	
	private static String TYPE = "click";
	private static final Logger logger =  Logger.getLogger(SelectUnit.class);
	
	public SelectUnit() {
		super(TYPE);
	}
	
	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	@Override
	protected void handler(WebDriver webDriver,String xpath,Storage storage) throws Exception {
		
		if(super.getWait()!=null){
			Thread.sleep(super.getWait());
		}
		
		if(super.getScript()!=null){
			((JavascriptExecutor)webDriver).executeScript(super.getScript());
		}
		
		WebElement webElement =DriverUtil.parseXpath(webDriver,xpath);
		List<String> range = null;
		if(this.getRange()!=null){
			range = Arrays.asList(this.getRange().split(","));
		}
		
		
		Select select = new Select(webElement);
		List<WebElement> selects =  select.getOptions();
		WebElement element = null;
		for (int i = 0; i < selects.size(); i++) {
			element=selects.get(i);
			if(!range.contains(i+1+"")){ //筛选范围内的数据
				continue;
			}
			
			element.click();
			/*if(super.getWait()!=null){
				Thread.sleep(super.getWait()*1000);
			}*/
			for (Unit unit: getChildUnit()) {
				UnitAdapter adapter = (UnitAdapter) unit;
				adapter.adapterHandler(webDriver,unit.getXpath(),storage); 
			}
			
		}
	}

}
