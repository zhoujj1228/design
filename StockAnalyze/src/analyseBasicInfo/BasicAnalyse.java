package analyseBasicInfo;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import domain.BasicInfoDomain;
import domain.HistoryDomain;
import Util.AnalyseUtil;
import Util.Constant;
import Util.FileUtil;
import analyseConcept.ConceptAnalyse;
import analyseHistory.HistoryAnalyse;
import analysePrice.PriceAnalyse;
import analyseTranAmount.TranAmountAnalyse;

public class BasicAnalyse {
	public static HashMap<String, BasicInfoDomain> stockBasicMap;
	public static HashMap<String, List<String>> stockIndustryMap;
	public static HashMap<String, List<String>> stockConcept1Map;
	public static HashMap<String, List<String>> stockConcept2Map;
	public static HashMap<String, List<String>> conceptStock2Map;

	public static void main(String[] args) {
		BasicAnalyse ba = new BasicAnalyse();
		initStockBasicMap();
		initConcept1Map();
		initConcept2Map();
		initConceptStock2Map();
		//PriceAnalyse.initTodayPriceMap();
		//System.out.println(conceptStock2Map.get("雄安新区").get(0));
		HistoryAnalyse.initHistoryOrderMap("2017-08-01", "2018-11-10");
		ba.call();
	}

	public static HashMap<String, List<String>> initConceptStock2Map() {
		List<String> conceptList = FileUtil.readByFileToList(Constant.concept2File, "GBK");
		conceptStock2Map = getConceptStock2Map(conceptList, "\t");
		return conceptStock2Map;
	}

	public static HashMap<String, BasicInfoDomain> initStockBasicMap(){
		List<String> basicInfoList = FileUtil.readByFileToList(Constant.basicInfoFile, "UTF-8");
		stockBasicMap = getBasicInfoMapByTxt(basicInfoList, "==");
		return stockBasicMap;
	}
	public static HashMap<String, List<String>> initConcept1Map(){
		List<String> conceptList = FileUtil.readByFileToList(Constant.concept1File, "GBK");
		stockConcept1Map = getConcept1Map(conceptList, "==");
		return stockConcept1Map;
	}
	public static HashMap<String, List<String>> initConcept2Map(){
		List<String> conceptList = FileUtil.readByFileToList(Constant.concept2File, "GBK");
		stockConcept2Map = getConcept2Map(conceptList, "\t");
		return stockConcept2Map;
	}
	private void call() {
		//String rootPath = "C:\\Users\\Administrator\\Desktop\\周家炬\\股票\\";
		//displayConceptMap(stockConceptMap, stockBasicMap);
		
		/*HashMap<String, List<HistoryDomain>> orderByDateHisMap = HistoryAnalyse.getOrderDateMap(historyMap, "2017-09-18", "2017-09-22");
		HashMap<String, List<HistoryDomain>> orderByDateHisMap2 = HistoryAnalyse.getOrderDateMap(historyMap, "2017-06-18", "2017-09-22");
		
		HashMap<String, List<HistoryDomain>> dieTingStockHisMap = PriceAnalyse.getDieTingStockHisMap(orderByDateHisMap2, 3, -30, -10);
		
		HashMap<String, HistoryDomain> startdayMap = HistoryAnalyse.getSomeDayMap(historyMap, "2017-09-21");
		HashMap<String, HistoryDomain> enddayMap = HistoryAnalyse.getSomeDayMap(historyMap, "2017-09-22");
		HashMap<String, List<HistoryDomain>> tranHighMap = TranAmountAnalyse.getTranRateHighStcok(orderByDateHisMap, stockBasicMap);
		HashMap<String, List<HistoryDomain>> tranSlowDownMap = TranAmountAnalyse.getTranAmountSlowDownStcok(orderByDateHisMap, stockBasicMap);
		HashMap<String, List<HistoryDomain>> tranHighTradeLowMap = TranAmountAnalyse.getTranAmountHighPriceLowStcok(orderByDateHisMap, stockBasicMap, 0, 50, true);
		HashMap<String, BasicInfoDomain> twodayPriceFlowMap = PriceAnalyse.getTwodayPriceHigh(startdayMap, enddayMap, stockBasicMap , 1, 5);
		HashMap<String, Double> twoTimeAverageMap = PriceAnalyse.getTwoTimeAverageMap(historyMap, twodayPriceFlowMap);*/
		//List<String> bankerList = getBankerStockList(twoTimeAverageMap, enddayMap);
		
		//displayTwodayPriceHighMap(twodayPriceHighMap, orderByDateMap, stockConceptMap);
		//HashMap<String, Double> profitRateMap = PriceAnalyse.getPriceRateMap(startdayMap, enddayMap, tranHighTradeLowMap, stockBasicMap, stockConcept1Map);
		
		//HashMap<String, List<String>> topIndustryMap = getIndustryTop(5, stockBasicMap, stockIndustryMap);
		//displayTopIndustryMap(topIndustryMap, stockBasicMap);
		//List<String> bankerFTList = getFanTanBankerList2(bankerList, orderByDateMap, 70, 100);
		//List<String> bankerFTList2 = getFanTanBankerList2(bankerList, dieTingStockHisMap, 55, 100);
		/*List<String> list = new ArrayList<String>();
		//list.add("美丽中国");
		list.add("一带一路");
		//list.add("节能环保");
		System.out.println(getStcokInterList(list, conceptStock2Map));*/
		analyzeProfitStock();
		analyzeGoodStock();
		
	}
	
