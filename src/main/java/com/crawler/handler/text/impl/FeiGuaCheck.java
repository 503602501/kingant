package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class FeiGuaCheck implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		Integer zan = Integer.parseInt(""+storage.getLabelByIndex(0)); 
		String str = storage.getLastRow().get(9);
		System.out.println(str);
		Integer currnet = 0;
		if(str.indexOf("w")!=-1){
			str = str.replace("w", "");
			float s = Float.parseFloat(str)*10000;
			String ss = s+"";
			ss =ss.replace(".0", "");
			currnet  = Integer.parseInt(ss);
		}else{
			currnet  = Integer.parseInt(str+"");
		}
		
		if(currnet>=zan){  //超过阀值，返回链接
			return "是" ;
		}else{
			return "否";
		}
		
	 
		
	}
}
