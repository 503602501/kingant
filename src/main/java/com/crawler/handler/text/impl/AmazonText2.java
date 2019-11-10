package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class AmazonText2 implements IText {

	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 String[] li  = text.split(" ");
		  
		 if(li.length==1){
			 return "";
		 }
		 
	      if( li.length==2 ){
	    	  return li[1];
	      }
	      
	      if(li.length==3){
	    	  return li[2];
	    	  
	      }
		 
		return text;
	}

}
