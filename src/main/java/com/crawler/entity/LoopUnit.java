package com.crawler.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * 循环页面或者元素,或者用户的输入值
 */
public class LoopUnit extends UnitAdapter{
	
	private String mode  ;	
	private Integer count  ;	
	private static final String TYPE = "loop";
	private static final Logger logger = Logger.getLogger(OpenUnit.class);
	
	public LoopUnit() {
		super(TYPE);
	}
	
	@Override
	protected void handler(WebDriver webDriver,String xpath,Storage storage) throws Exception {
		
		Unit unit = null;
		
		if("page".equals(this.getMode())){ //循环页面，处理多页面的数据提取
			String  currentHandle = webDriver.getWindowHandle() ; //当前窗口 
			webDriver.getWindowHandles();
			List<String> pageList= new ArrayList<String>();
			pageList.addAll(webDriver.getWindowHandles());
			for (String handle : pageList) { //处理主窗口之外的窗口
				if(!handle.equals(currentHandle)){
					webDriver.switchTo().window(handle); //切换到其他的窗口
					
					for (int j = 0; j < getChildUnit().size(); j++) {
						UnitAdapter adapter = (UnitAdapter) getChildUnit().get(j) ;
						adapter.adapterHandler(webDriver,unit.getXpath() , storage);
					}
					webDriver.close();
				}
				
			}
			
			webDriver.switchTo().window(currentHandle);//回到主窗口
		}else{
			/*List<WebElement> webElements = webDriver.findElements(By.xpath(super.getXpath()) );
			for (int i =0; i < webElements.size(); i++) {  //一行的元素
				for (int j = 0; j < getChildUnit().size(); j++) {
					UnitAdapter adapter = (UnitAdapter) getChildUnit().get(j) ;
					adapter.adapterHandler(webDriver,super.getXpath()+"["+(i+1)+"]"+unit.getXpath() , storage);
				}
			}*/
			while(true){
				
				if(storage.getEnv().isStop()){
					break;
				}
				
				try {
					WebElement element =  webDriver.findElement(By.xpath(super.getXpath()));
				} catch (Exception e) {
					logger.error("元素不存在"+e.getMessage());
					break;
				}
				
				for (int j = 0; j < getChildUnit().size(); j++) {
					UnitAdapter adapter = (UnitAdapter) getChildUnit().get(j) ;
					adapter.adapterHandler(webDriver,this.getXpath()+adapter.getXpath(), storage);
				}
				
			}
			
			
		}
		
		/*
		if(super.getFind()!=null){  //等待查找到元素为止
			try {
				WebDriverWait wait = new WebDriverWait(webDriver, super.getTimeout());
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(super.getFind())));
				
			} catch (Exception e) {
				System.out.println("--------无法加载出列表------：");
				return ;
			}
		}
		
		if(super.getWait()!=null){
			Thread.sleep(super.getWait()*1000);
		}
		
		List<WebElement> webElements = webDriver.findElements(By.xpath(super.getXpath()) );*/
		
		
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
