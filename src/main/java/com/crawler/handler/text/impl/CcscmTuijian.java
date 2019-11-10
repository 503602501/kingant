package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.StringUtils;

/*
 * 个性推荐所有信息采集
 */
public class CcscmTuijian implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,final TextUnit textUnit,Storage storage) {
		String html = "";
		boolean flag = true;
		/*****是否存在iframe框架******/
		try {
			WebElement iframe =  webDriver.findElement(By.xpath("//div[contains(@id,'div')]/iframe"));
			webDriver.switchTo().frame(iframe);
		} catch (Exception e) {
			System.out.println("框架进入异常");
			flag = false;
		}
		
		List<WebElement> elements = null;
		if(flag){
			try {
				  elements = webDriver.findElements(By.xpath(textUnit.getXpath()));
			}catch(NoSuchElementException ne){
				//查找不到就等待6秒进行查找
				long start =System.currentTimeMillis();
				 System.out.println("开始查找时间"+start);
				 WebDriverWait wait = new WebDriverWait(webDriver,6);  
		         wait.until(new ExpectedCondition<WebElement>(){  
		            @Override  
		            public WebElement apply(WebDriver d) {  
		                return d.findElement(By.xpath(textUnit.getXpath()));  
		            }
		          }); 
		         
		         System.out.println("耗时时间"+(System.currentTimeMillis()-start));
		         try{
		        	 elements = webDriver.findElements(By.xpath(textUnit.getXpath()));
		        	 flag = true;
		         }catch (Exception e) {
		        	 flag = false;
		        	 webDriver.switchTo().defaultContent();
		        	 e.printStackTrace();
		         }
			} catch (Exception e) {
				System.out.println("失败");
				flag = false;
				webDriver.switchTo().defaultContent();
				e.printStackTrace();
			}
		}
		//跳出iframe框架 
		if(flag){
			
			String html1 = elements.get(0).getAttribute("innerHTML");
			for (int i = 0; i < 15; i++) {
				if(StringUtils.isEmpty(elements.get(0).getText())){
					try {
						Thread.sleep(500); 
						elements = webDriver.findElements(By.xpath(textUnit.getXpath()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					break;
				}
			}
			
			if(!elements.isEmpty()){
				
				html1=html1.replaceAll("\r|\n", ""); 
				html1 = FilterUtil.getRegexContent(html1, "match|(?<=<ol)(.+?)(?=</ol>)");
				html1=html1.replaceFirst(">", "");
				String[] lis = html1.split("</li>");
				StringBuffer sb = new StringBuffer();
				for (String li : lis) {
					li = StringUtils.delHtml(li);
					if(!StringUtils.isEmpty(li) && sb.indexOf(li)==-1 ){
						sb.append(li+"|");
					}
				}
				
				String html2 =elements.size()==2 ? elements.get(1).getAttribute("innerHTML"): "";
				html2=html2.replaceAll("\r|\n", ""); 
				html2 = FilterUtil.getRegexContent(html2, "match|(?<=<ol)(.+?)(?=</ol>)");
				html2 = html2.substring(html2.indexOf(">")+1);
				
				lis = html2.split("</h4>|</li>|</p>");
				StringBuffer sb2 = new StringBuffer();
				for (String li : lis) {
					li = StringUtils.delHtml(li);
					if(!StringUtils.isEmpty(li) && sb2.indexOf(li)==-1){
						sb2.append(li+"|");
					}
				}
				html = sb.toString()+"<>"+sb2.toString();  //分割开
			}
			
			webDriver.switchTo().defaultContent();
		}
		if(html.equals("<>")){
			System.out.println("空数据");
			return "";
		}
		return html;
	}
	
	public static void main(String[] args) {
		String s = "style=\"max-height: 76px;\">飞asdfasdf";
		s = s.substring(s.indexOf(">")+1);
		
		System.out.println(s);
	}
	
}
