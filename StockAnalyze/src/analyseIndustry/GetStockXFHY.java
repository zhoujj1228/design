package analyseIndustry;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import domain.BasicInfoDomain;
import Util.FileUtil;
import Util.HttpClientHelper;
import analyseBasicInfo.BasicAnalyse;

public class GetStockXFHY {
	String saveFileDir = "E:\\test\\行业\\";
	public static void main(String[] args) {
		GetStockXFHY test = new GetStockXFHY();
		//String rsp = HttpClientHelper.sendGet("http://stockpage.10jqka.com.cn/000969/field/", null, "UTF-8");
		test.call();
	}

	private void call() {
		ExecutorService ex = Executors.newFixedThreadPool(50);
		final File file = FileUtil.createFileDeleteSource(saveFileDir + "细分行业.txt");
		HashMap<String, BasicInfoDomain> basicStockMap = BasicAnalyse.initStockBasicMap();
		Set<String> codes = basicStockMap.keySet();
		for(final String code : codes) {
			ex.execute(new Runnable() {
				public void run() {
					while(true) {
						String rsp = null;
						try {
							rsp = HttpClientHelper.sendGet("http://stockpage.10jqka.com.cn/" + code + "/field/", null, "UTF-8");
							String tempStr = getString(rsp);
							if(tempStr == null) {
								break;
							}
							String industryData = tempStr.replaceAll(" -- ", "\t");
							String data = code + "\t" + industryData + "\n";
							FileUtil.writeFileAppendWithEncode(file, data, "GBK");
							break;
						} catch (Exception e) {
							System.out.println(rsp);
							e.printStackTrace();
						}
					}
				}
			});
			
		}
		ex.shutdown();
	}
	
	private static String getString(String s) {
		String result = null;
		Pattern pattern = Pattern.compile("三级行业分类：<span class=\"tip f14\">(.+?) （共");// 匹配的模式
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			result = matcher.group(1); // 每次返回第一个即可 可用groupcount()方法来查看捕获的组数 个数
			//System.out.println(matcher.groupCount());
		}
		return result;
	}
}
