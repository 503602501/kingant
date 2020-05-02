package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class SycmKeyText implements IText {

	 
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		  
		 WebElement element = webDriver.findElement(By.xpath("//input[@class='ant-input']"));
		 String input = element.getAttribute("value") ; 
		 
//		 WebElement keyElement = webDriver.findElement(By.xpath(textUnit.getCurrentXpath()));
	
		 List<String> list =storage.getLastRow() ;
		 
		 
		 if( list.get(0).equals(text)){
			 storage.setPageCount(0);
			 return text ;
		 }else{
			 
			/* List<String> list = storage.getStoreData().getList().get(storage.getStoreData().getList().size()-2);  //最后第二
			 if(list.get(0).equals(list.get(1))){
				 storage.setPageCount(0);
			 }*/
			 
			 if(storage.getPageCount()==0){
				return "" ; 
			 }
			 
			 WebElement eleme=null;
			try {
				eleme = webDriver.findElement(By.xpath("//li[contains(@class,'ant-pagination-item-active')]"));
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
					eleme = webDriver.findElement(By.xpath("//li[contains(@class,'ant-pagination-item-active')]"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			 
			//没有分页参数
			if(eleme==null){
				return "" ;
			}
			
			if(new Integer(eleme.getAttribute("title"))>=4){
				 storage.setPageCount(0); 
			} else if(storage.getPageCount()!=0 && storage.getPageCount()<3){
				 storage.setPageCount(storage.getPageCount()+1);
			}
			 
		 }
		 return "" ;
	}

}
