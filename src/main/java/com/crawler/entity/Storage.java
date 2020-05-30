package com.crawler.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebElement;

import com.crawler.handler.row.IRowHander;
import com.crawler.util.ConfigUtil;
import com.crawler.util.DriverUtil;
import com.crawler.util.ImageUtil;

/*
 * 数据存储仓库类
 */
public class Storage {
	
	  private List<String> extensions  ; // 插件名称
	  
	  private String urlLabel ; // 提供给子程序采集的url索引
	
	  private List<String> downloadLables ; 
	  
	  private String type; //input用户从界面输入，url
	  
	  private String driverName ; 
	  
	  private ConcurrentLinkedQueue <Product> queues ;
	  
	  private ConcurrentLinkedQueue <Product> inputUrlQueues ;  //用户输入的链接队列
	  
	  private ConcurrentLinkedQueue <Product> downloadQueues ;
	  
	  private ArrayList <String> downloadList ;
	  
	  private ConcurrentLinkedQueue <Product> inputParamQueues ;
	  
	  private final ConcurrentLinkedQueue <String> downloadError ;
	  
	  private ConcurrentLinkedQueue <DriverEntity> driverQueues ;
	  
	  private StoreData storeData ;  //数据存储

	  private boolean download; //是否存在下载
		
	  private Environment env;  //全局环境
	  
	  private List<String> errorList ;  //采集后在最终的错误连接
	  
	  private boolean major ; //是否主线程
	  
	  private Product product ;//当前子页面线程运行的产品
	  
	  private DriverEntity driverEntity;  //目前运行的驱动
	  
	  private WebElement currentElement;  //目前运行的元素
	  
	  private String currentInput;  //目前运行的输入框数据
	  
	  private Document currentDoc;   //目前运行的元素
	 
	  private Integer pageCount; //当前分页计数器
	  
	  private IRowHander iRowHander;
	  
	  private ExecutorService downloadThreadPool;
	  
//	  private Lock lock ;
	  
	  private List<DriverEntity> driverEntities;
	  
	  private boolean isContinue; //是否继续采集
	  
	  private static final Logger logger =  Logger.getLogger(Storage.class);
	  
	  public Storage( CopyOnWriteArrayList<DriverEntity> driverEntities,  ConcurrentLinkedQueue <Product> downloadQueues , ConcurrentLinkedQueue <Product> queues , 
			  ConcurrentLinkedQueue <Product> inputUrlQueues ,
			  ConcurrentLinkedQueue <DriverEntity> driverQueues,Environment env, 
			  ConcurrentLinkedQueue <String> downloadError,ExecutorService downloadThreadPool,ConcurrentLinkedQueue <Product> inputParamQueues,ArrayList<String> downloadList ) throws Exception {
		  this.driverEntities = driverEntities;
		  this.env = env;
		  this.queues = queues;
		  this.driverQueues = driverQueues;
		  this.inputParamQueues = inputParamQueues;
		  this.downloadQueues = downloadQueues;
		  this.inputUrlQueues = inputUrlQueues;
		  this.downloadThreadPool = downloadThreadPool;
		  this.downloadError = downloadError;
		  this.errorList=new ArrayList<>();
		  this.storeData = new StoreData(ConfigUtil.getIntegerConfigByKey("init.excel.size"));
		  this.downloadLables =new ArrayList<>();
		  this.extensions=new ArrayList<>();
		  this.downloadList =downloadList;
	  }
	  
	  /*
	   * 获取最后一行
	   */
	  public List<String> getLastRow() {
		   return  storeData.getList().get(storeData.getList().size()-1);  //最后一行
	  }
	  //重新采集：子线程如果操作页面的时候出现异常，则把已经采集的数据删除，把当前运行的产品重新放回队列
	  public void  repeat() throws Exception{
		  if(!major && storeData.getList().size()>0){
			  List row =  getLastRow();
			  if(row.size()<storeData.getHeader().size() ){
				  storeData.getList().remove(row);
				  this.push(product);
			  }
		  }
	  }
	  
