package com.crawler.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.Environment;
import com.crawler.util.ConfigUtil;

/*
 * 进程监控者
 */
public class MonitorThread implements Runnable{

	private List<DriverEntity> driverEntities ;
	private Environment env;
	private static final Logger logger =  Logger.getLogger(MonitorThread.class);
	private static long DRIVER_DEAD_TIME = ConfigUtil.getIntegerConfigByKey("driver.dead.time");
	  
	public MonitorThread(List<DriverEntity> driverEntities,Environment env) {
		this.driverEntities = driverEntities;
		this.env = env;
	}
	/**
	* 所有的正在运行的进程
	* 
	* @return
	*/
	public  HashMap getAllProcesses() {
		
		HashMap<String,String> map = new HashMap<>();
		BufferedReader input= null;
		try {
			Process p = Runtime.getRuntime().exec("TASKLIST /NH /FO CSV "); //TASKLIST.EXE /FO CSV /NH
			input = new BufferedReader(new InputStreamReader( p.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				if ( line.indexOf("phantomjs.exe")!=-1 || line.indexOf("chromedriver.exe")!=-1){
					line=line.replaceAll("\",\"","-").replace(",", "").replace("K\"", "").trim();
					map.put(line.split("-")[1], line.split("-")[4]);
				}
			}
		} catch (Exception err) {
			logger.error("进程处理异常:"+err.getMessage());
			err.printStackTrace();
		}finally{
			try {
				input.close();
			} catch (IOException e) {
				logger.error(" 关闭流异常"+e.getMessage());
			}
		}
		return map;
	}
 
	
	private  void killProcesses() {
		
		HashMap map = getAllProcesses();
		try {
			for (int i = 0; i < 5; i++) {
				if(map.size()==0){
					break;
				}
				 Thread.sleep(3000);
				 Map next = getAllProcesses();
				 Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();  
				 while (entries.hasNext()) {  
				   
				   Map.Entry<String, String> entry = entries.next();  
				   if(! next.get(entry.getKey()).equals(entry.getValue()) ){  //把十秒内，内存没有发生变化的移除掉
					   entries.remove();
				   }
				 }
			}
			
			Iterator<Map.Entry<String, String>> dies = map.entrySet().iterator();  
			while (dies.hasNext()) {  
			   Map.Entry<String, String> entry = dies.next();  
			   logger.error("杀掉 pid:"+entry.getKey());
			   Runtime.getRuntime().exec("CMD.EXE /C TASKKILL /T /F  /PID "+entry.getKey());
			}
			 
		} catch (Exception e) {
			logger.error("杀进程异常"+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		if(env.isStop()){
			return ;
		}
		
		logger.info("监控进程运行");
		Iterator<DriverEntity> it = driverEntities.iterator();
		DriverEntity entity;
		while(it.hasNext()){
			entity = it.next();
			long  second =   (System.currentTimeMillis()-entity.getTime())/1000;  //秒
			if( entity.getTime()!=null && second >DRIVER_DEAD_TIME){
				logger.error("驱动异常僵死:"+entity);
				driverEntities.remove(entity);
//				if(entity.isChrome()){
//					entity.removeDirverEntity();
//				}else{
					killProcesses();
//				}
				
				
				break;
			}
		}
	}
	
}
