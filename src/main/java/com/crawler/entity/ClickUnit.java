package com.crawler.entity;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.crawler.util.DriverUtil;
import com.crawler.util.StringUtils;



public class ClickUnit extends UnitAdapter {
	
	private boolean newpage  ;
	private boolean skip  ;
	private Integer scroll  ;
	private String  script  ;
	private String  exists ;
	private String  spare ;
	private Integer timeout  ;
	private boolean global  ;  //全局
	private boolean focus  ;  //全局
	private boolean initurl  ;  //输入参数，回车刷新后，重新初始化分页url的所有数据
	private static final String TYPE = "click";
	private static final Logger logger =  Logger.getLogger(ClickUnit.class);
	
	public ClickUnit() {
		super(TYPE);
	}
	
	@Override
	protected void handler(WebDriver webDriver,String xpath,Storage storage) throws Exception {
		String  currentHandle = webDriver.getWindowHandle() ; //当前窗口
//		List<String> currentHandle = new ArrayList<String>(webDriver.getWindowHandles()); //一打开的所有页面
		WebElement webElement = null;
		
		if(isGlobal()){  //全局定位
			xpath = this.getXpath();
		}
		
		if(super.getWait()!=null){
			Thread.sleep(super.getWait());
		}
		
		if(this.getScript()!=null){
			((JavascriptExecutor)webDriver).executeScript(this.getScript());
		}
		
		if(StringUtils.isEmpty(this.getXpath())){
			webElement=storage.getCurrentElement();
		}
		
		try {
			if(StringUtils.isEmpty(this.getXpath())){
				webElement=storage.getCurrentElement();
			}else{
				webElement=DriverUtil.parseXpath(webDriver,xpath);
				
			}
		} catch (Exception e) {
			if(StringUtils.isEmpty(this.getSpare())){
				logger.error("元素查找异常："+e.getMessage());
				return ;
			}
			
			try {
				if(!StringUtils.isEmpty(this.getSpare())){
					webElement=DriverUtil.parseXpath(webDriver,getSpare());
				}
			} catch (Exception e2) {
				logger.error("元素查找异常："+e2.getMessage());
				return ;
			}
			
		}
		
/*//		WebElement e = webDriver.findElement(By.xpath(xpath));
 */
 	/*((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();",webElement);
		Thread.sleep(300);
		Actions action = new Actions(webDriver); 
		action.moveToElement(webElement).build().perform();
		action.release();*/
		 
/*		if(isSkip()){ //是否跳过该事件,如果没有找到该元素，说明直接跳过
			try {
				webElement=DriverUtil.parseXpath(webDriver,xpath);
			} catch (Exception e) {
				System.out.println("没有验证机制："+e.getMessage());
				return ;
			}
		}else{
			try {
				webElement=DriverUtil.parseXpath(webDriver,xpath);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("元素定位异常："+e.getMessage());
				return ;
//				DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
			}
		}*/
		
		
		if(this.isNewpage()){ //点击元素，打开新页面
			//最长的超时时间
			if(super.getTimeout()!=null){
				webDriver.manage().timeouts().pageLoadTimeout(super.getTimeout(), TimeUnit.SECONDS);
			}
			
//			long start = System.currentTimeMillis();
			if(super.getAttribute()!=null){
				String url = webElement.getAttribute(super.getAttribute());
				System.out.println("打开的网站："+url);
				((JavascriptExecutor)webDriver).executeScript("window.open('"+url+"','_blank');");
			}else{
				webElement.click(); //点击的方式打开新页面
			}
			
//			System.out.println("切回："+(System.currentTimeMillis()-start));
			/*if(getChildUnit().size()==0){ //只是纯粹的点击打开页面，并没有子类处理
				webDriver.switchTo().window(currentHandle); //切换回父窗口
				return ;
			}*/
			//关闭点击打开的页面
			WebDriver chlild = null ; 
		    for (String  handle : webDriver.getWindowHandles()) {
	        	if(!currentHandle.equals(handle)){
	        		chlild = webDriver.switchTo().window(handle); //切换到新的窗口
	        		break ; 
	        	}
			}
		    
		    //子类逻辑处理
			for (Unit  unit: getChildUnit() ) {
				UnitAdapter adapter = (UnitAdapter) unit;
				adapter.adapterHandler(webDriver,unit.getXpath(),storage); 
			}
			
			//关闭点击打开的页面
			chlild.close();
			
			webDriver.switchTo().window(currentHandle); //切换回父窗口

		    
		}else{ //不用打开新窗口的逻辑
			try {
//				 DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
				if(isFocus()){ //聚焦到元素上
					 String js = String.format("window.scroll(0, %s)", webElement.getLocation().getY()-100);  
					 ((JavascriptExecutor)webDriver).executeScript(js);  
					Thread.sleep(100);
				}
//				DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
//				if(this.getTimeout()!=null){
//					webDriver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);
//				}
				boolean flag = true;
				if(this.getExists()!=null){
					try {
						webDriver.findElement(By.xpath(this.getExists()));
					} catch (NoSuchElementException e) {
						 flag = false;
						logger.error("点击前提不存在："+e.getMessage());
					}
				}
				
				if(flag){
						webElement.click();
//					DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
				}
//				 DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
//				((JavascriptExecutor)webDriver).executeScript("document.getElementById('market').display='block';");
				
				/*
				 * 点击事件之后做滚动
				 */
				if(this.getScroll()!=null){  //滚动
					 DriverUtil.scrollScreen(webDriver, this.getScroll(),storage.getEnv());
				}
				
			}catch (Exception e) {
				
				//子线程：是否进行精确的采集，如果出现异常则重新采集
				if("exception".equals(this.getAccurate())){
					storage.repeat();
				}
//				DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
				logger.error(e.getMessage());
				logger.error(xpath+"点击后加载页面未在"+super.getTimeout()+"内完成");
			}

			if(isIniturl()){
				  storage.initInputUrlQueues(webDriver.getCurrentUrl());
			}
			  
			
			for (Unit  unit: getChildUnit()) {
				UnitAdapter adapter = (UnitAdapter) unit;
				adapter.adapterHandler(webDriver,unit.getXpath(),storage);
			}
			
		}
		
	}
	
	public boolean isNewpage() {
		return newpage;
	}

	public void setNewpage(boolean newpage) {
		this.newpage = newpage;
	}
	
	public Integer getScroll() {
		return scroll;
	}

	public void setScroll(Integer scroll) {
		this.scroll = scroll;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isIniturl() {
		return initurl;
	}

	public void setIniturl(boolean initurl) {
		this.initurl = initurl;
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getExists() {
		return exists;
	}

	public void setExists(String exists) {
		this.exists = exists;
	}

	public String getSpare() {
		return spare;
	}

	public void setSpare(String spare) {
		this.spare = spare;
	}
	
}
