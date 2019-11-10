package com.crawler.handler.open.impl;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.JsonUtil;

public class KrUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(KrUrl.class);
	
	
	/**
	 * 
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		int count= 1;
		String cookies = "";
		WebDriver webDriver = driverEntity.getWebDriver();
		WebElement webElement =null;
		while (!storage.isInputParamQueuesEmpty()) {
			Product product = storage.getInputParamProduct();
			
			Pattern p = Pattern.compile("\\s+");
			Matcher m = p.matcher(product.getParam());
			String input = m.replaceAll(" ");
			
			String no = input.split(" ")[0];
			String password = input.split(" ")[1];
//			boolean success =DriverUtil.navigateUrl(storage, "https://login.soffice.11st.co.kr/login/Login.page?returnURL=http://soffice.11st.co.kr/marketing/SellerNewMainEmergencyAction.tmall",15000);
			boolean success =DriverUtil.navigateUrl(storage, "https://login.soffice.11st.co.kr/login/Login.page",15000);
			if(!success){  //如果失败就放回去
				storage.putInputParamProduct(product);
				continue ;
			}
			
			
			webElement = webDriver.findElement(By.xpath("//input[@id='loginName']"));
			webElement.sendKeys(no);
			Thread.sleep(100);
			  
			webElement = webDriver.findElement(By.xpath("//input[@id='passWord']"));
			webElement.sendKeys(password);
			webElement.sendKeys(Keys.ENTER);
			
			Thread.sleep(1000);
			
			String questioncnt = webDriver.findElement(By.xpath("//em[@id='prdQnaAnswerCnt']")).getText();
			
			storage.addText("账号", no, storage);
			storage.addText("留言", questioncnt, storage);
			
			cookies =DriverUtil.getCookies(webDriver);
			try {
				storage.addText("产品数量", getProductNum(cookies), storage);
				storage.addText("订单数量", getOrderGood(cookies), storage);
				count++;
			} catch (Exception e) {
				e.printStackTrace();
				if(count==1){
					webElement = webDriver.findElement(By.xpath("//li[@data-log-actionid-label='logout']"));
					webElement.click();
					break;
				}
				storage.addText("产品数量", "异常", storage);
				storage.addText("订单数量", "异常", storage);
			}
			
			
			webElement = webDriver.findElement(By.xpath("//li[@data-log-actionid-label='logout']"));
			webElement.click();
			Thread.sleep(5000);
			
			
		}
		 
	}
	
	/*
	 * 获取商品的数量
	 */
	private static String getProductNum(String cookies) throws Exception {
		String s =  HttpUtil.getHtmlContent("https://soffice.11st.co.kr/product/SellProductAction.tmall?method=getSellProductList","euc-kr",cookies);
		System.out.println(s);
		s = s.substring(s.indexOf("판매중 : "));
		s = s.substring(s.indexOf(">")+1,s.indexOf("</em>"));
		return s; 
	}
	
	
/**
 * 获取留言
 * @param cookies
 * @return
 * @throws Exception
 */
	private static String getQuestionCnt(String cookies) throws Exception {
		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/marketing/SellerMenuAction.tmall?method=getEmerMainAlertStatAjax", cookies);
		ordergood = ordergood.replace("(", "");
		ordergood = ordergood.replace(")", "");
		return JsonUtil.getJSONValue("emerStat00", ordergood);
	}
	
	
	/**
	 * 获取订单数量
	 * @param url
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	private static String getOrderGood( String cookies)
			throws Exception {
		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?isAbrdSellerYn=&isItalyAgencyYn=&listType=orderingConfirm&method=getUnapprovedOrderTotal", cookies);

		
//		String ordergood= HttpUtil.getOrderGood(cookies);
		System.out.println(ordergood);
		/*String ordergood="**";
		
		for (int i = 0; i < 3; i++) {
			try {
				String url = "https://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?method=getUnapprovedOrderTotal";
				HashMap map = new HashMap<>();
				map.put("listType", "orderingConfirm");
				map.put("isAbrdSellerYn", "");
				map.put("isItalyAgencyYn", "");
				String s =  HttpUtil.postHtmlContent(url,map,cookies);
				System.out.println(s);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep(1000);
			}
		}*/
			
		ordergood = JsonUtil.getJSONValue("unapprovedOrderTotal->order_good_202", ordergood);
		return ordergood;
	}
	

}
