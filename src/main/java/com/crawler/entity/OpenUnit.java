package com.crawler.entity;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.crawler.handler.open.IOpen;
import com.crawler.handler.row.IRowHander;
import com.crawler.util.ConfigUtil;
import com.crawler.util.DriverUtil;
import com.crawler.util.StringUtils;

public class OpenUnit  extends Unit{
	private Expire  entity;
	private boolean sort ; //是否按照采集输入的字段作为排序结果
	private boolean exportall ; //是否导出所有的数据，包括漏采集的数据
	private boolean filter ; 
	private String id;
	private String expire; //期限
	private String name;
	private Boolean image;  //是否加载图片
	private String labels;
	private String link  ;	//input,queues
	private String init ; //初始化url，用户未启动软件就开始初始化页面，然后停止
	private String urlhandler ;  //处理者名  //
	private IOpen iOpen; ;   //初始化处理者
	private String rowhandler  ;	//行处理
	private IRowHander iRowHander; //行处理类
	private String sheet  ;	
	private boolean  major  ;	//是否主线程
	private boolean  monitor  ;	//是否开启监控进程
	private boolean  alreadyRun  ;	//是否启动了定时器
	private Integer scroll ; 
	private Integer schedule ;  //定时启动，秒为单位
	private Integer threads ; 
	private String params ;  //定量参数
	private String exists ;  //页面加载成功的xpath'
	private String help ;  //使用帮助
	private String extensions ;   //浏览器插件功能,插件名称
	private String brower ;    //浏览器的类型:chrome，或者phantomjs，默认是网页端访问； phone:表示手机端访问
	public static final String TYPE = "open";
	
	private String special ;     //各种功能的自身的特殊参数定制 ； repeatDownload:true
	public static final String REPEAT_DOWNLOAD = "repeatDownload";
	private static final Logger logger =  Logger.getLogger(OpenUnit.class);
	private static final Integer threshold = ConfigUtil.getIntegerConfigByKey("url.error.threshold");

