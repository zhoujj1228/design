package analyseConcept;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.record.common.UnicodeString;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import Util.Constant;
import Util.FileUtil;
import Util.HttpClientHelper;
import Util.PatternUtil;
import Util.StringUtil;

public class GetStockConceptData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getStockConceptData(Constant.concept4FilePath);
	}

	private static void getStockConceptData(String path) {
		String s = HttpClientHelper.sendGet("http://stock.jrj.com.cn/concept/conceptList.shtml?sort=todaypl&order=desc&test=3", null, "GBK");
		List<List<String>> patternList = PatternUtil.getPatternList(s, "conceptDetail_(.*?).shtml\">(.*?)</a>", 2);
		//System.out.println(patternList);
		for(List<String> list : patternList){
			String engSmplName = list.get(1);
			String name = list.get(2);
			getConceptStock(engSmplName, path, name);
		}
		
	}

	private static void getConceptStock(String engSmplName, String path, String fileName) {
		File file = FileUtil.createFileDeleteSource(path + fileName + ".txt");
		String s = HttpClientHelper.sendGet("http://stock.jrj.com.cn/concept/conceptdetail/conceptStock_"+engSmplName+".js", null, "GBK");
		s = s.replaceAll("var conceptstockdata=", "");
		s = s.replaceAll(";", "");
		//String json = StringUtil.unicodeAndStr2String(s);
		//System.out.println(StringUtil.unicodeAndStr2String(s));
		JSONObject jsonObj = JSONObject.parseObject(s);
		JSONArray conceptStockArray = jsonObj.getJSONArray("stockData");
		for(int i = 0; i < conceptStockArray.size(); i++){
			JSONArray stockDataArray = conceptStockArray.getJSONArray(i);
			String code = stockDataArray.getString(0);
			String name = stockDataArray.getString(1);
			name = StringUtil.unicodeAndStr2String(name);
			String reason = stockDataArray.getString(2);
			reason = StringUtil.unicodeAndStr2String(reason);
			String temp = code + "\t" + name + "\t" + reason + "\n";
			//System.out.println(temp);
			FileUtil.writeFileAppendWithEncode(file, temp, "GBK");
		}
	}

}
