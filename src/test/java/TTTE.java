import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;



public class TTTE {

	    
	    /*
	     * 获取当前网络时间
	     *  //在这里，如果是想返回sql的Date类型则修改方法的返回类型
	　　　　　　 //将目前获取到的网络时间util.Date转换成sql.Date的操作如下：
	 　　　　　 // java.sql.Date date1=new java.sql.Date(date.getTime());//年 月 日
	 　　　　　 //java.sql.Time date2=new java.sql.Time(date.getTime());//时   分    秒
	 　　　　　 //java.sql.Timestamp date3=new java.sql.Timestamp(date.getTime());//年  月 日  时  分   秒 毫秒
	 　　　　　 //System.out.println("输出当前时间年月日"+date1);
	　　　　　　// System.out.println("输出当前时间时分秒"+date2);
	　　　　　　//System.out.println("输出当前时间年月日时分秒毫秒"+date3);


	　　　　　　　//以下是将时间转换成String类型并返回
	     */
	    public static String getNetworkTime() {
	    	
	    	String webUrl="http://www.baidu.com";
	        try {
	            URL url=new URL(webUrl);
	            URLConnection conn=url.openConnection();
	            conn.connect();
	            long dateL=conn.getDate();
	            Date date=new Date(dateL);
	            SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd HH:mm");
	            return dateFormat.format(date);
	        }catch (MalformedURLException e) {
	            e.printStackTrace();
	        }catch (IOException e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	        return "";
	    }
	    
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		
		  //获取当前网络时间
        String webTime=getNetworkTime();
        Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(webTime);//定义起始日期
        System.out.println(d1);
        System.out.println("当前网络时间为："+webTime);
	
		
	/*	for (int i = 1001; i <= 1890; i++) { //1942
//			String s ="http://smg.xizihz.com/#/info/index?id="+i ; 
			String s = "https://www.chemicalbook.com/ProductNameList_1_"+i+"00.htm";
			System.out.println(s);
		}*/
//		System.out.println("https://shop-phinf.pstatic.net/20170512_229/dearmami_1494563934639VE9G4_JPEG/SAM_4659.JPG, https://shop-phinf.pstatic.net/20170512_287/dearmami_1494564005193oYezr_JPEG/SAM_4702.jpg, https://shop-phinf.pstatic.net/20170512_198/dearmami_1494564133849YeVoD_JPEG/SAM_4704.jpg, https://shop-phinf.pstatic.net/20180313_202/500198877_1520905575338hElNl_JPEG/SAM_4714.jpg, https://shop-phinf.pstatic.net/20180313_90/500198877_1520905575994b3jJ4_JPEG/SAM_4711.jpg, https://shop-phinf.pstatic.net/20180313_269/500198877_15209055739283yCR4_JPEG/SAM_4717.jpg, https://shop-phinf.pstatic.net/20180313_23/500198877_1520906583163TIVHY_JPEG/image_8494274811520906554883.jpg, https://shop-phinf.pstatic.net/20170512_229/dearmami_14945664599118czYQ_JPEG/SAM_4710.jpg, https://shop-phinf.pstatic.net/20170512_199/dearmami_1494563661564zOmGi_JPEG/SAM_4628.jpg, https://shop-phinf.pstatic.net/20170512_63/dearmami_1494563661744XABww_JPEG/SAM_4623.jpg, https://shop-phinf.pstatic.net/20170512_280/dearmami_1494564994071WvoSr_JPEG/SAM_4624.jpg, https://shop-phinf.pstatic.net/20170512_74/dearmami_1494564994176IPpsQ_JPEG/SAM_4625.jpg, https://shop-phinf.pstatic.net/20170512_167/dearmami_1494564994284b1rgh_JPEG/SAM_4634.jpg, https://shop-phinf.pstatic.net/20170512_177/dearmami_1494564994383LPRoK_JPEG/SAM_4647.jpg, https://shop-phinf.pstatic.net/20170512_125/dearmami_1494565695732Q8zUy_JPEG/SAM_4615.jpg, https://shop-phinf.pstatic.net/20170512_27/dearmami_1494565695844ae46a_JPEG/SAM_4616.jpg, https://shop-phinf.pstatic.net/20170512_24/dearmami_1494565695991lPmXU_JPEG/SAM_4617.jpg, https://shop-phinf.pstatic.net/20170512_153/dearmami_14945661779375YKGR_JPEG/SAM_4707.jpg, https://shop-phinf.pstatic.net/20170512_232/dearmami_1494565292162WuxtW_JPEG/SAM_4716.jpg, https://shop-phinf.pstatic.net/20170512_68/dearmami_1494565292322Gtq1S_JPEG/SAM_4714.jpg, https://shop-phinf.pstatic.net/20170512_146/dearmami_1494566700395pMhb0_JPEG/SAM_4607.jpg, https://shop-phinf.pstatic.net/20170512_194/dearmami_1494566726229hTzES_JPEG/SAM_4655.jpg, https://shop-phinf.pstatic.net/20170512_8/dearmami_1494566743178jovi5_JPEG/SAM_4612.jpg, https://shop-phinf.pstatic.net/20170512_171/dearmami_1494566755217IyCCi_JPEG/SAM_4613.jpg, https://shop-phinf.pstatic.net/20170512_19/dearmami_1494565944378VaWUN_JPEG/SAM_4619.jpg, https://shop-phinf.pstatic.net/20170512_151/dearmami_1494565944496Imm9J_JPEG/SAM_4622.jpg, https://shop-phinf.pstatic.net/20170512_282/dearmami_1494565944616782hm_JPEG/SAM_4663.jpg, https://shop-phinf.pstatic.net/20170512_75/dearmami_1494565944715jQzn4_JPEG/SAM_4664.jpg, https://shop-phinf.pstatic.net/20170512_251/dearmami_1494565944845oIXaz_JPEG/SAM_4665.jpg, https://shop-phinf.pstatic.net/20170512_201/dearmami_1494566029918bqIlV_JPEG/SAM_4713.jpg, https://shop-phinf.pstatic.net/20170512_243/dearmami_1494566030031Cf7K8_JPEG/SAM_4711.jpg, https://shop-phinf.pstatic.net/20170512_293/dearmami_1494566240685WVW7U_JPEG/SAM_4597.jpg, https://shop-phinf.pstatic.net/20170512_268/dearmami_1494566240808OUCpv_JPEG/SAM_4601.jpg, https://shop-phinf.pstatic.net/20170512_244/dearmami_1494566240914OYW16_JPEG/SAM_4605.JPG, https://shop-phinf.pstatic.net/20170512_94/dearmami_1494566412414S6pyV_JPEG/SAM_4603.jpg, https://shop-phinf.pstatic.net/20170512_293/dearmami_1494566412531vhMCu_JPEG/SAM_4630.jpg, https://shop-phinf.pstatic.net/20170512_176/dearmami_1494566412676DtOTA_JPEG/SAM_4631.jpg, https://shop-phinf.pstatic.net/20170512_147/dearmami_14945664128294Ioi2_JPEG/SAM_4634.jpg, https://shop-phinf.pstatic.net/20170512_85/dearmami_1494566623690gmf8y_JPEG/SAM_4717.jpg, https://shop-phinf.pstatic.net/20170512_94/dearmami_1494566623834s5odW_JPEG/SAM_4721.jpg, https://shop-phinf.pstatic.net/20170512_133/dearmami_1494566792096efFD9_JPEG/SAM_4585.jpg, https://shop-phinf.pstatic.net/20170512_246/dearmami_1494566792246wwhXK_JPEG/SAM_4589.jpg, https://shop-phinf.pstatic.net/20170512_28/dearmami_1494566821322j6cFe_JPEG/SAM_4593.jpg, https://shop-phinf.pstatic.net/20170512_97/dearmami_1494566847210a91Hi_JPEG/SAM_4591.jpg, https://shop-phinf.pstatic.net/20170512_159/dearmami_1494566905107qqKe7_JPEG/SAM_4595.jpg, https://shop-phinf.pstatic.net/20170512_55/dearmami_1494566905257R9dnh_JPEG/SAM_4596.jpg, https://shop-phinf.pstatic.net/20170512_246/dearmami_1494566905377VbhBj_JPEG/SAM_4640.jpg, https://shop-phinf.pstatic.net/20170512_257/dearmami_1494566905528JOYqQ_JPEG/SAM_4639.jpg, https://shop-phinf.pstatic.net/20170512_49/dearmami_1494566905833HnVRD_JPEG/SAM_4647.jpg, https://shop-phinf.pstatic.net/20170512_141/dearmami_1494566965347Mgm2G_JPEG/SAM_4619.jpg, https://shop-phinf.pstatic.net/20180313_153/500198877_1520906036269fFb5c_JPEG/image_5394704701520905970963.jpg".split(",").length);
		
	/*	
		Date d1 = new SimpleDateFormat("yyyy-M-d").parse("2012-1-1");//定义起始日期

		Date d2 = new SimpleDateFormat("yyyy-M-d").parse("2015-1-1");//定义结束日期
		Calendar dd = Calendar.getInstance();//定义日期实例

		dd.setTime(d1);//设置日期起始时间

		while(dd.getTime().before(d2)){//判断是否到结束日期

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

		String str = sdf.format(dd.getTime());

		System.out.println("http://c.spdex.com/MatchHistory?date="+str);//输出日期结果

		dd.add(Calendar.DATE, 1);//进行当前日期月份加1
*/
//		}
		
		
		
//		String s = "http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601044_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601023_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601029_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601048_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601049_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601044_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601001_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601002_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601003_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601004_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601005_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601006_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601007_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601008_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601009_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601010_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601011_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601012_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601013_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601014_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601015_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601016_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601017_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601018_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601019_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601020_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601021_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601022_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601023_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601024_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601025_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601026_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601027_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601028_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601029_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601030_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601031_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601032_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601033_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601034_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601035_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601036_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601037_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601038_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601039_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601040_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601041_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601042_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601043_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601044_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601045_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601046_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601047_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601048_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601049_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601050_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601051_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601052_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601053_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601054_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601055_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601056_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601057_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601058_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601059_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601060_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601061_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601062_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601063_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601064_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601065_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601066_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601067_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601068_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601069_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601070_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601071_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601072_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601073_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601074_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601075_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601076_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601077_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601078_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601079_750.jpg,http://2mm.i.ximgs.net/9/218929/20180121/20180121210234601080_750.jpg";
//		System.out.println(s.split("http").length);
//		Runtime rt = Runtime.getRuntime();
//		Process p = rt.exec("phantomjs.exe");
	/*	JFrame frame = new JFrame();
		frame.setSize(800, 500);
		InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); glasspane.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
		frame.setGlassPane(glasspane);
		glasspane.start();//开始动画加载效果
		frame.setVisible(true);*/
		 
		// Later, to disable,在合适的地方关闭动画效果
//		glasspane.stop();
		/*ImageProperty imageProperty = new ImageProperty();
		imageProperty.setMinHeight(100);
		imageProperty.setMinSize(20);
		String s = JSONObject.toJSONString(imageProperty);
		System.out.println(s);
		FolderUtil.createFile(new File("config/serialize.json"), s);
		String content = FolderUtil.readFileContent(new File("config/serialize.json"));
		System.out.println(content);
		JSONObject jsonObject=JSONObject.parseObject(content);
		ImageProperty image =  JSONObject.toJavaObject(jsonObject, ImageProperty.class);
//		ImageProperty image =  JSONObject.toB (JSONObject.toJSONString(content), ImageProperty.class);
		 System.out.println(image);
//		ImageProperty s = JSONObject.toJavaObject(JSONObject.class.cast(imageProperty), ImageProperty.class);
//		serializeProxy
*/
	}

}
