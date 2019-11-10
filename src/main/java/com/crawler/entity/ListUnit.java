package com.crawler.entity;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.util.DriverUtil;


public class ListUnit extends UnitAdapter{
	
	private String include ; 
	private String spare ; 
	private Integer scroll ; 
	private Integer browerbacks ;  //浏览器回退次数，减少浏览器的重新加载，特别是运用在手机端采集
	private String moveto ;   //移动到指定的元素
	private String exclude ;   //排除
	private String iframes ;  //iframes,支持多层
	private Integer currentCount; //当前计数器
	private static final String TYPE = "list";
	private static final Logger logger =  Logger.getLogger(ListUnit.class);
	
	public ListUnit() {
		super(TYPE);
	}
	
	@Override
	protected void handler(WebDriver webDriver,String xpath, Storage storage) throws Exception {
		
		List<WebElement> webElements =null;
		if(super.getFind()!=null){  //等待查找到元素为止
			try {
				WebDriverWait wait = new WebDriverWait(webDriver, super.getTimeout());
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(super.getFind())));
				
			} catch (Exception e) {
				logger.error("无法在指定的时间内加载出"+super.getFind()+"的元素"+e.getMessage());
				return ;
			}
		}
		
		if(super.getWait()!=null){
			try {
				Thread.sleep(super.getWait());
			} catch (Exception e) {
				logger.info("中断异常");
			}
		}
		
		if(this.getIframes()!=null){
			DriverUtil.switchToIframes(webDriver, this.getIframes());
		}
		
		if(this.getScroll()!=null){
			DriverUtil.scrollScreen(webDriver, this.getScroll(),storage.getEnv());
		}
		
		/**************************/
		/*WebElement element = webDriver.findElement(By.xpath("//div[@id='js-pager-limit']"));
		Coordinates coordinate = ((Locatable)element).getCoordinates(); 
		coordinate.onPage(); 
		coordinate.inViewPort();*/

		/*************测试*******/
		if(this.getMoveto()!=null){
			for (int i = 0; i < 10; i++) {
				WebElement e = webDriver.findElement(By.xpath(this.getMoveto()));
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();",e);
				Thread.sleep(200);
				Actions action = new Actions(webDriver); 
				action.moveToElement(e).build().perform();
				
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();",e);
				Thread.sleep(500);
				action = new Actions(webDriver); 
				action.moveToElement(e).build().perform();
				
				action.release();
			}
		}
		
//		 DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
		WebElement ul = null;
		if(super.getXpath().indexOf("->")!=-1){  //行中分行
			String ulXpath  = null; //存储第一层行的xpath
			String [] ulli = super.getXpath().split("->") ;
			int ulTotal = webDriver.findElements(By.xpath(ulli[0]) ).size(); //第一层有多少个元素
			for (int i = 0; i < ulTotal; i++) {
				 
				if(this.getInclude()!=null){ //每行必须包含的内容
					ul = webDriver.findElement(By.xpath(ulli[0]+"["+(i+1)+"]" ));
					String[] includes = this.getInclude().split("\\|");
					boolean flag = false;
					for (String string : includes) {
						if(ul.getAttribute("innerHTML").indexOf(string)!=-1){  //不包含内容
							flag = true;
							break;
						}
					}
					
					if(flag==false){  //如果都不存在就
						continue;
					}
				/*	//特殊化客户的特定需求
					if(ul.getAttribute("innerHTML").indexOf("</a>")!=-1){
						ulli[1]="/a"+ulli[1];
					}else{
						ulli[1] =ulli[1].replace("/a", "");
					}*/
				}
					
				ulXpath  =  ulli[0]+"["+(i+1)+"]"+ulli[1] ; 
				webElements = webDriver.findElements(By.xpath(ulXpath) ); 
				handlerRow(webDriver, webElements,ulXpath,storage);
			}
		}else{
			
			String listPath = this.getXpath();
			webElements = webDriver.findElements(By.xpath(this.getXpath()));
			if(webElements.size()==0){
				if(this.getSpare()!=null){
					listPath= this.getSpare();
					webElements = webDriver.findElements(By.xpath(listPath)); 
				}
			}
	 
//			webElements=webDriver.findElements(By.xpath(listPath) );
			handlerRow(webDriver, webElements,listPath,storage);
		}
		
		if(this.getIframes()!=null){
			webDriver.switchTo().defaultContent();
		}
		
		//浏览器回退，根据backs的次数，确定回退的次数
		if(this.getBrowerbacks()!=null){
			DriverUtil.navigateBack(this.getBrowerbacks(), webDriver,storage.getEnv());
		}
		
	}

	/*
	 * 出来行数据，调用子类进行处理
	 */
	private void handlerRow(WebDriver webDriver, List<WebElement> webElements,String xpath,Storage storage)throws Exception {
		
		this.currentCount = this.getCount();
		
		for (int i =0; i < webElements.size(); i++) {  //一行的元素
			String innerHTML = webElements.get(i).getAttribute("innerHTML") ; 
			
			if(this.getInclude()!=null){ //每行必须包含的内容
				String[] includes = this.getInclude().split("\\|");
				boolean flag = false;
				for (String string : includes) {
					if(innerHTML.indexOf(string)!=-1){  //包含内容
						flag = true;
						break;
					}
				}
				
				if(flag==false){  //如果都不存在就
					continue;
				}
			}
			
			if(this.getExclude()!=null){ //每行必须包含的内容
				if(webElements.get(i).getAttribute("innerHTML").indexOf(this.getExclude())!=-1){  //排除内容
					continue;
				}
			}
			
			if(getCount()!=null && this.currentCount==0){
				break; //循环完毕
			}
			
			storage.setCurrentElement(webElements.get(i));
			try {
				String doc = webElements.get(i).getAttribute("innerHTML");
				doc = doc.replaceAll("<td", "<div");
				doc = doc.replaceAll("</td>", "</div>");
				storage.setCurrentDoc(Jsoup.parse(doc)) ; 
			} catch (Exception e) {
				logger.error("错误:innerHTML:-->>>>>"+e.getMessage());
			}
			for (int j = 0; j < getChildUnit().size(); j++) {
				UnitAdapter adapter = (UnitAdapter) getChildUnit().get(j) ;
				if(adapter.getXpath().startsWith("//")){//属于全局的标志
					adapter.adapterHandler(webDriver,adapter.getXpath(),storage );
				}else{
					adapter.adapterHandler(webDriver,xpath+"["+(i+1)+"]"+adapter.getXpath(),storage );
				}
			}
			
			if(getCount()!=null){
				currentCount-=1;
			}
		}
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	 

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public Integer getScroll() {
		return scroll;
	}

	public void setScroll(Integer scroll) {
		this.scroll = scroll;
	}

	public String getMoveto() {
		return moveto;
	}

	public void setMoveto(String moveto) {
		this.moveto = moveto;
	}

	public String getIframes() {
		return iframes;
	}

	public void setIframes(String iframes) {
		this.iframes = iframes;
	}

	public Integer getBrowerbacks() {
		return browerbacks;
	}

	public void setBrowerbacks(Integer browerbacks) {
		this.browerbacks = browerbacks;
	}

	public String getSpare() {
		return spare;
	}

	public void setSpare(String spare) {
		this.spare = spare;
	}
 
}
