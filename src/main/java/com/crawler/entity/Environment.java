package com.crawler.entity;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;

import com.crawler.ui.Widgets;
import com.crawler.util.StringUtils;

/*
 * 全局运行环境控制类
 */
public class Environment {

	private long startTime ; //页面采集的开始时间
	private long endTime ;   //页面采集的结束时间
	private OpenUnit mainOpenUnit ; //主程序
	
	private static final Logger logger =  Logger.getLogger(OpenUnit.class);
	
	private boolean complete; // 完成生产

	private boolean stop; // 停止运行
	
	private volatile int mainthreads ; //主页面的线程的总运行数量
	
	private volatile int threads ; //主页面+子页面的线程的总运行数量，因为需要子线程顶上
	
	private volatile int  downloadThreads ; //下载图片的线程的总运行数量
	
	private volatile int  handlerFileThreads ; //图片处理总线程
	
	private long downloadStartTime ; //下载开始时间
	private long downloadEndTime ; //下载开始时间
	
	private long filehandlerStartTime ; //文件图片处理开始时间
	private long filehandlerEndTime ; //文件图片处理开始时间
	
	private Widgets widgets; //用户界面的控件 
	
	private boolean isExistsDetailThread ; //是否有子线程 
	
	private volatile boolean initUrl ; //初始化initurl
	
	private Lock lock ;
	
	private String suffix ; 
	private String fileName ; //文件名所在的索引位置
	
	public Environment(Widgets widgets) {
		this.widgets = widgets;
		this.lock= new ReentrantLock();
		this.initUrl = true;
		this.stop = true;
	}

	public void init(){
		this.startTime=System.currentTimeMillis();
		this.downloadEndTime=0;
		this.filehandlerEndTime=0;
		this.complete = false;
		this.stop = false;
		this.initUrl = true;
		showLogArea("启动中!");
	}
	
