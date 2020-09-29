package com.crawler.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.Environment;
import com.crawler.entity.Storage;


public class DriverUtil {

	private static final Logger logger =  Logger.getLogger(DriverUtil.class);
	private static final String USER_DATA_DIR = "user-data-dir";
	private static final String DISK_CACHE_DIR = "disk-cache-dir";
	private static final String EXPLICITLY_WAIT = "explicitly.wait";
	private static volatile long time =0;
	
	/**
	 * cookies提取
	 * @return
	 */
	public static String getCookies(WebDriver webDriver) {
		

		StringBuffer sb = new StringBuffer();
        for (Cookie ck : webDriver.manage().getCookies()) {
        	if(sb.length()!=0){
        		sb.append(";");
        	}
        	sb.append(ck.getName() + "=" + ck.getValue());
        }
		return sb.toString();
	}
	/*
	 * 导航到具体的链接
	 * 为啥加载页面失败，却没有抛出异常
	 */
	public static boolean navigateUrl(Storage storage ,String url,Integer timeout,String success) throws Exception{
		boolean flag = navigateUrl(storage, url, timeout);
		/*if(flag==false && StringUtils.isEmpty(success)){
			return false;
		}*/

		if(success.indexOf("||")!=-1){
			
			List<String> xpaths =Arrays.asList(success.split("\\|\\|"));
			
			WebDriver driver = storage.getDriverEntity().getWebDriver();
			
			for (String string : xpaths) {
				try {
					driver.findElement(By.xpath(string));
					return true ;
				} catch (Exception e) {
					logger.error("页面加载失败判断:"+e.getMessage());
				}
			}
			return false;
		}
		
		List<String> xpaths =Arrays.asList(success.split("\\|\\|"));
		
		try {
			WebDriver driver = storage.getDriverEntity().getWebDriver();
			for (String string : xpaths) {
				driver.findElement(By.xpath(string));
			}
		} catch (Exception e) {
			logger.error("页面加载失败判断:"+e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * 浏览器回退次数，不用重新加载页面：例子，京东手机端问问,出现进入问问的问题列表退出后，问问的问题列表重新刷新了
	 */
	public static void navigateBack(Integer num, WebDriver webDriver,Environment env) throws Exception {
		
		for (int i = 0; i < num; i++) {
			webDriver.navigate().back();
			DriverUtil.scrollScreen(webDriver, -1 , env);
		}
	}
	
	
	private static String currnet = null;
	/*
	 * 导航到具体的链接
	 * 为啥加载页面失败，却没有抛出异常
	 */
	public static boolean navigateUrl(Storage storage ,String url,Integer timeout) throws Exception{
		
		 
		WebDriver driver = storage.getDriverEntity().getWebDriver();
		driver.manage().timeouts().pageLoadTimeout(timeout , TimeUnit.SECONDS);
		try {
			for (int i = 0; i < 10; i++) {

/*//				//特殊处理
				if(driver.getWindowHandles().size()==1){
					((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
				}else{
					//特殊处理
					if(currnet==null){
						currnet = driver.getWindowHandle() ;
					}
					
					Set<String> handles = driver.getWindowHandles();
					for (String s : handles) { 
						if(!s.equals(currnet)){
							driver.switchTo().window(s);
							driver.close();
							break;
						}
					}
					
					((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
					
					 handles = driver.getWindowHandles();
					for (String s : handles) { 
						if(!s.equals(currnet)){
							driver.switchTo().window(s);
							break;
						}
					}
					
					
				}*/
				
//				((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
//				
//				Thread.sleep(1000);
//				ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
//				   driver.switchTo().window(tabs.get(tabs.size()-1)); //switches to new tab
//				   driver.get(url);
//				   System.out.println("------------------");
//				  System.out.println("------------------");
				   
//				((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
//				
//				driver.navigate().to(url);
//				Thread.sleep(1000);
//				driver.navigate().to(url);
//				((JavascriptExecutor)driver).executeScript("location.href=location.href");
				
				driver.navigate().to(url);
				
//				((JavascriptExecutor)driver).executeScript("location.reload()");
//				  System.out.println(driver.findElement(By.xpath("//div[@id='container']")).getAttribute("innerHTML") ); 
				
				if(driver.getPageSource().length()<50){
					logger.error("页面信息过少异常："+url);
					Thread.sleep(1000);
					continue;
				}else{
					break;
				}
			}
			
		}catch(TimeoutException  te){  //机器达到了http的连接数导致的异常，关闭浏览器重新初始化
			logger.error("页面加载超时："+url+"|"+te.getMessage());
			return false; 
		}
		
		return true; 
	}
	
	 public static boolean switchToWindow(String windowTitle,WebDriver dr){    
	        boolean flag = false;    
	        try {   
	        	
	        	dr.switchTo().window(windowTitle);
	            //将页面上所有的windowshandle放在入set集合当中  
	            String currentHandle = dr.getWindowHandle();    
	            Set<String> handles = dr.getWindowHandles();    
	            for (String s : handles) {    
	                if (s.equals(currentHandle))    
	                    continue;    
	                else {    
	                    dr.switchTo().window(s);  
	            //和当前的窗口进行比较如果相同就切换到windowhandle  
	            //判断title是否和handles当前的窗口相同  
	                    if (dr.getTitle().contains(windowTitle)) {    
	                        flag = true;    
	                        System.out.println("Switch to window: "    
	                                + windowTitle + " successfully!");    
	                        break;    
	                    } else    
	                        continue;    
	                }    
	            }    
	        } catch (Exception e) {    
	            System.out.printf("Window: " + windowTitle    
	                    + " cound not found!", e.fillInStackTrace());    
	            flag = false;    
	        }    
	        return flag;    
	    }  
	/*
	 * 初始化基本参数
	 */
	public static DriverEntity initDriver(Storage storage,String brower,Boolean isImage)   {
		long delay =  System.currentTimeMillis()-(time/1000);
		if(delay<=10 && time!=0){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.info("休眠中断异常");
			}
		}
		
		 DriverEntity entity = new DriverEntity(brower);
    	 WebDriver driver =null;
		 try {
			 if( storage.isChrome() ){ //chrome浏览器 
		       
				 ChromeOptions cps = new ChromeOptions();
		         cps.addArguments(USER_DATA_DIR+"=tmp/user"); //缓存地址 USER_DATA_DIR+"="+ConfigUtil.getConfigByKey(USER_DATA_DIR)
		         cps.addArguments(DISK_CACHE_DIR+"=tmp/cache"); //缓存地址 ConfigUtil.getConfigByKey(DISK_CACHE_DIR
		         cps.addArguments("--disable-infobars");
		         if (storage.isPhone()) {
		        	 cps.addArguments("--window-size=500,800");
		        	 cps.addArguments("--window-position=10,100"); //启动时指定浏览器屏幕坐标
		        	 cps.addArguments("--user-agent=iPhone 6"); //最大化窗口
		         }else{
		        	 cps.addArguments("--start-maximized"); //最大化窗口
		         }
		         cps.addArguments("--disable-translate"); 
		         cps.addArguments("--ignore-certificate-errors");
		         cps.addArguments("--disable-desktop-notifications");
		         //插件
		         if(storage.getExtensions().size()!=0){
			         for (String extension : storage.getExtensions()) {
			        	 cps.addExtensions(new File("chrome/Extensions/"+extension+".crx")); 
			         }	
		         }else{
		        	 cps.addArguments("--incognito"); 
		        	 cps.addArguments("--disable-extensions");
		         }
		         Map<String, Object> prefs = new HashMap<>();
		         prefs.put("profile.managed_default_content_settings.images",isLoadImage(isImage));
		         cps.setExperimentalOption("prefs", prefs);
		         prefs.put(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
			
				 ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File("chrome/chromedriver.exe")).usingAnyFreePort().build();
				 driver= new ChromeDriver(service, cps);
			/*	 String s = "ak_bmsc=06D5F51212DF6879AE476DA8A887000717352196AE49000060CDEC5E9B3B5835~pl403cf7pwoTpN2uG8Oq/2aLOGPMByALnCPs36MCqdUhu4EOYZg80bm5GR9SUcTyhFvZsRf34I2MYywyFVvhQgPs5shZM+XBVFKuqrI4GMTLz7VppAJo/SHHK57rJstQJ4izAPqz8zP7B8Gu3LdE9m6Dp79YbkxVJJKKqz3Uq+ZSMxD3+e/e9jzQ4hpgOcCR4oPceWtk/8a1q/IJK2O6H6jUNWpUQx3nkFjkUAqq8R6WM=; expires=Fri, 19 Jun 2020 16:36:16 GMT; max-age=7200; path=/; domain=.coupang.com; HttpOnly" ;
				 String[] array = s.split(";") ;
				 for (String string : array) {
					 driver.manage().addCookie(new Cookie(string.split("=")[0] , string.indexOf("=")==-1? "": string.split("=")[1])) ;
				}	*/ 
				 
				 service.start();
				 entity.setDriverService(service);
				 entity.setWebDriver(driver);
				 //  driver.manage().timeouts().implicitlyWait(Long.parseLong((String) ConfigUtil.getConfigByKey(EXPLICITLY_WAIT)), TimeUnit.SECONDS);
			}else{
				  
				 DesiredCapabilities dcaps = DesiredCapabilities.phantomjs();
				 if(storage.isPhone()){
					 dcaps = DesiredCapabilities.phantomjs().iphone();
				 } 
//				 String [] phantomJsArgs = {"--ignore-ssl-errors=true","--ssl-protocol=tlsv1","--webdriver-loglevel=NONE"};
//				 dcaps.setCapability(  PhantomJSDriverService.PHANTOMJS_CLI_ARGS,phantomJsArgs);
		         //ssl证书支持
		         dcaps.setCapability("acceptSslCerts", true);
		         //截屏支持
		         dcaps.setCapability("takesScreenshot", false);
		         //css搜索支持
		         dcaps.setCapability("cssSelectorsEnabled", true);
		         //禁止加载图片
		         dcaps.setCapability("phantomjs.page.settings.loadImages", isImage==null ? ConfigUtil.getBooleanConfigByKey("load.image") : isImage );
		         //js支持
		         dcaps.setJavascriptEnabled(true);
		         
	//	         dcaps.setCapability("diskCachePath","D:\\A");
		         //驱动支持
//		         dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"phantomjs/phantomjs.exe");
		    	 
			/*	String proxyIpAndPort= "91.214.62.168:53281";
		     	Proxy proxy = new Proxy();
		     	proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
				proxy.setProxyType(Proxy.ProxyType.MANUAL);	
				proxy.setAutodetect(false);
				dcaps.setCapability(CapabilityType.PROXY,proxy);*/
		        /* try {
					FolderUitl.getConfigInputStream("phantomjs.exe");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
		         
				 PhantomJSDriverService service = new PhantomJSDriverService.Builder().usingPhantomJSExecutable(new File("phantomjs/phantomjs.exe")).usingAnyFreePort().build();
			     driver = new PhantomJSDriver(service, dcaps);
			     service.start();
			     entity.setDriverService(service);
			     entity.setWebDriver(driver);
			}
			time = System.currentTimeMillis();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化驱动异常："+e.getMessage());
			return null;
		}
		 
		 driver.manage().timeouts().pageLoadTimeout(ConfigUtil.getIntegerConfigByKey("dirver.page.load.time") , TimeUnit.SECONDS);
		 driver.manage().timeouts().setScriptTimeout(ConfigUtil.getIntegerConfigByKey("dirver.script.timeout") ,TimeUnit.SECONDS);  
		 return entity ;
		 
	}
	
	/*
	 * 处理iframe框架
	 */
	public static void switchToIframes(WebDriver driver, String iframes) throws Exception {
		for (String iframe : iframes.split("->")) {
			WebElement element = driver.findElement(By.xpath(iframe)); 
			driver.switchTo().frame(element);
		}
	}
	
	/**
	 * 滑动屏幕
	 * @param driver
	 * @param type
	 * @throws Exception
	 */
	public static void scrollScreen(WebDriver driver, Integer type ,Environment env) throws Exception {
		
		//一下子到底部
		if(type==0){ 
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			return ; 
		}
		
		//type：-1，一屏一屏滚动到底部;type：2，滚动两屏
		int currentScreens = 0; //当前滚动屏幕的计数器
		int bottomCounter =0;  //到底部高度相同计数器
		Integer preHeight = 0;  //记录上次的高度
		while(true){
			if(env.isStop()){
				break;
			}
			
			currentScreens+=1;
			Integer height = Integer.parseInt(""+((JavascriptExecutor)driver).executeScript("var height= document.body.scrollHeight; return height"));
			Integer screensTotal=height/800;  //按照800递增滚动异常，总共需要滚动的次数
			if(type!=-1 && currentScreens >type){  //滚动10次
				break;
			}
			
			if(type==-1 && (currentScreens-screensTotal)>5 && bottomCounter>3){ //滚动的屏数要比总的多3，为了防止加载不到底部
				break;
			}
			
			//一屏一屏得滚动
			if((currentScreens-screensTotal)<=5){
				((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+(currentScreens*800) +")");
				Thread.sleep(300);
				continue;
			}
			
			//计算到底部的次数，如果底部的计算次数不大于3次，就继续滑动到底部
			if(bottomCounter<=3){
				((JavascriptExecutor)driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
				Thread.sleep(300);
			}
				
			if(preHeight.compareTo(height)!=0){
				bottomCounter=0;
			}else{
				bottomCounter+=1;
			}
			preHeight=height;
		}
		
	}
	
	/*
	 *xpath解析
	 */
	public static WebElement parseXpath(WebDriver webDriver,String xpath) throws Exception{
		WebElement web = webDriver.findElement(By.xpath(xpath) );
		return web ;
	}
	
	/*
	 * 获取处理之后的url
	 */
	public static String handleUrl(String windowUrl,String path) {
		String currentUrl = windowUrl ;
		String header =  currentUrl.startsWith("https") ? "https" : "http" ; // 头部
		String url = null;
		//http链接特殊处理
			if(path.startsWith("//")){
				url = header+":"+path;
			}else if(path.startsWith("/")){ //加上域名
				currentUrl=currentUrl.replaceAll(header+"://", "");
				currentUrl= (String) currentUrl.subSequence(0, currentUrl.indexOf("/"));
				url = header+"://"+ currentUrl+path;
			}
			return url ;
	}
	
 
	public static Integer isLoadImage( Boolean isImage) {
		
		if(isImage!=null){ //主配置设置加载图片
			return isImage ?  -1 : 2 ;
		}
		
		//默认使用公共配置
		if("true".contains(ConfigUtil.getStringConfigByKey("load.image"))){
			return -1;
		}else if("false".contains(ConfigUtil.getStringConfigByKey("load.image"))){
			return 2;
		}else{
			logger.error("浏览器是否加载图片设置错误");
		}
		return null;
		
	}
	
   public static String match(String source, String element, String attr) {  
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";  
        Matcher m = Pattern.compile(reg).matcher(source);  
        String result = null;
        while (m.find()) {  
           result =  m.group(1);  
           break;
        }  
//        result = result.substring(0, result.indexOf("\">"));
        result = result.replaceAll("amp;", "");
       return  result;
    }  
	
   public static void snapshot(TakesScreenshot drivername, String filename)
   {
       // this method will take screen shot ,require two parameters ,one is driver name, another is file name
       
     String currentPath ="C:\\Users\\rocky\\Desktop\\snapshot";
     System.out.println(currentPath);
     File scrFile = drivername.getScreenshotAs(OutputType.FILE);
         // Now you can do whatever you need to do with it, for example copy somewhere
         try {
             System.out.println("save snapshot path is:"+currentPath+"/"+filename);
             FileUtils.copyFile(scrFile, new File(currentPath+"\\"+filename));
         } catch (IOException e) {
             // TODO Auto-generated catch block
             System.out.println("Can't save screenshot");
             e.printStackTrace();
         } 
         finally
         {
            
             System.out.println("screen shot finished");
         }
   }
   
   public static void main(String[] args) {
	   String success = "//div[@id='prodDetails']||//h2[contains(text(),'Product details')]" ;
		List<String> xpaths =Arrays.asList(success.split("\\|\\|"));  
		System.out.println(xpaths);
		System.out.println(success.indexOf("||"));
  }
}
