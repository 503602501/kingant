package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * 降价
 */
public class TaobaoZtNewPricecHandler implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		boolean flag = false; // true 打开点击确定修改价格， false 不修改价格
		
		//转化率低于,降低出价%,低于展示量取消标蓝,过去（7或14或30）天做对比,低于平均点击花费取消标蓝
		
		try {
			//labels:转化率大于,提高出价
			String convert = storage.getLabelByIndex(0); //"4";    //点击转化率 
			String radio = storage.getLabelByIndex(1); // 降低出价  5%
			String huafeiinput = storage.getLabelByIndex(2); //低于展示量取消标蓝
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
				storage.getEnv().showLogArea("\n错误提示:低于展示量取消标蓝不能为空!!!!!\n\n");
				return "错误提示:低于展示量取消标蓝不能为空"; 
			}
			
			if(!Arrays.asList("7","14","30").contains(passdate) ){
				storage.getEnv().showLogArea("\n错误提示:过去天数非法!!!!!\n\n");
				return "错误提示:过去天数非法"; 
			}
			Integer radioInteger =   Integer.parseInt(radio);
			
			if(StringUtils.isEmpty(scorePoint) ){
				storage.getEnv().showLogArea("\n错误提示:低于价格取消标蓝取消标蓝非法!!!!!\n\n");
				return "错误提示:低于价格取消标蓝取消标蓝非法"; 
			}
			
			Float scorePointInteger = Float.parseFloat(scorePoint);
			
			Thread.sleep(5000);

			//先清理红色和蓝色的标签
			webDriver.findElement(By.xpath("//table[@class='bp-table freeze-th']/thead/th[1]")).click();  //全选
			webDriver.findElement(By.xpath("//span[text()='标签']")).click();
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[contains(@id,'addTagLayer')]/div/input[@value=1]")).click();
			Thread.sleep(1000);
			webDriver.findElement(By.xpath("//div[contains(@id,'addTagLayer')]/div/input[@value=2]")).click();
			webDriver.findElement(By.xpath("//span[text()='删除'][@style]")).click(); 
			//先清理红色和蓝色的标签 end end 
			Thread.sleep(2000);
			
		/*	WebElement date = webDriver.findElement(By.xpath("//span[@class='time-string']"));
			date.click();
			Thread.sleep(1500);
			
			webDriver.findElement(By.xpath("//a[text()='过去"+passdate+"天']")).click();
			
			Thread.sleep(2000);
			*/
			//点击转化率排序
			webDriver.findElement(By.xpath("//div[contains(text(),'点击转化率')]/i[1]")).click();  
			Thread.sleep(2000);
			//获取自定义的转化率
			
			if(!NumberUtil.isInteger(radio) ){
				storage.getEnv().showLogArea("\n错误提示:提高升价为整数5~1500之间!!!!!\n\n");
				return "错误提示：提高升价为整数5~1500之间"; 
			}
			
			List<WebElement> list;
			Integer convertInteger = getHeaderIndex(webDriver,"点击转化率");
			
			
			//转化率小于convertFloat
			List<Integer> set = new ArrayList();  //符合的转化率的位置
			convertInteger+=1;
			list = webDriver.findElements(By.xpath("//table[@class='bp-table']/tbody/tr[@id]/td["+convertInteger+"]"));
			for (int i = 0; i < list.size(); i++) {
				WebElement webElement = list.get(i);
				String s = webElement.getText();  //非法转化率如何处理
				
				if(StringUtils.isEmpty(s) || "-".equals(s)){
					set.add(i+1);
					continue;
				}
				
				if(s.indexOf("%")!=-1 ){
					s=s.replace("%", "");
					Float f = Float.parseFloat(s);
					if(f.compareTo(convertFloat)==-1){
						set.add(i+1);
					}
				}
			}
			
			
			//过去14天的通过点击转化率后排序， 转化率小于convertFloat的标蓝
			selectList(webDriver, set);
			
			//小于转化率的标蓝
			if(set.size()!=0){
				webDriver.findElement(By.xpath("//span[text()='标签']")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//div[contains(@id,'addTagLayer')]/div/input[@value=1]")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//span[text()='添加']")).click();
				Thread.sleep(2000);
			}
		
			//点击昨天的日期
//			focusClick("//span[@class='time-string']", webDriver);
			focusClick("//div[contains(text(),'今天')]", webDriver);
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//span[text()='昨天']")).click();
			Thread.sleep(2000);
			
			webDriver.findElement(By.xpath("//table[@class='bp-table freeze-th']/thead/th[3]/div")).click(); 
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul/li/span[contains(text(),'全部') and not(@mx-click)]")).click();
			Thread.sleep(2000);
			webDriver.findElement(By.xpath("//table[@class='bp-table freeze-th']/thead/th[3]/div")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul/li/span[contains(text(),'核心')]")).click();
			Thread.sleep(2000);
			
			/**********低于10的展现数取消标蓝**************/
			webDriver.findElement(By.xpath("//div[contains(text(),'展现量')]/i[1]")).click();  
			Thread.sleep(2000);
			Integer zhanxian = getHeaderIndex(webDriver, "展现量")+1;
			list = webDriver.findElements(By.xpath("//table[@class='bp-table']/tbody/tr[@id]/td["+zhanxian+"]"));
			List del = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				String zhanxiantext = list.get(i).getText();  //展示位空，也不要降价啦
				if( StringUtils.isEmpty(zhanxiantext) || "-".equals(zhanxiantext)){
					del.add(i+1);
					continue;
				}
				
				zhanxiantext = zhanxiantext.replaceAll("\\s*", "");
				zhanxiantext = zhanxiantext.replace("", "");
				zhanxiantext = zhanxiantext.replaceAll(",", "");
				
				if( StringUtils.isEmpty(zhanxiantext) || "-".equals(zhanxiantext)){
					del.add(i+1);
					continue;
				}
				
				Integer f = new Integer(zhanxiantext); //展现量
				
				if(f<huafeiFloat){
					del.add(i+1);
				}
			}
			
			selectList(webDriver, del);
			
			if(del.size()!=0){
				webDriver.findElement(By.xpath("//span[text()='标签']")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//div[contains(@id,'addTagLayer')]/div/input[@value=1]")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//span[text()='删除'][@style]")).click(); 
				Thread.sleep(2000);
			}
			webDriver.findElement(By.xpath("//table[@class='bp-table freeze-th']/thead/th[3]/div")).click(); 
			Thread.sleep(1000);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul/li/span[contains(text(),'全部') and not(@mx-click)]")).click();
			Thread.sleep(2000);
			focusClick("//table[@class='bp-table freeze-th']/thead/th[3]/div", webDriver);
			
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul/li/span[contains(text(),'核心')]")).click();
			Thread.sleep(2000);
	
			/*********平均点击花费小于xxx的取消标蓝, 空的不取消*********/
			webDriver.findElement(By.xpath("//div[contains(text(),'平均点击花费')]/i[1]")).click();  
			Thread.sleep(1500);
			Integer huafei = getHeaderIndex(webDriver, "平均点击花费")+1;
			list = webDriver.findElements(By.xpath("//table[@class='bp-table']/tbody/tr[@id]/td["+huafei+"]"));
			List<Integer> bigdel = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				String huafeiText = list.get(i).getText();
				if(StringUtils.isEmpty(huafeiText) || "-".equals(huafeiText)){
					continue;
				}
				
				huafeiText= huafeiText.replace("￥","");
				//huafeiFloat标准价格
				Float f = new Float(huafeiText); //实际平均点击花费价格
				if(scorePointInteger.compareTo(f)==1){
					bigdel.add(i+1);
				}
			}
			
			WebElement ee = webDriver.findElement(By.xpath("//div[@class='adgroup-detail']"));
			String js = String.format("window.scroll(0, %s)", ee.getLocation().getY()+50);  
			 ((JavascriptExecutor)webDriver).executeScript(js);  
			Actions actions = new Actions(webDriver);
			actions.moveToElement(ee).build().perform();
			
			selectList(webDriver, bigdel);
			 
			//取消标蓝
			if(bigdel.size()!=0){
				webDriver.findElement(By.xpath("//span[text()='标签']")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//div[contains(@id,'addTagLayer')]/div/input[@value=1]")).click();
				Thread.sleep(1500);
				webDriver.findElement(By.xpath("//span[text()='删除'][@style]")).click(); 
				Thread.sleep(2000);
			}
			
			//点击了全部后，再重新点击标蓝的数据
			webDriver.findElement(By.xpath("//table[@class='bp-table freeze-th']/thead/th[3]/div")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul/li/span[contains(text(),'全部') and not(@mx-click)]")).click();
			Thread.sleep(2000);
			focusClick("//table[@class='bp-table freeze-th']/thead/th[3]/div", webDriver);
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//div[@class='thead-title']/div/ul/li/span[contains(text(),'核心')]")).click();
					
			Thread.sleep(1500);
			focusClick("//input[@class='checkall']", webDriver);
			
			//********************降价处理********************
			Thread.sleep(500);
			webDriver.findElement(By.xpath("//span[contains(text(),'修改出价')]/parent::span")).click(); 
			Thread.sleep(1500);
			webDriver.findElement(By.xpath("//span[contains(text(),'移动设备')]/parent::li")).click(); //移动端
			Thread.sleep(1500);
			try {
				webDriver.findElement(By.xpath("//form[@id='J_batch_price_form']/div/label[4]")).click(); //移动端,提价单选按钮
			} catch (Exception e) {
				//筛选不出此的失败；
				return "筛选不到合适的升价数据，无法正常降价";
			}
			
			Thread.sleep(1500);
			
			webDriver.findElement(By.xpath("//div[@id='dp_batchPercent_priceipdown']")).click();
			Thread.sleep(100);
			webDriver.findElement(By.xpath("//div[@id='dp_batchPercent_priceipdown']/ul/li[2]")).click(); //降价
			Thread.sleep(100);
			WebElement element  = webDriver.findElement(By.xpath("//input[@id='batchPrice_percent']")); //移动端
			element.click();
			Thread.sleep(1500);
			element.sendKeys(radioInteger+"");  //提高5%
			Thread.sleep(3000);
			
			if(flag){
				//需要重复点击，才能提高价格
				focusClick("//form[@id='J_batch_price_form']/div/a[1]", webDriver);  //确定降价价格
				try {
					Thread.sleep(100);
					focusClick("//form[@id='J_batch_price_form']/div/a[1]", webDriver);  //确定降价价格
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
			
			webDriver.findElement(By.xpath("//div[@id='dp_batchPercent_priceipdown']")).click();
			Thread.sleep(100);
			webDriver.findElement(By.xpath("//div[@id='dp_batchPercent_priceipdown']/ul/li[2]")).click(); //降价

			Thread.sleep(100);
			element  = webDriver.findElement(By.xpath("//input[@id='batchPrice_percent']"));//输入框
			element.click();
			Thread.sleep(1500);
			element.sendKeys(radioInteger+"");  //提高5%
			Thread.sleep(3000);
			
			if(flag){
				//需要重复点击，才能降价价格
				focusClick("//form[@id='J_batch_price_form']/div/a[1]", webDriver);  //确定降价价格
				try {
					Thread.sleep(100);
					focusClick("//form[@id='J_batch_price_form']/div/a[1]", webDriver);  //确定降价价格
				} catch (Exception e) {
				}
			}
			   
			Thread.sleep(5000);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "失败运行:"+webDriver.getCurrentUrl() ;
		}
		
		return "成功运行|"+webDriver.getCurrentUrl();
	}

	private Integer getHeaderIndex(WebDriver webDriver,String label) {
		List<WebElement> list = webDriver.findElements(By.xpath("//table[@class='bp-table scroll-th']/thead/tr/th"));
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
	public void selectList(WebDriver webDriver,List<Integer> set) throws InterruptedException{
		WebElement web = null;
		for (Integer integer : set) {
			web = webDriver.findElement(By.xpath("//div[@class='freeze-td hide-scroll-bar']/table/tbody/tr[@id]["+integer+"]/td[1]/input"));
			
			Actions actions = new Actions(webDriver);
			actions.moveToElement(web).build().perform();
			web.click();
			Thread.sleep(300);
		}
		
	}
}
