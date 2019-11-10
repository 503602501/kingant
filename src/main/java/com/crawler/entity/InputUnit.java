package com.crawler.entity;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.crawler.util.ConfigUtil;
import com.crawler.util.DriverUtil;
import com.crawler.util.StringUtils;



public class InputUnit extends UnitAdapter {
	
	private String value  ;
	private String script  ;
	private boolean initurl  ;  //输入参数，回车刷新后，重新初始化分页url的所有数据
	private boolean reload  ;
	private boolean keydown  ;
	public static final String TYPE = "input";
	private static final Integer threshold = ConfigUtil.getIntegerConfigByKey("url.error.threshold");
	private static final Logger logger =  Logger.getLogger(InputUnit.class);
	
	public InputUnit() {
		super(TYPE);
	}
	
	@Override
	protected void handler(WebDriver webDriver,String xpath,Storage storage) throws Exception {
		WebElement webElement=null;
		try {
			 webElement =DriverUtil.parseXpath(webDriver,xpath);
		} catch (Exception e) {
			e.printStackTrace();

			return ;
		}
		
		if(super.getWait()!=null){
			Thread.sleep(super.getWait());
		}
		System.out.println("参数个数："+storage.getInputParamQueues().size());
		if("input".equals(this.getValue())){ //输入值，从客户端界面输入
			while (!storage.getEnv().isStop() && !storage.isInputParamQueuesEmpty() ) {//用户界面的多数据列输入
				
			     Product input = storage.getInputParamProduct();
			     
				 try {
					 if(this.isReload()){
						 Thread.sleep(1000);
						 webElement =DriverUtil.parseXpath(webDriver,xpath);
					 }
					 
					  if(input==null){
						  break;
					  }
					 
					  if(this.getScript()!=null){
						  ((JavascriptExecutor)webDriver).executeScript(this.getScript());
						  Thread.sleep(100);
					  }
					  
					 
					  Actions action = new Actions(webDriver); 
					  action.moveToElement(webElement).build().perform();
					  action.release();
						
					  input.setAddThreshold();
					  
					  String value = input.getParam();
					  storage.setCurrentInput(value);
					  if(StringUtils.isEmpty(value)){
						  continue;
					  }
					  Thread.sleep(150);
					  try {
						  webElement.clear();
					  } catch (Exception e) {  
						  //***********移动网站特殊处理***************
						  //异常input，重新处理
						  System.out.println("清空失败!!!!!!");
						  e.printStackTrace();
						  webDriver.get(webDriver.getCurrentUrl());
						  Thread.sleep(300);
						  try {  //存在退出的注销
							  WebElement logout=webDriver.findElement(By.xpath("//span[@id='logoutBtn']"));
							  logout.click();
							  Thread.sleep(1000);
						  } catch (Exception e2) {
							  e2.printStackTrace();
						  }
						  webElement =DriverUtil.parseXpath(webDriver,xpath);
						//***********移动网站特殊处理***************
					  }

					  Thread.sleep(150);
					  webElement.sendKeys(value);
					  if(this.isKeydown()){
						  webElement.sendKeys(Keys.ENTER);
					  }

//					  Actions action=new Actions(webDriver);
//					  action.sendKeys(Keys.ENTER).perform();//按回车键
					  
//					  if(StringUtils.isEmpty(webElement.getAttribute("value"))){
//						  webElement.sendKeys(value);
//					  }
//					  logger.info(value+":输入关键字："+webElement.getAttribute("value"));
					  
					  /*
					   * 输入数据后回车，重新初始化分页url
					   */
					  if(isIniturl()){
						  storage.initInputUrlQueues(webDriver.getCurrentUrl());
					  }
					  
					  for (Unit  unit: super.getChildUnit()) {
						  UnitAdapter adapter = (UnitAdapter) unit;
						  adapter.adapterHandler(webDriver,unit.getXpath(),storage); 
					  }
					}catch(UnreachableBrowserException ub){
						storage.putInputParamProduct(input);
					   throw new  UnreachableBrowserException(ub.getMessage());
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("处理元素失败："+e.getMessage());
						if( input.getThreshold()<threshold){
							storage.putInputUrlProduct(input);//清掉最后一行的不完整数据
							
						}else{
							storage.getErrorList().add(input.getUrl());
						}
					}
		   }
			
		}else{
			webElement.clear();
			Thread.sleep(150);
			webElement.sendKeys(this.getValue());
			if(StringUtils.isEmpty(webElement.getAttribute("value"))){
				  webElement.sendKeys(value);
			}
			for (Unit  unit: getChildUnit()) {
				UnitAdapter adapter = (UnitAdapter) unit;
				adapter.adapterHandler(webDriver,unit.getXpath(),storage); 
			}
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isReload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	public boolean isKeydown() {
		return keydown;
	}

	public void setKeydown(boolean keydown) {
		this.keydown = keydown;
	}

	public boolean isIniturl() {
		return initurl;
	}

	public void setIniturl(boolean initurl) {
		this.initurl = initurl;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
}
