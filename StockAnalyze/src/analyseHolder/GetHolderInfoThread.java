package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import domain.HolderDomain;
import domain.HolderHistoryDomain;
import Util.Constant;
import Util.FileUtil;
import Util.HttpClientHelper;

public class GetHolderInfoThread implements Runnable{

	static HashMap<String, String> dateCountMap = new HashMap<String, String>();
	static String rootPath = "E:\\test\\";
	String holdersFileDir = rootPath + "holders\\";
	String code;
	List<String> dateList;
	Hashtable<String, List<HolderHistoryDomain>> holderMap;
	static {
		for(int i = 2017,j = 10; i > 1950; i--,j = j+10) {
			dateCountMap.put(i+"", j+"");
		}
	}
	GetHolderInfoThread(String code, List<String> dateList, Hashtable<String, List<HolderHistoryDomain>> holderMap){
		this.code = code;
		this.dateList = dateList;
		this.holderMap = holderMap;
	}
	public void run() {
		List<HolderHistoryDomain> hdList = new ArrayList<HolderHistoryDomain>();
		for(int i = 0; i < dateList.size();){
			String date = dateList.get(i);
			HolderHistoryDomain hhd = null;
			try {
				hhd = getHolderData(code, date);
				i++;
			} catch (Exception e) {
				//e.printStackTrace();
			}
			if(hhd != null) {
				hdList.add(hhd);
			}
		}
		//writeLtgdFileByList(code, hdList, holdersFileDir);
		writeJjcgFileByList(code, hdList, Constant.jjHoldersFileDir);
		holderMap.put(code, hdList);
	}
	private HolderHistoryDomain getHolderData(String code, String date) throws Exception {
		String codePara = "";
		if("6".equals(code.substring(0, 1))){
			codePara = code + "01";
		}else{
			codePara = code + "02";
		}
		String year = date.substring(0,4);
		String count = dateCountMap.get(year);
		String url = "http://emh5.eastmoney.com/F10/V/GetShareHolderJson?Count="+count+"&selectDate=" + date + "&fc=" + codePara;
		String result = HttpClientHelper.sendGet(url, null, "UTF-8");
		//String result = FileUtil.readByFileWithEncoding(new File(rootPath + "\\流通股东统计\\holderJson.txt"), "GBK");
		HolderHistoryDomain hhd = getHolderHistoryDomainByString(result);
		//System.out.println(url);
		hhd.setDate(date);
		return hhd;
	}
	private HolderHistoryDomain getHolderHistoryDomainByString(String result) throws Exception {
		JSONArray shareHoldersdltgd = null;
		JSONArray shareHolderJjcg = null;
		try {
			JSONObject json = JSONObject.parseObject(result);
			shareHoldersdltgd = json.getJSONArray("ShareHolderSdltgd");
			shareHolderJjcg = json.getJSONArray("ShareHolderJjcg");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			System.out.println(result + e.getMessage());
			throw e;
		}
		HolderHistoryDomain hhd = new HolderHistoryDomain();
		for(int i = 0; i < shareHoldersdltgd.size(); i++){
			JSONObject holder = shareHoldersdltgd.getJSONObject(i);
			String date = holder.getString("ShareHolderDate");
			String name = holder.getString("Gdmc");
			String amount = holder.getString("Cgs");
			String rate = holder.getString("Cgbl");
			String upDown = holder.getString("Zj");
			String change = holder.getString("Bdbl");
			HolderDomain hd = new HolderDomain(date, name, amount, rate, upDown, change);
			hhd.getLtgdList().add(hd);
		}
		if(shareHolderJjcg != null) {
			for(int i = 0; i < shareHolderJjcg.size(); i++) {
				JSONObject holder = shareHolderJjcg.getJSONObject(i);
				String date = holder.getString("ReportDate");
				String name = holder.getString("Jjmc");
				String amount = holder.getString("Cgsz");
				String rate = holder.getString("Zzgbb");
				HolderDomain hd = new HolderDomain(date, name, amount, rate, "", "");
				hhd.getJjHolders().add(hd);
			}
		}
		return hhd;
	}
	private void writeLtgdFileByList(String code, List<HolderHistoryDomain> hhdList, String holdersFileDir) {
			File file = FileUtil.createFileDeleteSource(holdersFileDir + code + ".txt");
			for(HolderHistoryDomain hhd : hhdList){
				List<HolderDomain> ltgdList = hhd.getLtgdList();
				String date = hhd.getDate();
				for(HolderDomain hd : ltgdList){
					String name = hd.getName();
					String amount = hd.getAmount();
					String rate = hd.getRate();
					String upDown = hd.getUpDown();
					String change = hd.getChange();
					String data = date + "\t" + name + "\t" + amount + "\t" + rate + "\t" + upDown + "\t" + change + "\n";
					//System.out.println(data);
					FileUtil.writeFileAppendWithEncode(file, data, "GBK");
				}
			}
	}
	
	private void writeJjcgFileByList(String code, List<HolderHistoryDomain> hhdList, String dir) {
		File file = FileUtil.createFileDeleteSource(dir + code + ".txt");
		for(HolderHistoryDomain hhd : hhdList){
			List<HolderDomain> jjcgList = hhd.getJjHolders();
			String date = hhd.getDate();
			for(HolderDomain hd : jjcgList){
				String name = hd.getName();
				String amount = hd.getAmount();
				String rate = hd.getRate();
				String data = date + "\t" + name + "\t" + amount + "\t" + rate + "\n";
				//System.out.println(data);
				FileUtil.writeFileAppendWithEncode(file, data, "GBK");
			}
		}
}
}
