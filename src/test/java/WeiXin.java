import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.crawler.util.HttpUtil;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;



public class WeiXin {

	/**
	 * @param args
	 * @throws IOException 
//	 */
	public static void main(String[] args) throws Exception {
//		String s = HttpUtil.getHtmlContent("https://mon.snssdk.com/monitor/collect/?sdk_version=400&os_api=22&device_type=SM-G955F&ssmix=a&manifest_version_code=790&dpi=320&js_sdk_version=1.25.0.1&uuid=355757010216303&app_name=aweme&version_name=7.9.0&ts=1567641738&app_type=normal&ac=wifi&update_version_code=7902&channel=tengxun_new&_rticket=1567641738847&device_platform=android&iid=85187302329&version_code=790&openudid=d83062a5ca806056&device_id=69141304438&resolution=1600*900&os_version=5.1.1&language=zh&device_brand=samsung&aid=1128&mcc_mnc=46007&tt_data=a"); 
		String s = HttpUtil.getHtmlContent("https://aweme-hl.snssdk.com/aweme/v1/user/?sec_user_id=MS4wLjABAAAAGDbW8yUXrINqGGvZ4RrcXO94v6kSkSd8sF_urR6rg5Iy5H6S52NtO82bcdbM3LRT&address_book_access=1&retry_type=no_retry&iid=85187302329&device_id=69141304438&ac=wifi&channel=tengxun_new&aid=1128&app_name=aweme&version_code=790&version_name=7.9.0&device_platform=android&ssmix=a&device_type=SM-G955F&device_brand=samsung&language=zh&os_api=22&os_version=5.1.1&uuid=355757010216303&openudid=d83062a5ca806056&manifest_version_code=790&resolution=1600*900&dpi=320&update_version_code=7902&_rticket=1567654158324&mcc_mnc=46007&ts=1567654148&app_type=normal&js_sdk_version=1.25.3.0");
		System.out.println(s);
		
	}

}
