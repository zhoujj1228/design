package Util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import analyseBasicInfo.BasicAnalyse;
import domain.BasicInfoDomain;
import domain.HistoryDomain;
import domain.HolderHistoryDomain;

public class AnalyseUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getAfterDate("20120101", "yyyyMMdd", 50);
		System.out.println(getTranDate("20171230", "yyyyMMdd"));
	}
	
	public static String displayDoubleNum(double num){
		BigDecimal bd = new BigDecimal(num);
		return bd.toString();
	}
	
	public static HashMap<String, String> getCodeDateMap(Collection<String> codes, String date){
		 HashMap<String, String> result = new HashMap<String, String>();
		 for(String code : codes){
			 result.put(code, date);
		 }
		 return result;
	}
	
	public static String getAfterDate(String dateStr, String pattern, int dateNum){
		Date date = parseDate(dateStr, pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, dateNum);
		Date resultDate = c.getTime();
		String resultDateStr = parseDate(resultDate, pattern);
		//System.out.println(resultDateStr);
		return resultDateStr;
	}
	
	public static HashMap<String, String> getAfterDateMap(HashMap<String, String> codeDateMap, String pattern, int dateNum){
		 HashMap<String, String> result = new HashMap<String, String>();
		 for(String code : codeDateMap.keySet()){
			 String date = codeDateMap.get(code);
			 String afterDate = getAfterDate(date, pattern, dateNum);
			 result.put(code, afterDate);
		 }
		 return result;
	}
	
	public static HashMap<String, String> getAfterTranDateMap(HashMap<String, String> codeDateMap, String pattern, int dateNum){
		 HashMap<String, String> result = new HashMap<String, String>();
		 for(String code : codeDateMap.keySet()){
			 String date = codeDateMap.get(code);
			 String afterDate = getAfterDate(date, pattern, dateNum);
			 String tranDate = getTranDate(afterDate, pattern);
			 result.put(code, tranDate);
		 }
		 return result;
	}
	
	public static String getTranDate(String dateStr, String pattern) {
		Date date = parseDate(dateStr, pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int i = c.get(Calendar.DAY_OF_WEEK);
		if(i == 1) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		if(i == 7) {
			c.add(Calendar.DAY_OF_MONTH, 2);
		}
		String result = parseDate(c.getTime(), pattern);
		return result;
	}
	
	public static void displayList(List list){
		for(Object obj : list){
			System.out.println(obj);
		}
	}
	
	public static Date parseDate(String dateStr, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String parseDate(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dateStr = null;
		dateStr = sdf.format(date);
		return dateStr;
	}
	
	/**
	 * 把2017/6/30转化为2017/06/30这种格式的日期
	 * @param dateStr
	 * @param regex
	 * @return
	 */
	public static String changNumberDate(String dateStr, String regex){
		String[] split = dateStr.split(regex);
		String month = split[1];
		String day = split[2];
		if(month.length() < 2){
			month = "0" + month;
		}
		if(day.length() < 2){
			day = "0" + day;
		}
		return split[0] + month + day;
	}
	
	/**
	 * 把2017/6/30转化为2017/06/30这种格式的日期
	 * @param dateStr
	 * @param regex
	 * @return
	 */
	public static String changNumberDate(String dateStr, String regex, String newRegex){
		String[] split = dateStr.split(regex);
		String month = split[1];
		String day = split[2];
		if(month.length() < 2){
			month = "0" + month;
		}
		if(day.length() < 2){
			day = "0" + day;
		}
		return split[0] + newRegex + month + newRegex + day;
	}
	
	public static String getNextSeasonDate(String dateStr, String regex, String today){
		String[] split = dateStr.split(regex);
		int year = Integer.parseInt(split[0]);
		int month =Integer.parseInt(split[1]);
		int day = Integer.parseInt(split[2]);
		if(month == 3){
			month = 6;
			day = 30;
		}else if(month == 6){
			month = 9;
			day = 30;
		}else if(month == 9){
			month = 12;
			day = 31;
		}else if(month == 12){
			year = year + 1;
			month = 3;
			day = 31;
		}
		String date = year + regex + month + regex +  day;
		if(today != null){
			if(date.compareTo(today) > 0){
				date = null;
			}
		}
		return date;
	}
	
	public static String getLastSeasonDate(String dateStr, String regex, String codeFirstDate){
		String[] split = dateStr.split(regex);
		int year = Integer.parseInt(split[0]);
		int month =Integer.parseInt(split[1]);
		int day = Integer.parseInt(split[2]);
		if(month == 3){
			year = year - 1;
			month = 12;
			day = 31;
		}else if(month == 6){
			month = 3;
			day = 31;
		}else if(month == 9){
			month = 6;
			day = 30;
		}else if(month == 12){
			month = 9;
			day = 30;
		}
		String date = year + regex + month + regex +  day;
		if(codeFirstDate != null){
			if(date.compareTo(codeFirstDate) < 0){
				date = null;
			}
		}
		return date;
	}
	
	public static String changeDateToNumberDate(Date date, String regex){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + regex + "MM" + regex + "dd");
		String dateStr = sdf.format(date);
		dateStr = dateStr.replaceAll(regex + "0", regex);
		return dateStr;
	}
	
	public static String changeDateStr(String dateStr, String sourcePattern, String targetPattern){
		SimpleDateFormat sdf = new SimpleDateFormat(sourcePattern);
		SimpleDateFormat sdf2 = new SimpleDateFormat(targetPattern);
		Date date = null;
		String s = null;
		try {
			date = sdf.parse(dateStr);
			s = sdf2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static List<String> getListByTwoList(List<String> list1, List<String> list2) {
		List<String> result = new ArrayList<String>();
		for(String s : list1){
			if(list2.contains(s)){
				result.add(s);
			}
		}
		return result;
	}
	
	public static List<String> getSortStringList(List<String> list, boolean isUp) {
		List<String> result = new ArrayList<String>();
		result.add(list.get(0));
		for(int i = 1; i < list.size(); i++){
			String s = list.get(i);
			int insertIndex = 0;
			for(int j = 0; j < result.size(); j++){
				String compareStr = result.get(j);
				if(isUp){
					if(s.compareTo(compareStr) > 0){
						insertIndex = j + 1;
					}
				}else{
					if(s.compareTo(compareStr) < 0){
						insertIndex = j + 1;
					}
				}
			}
			result.add(insertIndex, s);
		}
		return result;
	}
	
	public static String listToString(List<String> list, String regex){
		StringBuilder sb = new StringBuilder();
		for(String s : list){
			sb.append(s + regex);
		}
		return sb.substring(0, sb.length()-1);
	}
	
	public static String getTime(){
		return AnalyseUtil.parseDate(new Date(), "yyyy-MM-dd hh:mm:ss SSS");
	}
	
	
	public static HashMap<String, Object> addNameMap(HashMap<String, Object> map, HashMap<String, BasicInfoDomain> basicMap){
		HashMap<String, Object> result = new HashMap<String, Object>();
		for(String code : map.keySet()){
			String name = basicMap.get(code).getName();
			Object obj = map.get(code);
			result.put(code + name, obj);
		}
		return result;
	}

	public static void displayMap(HashMap<String, String> map, boolean isNoCYB) {
		for(String key : map.keySet()){
			String value = map.get(key);
			if(isNoCYB){
				if(key.startsWith("300")){
					continue;
				}
			}
			System.out.println(key + "\t" + BasicAnalyse.stockBasicMap.get(key).getName() + "\t" + value + "\n");
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
		double result;
		try {
			result = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			// TODO 自动生成的 catch 块
			result = 0;
		}
		return result;
	}

	public static double getRate(double lastNum, double currentNum) {
		double balance = currentNum - lastNum;
		double rate = (balance/lastNum) * 100;
		return rate;
	}
	
	public static double getListAverage(List<Double> list) {
		double sum = 0;
		double num = 0;
		for(Double dNum : list){
			sum = sum + dNum;
			num++;
		}
		return sum/num;
	}


	
}
