package analyseBusiness;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import Util.Constant;
import Util.FileUtil;
import Util.HttpClientHelper;
import analyseBasicInfo.BasicAnalyse;
import domain.BusinessManageRangeDomain;

public class GetBusinessManage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicAnalyse.initStockBasicMap();
		GetBusinessManage gm = new GetBusinessManage();
		gm.call();
	}

	private void call() {
		ExecutorService es = Executors.newFixedThreadPool(8);
		
		Set<String> codes = BasicAnalyse.stockBasicMap.keySet();
		for(final String code : codes){
			String urlCode = "";
			if(code.startsWith("6")){
				urlCode = "sh" + code;
			}else{
				urlCode = "sz" + code;
			}
			final String uCode = urlCode;
			es.execute(new Runnable() {
				public void run() {
					String jsonStr = getBusinessManageJSONStr(uCode);
					writeBusinessMagageFileByJson(jsonStr, code);
				}
			});
			es.shutdown();
		}
	}

	private void writeBusinessMagageFileByJson(String jsonStr, String code) {
		File file = FileUtil.createFileDeleteSource(Constant.businessManagePath + code + ".txt");
		if(jsonStr == null || jsonStr.equals("")) {
			return;
		}
		List<String> list = new ArrayList<String>();
		JSONObject json = null;
		JSONArray zyfw = null;
		try {
			json = JSONObject.parseObject(jsonStr);
			zyfw = json.getJSONArray("zyfw");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(code + jsonStr);
			return;
		}
		String zyfwAllStr = "";
		if(zyfw != null&& zyfw.size() > 0){
			StringBuilder sbzyfw = new StringBuilder();
			for(int i = 0; i < zyfw.size(); i++){
				JSONObject zyfwObj = zyfw.getJSONObject(i);
				String zyfwStr = zyfwObj.getString("ms");
				sbzyfw.append("$$" + zyfwStr);
			}
			zyfwAllStr = sbzyfw.substring(2);
		}
		list.add(zyfwAllStr);
		
		JSONArray jyps = json.getJSONArray("jyps");
		String jypsAllStr = "";
		if(jyps != null && jyps.size() > 0){
			StringBuilder sbjyps = new StringBuilder();
			for(int i = 0; i < jyps.size(); i++){
				JSONObject jypsObj = jyps.getJSONObject(i);
				String jypsStr =jypsObj.getString("ms");
				sbjyps.append("$$" + jypsStr);
			}
			jypsAllStr = sbjyps.substring(2);
		}
		list.add(jypsAllStr);
		
		JSONArray zygcfx = json.getJSONArray("zygcfx");
		if(zygcfx != null){
			for(int i = 0; i < zygcfx.size(); i++){
				JSONObject zygcfxObj = zygcfx.getJSONObject(i);
				String date =zygcfxObj.getString("rq");
				//System.out.println(date);
				JSONArray hy = zygcfxObj.getJSONArray("hy");
				for(int j = 0; j < hy.size(); j++){
					JSONObject hyObj = hy.getJSONObject(j);
					String type = "行业";
					String pdName = hyObj.getString("zygc");
					String income = hyObj.getString("zysr");
					String incomeRate = hyObj.getString("srbl");
					String cost = hyObj.getString("zycb");
					String costRate = hyObj.getString("cbbl");
					String profit = hyObj.getString("zylr");
					String profitRate = hyObj.getString("lrbl");
					String pdProfitRate = hyObj.getString("mll");
					list.add(date+ "\t" + type+ "\t" + pdName+ "\t" + income+ "\t" + incomeRate+ "\t" + cost+ "\t" + costRate+ "\t" + profit+ "\t" + profitRate+ "\t" + pdProfitRate);
				}
				
				JSONArray qy = zygcfxObj.getJSONArray("qy");
				for(int j = 0; j < qy.size(); j++){
					JSONObject hyObj = qy.getJSONObject(j);
					String type = "区域";
					String pdName = hyObj.getString("zygc");
					String income = hyObj.getString("zysr");
					String incomeRate = hyObj.getString("srbl");
					String cost = hyObj.getString("zycb");
					String costRate = hyObj.getString("cbbl");
					String profit = hyObj.getString("zylr");
					String profitRate = hyObj.getString("lrbl");
					String pdProfitRate = hyObj.getString("mll");
					list.add(date+ "\t" + type+ "\t" + pdName+ "\t" + income+ "\t" + incomeRate+ "\t" + cost+ "\t" + costRate+ "\t" + profit+ "\t" + profitRate+ "\t" + pdProfitRate);
					
				}
				
				JSONArray cp = zygcfxObj.getJSONArray("cp");
				for(int j = 0; j < cp.size(); j++){
					JSONObject hyObj = cp.getJSONObject(j);
					String type = "产品";
					String pdName = hyObj.getString("zygc");
					String income = hyObj.getString("zysr");
					String incomeRate = hyObj.getString("srbl");
					String cost = hyObj.getString("zycb");
					String costRate = hyObj.getString("cbbl");
					String profit = hyObj.getString("zylr");
					String profitRate = hyObj.getString("lrbl");
					String pdProfitRate = hyObj.getString("mll");
					list.add(date+ "\t" + type+ "\t" + pdName+ "\t" + income+ "\t" + incomeRate+ "\t" + cost+ "\t" + costRate+ "\t" + profit+ "\t" + profitRate+ "\t" + pdProfitRate);
				}
				
			}
			//System.out.println(list);
		}
		FileUtil.writeByFileAppendWithEncodeByList(list, file, "GBK");
	}

	private String getBusinessManageJSONStr(String code) {
		String url = "http://emweb.securities.eastmoney.com/PC_HSF10/BusinessAnalysis/BusinessAnalysisAjax?code=" + code;
		String rsp = null;
		int redoNum = 0;
		while(rsp == null || rsp.equals("") || redoNum > 10) {
			try {
				rsp = HttpClientHelper.sendGet(url, null, "UTF-8");
				if(redoNum > 10) {
					System.out.println(url + rsp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//String rsp = FileUtil.readByFileWithEncoding(new File("C:\\Users\\Administrator\\Desktop\\周家炬\\股票\\备份\\1016\\经营结构分析报文.txt"), "GBK");
		return rsp;
	}
	

}