	  /*
	   * 设置行处理者
	   */
	  public void initRowHandler(OpenUnit openUnit){
		 this.iRowHander= openUnit.getiRowHander();
	  }
	  
	  /*
	   * 是否已经完成生产
	   */
	  public boolean isCompleteProduct(){
		 return env.isComplete();
	  }
	  
	  public void initStorageHeader(OpenUnit openUnit) throws Exception {
		  storeData.setSheet(openUnit.getSheet()) ;
		  initRowHandler(openUnit);
		  initHeader(openUnit);
		  driverName = openUnit.getBrower();
		  logger.info("\n*****************************配置初始化完毕*******************************");
	  }
	  
	  public void initStorageHeader(OpenUnit main,OpenUnit detail) throws Exception {
		  storeData.setSheet(main.getSheet());
		  initRowHandler(main);
		  initHeader(main);
		  initHeader(detail);
		  driverName = detail.getBrower();
		  logger.info("\n------------------------------子程序初始化完毕---------------------------------");
	  }
	  
	  //采集过程中出现异常，导致最后一行数据采集不全，所以清理最后一行数据，
	  public void clearLastRow() {
		  int headers = storeData.getHeader().size();
		  if(storeData.getList().size()==0){
			  return ;
		  }
		  
		  List list =storeData.getList().get(storeData.getList().size()-1);
		  if(list.size()!=headers){
			  storeData.getList().remove(storeData.getList().size()-1);
		  }
	  }
	  
	  /**
       * 生产
       */
      public void push(Product p) throws InterruptedException {
          queues.offer(p) ;
      }

      
      /**
       * 消费
       */
      public Product pop() throws InterruptedException {
    	  logger.info("剩余链接:"+queues.size());
    	  this.product =  queues.poll();
    	  return product ;
      }
      
    /*
  	 * 初始化excel表头
  	 */
  	private void initHeader(OpenUnit openUnit) throws Exception{
  		setMajor(openUnit.isMajor());
  		List<Unit> result = new ArrayList<>();
  		loopText(openUnit, result);
  		for (Unit unit : result) {
  			storeData.addHeader(unit.getName());
  			TextUnit textUnit =(TextUnit) unit;
  			if(textUnit.isDownload()){ //初始化是否下载
  				 this.setDownload(true);
  				 this.downloadLables.add(textUnit.getName());
  			}
  			if(textUnit.isUrl()){
  				this.setUrlLabel(textUnit.getName());
  			}
  			
  			if(textUnit.isShow()){ //字段是否作为显示的字段
  				storeData.addShowHeaders(textUnit.getName());
  			}
  		}
  	}
  	
	public  void loopText(Unit unit , List<Unit> list) throws Exception{
		for (Unit u : unit.getChildUnit()) {
			if(TextUnit.TYPE.equals( u.getType())){
				list.add(u);
				TextUnit tu =(TextUnit) u ; 
				if(tu.getSuffix()!=null){
					env.setSuffix(tu.getSuffix());
				}
				
				if(tu.isFilename() ){ //是否作为文件名称
					env.setFileName(tu.getName());
				}
				
	  			if(storeData.getHeader().contains(u.getName())){
	  				throw new Exception("++++++++++++++++++++++++++重复字段名:"+u.getName()+"+++++++++++++++++++++++++++++++++++");
	  			}
	  				
			}
			
			if(InputUnit.TYPE.equals(u.getType())){
				InputUnit input = (InputUnit) u;
				if("input".equals(input.getValue())){
					type= "input";
				}
			}else if(OpenUnit.TYPE.equals(u.getType())){
				OpenUnit open = (OpenUnit) u;
				if(Arrays.asList("input","queues").equals(open.getLink())){
					type= "url";
				}
			}
			
			if(null!=u.getChildUnit()){
				loopText(u, list);
			}
		}
	}

