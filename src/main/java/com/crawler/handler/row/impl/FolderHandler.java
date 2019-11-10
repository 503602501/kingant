package com.crawler.handler.row.impl;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crawler.entity.Environment;
import com.crawler.entity.Product;
import com.crawler.handler.row.IRowHander;
import com.crawler.util.StringUtils;

/*
 * 具体的行处理类
 */
public class FolderHandler implements IRowHander {

	/*
	 * row:每行的数据
	 * path：下载的图片的具体路径
	 */
//https://image.tuandai.com/project_images/201711/20171102192236_7255_S.jpg
/*	@Override
	public String handerRow(List<String> row, String path) {
		
		String folder =row.get(0).replaceAll("\\|", ""); //
		String folderPath = path.substring(0,path.lastIndexOf("\\"))+File.separator+folder;
		File file = new File(folderPath);
		if(!file.exists()){
			file.mkdirs();
		}
		String fullpath = null ;
		if(StringUtils.isEmpty(row.get(2))){  //图片名为空??
			fullpath = folderPath+File.separator+ getRandom()+row.get(1).substring( row.get(1).lastIndexOf("."));
		}else{
			String format = row.get(2).substring(row.get(2).lastIndexOf("."));
			fullpath=folderPath+File.separator+row.get(2).replaceFirst(format, getRandom()+format); 
		}
		return fullpath ; 
	}*/
	
	public String getFolder(String content) {
		Pattern pattern = Pattern.compile("【[a-zA-Z0-9].*】");
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb= new StringBuffer();
		while (matcher.find()) {  
	      int i = 0;  
	      if(sb.length()!=0){
	    	 sb.append("|");	    	  
	      }
	      sb.append(matcher.group(i));
	       i++; 
	       break;
	    }
		String result = sb.toString().replaceAll("【", "").replaceAll("】", "");
		return result ;
	}
	public static void main(String[] args) {
		FolderHandler folderHandler  = new FolderHandler();
		System.out.println(folderHandler.getFolder("【常州】房贷【TDW-FSD20171023615】"));
	}
	/*
	 * 随机数
	 */
	public String getRandom() {
		   int max=50;
	       int min=1;
	       Random random = new Random();
	      int s = random.nextInt(max)%(max-min+1) + min;
	      return "_"+s;
	}

	@Override
	public String handerRow(Product product,Environment env) {
		String path  = env.getDownloadPath();
		// TODO Auto-generated method stub
		return null;
	}

	 
}
