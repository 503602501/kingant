package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.open.impl.FeiGuaUrl;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;

public class FeiGuaTextUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		Integer zan = Integer.parseInt(""+storage.getLabelByIndex(0)); //24第一个通用的阀值
		Integer zan3 = Integer.parseInt(""+storage.getLabelByIndex(1)); //3天阀值
		Integer zan7 = Integer.parseInt(""+storage.getLabelByIndex(2)); //7天的阀值
		Integer zan15 = Integer.parseInt(""+storage.getLabelByIndex(3)); //15天的阀值
		Integer zan30 = Integer.parseInt(""+storage.getLabelByIndex(4)); //30天的阀值
		String cycle = ""+storage.getLastRow().get(0);
		
		if(cycle.contains("15")){
			zan = zan15 ;
		}else if(cycle.contains("30")){
			zan = zan30 ;
		}else if(cycle.contains("3天")){
			zan = zan3;
		}else if(cycle.contains("7天")){
			zan = zan7;
		}
		
		String str = storage.getLastRow().get(9);  //点赞数量
		Integer currnet = 0;
		if(str.indexOf("w")!=-1){
			str = str.replace("w", "");
			float s = Float.parseFloat(str)*10000;
			String ss = s+"";
			ss =ss.replace(".0", "");
			ss =ss.replace(".", "");
			currnet  = Integer.parseInt(ss);
		}else{
			currnet  = Integer.parseInt(str+"");
		}
		
		if(currnet>=zan){  //超过阀值，返回链接
			
			//还需要判断一下是否已经返回了存在的链接
			String id = HttpUtil.getQueryString(text, "id");
			if(!FeiGuaUrl.map.containsKey(id)){ //如果不存在就存储起来，然后返回
				FeiGuaUrl.map.put(id, text);
				return text ;
			}else{ //如果已经存在就直接返回空
				return "";
			}
		}else{
			return "";
		}
		
	 
		
	}
}
