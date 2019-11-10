package com.crawler.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/*
 * 目录管理工具
 */
public class FolderUtil {
	
	private static final Logger logger = Logger.getLogger(FolderUtil.class);
	
	/*
	 *获取配置文件的路径; 客户配置的路径读取不到文件，就读取开发的配置目录
	 */
	public static InputStream getConfigInputStream(String fileName) throws Exception {
		
		InputStream input = null;
		File file = new File("config");
		if(file.exists()){
			input =new FileInputStream( new File("config"+File.separator+fileName));
		}else{
			input =  getInputStreamFromEncrypt(fileName);
		}
		
		return  input;
	}
	
	/*
	 *获取配置文件的路径; 客户配置的路径读取不到文件，就读取开发的配置目录
	 */
	public static InputStream getInputStreamFromEncrypt(String fileName) throws Exception {
		
		InputStream input = null;
		File file = new File("encrypt"+File.separator+fileName);
		byte[] bytes = readByteFileContent(file);
		bytes =RSAUtils.decryptByPublicKey(bytes, RSAUtils.publicKey);
		input = new ByteArrayInputStream(bytes);
		return  input;
	}
	
	/*
	 *获取配置文件的路径; 客户配置的路径读取不到文件，就读取开发的配置目录
	 */
	public static File getDriverFile() {
		String dirver = ConfigUtil.getStringConfigByKey("brower.driver");
        URL url = FolderUtil.class.getClass().getResource("/"+dirver);
        File file=null;
        file = new File(url.getPath());
		return file ;
	}
	
 
	
	public static String filterFolderName(String name){
		Pattern pattern = Pattern.compile("[\\\\/:\\*\\?\\\"<>\\|]");
		Matcher matcher = pattern.matcher(name);
		String folder =  matcher.replaceAll(""); // 将匹配到的非法字符以空替换
		if(folder.length()==0){ //过滤非法字符后，目录长度为0，需要给一个默认的名字
			String prefix = (Math.abs(name.hashCode())+"1234").substring(0, 4);  //文件名增加后缀，避免过滤掉了特殊字符后，文件变成无效的啦
			folder =  folder+"_"+prefix ; 
		}
		return folder;
	}
	
    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static void createFile(File file,String filecontent){
        try {
        	if(file.exists()){
        		file.delete();
        	}
            //如果文件不存在，则创建新的文件
        	file.createNewFile();
        	//创建文件成功后，写入内容到文件里
        	writeFileContent(file, filecontent);
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.error("文件创建异常："+e.getMessage());
        }
    }
    
    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static void writeFileContent(File file,String content) {
    	
    	  FileOutputStream fos = null;
    	  try {  
    		  fos= new FileOutputStream(file);  ;  
    		  fos.write(content.getBytes("UTF-8"));  
    	  } catch (Exception e) {  
    		  logger.error("写文件失败："+e.getMessage());
    	  }finally{
    		  if(fos!=null){
    			  try {
					fos.close();
				} catch (IOException e) {
					logger.error("关闭文件失败："+e.getMessage());
				}  
    		  }
    	  }
    }
    
    public static byte[] readByteFileContent(File file) {
    	String content =  readFileContent(file);
    	List<Byte> list = new ArrayList<>();
    	for (String bytestring : content.split(",")) {
    		list.add(new Byte(bytestring.trim()));
		}
    	byte[] bytes = new byte[list.size()];
    	for (int i = 0; i < list.size(); i++) {
    		bytes[i]=list.get(i);
		}
    	return bytes;
    }
    
    /*
     * 地柜删除所有的文件
     */
    public  static boolean deleteDir(File dir) {
        if (!dir.exists()) return false;
        if (dir.isDirectory()) {
            String[] childrens = dir.list();
            // 递归删除目录中的子目录下
            for (String child : childrens) {
//                boolean success = 
            	deleteDir(new File(dir, child));
//                if (!success) return false;
            }
        }
        // 目录此时为空，可以删除
        System.out.println(dir.getName()+"|" +dir.delete());
        return dir.delete();
    }
    
    
    /*
     * 获取目录下的文件，最底层是二级目录下的文件
     */
    public static File[] getFolderFiles( File folder) {
    	List<File> files = new ArrayList<>();
    	for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				for (File secondFile : f.listFiles()) {
					files.add(secondFile);
				}
			} else {
				files.add(f);
			}
		}
    	
    	File[] filearray = new File[files.size()];

    	files.toArray(filearray);
    	
		return filearray;
	}
    
    
	/**
	 * 读取文件数据
	 * @param file
	 * @return
	 */
	public static String readFileContent(File file) {
		String result = "";
		BufferedReader bufferedReader = null;
		InputStreamReader  isr  = null;
		try {
		/*	fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);*/
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8") ;
			bufferedReader = new BufferedReader(isr);
			
			String read = null;
			while ((read = bufferedReader.readLine()) != null) {
				result += read + "\r\n";
			}
		} catch (Exception e) {
			logger.error("文件读取失败:"+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (  isr!=null) {
					isr.close();
				}
			} catch (IOException e) {
				logger.error("关闭异常："+e.getMessage());
				e.printStackTrace();
			}
			
			try {
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
				logger.error("关闭异常："+e.getMessage());
				e.printStackTrace();
			}
		}
		return result;
	}
    
    public static void main(String[] args) {
     
    	String s= filterFolderName("LOEWE 罗意威 男包 女包 钱包 手拿包 相册批发工厂【品牌导航-客服QQ:103993888-广州一手货源厂家直销】");
    	System.out.println(s);
    	
    }

	public static void deleteFile(String pathname) {
		File file = new File(pathname);
		if(file.exists()){
			file.delete();
		}
	}
    
}
