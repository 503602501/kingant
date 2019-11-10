package com.crawler.handler.open.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

public class FanaticsUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(FanaticsUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 
		
		driverEntity = storage.getDriverEntity(openUnit.getBrower(),openUnit.getImage()); //获取驱动
		WebDriver webDriver = driverEntity.getWebDriver();
		
		//打开网页
	 
		 for (Product product : storage.getInputUrlQueues()) {
		 
			try {
				
				boolean success =DriverUtil.navigateUrl(storage, product.getUrl(),openUnit.getTimeout());
				
				String name ="";
				String url = "";
				List<WebElement> webs = webDriver.findElements(By.xpath("//div[@class='color-selector-list']/a"));
				for (WebElement webElement : webs) {
					webElement.click();
					Thread.sleep(200);
					name = webDriver.findElement(By.xpath("//h1[@data-talos='labelPdpProductTitle']")).getText();
					storage.getStoreData().addText(name, "文件夹名-商品名",storage);
					List<WebElement> imgs = webDriver.findElements(By.xpath("//div[@class='thumbnails text-center']/a/img"));
					StringBuffer sb = new StringBuffer();
					for (WebElement img : imgs) {
						sb.append(img.getAttribute("src").replace("w35", "2000")+",");
					}
					
					storage.getStoreData().addText(sb.toString(), "主图片链接",storage);
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 
	}

}
