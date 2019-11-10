package com.crawler.entity;

import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.handler.text.IText;
import com.crawler.util.DriverUtil;
import com.crawler.util.FilterUtil;
import com.crawler.util.StringUtils;
import com.us.codecraft.Xsoup;

public class TextUnit extends UnitAdapter{
	
	private String currentXpath ; //list设置
	private boolean filename ;  //文件名称
	private boolean fragment;  //是否启用片段采集定位   
	private boolean url  ;     //是否为url采集的链接
	private String handler;    //处理者名
	private String suffix;    //后缀
	private String params ;    //定量参数
	private String regex  ;    //正则表达式:match|** , replace|**
	private String iframe ;    //iframe 使用xpath定位
	private String exists ;    //存在的xpath,才继续往下执行，不存在，默认为空字符串
	private String spare  ;    //备用xpath
	private boolean show  ;    //字段是否显示到用户界面
	private boolean global;    //全局,
	private boolean download; //是否为下载字段
	private IText iText; ;     //处理者接口
	public  static final String TYPE = "text";
	private static final Logger logger =  Logger.getLogger(TextUnit.class);

	public TextUnit() throws Exception {
		super(TYPE);
		this.show=true;
	}
	
	@Override
	protected void handler(WebDriver webDriver,String xpath,Storage storage) throws Exception {
		this.setCurrentXpath(xpath);
		
		String content = "";
		WebElement web = null; 
		
		if(super.getWait()!=null){
			Thread.sleep(super.getWait());
		}
		
		/*****是否存在iframe框架******/
		if(this.getIframe()!=null){
			WebElement iframe =  webDriver.findElement(By.xpath(this.getIframe()));
			webDriver.switchTo().frame(iframe);
		}
		
		/******************提取页面数据*************************/
		boolean existsFlag = true;
		if(this.getExists()!=null){ //元素存在的先决条件
			try {
				webDriver.findElement(By.xpath(this.getExists()));
			} catch (NoSuchElementException e) {
				logger.info("不存在元素:"+this.getExists()+",设置空默认值");
				content ="";
				existsFlag = false;
			}
		}
		
		
		/******************默认数据*************************/
		//xpath为空，params不为空，说明是设置了默认的数值
		if(existsFlag && StringUtils.isEmpty(xpath ) && this.getParams()!=null){
			content = this.getParams();
		}
		
		if(!StringUtils.isEmpty(xpath) &&  existsFlag){
			
			if(this.isFragment()){  //内容片段定位法
				if(this.getXpath().startsWith("//")){
					content = Xsoup.select(storage.getCurrentDoc(), this.getXpath()).get();
				}else{
					content = Xsoup.select(storage.getCurrentDoc(), "//body"+this.getXpath()).get();
				}
				
			}else{ //页面整体元素定位
				web = getTextWebElement(webDriver, xpath, storage);
				if(web!=null){
					if(this.getAttribute()==null){ //提取内容
						content=web.getText();
					}else{
						content=web.getAttribute(this.getAttribute()); //提取属性内容
						if(!StringUtils.isEmpty(content) && "src".equals(this.getAttribute()) && content.indexOf("http")==-1 ){
							content=DriverUtil.handleUrl(webDriver.getCurrentUrl(), content);
						}
					}
				}else{ //精确采集， 为空就是异常情况
					if(this.getAccurate()!=null){
						content ="exception";
					}else{
						content="";
					}
				}
			}
		}
		
		/*****************开始自定义的特殊处理*****************/
		if(this.getRegex()!=null){
			content =  FilterUtil.getRegexContent(content, this.getRegex(), this.getParams());
		}
		
		if(content!=null){
			content = content.trim();
		}
		
		//最后的文本处理者处理：处理者接口处理 
		if(iText!=null && existsFlag){
			content  = iText.getText(content,web,webDriver,this,storage);
		}
	 
/*		
		if(this.getAccurate()!=null && content.equals(this.getAccurate())){
			logger.info("精确重采集:"+content+" "+this.getAccurate());
			storage.repeatInputUrlProduct(); //重采输入框
		}else{
			storage.getStoreData().addText(content.trim(), super.getName(),storage);
		}*/
		
	/*	//如果是主线程,数据存储到仓库中，如果存储到了主配置的最后一个字段，就把这行的数据变成产品，增加到队列里去
		if(content.equals(this.getAccurate()) && storage.isInputParam() ){ //精确要求
			logger.info("精确重采集:"+content+" "+this.getAccurate());
			storage.repeatInputUrlProduct(); //重采输入框
		}*/
		
		
		if(this.getAccurate()!=null && content.equals(this.getAccurate()) ){
			storage.setContinue(false);
			storage.repeat();
		}else{
			storage.getEnv().showLogArea(content);
			storage.getStoreData().addText(content, super.getName(),storage);
		}
		
		//跳出iframe框架
		if(this.getIframe()!=null){
			webDriver.switchTo().defaultContent();
		}
	}
 
