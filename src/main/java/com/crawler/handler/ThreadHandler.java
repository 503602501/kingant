package com.crawler.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.crawler.entity.StoreData;
import com.crawler.handler.row.IRowHander;
import com.crawler.handler.row.impl.DefaultFolderHandler;
import com.crawler.util.ConfigUtil;
import com.crawler.util.ExcelUtil;
import com.crawler.util.ImageUtil;

/*
 * 多线程处理图片下载
 */
public class ThreadHandler {

	private IRowHander iRowHander;
	private List<List> list = null;
	private List<String> header = null;
	private List<List> errorList = null;
	private List<Future<List<String>>> doubleFuture = new ArrayList<>();;
	private List<Future<List<String>>> resultFuture = null;
	private static final Logger logger =  Logger.getLogger(ThreadHandler.class);
	private static final Integer index = ConfigUtil.getIntegerConfigByKey("read.download.index");  //链接的所在位置
	public ThreadHandler(StoreData storeData) {
		this.list = storeData.getList();
		this.header = storeData.getHeader();
		this.errorList = new ArrayList<List>();
		this.resultFuture=new ArrayList<Future<List<String>>>();  
		this.iRowHander = new DefaultFolderHandler();
	}
	
	public ThreadHandler(StoreData storeData,IRowHander iRowHander) {
		this.list = storeData.getList();
		this.header = storeData.getHeader();
		this.errorList = new ArrayList<List>();
		this.resultFuture=new ArrayList<Future<List<String>>>();  
		this.iRowHander = iRowHander;
	}
	
	
	/*
	 * 多线程批量下载图片
	 */
	public void runDownLoad() throws Exception, ExecutionException{
		
//		String path =System.getProperty("user.dir")+File.separator+"handler"+File.separator+"image"+File.separator+System.currentTimeMillis();
		String path =System.getProperty("user.dir")+File.separator+"handler"+File.separator+"image"+File.separator+"1509725133231";
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
		/******************开始处理多线程处理链接**********************/
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool( ConfigUtil.getIntegerConfigByKey("download.thread.total") );
		Future<List<String>> future =null;
		for (List<String> item : list) {
			final List<String>  row = item ; //一行的数据
			future=fixedThreadPool.submit(new TaskResult(row,path));
			resultFuture.add(future);
		}
		
		//遍历任务的结果 ,失败的链接重新启动
		long start = System.currentTimeMillis();
		logger.info("******************开始结果处理**********************");
        for (Future<List<String>> fs : resultFuture){
            while(!fs.isDone()) ; //Future返回如果没有完成，则一直循环等待，直到Future返回完成  
            
            if(fs.get()!=null){
            	//异常链接重新处理
            	future=fixedThreadPool.submit(new TaskResult(fs.get(),path));
            	doubleFuture.add(future);
            }
        }
        
      //遍历任务的结果   
        for (Future<List<String>> fs : doubleFuture){
            while(!fs.isDone());//Future返回如果没有完成，则一直循环等待，直到Future返回完成  
            if(fs.get()!=null){ //采集不了的连接，就别采集啦，写到excel给客户好了
            	 errorList.add(fs.get());
            }
        }
        logger.info("结果处理耗时：" +(System.currentTimeMillis()-start)/1000);
    
		fixedThreadPool.shutdown();
		
		//异常的连接写到excel
		ExcelUtil.createErrorExcel(errorList, header);
	}
	
	public class TaskResult implements Callable<List<String>>{

		private List<String> row = null;
		private String path = null;
		public TaskResult(List<String> row,String path) {
			this.row=row;
			this.path=path;
		}
		
		@Override
		public List<String> call() throws Exception {
			String url = row.get(index)  ;
			String imgName =url.substring(url.lastIndexOf("/")+1 );
			String fullPath = path+File.separator+imgName ; 
			
			fullPath = null ; //iRowHander.handerRow(row, fullPath);
			Error error = ImageUtil.upload(row.get(index), fullPath);
			if(error!=null){
				return row;
			}else{
				logger.info("已下载:"+row.get(index));
				return null;
			}
			
		}
		
	}

}