	public void clearUiArea() {
		clearTableArea();
		clearLogArea();
	}
	
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete() {
		this.complete = true;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop() {
		this.stop = true;
	}
	
	public void setStop( boolean flag) {
		this.stop = flag;
	}

	/*
	 * 子页面线程总数减一
	 */
	 public  int reduceThreads() {
		this.threads-=1;
		 this.showLogArea(Thread.currentThread().getName()+"子线程销毁,剩余线程"+threads);
		if(threads==0){
			setEndTime(System.currentTimeMillis());	
		}
		return threads;
	}
	 
	 /*
	  * 主页面线程减一
	  */
	 public  void reduceMainthreads(){
		 this.mainthreads-=1;
		 this.showLogArea(Thread.currentThread().getName()+"主线程销毁，剩余主线程"+mainthreads);
		 if(mainthreads==0){
			 setEndTime(System.currentTimeMillis());
			 this.setComplete();
		 }
	 }
	 
	 /*
	  * 文件图片线程减一
	  */
	 public  void reduceFileThreads() {
		 lock.lock();
		 this.handlerFileThreads-=1;
		 this.showLogArea(Thread.currentThread().getName()+"图片处理线程销毁，剩余线程"+handlerFileThreads);
		 if(handlerFileThreads==0){
			 setFilehandlerEndTime(System.currentTimeMillis());
			 complatePage();
		 }
		 lock.unlock();//解锁
	 }
	 
	/*
	 * 初始化按钮
	 */
	public void initButton(){
		widgets.getStartButton().setEnabled(true);
		widgets.getExportButton().setEnabled(true);
		widgets.getFilerButton().setEnabled(true);
		widgets.getStopButton().setEnabled(false);
	}
	 
	 /*
	  * 下载线程总数减一
	  */
	 public void reduceDownloadThreads() {
		 lock.lock();
		 this.downloadThreads-=1;
		 this.showLogArea(Thread.currentThread().getName()+"下载线程销毁，剩余线程"+downloadThreads);
		 if(downloadThreads==0){
			 //调用点击事件自动的图片处理
			 this.setDownloadEndTime(System.currentTimeMillis());
			 if(widgets.getAutoBox().isSelected() && !isStop() && !StringUtils.isEmpty(widgets.getImageFolderField().getText())){ 
				 widgets.clickFilerButton();
			 }else{
				 complatePage();
			 }
		 }
		 lock.unlock();//解锁
	 }

	public JTextArea getLogArea() {
		return widgets.getLogArea();
	}

	/*
	 * 用户界面的日志信息
	 */
	public  void showLogArea(String message) {
		if(widgets.getLogArea()==null){
			return ;
		}
		widgets.getLogArea().append(message+"\r\n");
		widgets.getLogArea().setCaretPosition(widgets.getLogArea().getDocument().getLength());
		logger.info(message);
	}
	/*
	 * 用户界面的数据table
	 */
	public synchronized void showTableArea(List<String> list ,List<String> header,List<String> showHeaders) {
		List<String> shows = new ArrayList<>();
		shows.add(widgets.getModel().getRowCount()+1+"");
		for (int i = 0; i < header.size(); i++) {
			if(showHeaders.contains(header.get(i))){
				shows.add(list.get(i));
			}
		}
		
		widgets.getModel().insertRow(widgets.getModel().getRowCount(), shows.toArray(new String[shows.size()]));
		JTable  table = widgets.getTable();
		int rowCount=table.getRowCount();
		table.getSelectionModel().setSelectionInterval(rowCount-1,rowCount-1);
	    Rectangle rect=table.getCellRect(rowCount-1,0,true);
//	    table.updateUI();
	    try {
	    	table.scrollRectToVisible(rect);
		} catch (Exception e) {
			logger.error("更新用户界面异常:"+e.getMessage());
		}
	}
	
	/*
	 * 用户界面的数据table
	 */
	public void clearTableArea() {
		widgets.getModel().setRowCount(0);
	}
	
	/*
	 * 用清空户界面的数据Log
	 */
	public void clearLogArea() {
		widgets.getLogArea().setText("");
	}
	
	/*
	 *excel:用户界面的输出路径 
	 */
	public String getOutputPath() {
		String path =widgets.getFolderField().getText();
		//这便是读取桌面路径的方法
		if(StringUtils.isEmpty(path)){
			FileSystemView fsv = FileSystemView.getFileSystemView();
			File desktop=fsv.getHomeDirectory();   
			return desktop.getPath();
		}else{
			return path ;
		}
	}
	
	//asdfadf/p{1}aa.html
	//url通过用户界面设置的分页，生产一批分页链接
	public List<Product> getPageUrls(String url ) {
		//设置了分页规则
		List<Product> urls =new ArrayList<>();
		if(!StringUtils.isEmpty(widgets.getPageParamValue())){
			Integer start = widgets.getFromFieldValue();
			Integer end = widgets.getToFieldValue();
			String param = widgets.getPageParamValue().replace("{", "").replace("}", ""); //url中要替换的
			
			String regexMatch = "\\{(.+?)\\}";
			Pattern p = Pattern.compile(regexMatch);
			Matcher m = p.matcher(widgets.getPageParamValue());
			String prefix = "";  //页码变量
			if(m.find()){
				prefix= m.group(0);
			}else{
				logger.error("没有找要要替换的参数");
			}
			
//			String param = getUrlParam(url, widgets.getPageParamValue()); //分页参数  page=1这种的方式
			String pageUrl = null;
			for (int i = start; i <= end; i++) {
				Product product = new Product();
//				if(){ //强制加上参数
//					pageUrl
//				}else{
//				}
				if(url.indexOf(param)!=-1){ //url中存在分页的参数就替换
					pageUrl=url.replaceFirst(param, widgets.getPageParamValue().replace(prefix, i+""));
				}else{ //url不存在分页链接，则在最后增加
					pageUrl=url+ widgets.getPageParamValue().replace(prefix, i+"");
				}
				product.setUrl(pageUrl);
				urls.add(product);
			}
		}else{
			Product product = new Product() ; 
			product.setUrl(url);
			urls.add(product);
		}
		return urls;
	}
	
	/*
	 * 获取用户界面的用户输入参数
	 */
	public List<Product> getInputParams() {
		JTextArea  urlArea = widgets.getUrlArea();
		List<Product> params =new ArrayList<>();
		
		for (String input : urlArea.getText().split("\n")) {
			
			if(StringUtils.isEmpty(input.trim())){
				continue;
			}
			Product product = new Product();
			product.setParam(input.trim());
			params.add(product);
		}
		
		return params;
	}
	
	/*
	 * 默认用户界面的url输入域
	 * http://www.abbs.com.cn/bbs/post/page?bid=52&tpg=2&sty=1&s=0&age=0
	 */
	public List<Product> getPageUrls() {
		JTextArea  urlArea = widgets.getUrlArea();
		List<Product> urls =new ArrayList<>();
		
		for (String url : urlArea.getText().split("\n")) {
			
			if(StringUtils.isEmpty(url.trim())){
				continue;
			}
			
			List<Product> pages = getPageUrls( url );
			for (Product product : pages) {
				urls.add(product);
			}
		}
		
		return urls;
	}
	
	/*
	 * 获取从开始页面到结束页面的url
	 */
	public List<Product> getPageUrls(Integer start , Integer end) {
		JTextArea  urlArea = widgets.getUrlArea();
		List<Product> urls =new ArrayList<>();
		
		for (String url : urlArea.getText().split("\n")) {
			
			if(StringUtils.isEmpty(url.trim())){
				continue;
			}
			
			List<Product> pages = getPageUrls( url );
			for (Product product : pages) {
				urls.add(product);
			}
		}
		
		return urls;
	}
	

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}
	
 
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getDownloadPath(){
		
		String path =widgets.getImageFolderField().getText();
		if(StringUtils.isEmpty(path)){
			FileSystemView fsv = FileSystemView.getFileSystemView();
			File desktop=fsv.getHomeDirectory();   
			path= desktop.getPath()+File.separator;
		}
			
		return path ;
			
	}

