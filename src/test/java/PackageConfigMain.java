

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.crawler.util.ConfigUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.RSAUtils;

public class PackageConfigMain {

	private static String desc = "encrypt";
	private static String src = "config";
	private static String INSTALL_CONFIG_PATH = "C:\\Users\\rocky\\Desktop\\install\\encrypt"; //打包后配置文件输出的路径
	
	/** 
	 * 打包配置信息
	 * 加密config的所有到encrypt
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		File file = new File(src);
		File[] xpathFile = file.listFiles();
		for (File f : xpathFile) {
			String source = FolderUtil.readFileContent(f);
//	        System.out.println("原文字：\r\n" + source);
	        byte[] data = source.getBytes();
	        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, RSAUtils.privateKey);  //二进制字节通过密匙加密
	        File descFile = new File(desc+File.separator+f.getName());  //目标文件
	        FolderUtil.createFile(descFile, Arrays.toString(encodedData).replace("[", "").replace("]", ""));  //字节的方式存
		}
		
		//读取config.xml配置的主配置和子配置,输出打包路径
		String mainXpath  = ConfigUtil.getStringConfigByKey("main.xpath.config");
		String detailXpath= ConfigUtil.getStringConfigByKey("detail.xpath.config");
		
		FileUtils.copyFile(new File(desc+File.separator+"config.properties"),new File(INSTALL_CONFIG_PATH+File.separator+"config.properties"));
		FileUtils.copyFile(new File(desc+File.separator+"log4j.properties"),new File(INSTALL_CONFIG_PATH+File.separator+"log4j.properties"));
		
		FileUtils.copyFile(new File(desc+File.separator+mainXpath),new File(INSTALL_CONFIG_PATH+File.separator+mainXpath));
		if(detailXpath!=null){
			FileUtils.copyFile(new File(desc+File.separator+detailXpath),new File(INSTALL_CONFIG_PATH+File.separator+detailXpath));
		}
		
		System.out.println("-----------------************配置打包完毕************-----------------");
		
	}

}
