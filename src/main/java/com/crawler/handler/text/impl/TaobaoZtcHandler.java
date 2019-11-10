package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

/*
 * 淘宝直通车，业务点击处理
 */
public class TaobaoZtcHandler implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		boolean flag = false ; // 不点击确定 升价的按钮
		
		try {
			//labels:转化率大于,提高出价
			String convert = storage.getLabelByIndex(0); //"4";    //点击转化率 
			String radio = storage.getLabelByIndex(1); // 提高出价  5%
			String huafeiinput = storage.getLabelByIndex(2); //平均点击花费
			String passdate = storage.getLabelByIndex(3); //过去天数
			String scorePoint = storage.getLabelByIndex(4); //平均展现排名升价排名临界点
			
			if(StringUtils.isEmpty(huafeiinput) ){
				storage.getEnv().showLogArea("\n错误提示:/平均点击花费不能为空!!!!!\n\n");
				return "错误提示:/平均点击花费不能为空"; 
			}
			Float huafeiFloat = new Float(huafeiinput);  //平均点击花费
			
			if(StringUtils.isEmpty(convert)){
				storage.getEnv().showLogArea("\n错误提示:转化率不能为空!!!!!\n\n");
				return "错误提示:转化率不能为空"; 
			}
			Float convertFloat =Float.parseFloat(convert);
			
			if(StringUtils.isEmpty(radio) ){
				storage.getEnv().showLogArea("\n错误提示:提高升价不能为空!!!!!\n\n");
				return "错误提示:提高升价不能为"; 
			}
			
			if(!Arrays.asList("7","14","30").contains(passdate) ){
				storage.getEnv().showLogArea("\n错误提示:过去天数非法!!!!!\n\n");
				return "错误提示:过去天数非法"; 
			}
			Integer radioInteger =   Integer.parseInt(radio);
			
			if(StringUtils.isEmpty(scorePoint) ){
				storage.getEnv().showLogArea("\n错误提示:平均展现排名升价排名临界点非法!!!!!\n\n");
				return "错误提示:平均展现排名升价排名临界点非法"; 
			}
			Integer scorePointInteger = Integer.parseInt(scorePoint);
			
			Thread.sleep(3000);

			//先清理红色和蓝色的标签
			webDriver.findElement(By.xpath("//table[@id='bp-scroll-table']/thead/tr/th[1]/div/input")).click();  //全选
			webDriver.findElement(By.xpath("//span[text()='标签']")).click();
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//input[@value=1]")).click();
			Thread.sleep(1000);
			webDriver.findElement(By.xpath("//input[@value=2]")).click();
			webDriver.findElement(By.xpath("//span[text()='删除'][@style]")).click(); 
			//先清理红色和蓝色的标签 end end 
			Thread.sleep(2000);
			
			WebElement date = webDriver.findElement(By.xpath("//div[@id='J_table_time_filter']"));
			date.click();
			Thread.sleep(1500);
			
			webDriver.findElement(By.xpath("//a[text()='过去"+passdate+"天']")).click();
			
			Thread.sleep(2000);
			
			//点击转化率排序
			webDriver.findElement(By.xpath("//div[contains(text(),'点击转化率')]")).click();  
			Thread.sleep(2000);
			//获取自定义的转化率
			
			
			if(!NumberUtil.isInteger(radio) ){
				storage.getEnv().showLogArea("\n错误提示:提高升价为整数5~1500之间!!!!!\n\n");
				return "错误提示：提高升价为整数5~1500之间"; 
			}
			
			List<WebElement> list;
			Integer convertInteger = getHeaderIndex(webDriver,"点击转化率");
			
			
			//转化率大于 convertFloat
			List<Integer> set = new ArrayList();  //符合的转化率的位置
			convertInteger+=1;
			list = webDriver.findElements(By.xpath("//table[@class='bp-table']/tbody/tr[@id]/td["+convertInteger+"]"));
			for (int i = 0; i < list.size(); i++) {
				WebElement webElement = list.get(i);
				String s = webElement.getText();
				if(s.indexOf("%")!=-1 ){
					s=s.replace("%", "");
					Float f = Float.parseFloat(s);
					if(f.compareTo(convertFloat)==1 ||f.compareTo(convertFloat)==0){
						set.add(i+1);
					}
					
				}
			}
			
			
			//过去14天的通过点击转化率后排序， 转化率大于convertFloat的标红
			for (Integer integer : set) {
				webDriver.findElement(By.xpath("//table[@class='bp-table']/tbody/tr[@id]["+integer+"]/td[1]")).click();
				Thread.sleep(300);
			}
			if(set.size()==0){
				System.out.println("转化率太大啦，根本没有满足条件的啊，没办法往下运行啊！！！！！");
				return "转化率太大啦，根本没有满足条件的啊，没办法往下运行啊！！！！！"; 
			}
			webDriver.findElement(By.xpath("//span[text()='标签']")).click();
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//input[@value=2]")).click();
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//span[text()='添加']")).click();
			Thread.sleep(2000);
			//点击昨天的日期
			webDriver.findElement(By.xpath("//div[@id='J_table_time_filter']")).click();
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//a[text()='昨天']")).click();
			Thread.sleep(2000);
			
			webDriver.findElement(By.xpath("//div[contains(text(),' 平均点击花费 ')]")).click();  
			Thread.sleep(2000);
			
			//过滤出红色的标签
			webDriver.findElement(By.xpath("//table/thead/tr/th[3]/div/div/span")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul[@class='dropdown-list']/li/span[contains(text(),'优化')]")).click();
			Thread.sleep(1500);
			//过滤出红色的标签 end end 
			
			
			//大于3块的去掉标红
			//平均点击花费的所在索引位置
			list = webDriver.findElements(By.xpath("//table[@id='bp-scroll-table']/thead/tr/th"));
			Integer huafei = null; 
			huafei = getHeaderIndex(webDriver, "平均点击花费")+1;
			
			list = webDriver.findElements(By.xpath("//table[@class='bp-table']/tbody/tr[@id]/td["+huafei+"]"));
			
			List<Integer> bigdel = new ArrayList<>();
			List<Integer> smallup = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				String huafeiText = list.get(i).getText();
				if(StringUtils.isEmpty(huafeiText)){
					huafeiText = "0";
//					bigdel.add(i+1);
//					continue;
				}
				
				huafeiText= huafeiText.replace("￥","");
				if(!NumberUtil.isNumeric(huafeiText)){
					huafeiText = "0";
//					bigdel.add(i+1);
//					continue;
				}
				
				//huafeiFloat标准价格
				Float f = new Float(huafeiText); //实际平均点击花费价格
				if(huafeiFloat.compareTo(f)==1){ //小于3元，提价
					smallup.add(i+1);
				}else if(huafeiFloat.compareTo(f)==-1){ //大于3就删除标红
					bigdel.add(i+1);
				}
			}
			
	/*		//过滤出红色的标签
			webDriver.findElement(By.xpath("//table/thead/tr/th[3]/div/div/span")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul[@class='dropdown-list']/li/span[contains(text(),'优化')]")).click();
			Thread.sleep(1500);*/
			
			//大于3块的要提价 自定义价格，去掉标红
			for (int i = 0; i < bigdel.size(); i++) {
				webDriver.findElement(By.xpath("//table[@class='bp-table']/tbody/tr[@id]["+bigdel.get(i)+"]/td[1]")).click();
				Thread.sleep(300);
			}
			
			if(bigdel.size()!=0){
				webDriver.findElement(By.xpath("//span[text()='标签']")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//div[contains(@id,'addTagLayer')]/div[3]/input[@value=2]")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//span[text()='删除'][@style]")).click(); 
				Thread.sleep(2000);
			}
			
			//点击了全部后，再重新点击会标红的数据
			webDriver.findElement(By.xpath("//table/thead/tr/th[3]/div/div/span")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul[contains(@style,'block')]/li/span[text()='全部']")).click();
			Thread.sleep(2000);
			webDriver.findElement(By.xpath("//table/thead/tr/th[3]/div/div/span")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul[@class='dropdown-list']/li/span[contains(text(),'优化')]")).click();
			
			//提高价格
		/*	webDriver.findElement(By.xpath("//table/thead/tr/th[3]/div/div/span")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul[@class='dropdown-list']/li/span[contains(text(),'优化')]")).click();*/
			
			//平均排名排序，点击两次
			WebElement ee = webDriver.findElement(By.xpath("//div[contains(text(),' 平均展现排名')]"));  // 是否需要点击两次？？？？？排序有问题么？
			ee.click();
			Thread.sleep(2000);
			
			ee = webDriver.findElement(By.xpath("//div[@id='J_common_header']"));
			String js = String.format("window.scroll(0, %s)", ee.getLocation().getY()+50);  
			 ((JavascriptExecutor)webDriver).executeScript(js);  
			Actions actions = new Actions(webDriver);
			actions.moveToElement(ee).build().perform();
			/***********排名筛选********/
			//平均排名位置 , 小于或者等于 scorePointInteger 的就不涨价啊啊啊啊啊啊啊啊
			Integer paimingindex = getHeaderIndex(webDriver, "平均展现排名")+1;
			list = webDriver.findElements(By.xpath("//table[@class='bp-table']/tbody/tr[@id]/td["+paimingindex+"]"));
			for (int i = 0; i < list.size(); i++) {
				String paimingtext = list.get(i).getText();
				if("".equals(paimingtext) || "-".equals(paimingtext)){  //升价
					
//					focusClick("//div[@class='table-container']/table/tbody/tr[@id]["+(i+1)+"]/td[1]/input", webDriver);
					webDriver.findElement(By.xpath("//div[@class='table-container']/table/tbody/tr[@id]["+(i+1)+"]/td[1]/input")).click();
					Thread.sleep(500);
					continue;
				}
				
				Integer paiming = Integer.parseInt(paimingtext);
				if(paiming > scorePointInteger){  //大于排名的要涨价
//					focusClick("//div[@class='table-container']/table/tbody/tr[@id]["+(i+1)+"]/td[1]/input", webDriver);
					webDriver.findElement(By.xpath("//div[@class='table-container']/table/tbody/tr[@id]["+(i+1)+"]/td[1]/input")).click();
					Thread.sleep(500);
				}
				
			}
			/***********end********/
			//************价格处理************///
			
			/*if(!StringUtils.isEmpty(paimingindex)){
				return "";
			}*/
		 
			
			Thread.sleep(1000);
			webDriver.findElement(By.xpath("//span[contains(text(),'修改出价')]/parent::span")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//span[contains(text(),'移动设备')]/parent::li")).click(); //移动端
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//form[@id='J_batch_price_form']/div/label[4]")).click(); //移动端,提价单选按钮
			Thread.sleep(1500);
			
			WebElement element  = webDriver.findElement(By.xpath("//input[@id='batchPrice_percent']")); //移动端
			element.click();
			Thread.sleep(1500);
			element.sendKeys(radioInteger+"");  //提高5%
			Thread.sleep(3000);
			
			
			if(flag){
				//需要重复点击，才能提高价格
				focusClick("//form[@id='J_batch_price_form']/div/a[1]", webDriver);  //确定提高价格
				try {
					Thread.sleep(100);
					focusClick("//form[@id='J_batch_price_form']/div/a[1]", webDriver);  //确定提高价格
				} catch (Exception e) {
				}
			}
			
			
			/******PC端设备*********/
			focusClick("//span[contains(text(),'修改出价')]/parent::span", webDriver);  //确定提高价格
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//span[contains(text(),'计算机')]/parent::li")).click(); //pc
			Thread.sleep(1500);
			
			webDriver.findElement(By.xpath("//form[@id='J_batch_price_form']/div/label[4]")).click(); //PC,提价单选按钮
			Thread.sleep(1500);
			
		  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			element  = webDriver.findElement(By.xpath("//input[@id='batchPrice_percent']")); //移动端
			element.click();
			Thread.sleep(1500);
			element.sendKeys(radioInteger+"");  //提高5%
			Thread.sleep(3000);
			
			if(flag){
				//重复点击，提高价格
				webDriver.findElement(By.xpath("//form[@id='J_batch_price_form']/div/a[1]")).click();  //点击最后的“确定”按钮，提交
				Thread.sleep(100);
				try {
					webDriver.findElement(By.xpath("//form[@id='J_batch_price_form']/div/a[1]")).click();  //点击最后的“确定”按钮，提交
				} catch (Exception e) { }
			}
			
			Thread.sleep(5000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		return "";
	}

	private Integer getHeaderIndex(WebDriver webDriver,String label) {
		List<WebElement> list = webDriver.findElements(By.xpath("//table[@id='bp-scroll-table']/thead/tr/th"));
		Integer convertInteger = null; 
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getText().indexOf(label)!=-1){
				convertInteger = i ;
				break;
			}
		}
		return convertInteger;
	}
	
	public static void main(String[] args) {
		System.out.println("sdf%".replace("%", ""));
	}
	
	public void focusClick(String xpath, WebDriver webDriver) throws InterruptedException{
		
		WebElement webElement  = webDriver.findElement(By.xpath(xpath));
		 String js = String.format("window.scroll(0, %s)", webElement.getLocation().getY()-100);  
		 ((JavascriptExecutor)webDriver).executeScript(js);  
		 
		Thread.sleep(100);
		
		Actions actions = new Actions(webDriver);
		actions.moveToElement(webElement).build().perform();
		Thread.sleep(50);
		webElement.click();
	}
}
