package analyseConcept;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Util.Constant;
import Util.FileUtil;
import analyseBasicInfo.BasicAnalyse;
import analyseBusiness.BusinessAnalyse;

public class ConceptAnalyse {
	public static HashMap<String, List<String>> concept3Map = new HashMap<String, List<String>>();
	public static HashMap<String, List<String>> stockConcept3Map = new HashMap<String, List<String>>();
	public static void main(String[] args) {
		initConcept3Map();
		initStockConcept3Map();
		BusinessAnalyse.initBusinessIncomeMap();
		BusinessAnalyse.initProfitMap();
		call();
	}
	private static void call() {
		List<String> list = concept3Map.get("京津冀");
		List<String> list1 = concept3Map.get("节能环保");
		list.retainAll(list1);
		System.out.println(getConceptListMap(list));

		List<String> list2 = concept3Map.get("雄安新区");
		List<String> list3 = concept3Map.get("节能环保");
		list2.retainAll(list3);
		HashMap<String, List<String>> conceptListMap = getConceptListMap(list2);
		System.out.println(conceptListMap);
		List<String> twoYearIncomeInRateList = BusinessAnalyse.getTwoYearIncomeInRateList(conceptListMap.keySet(), 10, 10000, "2017-2", "2016-2");
		List<String> twoYearProfitInRateList = BusinessAnalyse.getTwoYearProfitInRateList(twoYearIncomeInRateList, 10, 10000, "2017-2", "2016-2");
		
		System.out.println(twoYearProfitInRateList);
		
	}
	
	public static HashMap<String, Integer> getConceptTimeCount(HashMap<String, List<String>> conceptMap, int startNum, int endNum){
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		HashMap<String, Integer> result2 = new HashMap<String, Integer>();
		for(String code : conceptMap.keySet()){
			List<String> list = conceptMap.get(code);
			if(list == null){
				continue;
			}
			for(String concept : list){
				int count = 0;
				if(result.containsKey(concept)){
					count = result.get(concept);
				}
				count++;
				result.put(concept, count);
			}
		}
		for(String concept : result.keySet()){
			int count = result.get(concept);
			if(count >= startNum & count <= endNum){
				result2.put(concept, count);
				System.out.println("getConceptTimeCount:" + concept + "\t" + count);
			}
		}
		return result2;
	}
	
	public static HashMap<String, List<String>> initConcept3Map() {
		List<String> list = FileUtil.readByFileToList(new File(Constant.concept3FilePath), "GBK");
		for(String s : list) {
			String[] split = s.split("\t");
			String name = split[0];
			List<String> stockList = new ArrayList<String>();
			for(int i = 1; i < split.length; i++) {
				stockList.add(split[i]);
			}
			concept3Map.put(name, stockList);
		}
		return concept3Map;
	}
	
	public static HashMap<String, List<String>> initStockConcept3Map() {
		List<String> list = FileUtil.readByFileToList(new File(Constant.concept3FilePath), "GBK");
		for(String s : list) {
			String[] split = s.split("\t");
			String name = split[0];
			for(int i = 1; i < split.length; i++) {
				String code = split[i];
				List<String> conceptList = stockConcept3Map.get(code);
				if(conceptList == null) {
					conceptList = new ArrayList<String>();
				}
				conceptList.add(name);
				stockConcept3Map.put(code, conceptList);
			}
		}
		return stockConcept3Map;
	}
	
	public static HashMap<String, List<String>> getConceptListMap(Collection<String> c) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		for(String code : c) {
			List<String> list = stockConcept3Map.get(code);
			result.put(code, list);
		}
		return result;
	}
	
	public static void displayNameAndCocept(Collection<String> c){
		for(String code : c) {
			List<String> list = stockConcept3Map.get(code);
			System.out.println("displayNameAndCocept:" + code + "\t" +  BasicAnalyse.getName(code) + "\t" + list);
		}
	}
}
