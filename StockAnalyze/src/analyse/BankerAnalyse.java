package analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import analyseBasicInfo.BasicAnalyse;
import analyseHistory.HistoryAnalyse;
import analysePrice.PriceAnalyse;
import analyseTranAmount.TranAmountAnalyse;
import domain.BasicInfoDomain;
import domain.HistoryDomain;

public class BankerAnalyse {
	public static HashMap<String, BasicInfoDomain> stockBasicMap;
	HashMap<String, List<String>> stockIndustryMap;
	public static HashMap<String, List<HistoryDomain>> historyMap;
	HashMap<String, List<String>> stockConceptMap;
	public static void main(String[] args) {
		BankerAnalyse ba = new BankerAnalyse();
		stockBasicMap = BasicAnalyse.initStockBasicMap();
		historyMap = HistoryAnalyse.initHistoryMap("2017-08-01", "2017-09-22");
		ba.call();
	}
	private void call() {
		//String rootPath = "C:\\Users\\Administrator\\Desktop\\ÖÜ¼Ò¾æ\\¹ÉÆ±\\";
		//displayConceptMap(stockConceptMap, stockBasicMap);
		
		HashMap<String, List<HistoryDomain>> orderByDateHisMap = HistoryAnalyse.getOrderDateMap(historyMap, "2017-09-18", "2017-09-22");
		//HashMap<String, List<HistoryDomain>> orderByDateHisMap2 = HistoryAnalyse.getOrderDateMap(historyMap, "2017-06-18", "2017-09-22");
		
		//HashMap<String, List<HistoryDomain>> dieTingStockHisMap = PriceAnalyse.getDieTingStockHisMap(orderByDateHisMap2, 3, -30, -10);
		
		HashMap<String, HistoryDomain> startdayMap = HistoryAnalyse.getSomeDayMap(historyMap, "2017-09-22");
		HashMap<String, HistoryDomain> enddayMap = HistoryAnalyse.getSomeDayMap(historyMap, "2017-09-25");
		//HashMap<String, List<HistoryDomain>> tranHighMap = TranAmountAnalyse.getTranRateHighStcok(orderByDateHisMap, stockBasicMap);
		//HashMap<String, List<HistoryDomain>> tranSlowDownMap = TranAmountAnalyse.getTranAmountSlowDownStcok(orderByDateHisMap, stockBasicMap);
		//HashMap<String, List<HistoryDomain>> tranHighTradeLowMap = TranAmountAnalyse.getTranAmountHighPriceLowStcok(orderByDateHisMap, stockBasicMap, 0, 50, true);
		HashMap<String, BasicInfoDomain> twodayPriceFlowMap = PriceAnalyse.getTwodayPriceHigh(startdayMap, enddayMap, stockBasicMap , 1, 5);
		HashMap<String, Double> twoTimeAverageMap = PriceAnalyse.getTwoTimeAverageMap(historyMap, twodayPriceFlowMap);
		List<String> bankerList = getBankerStockList(twoTimeAverageMap, enddayMap);
		
		//displayTwodayPriceHighMap(twodayPriceHighMap, orderByDateMap, stockConceptMap);
		//HashMap<String, Double> profitRateMap = getProfitRateList(startdayMap, enddayMap, tranHighTradeLowMap, stockBasicMap, stockConceptMap);
		
		//HashMap<String, List<String>> topIndustryMap = getIndustryTop(5, stockBasicMap, stockIndustryMap);
		//displayTopIndustryMap(topIndustryMap, stockBasicMap);
		//List<String> bankerFTList = getFanTanBankerList2(bankerList, orderByDateMap, 70, 100);
		//List<String> bankerFTList2 = getFanTanBankerList2(bankerList, dieTingStockHisMap, 55, 100);
	}
	
	private List<String> getBankerStockList(HashMap<String, Double> twoTimeAverageMap,
			HashMap<String, HistoryDomain> enddayMap) {
		List<String> result = new ArrayList<String>();
		Set<String> codes = twoTimeAverageMap.keySet();
		for(String code : codes){
			double average = twoTimeAverageMap.get(code);
			HistoryDomain hd = enddayMap.get(code);
			double endClose = hd.getNumberClose();
			if(average > endClose){
				//System.out.println("getBankerStockList:"+code);
				result.add(code);
			}
		}
		return result;
	}
	
	private List<String> getFanTanBankerList2(List<String> bankerList,
			HashMap<String, List<HistoryDomain>> orderByDateMap, double startRate, int endRate) {
		List<String> result = new ArrayList<String>();
		for(String code : bankerList) {
			 List<HistoryDomain> list = orderByDateMap.get(code);
			 if(list == null) {
				 continue;
			 }
			 double theLastFlow = list.get(list.size()-1).getNumberClose() - list.get(list.size()-2).getNumberClose();
			 if(theLastFlow < 0) {
				 continue;
			 }
			 double lastAmount = list.get(0).getNumberCount();
			 double lastPrice = list.get(0).getNumberClose();
			 double sum = list.size() - 1;
			 double reachSum = 0;
			 for(int i = 1; i < list.size() - 1; i++) {
				 double amount = list.get(i).getNumberCount();
				 double price = list.get(i).getNumberClose();
				 if(amount <= lastAmount) {
					 reachSum++;
				 }
				 if(price <= lastPrice) {
					 reachSum++;
				 }
				 lastAmount = amount;
				 lastPrice = price;
			 }
			 double reachRate = ((reachSum/2)/sum) * 100;
			 if(reachRate >= startRate && reachRate <= endRate) {
				 result.add(code);
				 System.out.println("getFanTanBankerList:"+code + "-" + reachRate);
			 }
		}
		return result;
	}

	
}