	public StoreData getStoreData() {
		return storeData;
	}

	public void setStoreData(StoreData storeData) {
		this.storeData = storeData;
	}
 
	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public ConcurrentLinkedQueue <Product> getQueues() {
		return queues;
	}

	public void setQueues(ConcurrentLinkedQueue <Product> queues) {
		this.queues = queues;
	}

	public boolean isMajor() {
		return major;
	}

	public void setMajor(boolean major) {
		this.major = major;
	}

	public DriverEntity getDriverEntity(String brower, Boolean isImage){
		return initDriverEntity(brower,isImage);
	}
	
	public DriverEntity  initDriverEntity(String brower,Boolean isImage) {
	 	if(driverEntity!=null ){ //google浏览器，不能选择phtomjs
			if( (driverEntity.isChrome() && brower.indexOf("chrome")!=-1 ) || brower.indexOf("chrome")==-1 ){
				return driverEntity;
			}
		}
		
		//驱动队列中提取浏览器
		 Iterator<DriverEntity> iter=driverQueues.iterator();
		 while (iter.hasNext()) {
			 DriverEntity entity =  iter.next();
			 if( (brower.indexOf("chrome")!=-1 && entity.isChrome()) || brower.indexOf("chrome")==-1){
				 driverEntity = entity;
				 iter.remove();
				 return entity;
			 }
		} 
		
		driverEntity= DriverUtil.initDriver(this,brower,isImage);
		
	 
		return driverEntity ;
	}

	public void backDriverQuences(DriverEntity entity){
		if(entity!=null){
			driverQueues.offer(entity);
		}
	}
	
	public List<String> getErrorList() {
		return errorList;
	}

