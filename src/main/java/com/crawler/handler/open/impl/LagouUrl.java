package com.crawler.handler.open.impl;

import java.util.List;

import javax.swing.JTextField;

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
import com.crawler.util.StringUtils;

public class LagouUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(LagouUrl.class);
	
	
	/**
	 * 
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		/*WebDriver webDriver = driverEntity.getWebDriver();
		boolean success =DriverUtil.navigateUrl(storage, "https://easy.lagou.com/talent/search/list.htm?keyword=招聘&pageNo=1&city=北京&education=本科及以上&workYear=3年-10年&industryField=不限&expectSalary=13k-25k&isBigCompany=1&searchVersion=1",openUnit.getTimeout());
		
		WebElement web = webDriver.findElement(By.xpath("//li[contains(@title,'最后一页')]"));
		String pages ="1";
		if(!StringUtils.isEmpty(web.getText())){
			pages = web.getText();
		}
		
		//初始化分页数据
			
		storage.getEnv().getWidgets().setFromField(new JTextField("1"));
		storage.getEnv().getWidgets().setToField( new JTextField("3")); //new JTextField(pages));
		storage.getEnv().getWidgets().setPageParam(new JTextField("pageNo={1}"));
		
		List<Product> products = storage.getEnv().getPageUrls();
		
		storage.getInputUrlQueues().clear();
		storage.setInputUrlProduct(products);
		System.out.println(storage.getInputUrlQueues().size());*/
		
		 
        
		 
	}

}
