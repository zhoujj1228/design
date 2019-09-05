package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import Util.AnalyseUtil;
import Util.Constant;
import Util.FileUtil;
import domain.HolderDomain;

public class HolderConnectWithCPNumThread implements Runnable {
	File file;
	HashMap<String, List<String>> result;
	int connectNum;
	int peopleNum;
	boolean isRemoveJG = false;
	boolean isNextSeasonNotIN = false;

	public HolderConnectWithCPNumThread(File file, HashMap<String, List<String>> result, int connectNum,
			int peopleNum, boolean isRemoveJG, boolean isNextSeasonNotIN) {
		super();
		this.file = file;
		this.result = result;
		this.connectNum = connectNum;
		this.peopleNum = peopleNum;
		this.isRemoveJG = isRemoveJG;
		this.isNextSeasonNotIN = isNextSeasonNotIN;
	}


	public void run() {
		HashMap<String, List<String>> resultMap = new HashMap<String,  List<String>>();
		HashMap<String, List<String>> codeDateToNameMap = new HashMap<String,  List<String>>();
		HashMap<String, List<String>> namesToCodeDateMap = new HashMap<String,  List<String>>();
		List<String> strList = FileUtil.readByFileToList(file, "GBK");
		String name = null;
		try {
			name = file.getName().substring(0,file.getName().lastIndexOf("."));
		} catch (Exception e) {
			System.out.println("file.getName():" + file.getName());
			e.printStackTrace();
			return;
		}
		for(String s : strList){
			String[] split = s.split("\t");
			String nameStr = split[0];
			String[] names = nameStr.split("\\$");
			String targetName = names[1];
			if(isRemoveJG){
				if(name.length() > 3){
					break;
				}
				if(targetName.length() > 3){
					continue;
				}
			}
			for(int i = 1; i < split.length; i++){
				String codeDate = split[i];
				List<String> nameList;
				if(codeDateToNameMap.containsKey(codeDate)){
					nameList = codeDateToNameMap.get(codeDate);
				}else{
					nameList = new ArrayList<String>();
					nameList.add(name);
				}
				nameList.add(targetName);
				codeDateToNameMap.put(codeDate, nameList);
			}
		}
		Set<String> codeDates = codeDateToNameMap.keySet();
		codeDateItor:for(String codeDate : codeDates){
			List<String> nameList = codeDateToNameMap.get(codeDate);
			if(nameList.size() >= peopleNum){
				if(isNextSeasonNotIN){
					String[] split = codeDate.split("_");
					//String code = split[0];
					String date = split[1];
					String nextSeasonDate = AnalyseUtil.getNextSeasonDate(date, "/", Constant.today);
					if(nextSeasonDate == null){
						continue codeDateItor;
					}
					for(String cname : nameList){
						List<HolderDomain> list = null;
						try {
							list = HolderAnalyse.holderDateStockMap.get(cname).get(nextSeasonDate);
						} catch (Exception e) {
							System.out.println("HolderAnalyse.holderDateStockMap.get(cname) == null:"+cname);
							e.printStackTrace();
							return;
						}
						if(list != null && list.size() > 0){
							continue codeDateItor;
						}
					}
				}
				List<String> sortNameList = AnalyseUtil.getSortStringList(nameList, true);
				String names = AnalyseUtil.listToString(sortNameList, "$");
				List<String> codeDateList;
				if(namesToCodeDateMap.containsKey(names)){
					codeDateList = namesToCodeDateMap.get(names);
				}else{
					codeDateList = new ArrayList<String>();
				}
				codeDateList.add(codeDate);
				namesToCodeDateMap.put(names, codeDateList);
			}
		}
		
		Set<String> namesSet = namesToCodeDateMap.keySet();
		for(String names : namesSet){
			List<String> list = namesToCodeDateMap.get(names);
			if(list.size() >= connectNum){
				resultMap.put(names, list);
				System.out.println(names + "----" + list.toString());
			}
		}
		if(resultMap.size() > 0){
			FileUtil.writeByMapWithEncoding(resultMap, new File(Constant.sdltHoldersPath + 
					"connect"+ connectNum + peopleNum + "\\" + name + ".txt"), Constant.encode);
			System.out.println("HolderAnalyse.ex4.getQueue().size():" + name + HolderAnalyse.ex4.getQueue().size() + "--" +resultMap.size());
		}
		
	}

}
