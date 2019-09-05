package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import Util.Constant;
import Util.FileUtil;
import domain.HolderDomain;

public class HolderConnectThread implements Runnable{
	public static Object lockObj = new Object();
	String name;
	Set<String> names;
	int connectNum;
	HolderConnectThread(String name, Set<String> names, int connectNum){
		this.name = name;
		this.names = names;
		this.connectNum = connectNum;
	}

	public void run() {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		for(String otherName : names){
			if(otherName.equals(name) || otherName.equals("合计") || name.equals("合计")){
				continue;
			}
			List<String> codes = getConnectCode(name, otherName);
			if(codes.size() >= connectNum){
				result.put(name+"$"+otherName, codes);
				//String s = name + "$$" + otherName + "$$" + codes.toString()+"\n\r";
				//FileUtil.writeFileAppendWithEncode(Constant.sdltHoldersConnectFile, s, "GBK");
				//System.out.println("result.size():" + result.size() + "\t" + name +"_"+otherName + "\t" + codes.toString());
			}
		}
		
		if(result.size() > 0){
			FileUtil.writeByMapWithEncoding(result, new File(Constant.sdltHoldersConnectFilePath+name+".txt"), "GBK");
		}
		
	}
	
	private static List<String> getConnectCode(String name, String otherName) {
		List<String> holderStockList1 = getHolderStockList(name);
		List<String> holderStockList2 = getHolderStockList(otherName);
		List<String> holderStockDateList = getTwoListContain(holderStockList1, holderStockList2);
		List<String> holderStockList = getCodeList(holderStockDateList);
		return holderStockList;
	}
	
	public static List<String> getHolderStockList(String name){
		List<String> result = new ArrayList<String>();
		HashMap<String, List<HolderDomain>> hashMap = HolderAnalyse.holderDateStockMap.get(name);
		Set<String> dates = hashMap.keySet();
		for(String date : dates){
			List<HolderDomain> list = hashMap.get(date);
			for(HolderDomain hd : list){
				String code = hd.getCode();
				result.add(code+"_"+date);
			}
		}
		return result;
	}
	
	public static List<String> getTwoListContain(List<String> list1, List<String> list2){
		List<String> result = new ArrayList<String>();
		for(String s : list1){
			if(list2.contains(s)){
				if(!result.contains(s)){
					result.add(s);
				}
			}
		}
		return result;
	}
	public static List<String> getCodeList(List<String> list){
		List<String> temp = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		for (String s : list) {
			String code = s.substring(0, s.indexOf("_"));
			if (!temp.contains(code)) {
				temp.add(code);
				result.add(s);
			}
		}
		return result;
	}
}
