package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class WipoCheck implements IText {

	@Override
	public String getText(String text, WebElement web, WebDriver webDriver,
			TextUnit textUnit, Storage storage) {

		
		String result = "";
		WebElement wele =null;
		
		//********没有结果的情况************//
		try {
			  wele = webDriver.findElement(By.xpath("//div[@class='noMatches']"));
			  closeSearch(webDriver);
			  return  "否";
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try {
			/* WebDriverWait wait = new WebDriverWait(WebDriver, 10); // 最多等10秒
		        WebElement element = wait.until(new ExpectedCondition<WebElement>() {
		            @Override
		            public WebElement apply(WebDriver d) {
		                return d.findElement(By.xpath(path));
		            }
		        });
		        */
			  wele = webDriver.findElement(By.xpath("//div[@class='results_navigation bottom_results_navigation displayButtons']"));
			 
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			closeSearch(webDriver);
			return "否";
		}
		
		//是否加载中.........
		if(! wele.isDisplayed()){ //不显示，表示加载中。。。。。。
			 WebDriverWait wait = new WebDriverWait(webDriver, 10); // 最多等10秒
		        WebElement element = wait.until(new ExpectedCondition<WebElement>() {
		            @Override
		            public WebElement apply(WebDriver d) {
		            	WebElement wel = d.findElement(By.xpath("//div[@class='results_navigation bottom_results_navigation displayButtons']"));
		            	if(wel.isDisplayed()){
		            		return  wel ; 
		            	}
						return wel;
		            }
		        });
		        wele = webDriver.findElement(By.xpath("//div[@class='results_navigation bottom_results_navigation displayButtons']"));
		        if(!wele.isDisplayed()){  //不显示,返回异常
		        	closeSearch(webDriver);
		        	return "查询异常!";
		        }
		}
		
		
		
		/*********有结果的情况***********/
		try {
			List<WebElement> elements = webDriver.findElements(By.xpath("//table[@id='gridForsearch_pane']/tbody/tr"));
			if(elements.size()>1){
				result=  "是";
				
			}else{
				result= "否";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//清除掉查询的结果
		closeSearch(webDriver);
				
		/*
		if(StringUtils.isEmpty(result)){
			result = storage.getCurrentInput()+"-异常";
			
			try {
				WebElement wele = webDriver.findElement(By.xpath("//div[@role='dialog']"));
				//显示了提示框
				if(wele.isDisplayed()){
					 Thread.sleep(2000);
					 closeDialog(webDriver);  //关闭对话框
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}*/
		
		
	/*
		try {
			WebElement wele = webDriver.findElement(By.xpath("//div[@class='pagerPos']"));
			if (wele == null) {
				result= "否";
			} else {
				result=  "是";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
		

		return result;

	}


	private void closeSearch(WebDriver webDriver) {
		WebElement wele;
		try {
			 wele = webDriver.findElement(By.xpath("//div[@class='searchFunctions']/a"));
			wele.click();
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(1000);
				 wele = webDriver.findElement(By.xpath("//div[@class='searchFunctions']/a"));
				wele.click();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	

	private void doSearch(WebDriver webDriver) {
		try {
			WebElement wele;
			wele = webDriver.findElement(By.xpath("//div[@class='searchButtonContainer bottom right']/a"));
			 wele.click();  //点击查询
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void closeDialog(WebDriver webDriver) {
		WebElement wele;
		try {
			wele = webDriver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']/button"));
			 wele.click();  //关闭提示框
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
