package com.crawler.entity;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.crawler.handler.FileHandlerThread;
import com.crawler.handler.IWebBrower;
import com.crawler.handler.MonitorThread;
import com.crawler.handler.WebBrower;
import com.crawler.handler.WebBrowerThread;
import com.crawler.ui.Widgets;
import com.crawler.util.ConfigUtil;
import com.crawler.util.DriverUtil;
import com.crawler.util.ExcelUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.ImageUtil;
import com.crawler.util.LogUtil;
import com.crawler.util.StringUtils;
/*
 * 浏览器的总包装类:主流程、子流程
 */
public class WebBrowerWrap {
	
	private List<String> labels ; //用户特殊需求的输入
	private Environment env ;
	private Integer mainSize=0 ;
	private Integer detailSize=0 ;
	private Setting setting ;
	private List<IWebBrower> webBrowers ; 
	private ExecutorService fixedThreadPool;
	private ExecutorService downloadThreadPool;
	private ConcurrentLinkedQueue <Product> queues ;
	private ConcurrentLinkedQueue <Product> inputUrlQueues ;  //用户输入的链接队列
	private ConcurrentLinkedQueue <Product> inputParamQueues ;  //用户输入的链接队列
	private ConcurrentLinkedQueue <Product> downloadQueues ;
	private ConcurrentLinkedQueue <String> downloadError ;
	private ConcurrentLinkedQueue <DriverEntity> driverQueues ;
	private ConcurrentLinkedQueue<File> filequeues ;
	private CopyOnWriteArrayList<DriverEntity>  driverEntities ;
	private ScheduledExecutorService scheduledService;
	private OpenUnit mainOpenUnit ;
	
	
	public OpenUnit getMainOpenUnit() {
		return mainOpenUnit;
	}

	public void setMainOpenUnit(OpenUnit mainOpenUnit) {
		this.mainOpenUnit = mainOpenUnit;
	}

	private ArrayList <String> downloadList ;
	
	private static final Logger logger =  Logger.getLogger(WebBrower.class);
	
	public WebBrowerWrap(Widgets widgets ) throws Exception {
		LogUtil.initLog(); //初始化日志配置文件
		this.env = new Environment(widgets); //状态控制
		this.setting= new Setting();
		this.queues = new ConcurrentLinkedQueue <Product>();
//		this.mainXpath = xpath.get mainXpath ; //ConfigUtil.getStringConfigByKey("main.xpath.config");
//		this.detailXpath = detailXpath; //ConfigUtil.getStringConfigByKey("detail.xpath.config");
		this.driverQueues = new ConcurrentLinkedQueue <DriverEntity>();
		this.downloadQueues = new ConcurrentLinkedQueue <Product>();
		this.inputUrlQueues = new ConcurrentLinkedQueue <Product>();
		this.downloadError = new ConcurrentLinkedQueue <String>();
		this.inputParamQueues = new ConcurrentLinkedQueue <Product>();
		this.downloadThreadPool = Executors.newFixedThreadPool(ConfigUtil.getIntegerConfigByKey("download.thread.total"));
		this.webBrowers = new ArrayList<>();
		this.downloadList = new ArrayList<>();
		this.filequeues=new ConcurrentLinkedQueue<>();
		this.driverEntities = new CopyOnWriteArrayList<DriverEntity>();
		
	}
	//重新初始化后,清理浏览器的缓存文件,切换功能，保存配置信息，初始化配置信息
	public void reloadInit(Xpath xpath) throws Exception{
		this.quit();
		this.deleteTemp();
		this.init(xpath);
		this.initWidgets(xpath.getMainOpen().getId());
	}
	
