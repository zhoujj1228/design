package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Test;

import Util.AnalyseUtil;
import Util.Constant;
import Util.FileUtil;
import Util.NumCountUtil;
import Util.Comparator.NumDateComparator;
import analyseBasicInfo.BasicAnalyse;
import analyseBusiness.BusinessAnalyse;
import analyseConcept.ConceptAnalyse;
import analyseHistory.HistoryAnalyse;
import analysePrice.PriceAnalyse;
import domain.HistoryDomain;
import domain.HolderConnectDomain;
import domain.HolderDomain;
import domain.HolderHistoryDomain;

public class HolderAnalyse {
	public static CountDownLatch completeCount;
	public static Hashtable<String, HashMap<String, HolderHistoryDomain>> holderMap = new Hashtable<String, HashMap<String, HolderHistoryDomain>>();
	public static HashMap<String, HashMap<String, List<HolderDomain>>> holderDateStockMap = new HashMap<String, HashMap<String, List<HolderDomain>>>();
	public static Hashtable<String, HashMap<String,HolderHistoryDomain>> jjHolderMap = new Hashtable<String, HashMap<String, HolderHistoryDomain>>();
	public static HashMap<String, HashMap<String,List<HolderDomain>>> jjDateCodeMap = new HashMap<String, HashMap<String, List<HolderDomain>>>();
	public static HashMap<String, HashMap<String, Double>> gdNumMap = new HashMap<String, HashMap<String, Double>>();
	private static ExecutorService ex1 = Executors.newFixedThreadPool(20);
	private static ExecutorService ex2 = Executors.newFixedThreadPool(20);
	public static ThreadPoolExecutor ex3 = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	public static ThreadPoolExecutor ex4 = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	public static ThreadPoolExecutor ex5 = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	public static void main(String[] args) {
		initHolderMap(Constant.sdltHoldersDataFile);
		BasicAnalyse.initStockBasicMap();
		//initHolderDateStockMap(Constant.sdltHoldersDataFile);
		//initJjHolderMap(Constant.jjHoldersDataFile);
		//initJjDateCodeMap(jjHolderMap);
		//HistoryAnalyse.initHistoryMap("2017-06-30", "2017-11-01");
		//PriceAnalyse.initTodayPriceMap();
		ConceptAnalyse.initStockConcept3Map();
		BusinessAnalyse.initBusinessIncomeMap();
		BusinessAnalyse.initProfitMap();
		initGdNumMap();
		
		
		//analyseGDNumLowerAndLTGDHigherAndIPHigh();
		analyzeGDNumAvgInRate();
	}
	
	public static void analyzeGDNumAvgInRate(){
		String date = "2017-09-30";
		double startRate = -100;
		double endRate = -20;
		HashMap<String, Double> averageGDNumMap = getAverageGDNumMap();
		List<String> result = new ArrayList<String>();
		for(String code : BasicAnalyse.stockBasicMap.keySet()){
			HashMap<String, Double> hashMap = gdNumMap.get(code);
			if(hashMap == null || !hashMap.containsKey(date)){
				continue;
			}
			double todayGDNum = hashMap.get(date);
			double averageGDNum = averageGDNumMap.get(code);
			double balance = todayGDNum - averageGDNum;
			double rate = (balance/averageGDNum) * 100;
			if(rate >= startRate && rate <= endRate){
				result.add(code);
				System.out.println("analyzeGDNumAvgInRate:" + code + "\t" + BasicAnalyse.getName(code) + "\t" + rate + "\t" + ConceptAnalyse.stockConcept3Map.get(code));
			}
		}
		List<String> twoYearIncomeInRateList = BusinessAnalyse.getTwoYearIncomeInRateList(result, 0, 10000, "2017-3", "2016-3");
		List<String> twoYearProfitInRateList = BusinessAnalyse.getTwoYearProfitInRateList(twoYearIncomeInRateList, 0, 10000, "2017-3", "2016-3");
		List<String> twoSeasonIncomeInRateList = BusinessAnalyse.getTwoSeasonIncomeInRateList(twoYearProfitInRateList, 0, 10000, "2017-3", "2017-2");
		List<String> twoSeasonProfitInRateList = BusinessAnalyse.getTwoSeasonProfitInRateList(twoSeasonIncomeInRateList, 0, 10000, "2017-3", "2017-2");
		ConceptAnalyse.displayNameAndCocept(twoSeasonProfitInRateList);
		BasicAnalyse.getRemoveNewCompanyList(twoSeasonProfitInRateList);
	}
	
	public static void lastCall(){
		/*System.out.println(jjHolderMap.get("600887").get("2017/9/30").getJjHolders().size());
		System.out.println(jjHolderMap.get("600887").get("2017/6/30").getJjHolders().size());*/
		//analyseTwoDateJJHolderNewStockCount();
		//analyseTwoDateJJHolderNewStockPriceCount();
		//analyseGDNumLowerAndLTGDHigherAndIPHigh();
		//analyseGDNumLowerAndLTGDHigher();
		//analyseTwoSeasonJJIndecreateLessAndFlowLow();
		//getHolderAllStock("林玥佳");
		//List<String> ltgdHigtRateList = getLTGDHighRateList("合计","2017/6/30", 20, 50);
		//List<String> ltgdHigtRateList2 = getLTGDHighRateList("合计","2017/3/31", 10, 20);
		//List<String> ltgdRateHigh = AnalyseUtil.getListByTwoList(ltgdHigtRateList, ltgdHigtRateList2);
		//getHolderAllStock("潘英俊");
		//getHolderAllStock("黄泽圣");
		//System.out.println(getHolderFullName("哈尔滨工业"));
		//System.out.println(getHolderFullName("哈尔滨工大"));
		//getTwotimeRateMap("合计", null, "2017/3/31", "2017/6/30", 10, 100);
		//HashMap<String,List<HolderConnectDomain>> connectorMap = getConnectHolderMap();
		//System.out.println(jjHolderMap.get("601318").get("2007/3/31").getJjHolders().size());
		//HashMap<String,List<String>> connectorMap = getConnectHolderMapByStockNumWithThread(2);
		//HashMap<String,List<String>> connector23Map = getConnectHolderMapByStockNumAndHolderNum(2, 3);
		/*List<String> codes = new ArrayList<String>();
		codes.add("300154");
		codes.add("601009");
		getTwoHolderStock("张筠","朱晓净",codes);*/

		//getConnectHolderMapByConnNumAndPeopleNum(2,3,false,true);
		//getTwoSeasonJJIncreateStock();
		/*List<String> names = new ArrayList<String>();
		names.add("易方达行业领先");
		getJjHolderAllStock(names, "2017/6/30");*/
		//analyseTwoSeasonJJIndecreateLessAndFlowLow();

		/*double holderStockBestProfitRate = HolderAnalyse.getHolderStockLowestProfitRate("赵嘉馨", "600433", "2003/12/31", "2017/3/31", HistoryAnalyse.historyMap);
		System.out.println(holderStockBestProfitRate);*/
		//analyseHolderProfit("2016/3/31","2017/9/30");
		//analyseGDNumLow();
		/*List<String> twoDateLTGDInRateList = getTwoDateLTGDInRateList("合计", "2017/6/30", "2017/9/30", 20, 50);
		List<String> removeCYBStockList = BasicAnalyse.getRemoveCYBStockList(twoDateLTGDInRateList);
		HashMap<String, List<String>> conceptListMap = ConceptAnalyse.getConceptListMap(removeCYBStockList);
		System.out.println(conceptListMap);*/
		//analyseGDNumLowerAndLTGDHigher();
		//getLTGDChangeInRateList("2017/6/30", "2017/9/30", 15, 20, true);
		/*List<String> gdNumBalanceInRateList = getGDNumBalanceInRateList(gdNumMap, "2017-06-30", "2017-09-30", -300, -20);
		List<String> removeNewCompanyList = BasicAnalyse.getRemoveNewCompanyList(gdNumBalanceInRateList);
		ConceptAnalyse.displayNameAndCocept(removeNewCompanyList);*/
		//analyseGDNumLowerAndLTGDHigher2();

	}

