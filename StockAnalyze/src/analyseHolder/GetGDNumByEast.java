package analyseHolder;

import java.io.File;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import Util.Constant;
import Util.FileUtil;
import Util.HttpClientHelper;
import analyseBasicInfo.BasicAnalyse;

public class GetGDNumByEast {

	public static void main(String[] args) {
		BasicAnalyse.initStockBasicMap();
		call();
	}

	private static void call() {
		for(String code : BasicAnalyse.stockBasicMap.keySet()) {
			StringBuilder sb = new StringBuilder();
			String rCode;
			if(code.startsWith("6")) {
				rCode = "sh" + code;
			}else {
				rCode = "sz" + code;
			}
			String url = "http://emweb.securities.eastmoney.com/PC_HSF10/ShareholderResearch/ShareholderResearchAjax?code="+rCode ;
			String rsp = null;
			JSONArray jsonArray;
			while(true) {
				try {
					rsp = HttpClientHelper.sendGet(url, null, "UTF-8");
					JSONObject jsonObj = JSONObject.parseObject(rsp);
					jsonArray = jsonObj.getJSONArray("gdrs");
					break;
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			if(jsonArray == null) {
				System.out.println("jsonArray == null");
				continue;
			}
			for(int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String date = jsonObject.getString("rq");
				String gdrs = jsonObject.getString("gdrs");
				sb.append(date + "\t" + gdrs + "\n");
			}
			File file = FileUtil.createFileDeleteSource(Constant.gdNumFileDir + code + ".txt");
			FileUtil.writeByFileWithEncoding(sb.toString(), file, "GBK");
		}
	}
	
	public static double getNumOfStr(String str){
		int wanIndex = str.indexOf("万");
		int yiIndex = str.indexOf("亿");
		if(wanIndex > -1) {
			double result = Double.parseDouble(str.substring(0, wanIndex)) * 10000;
			return result;
		}

		if(yiIndex > -1) {
			double result = Double.parseDouble(str.substring(0, yiIndex)) * 100000000;
			return result;
		}
		return Double.parseDouble(str);
	}

}
