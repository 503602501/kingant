package com.crawler.handler.row.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.crawler.entity.Environment;
import com.crawler.entity.Product;
import com.crawler.handler.row.IRowHander;
import com.crawler.util.FolderUtil;
import com.crawler.util.ImageUtil;
import com.crawler.util.StringUtils;

/*
 * 具体的行处理类
 */
public class DefaultHandler implements IRowHander {

	private volatile  List<Integer> indexs ;
	private Lock lock ;

	public DefaultHandler() {
		lock= new ReentrantLock();
	}
	
	@Override
	public String handerRow(Product product,Environment env) {
		String path  = env.getDownloadPath();
		//获取标题列的名称作为文件夹名
		if(indexs==null){
			lock.lock(); //加锁
				if(indexs==null){
					List folders= new ArrayList<>();
					for (int i = 0; i < product.getHeader().size(); i++) {
						if(product.getHeader().get(i).indexOf("文件夹名")!=-1){
							folders.add(i);
						}
					}
					indexs.addAll(folders);  //解决高并发 ，出现的一些图片没有创建文件夹的情况!!!!!
				}
			lock.unlock();//解锁
		}
		
		String folder = "";
		if(indexs.size()!=0){
			for (Integer index: indexs) {
				folder=folder+File.separator+FolderUtil.filterFolderName(product.getList().get(index));
			}
		}else{
			System.out.println(indexs.size());
			folder = File.separator+"download";
		}
			
		path=path+File.separator+folder;
		
		File file =new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
	    String url = product.getUrl();
		String imgName = FolderUtil.filterFolderName( url.substring(url.lastIndexOf("/")+1 ) ) ;  //图片名
		/*imgName = ImageUtil.validatePrefix(imgName);
		//图片名大于100,特殊处理以系统时间作为图片名
		if(imgName.length()>50){
			imgName=System.currentTimeMillis()+imgName.substring(imgName.lastIndexOf("."));
		}*/
		//没有后缀,默认使用png
		imgName=ImageUtil.validatePrefix(imgName);
		 
		
		String fullPath =path+File.separator+imgName  ;  
		
		return fullPath;
	}
	public static void main(String[] args) {
		String s = FolderUtil.filterFolderName("2988287685_1602192195.jpg?__r__=1461825800672" );
				System.out.println(s);
		
	}

}