	public OpenUnit() {
		super(TYPE);
		this.filter = false;
	}
	/*
	 * 启动爬虫
	 */
	public void runSpider(Storage storage) throws Exception {
		DriverEntity driverEntity = null;
		WebDriver driver= null; 
		Product product = null;
		String  url= null;

		/*
		 * 主线程和子线程合并
		 * 主线程，抓取链接提供给子线程运行
		 * 
		 */
		//特殊处理运行配置前的一些初始化数据,发生异常,重复处理3次为极限
		if( this.urlhandler!=null){
			//代码块同步，只有一个主程序能够进入
			/**********代码块加锁************/
			synchronized(storage.getEnv()){
				if(storage.getEnv().isInitUrl()){
					for (int i = 0; i < 3; i++) {
						try {
							driverEntity = storage.getDriverEntity(this.getBrower(),this.image); //获取驱动
							driverEntity.setTime(System.currentTimeMillis());
							driver =driverEntity.getWebDriver();
							iOpen.init(this, storage,driverEntity);
							break;
						} catch (Exception e) {
							logger.error("初始化特殊分页数据异常:",e);
							Thread.sleep(1000);
						}
					}
				}
				storage.getEnv().setInitUrl(false);
			}
			/**********代码块解锁************/
		}
		
		System.out.println("剩余运行："+storage.getInputUrlQueues().size());
		while (!storage.getEnv().isStop()) { 
			
			try {
				driverEntity = storage.getDriverEntity(this.getBrower(),this.image); //获取驱动
				driverEntity.setTime(System.currentTimeMillis());
				driver =driverEntity.getWebDriver();
				
				if(isMajor()){
					if(StringUtils.isEmpty(this.getLink())){  //没有连接，说明不需要重新定位页面
						product = new Product();
						if(!StringUtils.isEmpty(this.getInit())){
							storage.setContinue(true);
							//页面处理逻辑
							for (Unit  unit: getChildUnit()) {
								// 是否自定义翻页数量
								boolean definePage = false; 
								Integer pageCount = null; 
								//是否是分页子类，并且是用户动态设置翻页参数？
								if(unit.getClass().getName().endsWith("PageUnit")){
									PageUnit pageUnit = (PageUnit) unit;
									//需要设置用户自定义的分页数量
									// count:0 ,标识设置了特殊输入的数组索引为0的输入框，设置为翻页参数 ； 
									//分页默认为-1，标识特殊输入的索引位置为1的位置为设置翻页的次数
									if(pageUnit.getCount()!=null && pageUnit.getCount()<=0 ){  
										definePage= true;
										pageCount =pageUnit.getCount();
										String pagestr = storage.getEnv().getWidgets().getInputFields().get(Math.abs( pageUnit.getCount())).getText();
										if(StringUtils.isEmpty(pagestr)){ //用户忘记输入翻页参数
											pageUnit.setCount(null);
										}else{
											pageUnit.setCount(Integer.parseInt(pagestr.trim()));
										}
									}
								}
								
								UnitAdapter adapter = (UnitAdapter) unit;
								adapter.adapterHandler(driver,unit.getXpath(), storage); 
								
								if(definePage){
									PageUnit pageUnit = (PageUnit) unit;
									pageUnit.setCount(pageCount);
								}
							}
							break;
						}
						
					}else if(storage.isInputUrlEmpty()){ //队列为空，退出循环
						break; 
					}else{
						product=storage.getInputUrlProduct();
						if("this".equals(product.getUrl())){
							product.setUrl(driver.getCurrentUrl());
						}
					}
				}else{
					if(storage.isCompleteProduct() && storage.getQueues().isEmpty()){
						storage.getEnv().showLogArea(Thread.currentThread().getName()+"子线程销毁");
						break;
					}
					
					if(storage.getQueues().isEmpty()){
						storage.getEnv().showLogArea(Thread.currentThread().getName()+"子线程运行");
						try {
							Thread.sleep(2000);
						} catch (Exception e) {
							logger.info("线程中断异常："+e.getMessage());
						}
						continue;
					}
					product =storage.pop();
				}

				if(product==null ){
					continue;
				}
				
				if( product.getThreshold()>threshold){
					storage.addErrorList((isMajor()?"主":"子")+"链接："+url);
					continue;
				}
				
				product.setAddThreshold();
				url = product.getUrl(); //如果是不需要打开新的页面此处为空
				/*if(StringUtils.isEmpty(url)){
					continue;
				}*/
				
//				System.out.println("链接_____________________:"+url);
				boolean success = false;
				
				if(this.getExists()!=null && url!=null){
					success =DriverUtil.navigateUrl(storage, url,this.getTimeout(),this.getExists());
				}
				
				if(this.exists==null && !StringUtils.isEmpty(url)){
					success =DriverUtil.navigateUrl(storage, url,this.getTimeout());
				}
				
				if(!StringUtils.isEmpty(url)&& success==false){
					if( product.getThreshold()<threshold){
					    if(isMajor()){  //主线程
					    	storage.putInputUrlProduct(product); 
					    }else{  //子线程
					    	storage.getQueues().add(product);
					    }
						continue;
					}else{
						storage.addErrorList((isMajor()?"主":"子")+"链接："+url);
					}
				}
				
				if(this.getScroll()!=null){
					DriverUtil.scrollScreen(driver, this.getScroll(),storage.getEnv());
				}
				
				if(!isMajor()){  //子线程
					for (int i = 0; i <product.getHeader().size() ; i++) {  //先把产品数据提取出来，添加到子线程的仓库中
						storage.getStoreData().addText(product.getList().get(i), product.getHeader().get(i), storage);
					}
				}
				
				storage.setContinue(true);
				
 
				if(this.entity!=null && this.entity.isExpire()){
					int count = RandomUtils.nextInt(10, 50);
					for (int i = 0; i < count; i++) {
						storage.getEnv().showLogArea(entity.getMessage());
					}
					storage.getInputUrlQueues().clear();
					logger.info("expssss------------------");
				}else{
					for (Unit  unit: getChildUnit()) {
						UnitAdapter adapter = (UnitAdapter) unit;
						adapter.adapterHandler(driver,unit.getXpath(), storage); 
						
					}
				}
				
			/*	最后执行
				 
				String pname ="";
				List<WebElement> webs = driver.findElements(By.xpath("//div[@class='color-selector-list']/a"));
				for (WebElement webElement : webs) {
					webElement.click();
					Thread.sleep(200);
					pname = driver.findElement(By.xpath("//h1[@data-talos='labelPdpProductTitle']")).getText();
					storage.getStoreData().addText(pname, "文件夹名-商品名",storage);
					List<WebElement> imgs = driver.findElements(By.xpath("//div[@class='thumbnails text-center']/a/img"));
					StringBuffer sb = new StringBuffer();
					for (WebElement img : imgs) {
						sb.append(img.getAttribute("src").replace("w35", "2000")+",");
					}
					storage.getStoreData().addText(sb.toString(), "主图片链接",storage);
					storage.getStoreData().addText(driver.getCurrentUrl(), "商品链接",storage);
				}
				最后执行
				 */
				
			}catch (InterruptedException e) {
				logger.error("线程中断异常");
				storage.removeDirverEntity();
				break;
			}catch(NoSuchWindowException  | UnreachableBrowserException conn ){
				logger.error("浏览器运行异常："+url+"|"+conn.getMessage());
				storage.removeDirverEntity();
				if(isMajor()){
					storage.putInputUrlProduct(product);//清掉最后一行的不完整数据
				}else{
					storage.getQueues().offer(product);
				}
				storage.clearLastRow();//清掉最后一行的不完整数据
			}catch(TimeoutException te){
				logger.error("超时："+url+"|"+te.getMessage());
				if(isMajor()){
					storage.putInputUrlProduct(product);//清掉最后一行的不完整数据
				}else{
					storage.getQueues().offer(product);
				}
				storage.clearLastRow();//清掉最后一行的不完整数据
			}catch (Exception e) {
				e.printStackTrace();
				storage.addErrorList((isMajor()?"主":"子")+"链接："+url);
				logger.error("主线程运行页面处理异常:",e);
			}
			storage.clearPageCount(); 
			if(storage.isInputParamQueuesEmpty()){
				storage.getEnv().setStop(true);			
			}
			
			if(this.link==null ){  //说明是init初始化页面，页面打开完成采集后，就不需循环,运行一次就退出循环啦
				logger.info("link为空退出");
				break;
			}
		}
 
		//线程结束，将线程数减一
		storage.reduceThreads();
		//驱动放回队列，以便提供给其他的线程使用
		storage.backDriverQuences(driverEntity);
		
	}
	
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	public boolean isMajor() {
		if(this.getLink().equals("queues")){
			return false;
		}
	 
		return major;
	}
	public void setMajor(boolean major) {
		this.major = major;
	}
	
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
		if("input".equals(link)){
			setMajor(true);
		}
		
	}
	public Integer getScroll() {
		return scroll;
	}
	public void setScroll(Integer scroll) {
		this.scroll = scroll;
	}
	
	
	public IRowHander getiRowHander() {
		return iRowHander;
	}
	public void setiRowHander(IRowHander iRowHander) {
		this.iRowHander = iRowHander;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	@Override
	public void init() throws Exception {
		if(rowhandler!=null){  //初始化行处理类
			logger.error("不存在下载文件夹的行处理类");
		}else{
			setRowhandler("DefaultFolderHandler");
		}
		//默认的下载文件夹处理者
		Class clazz = Class.forName("com.crawler.handler.row.impl."+this.rowhandler) ; 
		if(this.getParams()==null){
			iRowHander  = (IRowHander) clazz.newInstance();
		}else{
			Constructor  c=clazz.getConstructor(String.class);//获取有参构造  
			iRowHander =(IRowHander)  c.newInstance(this.getParams());     
		}
		
		//url处理者
		if(urlhandler!=null){  //初始化具体的文本处理者
			clazz = Class.forName("com.crawler.handler.open.impl."+urlhandler) ; 
			if(this.getParams()==null){
				iOpen = (IOpen) clazz.newInstance();
			}else{
				Constructor  c=clazz.getConstructor(String.class);//获取有参构造  
				iOpen =(IOpen)  c.newInstance(this.getParams());     
				
			}
		}
		
	}
	public String getRowhandler() {
		return rowhandler;
	}
	public void setRowhandler(String rowhandler) {
		this.rowhandler = rowhandler;
	}
	public Integer getThreads() {
		if(threads==null){
			return 0 ;
		}
		return threads;
	}
	public void setThreads(Integer threads) {
		this.threads = threads;
	}
	 
	public String getExists() {
		return exists;
	}
	public void setExists(String exists) {
		this.exists = exists;
	}
	public String getBrower() {
		return brower;
	}
	public void setBrower(String brower) {
		this.brower = brower;
	}
	public IOpen getiOpen() {
		return iOpen;
	}
	public void setiOpen(IOpen iOpen) {
		this.iOpen = iOpen;
	}
 
	public String getUrlhandler() {
		return urlhandler;
	}
	public void setUrlhandler(String urlhandler) {
		this.urlhandler = urlhandler;
	}
	public boolean isMonitor() {
		return monitor;
	}
	public void setMonitor(boolean monitor) {
		this.monitor = monitor;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public boolean isFilter() {
		return filter;
	}
	public void setFilter(boolean filter) {
		this.filter = filter;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHelp() {
		return help;
	}
	public void setHelp(String help) {
		this.help = help;
	}
	public Boolean getImage() {
		return image;
	}
	public void setImage(Boolean image) {
		this.image = image;
	}
	public Integer getSchedule() {
		return schedule;
	}
	
	//是否定时器
	public boolean isSchedule(){
		if(StringUtils.isEmpty(this.getSchedule())){
			return false;
		}
		return true;
	}
	
	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
	}
	public boolean isAlreadyRun() {
		return alreadyRun;
	}
	public void setAlreadyRun(boolean alreadyRun) {
		this.alreadyRun = alreadyRun;
	}
	public String getExtensions() {
		return extensions;
	}
	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}
	public boolean isSort() {
		return sort;
	}
	public void setSort(boolean sort) {
		this.sort = sort;
	}
	public boolean isExportall() {
		return exportall;
	}
	public void setExportall(boolean exportall) {
		this.exportall = exportall;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		this.expire = expire;
	}
	public Expire getEntity() {
		return entity;
	}
	public void setEntity(Expire entity) {
		this.entity = entity;
	}
 
}
