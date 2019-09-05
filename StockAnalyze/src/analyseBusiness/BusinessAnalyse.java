package analyseBusiness;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Util.AnalyseUtil;
import Util.Constant;
import Util.FileUtil;
import analyseBasicInfo.BasicAnalyse;
import analyseConcept.ConceptAnalyse;
import analyseHistory.HistoryAnalyse;
import analysePrice.PriceAnalyse;
import domain.BusinessManageDomain;
import domain.BusinessManageRangeDomain;
import domain.HistoryDomain;

public class BusinessAnalyse {
	public static HashMap<String, HashMap<String, Double>> businessIncomeMap = new HashMap<String, HashMap<String, Double>>();
	public static HashMap<String, HashMap<String, Double>> profitMap = new HashMap<String, HashMap<String, Double>>();
	public static HashMap<String, BusinessManageDomain> businessManageMap = new HashMap<String, BusinessManageDomain>();
	public static void main(String[] args) {
		//System.out.println("2016-08777777777".matches("^[0-9]{4}-[0-9]{2}.*$"));
		BasicAnalyse.initStockBasicMap();
		initBusinessManageMap();
		/*initBusinessIncomeMap();
		initProfitMap();
		//double twoYearProfitRate = getTwoSeasonProfitRate("600068", "2017-1", "2016-1");
		//System.out.println(twoYearProfitRate);
		PriceAnalyse.initTodayPriceMap();
		HistoryAnalyse.initHistoryMap("2017-03-01", "2017-10-13");
		ConceptAnalyse.initStockConcept3Map();
		ConceptAnalyse.initConcept3Map();
		//getHighICAndHighPFAndDTLowFlowConceptMap(30, 60);*/
		call();
	}
	

	private static void call() {
		HashMap<String, String> infoMap = getInfoFromBsnsMgMap("食品安全");
		AnalyseUtil.displayMap(infoMap, true);
	}
	
