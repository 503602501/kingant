package com.crawler.handler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.crawler.entity.Environment;
import com.crawler.entity.ImageProperty;
import com.crawler.util.ImageUtil;
import com.crawler.util.StringUtils;

public class FileHandlerThread implements Runnable{

	private Environment env;
	private ImageProperty image ; 
	private ConcurrentLinkedQueue <File> queue ;
	private static final Logger logger =  Logger.getLogger(FileHandlerThread.class);
	public FileHandlerThread(ConcurrentLinkedQueue<File> queue,ImageProperty image,Environment env) {
		this.queue = queue ;
		this.image = image;
		this.env = env;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void run() {
		List<String> suffix =  Arrays.asList( image.getSuffix().split("\\.")) ;
//		BufferedImage sourceImg =null;
//		FileInputStream fis=null; 
		File pathname = null;
		while (!env.isStop()) {
			if(queue.isEmpty()){
				break;
			}
			
			pathname=queue.poll();
			if(pathname==null){  //弹出的数据为空
				continue;
			} 
			
			ImageProperty sourceImage =  ImageUtil.getImageInfo(pathname);
			
/*			//特殊处理失败的图片
			try {
				fis=new FileInputStream(pathname);
				sourceImg =ImageIO.read(fis);
				fis.close();
				if(sourceImg==null){
					boolean flag = pathname.delete();
					env.showLogArea("删除无法打开的图片:"+pathname.getPath()+","+flag);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}finally{
				try {
					if(fis!=null){
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
    	
        	 //过滤后缀
        	 String prefix =  pathname.getName().substring(pathname.getName().lastIndexOf(".")+1);
             if (!StringUtils.isEmpty(image.getSuffix()) && !suffix.contains(prefix.toLowerCase())){
            	  pathname.delete();
            	  env.showLogArea("删除不合格图片:"+pathname.getPath());
            	  continue;
             }
             
             long size = sourceImage.getSize(); //KB
             //过滤小于最小大小的文件
             if (!StringUtils.isEmpty(image.getMinSize() ) ){
            	 if(size < image.getMinSize()){
            		 pathname.delete();
            		 env.showLogArea("删除不合格图片:"+pathname.getPath());
            		 continue;
            	 }
             }
             
             //过滤最大最大的大小的文件
             if (!StringUtils.isEmpty(image.getMaxSize() ) ){
            	 if(image.getMaxSize()<size){
            		 pathname.delete();
            		 env.showLogArea("删除不合格图片:"+pathname.getPath());
            		 continue;
            	 }
             }
             
             
             //像素处理
             if(!StringUtils.isEmpty(image.getMinWidth()) || !StringUtils.isEmpty(image.getMinHeight()) || !StringUtils.isEmpty(image.getRatioFromField()) || !StringUtils.isEmpty(image.getRatioToField())){
            	 Float ratio =  (float) (1.0*sourceImage.getWidth()/sourceImage.getHeight());  //  宽/高比例
//            	 try {
                	/*  fis=new FileInputStream(pathname);
                	  try {
                		  sourceImg =ImageIO.read(fis);
                		  fis.close();
                	  } catch (Exception e) {
                		  logger.info("错误读取:"+pathname);
                	  }*/
            		  
            		/*  if(sourceImg==null){
            			  pathname.delete();
            			  env.showLogArea("删除不合格图片:"+pathname.getPath());
             			  continue;
            		  }*/
            		 if( !StringUtils.isEmpty(image.getMinWidth()) &&  sourceImage.getWidth() < image.getMinWidth()){
            			 pathname.delete();
            			 env.showLogArea("删除不合格图片:"+pathname.getPath());
            			 continue;
            		 }
            		 
            		 if(!StringUtils.isEmpty(image.getMinHeight()) &&  sourceImage.getHeight() < image.getMinHeight()){
            			 pathname.delete();
            			 env.showLogArea("删除不合格图片:"+pathname.getPath());
            			 continue;
            		 }
            		 
            		 //宽高最小比例
            		 if(!StringUtils.isEmpty(image.getRatioFromField()) && ratio.compareTo(image.getRatioFromField())==-1){
            			 pathname.delete();
            			 env.showLogArea("删除高宽最小比例:"+pathname.getPath());
            			 continue;
            		 }
            		 
            		 //宽高最大比例
            		 if(!StringUtils.isEmpty(image.getRatioToField()) && ratio.compareTo(image.getRatioToField())==1){
            			 pathname.delete();
            			 env.showLogArea("删除高宽最大比例:"+pathname.getPath());
            			 continue;
            		 }
            		 
            	/* } catch (IOException e) {
            		 logger.error("图片处理异常:"+e.getMessage());
            		 e.printStackTrace();
            	 }*/
            	 
             }
		}
		env.reduceFileThreads();
	}
	
	public static void main(String[] args) throws IOException {
		String pathname = "C:\\Users\\rocky\\Desktop\\download\\000071128495512-1_w_1.jpg";
		FileInputStream fis=new FileInputStream(pathname);
//		 fis=new FileInputStream(pathname);
		BufferedImage  sourceImg =ImageIO.read(fis);
		System.out.println(sourceImg);
	}
}