	public void addErrorList(String errorUrl) {
		errorList.add(errorUrl);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public DriverEntity getDriverEntity() {
		return driverEntity;
	}

	public void removeDirverEntity() {
		removeDirverEntity( this.driverEntity);
		this.driverEntity = null; //移除了驱动，将目前的驱动置空
	}
	
	//移除当前的驱动信息
	public void removeDirverEntity( DriverEntity dirverEntity) {
		try {
			dirverEntity.getWebDriver().quit();
		} catch (Exception e) {
			logger.error("驱动销毁异常："+e.getMessage());
		}
		try {
			dirverEntity.getDriverService().stop();
		} catch (Exception e) {
			logger.error("服务销毁异常："+e.getMessage());
		}
		
		driverEntities.remove(dirverEntity);
	}

	public WebElement getCurrentElement() {
		return currentElement;
	}

	public void setCurrentElement(WebElement currentElement) {
		this.currentElement = currentElement;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void clearPageCount() {
		this.pageCount = null;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	//采集线程运行完毕后,启动下载高并发服务
	public void reduceThreads() {
		if(isDownload()){
			List<String> header = storeData.getHeader();
			List<List> data = storeData.getList();
			Product product  = null;
			
			for (List<String> row : data) {
				if(row.size()!=header.size()){  //不全的数据不用下载
					continue;
				}
				
				for (String download : downloadLables) {  //循环所有需要下载的字段名称
					
					Integer index = getDownloadLableIndex(download);
					String https = null;
					
					 product = new Product();
					 product.setList(row);
					 product.setHeader(header);
					 product.setDownloadIndex(index);
					 https =  row.get(index) ;
					 
					 if(StringUtils.isEmpty(https) ){
						 continue ;
					 }
					 
					 if(  https.trim().lastIndexOf("http")!=0){//http的位置不为0,说明有多个http下载，需要做分割处理
						 for (String http : https.split(",")) {
							 if(http.trim().length()==0){
								 continue;
							 }
							 Product p = new Product();
							 p.setList(row);
							 p.setHeader(header);
							 p.setUrl(http.trim());
							
							 //重复下载
							 if( OpenUnit.REPEAT_DOWNLOAD.equals(this.env.getMainOpenUnit().getSpecial())){
								 downloadQueues.offer(p);
								 downloadList.add(p.getUrl());
							 }else if( !downloadList.contains(p.getUrl()) ){ //非重复下载
								 downloadQueues.offer(p);
								 downloadList.add(p.getUrl());
							 }
							 
						}
					 }else{
						 if(https.trim().length()==0){
							 continue;
						 }
						 product.setUrl(https.trim()); //最后的一个链接作为下载的链接
						 
						 //重复下载
						 if( OpenUnit.REPEAT_DOWNLOAD.equals(this.env.getMainOpenUnit().getSpecial())){
							 downloadQueues.offer(product);
							 downloadList.add(product.getUrl());
						 }else if( !downloadList.contains(product.getUrl()) ){ //非重复下载
							 downloadQueues.offer(product);
							 downloadList.add(product.getUrl());
						 }
					 }
				}
			}

		}
		
		//加锁处理
		synchronized(this.getEnv()){
			if(this.isMajor()){  //主页面线程
				this.env.reduceMainthreads();
			}else{
				this.env.reduceThreads();
			}
			
			boolean finish = env.isFinish();
			if( finish && ( env.isStop() ||  !isDownload())){
				env.complatePage() ; 
			}
			
			//页面采集线程运行完毕,启动下载高并发服务
			if(!env.isStop() && finish && isDownload()){
				env.setDownloadStartTime(System.currentTimeMillis());
				if(downloadQueues.isEmpty()){
					env.showLogArea("下载链接为空！！！！！");
					env.setDownloadEndTime(System.currentTimeMillis());
					env.complatePage() ;
				}else{
					
					int downloadThreads = downloadQueues.size()/2 ; //下载线程为需要下载图片的一半
					if(downloadThreads==0) downloadThreads= 1;
					if(downloadThreads > ConfigUtil.getIntegerConfigByKey("download.thread.total")){  //最大的线程数量
						downloadThreads = ConfigUtil.getIntegerConfigByKey("download.thread.total");
					}
					env.initDownloadThreads(downloadThreads);
					for (int i = 0; i < downloadThreads; i++) {
						downloadThreadPool.execute(new DownloadThread(downloadQueues,env,downloadError));
					}
				}
			}
		}
		
		
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}
	
	/*
	 * 下载图片线程
	 */
	public class DownloadThread implements Runnable{

		private ConcurrentLinkedQueue <Product> downloadQueues ;
		private Environment env ; 
		private Integer threshold = ConfigUtil.getIntegerConfigByKey("download.error.threshold");
		public DownloadThread(ConcurrentLinkedQueue <Product> downloadQueues ,Environment env ,ConcurrentLinkedQueue<String> downloadError) {
			this.downloadQueues = downloadQueues ;
			this.env = env ;
		}
		
		@Override
		public void run() {
			Product product =null;
			Error error=null;
			String fullPath = null;
			while ( !env.isStop() ) {
				
				if(downloadQueues.isEmpty()){
					break;
				}
				
				product=downloadQueues.poll();
				if(product==null){  //弹出的数据为空
					continue;
				} 
				
				if( product.getThreshold()>threshold){
					downloadError.add(product.getUrl());
					continue;
				}
				
				
				fullPath = iRowHander.handerRow(product,env);
				File file = new File(fullPath);
				if(file.exists()){
					continue;
				}
				try {
					if(StringUtils.isEmpty(product.getUrl())){
						continue;
					}
//					error = ImageUtil.upload(product.getUrl(), fullPath);
					error = ImageUtil.uploadImage(product.getUrl(), fullPath);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("下载异常:"+product.getUrl());
					getEnv().showLogArea("运行中。。。");
					downloadQueues.offer(product);
					product.setAddThreshold();
					continue;
				}
				
				if(error!=null && product.getThreshold()<threshold){
					product.setAddThreshold();
					downloadQueues.offer(product);
					continue;
				}
				if(product.getThreshold()>=threshold){
					downloadError.add(product.getUrl());
				}
				
				if(error==null){
					getEnv().showLogArea("已下载:"+product.getUrl());
				}
			}
			
			env.reduceDownloadThreads();
		}
	}

	public void setDirverEntity(DriverEntity dirverEntity) {
		this.driverEntity = dirverEntity;
	}
	
	//主线程的用户输入连接队列
	public void  putInputParamProduct(Product product) {
		inputParamQueues.offer(product);
	}
	
	//主线程的用户输入连接队列
	public Product getInputParamProduct() {
		return inputParamQueues.poll();
	}
	
	public boolean isInputParamQueuesEmpty() {
		return inputParamQueues.isEmpty();
	}
	
	public ConcurrentLinkedQueue<Product> getInputParamQueues() {
		return inputParamQueues;
	}

	public void setInputParamQueues(ConcurrentLinkedQueue<Product> inputParamQueues) {
		this.inputParamQueues = inputParamQueues;
	}

	/*
	 * 初始化输入的信息
	 */
	public void setInputUrlProduct(List urls) {
		inputUrlQueues.addAll(urls);
	}
	
	//主线程的用户输入连接队列
	public Product getInputUrlProduct() {
		product =  inputUrlQueues.poll();
		return product;
	}

	public void putInputUrlProduct(Product p) {
		inputUrlQueues.offer(p);
	}
	
	/*
	 * 重新初始化分页url
	 */
	public void initInputUrlQueues(String url) {
		inputUrlQueues.addAll(env.getPageUrls(url));
	}
	
	
	public void  repeatInputUrlProduct() {
		inputUrlQueues.offer(product);
	}
	
	public boolean isInputUrlEmpty() {
		return inputUrlQueues.isEmpty();
	}
	
	public boolean isInputParam() {
		return "input".equals(this.type) ? true : false;
	}
	/*
	 * 浏览器驱动
	 */
	public boolean isChrome() {
		if("chrome".equals(this.driverName.split("-")[0])){
			return true;
		}
		return false;
	}
	
	public boolean isPhone(){
		if(driverName.indexOf("-")!=-1){
			if("phone".equals(this.driverName.split("-")[1])){
				return true;
			}else{
				logger.error("错误的浏览器驱动类型设置:"+this.driverName.split("-")[1]);
			}
		}
		return false;
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	public ConcurrentLinkedQueue<Product> getInputUrlQueues() {
		return inputUrlQueues;
	}

	public void setInputUrlQueues(ConcurrentLinkedQueue<Product> inputUrlQueues) {
		this.inputUrlQueues = inputUrlQueues;
	}
	
	/*
	 *获取自定义的参数	 
	 */
	public String getLabelByIndex(Integer index){
		return this.getEnv().getWidgets().getInputFields().get(index).getText() ; 
	}
	/*
	 * 下载图片的索引位置
	 */
	public Integer getDownloadLableIndex(String download) {
		
		return  storeData.getHeader().indexOf(download);
	}

	public void setDownloadLable(String downloadLable) {
		this.downloadLables.add(downloadLable);
	}

	public String getUrlLabel() {
		return urlLabel;
	}

	public void setUrlLabel(String urlLabel) {
		this.urlLabel = urlLabel;
	}

	public Document getCurrentDoc() {
		return currentDoc;
	}

	public void setCurrentDoc(Document currentDoc) {
		this.currentDoc = currentDoc;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
	}
	
	/**
	 * 数据填充工具
	 * @throws Exception 
	 */
	public static void addText(String name, String value,Storage storage) throws Exception {
		storage.getStoreData().addText(value, name,storage);
	}

	public String getCurrentInput() {
		return currentInput;
	}

	public void setCurrentInput(String currentInput) {
		this.currentInput = currentInput;
	}
	
	

}
