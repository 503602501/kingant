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
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

/*
 * 公用的图片下载
 */
public class DownloadPictureFolderHandler implements IRowHander {


	private volatile  List<Integer> indexs ;  //文件夹的索引位置
	private volatile  Integer nameIndex ;  //文件名的索引位置
	private Lock lock ;

	public DownloadPictureFolderHandler() {
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
					
					if(product.getHeader().get(i).indexOf("文件名")!=-1){
						nameIndex = i;
					}
				}
				indexs=folders;  //解决高并发 ，出现的一些图片没有创建文件夹的情况!!!!!
				
			}
			lock.unlock();//解锁
		}
		
		String folder = "";
		if(indexs.size()!=0){
			for (Integer index: indexs) {
				folder=folder+File.separator+FolderUtil.filterFolderName(product.getList().get(index));
			}
		}else{
			folder = File.separator+"download";
		}
			
		path=path+File.separator+folder;
		
		File file =new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
	    String url = product.getUrl();
	    
//	    String imgName =nameIndex!=null ? product.getList().get(nameIndex) :  FolderUtil.filterFolderName( url.substring( url.substring(0, url.lastIndexOf("/")).lastIndexOf("/")) );
	    String imgName =nameIndex!=null ? product.getList().get(nameIndex) :  FolderUtil.filterFolderName( url.substring(url.lastIndexOf("/")));
//	    if(StringUtils.isEmpty(imgName)){
//	    	imgName=NumberUtil.autoNumber()+System.currentTimeMillis();
//	    }
	    
//		String imgName = FolderUtil.filterFolderName(url.substring(url.lastIndexOf("/")+1 ) ) ;  //图片名
		String end = ImageUtil.validatePrefix(imgName);
		end  = end.substring(end.lastIndexOf("."));
		//图片名大于100,特殊处理以系统时间作为图片名
		 
 
		imgName =product.getList().get(0)+end;
		//没有后缀,默认使用png
		
		String fullPath =path+File.separator+imgName  ;  
		
		return fullPath;
	}
	public static void main(String[] args) {
		String s = "http://cdn1.ykso.co/mjboutique/product/crystal-butterfly-venetian-masquerade-mask-64223/images/39a4bfa/1448657451/generous.jpg";
//		String folder = s.substring( s.substring(0, s.lastIndexOf("/")).lastIndexOf("/"));
		String folder = s.substring(s.lastIndexOf("/"));
		System.out.println(FolderUtil.filterFolderName(folder));
		
		
	}

}
