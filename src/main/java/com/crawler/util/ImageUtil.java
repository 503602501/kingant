package com.crawler.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.crawler.entity.ImageProperty;
import com.crawler.entity.MyQRCodeImage;

public class ImageUtil {

	
	private static int socketTimeout = ConfigUtil.getIntegerConfigByKey("download.socket.timeout") *1000;  //读超时时间（等待数据时间）
	private static int connectTimeout = ConfigUtil.getIntegerConfigByKey("download.connect.timeout") * 1000;
	private static int connectionRequestTimeout = 10000;  //从连接池中获取连接的等待时间
	private static int maxConnTotal = 300;   //最大不要超过1000  
	private static int maxConnPerRoute = 200;//路由的实际的单个连接池大小，如tps定为50，那就配置50  
	private static final Logger logger =  Logger.getLogger(ImageUtil.class);
	private static 	CloseableHttpClient httpclient ;
	private static SSLContext sslContext ;
	private static HashMap<String,String> refererMap = new HashMap<>();  //防盗链处理
	
	static{
		
		refererMap.put("abbs.cn", "http://abbs.cn");
		refererMap.put("photo.yupoo.com", "https://x.yupoo.com/photos");
		refererMap.put("www.lingganjia.com", "http://www.lingganjia.com/view/114096.htm");
		
		/*******第一种https验证****** 
		 try {
			 sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
			      @Override
			      public boolean isTrusted(X509Certificate[] x509Certificates, String s) {
			         return true;
			      }
			   }).build();
			 sslContext = SSLContext.getInstance("TLS");
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		*/
		
		/*******第二种https验证*******/
		//异常： javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
		sslContext = createIgnoreVerifySSL();
		
		RequestConfig config = RequestConfig.custom()  
		         .setSocketTimeout(socketTimeout)  
		         .setConnectTimeout(connectTimeout)  
		         .setConnectionRequestTimeout(connectionRequestTimeout).build();  
		httpclient = HttpClients.custom().setDefaultRequestConfig(config)  
		         .setMaxConnTotal(maxConnTotal)  
		         .setMaxConnPerRoute(maxConnPerRoute)
            .setSslcontext(sslContext)
            .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        
//		httpclient.getConnectionManager().shutdown(); 未做关闭不知道是否有问题
	}
	
	public static SSLContext createIgnoreVerifySSL() {  
	    SSLContext sslContext = null;// 创建套接字对象  
	    try {  
	        sslContext = SSLContext.getInstance("TLSv1.2");//指定TLS版本  
	    } catch (NoSuchAlgorithmException e) {  
	        logger.error("创建套接字失败！", e);  
	    }  
	    // 实现X509TrustManager接口，用于绕过验证  
	    X509TrustManager trustManager = new X509TrustManager() {  
	        @Override  
	        public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) {  
	        }  
	  
	        @Override  
	        public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) {  
	        }  
	  