	public void deleteTemp() {
		File file = new File("tmp/user");
		FolderUtil.deleteDir(file);
	}
	//初始化配置信息
	public void init(Xpath xpath) throws Exception{
		
		webBrowers.clear();
		//*************************主线程***************************//
		Storage mainstorage = new Storage(driverEntities,downloadQueues,queues,inputUrlQueues,driverQueues,env,downloadError,downloadThreadPool,inputParamQueues,downloadList);
		IWebBrower mainBrower = new WebBrower(xpath.getMainOpen(),mainstorage);
		
		mainstorage.initStorageHeader(mainBrower.getOpenUnit());
		if(xpath.getMainOpen().getExtensions()!=null){
			mainstorage.setExtensions(Arrays.asList(xpath.getMainOpen().getExtensions().split(",")));
		}
		
		this.webBrowers.add(mainBrower);
		this.mainSize=mainBrower.getOpenUnit().getThreads();
		mainOpenUnit = mainBrower.getOpenUnit();
		
		labels=new ArrayList<>();
		labels.addAll(mainOpenUnit.getLabels()!=null ? Arrays.asList(mainOpenUnit.getLabels().split(",")):new ArrayList<String>());
//      List<JTextField> inputFields = new ArrayList<>();
		//初始化用户定制输入
		env.getWidgets().initInputs(labels);
 
		for (int i = 0; i < mainSize-1; i++) {  //上面已经初始化了一个啦
			mainstorage = new Storage(driverEntities,downloadQueues,queues,inputUrlQueues,driverQueues,env,downloadError,downloadThreadPool,inputParamQueues,downloadList);
			mainBrower = new WebBrower(xpath.getMainOpen(),mainstorage);
			mainstorage.initStorageHeader(mainBrower.getOpenUnit());
			this.webBrowers.add(mainBrower);
		}
		
		/*********************子线程***********************/
		if(!StringUtils.isEmpty(xpath.getDetailOpen())){  //没有子线程
			
			env.setExistsDetailThread(true); //存在子线程
			//初始化第一个子线程
			Storage detailtorage = new Storage(driverEntities,downloadQueues,queues,inputUrlQueues,driverQueues,env,downloadError,downloadThreadPool,inputParamQueues,downloadList); 
			IWebBrower webBrower = new WebBrower(xpath.getDetailOpen(),detailtorage);
			detailtorage.initStorageHeader(mainOpenUnit,webBrower.getOpenUnit());
			this.webBrowers.add(webBrower);
//			logger.info("子程序的默认默认下载文件夹的行处理类："+webBrower.getOpenUnit().getRowhandler());
			this.detailSize = webBrower.getOpenUnit().getThreads();
			
			//处理多个子线程：创建数量为主线程+子线程，因为主线程结束后，子线程顶上
			for (int i = 0; i < mainSize+detailSize-1; i++) {
				detailtorage = new Storage(driverEntities,downloadQueues,queues,inputUrlQueues,driverQueues,env,downloadError,downloadThreadPool,inputParamQueues,downloadList); 
				webBrower = new WebBrower(xpath.getDetailOpen(),detailtorage);
				detailtorage.initStorageHeader(mainOpenUnit,webBrower.getOpenUnit());
				this.webBrowers.add(webBrower);
			}
		}else{
			this.detailSize = 0;
			env.setExistsDetailThread(false); //不存在子线程
		}
		env.setMainOpenUnit(mainOpenUnit);
		
		if(!StringUtils.isEmpty(scheduledService)){
			scheduledService.shutdownNow();
		}
		this.scheduledService  = Executors.newScheduledThreadPool(1);
		
		if(!StringUtils.isEmpty(fixedThreadPool)){
			fixedThreadPool.shutdownNow();
		}
		
		this.fixedThreadPool=Executors.newFixedThreadPool(mainSize+detailSize);
		//是否开启驱动监控进程
		if(mainOpenUnit.isMonitor()){
			scheduledService.scheduleWithFixedDelay(new MonitorThread(driverEntities,env),
					ConfigUtil.getIntegerConfigByKey("monitor.thread.first.time"),
					ConfigUtil.getIntegerConfigByKey("monitor.thread.delay.time"),TimeUnit.SECONDS);
		}
		
		logger.info(mainOpenUnit.getName()+"主程序的下载文件夹的行处理类："+mainOpenUnit.getRowhandler());
		
		//初始化导出的参数
		ConfigUtil.setCache(ConfigUtil.EXPORT_ALL_DATA, ""+mainOpenUnit.isExportall());
		ConfigUtil.setCache(ConfigUtil.EXPORT_SORT, ""+mainOpenUnit.isSort());
		ConfigUtil.setCache(ConfigUtil.FILTER_DUPLICATE_DATA, !mainOpenUnit.isExportall()+"");
		
		//新开一条线程，异步初始化一开始的浏览器
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	try {
	        		
	        		initMainOpen();
	        		
				} catch (Exception e) {
					logger.error("浏览器初始化异常",e);
					e.printStackTrace();
				} 
	        }
	    }).start();	
		
		
	}
	
	public void  start() {
		
		new DateThread(this.mainOpenUnit).start();
		
		//启动定时器
		if(!mainOpenUnit.isAlreadyRun() && mainOpenUnit.isSchedule()){ 
			this.scheduledService.scheduleWithFixedDelay(new WebBrowerThread(this), 2, mainOpenUnit.getSchedule() ,TimeUnit.SECONDS);  //10秒跑一次
			mainOpenUnit.setAlreadyRun(true);
			return ;
		}
		
		
		env.getWidgets().getStopButton().setEnabled(true);
		env.getWidgets().getStartButton().setEnabled(false);
		
		//开始采集是否清空之前的记录
		boolean  clearFlag = ConfigUtil.getStringConfigByKey("excel.start.clear")==null ||
				"true".equals(ConfigUtil.getStringConfigByKey("excel.start.clear"));
		
		try {
			
			for (DriverEntity entity : driverEntities) {
				entity.setTime(System.currentTimeMillis());
			}
			
			env.setMainthreads(this.mainSize);
			
			if(detailSize==0 && env.isExistsDetailThread()){ //存在子页面的采集，但是子页面的线程却设置为0，那说明不重新开启一个页面，而是沿用主页面来做采集
				env.setThreads(1);  //设置总线程数量
			} else if(detailSize!=0){
				env.setThreads(this.mainSize+this.detailSize);  //设置总线程数量
			}
			
			if(clearFlag){
				env.clearUiArea();
			}
			
			env.init();
			queues.clear();
			downloadQueues.clear();
			downloadError.clear();
			inputUrlQueues.clear();
			downloadList.clear();
			inputParamQueues.clear(); //输入的参数清空
			
				if(mainOpenUnit.getLink()!=null && mainOpenUnit.getLink().indexOf("http")!=-1){ //自定义了url，那么界面输入的肯定是参数变量
					for (int i = 0; i < mainOpenUnit.getThreads(); i++) {  //处理多个主线程的链接
						Product product = new Product();
						product.setUrl(mainOpenUnit.getLink());
						inputUrlQueues.offer(product);
					}
				}else if("this".equals(mainOpenUnit.getLink())){
					Product product = new Product();
					product.setUrl("this");
					inputUrlQueues.offer(product);
				}else{
					inputUrlQueues.addAll(env.getPageUrls());
				}
			
			inputParamQueues.addAll(env.getInputParams());
			
			
			for (IWebBrower webBrower : webBrowers) {
				if(clearFlag){
					webBrower.clearStoreData(); //先清理仓库数据
				}
					
				fixedThreadPool.submit(new WebBrowerTask(webBrower,this));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
 
	/*
	 * 停止运行
	 */
	public void  stop() {
		env.setStop();
		for (DriverEntity driverEntity : driverEntities) {
			driverEntity.setTime(null);
		}
	}
	
	/*
	 * 初始化组件的参数信息
	 */
	public void initWidgets(String id) {
		setting.initWidgets(env.getWidgets(),id);
	}
	/*
	 * 线程停止运行
	 * 关闭驱动
	 * 退出驱动服务
	 */
	public void  quit()  {
		
	 	if(env.getWidgets().getAutoSaveBox().isSelected()){
			setting.save(env.getWidgets(),mainOpenUnit.getId());
		}else{
			setting.clear(mainOpenUnit.getId());
		} 
		
		driverEntities.clear();//清空所有的驱动
		driverQueues.clear();
		
		try {
			Runtime.getRuntime().exec("taskkill /f /t /im chromedriver.exe");
			Runtime.getRuntime().exec("taskkill /f /t /im phantomjs.exe");
		} catch (IOException e) {
			logger.error("进程退出异常："+e.getMessage());
		}
		
		//清理日志信息
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		File dir = new File("log");
    	File[] files =  dir.listFiles();
    	Date current = new  Date();
    	for (File file : files) {
    		if(!Arrays.asList("web_error.log","web_info.log").contains(file.getName())){
    			String s =file.getName().substring(file.getName().lastIndexOf(".")+1, file.getName().lastIndexOf(".")+1+10); 
    			Date date = null;
				try {
					date = sdf.parse(s);
				} catch (ParseException e) {
					logger.error("日期格式化异常："+e.getMessage());
				}
    			if(date.before(current)){ //小于当前的日期
    				file.delete();
    			}
    		}
		}
	}
	
	
	
	/*
	 * addAll是浅拷贝
	 * 数据导出
	 */
	public void  export()  {
		boolean flag = ConfigUtil.getBooleanConfigByKey(ConfigUtil.EXPORT_ALL_DATA);
		boolean sort = ConfigUtil.getBooleanConfigByKey(ConfigUtil.EXPORT_SORT);
		//如果存在两个或以上的子线程收集器,将其余的数据全部添加的第一个子线程的数据list
		List<IWebBrower> details = new ArrayList<>();
		List<List> data = new ArrayList();  //多个浏览器数据整合到list
		List<String> errorList= new ArrayList<>();  //加载失败的链接
		List<String> missData= new ArrayList<>();  //漏采集的数据
		//存在子线程
		for (IWebBrower iWebBrower : webBrowers) {
			errorList.addAll(iWebBrower.getStorage().getErrorList());
			if(!iWebBrower.getOpenUnit().isMajor()){
				details.add(iWebBrower);
			}
		}
		
		//只有主线程数据
		if(details.size()==0){
			details.addAll(webBrowers);
		}
		
		int urlsize = 0;
		for (IWebBrower iWebBrower : details) {
			urlsize+=iWebBrower.getStoreDataList().size();
		}
		logger.info("采集总行(包括漏字段数据)："+urlsize);
		
		for (int i = 0; i < details.size(); i++) {
			IWebBrower  iWebBrower = details.get(i);
			int headers = iWebBrower.getStoreData().getHeader().size();
			for (List row : iWebBrower.getStoreDataList()) {
				if(row.size()==headers){  //完整的行数据
					data.add(row);
					continue;
				}
					
				if(flag){//采集不全的数据也输出
					for (int j = 1; j <= headers; j++) {
						if(j > row.size()){
							row.add(j-1, "");
						}
					}
					data.add(row);
					continue;
				}
				
				//记录漏采集的数据
				String miss = org.apache.commons.lang3.StringUtils.join(row, ","); 
				if(!missData.contains(miss) && !flag){
					missData.add(miss);
				}
			}
		}
		
		if(sort) { //排序,根据输入的顺序对应采集的第一个字段进行排序
			List<List> sortList = new ArrayList();  //多个浏览器数据整合到list
			List<Product> inputs =  env.getInputParams();
			for (Product product : inputs) {
				for (List row : data) {
					if( product.getParam().equals(row.get(0))){
						sortList.add(new ArrayList<>(row));
					}
				}
			}
			data = sortList;
		}
		
		
//		details.get(0).getStoreData().setList(data);  //所有数据  
		StoreData storeData = new StoreData(ConfigUtil.getIntegerConfigByKey("init.excel.size"));
		storeData.setList(data);
		storeData.setShowHeaders(details.get(0).getStoreData().getShowHeaders());
		storeData.setHeader(details.get(0).getStoreData().getHeader());
		storeData.setSheet(details.get(0).getStoreData().getSheet());
		
		logger.info("采集总行(过滤漏字段)："+data.size());
		if(missData.size()!=0){
			logger.info("数据不全总行:"+missData.size());
			for (String string : missData) {
				logger.info("数据不全:"+string);
			}
		}
		
		if(errorList.size()!=0){
			logger.info("漏采集链接数"+errorList.size()+"条,如下:");
			for (String error : errorList) {
				logger.info(error);
			}
		}
		
		if(errorList.size()!=0){
			logger.info("漏采集链接数"+errorList.size()+"条,如下:");
			for (String error : errorList) {
				System.out.println(error);
			}
		}

		
		if(downloadError.size()!=0){
			logger.info("异常下载链接数："+downloadError.size()+",如下：");
			StringBuffer sb = new StringBuffer();
			for (String url : downloadError) {
				sb.append("\r\n"+url);
			}
			logger.info(sb.toString());
		}
		
		try {
			ExcelUtil.createExcel(storeData,env.getOutputPath() );
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.getMessage(), "提示 " ,JOptionPane.INFORMATION_MESSAGE);
			logger.error(e.getMessage());
		}
		
	}
	
	public void showLogArea(String message) {
		env.showLogArea(message);
	}
	
	/*
	 * 获取表头列
	 * 如果只有一个线程，就作为数据的输出
	 * 如果有子线程，提取其中的一个子线程作为输出的列
	 */
	public String[] getShowHeaders(){
		IWebBrower webBrower=webBrowers.get(webBrowers.size()-1);
		List<String> headersList = webBrower.getStoreData().getShowHeaders();
		String [] headers = new String[headersList.size() +1];
		headers[0]="序号";
		for (int i = 0; i < headersList.size(); i++) {
			headers[i+1]=headersList.get(i).replace("文件夹名-", "");
		}
		
		return headers;
	}

	/*
	 * 浏览器的线程执行类
	 */
	public class WebBrowerTask implements Callable{
		private IWebBrower webBrower;
		private WebBrowerWrap webBrowerWrap;
		public WebBrowerTask(IWebBrower webBrower,WebBrowerWrap webBrowerWrap) {
			this.webBrower=webBrower;
			this.webBrowerWrap=webBrowerWrap;
		}
		@Override
		public Object call() throws Exception {
			webBrower.start();
			boolean flag  = ConfigUtil.getBooleanConfigByKey("excel.schedule.auto.export");
			
			if(flag){ //自动导出excel
				webBrowerWrap.export();
			}
			return null;
		}
	}

	public ExecutorService getDownloadThreadPool() {
		return downloadThreadPool;
	}

	/*
	 * 处理图片
	 */
	public void handerImage() {
		filequeues.clear();
		env.setFilehandlerStartTime(System.currentTimeMillis());
		
		ImageProperty image =  env.getWidgets().getImageProperty();
		String folderPath =  env.getWidgets().getImageFolderField().getText();
    	File dir = new File(folderPath);
    	File[] files =  dir.listFiles();
    	if(files==null){
    		logger.error("不存在文件，过滤啥？？");
    	}else{
    		ImageUtil.method(files, filequeues);
    	}
    	
        int size = filequeues.size()/ConfigUtil.getIntegerConfigByKey("download.file.hanlder.size")+1;
        env.setHandlerFileThreads(size); 
        for (int i = 0; i <size; i++) {
        	 downloadThreadPool.execute(new FileHandlerThread(filequeues, image,env));
		}
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	/*
	 * 初始化第一次软件自动打开的页面
	 */
	public void initMainOpen() throws Exception {
		
		//初始化导出excel是否覆盖，定时器默认以时间命名导出excel
		if(mainOpenUnit.isSchedule()){
			ConfigUtil.setCache(ConfigUtil.EXCEL_COVER,"false");
		}else{
			ConfigUtil.setCache(ConfigUtil.EXCEL_COVER,ConfigUtil.getStringConfigByKey(("common.excel.cover")));
		}
		
		if(mainOpenUnit.getInit()==null){  //无需初始化启动的页面
			return ;
		}
	
		logger.info("-----------软件启动页面初始化-----------");
		Storage storage = null;
		for (IWebBrower iWebBrower : webBrowers) {
			if(iWebBrower.getOpenUnit().isMajor()){
				storage= iWebBrower.getStorage();
			}
		}
		storage.getEnv().showLogArea("正在打开浏览,请稍等。。。");
		Thread.sleep(500);
		storage.getEnv().showLogArea("正在打开浏览,请稍等。。。");
		Thread.sleep(500);
		storage.getEnv().showLogArea("正在打开浏览,请稍等。。。");
		
		storage.initDriverEntity(mainOpenUnit.getBrower(),mainOpenUnit.getImage()); //获取驱动
		DriverUtil.navigateUrl(storage, mainOpenUnit.getInit(),mainOpenUnit.getTimeout());  //初始化页面
		storage.getEnv().showLogArea("浏览器打开完毕，请进行采集操作！！！");
		
		
		//未开启就不给过滤
		if(!mainOpenUnit.isFilter()){ 
			this.env.getWidgets().getAutoBox().setEnabled(false);
		}
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	class DateThread extends Thread{
	     
		private OpenUnit openUnit ; 
		
	    public DateThread( OpenUnit openUnit){
	    	this.openUnit = openUnit;
	    }
	     
	    @Override
	    public void run() {
	    	try {
	    		Expire e = new Expire(openUnit.getExpire());
	    		openUnit.setEntity(e);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
}
