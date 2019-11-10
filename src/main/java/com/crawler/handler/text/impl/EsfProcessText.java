package com.crawler.handler.text.impl;



import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.DriverUtil;

public class EsfProcessText implements IText {

	private static final Logger logger =  Logger.getLogger(EsfProcessText.class);
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		WebElement process =null;
		String content  ="异常";
		try {
			
			if("提示".equals(text)){
				process = DriverUtil.parseXpath(webDriver, "//div[contains(@class,'aui_content')]");
				return  process.getText();
			}
			
			try {
				WebElement iframe =  webDriver.findElement(By.xpath("//iframe[@name='OpenmyDialog']"));
				webDriver.switchTo().frame(iframe);
				try {
					process = webDriver.findElement(By.xpath("//div[@class='step clearfix']/span[@class='on']/i[1]"));
				} catch (Exception e) {
					process = webDriver.findElement(By.xpath("//div[@class='error']"));
				}
				
				content = process.getText();
				webDriver.switchTo().defaultContent();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return content ;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return "";
		//*********************特殊的客户处理：一休查询码的特殊逻辑，
		/*if(this.getExists()!=null){  
			try {
				web = DriverUtil.parseXpath(webDriver, "//span[@class='aui_title']");
			} catch (Exception e) {
				logger.error("不存在元素:"+e.getMessage());
				return ; 
			}
			content=web.getText();
			if("提示".equals(content)){
				web = DriverUtil.parseXpath(webDriver, "//div[contains(@class,'aui_content')]");
				content = web.getText();
				storage.getStoreData().addText(content.trim(), super.getName(),storage);
				return ;
			}
		}*/
		//*********************特殊的客户处理
		
		
	}

}