	/*
	 * 提取xpath,提取备用xpath
	 * 返回值为空，说明一定说明元素不存在，一定是异常
	 */
	private WebElement getTextWebElement(WebDriver webDriver, String xpath, Storage storage) throws Exception {
		WebElement web=null;
		try {
			if(this.isGlobal()){  //全局xpaht，不需要行的xpath
				web = DriverUtil.parseXpath(webDriver, super.getXpath());
			}else{ //xpath为空，则提取上下文设置的元素
				web = StringUtils.isEmpty(this.getXpath()) ? storage.getCurrentElement() : DriverUtil.parseXpath(webDriver, xpath);
			}
		} catch (NoSuchElementException e) {
			try {
				//备用xpath提取
				if(this.getSpare()!=null){  
					//说明不是全局的定位，而是列表的定位/
					String spareXpath=this.getSpare().indexOf("//")==-1 ? xpath.replace(super.getXpath(), this.getSpare()) : this.getSpare();
					logger.info("启用备用xpath："+this.getSpare());
					web = DriverUtil.parseXpath(webDriver, spareXpath);
				}else{
					logger.error("内容提取异常:"+e.getMessage());
//					DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
				}
			} catch (NoSuchElementException e2) {
				logger.error("备用xpath提取异常:"+e2.getMessage());
			}
		}catch (org.openqa.selenium.TimeoutException te) {
			te.printStackTrace();
			logger.error("查找元素超时:"+xpath);
		}
		
		return web;
	}

	public String getHandler() {
		return handler;
	}


	public void setHandler(String handler) {
		this.handler = handler;
	}


	public IText getiText() {
		return iText;
	}


	public void setiText(IText iText) {
		this.iText = iText;
	}
	
	
	public String getParams() {
		return params;
	}


	public void setParams(String params) {
		this.params = params;
	}


	public boolean isGlobal() {
		return global;
	}


	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public void init() throws Exception {
		if(handler!=null){  //初始化具体的文本处理者
			Class clazz = Class.forName("com.crawler.handler.text.impl."+handler) ; 
			if(this.getParams()==null){
				iText = (IText) clazz.newInstance();
			}else{
				Constructor  c=clazz.getConstructor(String.class);//获取有参构造  
				iText =(IText)  c.newInstance(this.getParams());     
				
			}
		}
	}
	
	public static void main(String[] args) {
		String s= "是酿梓啊：超级想我屋里七七去美腻的东方旅行 超级想啊@Chen七七";
		String regexMatch = "(?<=：)(.*)";
			Pattern p = Pattern.compile(regexMatch);
			Matcher m = p.matcher(s);
//			System.out.println(m.);
			if(m.find()){
				System.out.println( m.group(0));
			}
			 
		  String str = "??顏色：淺灰/藍色/粉色";
	        //将字符串中的.替换成_，因为.是特殊字符，所以要用\.表达，又因为\是特殊字符，所以要用\\.来表达.
	        str = str.replaceAll("\\?", "");
	        System.out.println(str);
//		System.out.println("/div[@node-type='feed_content']/div[@class='WB_detail']/div[@node-type='feed_list_content_full']".indexOf("//")==-1);
	}

	public String getIframe() {
		return iframe;
	}

	public void setIframe(String iframe) {
		this.iframe = iframe;
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

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isUrl() {
		return url;
	}

	public void setUrl(boolean url) {
		this.url = url;
	}

	public boolean isFragment() {
		return fragment;
	}

	public void setFragment(boolean fragment) {
		this.fragment = fragment;
	}

	public boolean isFilename() {
		return filename;
	}

	public void setFilename(boolean filename) {
		this.filename = filename;
	}

	public String getCurrentXpath() {
		return currentXpath;
	}

	public void setCurrentXpath(String currentXpath) {
		this.currentXpath = currentXpath;
	}

 
	
}
