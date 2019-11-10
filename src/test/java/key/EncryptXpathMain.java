package key;

import java.io.File;
import java.util.Arrays;

import com.crawler.util.FolderUtil;
import com.crawler.util.RSAUtils;

public class EncryptXpathMain {

	private static String desc = "encrypt";
	private static String src = "config";
	/**
	 * 加密config的所有到encrypt
	 * @param args
	 * @throws Exception
	 * 
	 */
	public static void main(String[] args) throws Exception {
		
		File file = new File(src);
		File[] xpathFile = FolderUtil.getFolderFiles(new File(src)) ;
		for (File f : xpathFile) {
			String source = FolderUtil.readFileContent(f);
			
	        System.out.println("原文字：\r\n" + source);
	        byte[] data = source.getBytes();
	        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, RSAUtils.privateKey);
	        File descFile = new File(desc+File.separator+f.getName());  //目标文件
	        FolderUtil.createFile(descFile, Arrays.toString(encodedData).replace("[", "").replace("]", ""));  //字节的方式存
		}
	}

}
