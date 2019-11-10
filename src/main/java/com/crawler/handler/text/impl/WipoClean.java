package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class WipoClean implements IText {
	
	 

	@Override
	public String getText(String text, WebElement web, WebDriver webDriver,
			TextUnit textUnit, Storage storage) {
		
		
	 	try {
			WebElement wele = webDriver.findElement(By.xpath("//div[@role='dialog']"));
			//显示了提示框
			if(wele.isDisplayed()){
				 Thread.sleep(2000);
				 closeDialog(webDriver);  //关闭对话框
				 
				/* doSearch(webDriver);  //点击查询
				 Thread.sleep(1500);
				 */
				 //chuli t
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	 	
	 	return storage.getCurrentInput();
/*
		
	 
		try {
			WebElement wele = webDriver.findElement(By.xpath("//div[@class='searchFunctions']/a"));
			if(wele!=null){
				Thread.sleep(2000);
			}
			
			wele.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			WebElement wele = webDriver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']/button"));
			wele.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
*/

	}

	private void doSearch(WebDriver webDriver) {
		try {
			WebElement wele;
			wele = webDriver.findElement(By.xpath("//div[@class='searchButtonContainer bottom right']/a"));
			 wele.click();  //点击查询
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void closeDialog(WebDriver webDriver) {
		WebElement wele;
		try {
			wele = webDriver.findElement(By.xpath("//div[@class='ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix ui-draggable-handle']/button"));
			 wele.click();  //关闭提示框
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