	/*
	 * 页面采集完成的输出
	 */
	public void complatePage() {
		setStop();
		showLogArea("页面采集总耗时:"+(getEndTime()-getStartTime())/1000+"秒");
		if(getDownloadEndTime()!=0){
			showLogArea("下载总耗时:"+(getDownloadEndTime() -getDownloadStartTime())/1000+"秒");
		}
		if(getFilehandlerStartTime()!=0){
			showLogArea("图片处理总耗时:"+(getFilehandlerEndTime()-getFilehandlerStartTime())/1000+"秒");
		}
		
		//不是定时器，采集完成后需要提示
		if(!this.mainOpenUnit.isSchedule()){
			JOptionPane.showMessageDialog(null,"运行完毕！", "提示 " ,JOptionPane.INFORMATION_MESSAGE);
		}
		
		initButton(); //初始化按钮
		
	}
	
/*	private static String getUrlParam(String url,String param) {
		
		String regexMatch = "\\{(.+?)\\}";
		Pattern p = Pattern.compile(regexMatch);
		Matcher m = p.matcher(url);
//		System.out.println(m.);
		if(m.find()){
			System.out.println( m.group(0));
		}
		
		Pattern p = Pattern.compile("(?="+param+")(.+?)(?<=&)"); 
		Matcher m = p.matcher(url);
		if(m.find()){
			 return m.group(0);
		}
		
		p = Pattern.compile("(?="+param+")(.*)");
		m = p.matcher(url);
		
		if(m.find()){
			 return m.group(0);
		}
		logger.error("提取链接参数出错："+url+"|"+param);
		return "";
	}
*/
	public int getHandlerFileThreads() {
		return handlerFileThreads;
	}

	public void setHandlerFileThreads(int handlerFileThreads) {
		this.handlerFileThreads = handlerFileThreads;
	}

	public Widgets getWidgets() {
		return widgets;
	}

	public void setWidgets(Widgets widgets) {
		this.widgets = widgets;
	}

	public long getDownloadStartTime() {
		return downloadStartTime;
	}

	public void setDownloadStartTime(long downloadStartTime) {
		this.downloadStartTime = downloadStartTime;
	}

	public long getDownloadEndTime() {
		return downloadEndTime;
	}

	public void setDownloadEndTime(long downloadEndTime) {
		this.downloadEndTime = downloadEndTime;
	}

	public long getFilehandlerStartTime() {
		return filehandlerStartTime;
	}

	public void setFilehandlerStartTime(long filehandlerStartTime) {
		this.filehandlerStartTime = filehandlerStartTime;
	}

	public long getFilehandlerEndTime() {
		return filehandlerEndTime;
	}

	public void setFilehandlerEndTime(long filehandlerEndTime) {
		this.filehandlerEndTime = filehandlerEndTime;
	}

	public boolean isFinish() {
		if(this.mainthreads==0 && this.threads==0){
			return true;
		}
		return false;
	}

	public int getMainthreads() {
		return mainthreads;
	}

	public void setMainthreads(int mainthreads) {
		this.mainthreads = mainthreads;
	}
	
	public void initDownloadThreads(Integer threads){	
		this.downloadThreads =threads;
	}

	public boolean isInitUrl() {
		return initUrl;
	}
	
	public boolean getInitUrl() {
		return initUrl;
	}


	public void setInitUrl(boolean initUrl) {
		this.initUrl = initUrl;
	}

	public boolean isExistsDetailThread() {
		return isExistsDetailThread;
	}

	public void setExistsDetailThread(boolean isExistsDetailThread) {
		this.isExistsDetailThread = isExistsDetailThread;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public OpenUnit getMainOpenUnit() {
		return mainOpenUnit;
	}

	public void setMainOpenUnit(OpenUnit mainOpenUnit) {
		this.mainOpenUnit = mainOpenUnit;
	}
	
	/*
	 * 初始化用户特殊的输入框
	 */
	public  void initInput() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
