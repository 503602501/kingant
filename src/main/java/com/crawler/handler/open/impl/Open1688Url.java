package com.crawler.handler.open.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.WebBrower;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;

public class Open1688Url implements IOpen {

	private static final Logger logger =  Logger.getLogger(Open1688Url.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		WebDriver webDriver = driverEntity.getWebDriver();
		boolean success =DriverUtil.navigateUrl(storage, openUnit.getLink(),openUnit.getTimeout());
		if(success){
			
		}
		Thread.sleep(1000);;
		WebElement element = webDriver.findElement(By.xpath("//i[@class='fui-search-submit']"));
		element.click();
		Thread.sleep(2000);;
		element=webDriver.findElement(By.xpath("//div[@class='s-type-button']"));
		element.click();
		
		Thread.sleep(1000);
		element=webDriver.findElement(By.xpath("//span[@data-type='company']"));
		element.click();
		
		element=webDriver.findElement(By.xpath("//div[@class='search-input']/input"));
		
		Product product =storage.getInputParamProduct();
		String intput = product.getParam();
		
		element.sendKeys(intput);
		element.sendKeys(Keys.ENTER);
		
		element=webDriver.findElement(By.xpath("//a[not(contains(@class,'disable'))]/div[@id='nextBtn']"));  //点击下一页
		element.click();
		
		element=webDriver.findElement(By.xpath("//span[@id='cPageSpan']/parent::div"));  //点击下一页
		Integer end = new Integer (element.getText().split("/")[1].trim());  //尾页码
		
		//https://m.1688.com/gongsi_search/-61736466.html?spm=a26g8.7662816.0.0&&beginPage=2
		String currentUrl = webDriver.getCurrentUrl(); 
		List<Product> list = new ArrayList<>();
		Product p = null;
		for (int i = 1; i <= end; i++) {
			p = new Product();
			p.setUrl(currentUrl.replace("beginPage=2", "beginPage="+i) );
			list.add(p);
		}
		storage.getInputUrlProduct(); //弹出初始化的url
		logger.info("个性化分页总数:"+list.size());
		storage.setInputUrlProduct(list);  //初始化url的所有数据
	}
	/*public static void main(String[] args) {
		System.out.println("1/100".split("/")[1]);
	}*/

}
