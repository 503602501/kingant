package com.crawler.handler.row.impl;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crawler.entity.Environment;
import com.crawler.entity.Product;
import com.crawler.handler.row.IRowHander;
import com.crawler.util.FolderUtil;
import com.crawler.util.ImageUtil;

/*
 * 具体的行处理类
 */
public class AbbsHandler implements IRowHander {

	
	/*
	 * path:用户界面的选择的下载路径
	 * 客户个性化：文件夹存在：-已删除，这个文件夹的图片就不下载啦
	 * 
	 */
	@Override
	public String handerRow(Product product,Environment env) {
		String path = env.getDownloadPath();
		//获取标题列的名称作为文件夹名
		String folder = product.getList().get(product.getHeader().indexOf("标题"));
		folder = FolderUtil.filterFolderName(folder);
		if(folder.indexOf("[精华]")!=-1){
			path=path +File.separator+"精华"+File.separator+folder;
		}else{
			path=path +File.separator+"所有"+File.separator+folder;
		}
		
		//* 客户个性化：文件夹存在：-已删除，这个文件夹的图片就不下载啦
		File check =new File(path+"-已删除");
		if(check.exists()){
			return "";
		}
		
		File file =new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
	    String url = product.getUrl();
		String imgName =FolderUtil.filterFolderName(url.substring(url.lastIndexOf("/")+1 ));  //图片名
		imgName = ImageUtil.validatePrefix(imgName);
		//图片名大于100,特殊处理以系统时间作为图片名
		if(imgName.length()>50){
			imgName=System.currentTimeMillis()+imgName.substring(imgName.lastIndexOf("."));
		}
		
		imgName=folder+"_"+imgName;
//		imgName.lastIndexOf(".")
//		String suffix = imgName.substring(imgName.indexOf(".")) ;
//		imgName = imgName.substring(0, imgName.indexOf("."));
//		imgName = imgName+"_青岛宏景"+suffix;
		String fullPath =path+File.separator+imgName ;  
		
		return fullPath;
	}
	
	public static void main(String[] args) {
		String s = "180622_一一一十年模型一一一专业画程度高一一一沟通顺畅好配合_青岛宏景_1529664773.jpg";
		System.out.println();
		System.out.println(s.substring(s.indexOf(".")));
	}
}
