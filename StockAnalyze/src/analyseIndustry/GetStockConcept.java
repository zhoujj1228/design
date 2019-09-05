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

public class GetStockConcept {
	String saveFileDir = "E:\\test\\��ҵ\\";
	public static void main(String[] args) {
		GetStockConcept test = new GetStockConcept();
		//String rsp = HttpClientHelper.sendGet("http://stockpage.10jqka.com.cn/000969/field/", null, "UTF-8");
		test.call();
	}

	private void call() {
		ExecutorService ex = Executors.newFixedThreadPool(50);
		final File file = FileUtil.createFileDeleteSource(saveFileDir + "��������.txt");
		HashMap<String, BasicInfoDomain> basicStockMap = BasicAnalyse.initStockBasicMap();
		Set<String> codes = basicStockMap.keySet();
		for(final String code : codes) {
			ex.execute(new Runnable() {
				public void run() {
					while(true) {
						String rsp = null;
						try {
							rsp = HttpClientHelper.sendGet("http://stockpage.10jqka.com.cn/" + code + "/", null, "UTF-8");
							String tempStr = getString(rsp);
							if(tempStr == null) {
								break;
							}
							String conceptData = tempStr.substring(tempStr.indexOf("\"")+1, tempStr.lastIndexOf("\""));
							conceptData = conceptData.replaceAll("��", "\t");
							String data = code + "\t" + conceptData + "\n";
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
		Pattern pattern = Pattern.compile("�漰���</dt>(.+?)<dt>��Ӫҵ��");// ƥ���ģʽ
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			result = matcher.group(1); // ÿ�η��ص�һ������ ����groupcount()�������鿴��������� ����
			//System.out.println(matcher.group(1));
		}
		return result;
	}
}
