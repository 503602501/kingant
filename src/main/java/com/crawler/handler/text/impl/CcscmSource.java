package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

/*
 * 个性推荐所有信息采集
 */
public class CcscmSource implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String html = "";
		boolean flag = true;
		/*****是否存在iframe框架******/
		try {
			WebElement iframe =  webDriver.findElement(By.xpath("//div[@class='aui_content aui_state_full']/iframe"));
			webDriver.switchTo().frame(iframe);
		} catch (Exception e) {
			flag = false;
		}
			
		//跳出iframe框架
		if(flag){
			try {
				WebElement element  = webDriver.findElement(By.xpath(textUnit.getXpath()));  //元素不存在
				html=element.getText();
			} catch (Exception e) {
				
			}
			webDriver.switchTo().defaultContent();
		}
		
		if(textUnit.getName().equals("是否有成员")){
			if(StringUtils.isEmpty(html)){
				return "否";
			}
			return "是";
		}
		
		
		return html;
	}

}