	public static HashMap<String, HashMap<String, List<HolderDomain>>> initJjDateCodeMap(Hashtable<String, HashMap<String, HolderHistoryDomain>> jjHolderMap) {

		if(jjDateCodeMap.size() != 0){
			return jjDateCodeMap;
		}
		for(String code : jjHolderMap.keySet()){
			HashMap<String, HolderHistoryDomain> dateJjHolderMap = jjHolderMap.get(code);
			for(String date : dateJjHolderMap.keySet()){
				HolderHistoryDomain hhd = dateJjHolderMap.get(date);
				List<HolderDomain> jjHolders = hhd.getJjHolders();
				for(HolderDomain hd : jjHolders){
					String name = hd.getName();
					HashMap<String, List<HolderDomain>> dateCodeMap = jjDateCodeMap.get(name);
					if(dateCodeMap == null){
						dateCodeMap = new HashMap<String, List<HolderDomain>>();
					}
					List<HolderDomain> list = dateCodeMap.get(date);
					if(list == null){
						list = new ArrayList<HolderDomain>();
					}
					list.add(hd);
					dateCodeMap.put(date, list);
					jjDateCodeMap.put(name, dateCodeMap);
				}
			}
		}
		return jjDateCodeMap;
	
	}

	public static Hashtable<String, HashMap<String, HolderHistoryDomain>> initJjHolderMap(File jjHoldersDataFile) {

		if(jjHolderMap.size() != 0){
			return jjHolderMap;
		}
		List<File> files = FileUtil.getAllFileByFile(jjHoldersDataFile);
		completeCount = new CountDownLatch(files.size());
		for(File file : files){
			JjHolderInitThread hit = new JjHolderInitThread(file);
			ex5.execute(hit);
		}
		while(true){
			if(completeCount.getCount() == 0){
				ex5.shutdown();
				break;
			}
		}
		return jjHolderMap;
	
	}
	
	private static HashMap<String, HashMap<String, Double>> initGdNumMap() {
		HashMap<String, HashMap<String, Double>> result = new HashMap<String, HashMap<String, Double>>();
		List<File> files = FileUtil.getAllFileByFile(new File(Constant.gdNumFileDir));
		for(File file : files) {
			String fileName =  file.getName();
			String code = fileName.substring(0, fileName.lastIndexOf("."));
			List<String> strList = FileUtil.readByFileToList(file, "GBK");
			for(String s : strList) {
				String[] split = s.split("\t");
				String date = split[0];
				double num = AnalyseUtil.getNumOfStr(split[1]);
				HashMap<String, Double> hashMap = result.get(code);
				if(hashMap == null) {
					hashMap = new HashMap<String, Double>();
				}
				hashMap.put(date, num);
				result.put(code, hashMap);
			}
		}
		gdNumMap = result;
		return gdNumMap;
	}
	
	

	public static void analyseTwoDateJJHolderNewStockCount() {
		String lastDate = "2017/3/31";
		String todayDate = "2017/6/30";
		NumCountUtil.init();
		for(String name : jjDateCodeMap.keySet()){
			HashMap<String, List<HolderDomain>> hashMap = jjDateCodeMap.get(name);
			List<String> lastList = new ArrayList<String>();
			List<String> todayList = new ArrayList<String>();
			if(!hashMap.containsKey(lastDate) || !hashMap.containsKey(todayDate)){
				continue;
			}
			List<HolderDomain> list1 = hashMap.get(lastDate);
			for(HolderDomain hd : list1){
				lastList.add(hd.getCode());
			}
			List<HolderDomain> list2 = hashMap.get(todayDate);
			for(HolderDomain hd : list2){
				if(jjHolderMap.get(hd.getCode()).get(lastDate) == null){
					continue;
				}
				todayList.add(hd.getCode());
			}
			todayList.removeAll(lastList);
			for(String code : todayList){
				NumCountUtil.increase(code, 1);
			}
		}
		List<String> sortList = NumCountUtil.getSortList();
		for(String s : sortList){
			String code = s.split("_")[0];
			System.out.println("analyseTwoDateJJHolderNewStockCount" + s + "\t" + BasicAnalyse.getName(code) + "\t" + ConceptAnalyse.stockConcept3Map.get(code));
		}
	}
	
	public static void analyseTwoDateJJHolderNewStockPriceCount() {
		String lastDate = "2017/6/30";
		String todayDate = "2017/9/30";
		NumCountUtil.init();
		for(String name : jjDateCodeMap.keySet()){
			HashMap<String, List<HolderDomain>> hashMap = jjDateCodeMap.get(name);
			if(!hashMap.containsKey(lastDate) || !hashMap.containsKey(todayDate)){
				continue;
			}
			List<HolderDomain> list1 = hashMap.get(lastDate);
			for(HolderDomain hd : list1){
				NumCountUtil.decrease(hd.getCode(), hd.getNumberOfJjAmount());
			}
			List<HolderDomain> list2 = hashMap.get(todayDate);
			for(HolderDomain hd : list2){
				if(jjHolderMap.get(hd.getCode()).get(lastDate) == null){
					continue;
				}
				NumCountUtil.increase(hd.getCode(), hd.getNumberOfJjAmount());
			}
		}
		List<String> sortList = NumCountUtil.getSortList();
		List<String> result = new ArrayList<String>();
		for(String s : sortList){
			if(s.indexOf(".") > -1){
				s = s.substring(0, s.indexOf("."));
			}
			String code = s.split("_")[0];
			String priceCount = s.split("_")[1];
			if(Double.parseDouble(priceCount) > 100000000){
				result.add(code);
				System.out.println("analyseTwoDateJJHolderNewStockPriceCount:" + code + "\t" + priceCount + "\t" + BasicAnalyse.getName(code) + "\t" + ConceptAnalyse.stockConcept3Map.get(code));
			}
		}
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(HistoryAnalyse.historyMap, "2017-09-30", "2017-10-31");
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(orderDateMap, result);
		HashMap<String, Double> highestFlowInRate = PriceAnalyse.getHighestFlowInRate(codeHisMap, -2000, 200);
		HashMap<String, List<HistoryDomain>> orderDateMap2 = HistoryAnalyse.getOrderDateMap(HistoryAnalyse.historyMap, "2017-06-30", "2017-09-30");
		HashMap<String, List<HistoryDomain>> codeHisMap2 = HistoryAnalyse.getCodeHisMap(orderDateMap2, result);
		HashMap<String, Double> highestFlowInRate2 = PriceAnalyse.getHighestFlowInRate(codeHisMap2, -2000, 200);
		
		
		List<String> dates = new ArrayList<String>();
		dates.add("2017-10-27");
		HashMap<String, List<Double>> moreDayFlowRateMap= PriceAnalyse.getMoreDayFlowRateMap(highestFlowInRate.keySet(), HistoryAnalyse.historyMap, "2017-09-29", dates );
		for(String code : highestFlowInRate.keySet()){
			System.out.println("analyseTwoDateJJHolderNewStockPriceCount:" + code + "\t" + BasicAnalyse.getName(code) + "\t" + highestFlowInRate.get(code) + "\t" + highestFlowInRate2.get(code) + "\t" + moreDayFlowRateMap.get(code) + "\t" + ConceptAnalyse.stockConcept3Map.get(code));
		}
	}
	
