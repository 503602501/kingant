package com.crawler.handler.open.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.HttpUtil;
import com.crawler.util.JsonUtil;
import com.crawler.util.StringUtils;
 

public class ToutiaoUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(IisbnUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 
		Iterator<Product> iterator = storage.getInputUrlQueues().iterator();
		
		try {
			while ( iterator.hasNext()) {
				Product p = iterator.next();
				String contents= HttpUtil.getHtmlContent(p.getUrl());
				if(StringUtils.isEmpty(contents)){
					continue;
				}
				contents = contents.substring(contents.indexOf("BASE_DATA.galleryInfo ="));
				contents = contents.replace("BASE_DATA.galleryInfo =", "");
				contents = contents.substring(0, contents.indexOf("</script>"));
				String title = contents.substring(contents.indexOf("title:")+6, contents.indexOf(",")).replace("'", "");
				String gallery = contents.substring(contents.indexOf("JSON.parse(\"")+12,contents.indexOf("siblingList"));
				gallery = gallery.trim() ;
				gallery = gallery.substring(0,gallery.length()-3);
				gallery = StringEscapeUtils.unescapeJava(gallery);
				List<String> imgages = JsonUtil.getJSONListValue("sub_images->url", gallery);
				
				Storage.addText("文件夹名-标题", title, storage);
				
				for (String string : imgages) {
					Storage.addText("链接", string, storage);
				}
				/*System.out.println(gallery);
				System.out.println(title);
				System.out.println(contents);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		storage.getInputUrlQueues().clear();
		
		
	}

}

