package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;

public class JdCommentNumText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		/*final String path = textUnit.getXpath();
		
		if(StringUtils.isEmpty(text)){
			WebDriverWait wait = new WebDriverWait(webDriver,10);
	        wait.until(new ExpectedCondition<WebElement>(){
	        @Override
	            public WebElement apply(WebDriver d) {
	                return d.findElement(By.xpath(path));
	        }
	       
	      });
		}*/
		
		String label = storage.getLabelByIndex(0);
		Integer defined = NumberUtil.converInteger(label);
		
		text = text.replace("+","");
		text = text.replace("万","0000");
		Integer  countInt =  NumberUtil.converInteger(text);
		if(countInt >= defined){
			return text;
		}
		
		return "";
		
	}

}
