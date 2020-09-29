package com.crawler.handler.text.impl;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class TechnicalText implements IText {

	
	private String param  ;
	
	public TechnicalText(String param) {
		this.param = param ;
		
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		 
		String[] s = text.split("\n") ;
		
		if(StringUtils.isEmpty(text)){
			try {
				web = webDriver.findElement(By.xpath("//span[contains(text(),'Best Sellers Rank')]/parent::span/parent::li")) ;
			} catch (Exception e) {
				e.printStackTrace() ;
			}
			if(web==null || StringUtils.isEmpty(web.getText())){
				return "" ;
			}
			
			text = web.getText() ;
			text = text.replace("Best Sellers Rank: ", "") ;
			s = text.split("\n") ;
		}
		
		if("1".equals(param)){
			String[] ss = s[0].split(" ") ;
			if(s.length>0){
				return  ss[0].replace("#", "").replace(",", "") ;
			}else{
				return "" ;
			}
		}
		
		if("2".equals(param)){
			if(s.length>0){
				String[] ss = s[0].split(" ") ;
				return  ss[2].replace("#", "").replace(",", "") ;
			}else{
				return "" ;
			}
		}
		
		if("3".equals(param)){
			if(s.length>1){
				String[] ss = s[1].split(" ") ;
				return  ss[0].replace("#", "").replace(",", "") ;
			}else{
				return "" ;
			}
		}
		
		if("4".equals(param)){
			if(s.length>1){
				String[] ss = s[1].split(" ") ;
				
				String last= s[1].substring(s[1].indexOf(" ")) ;
				
				return  last.replace("in","")  ;
			}else{
				return "" ;
			}
		}
		
		
		String aa = text.replaceFirst("#","");
		
		if(aa.indexOf("#")!=0 && ( "3".equals(param) || "4".equals(param)) ){
			
			try {
				aa = aa.substring(aa.indexOf("#")+1) ;
			} catch (Exception e) {
				e.printStackTrace() ;
			}
			s = aa.split(" ") ;
			
			if("3".equals(param) ){
				return  s[0].length()>0 ? s[0].replace("#", "").replace(",", "")   :"";
			}else{
				return s[0].length()>1 ?  s[2]  :"";
			}
		}
		
		return "" ;
		
		
		/*#3,654 in Automotive (See Top 100 in Automotive) 
		#3 in Cargo Racks */
		
		
	}

}