	public static void analyzeProfitStock(){
		//List<String> stockList = getGoodStockList();
		List<String> stockList = getProfitStockList();
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyOrderMap, stockList);
		for(String code : codeHisMap.keySet()){
			List<HistoryDomain> list = codeHisMap.get(code);
			double lastHighest = list.get(list.size() - 1).getNumberClose();
			int lastHighestIndex = list.size() - 1;
			double lastLowest = list.get(list.size() - 1).getNumberClose();
			int lastlowestIndex = list.size() - 1;
			for(int i = 1; i < list.size() - 1; i++){
				double currentPrice = list.get(i).getNumberClose();
				double lastPrice = list.get(i + 1).getNumberClose();
				double nextPrice = list.get(i - 1).getNumberClose();
				if(currentPrice > lastHighest && currentPrice > nextPrice && currentPrice > lastPrice){
					lastHighest = list.get(i).getNumberClose();
					lastHighestIndex = i;
				}
			}
			for(int i = lastHighestIndex; i < list.size() - 1; i++){
				double currentPrice = list.get(i).getNumberClose();
				double lastPrice = list.get(i + 1).getNumberClose();
				double nextPrice = list.get(i - 1).getNumberClose();
				if(currentPrice < lastLowest && currentPrice < nextPrice && currentPrice < lastPrice){
					lastLowest = list.get(i).getNumberClose();
					lastlowestIndex = i;
				}
			}
			double amountRate = TranAmountAnalyse.getLastDayToAvgTranAmountRate(list);
			
			double lowestRate = AnalyseUtil.getRate(lastHighest, lastLowest);
			double rate = AnalyseUtil.getRate(lastHighest, list.get(list.size() - 1).getNumberClose());
			System.out.println("analyzeProfitStock:" + list.get(lastHighestIndex).getDate() + "-" + list.get(lastlowestIndex).getDate() + "\t" + code + "\t" + getName(code) + "\t" + rate + "\t" + lowestRate + "\t" + amountRate);
		}
	}
	

	public static void analyzeGoodStock(){
		List<String> stockList = getGoodStockList();
		//List<String> stockList = getProfitStockList();
		HashMap<String, List<HistoryDomain>> codeHisMap = HistoryAnalyse.getCodeHisMap(HistoryAnalyse.historyOrderMap, stockList);
		for(String code : codeHisMap.keySet()){
			List<HistoryDomain> list = codeHisMap.get(code);
			double lastHighest = list.get(list.size() - 1).getNumberClose();
			int lastHighestIndex = list.size() - 1;
			double lastLowest = list.get(list.size() - 1).getNumberClose();
			int lastlowestIndex = list.size() - 1;
			for(int i = 1; i < list.size() - 1; i++){
				double currentPrice = list.get(i).getNumberClose();
				double lastPrice = list.get(i + 1).getNumberClose();
				double nextPrice = list.get(i - 1).getNumberClose();
				if(currentPrice > lastHighest && currentPrice > nextPrice && currentPrice > lastPrice){
					lastHighest = list.get(i).getNumberClose();
					lastHighestIndex = i;
				}
			}
			for(int i = lastHighestIndex; i < list.size() - 1; i++){
				double currentPrice = list.get(i).getNumberClose();
				double lastPrice = list.get(i + 1).getNumberClose();
				double nextPrice = list.get(i - 1).getNumberClose();
				if(currentPrice < lastLowest && currentPrice < nextPrice && currentPrice < lastPrice){
					lastLowest = list.get(i).getNumberClose();
					lastlowestIndex = i;
				}
			}
			double amountRate = TranAmountAnalyse.getLastDayToAvgTranAmountRate(list);
			double lowestRate = AnalyseUtil.getRate(lastHighest, lastLowest);
			double rate = AnalyseUtil.getRate(lastHighest, list.get(list.size() - 1).getNumberClose());
			System.out.println("analyzeGoodStock:" + list.get(lastHighestIndex).getDate() + "-" + list.get(lastlowestIndex).getDate() + "\t" + code + "\t" + getName(code) + "\t" + rate + "\t" + lowestRate + "\t" + amountRate);
		}
	}
	
	public static List<String> getGoodStockList(){
		List<String> result = new ArrayList<String>();
		List<String> readByFileToList = FileUtil.readByFileToList(new File(Constant.goodStockFilePath), Constant.encode);
		for(String s : readByFileToList){
			if(s.indexOf("--") > -1||s.equals("")){
				continue;
			}
			String[] ss = s.split("\t");
			String name = BasicAnalyse.getName(ss[0]);
			System.out.println("getGoodStockList:" + ss[0] + "\t" + name);
			result.add(ss[0]);
		}
		return result;
	}
	
	public static List<String> getProfitStockList(){
		List<String> result = new ArrayList<String>();
		List<String> readByFileToList = FileUtil.readByFileToList(new File(Constant.profitStockFilePath), Constant.encode);
		for(String s : readByFileToList){
			String[] ss = s.split("\t");
			String name = BasicAnalyse.getName(ss[0]);
			System.out.println("getGoodStockList:" + ss[0] + "\t" + name);
			result.add(ss[0]);
		}
		return result;
	}
	
	public static List<String> getSmallCompanyList(Collection<String> coll, double startLTAmount, double endLTAmount) {
		List<String> result = new ArrayList<String>();
		for(String code : coll){
			BasicInfoDomain bid = stockBasicMap.get(code);
			if(!PriceAnalyse.todayPriceMap.containsKey(code)){
				System.out.println("getSmallCompanyList:无此股票当前行情" + code);
				continue;
			}
			double ltTranAmount = Double.parseDouble(bid.getOutstanding()) * PriceAnalyse.todayPriceMap.get(code);
			if(ltTranAmount >= startLTAmount && ltTranAmount <= endLTAmount){
				//System.out.println("getSmallCompanyList:" + code + " " + ltTranAmount);
				result.add(code);
			}
		}
		return result;
	}
	
	public static String getName(String code){
		if(!stockBasicMap.containsKey(code)){
			return "";
		}
		return stockBasicMap.get(code).getName();
	}
	
	public static List<String> getRemoveNewCompanyList(Collection<String> c){
		List<String> result = new ArrayList<String>();
		for(String code : c){
			BasicInfoDomain bid = stockBasicMap.get(code);
			String timeToMarket = bid.getTimeToMarket();
			if(timeToMarket.indexOf("2017") > -1){
				continue;
			}
			result.add(code);
		}
		return result;
	}
	
	public static List<String> getRemoveCYBStockList(Collection<String> c){
		List<String> result = new ArrayList<String>();
		for(String code : c){
			if(code.substring(0, 3).indexOf("300") > -1){
				continue;
			}
			result.add(code);
		}
		return result;
	}
	
	
	
	public static List<String> getInPBRateList(Collection<String> c, double startNum, double endNum) {
		List<String> result = new ArrayList<String>();
		for(String code : c){
			BasicInfoDomain bid = stockBasicMap.get(code);
			double pb = Double.parseDouble(bid.getPb());
			if(pb >= startNum && pb <= endNum){
				result.add(code);
			}
		}
		return result;
	}
	
	public static List<String> getInPERateList(Collection<String> c, double startNum, double endNum) {
		List<String> result = new ArrayList<String>();
		for(String code : c){
			BasicInfoDomain bid = stockBasicMap.get(code);
			double pe = Double.parseDouble(bid.getPe());
			if(pe >= startNum && pe <= endNum){
				result.add(code);
			}
		}
		return result;
	}
	
	public static HashMap<String, List<String>> getConceptStock2Map(List<String> conceptList, String regex) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		for (String s : conceptList) {
			String[] ss = s.split(regex);
			String code = ss[0];
			List<String> oneConceptsStocks; 
			for (int i = 1; i < ss.length; i++) {
				if(result.containsKey(ss[i])){
					oneConceptsStocks = result.get(ss[i]);
				}else{
					oneConceptsStocks = new ArrayList<String>();
				}
				oneConceptsStocks.add(code);

				result.put(ss[i], oneConceptsStocks);
			}
		}
		
		Set<String> keySet = result.keySet();
		//System.out.println(keySet.toString());
		return result;
	}
	private static HashMap<String, List<String>> getConcept2Map(List<String> conceptList, String regex) {
		HashMap<String, List<String>> conceptMap = new HashMap<String, List<String>>();
		for (String s : conceptList) {
			String[] ss = s.split(regex);
			String code = ss[0];
			List<String> oneStockConcepts; 
			if(conceptMap.containsKey(code)){
				oneStockConcepts = conceptMap.get(code);
			}else{
				oneStockConcepts = new ArrayList<String>();
			}
			for (int i = 1; i < ss.length; i++) {
				oneStockConcepts.add(ss[i]);
			}
			conceptMap.put(code, oneStockConcepts);
		}
		return conceptMap;
	}

	public static HashMap<String, List<String>> initIndustryMap(){
		List<String> basicInfoList = FileUtil.readByFileToList(Constant.basicInfoFile, "UTF-8");
		stockIndustryMap = getIndustryInfoMapByTxt(basicInfoList, "-");
		return stockIndustryMap;
	}
	
	/**
	 * 得到拥有概念集合之一的股票(并集)
	 * @param conceptList
	 * @return
	 */
	public static List<String> getStcokUnionList(List<String> conceptList, HashMap<String, List<String>> conceptStockMap) {
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < conceptList.size(); i++){
			String s = conceptList.get(i);
			List<String> list = conceptStockMap.get(s);
			System.out.println("getStcokUnionList:" + s + list);
			list.removeAll(result);
			result.addAll(list);
		}
		return result;
	}

	/**
	 * 得到同时拥有所有概念集合的股票(交集)
	 * @param conceptList
	 * @return
	 */
	public static List<String> getStcokInterList(List<String> conceptList, HashMap<String, List<String>> conceptStockMap){
		List<String> result = conceptStockMap.get(conceptList.get(0));
		for(int i = 1; i < conceptList.size(); i++){
			String s = conceptList.get(i);
			List<String> list = conceptStockMap.get(s);
			result.retainAll(list);
		}
		return result;
	}
	

	public List<String> getFanTanBankerList(List<String> bankerList,
			HashMap<String, List<HistoryDomain>> orderByDateMap, double rate) {
		List<String> result = new ArrayList<String>();
		for(String code : bankerList) {
			 List<HistoryDomain> list = orderByDateMap.get(code);
			 double theLastFlow = list.get(list.size()-1).getNumberClose() - list.get(list.size()-2).getNumberClose();
			 if(theLastFlow < 0) {
				 continue;
			 }
			 double lastAmount = list.get(0).getNumberCount();
			 double lastPrice = list.get(0).getNumberClose();
			 double sum = list.size() - 1;
			 double reachSum = 0;
			 for(int i = 1; i < list.size(); i++) {
				 double amount = list.get(i).getNumberCount();
				 double price = list.get(i).getNumberClose();
				 if(amount <= lastAmount && price < lastPrice) {
					 reachSum++;
				 }
				 lastAmount = amount;
				 lastPrice = price;
			 }
			 double reachRate = (reachSum/sum) * 100;
			 if(reachRate >= rate) {
				 result.add(code);
				 System.out.println("getFanTanBankerList:"+code + "-" + reachRate);
			 }
		}
		return result;
	}
	

	private void displayTwodayPriceHighMap(HashMap<String, BasicInfoDomain> twodayPriceHighMap,
			HashMap<String, List<HistoryDomain>> orderByDateMap, HashMap<String, List<String>> stockConceptMap) {
		Set<String> codes = twodayPriceHighMap.keySet();
		for(String code : codes){
			BasicInfoDomain bd = twodayPriceHighMap.get(code);
			if(bd.getTimeToMarket().indexOf("2017") > -1){
				continue;
			}
			List<HistoryDomain> list = orderByDateMap.get(code);
			System.out.println("strat---code:" + code + stockConceptMap.get(code) + "\t" + bd.getBackupDouble());
			System.out.print("交易量");
			for(HistoryDomain hd : list){
				System.out.print("\t" + hd.getCount());
			}
			System.out.print("\n收盘价");

			double lastClose = 0;
			for(HistoryDomain hd : list){
				if(lastClose == 0){
					lastClose = hd.getNumberClose();
					System.out.print("\t" + lastClose);
				}else{
					double flow = ((hd.getNumberClose() - lastClose)/lastClose) * 100;
					String flowStr = flow + "    ";
					System.out.print("\t" + flowStr.substring(0, 4) + "%");
				}
			}
			System.out.print("\n换手率");
			for(HistoryDomain hd : list){
				double allTranAmount = Double.parseDouble(bd.getOutstanding()) * 100000000;
				double tranAmount = Double.parseDouble(hd.getCount());
				double tranRate = (tranAmount/allTranAmount) * 100 * 100;
				String tranRateStr = tranRate+"";
				System.out.print("\t" + tranRateStr.substring(0, 4));
			}
			System.out.println("\nend----");
		}
	}

	

	

	private void displayConceptMap(HashMap<String, List<String>> stockConceptMap,
			HashMap<String, BasicInfoDomain> stockBasicMap) {
		Set<String> keys = stockConceptMap.keySet();
		System.out.println("stockConceptMap.size():" + stockConceptMap.size());
		for(String key : keys){
			List<String> list = stockConceptMap.get(key);
			System.out.print("displayConceptMap:" + key + list.size());
			for(String concept : list){
				System.out.print("\t" + concept);
			}
			System.out.println();
		}
	}

	private static HashMap<String, List<String>> getConcept1Map(List<String> conceptList, String regex) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		for(String s : conceptList){
			String[] ss = s.split(regex);
			String code = ss[0];
			String concept = ss[2];
			List<String> list;
			if(result.containsKey(code)){
				list = result.get(code);
			}else{
				list = new ArrayList<String>();
			}
			/*if(list.contains(concept)){
				System.out.println("-----");
			}*/
			list.add(concept);
			result.put(code, list);
			
		}
		return result;
	}

	

	

	
	
	

	
	private static List<HistoryDomain> sortWithQuickByDate(List<HistoryDomain> sourceList){
		List<HistoryDomain> result = new ArrayList<HistoryDomain>();
		List<HistoryDomain> headList = new ArrayList<HistoryDomain>();
		List<HistoryDomain> tailList = new ArrayList<HistoryDomain>();
		if(sourceList.size() < 2){
			return sourceList;
		}
		HistoryDomain first = sourceList.get(0);
		long firstDateTamp = parseDate(first.getDate(), "yyyy-MM-dd").getTime();
		for(int i = 1; i < sourceList.size(); i++){
			HistoryDomain hd = sourceList.get(i);
			long dateTamp = parseDate(hd.getDate(), "yyyy-MM-dd").getTime();
			if(dateTamp > firstDateTamp){
				tailList.add(hd);
			}else{
				headList.add(hd);
			}
		}
		headList = sortWithQuickByDate(headList);
		tailList = sortWithQuickByDate(tailList);
		result.addAll(headList);
		result.add(first);
		result.addAll(tailList);
		
		return result;
	}

	private static Date parseDate(String dateStr, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	private static boolean getIsTwoTimeMiddle(String date, String startDateStr, String endDateStr, String timePattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(startDateStr);
			endDate = sdf.parse(endDateStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
			return false;
		}
		Long startTamp = startDate.getTime();
		Long endTamp = endDate.getTime();
		long dateTamp = 0;
		try {
			dateTamp = sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		if(dateTamp >= startTamp && dateTamp <= endTamp){
			return true;
		}
		return false;
	}

	

	

	private void displayTopIndustryMap(HashMap<String, List<String>> topIndustryMap, 
			HashMap<String, BasicInfoDomain> stockBasicMap) {
		System.out.println("行业\t流动资产最高");
		for(String key : topIndustryMap.keySet()){
			List<String> topList = topIndustryMap.get(key);
			System.out.print(key + "\t");
			for(String code : topList){
				System.out.print(code + "_" +  stockBasicMap.get(code).getName() + "\t");
			}
			System.out.println("");
		}
	}

	private HashMap<String, List<String>> getIndustryTop(int topNum, HashMap<String, BasicInfoDomain> stockBasicMap,
			HashMap<String, List<String>> stockIndustryMap) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		Set<String> keys = stockIndustryMap.keySet();
		for(String industry : keys){
			/*if(industry.equals("银行")){
				System.out.println("");
			}*/
			List<String> topList = new ArrayList<String>();
			List<String> codes = stockIndustryMap.get(industry);
			for(String code : codes){
				BasicInfoDomain bid = stockBasicMap.get(code);
				//使用冒泡排序
				if(topList.size() == 0){
					topList.add(bid.getCode());
					continue;
				}
				int insertIndex = 0;
				for(int i = 0; i < topList.size(); i++){
					Double p = Double.parseDouble(stockBasicMap.get(topList.get(i)).getOutstanding());
					Double n = Double.parseDouble(bid.getOutstanding());
					if(n > p){
						insertIndex = i + 1;
					}
				}
				topList.add(insertIndex, bid.getCode());
				if(topList.size() > topNum){
					topList.remove(0);
				}
			}
			result.put(industry, topList);
		}
		return result;
	}

	private static HashMap<String, BasicInfoDomain> getBasicInfoMapByTxt(List<String> list, String splitFlag) {
		HashMap<String, BasicInfoDomain> result = new HashMap<String, BasicInfoDomain>();
		for(String s : list){
			String[] ss = s.split(splitFlag);
			BasicInfoDomain bid = new BasicInfoDomain(ss[0], ss[1], ss[2], ss[3], ss[4], ss[5], ss[6],
					ss[7], ss[8], ss[9], ss[10], ss[11], ss[12], ss[13], ss[14], ss[15], ss[16], ss[17],
					ss[18], ss[19], ss[20], ss[21], ss[22]);
			if(result.containsKey(ss[0])){
				System.out.println("已经存在："+ss[0]);
				continue;
			}
			result.put(ss[0], bid);
		}
		return result;
	}
	private static HashMap<String, List<String>> getIndustryInfoMapByTxt(List<String> list, String splitFlag) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		for(String s : list){
			String[] ss = s.split(splitFlag);
			List<String> industryList = null;
			if(result.containsKey(ss[2])){
				industryList = result.get(ss[2]);
				if(industryList.contains(ss[0])){
					System.out.println("已经存在："+ss[0]);
					continue;
				}
			}else{
				industryList = new ArrayList<String>();
			}
			industryList.add(ss[0]);
			result.put(ss[2], industryList);
		}
		return result;
	}
	
	public static void analyzeStock(String code, HashMap<String, List<HistoryDomain>> historyMap){
		System.out.println("analyzeStock:" + code + "\t" + getName(code) + "\t" + ConceptAnalyse.stockConcept3Map.get(code));
		if(historyMap != null){
			double todayTranAmount = historyMap.get(code).get(historyMap.size() - 1).getNumberCount();
			System.out.println("analyzeStock:" + code + "\t平均交易量:" + TranAmountAnalyse.getOneStockAvgTranAmount(historyMap, code) + "\t当前交易量" + todayTranAmount);
			if(historyMap.size() > 5){
				double today1TranAmount = historyMap.get(code).get(historyMap.size() - 2).getNumberCount();
				double today2TranAmount = historyMap.get(code).get(historyMap.size() - 3).getNumberCount();
				double today3TranAmount = historyMap.get(code).get(historyMap.size() - 4).getNumberCount();
				double today4TranAmount = historyMap.get(code).get(historyMap.size() - 5).getNumberCount();
				System.out.println("analyzeStock:" + code + "\t近五天交易量:" + today4TranAmount + "\t" + today3TranAmount + "\t" + today2TranAmount + "\t" + today1TranAmount + "\t" + todayTranAmount);
			}
		}
		
	}

}
