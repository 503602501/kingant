package key;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import com.crawler.util.FolderUtil;
import com.crawler.util.RSAUtils;

public class RSAMain {
	
	    
	    public static void main(String[] args) throws Exception {
//	        test();       //公钥加密——私钥解密
	        testSign();  //私钥加密——公钥解密
	    }

	    static void test() throws Exception {
	        System.err.println("公钥加密——私钥解密");
	    	File xpathFile = new File("config/suobei-detail-xpath-config.yml");
			String source = FolderUtil.readFileContent(xpathFile);
			
//	        String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
	        System.out.println("\r加密前文字：\r\n" + source);
	        byte[] data = source.getBytes();
	        byte[] encodedData = RSAUtils.encryptByPublicKey(data, RSAUtils.publicKey);
	      
	        
	        System.out.println("加密后文字：\r\n" + new String(encodedData));
	        byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData, RSAUtils.privateKey);
	        String target = new String(decodedData);
	        System.out.println("解密后文字: \r\n" + target);
	    }

	    static void testSign() throws Exception {
	        System.err.println("私钥加密——公钥解密");
//	        String source = "这是一行测试RSA数字签名的无意义文字";
	        File xpathFile = new File("xpath/abbs-xpath-config.yml");
			String source = FolderUtil.readFileContent(xpathFile);
			
	        System.out.println("原文字：\r\n" + source);
	        byte[] data = source.getBytes();
	        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, RSAUtils.privateKey);
	        System.out.println("加密后：\r\n" + new String(encodedData));
	        File file = new File("encrypt/abbs-xpath-config.yml");
	        FolderUtil.createFile(file, Arrays.toString(encodedData).replace("[", "").replace("]", ""));  //字节的方式存
//	        
//	        File xpathFile = new File("encrypt/abbs-detail-xpath-config.yml");
	     	byte[] jiami = FolderUtil.readByteFileContent(file);
	     			
	        System.out.println(Arrays.toString(jiami));
	        System.out.println(Arrays.toString(encodedData));
	        byte[] decodedData = RSAUtils.decryptByPublicKey(jiami, RSAUtils.publicKey);
	         decodedData = RSAUtils.decryptByPublicKey(encodedData, RSAUtils.publicKey);
	        String target = new String(decodedData);
	        System.out.println("解密后: \r\n" + target);
	        System.err.println("私钥签名——公钥验证签名");
	        
	   /*     String sign = RSAUtils.sign(encodedData, privateKey);
	        System.err.println("签名:\r" + sign);
	        boolean status = RSAUtils.verify(encodedData, publicKey, sign);
	        System.err.println("验证结果:\r" + status);*/
	    }
}