	public static void analyseJJNumCount() {
		//HashMap<String, Integer> result = new HashMap<String, Integer>();
		for(String code : jjHolderMap.keySet()){
			HashMap<String, HolderHistoryDomain> hashMap = jjHolderMap.get(code);
			for(String date : hashMap.keySet()){
				if(date.equals("2017/9/30")){
					System.out.println(code + "\t" + BasicAnalyse.getName(code) + "\t" + hashMap.get(date).getJjHolders().size() );
				}
			}
		}
	}
	
	public static void analyseGDNumLow() {
		List<String> gdNumBalanceInRateList = getGDNumBalanceInRateList(gdNumMap, "2017-06-30", "2017-09-30", -300, -20);
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyMap, gdNumBalanceInRateList);
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(codeHisMap);
		HashMap<String, String> dieTingLastTimeMap = PriceAnalyse.getDieTingLastTimeMap(orderDateMap, 5, -100, -20);
		HashMap<String, String> endDateMap = AnalyseUtil.getCodeDateMap(dieTingLastTimeMap.keySet(), "2017-10-13");
		HashMap<String, List<HistoryDomain>> orderDateMap2 = HistoryAnalyse.getCodeHisMap(orderDateMap, dieTingLastTimeMap.keySet());
		HashMap<String, List<HistoryDomain>> afterDieTingHisMap = HistoryAnalyse.getHisMapByStartAndEndDateMap(orderDateMap2, dieTingLastTimeMap, endDateMap);
		HashMap<String, Double> highestFlowInRate = PriceAnalyse.getHighestFlowInRate(afterDieTingHisMap, -100, 30);
		ConceptAnalyse.displayNameAndCocept(highestFlowInRate.keySet());
	}
	
	public static void analyseGDNumLowerAndLTGDHigher() {
		List<String> twoDateLTGDInRateList = getTwoDateLTGDInRateList("合计", "2017/6/30", "2017/9/30", 10, 500);
		List<String> gdNumBalanceInRateList = getGDNumBalanceInRateList(gdNumMap, "2017-06-30", "2017-09-30", -300, -15);
		twoDateLTGDInRateList.retainAll(gdNumBalanceInRateList);
		List<String> twoYearIncomeInRateList2 = BusinessAnalyse.getTwoYearIncomeInRateList(twoDateLTGDInRateList, 0, 10000, "2016-4", "2015-4");
		List<String> twoYearIncomeInRateList = BusinessAnalyse.getTwoYearIncomeInRateList(twoYearIncomeInRateList2, 0, 10000, "2017-2", "2016-2");
		List<String> twoYearProfitInRateList = BusinessAnalyse.getTwoYearProfitInRateList(twoYearIncomeInRateList, 0, 10000, "2017-2", "2016-2");
		List<String> twoSeasonIncomeInRateList = BusinessAnalyse.getTwoSeasonIncomeInRateList(twoYearProfitInRateList, 0, 10000, "2017-2", "2017-1");
		List<String> twoSeasonProfitInRateList = BusinessAnalyse.getTwoSeasonProfitInRateList(twoSeasonIncomeInRateList, 0, 10000, "2017-2", "2017-1");
		
		ConceptAnalyse.displayNameAndCocept(twoSeasonProfitInRateList);
	}
	
	public static void analyseGDNumLowerAndLTGDHigher2() {
		List<String> twoDateLTGDInRateList = getLTGDChangeInRateList("2017/6/30", "2017/9/30", 3, 20, true);
		List<String> gdNumBalanceInRateList = getGDNumBalanceInRateList(gdNumMap, "2017-06-30", "2017-09-30", -15, -10);
		twoDateLTGDInRateList.retainAll(gdNumBalanceInRateList);
		ConceptAnalyse.displayNameAndCocept(twoDateLTGDInRateList);
	}
	
	private static void analyseGDNumLowerAndLTGDHigherAndIPHigh() {
		List<String> twoDateLTGDInRateList = getLTGDChangeInRateList("2017/6/30", "2017/9/30", 1, 20, true);
		List<String> gdNumBalanceInRateList = getGDNumBalanceInRateList(gdNumMap, "2017-06-30", "2017-09-30", -50, 0);
		twoDateLTGDInRateList.retainAll(gdNumBalanceInRateList);
		List<String> twoYearIncomeInRateList2 = BusinessAnalyse.getTwoYearIncomeInRateList(twoDateLTGDInRateList, 0, 10000, "2016-4", "2015-4");
		List<String> twoYearIncomeInRateList = BusinessAnalyse.getTwoYearIncomeInRateList(twoYearIncomeInRateList2, 0, 10000, "2017-3", "2016-3");
		List<String> twoYearProfitInRateList = BusinessAnalyse.getTwoYearProfitInRateList(twoYearIncomeInRateList, 0, 10000, "2017-3", "2016-3");
		List<String> twoSeasonIncomeInRateList = BusinessAnalyse.getTwoSeasonIncomeInRateList(twoYearProfitInRateList, 0, 10000, "2017-3", "2017-2");
		List<String> twoSeasonProfitInRateList = BusinessAnalyse.getTwoSeasonProfitInRateList(twoSeasonIncomeInRateList, 0, 10000, "2017-3", "2017-2");
		
		ConceptAnalyse.displayNameAndCocept(twoSeasonProfitInRateList);
	}
	
	public static HashMap<String, Double> getAverageGDNumMap(){
		HashMap<String, Double> result = new HashMap<String, Double>();
		for(String code : gdNumMap.keySet()){
			HashMap<String, Double> hashMap = gdNumMap.get(code);
			double sum = 0;
			double count = 0;
			for(Double gdNum : hashMap.values()){
				sum = sum + gdNum;
				count++;
			}
			double average = sum/count;
			result.put(code, average);
		}
		return result;
	}
	
	public static List<String> getLTGDChangeInRateList(String date1, String date2, double startRate, double endRate, boolean isRmJJ){
		List<String> result = new ArrayList<String>();
		for(String code : holderMap.keySet()){
			if(!holderMap.get(code).containsKey(date1) || !holderMap.get(code).containsKey(date2)){
				continue;
			}
			if(BasicAnalyse.stockBasicMap.get(code).getTimeToMarket().indexOf("2017") > -1){
				continue;
			}
			List<HolderDomain> list1 = holderMap.get(code).get(date1).getLtgdList();
			List<HolderDomain> list2 = holderMap.get(code).get(date2).getLtgdList();
			double sumRate = 0;
			for(HolderDomain hd2 : list2){
				if(isRmJJ && hd2.getName().length() > 3){
					continue;
				}
				double rate = hd2.getNumberOfRate();
				if(rate > 10){
					continue;
				}
				for(HolderDomain hd1 : list1){
					if(hd2.getName().equals(hd1.getName())){
						rate = rate - hd1.getNumberOfRate();
						continue;
					}
				}
				sumRate = sumRate + rate;
			}
			if(sumRate >= startRate && sumRate <= endRate){
				System.out.println("getLTGDChangeInRateList:" + code + "\t" + sumRate);
				result.add(code);
			}
		}
		
		return result;
	
	}
	
	public static void analyseHolderProfit(String startDate, String endDate){
		//String firstDate = "2015/9/30";
		for(String code : holderMap.keySet()){
			HashMap<String, HolderHistoryDomain> dateHolderMap = holderMap.get(code);
			List<String> dateList = new ArrayList<String>();
			NumDateComparator ndc = new NumDateComparator();
			dateList.addAll(dateHolderMap.keySet());
			if(dateList.size() == 0){
				continue;
			}
			dateList.sort(ndc);
			for(String date : dateList){
				if(!(ndc.compare(date, startDate) >= 0) || !(ndc.compare(date, endDate) <= 0)){
					System.out.println("analyseHolderProfit !ndc.compare(date, startDate) >= 0 || !ndc.compare(date, endDate) <= 0"+date);
					continue;
				}
				List<HolderDomain> ltgdList = dateHolderMap.get(date).getLtgdList();
				for(HolderDomain hd : ltgdList){
					/*String firstHoldDate = */getFirstHoldSeasonDate(date, dateHolderMap, hd.getName());
				}
				
			}
			
			
			System.out.println("end");
		}
	}
	
	private static String getFirstHoldSeasonDate(String date, HashMap<String, HolderHistoryDomain> dateHolderMap,
			String name) {
		String lastSeasonDate = AnalyseUtil.getLastSeasonDate(date, "/", null);
		HolderHistoryDomain hhd = dateHolderMap.get(lastSeasonDate);
		if(hhd == null){
			return date;
		}
		List<HolderDomain> ltgdList = hhd.getLtgdList();
		for(HolderDomain hd : ltgdList){
			if(hd.getName().equals(name)){
				String firstHoldSeasonDate = getFirstHoldSeasonDate(lastSeasonDate, dateHolderMap, name);
				return firstHoldSeasonDate;
			}
		}
		return date;
	}

	@Test
	public static double getHolderStockHighestProfitRate(String holder, String code, String startSeason, String endSeason, HashMap<String, List<HistoryDomain>> historyMap){
		String marketTime;
		Date marketDate;
		try {
			marketTime = BasicAnalyse.stockBasicMap.get(code).getTimeToMarket();
			marketDate = AnalyseUtil.parseDate(marketTime, "yyyyMMdd");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		marketTime = AnalyseUtil.changeDateToNumberDate(marketDate, "-");
		String lastSeasonDate = AnalyseUtil.getLastSeasonDate(startSeason, "/", marketTime);
		String lastSeasonDate2 = AnalyseUtil.getLastSeasonDate(endSeason, "/", marketTime);
		if(lastSeasonDate == null){
			System.out.println("getHolderStockBestProfitRate : lastSeasonDate == null");
			lastSeasonDate = marketTime;
		}
		if(lastSeasonDate2 == null){
			System.out.println("getHolderStockBestProfitRate : lastSeasonDate2 == null");
			lastSeasonDate2 = marketTime;
		}
		String slDate = AnalyseUtil.changNumberDate(lastSeasonDate, "/", "-");
		String elDate = AnalyseUtil.changNumberDate(lastSeasonDate2, "/", "-");
		String sDate = AnalyseUtil.changNumberDate(startSeason, "/", "-");
		String eDate = AnalyseUtil.changNumberDate(endSeason, "/", "-");
		double sLowestPrice = PriceAnalyse.getLowestPrice(historyMap.get(code), slDate, sDate);
		double eHighestPrice = PriceAnalyse.getHighestPrice(historyMap.get(code), elDate, eDate);
		double balance = eHighestPrice - sLowestPrice;
		double rate = (balance/sLowestPrice) * 100;
		return rate;
	}
	
	public static double getHolderStockLowestProfitRate(String holder, String code, String startSeason, String endSeason, HashMap<String, List<HistoryDomain>> historyMap){
		String marketTime;
		Date marketDate;
		try {
			marketTime = BasicAnalyse.stockBasicMap.get(code).getTimeToMarket();
			marketDate = AnalyseUtil.parseDate(marketTime, "yyyyMMdd");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		marketTime = AnalyseUtil.changeDateToNumberDate(marketDate, "-");
		String lastSeasonDate = AnalyseUtil.getLastSeasonDate(startSeason, "/", marketTime);
		String lastSeasonDate2 = AnalyseUtil.getLastSeasonDate(endSeason, "/", marketTime);
		if(lastSeasonDate == null){
			System.out.println("getHolderStockBestProfitRate : lastSeasonDate == null");
			lastSeasonDate = marketTime;
		}
		if(lastSeasonDate2 == null){
			System.out.println("getHolderStockBestProfitRate : lastSeasonDate2 == null");
			lastSeasonDate2 = marketTime;
		}
		String slDate = AnalyseUtil.changNumberDate(lastSeasonDate, "/", "-");
		String elDate = AnalyseUtil.changNumberDate(lastSeasonDate2, "/", "-");
		String sDate = AnalyseUtil.changNumberDate(startSeason, "/", "-");
		String eDate = AnalyseUtil.changNumberDate(endSeason, "/", "-");
		double sHighestPrice = PriceAnalyse.getHighestPrice(historyMap.get(code), slDate, sDate);
		double eLowestPrice = PriceAnalyse.getLowestPrice(historyMap.get(code), elDate, eDate);
		double balance = eLowestPrice - sHighestPrice;
		double rate = (balance/sHighestPrice) * 100;
		return rate;
	}
	
	public static void analyseTwoSeasonJJIndecreateLessAndFlowLow2(){

		List<String> inRateJjHolderNumList = getInNumJjHolderBalanceList(jjHolderMap.keySet(), 100, 5000, "2017/3/31", "2017/6/30");
		List<String> inRateJjHolderBalanceList = getInRateJjHolderBalanceList(inRateJjHolderNumList, -80, -70, "2017/6/30", "2017/9/30");
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(HistoryAnalyse.historyMap, "2017-09-30", "2017-10-30");
		HashMap<String, Double> highestFlowPriceMap = PriceAnalyse.getHighestFlowPriceMap(orderDateMap);
		List<String> smallCompanyList = BasicAnalyse.getSmallCompanyList(inRateJjHolderBalanceList, 20, 5000);
		System.out.println(inRateJjHolderNumList.size() + "\t" + inRateJjHolderBalanceList.size() + "\t" + smallCompanyList.size());
		double sumRate = 0;
		double count = 0;
		for(String code : smallCompanyList) {
			List<HistoryDomain> list = orderDateMap.get(code);
			if(list == null || list.size() == 0) {
				continue;
			}
			double hlRate = highestFlowPriceMap.get(code);
			double numberClose1 = list.get(0).getNumberClose();
			double numberClose2 = list.get(list.size() - 1).getNumberClose();
			double rate = ((numberClose2 - numberClose1)/numberClose1) * 100;
			sumRate = sumRate + rate;
			double profitRate = 0;
			double profitRate2 = 0;
			double profitRate3 = 0;
			if(hlRate < 200){
				for(HistoryDomain hd : HistoryAnalyse.historyMap.get(code)){
					if(hd.getDate().equals("2016-11-15")){
						double numberClose3 = hd.getNumberClose();
						profitRate = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2016-11-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate2 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2016-12-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate3 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
				}
				//System.out.println(list.get(0).getDate() + list.get(list.size() - 1).getDate());
				System.out.println(code + "\t" + BasicAnalyse.getName(code) + "\t" + list.get(0).getDate() + list.get(list.size()-1).getDate() + "\t" + rate + "\t" + profitRate + "\t" + profitRate2 + "\t" + profitRate3  + "\t" + hlRate);
			}
			count++;
			
		}
		System.out.println("sumRate:" + sumRate + "\tcount" + count + "\t" + sumRate/count);
	
	}
	
	public static void analyseTwoSeasonJJIndecreateLessAndFlowLow() {
		List<String> inRateJjHolderNumList = getInNumJjHolderBalanceList(jjHolderMap.keySet(), 150, 5000, "2017/3/31", "2017/6/30");
		List<String> inRateJjHolderBalanceList = getInRateJjHolderBalanceList(inRateJjHolderNumList, -79, -70, "2017/6/30", "2017/9/30");
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(HistoryAnalyse.historyMap, "2017-09-30", "2017-10-13");
		HashMap<String, Double> highestFlowPriceMap = PriceAnalyse.getHighestFlowPriceMap(orderDateMap);
		List<String> smallCompanyList = BasicAnalyse.getSmallCompanyList(inRateJjHolderBalanceList, 20, 5000);
		System.out.println(inRateJjHolderNumList.size() + "\t" + inRateJjHolderBalanceList.size() + "\t" + smallCompanyList.size());
		double sumRate = 0;
		double count = 0;
		for(String code : smallCompanyList) {
			List<HistoryDomain> list = orderDateMap.get(code);
			if(list == null || list.size() == 0) {
				continue;
			}
			double hlRate = highestFlowPriceMap.get(code);
			double numberClose1 = list.get(0).getNumberClose();
			double numberClose2 = list.get(list.size() - 1).getNumberClose();
			double rate = ((numberClose2 - numberClose1)/numberClose1) * 100;
			sumRate = sumRate + rate;
			double profitRate = 0;
			double profitRate2 = 0;
			double profitRate3 = 0;
			if(hlRate < 200){
				for(HistoryDomain hd : HistoryAnalyse.historyMap.get(code)){
					if(hd.getDate().equals("2016-11-15")){
						double numberClose3 = hd.getNumberClose();
						profitRate = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2016-11-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate2 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2016-12-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate3 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
				}
				//System.out.println(list.get(0).getDate() + list.get(list.size() - 1).getDate());
				System.out.println(code + "\t" + BasicAnalyse.getName(code) + "\t" + list.get(0).getDate() + list.get(list.size()-1).getDate() + "\t" + rate + "\t" + profitRate + "\t" + profitRate2 + "\t" + profitRate3  + "\t" + hlRate);
			}
			count++;
			
		}
		System.out.println("sumRate:" + sumRate + "\tcount" + count + "\t" + sumRate/count);
	}
	
	public static void analyseGDNum() {
		HashMap<String, HashMap<String, Double>> gdNumMap = initGdNumMap();
		List<String> list = getGDNumBalanceInRateList(gdNumMap, "2017-06-30", "2017-09-30", -20, -10);
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyMap, list);
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(codeHisMap);
		HashMap<String, String> dieTingLastTimeMap = PriceAnalyse.getDieTingLastTimeMap(orderDateMap, 5, -100, -20);
		HashMap<String, String> endDateMap = AnalyseUtil.getCodeDateMap(dieTingLastTimeMap.keySet(), "2017-10-13");
		HashMap<String, List<HistoryDomain>> afterDieTingHisMap = HistoryAnalyse.getHisMapByStartAndEndDateMap(orderDateMap, dieTingLastTimeMap, endDateMap);
		HashMap<String, Double> highestFlowInRate = PriceAnalyse.getHighestFlowInRate(afterDieTingHisMap, -100, 30);
		HashMap<String, List<String>> conceptListMap = ConceptAnalyse.getConceptListMap(highestFlowInRate.keySet());
		System.out.println(conceptListMap);
	}
	
	public static List<String> getGDNumBalanceInRateList(HashMap<String, HashMap<String, Double>> gdNumMap, String lastDate, String nowDate, double startRate, double endRate) {
		List<String> result = new ArrayList<String>();
		for(String code : gdNumMap.keySet()) {
			HashMap<String, Double> dateNumMap = gdNumMap.get(code);
			if(!dateNumMap.containsKey(nowDate) || !dateNumMap.containsKey(lastDate)) {
				continue;
			}
			if(BasicAnalyse.stockBasicMap.get(code).getTimeToMarket().indexOf("2017") > -1) {
				continue;
			}
			if(code.startsWith("300")) {
				continue;
			}
			Double lastNum = dateNumMap.get(lastDate);
			Double nowNum = dateNumMap.get(nowDate);
			double balance = nowNum - lastNum;
			double rate = (balance/lastNum) * 100;
			if(rate >= startRate && rate <= endRate) {
				result.add(code);
				System.out.println("getGDNumBalanceInRate:" + code + "\t" + BasicAnalyse.stockBasicMap.get(code).getName() + "\t" + rate + "\t" + ConceptAnalyse.stockConcept3Map.get(code));
			}
		}
		return result;
	}


	

	public static List<String> getJjHolderAllStock(List<String> names, String date) {
		List<String> result = new ArrayList<String>();
		for(String key : jjDateCodeMap.keySet()){
			for(String name : names){
				if(key.indexOf(name) > -1){
					HashMap<String, List<HolderDomain>> hashMap = jjDateCodeMap.get(key);
					for(List<HolderDomain> list : hashMap.values()){
						for(HolderDomain hd : list){
							if(date == null || !date.equals(hd.getDate())){
								continue;
							}
							result.add(hd.getCode());
							System.out.println(key + "\t" + hd.getDate() + "\t" + hd.getCode() + "\t" + BasicAnalyse.stockBasicMap.get(hd.getCode()).getName() + "\t" + hd.getNumberOfJjAmount());
						}
					}
					break;
				}
			}
		}
		return result;
	}

	public static void analyseTwoSeasonJJIncreateStock() {
		//List<String> inRateJjHolderNumList = getInRateJjHolderNumList(jjHolderMap.keySet(), 100, 5000, "2016/6/30");
		List<String> inRateJjHolderNumList = getInNumJjHolderBalanceList(jjHolderMap.keySet(), 100, 5000, "2017/3/31", "2017/6/30");
		List<String> inRateJjHolderBalanceList = getInRateJjHolderBalanceList(inRateJjHolderNumList, -100000, 10000, "2017/6/30", "2017/9/30");
		//List<String> inRateJjHolderBalanceList = getInNumJjHolderBalanceList(inRateJjHolderNumList, -50, 500, "2016/6/30", "2016/9/30");
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(HistoryAnalyse.historyMap, "2017-06-30", "2017-10-30");
		HashMap<String, Double> highestFlowPriceMap = PriceAnalyse.getHighestFlowPriceMap(orderDateMap);
		List<String> smallCompanyList = BasicAnalyse.getSmallCompanyList(inRateJjHolderBalanceList, 20, 5000);
		System.out.println(inRateJjHolderNumList.size() + "\t" + inRateJjHolderBalanceList.size() + "\t" + smallCompanyList.size());
		double sumRate = 0;
		double count = 0;
		for(String code : smallCompanyList) {
			List<HistoryDomain> list = orderDateMap.get(code);
			if(list == null || list.size() == 0) {
				continue;
			}
			double hlRate = highestFlowPriceMap.get(code);
			double numberClose1 = list.get(0).getNumberClose();
			double numberClose2 = list.get(list.size() - 1).getNumberClose();
			double rate = ((numberClose2 - numberClose1)/numberClose1) * 100;
			sumRate = sumRate + rate;
			double profitRate = 0;
			double profitRate2 = 0;
			double profitRate3 = 0;
			if(hlRate - rate < 100){
				for(HistoryDomain hd : HistoryAnalyse.historyMap.get(code)){
					if(hd.getDate().equals("2017-11-15")){
						double numberClose3 = hd.getNumberClose();
						profitRate = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2017-11-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate2 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2017-12-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate3 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
				}
				//System.out.println(list.get(0).getDate() + list.get(list.size() - 1).getDate());
				System.out.println(code + "\t" + list.get(0).getDate() + list.get(list.size()-1).getDate() + "\t" + rate + "\t" + profitRate + "\t" + profitRate2 + "\t" + profitRate3  + "\t" + hlRate);
			}
			count++;
			
		}
		System.out.println("sumRate:" + sumRate + "\tcount" + count + "\t" + sumRate/count);
	}
	
	public static void analyseTwoSeasonJJIncreateStock16() {
		//List<String> inRateJjHolderNumList = getInRateJjHolderNumList(jjHolderMap.keySet(), 100, 5000, "2016/6/30");
		List<String> inRateJjHolderNumList = getInNumJjHolderBalanceList(jjHolderMap.keySet(), 100, 5000, "2016/3/31", "2016/6/30");
		List<String> inRateJjHolderBalanceList = getInRateJjHolderBalanceList(inRateJjHolderNumList, -70, 10000, "2016/6/30", "2016/9/30");
		//List<String> inRateJjHolderBalanceList = getInNumJjHolderBalanceList(inRateJjHolderNumList, -50, 500, "2016/6/30", "2016/9/30");
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(HistoryAnalyse.historyMap, "2016-06-30", "2016-10-30");
		HashMap<String, Double> highestFlowPriceMap = PriceAnalyse.getHighestFlowPriceMap(orderDateMap);
		List<String> smallCompanyList = BasicAnalyse.getSmallCompanyList(inRateJjHolderBalanceList, 20, 5000);
		System.out.println(inRateJjHolderNumList.size() + "\t" + inRateJjHolderBalanceList.size() + "\t" + smallCompanyList.size());
		double sumRate = 0;
		double count = 0;
		for(String code : smallCompanyList) {
			List<HistoryDomain> list = orderDateMap.get(code);
			if(list == null || list.size() == 0) {
				continue;
			}
			double hlRate = highestFlowPriceMap.get(code);
			double numberClose1 = list.get(0).getNumberClose();
			double numberClose2 = list.get(list.size() - 1).getNumberClose();
			double rate = ((numberClose2 - numberClose1)/numberClose1) * 100;
			sumRate = sumRate + rate;
			double profitRate = 0;
			double profitRate2 = 0;
			double profitRate3 = 0;
			if(hlRate < 200){
				for(HistoryDomain hd : HistoryAnalyse.historyMap.get(code)){
					if(hd.getDate().equals("2016-11-15")){
						double numberClose3 = hd.getNumberClose();
						profitRate = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2016-11-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate2 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
					if(hd.getDate().equals("2016-12-30")){
						double numberClose3 = hd.getNumberClose();
						profitRate3 = ((numberClose3 - numberClose2)/numberClose2) * 100;
					}
				}
				//System.out.println(list.get(0).getDate() + list.get(list.size() - 1).getDate());
				System.out.println(code + "\t" + list.get(0).getDate() + list.get(list.size()-1).getDate() + "\t" + rate + "\t" + profitRate + "\t" + profitRate2 + "\t" + profitRate3  + "\t" + hlRate);
			}
			count++;
			
		}
		System.out.println("sumRate:" + sumRate + "\tcount" + count + "\t" + sumRate/count);
	}

	public static List<String> getInNumJjHolderBalanceList(Collection<String> c, int startNum, int endNum, String startTime, String endTime) {
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			HashMap<String, HolderHistoryDomain> hashMap = jjHolderMap.get(code);
			HolderHistoryDomain hhd1 = hashMap.get(startTime);
			HolderHistoryDomain hhd2 = hashMap.get(endTime);
			if(hhd1 == null || hhd2 == null) {
				//System.out.println("getInRateJjHolderBalanceList:hhd == null" + code);
				continue;
			}
			List<HolderDomain> jjHolders1 = hhd1.getJjHolders();
			List<HolderDomain> jjHolders2 = hhd2.getJjHolders();
			double size1 = jjHolders1.size();
			double size2 = jjHolders2.size();
			double num = size2 - size1;
			if(num >= startNum && num <= endNum) {
				result.add(code);
				System.out.println("getInNumJjHolderBalanceList:" + code + "\t" + size1 + "\t" + size2 + "\t" +num);
			}
		}
		return result;
	}

	public static List<String> getInRateJjHolderNumList(Collection<String> c, int startNum, int endNum, String time) {
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			HashMap<String, HolderHistoryDomain> hashMap = jjHolderMap.get(code);
			HolderHistoryDomain hhd = hashMap.get(time);
			if(hhd == null) {
				continue;
			}
			List<HolderDomain> jjHolders = hhd.getJjHolders();
			int size = jjHolders.size();
			if(size >= startNum && size <= endNum) {
				result.add(code);
			}
		}
		return result;
	}
	
	public static List<String> getInRateJjHolderBalanceList(Collection<String> c, int startRate, int endRate, String startTime, String endTime) {
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			HashMap<String, HolderHistoryDomain> hashMap = jjHolderMap.get(code);
			HolderHistoryDomain hhd1 = hashMap.get(startTime);
			HolderHistoryDomain hhd2 = hashMap.get(endTime);
			if(hhd1 == null || hhd2 == null) {
				//System.out.println("getInRateJjHolderBalanceList:hhd == null" + code + hhd1 + hhd2);
				continue;
			}
			List<HolderDomain> jjHolders1 = hhd1.getJjHolders();
			List<HolderDomain> jjHolders2 = hhd2.getJjHolders();
			double size1 = jjHolders1.size();
			double size2 = jjHolders2.size();
			double balance = size2 - size1;
			double rate = (balance/size1) * 100;
			if(rate >= startRate && rate <= endRate) {
				result.add(code);
				System.out.println("getInRateJjHolderBalance:" + code + "\t" + size1 + "\t" + size2 + "\t" +rate);
			}
		}
		return result;
	}
	
	public static HashMap<String, List<String>> getConnectHolderMapByConnNumAndPeopleNum(int connectNum, int peopleNum, boolean isRemove, boolean isNextSeasonNotIN){
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		List<File> allFile = FileUtil.getAllFileByFile(new File(Constant.sdltHoldersConnectFilePath));
		for(File file : allFile){
			HolderConnectWithCPNumThread hccpt = new HolderConnectWithCPNumThread(file, result, connectNum, peopleNum, isRemove, isNextSeasonNotIN);
			ex4.execute(hccpt);
		}
		ex4.shutdown();
		return result;
	}
	

	public static HashMap<String, List<String>> getConnectHolderMapByStockNumWithThread(int connectNum) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		Set<String> names = holderDateStockMap.keySet();
		int sum = 0;
		for(String name : names){
			System.out.println(++sum);
			HolderConnectThread hct = new HolderConnectThread(name, names, connectNum);
			ex3.execute(hct);
		}
		ex3.shutdown();
		return result;
	}
	
	/*private static HashMap<String, List<String>> getConnectHolderMapByStockNumAndHolderNum(int stockNum, int holderNum) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		result = getNameStockMap(stockNum, holderNum, result, new ArrayList<String>());
		return result;
	}
	private static HashMap<String, List<String>> getNameStockMap(int stockNum, int holderNum,
			HashMap<String, List<String>> result, ArrayList<String> nameList) {
		if(holderNum != 0){
		}
		return null;
	}
	public static List<String> getTwoHolderStock(String name, String otherName, List<String> codes){
		List<String> result = new ArrayList<String>();
		for(String code : codes){
			HashMap<String, HolderHistoryDomain> hashMap = holderMap.get(code);
			Set<String> dates = hashMap.keySet();
			for(String date : dates){
				HolderHistoryDomain hhd = hashMap.get(date);
				List<HolderDomain> ltgdList = hhd.getLtgdList();
				boolean isNameIn = false;
				boolean isOtherNameIn = false;
				for(HolderDomain hd : ltgdList){
					if(hd.getName().equals(name)){
						isNameIn = true;
					}
					if(hd.getName().equals(otherName)){
						isOtherNameIn = true;
					}
				}
				if(isNameIn && isOtherNameIn){
					result.add(code + "_" + date);
					System.out.println("getTwoHolderStock:\t" + code + "\t" + date);
				}
			}
		}
		return result;
	}
	
	private static HashMap<String, List<String>> getConnectHolderMapByStockNum(int connectNum) {

		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		Set<String> names = holderDateStockMap.keySet();
		for(String name : names){
			for(String otherName : names){
				if(otherName.equals(name) || otherName.equals("合计") || name.equals("合计")){
					continue;
				}
				List<String> codes = getConnectCode(name, otherName);
				if(codes.size() >= connectNum){
					result.put(name+"$"+otherName, codes);
					//String s = name + "$$" + otherName + "$$" + codes.toString()+"\n\r";
					//FileUtil.writeFileAppendWithEncode(Constant.sdltHoldersConnectFile, s, "GBK");
					//System.out.println("result.size():" + result.size() + "\t" + name+"_"+otherName + "\t" + codes.toString());
				}
			}
		}
		return result;
	}*/
	
	
	public static List<String> getConnectCode(String name, String otherName) {
		List<String> holderStockList1 = getHolderStockList(name);
		List<String> holderStockList2 = getHolderStockList(otherName);
		List<String> holderStockDateList = getTwoListContain(holderStockList1, holderStockList2);
		List<String> holderStockList = getCodeList(holderStockDateList);
		return holderStockList;
	}
	
	public static List<String> getHolderStockList(String name){
		List<String> result = new ArrayList<String>();
		HashMap<String, List<HolderDomain>> hashMap = holderDateStockMap.get(name);
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
		List<String> result = new ArrayList<String>();
		for (String s : list) {
			String code = s.substring(0, s.indexOf("_"));
			if (!result.contains(code)) {
				result.add(code);
			}
		}
		return result;
	}
	
	public static HashMap<String,List<HolderConnectDomain>> getConnectHolderMap(){
		HashMap<String, List<HolderConnectDomain>> result = new HashMap<String, List<HolderConnectDomain>>();
		Set<String> names = holderDateStockMap.keySet();
		for(String name : names){
			//System.out.println(result.size());
			HashMap<String, List<HolderDomain>> dateHolderMap = holderDateStockMap.get(name);
			Iterator<Entry<String, List<HolderDomain>>> itor = dateHolderMap.entrySet().iterator();
			while(itor.hasNext()){
				Entry<String, List<HolderDomain>> next = itor.next();
				String date = next.getKey();
				List<HolderDomain> holders = next.getValue();
				for(HolderDomain hd : holders){
					String code = hd.getCode();
					HolderHistoryDomain hhd = holderMap.get(code).get(date);
					List<HolderDomain> ltgdList = hhd.getLtgdList();
					for(HolderDomain hd1 : ltgdList){
						String targetName = hd1.getName().trim();
						if(targetName.compareTo(name) < 0){
							HolderConnectDomain hcd = new HolderConnectDomain();
							hcd.setCode(code);
							hcd.setDate(date);
							hcd.setSourceName(name);
							hcd.setTargetName(targetName);
							String s = name + "_" + targetName;
							List<HolderConnectDomain> list;
							if(result.containsKey(s)){
								list = result.get(s);
							}else{
								list = new ArrayList<HolderConnectDomain>();
							}
							list.add(hcd);
							result.put(s, list);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 查询股东的合作次数，注意：同一个股票合作的不算
	 * @param connectNum
	 * @return
	 */
	public static HashMap<String,List<String>> getConnectHolderStrByNumMap(int connectNum){
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		Set<String> names = holderDateStockMap.keySet();
		for(String name : names){
			//System.out.println(result.size());
			HashMap<String, List<String>> tempResult = new HashMap<String, List<String>>();
			HashMap<String, List<HolderDomain>> dateHolderMap = holderDateStockMap.get(name);
			Iterator<Entry<String, List<HolderDomain>>> itor = dateHolderMap.entrySet().iterator();
			while(itor.hasNext()){
				Entry<String, List<HolderDomain>> next = itor.next();
				String date = next.getKey();
				List<HolderDomain> holders = next.getValue();
				for(HolderDomain hd : holders){
					String code = hd.getCode();
					HolderHistoryDomain hhd = holderMap.get(code).get(date);
					List<HolderDomain> ltgdList = hhd.getLtgdList();
					for (HolderDomain hd1 : ltgdList) {
						String targetName = hd1.getName().trim();
						if(targetName.equals(name) || targetName.equals("合计")){
							continue;
						}
						String s = name + "$$" + targetName;
						
						String codeAndDate = code + "_" + date;
						List<String> list;
						if (tempResult.containsKey(s)) {
							list = tempResult.get(s);
						} else {
							list = new ArrayList<String>();
						}
						list.add(codeAndDate);
						tempResult.put(s, list);
					}
				}
			}
			tempResult = getTempResultBySize(tempResult, connectNum);
			result.putAll(tempResult);
			System.out.println("result.size():" + result.size());
		}
		return result;
	}
	
	private static HashMap<String, List<String>> getTempResultBySize(HashMap<String, List<String>> tempResult,
			int connectNum) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		Set<String> connectors = tempResult.keySet();
		for(String connector : connectors){
			List<String> list = tempResult.get(connector);
			int size = list.size();
			if(size >= connectNum){
				result.put(connector, tempResult.get(connector));
				System.out.println(connector + "-" + list.toString());
			}
		}
		return result;
	}
	public static List<String> getHolderAllStock(String name){
		List<String> result = new ArrayList<String>();
		for(List<HolderDomain> list : holderDateStockMap.get(name).values()){
			for(HolderDomain hd : list){
				//System.out.println(hd.getCode() + "\t" + hd.getDate() + "\t" + hd.getRate());
				result.add(hd.getCode() + "\t" + hd.getDate() + "\t" + hd.getRate());
			}
		}
		result = sortCodeDateList(result);
		for(String s : result){
			System.out.println(s);
		}
		
		return result;
	}
	
	public static List<String> sortCodeDateList(List<String> sourceList){
		List<String> afterSortResult = new ArrayList<String>();
		afterSortResult.add(sourceList.get(0));
		for(int i = 1; i < sourceList.size(); i++){
			String s = sourceList.get(i);
			String[] split = s.split("\t");
			String code = split[0];
			String date = AnalyseUtil.changNumberDate(split[1], "/");
			int insertIndex = 0;
			for(int j = 0; j < afterSortResult.size(); j++){
				String temps = afterSortResult.get(j);
				String[] tempsplit = temps.split("\t");
				String tempcode = tempsplit[0];
				String tempdate = AnalyseUtil.changNumberDate(tempsplit[1], "/");
				if(code.compareTo(tempcode) > 0){
					insertIndex = j + 1;
				}else if(code.compareTo(tempcode) == 0){
					if(date.compareTo(tempdate) > 0){
						insertIndex = j + 1;
					}
				}
			}
			afterSortResult.add(insertIndex, s);
		}
		return afterSortResult;
	}
	
	public static HashMap<String, Double> getTwotimeRateMap(String name, String targetCode, String startDate, String endDate, double startRate, double endRate) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = new HashSet<String>(holderMap.keySet());
		if(targetCode != null){
			codes.clear();
			codes.add(targetCode);
		}
		for(String code : codes){
			HashMap<String, HolderHistoryDomain> dateHolderMap = holderMap.get(code);
			if(dateHolderMap.containsKey(startDate) && dateHolderMap.containsKey(endDate)){
				List<HolderDomain> startHis = dateHolderMap.get(startDate).getLtgdList();
				List<HolderDomain> endHis = dateHolderMap.get(endDate).getLtgdList();
				double startDayRate = 0;
				double endDayRate = 0;
				for(HolderDomain hd : startHis){
					if(hd.getName().equals(name)){
						startDayRate = hd.getNumberOfRate();
					}
				}
				for(HolderDomain hd : endHis){
					if(hd.getName().equals(name)){
						endDayRate = hd.getNumberOfRate();
					}
				}
				double balance = endDayRate - startDayRate;
				if(balance < endRate && balance > startRate){
					result.put(code, balance);
					System.out.println("getTwotimeRateMap:" + code + "\t" + name + "\t" + balance);
				}
			}
		}
		return result;
	}
	
	public static List<String> getHolderFullNameByStrs(String nameStr, String regex) {
		String[] split = nameStr.split(regex);
		List<String> result = new ArrayList<String>();
		Set<String> names = holderDateStockMap.keySet();
		nameItor:for(String s : names){
			for(int i = 0; i< split.length; i++){
				String name = split[i];
				if(s.indexOf(name) == -1){
					continue nameItor;
				}
			}
			result.add(s);
			System.out.println("getHolderFullName:\t" + s);
			
		}
		return result;
	}
	
	public static List<String> getHolderFullName(String name) {
		List<String> result = new ArrayList<String>();
		Set<String> names = holderDateStockMap.keySet();
		for(String s : names){
			if(s.indexOf(name) > -1){
				result.add(s);
				System.out.println("getHolderFullName:\t" + s);
			}
		}
		return result;
	}
	
	public static List<String> getLTGDHighRateList(String name, String date, double startRate, double endRate) {
		List<String> result = new ArrayList<String>();
		List<HolderDomain> list = holderDateStockMap.get(name).get(date);
		for(HolderDomain hd : list){
			double rate = hd.getNumberOfRate();
			if(rate > startRate && rate < endRate){
				result.add(hd.getCode());
				//System.out.println("getLTGDHighRateList-------------------\t" + hd.getCode() + "\t" + hd.getDate() + "\t" + hd.getRate());
				/*for(HolderDomain temp : holderMap.get(hd.getCode()).get(date).getLtgdList()){
					System.out.println("getLTGDHighRateList:\t" + temp.getName() + "\t" + date + "\t" + temp.getRate());
				}*/
			}
		}
		return result;
	}
	
	public static List<String> getTwoDateLTGDInRateList(String name, String date1, String date2, double startRate, double endRate) {
		List<String> result = new ArrayList<String>();
		List<HolderDomain> list1 = holderDateStockMap.get(name).get(date1);
		List<HolderDomain> list2 = holderDateStockMap.get(name).get(date2);
		for(HolderDomain hd1 : list1){
			double rate1 = hd1.getNumberOfRate();
			for(HolderDomain hd2 : list2){
				if(hd2.getCode().equals(hd1.getCode())){
					double rate2 = hd2.getNumberOfRate();
					double rate = rate2 - rate1;
					if(rate  > startRate && rate < endRate){
						result.add(hd1.getCode());
						System.out.println("getTwoDateLTGDInRateList-------\t" + hd1.getCode() + "\t" + BasicAnalyse.getName(hd1.getCode())  + "\t" + rate1 + "\t" + rate2 + "\t" + ConceptAnalyse.stockConcept3Map.get(hd1.getCode()));
						/*for(HolderDomain temp : holderMap.get(hd.getCode()).get(date).getLtgdList()){
							System.out.println("getLTGDHighRateList:\t" + temp.getName() + "\t" + date + "\t" + temp.getRate());
						}*/
					}
				}
			}
			
		}
		return result;
	}
	
	public static Hashtable<String, HashMap<String, HolderHistoryDomain>> initHolderMap(File sdltHoldersFile) {
		if(holderMap.size() != 0){
			return holderMap;
		}
		List<File> files = FileUtil.getAllFileByFile(sdltHoldersFile);
		completeCount = new CountDownLatch(files.size());
		for(File file : files){
			HolderInitThread hit = new HolderInitThread(file);
			ex1.execute(hit);
		}
		while(true){
			if(completeCount.getCount() == 0){
				ex1.shutdown();
				break;
			}
		}
		return holderMap;
	}
	public static HashMap<String, HashMap<String, List<HolderDomain>>> initHolderDateStockMap(File sdltHoldersFile) {
		if(holderDateStockMap.size() != 0){
			return holderDateStockMap;
		}
		List<File> files = FileUtil.getAllFileByFile(sdltHoldersFile);
		completeCount = new CountDownLatch(files.size());
		for(File file : files){
			HolderDateStockInitThread hdsit = new HolderDateStockInitThread(file);
			ex2.execute(hdsit);
		}
		while(true){
			if(completeCount.getCount() == 0){
				ex2.shutdown();
				break;
			}
		}
		return holderDateStockMap;
	}
	
	
	
	

}