	private static void analyse1() {
		//List<String> list = ConceptAnalyse.concept3Map.get("OLED");
		List<String> twoYearIncomeInRateList2 = getTwoYearIncomeInRateList(BasicAnalyse.stockBasicMap.keySet(), 0, 10000, "2016-4", "2015-4");
		List<String> twoYearIncomeInRateList = getTwoYearIncomeInRateList(twoYearIncomeInRateList2, 0, 10000, "2017-2", "2016-2");
		List<String> twoYearProfitInRateList = getTwoYearProfitInRateList(twoYearIncomeInRateList, 0, 10000, "2017-2", "2016-2");
		List<String> twoSeasonIncomeInRateList = getTwoSeasonIncomeInRateList(twoYearProfitInRateList, 0, 10000, "2017-2", "2017-1");
		List<String> twoSeasonProfitInRateList = getTwoSeasonProfitInRateList(twoSeasonIncomeInRateList, 0, 10000, "2017-2", "2017-1");
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyMap, twoSeasonProfitInRateList);
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(codeHisMap);
		HashMap<String, String> dieTingTimeMap = PriceAnalyse.getDieTingLastTimeMap(orderDateMap, 5, -100, -20);
		HashMap<String, String> endDateMap = AnalyseUtil.getCodeDateMap(orderDateMap.keySet(), "2017-10-13");
		HashMap<String, List<HistoryDomain>> orderDateDTMap = HistoryAnalyse.getCodeHisMap(orderDateMap, dieTingTimeMap.keySet());
		HashMap<String, List<HistoryDomain>> afterDieTingHisMap = HistoryAnalyse.getHisMapByStartAndEndDateMap(orderDateDTMap, dieTingTimeMap, endDateMap);
		HashMap<String, Double> highestFlowInRate = PriceAnalyse.getHighestFlowInRate(afterDieTingHisMap, -2000, 20);
		HashMap<String, List<String>> conceptListMap = ConceptAnalyse.getConceptListMap(highestFlowInRate.keySet());

		
		System.out.println(conceptListMap);
	}
	
	public static HashMap<String, String> getInfoFromBsnsMgMap(String keyWord){
		HashMap<String, String> result = new HashMap<String, String>();
		for(String code : businessManageMap.keySet()){
			BusinessManageDomain bmd = businessManageMap.get(code);
			if(bmd.getJyps().indexOf(keyWord) > -1){
				result.put(code, "经营评述" + "\t" + bmd.getJyps());
			}
			if(bmd.getZyfw().indexOf(keyWord) > -1){
				result.put(code, "主营服务" + "\t" + bmd.getZyfw());
			}
			
			/*HashMap<String, HashMap<String, HashMap<String, BusinessManageRangeDomain>>> bmRangeMap = bmd.getBmRangeMap();
			for(HashMap<String, HashMap<String, BusinessManageRangeDomain>> typeMap :bmRangeMap.values()){
				for(HashMap<String, BusinessManageRangeDomain> pdMap : typeMap.values()){
					for(BusinessManageRangeDomain bmrd : pdMap.values()){
						if(bmrd.get)
					}
				}
			}*/
		}
		return result;
	}

	public static void getHighYSIcPfLowFlowStockConcept() {
		List<String> twoYearIncomeInRateList1 = getTwoYearIncomeInRateList(BasicAnalyse.stockBasicMap.keySet(), 20, 10000, "2016-4", "2015-4");
		List<String> twoYearIncomeInRateList = getTwoYearIncomeInRateList(twoYearIncomeInRateList1, 20, 10000, "2017-2", "2016-2");
		List<String> twoYearProfitInRateList = getTwoYearProfitInRateList(twoYearIncomeInRateList, 20, 10000, "2017-2", "2016-2");
		List<String> twoSeasonIncomeInRateList = getTwoSeasonIncomeInRateList(twoYearProfitInRateList, 20, 10000, "2017-2", "2017-1");
		List<String> twoSeasonProfitInRateList = getTwoSeasonProfitInRateList(twoSeasonIncomeInRateList, 20, 10000, "2017-2", "2017-1");
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyMap, twoSeasonProfitInRateList);
		HashMap<String, Double> oldDayAllPriceMap = PriceAnalyse.getSomeDayAllPriceMap(codeHisMap, "2016-05-10");
		HashMap<String, Double> todayDayAllPriceMap = PriceAnalyse.getSomeDayAllPriceMap(codeHisMap, "2017-10-13");
		HashMap<String, Double> twoDayBalanceRateMap = PriceAnalyse.getTwoDayBalanceRateMap(oldDayAllPriceMap, todayDayAllPriceMap, -500, 50);
		HashMap<String, List<String>> conceptListMap = ConceptAnalyse.getConceptListMap(twoDayBalanceRateMap.keySet());
		ConceptAnalyse.getConceptTimeCount(conceptListMap, 4, 100);
		for(String code : conceptListMap.keySet()){
			if(code.startsWith("300")){
				continue;
			}
			String name = BasicAnalyse.stockBasicMap.get(code).getName();
			System.out.println(code + "\t" + name + "\t" + conceptListMap.get(code));
		}
		/*
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyMap, twoYearProfitInRateList);
		HashMap<String, Double> oldDayAllPriceMap = PriceAnalyse.getSomeDayAllPriceMap(codeHisMap, "2016-05-16");
		HashMap<String, Double> todayDayAllPriceMap = PriceAnalyse.getSomeDayAllPriceMap(codeHisMap, "2017-06-16");
		HashMap<String, Double> twoDayBalanceRateMap = PriceAnalyse.getTwoDayBalanceRateMap(oldDayAllPriceMap, todayDayAllPriceMap, -100, 10);
		HashMap<String, Double> todayDay2AllPriceMap = PriceAnalyse.getSomeDayAllPriceMap(codeHisMap, "2017-10-13");
		System.out.println("------------");
		HashMap<String, Double> twoDayBalanceRateMap2 = PriceAnalyse.getTwoDayBalanceRateMap(todayDayAllPriceMap, todayDay2AllPriceMap, 0, 1000);
		HashMap<String, Double> twoDayBalanceRateMap3 = PriceAnalyse.getTwoDayBalanceRateMap(todayDayAllPriceMap, todayDay2AllPriceMap, -1000, 0);
		HashMap<String, List<String>> conceptListMap = ConceptAnalyse.getConceptListMap(twoDayBalanceRateMap3.keySet());
		System.out.println(twoDayBalanceRateMap2.size() + "\t" + twoDayBalanceRateMap3.size());
		
		
		
		
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(codeHisMap);
		HashMap<String, String> dieTingTimeMap = PriceAnalyse.getDieTingFirstTimeMap(orderDateMap, 5, -100, -20);
		//HashMap<String, String> endDateMap = AnalyseUtil.getAfterTranDateMap(dieTingTimeMap, "yyyy-MM-dd", 60);
		HashMap<String, String> endDateMap = AnalyseUtil.getCodeDateMap(dieTingTimeMap.keySet(), "2017-06-31");
		HashMap<String, List<HistoryDomain>> afterDieTingHisMap = HistoryAnalyse.getHisMapByStartAndEndDateMap(orderDateMap, dieTingTimeMap, endDateMap);
		HashMap<String, Double> highestFlowInRate = PriceAnalyse.getHighestFlowInRate(afterDieTingHisMap, -20, 20);
		HashMap<String, List<String>> conceptListMap = ConceptAnalyse.getConceptListMap(highestFlowInRate.keySet());
		System.out.println("---------------------");
		HashMap<String, List<HistoryDomain>> orderDate2Map = HistoryAnalyse.getCodeHisMap(orderDateMap, endDateMap.keySet());
		HashMap<String, String> endDate2Map = AnalyseUtil.getCodeDateMap(orderDate2Map.keySet(), "2017-9-13");
		//HashMap<String, String> endDate2Map = AnalyseUtil.getAfterTranDateMap(endDateMap, "yyyy-MM-dd", 60);
		HashMap<String, List<HistoryDomain>> profitHisMap = HistoryAnalyse.getHisMapByStartAndEndDateMap(orderDate2Map, endDateMap, endDate2Map);
		HashMap<String, Double> highestFlowInRate2 = PriceAnalyse.getHighestFlowInRate(profitHisMap, -500, 500);
		System.out.println(conceptListMap);*/
	}
	
	public static void getHighICAndHighPFAndDTLowFlowConceptMap(int afterDTDayNum, int holdDayNum) {
		List<String> twoYearIncomeInRateList = getTwoYearIncomeInRateList(BasicAnalyse.stockBasicMap.keySet(), 20, 10000, "2016-4", "2015-4");
		List<String> twoYearProfitInRateList = getTwoYearProfitInRateList(twoYearIncomeInRateList, 20, 10000, "2016-4", "2015-4");
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyMap, twoYearProfitInRateList);
		HashMap<String, List<HistoryDomain>> orderDateMap = HistoryAnalyse.getOrderDateMap(codeHisMap);
		HashMap<String, String> dieTingTimeMap = PriceAnalyse.getDieTingFirstTimeMap(orderDateMap, 5, -100, -20);
		//HashMap<String, String> endDateMap = AnalyseUtil.getCodeDateMap(dieTingTimeMap.keySet(), dtEndDate);
		HashMap<String, String> endDateMap = AnalyseUtil.getAfterTranDateMap(dieTingTimeMap, "yyyy-MM-dd", afterDTDayNum);
		
		HashMap<String, List<HistoryDomain>> orderDateDTMap = HistoryAnalyse.getCodeHisMap(orderDateMap, dieTingTimeMap.keySet());
		HashMap<String, List<HistoryDomain>> afterDieTingHisMap = HistoryAnalyse.getHisMapByStartAndEndDateMap(orderDateDTMap, dieTingTimeMap, endDateMap);
		HashMap<String, Double> highestFlowInRate = PriceAnalyse.getHighestFlowInRate(afterDieTingHisMap, -20, 20);
		HashMap<String, List<String>> conceptListMap = ConceptAnalyse.getConceptListMap(highestFlowInRate.keySet());

		System.out.println("---------------------");
		HashMap<String, List<HistoryDomain>> orderDate2Map = HistoryAnalyse.getCodeHisMap(orderDateMap, orderDateDTMap.keySet());
		//HashMap<String, String> endDate2Map = AnalyseUtil.getCodeDateMap(orderDate2Map.keySet(), holdEndDate);

		HashMap<String, String> endDate2Map = AnalyseUtil.getAfterTranDateMap(endDateMap, "yyyy-MM-dd", holdDayNum);
		HashMap<String, List<HistoryDomain>> profitHisMap = HistoryAnalyse.getHisMapByStartAndEndDateMap(orderDate2Map, endDateMap, endDate2Map);
		PriceAnalyse.getHighestFlowInRate(profitHisMap, -500, 500);
		
		System.out.println(conceptListMap);
	}

	public static List<String> getTwoYearIncomeAndProfitHighAndInConceptAndSCompanyList() {
		// System.out.println(businessIncomeMap);
		ConceptAnalyse.initConcept3Map();
		ConceptAnalyse.initStockConcept3Map();
		List<String> list = ConceptAnalyse.concept3Map.get("军工");
		/*
		 * List<String> list1 = concept3Map.get("一带一路"); list.retainAll(list1);
		 */
		List<String> twoYearIncomeInRateList = getTwoYearIncomeInRateList(list, 20, 10000, "2016-4", "2015-4");
		List<String> twoYearProfitInRateList = getTwoYearProfitInRateList(twoYearIncomeInRateList, 20, 10000, "2016-4", "2015-4");
		ConceptAnalyse.getConceptListMap(twoYearProfitInRateList);
		System.out.println(twoYearProfitInRateList);
		System.out.println(ConceptAnalyse.getConceptListMap(twoYearProfitInRateList));
		List<String> smallCompanyList = BasicAnalyse.getSmallCompanyList(twoYearProfitInRateList, 5, 1800);
		System.out.println(smallCompanyList);
		return smallCompanyList;
	}

	public static HashMap<String, BusinessManageDomain>  initBusinessManageMap(){
		List<File> allFileByFile = FileUtil.getAllFileByFile(new File(Constant.businessManagePath));
		ExecutorService es = Executors.newFixedThreadPool(20);
		final HashMap<String, BusinessManageDomain> result = new HashMap<String, BusinessManageDomain>();
		final CountDownLatch completeCount = new CountDownLatch(allFileByFile.size());
		for(final File file : allFileByFile){
			es.execute(new Runnable() {
				
				public void run() {
					List<String> list = FileUtil.readByFileToList(file, Constant.encode);
					String zyfw = list.get(0);
					String jyps = list.get(1);
					
					BusinessManageDomain bmd = new BusinessManageDomain();
					bmd.setJyps(jyps);
					bmd.setZyfw(zyfw);
					if(list.size() < 3){
						//System.out.println("initBusinessManageMap:经营数据缺少="+file.getName().substring(0,file.getName().lastIndexOf(".")));
						return;
					}
					String lastString = "";
					boolean isHYDataBegin = false;
					for(int i = 2; i < list.size(); i++){
						if(list.get(i).matches("^[0-9]{4}-[0-9]{2}.*$")){
							isHYDataBegin = true;
						}
						String[] datas = (lastString + list.get(i)).split("\t");
						if(datas.length < 10){
							if(!isHYDataBegin){
								bmd.setJyps(bmd.getJyps() + list.get(i));
								//System.out.println("initBusinessManageMap:bmd.getJyps()行业经营数据缺少=" + file.getName().substring(0,file.getName().lastIndexOf(".")) + "\t" + bmd.getJyps() + "\t" + isHYDataBegin);
								
							}else{
								lastString = lastString + list.get(i);
							}
							continue;
						}
						if(lastString.length() > 0){
							//System.out.println("initBusinessManageMap:行业经营数据缺少=" + file.getName().substring(0,file.getName().lastIndexOf(".")) + "\t" + lastString + list.get(i));
							lastString = "";
						}
						String date = datas[0];
						String type = datas[1];
						String pdName = datas[2];
						String income = datas[3];
						String incomeRate = datas[4];
						String cost = datas[5];
						String costRate = datas[6];
						String profit = datas[7];
						String profitRate = datas[8];
						String pdProfitRate = datas[9];
						BusinessManageRangeDomain bmrd = new BusinessManageRangeDomain(type, pdName, income, incomeRate, cost, costRate, profit, profitRate, pdProfitRate, date);
						HashMap<String, HashMap<String, HashMap<String, BusinessManageRangeDomain>>> bmRangeMap = bmd.getBmRangeMap();
						HashMap<String, HashMap<String, BusinessManageRangeDomain>> dateBMRDMap = bmRangeMap.get(date);
						if(dateBMRDMap == null){
							dateBMRDMap = new HashMap<String, HashMap<String, BusinessManageRangeDomain>>();
						}
						HashMap<String, BusinessManageRangeDomain> typeBMRDMap = dateBMRDMap.get(type);
						if(typeBMRDMap == null){
							typeBMRDMap = new HashMap<String, BusinessManageRangeDomain>();
						}
						typeBMRDMap.put(pdName, bmrd);
						dateBMRDMap.put(type, typeBMRDMap);
						bmRangeMap.put(date, dateBMRDMap);
					}
					result.put(file.getName().substring(0,file.getName().lastIndexOf(".")), bmd);
					completeCount.countDown();
				}
			});
		}
		while(completeCount.getCount() != 0){
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		es.shutdown();
		System.out.println(result.get("600710").getBmRangeMap().get("2016-06-30").get("产品"));
		businessManageMap = result;
		return businessManageMap;
	}
	
	public static HashMap<String, HashMap<String, Double>> initBusinessIncomeMap(){
		List<File> allFileByFile = FileUtil.getAllFileByFile(Constant.profitFile);
		for(File file : allFileByFile) {
			String fileName = file.getName();
			//System.out.println(fileName.lastIndexOf("."));
			String time = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("."));
			List<String> list = FileUtil.readByFileToList(file, "GBK");
			for(String s : list) {
				String[] split = s.split("\t");
				String code = split[0];
				String income = split[7];
				HashMap<String, Double> dateIncomeMap = businessIncomeMap.get(code);
				if(dateIncomeMap == null) {
					dateIncomeMap = new HashMap<String, Double>();
				}
				double incomeDouble;
				try {
					incomeDouble = Double.parseDouble(income);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					//System.out.println(e.getMessage());
					incomeDouble = 0;
				}
				dateIncomeMap.put(time, incomeDouble);
				businessIncomeMap.put(code, dateIncomeMap);
			}
		}
		return businessIncomeMap;
	}
	
	public static HashMap<String, HashMap<String, Double>> initProfitMap(){
		List<File> allFileByFile = FileUtil.getAllFileByFile(Constant.profitFile);
		for(File file : allFileByFile) {
			String fileName = file.getName();
			//System.out.println(fileName.lastIndexOf("."));
			String time = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("."));
			List<String> list = FileUtil.readByFileToList(file, "GBK");
			for(String s : list) {
				String[] split = s.split("\t");
				String code = split[0];
				String profit = split[5];
				HashMap<String, Double> dateIncomeMap = profitMap.get(code);
				if(dateIncomeMap == null) {
					dateIncomeMap = new HashMap<String, Double>();
				}
				double profitDouble;
				try {
					profitDouble = Double.parseDouble(profit);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					//System.out.println(e.getMessage());
					profitDouble = 0;
				}
				dateIncomeMap.put(time, profitDouble);
				profitMap.put(code, dateIncomeMap);
			}
		}
		return profitMap;
	}
	
	public static List<String> getYearsIncomeProfitInRateList(Collection<String> c, double startRate, double endRate, String... seasons){
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			boolean isNeed = true;
			for(String season : seasons){
				double income = 0;
				double profit = 0;
				HashMap<String, Double> incomeMap = businessIncomeMap.get(code);
				HashMap<String, Double> pfMap = profitMap.get(code);
				if(!incomeMap.containsKey(season) || pfMap.containsKey(season)) {
					//System.out.println("getTwoYearIncomeRate:缺少日期报告：" + code + season);
					continue;
				}
				income = incomeMap.get(season);
				profit = pfMap.get(season);
				double rate = (profit/income) * 100;
				if(rate < startRate || rate > endRate){
					isNeed = false;
					break;
				}
			}
			if(isNeed){
				result.add(code);
			}
			
		}
		return result;
	}
	
	public static List<String> getTwoYearIncomeInRateList(Collection<String> c, double startRate, double endRate, String currentSeason, String lastSeason){
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			double twoYearIncomeRate = getTwoYearIncomeRate(code, currentSeason, lastSeason);
			if(twoYearIncomeRate >= startRate && twoYearIncomeRate <= endRate) {
				result.add(code);
			}
		}
		return result;
	}
	
	public static List<String> getTwoSeasonIncomeInRateList(Collection<String> c, double startRate, double endRate, String currentSeason, String lastSeason){
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			double twoSeasonIncomeRate;
			try {
				twoSeasonIncomeRate = getTwoSeasonIncomeRate(code, currentSeason, lastSeason);
			} catch (Exception e) {
				//e.printStackTrace();
				continue;
			}
			if(twoSeasonIncomeRate >= startRate && twoSeasonIncomeRate <= endRate) {
				result.add(code);
			}
		}
		return result;
	}
	
	
	public static List<String> getTwoYearProfitInRateList(Collection<String> c, double startRate, double endRate, String currentSeason, String lastSeason){
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			double twoYearProfitRate = getTwoYearProfitRate(code, currentSeason, lastSeason);
			if(twoYearProfitRate >= startRate && twoYearProfitRate <= endRate) {
				result.add(code);
			}
		}
		return result;
	}

	
	public static List<String> getTwoSeasonProfitInRateList(Collection<String> c, double startRate, double endRate, String currentSeason, String lastSeason){
		List<String> result = new ArrayList<String>();
		for(String code : c) {
			double twoSeasonProfitRate = getTwoSeasonProfitRate(code, currentSeason, lastSeason);
			if(twoSeasonProfitRate >= startRate && twoSeasonProfitRate <= endRate) {
				result.add(code);
			}
		}
		return result;
	}
	public static double getTwoYearProfitRate(String code, String currentSeason, String lastSeason) {
		HashMap<String, Double> hashMap = profitMap.get(code);
		if(!hashMap.containsKey(currentSeason) || !hashMap.containsKey(lastSeason)) {
			//System.out.println("getTwoYearProfitRate:缺少日期报告：" + code);
			return -999;
		}
		double currentProfit = hashMap.get(currentSeason);
		double lastProfit = hashMap.get(lastSeason);
		double balance = currentProfit - lastProfit;
		double rate = (balance/lastProfit) * 100;
		return rate;
	}
	
	public static double getTwoYearProfitBalance(String code , String currentSeason, String lastSeason) {
		HashMap<String, Double> hashMap = profitMap.get(code);
		if(!hashMap.containsKey(currentSeason) || !hashMap.containsKey(lastSeason)) {
			//System.out.println("getTwoYearProfitBalance:缺少日期报告：" + code);
			return -999;
		}
		double currentProfit = hashMap.get(currentSeason);
		double lastProfit = hashMap.get(lastSeason);
		double balance = currentProfit - lastProfit;
		return balance;
	}
	
	public static double getTwoYearIncomeRate(String code , String currentSeason, String lastSeason) {
		HashMap<String, Double> hashMap = businessIncomeMap.get(code);
		if(hashMap == null || !hashMap.containsKey(currentSeason) || !hashMap.containsKey(lastSeason)) {
			//System.out.println("getTwoYearIncomeRate:缺少日期报告：" + code);
			return -999;
		}
		double currentIncome = hashMap.get(currentSeason);
		double lastIncome = hashMap.get(lastSeason);
		double balance = currentIncome - lastIncome;
		double rate = (balance/lastIncome) * 100;
		return rate;
	}
	public static double getTwoYearIncomeBalance(String code , String currentSeason, String lastSeason) {
		HashMap<String, Double> hashMap = businessIncomeMap.get(code);
		if(!hashMap.containsKey(currentSeason) || !hashMap.containsKey(lastSeason)) {
			//System.out.println("getTwoYearIncomeBalance:缺少日期报告：" + code);
			return -999;
		}
		double currentIncome = hashMap.get(currentSeason);
		double lastIncome = hashMap.get(lastSeason);
		double balance = currentIncome - lastIncome;
		return balance;
	}

	public static double getTwoSeasonProfitRate(String code , String currentSeason, String lastSeason) {
		double currentProfit = getSeasonProfit(code, currentSeason);
		double lastProfit = getSeasonProfit(code, lastSeason);
		double balance = currentProfit - lastProfit;
		double rate = (balance/lastProfit) * 100;
		return rate;
	}
	
	public static double getTwoSeasonIncomeRate(String code , String currentSeason, String lastSeason) throws Exception {
		double currentIncome = getSeasonIncome(code, currentSeason);
		double lastIncome = getSeasonIncome(code, lastSeason);
		double balance = currentIncome - lastIncome;
		double rate = (balance/lastIncome) * 100;
		return rate;
	}
	
	public static double getTwoSeasonProfitBalance(String code , String currentSeason, String lastSeason) {
		double currentProfit = getSeasonProfit(code, currentSeason);
		double lastProfit = getSeasonProfit(code, lastSeason);
		double balance = currentProfit - lastProfit;
		return balance;
	}
	
	public static double getTwoSeasonIncomeBalance(String code , String currentSeason, String lastSeason) throws Exception {
		double currentIncome = getSeasonIncome(code, currentSeason);
		double lastIncome = getSeasonIncome(code, lastSeason);
		double balance = currentIncome - lastIncome;
		return balance;
	}
	
	public static String getLastSeason(String currentSeason){
		String[] split = currentSeason.split("-");
		int seasonNum = Integer.parseInt(split[1]);
		if(seasonNum > 1){
			seasonNum--;
		}
		String lastSeason = split[0] + "-" + seasonNum;
		return lastSeason;
	}
	
	public static double getSeasonProfit(String code, String season){
		Double double1 = profitMap.get(code).get(season);
		String cLastSeason = getLastSeason(season);
		double double2 = 0;
		if(!cLastSeason.equals(season)){
			double2 = profitMap.get(code).get(cLastSeason);
		}
		double currentProfit = double1 - double2;
		return currentProfit;
		
	}
	
	public static double getSeasonIncome(String code, String season) throws Exception{
		Double double1 = businessIncomeMap.get(code).get(season);
		String cLastSeason = getLastSeason(season);
		double double2 = 0;
		if(!cLastSeason.equals(season)){
			if(!businessIncomeMap.get(code).containsKey(cLastSeason)){
				System.out.println("getSeasonIncome:缺少季度报告="+cLastSeason);
				throw new Exception("缺少季度报告="+cLastSeason);
			}
			double2 = businessIncomeMap.get(code).get(cLastSeason);
		}
		double currentIncome = double1 - double2;
		return currentIncome;
		
	}
}
