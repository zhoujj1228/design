package Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class NumCountUtil {
	public static HashMap<String, Double> numMap = new HashMap<String, Double>();
	public static void main(String[] args) {
		
	}
	public static void init(){
		numMap = new HashMap<String, Double>();
	}
	
	public static HashMap<String, Double> getNumMap(){
		return numMap;
	}
	
	public static void increase(String key, double value){
		double num = 0;
		if(numMap.containsKey(key)){
			num = numMap.get(key);
		}
		num = num + value;
		numMap.put(key, num);
	}
	
	public static void decrease(String key, double value){
		double num = 0;
		if(numMap.containsKey(key)){
			num = numMap.get(key);
		}
		num = num - value;
		numMap.put(key, num);
	}
	
	public static List<String> getSortList(){
		List<String> list = new ArrayList<String>();
		for(String key : numMap.keySet()){
			list.add(key + "_" + AnalyseUtil.displayDoubleNum(numMap.get(key)));
		}
		list.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String num1 = o1.split("_")[1];
				String num2 = o2.split("_")[1];
				double parseDouble1 = Double.parseDouble(num1);
				double parseDouble2 = Double.parseDouble(num2);
				if(parseDouble1 > parseDouble2){
					return 1;
				}
				if(parseDouble1 < parseDouble2){
					return -1;
				}
				return 0;
			}
		});
		//AnalyseUtil.displayList(list);
		return list;
	}
}