	        @Override  
	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	            return null;  
	        }  
	    };  
	    try {  
	        sslContext.init(null, new TrustManager[] { trustManager }, null);//初始化sslContext对象  
	    } catch (KeyManagementException e) {  
	    	 logger.error("初始化套接字失败！", e);  
	    }  
	    return sslContext;  
	}  
	 /* 
	    * 
	    */
	public static Error  uploadImage(String httpPath,String uploadPath) throws Exception{
		 
//		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
		InputStream is =null;
		FileOutputStream outputStream =null;
		CloseableHttpResponse respond=null;
		boolean isDelete =false;
		try {
			// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
			HttpGet httpGet = new HttpGet(httpPath);
			URL url= new URL(httpPath);
//			httpGet.setHeader("origin", url.getProtocol());
			
			if(refererMap.containsKey(url.getHost())){
				httpGet.setHeader("referer", refererMap.get(url.getHost()));
			}
			
			// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
			httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20170101 Firefox/56.0");
			// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
			respond = httpclient.execute(httpGet);
			respond.setHeader("Connection", "close");
			if( HttpStatus.SC_OK!=respond.getStatusLine().getStatusCode() ){
				httpGet.abort();
				return new Error("异常返回码:"+respond.getStatusLine().getStatusCode()+","+httpPath);
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				is = entity.getContent();
//				FileUtils.copyInputStreamToFile(is, new File(uploadPath));
				File file = new File(uploadPath);
				outputStream = new FileOutputStream(file);
		        byte b[] = new byte[5*1024];
		        int j = 0;
		  
		        while( (j = is.read(b)) != -1 )
		        {
		            outputStream.write(b,0,j);
		        }
		        outputStream.flush();
		        
			}else{
				return new Error("返回空请求内容:"+httpPath);
			}
		}catch (SocketTimeoutException se) {//socket读取超时，删除已经创建的文件吧
			se.printStackTrace();
			logger.error("下载超时:"+httpPath);
			closeOutputStream(outputStream, uploadPath);
			return new Error("SocketTimeoutException:"+se.getMessage());
		}catch (SocketException e) {
			e.printStackTrace();
			closeOutputStream(outputStream, uploadPath);
			return new Error("SocketException:"+e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			closeOutputStream(outputStream, uploadPath);
			System.out.println("下载异常信息:"+e.getMessage());
			logger.error("下载异常："+httpPath+","+e.getMessage(),e);
			return new Error("下载异常："+httpPath+","+e.getMessage());
		}finally{
			System.out.println(Thread.currentThread().getName()+"完毕");
			try {
				if(is!=null){
					is.close();
				}
			} catch (IOException io) {
				logger.error("关闭异常："+httpPath+","+io.getMessage(),io);
			}
			
			try {
				if(respond!=null){
					respond.close();
				}
			} catch (Exception e) {
				logger.error("关闭异常："+httpPath+","+e.getMessage(),e);
			}
			
			try {
				 if(outputStream!=null){
					 outputStream.close();
				 }
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("关闭异常："+httpPath+","+e.getMessage(),e);
			} 
		}
		return null;
	}
		
	public static void  closeOutputStream(FileOutputStream outputStream ,String uploadPath) {
		try {
			 if(outputStream!=null){
				 outputStream.close();
			 }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("关闭异常："+uploadPath+","+e.getMessage(),e);
		}
		
		File file = new File(uploadPath);
		if(file.exists()){
			System.out.println("存在需要删除:"+file.delete()+"|"+uploadPath);
		}else{
			System.out.println("居然不存在:"+file.delete()+"|"+uploadPath);
		}
		
		
		
	}

	public static Error upload(String httpPath, String uploadPath)
			throws Exception {
		// 判断一下图片是否已经下载了，如果存在就不下载
		// D:\workspace\crawler\handler\image\1509725133231\TDW-YDCD-YCD170811054001\人车合影_副..._44.jpg
		// String content =
		// uploadPath.substring(0,uploadPath.lastIndexOf("_"))+".jpg";
		//
		// File file = new File(content);
		// if(file.exists()){
		// return null;
		// }else{
		// logger.info("已下载过");
		// }

		byte[] btImg = getImageFromNetByUrl(httpPath);
		if (null != btImg && btImg.length > 0) {
			return writeImageToDisk(btImg, uploadPath);
		} else {
			return new Error("图片为空异常");
		}
	}

	public static Error writeImageToDisk(byte[] img, String uploadPath) {

		FileOutputStream fops = null;
		try {
			File file = new File(uploadPath);
			fops = new FileOutputStream(file);
			fops.write(img);
			fops.flush();
		} catch (Exception e) {
			logger.error("下载异常" + e.getMessage());
			return new Error("下载异常");
		} finally {
			try {
				if (fops != null) {
					fops.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static byte[] getImageFromNetByUrl(String strUrl) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(strUrl);
			if ("https".equalsIgnoreCase(url.getProtocol())) {
				SslUtils.ignoreSsl();
			}

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
			conn.setRequestMethod("GET");
			if ("http".equals(url.getProtocol())) {
				conn.setRequestProperty("Referer", url.getProtocol() + "://"
						+ url.getHost());
			}
			conn.setConnectTimeout(ConfigUtil
					.getIntegerConfigByKey("download.connect.timeout") * 1000);
			conn.setReadTimeout(ConfigUtil
					.getIntegerConfigByKey("download.connect.timeout") * 1000);
			InputStream inStream = conn.getInputStream();
			byte[] btImg = readInputStream(inStream);
			return btImg;
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("下载异常:" + strUrl);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.flush();
		inStream.close();
		return outStream.toByteArray();
	}

	public static String getPostfix(String fileName) {
		String imgeArray[] = { ".bmp", ".dib", ".gif", ".jfif", ".jpe",
				".jpeg", ".jpg", ".png", ".tif", ".tiff", ".ico" };
		for (String postfix : imgeArray) {
			if (fileName.toLowerCase().endsWith(postfix)) {
				return postfix;
			}
		}

		return ".png";
	}

	/*
	 * 验证图片后缀
	 */
	public static String validatePrefix(String fileName) {
		String imgeArray[] = { ".bmp", ".dib", ".gif", ".jfif", ".jpe",
				".jpeg", ".jpg", ".png", ".tif", ".tiff", ".ico" };
		for (String postfix : imgeArray) {
			if (fileName.toLowerCase().endsWith(postfix)) {
				return fileName;
			}
		}
		return fileName + ".jpg";
	}

	public static void method(File[] files, ConcurrentLinkedQueue<File> queue) {
		for (File f : files) {
			if (f.isDirectory()) {
				method(f.listFiles(), queue);
			} else {
				queue.offer(f);
			}
		}
	}
	
	//获取图片的一些基本属性
	public static ImageProperty getImageInfo(File file) {
		ImageProperty imageProperty = new ImageProperty();
		
		FileChannel fc = null;
        FileInputStream is = null;
        try {
        	 is =new FileInputStream(file);
        	 
        	/* QRCodeDecoder codeDecoder = new QRCodeDecoder();
        	 BufferedImage bufferedImage = 
             //通过解析二维码获得信息
             try {
             	String result = new String(codeDecoder.decode(new MyQRCodeImage(is)), "utf-8");
             	System.out.println(result);
     			
     		} catch (DecodingFailedException e) {
     			System.out.println("不是二维码");
     		}
        */
        
        	 fc= is.getChannel();
        	 imageProperty.setSize((int) (fc.size()/1024));  //KB单位 
        	 int c1 = is.read();
             int c2 = is.read();
             int c3 = is.read();

             String mimeType = null;
             int width =-1 ; 
             int height = -1;

             if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
                 is.skip(3);
                 width = readInt(is,2,false);
                 height = readInt(is,2,false);
                 mimeType = "image/gif";
             } else if (c1 == 0xFF && c2 == 0xD8) { // JPG
                 while (c3 == 255) {
                     int marker = is.read();
                     int len = readInt(is,2,true);
                     if (marker == 192 || marker == 193 || marker == 194) {
                         is.skip(1);
                         height = readInt(is,2,true);
                         width = readInt(is,2,true);
                         mimeType = "image/jpeg";
                         break;
                     }
                     is.skip(len - 2);
                     c3 = is.read();
                 }
             } else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
                 is.skip(15);
                 width = readInt(is,2,true);
                 is.skip(2);
                 height = readInt(is,2,true);
                 mimeType = "image/png";
             } else if (c1 == 66 && c2 == 77) { // BMP
                 is.skip(15);
                 width = readInt(is,2,false);
                 is.skip(2);
                 height = readInt(is,2,false);
                 mimeType = "image/bmp";
             } else {
                 int c4 = is.read();
                 if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
                         || (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) { //TIFF
                     boolean bigEndian = c1 == 'M';
                     int ifd = 0;
                     int entries;
                     ifd = readInt(is,4,bigEndian);
                     is.skip(ifd - 8);
                     entries = readInt(is,2,bigEndian);
                     for (int i = 1; i <= entries; i++) {
                         int tag = readInt(is,2,bigEndian);
                         int fieldType = readInt(is,2,bigEndian);
                         int valOffset;
                         if ((fieldType == 3 || fieldType == 8)) {
                             valOffset = readInt(is,2,bigEndian);
                             is.skip(2);
                         } else {
                             valOffset = readInt(is,4,bigEndian);
                         }
                         if (tag == 256) {
                             width = valOffset;
                         } else if (tag == 257) {
                             height = valOffset;
                         }
                         if (width != -1 && height != -1) {
                             mimeType = "image/tiff";
                             break;
                         }
                     }
                 }
             }
             
             imageProperty.setHeight(height);
             imageProperty.setWidth(width);
             
             if (mimeType == null) {
                 throw new IOException("Unsupported image type");
             }
             
        }catch (Exception e) {
        	logger.error("图片解析异常：",e);
        	e.printStackTrace();
		} finally {
            try {
            	if(is!=null){
            		is.close();
            	}
            	if(fc!=null){
            		fc.close();
            	}
			} catch (IOException e) {
				logger.error("图片关闭异常：",e);
				e.printStackTrace();
			}
        }
        
        return imageProperty;
	}
 
	   private static int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
	        int ret = 0;
	        int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
	        int cnt = bigEndian ? -8 : 8;
	        for(int i=0;i<noOfBytes;i++) {
	            ret |= is.read() << sv;
	            sv += cnt;
	        }
	        return ret;
	    }
	   
	/**
	 * @param args
     * @throws Exception 
	 */ 
	public static void main(String[] args) throws Exception {
//		  File f = new File("C:\\Users\\rocky\\Desktop\\A\\11.jpg");
//          // Getting image data from a InputStream
//          FileInputStream b = new FileInputStream(f);
//          SimpleImageInfo imageInfo = new SimpleImageInfo(b);
//          System.out.println(imageInfo);
//           Getting image data from a file
//          ImageProperty image =  ImageUtil.getImageInfo(f);
//          System.out.println(image.getHeight()+" "+image.getWidth()+" "+image.getSize());
        
		final String URL = "https://mrchstm.alipay.com/mart/order/resource.htm?id=9d8aed91b03846b892c2e7b888b5e20c";
////		for (int i = 0; i <2000; i++) {
////			ImageUtil  jj =new ImageUtil();
////			 jj.new Tths().start();
////		}
		ImageUtil.uploadImage(URL, "D:\\A\\"+System.currentTimeMillis()+".jpg");
//		System.out.println("下载完毕");
	} 
	
	/*public class Tths extends Thread{
		final String URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510654808536&di=957d74f32cf5983dfe1b5448a145038a&imgtype=0&src=http%3A%2F%2Fwww.people.com.cn%2Fmediafile%2Fpic%2F20160812%2F56%2F6695386280472753768.jpg";
		
		@Override
		public void run() {
			try {
				ImageUtil.uploadImage(URL, "C:\\Users\\rocky\\Desktop\\"+UUID.randomUUID()+System.currentTimeMillis()+(new Random().nextInt(100)) +".jpg");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

}
