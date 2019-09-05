package analysePrice;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import Util.AnalyseUtil;
import Util.Constant;
import Util.FileUtil;
import Util.NumCountUtil;
import analyseBasicInfo.BasicAnalyse;
import analyseConcept.ConceptAnalyse;
import analyseHistory.HistoryAnalyse;
import analyseTranAmount.TranAmountAnalyse;
import domain.BasicInfoDomain;
import domain.HistoryDomain;

public class PriceAnalyse {

	public static HashMap<String, Double> todayPriceMap;
	public static HashMap<String, List<HistoryDomain>> historyMap;
	public static HashMap<String, List<String>> concept2Map;

	public static void main(String[] args) {
		HistoryAnalyse.initHistoryMap("2016-03-14", "2018-11-10");
		ConceptAnalyse.initConcept3Map();
		BasicAnalyse.initStockBasicMap();
		//getAllStockDayPriceMap();
		getConceptDayPriceMap("雄安新区");
		getConceptDayPriceMap("医疗器械");
		getConceptDayPriceMap("增强现实");
		getConceptDayPriceMap("新材料");
		getConceptDayPriceMap("一带一路");
		getConceptDayPriceMap("智能家居");
		getConceptDayPriceMap("物联网");
		getConceptDayPriceMap("云计算");
		getConceptDayPriceMap("大数据");
		getConceptDayPriceMap("智慧城市");
		getConceptDayPriceMap("智能机器");
		getConceptDayPriceMap("网络安全");
		getConceptDayPriceMap("车联网");
		getConceptDayPriceMap("北斗导航");
		getConceptDayPriceMap("2025规划");
		getConceptDayPriceMap("3D打印");
		getConceptDayPriceMap("太阳能");
		getConceptDayPriceMap("新能源车");
		getConceptDayPriceMap("国产芯片");
		getConceptDayPriceMap("人工智能");
		getConceptDayPriceMap("节能环保");
		getConceptDayPriceMap("国产芯片");
		getConceptDayPriceMap("5G概念");
		getConceptDayPriceMap("OLED");
		getConceptDayPriceMap("虚拟现实");
		getConceptDayPriceMap("单抗概念");
		getConceptDayPriceMap("新能源");
		getConceptDayPriceMap("租售同权");
	}
	
	public static void lastCall(){
		HashMap<String, List<String>> conceptStock2Map = BasicAnalyse.initConceptStock2Map();
		HashMap<String, List<HistoryDomain>> orderByDateHisMap = HistoryAnalyse.getOrderDateMap(historyMap,
				"2017-04-30", "2017-10-10");
		List<String> conceptList = new ArrayList<String>();
		// conceptList.add("美丽中国");
		conceptList.add("一带一路");
		conceptList.add("节能环保");
		conceptList.add("雄安新区");
		conceptList.add("集成电路");
		List<String> stcokUnionList = BasicAnalyse.getStcokUnionList(conceptList, conceptStock2Map);
		HashMap<String, List<HistoryDomain>> conceptHisMap = HistoryAnalyse.getCodeHisMap(orderByDateHisMap,
				stcokUnionList);
		HashMap<String, List<HistoryDomain>> dieTingLowTAHisMap = getDieTingAndLowTranAmountStockHisMap(conceptHisMap,
				5, -40, -20, 0.5);
		// getDieTingTimeMap(orderByDateHisMap, 5, -30, -10);
		getDieTingAndboDongHighStockMap(dieTingLowTAHisMap, "2017-10-10", 5, -80, -10, 1, 100, 5, 5, false, false, -50,
				10);
	}

	public static HashMap<String, Double> initTodayPriceMap() {
		todayPriceMap = new HashMap<String, Double>();
		List<File> allFile = FileUtil.getAllFileByFile(new File(Constant.updateFilePath));
		List<String> fileNameList = new ArrayList<String>();
		for (File file : allFile) {
			fileNameList.add(file.getAbsolutePath());
		}
		List<String> sortStringList = AnalyseUtil.getSortStringList(fileNameList, false);
		File file = new File(sortStringList.get(0));

		List<String> updateDataList = FileUtil.readByFileToList(file, "GBK");
		for (String oneData : updateDataList) {
			String[] datas = oneData.split("\t");
			String code = datas[0];
			String close = datas[3];
			Double doubleClose = Double.parseDouble(close);
			todayPriceMap.put(code, doubleClose);
		}

		return todayPriceMap;
	}
	
	@Test
	public static HashMap<String, Double> getConceptDayPriceMap(String conceptName){
		HashMap<String, Double> result = new HashMap<String, Double>();
		HashMap<String, List<Double>> datePriceMap = new HashMap<String, List<Double>>();
		List<String> list = ConceptAnalyse.concept3Map.get(conceptName);
		System.out.println("getConceptDayPriceMap:" + conceptName);
		for(String code : list){
			List<HistoryDomain> hisList = HistoryAnalyse.historyMap.get(code);
			if(hisList == null){
				System.out.println("HistoryAnalyse.historyMap.get(code) == null" + code);
				continue;
			}
			for(int i = 1; i < hisList.size(); i++){
				HistoryDomain hd = hisList.get(i);
				HistoryDomain lastHd = hisList.get(i - 1);
				String date = hd.getDate();
				double rate = hd.getNumberRate(lastHd.getNumberClose());
				if(rate == -999){
					continue;
				}
				List<Double> datePriceList = datePriceMap.get(date);
				if(datePriceList == null){
					datePriceList = new ArrayList<Double>();
				}
				datePriceList.add(rate);
				datePriceMap.put(date, datePriceList);
				//System.out.println(date + "\t" +  NumCountUtil.numMap.get(date));
			}
		}
		for(String date : datePriceMap.keySet()){
			double average = getListAverage(datePriceMap.get(date));
			result.put(date, average);
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(datePriceMap.keySet());
		dateList.sort(null);
		/*System.out.println("------平均升幅");
		for(String date : dateList){
			System.out.println(date + "\t" + result.get(date));
		}*/

		System.out.println("------持续升幅");
		double sumRate = 0;
		for(String date : dateList){
			sumRate = sumRate + result.get(date);
			System.out.println(date + "\t" + sumRate);
		}
		
		return result;
	}
	
	public static HashMap<String, Double> getAllStockDayPriceMap(){
		HashMap<String, Double> result = new HashMap<String, Double>();
		HashMap<String, List<Double>> datePriceMap = new HashMap<String, List<Double>>();
		for(String code : BasicAnalyse.stockBasicMap.keySet()){
			String timeToMarket = BasicAnalyse.stockBasicMap.get(code).getTimeToMarket();
			/*if(timeToMarket.compareTo("20130101") > 0){
				continue;
			}*/
			List<HistoryDomain> hisList = HistoryAnalyse.historyMap.get(code);
			if(hisList == null){
				System.out.println("HistoryAnalyse.historyMap.get(code) == null" + code);
				continue;
			}
			for(int i = 1; i < hisList.size(); i++){
				HistoryDomain hd = hisList.get(i);
				HistoryDomain lastHd = hisList.get(i - 1);
				String date = hd.getDate();
				double rate = hd.getNumberRate(lastHd.getNumberClose());
				if(rate == -999){
					continue;
				}
				List<Double> datePriceList = datePriceMap.get(date);
				if(datePriceList == null){
					datePriceList = new ArrayList<Double>();
				}
				datePriceList.add(rate);
				datePriceMap.put(date, datePriceList);
				//System.out.println(date + "\t" +  NumCountUtil.numMap.get(date));
			}
		}
		for(String date : datePriceMap.keySet()){
			double average = getListAverage(datePriceMap.get(date));
			result.put(date, average);
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(datePriceMap.keySet());
		dateList.sort(null);
		/*System.out.println("------平均升幅");
		for(String date : dateList){
			System.out.println(date + "\t" + result.get(date));
		}*/

		System.out.println("------持续升幅");
		double sumRate = 0;
		for(String date : dateList){
			sumRate = sumRate + result.get(date);
			System.out.println(date + "\t" + sumRate);
		}
		
		return result;
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

	public static HashMap<String, List<Double>> getMoreDayFlowRateMap(Collection<String> c, HashMap<String, List<HistoryDomain>> historyMap, String startDate, List<String> dates){
		HashMap<String, List<Double>> result = new HashMap<String, List<Double>>();
		HashMap<String, Double> someDayAllPriceMap = getSomeDayAllPriceMap(historyMap, startDate);
		for(String date : dates){
			HashMap<String, Double> someDayAllPriceMap2 = getSomeDayAllPriceMap(historyMap, date);
			for(String code : c){
				double rate = 0;
				if(someDayAllPriceMap.containsKey(code) && someDayAllPriceMap2.containsKey(code)){
					double startRate = someDayAllPriceMap.get(code);
					double endRate = someDayAllPriceMap2.get(code);
					rate = ((endRate - startRate)/startRate) * 100;
				}
				List<Double> list = result.get(code);
				if(list == null){
					list = new ArrayList<Double>();
				}
				list.add(rate);
				result.put(code, list);
			}
		}
		return result;
		
	}

	public static HashMap<String, Double> getHighestFlowInRate(HashMap<String, List<HistoryDomain>> historyMap, double startRate, double endRate) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		for (String code : historyMap.keySet()) {
			if(code.equals("600060")){
				System.out.println("getHighestFlowInRate:"+code);
			}
			List<HistoryDomain> list = historyMap.get(code);
			double highestFlowPriceRate = getHighestFlowPriceRate(list);
			if(highestFlowPriceRate >= startRate && highestFlowPriceRate <= endRate) {
				result.put(code, highestFlowPriceRate);
				System.out.println("getHighestFlowInRate:" + code + "\t" + list.size() + "\t" + highestFlowPriceRate);
			}
		}
		return result;
	}
	
	public static double getSomeDayPrice(HashMap<String, List<HistoryDomain>> historyMap,String code, String date){
		for(String key : historyMap.keySet()){
			if(key.equals(code)){
				List<HistoryDomain> list = historyMap.get(code);
				for(HistoryDomain hd : list){
					if(hd.getDate().equals(date)){
						return hd.getNumberClose();
					}
				}
			}
		}
		return 0;
	}
	
	public static HashMap<String, Double> getSomeDayAllPriceMap(HashMap<String, List<HistoryDomain>> historyMap, String date){
		HashMap<String, Double> result = new HashMap<String, Double>();
		for (String key : historyMap.keySet()) {
			List<HistoryDomain> list = historyMap.get(key);
			for (HistoryDomain hd : list) {
				if (hd.getDate().equals(date)) {
					result.put(key, hd.getNumberClose());
				}
			}
		}
		return result;
	}
	
	public static HashMap<String, Double> getTwoDayBalanceRateMap(HashMap<String, Double> oldDayMap, HashMap<String, Double> newDayMap, double startRate, double endRate){
		HashMap<String, Double> result = new HashMap<String, Double>();
		for(String code : oldDayMap.keySet()){
			//System.out.println("getTwoDayBalanceRateMap:新日期Map缺少信息:"+code);
			if(!newDayMap.containsKey(code)){
				continue;
			}
			double oldPrice = oldDayMap.get(code);
			double todayPrice = newDayMap.get(code);
			double balance = todayPrice - oldPrice;
			double rate = (balance/oldPrice) * 100;
			if(rate >= startRate && rate <= endRate){
				result.put(code, rate);
				System.out.println("getTwoDayBalanceRateMap:" + code + "\t" + rate);
			}
		}
		return result;
	}
	
	public static HashMap<String, Double> getLowestFlowInRate(HashMap<String, List<HistoryDomain>> historyMap, double startRate, double endRate) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		for (String code : historyMap.keySet()) {
			List<HistoryDomain> list = historyMap.get(code);
			double highestFlowPriceRate = getLowestFlowPriceRate(list);
			if(highestFlowPriceRate >= startRate && highestFlowPriceRate <= endRate) {
				result.put(code, highestFlowPriceRate);
				System.out.println("getLowestFlowInRate:" + code + "\t" + list.size() + "\t" + highestFlowPriceRate);
			}
		}
		return result;
	}

	public static double getFlowRate(double startPrice, double endPrice) {
		double rate = ((endPrice - startPrice) / startPrice) * 100;
		return rate;
	}

	public static HashMap<String, List<HistoryDomain>> getDieTingStockHisMap(
			HashMap<String, List<HistoryDomain>> historyMap, int dayNum, int startRate, int endRate) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> hisList = historyMap.get(code);
			int listSize = hisList.size();
			for (int i = 0; i < listSize; i++) {
				double startDayClose = hisList.get(i).getNumberClose();
				int endDayIndex = i + dayNum;
				if (endDayIndex >= listSize) {
					break;
				}
				double endDayClose = hisList.get(endDayIndex).getNumberClose();
				double balance = endDayClose - startDayClose;
				double rate = (balance / startDayClose) * 100;
				if (rate >= startRate && rate <= endRate) {
					result.put(code, hisList);
					System.out.println("getDieTingStockHisMap:" + code + " " + hisList.get(i).getDate());
				}
			}
		}
		return result;
	}

	/**
	 * 跌停且跌停成交量对比平均成交量要低的股票
	 * 
	 * @param historyMap
	 * @param dayNum
	 * @param startRate
	 * @param endRate
	 * @param lowRate
	 * @return
	 */
	public static HashMap<String, List<HistoryDomain>> getDieTingAndLowTranAmountStockHisMap(
			HashMap<String, List<HistoryDomain>> historyMap, int dayNum, int startRate, int endRate, double lowRate) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		HashMap<String, Double> tranAmountAverageMap = TranAmountAnalyse.getTranAmountAverage(historyMap);
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> hisList = historyMap.get(code);
			int listSize = hisList.size();
			for (int i = 0; i < listSize; i++) {
				double startDayClose = hisList.get(i).getNumberClose();
				int endDayIndex = i + dayNum;
				if (endDayIndex >= listSize) {
					break;
				}
				double endDayClose = hisList.get(endDayIndex).getNumberClose();
				double balance = endDayClose - startDayClose;
				double rate = (balance / startDayClose) * 100;
				if (rate >= startRate && rate <= endRate) {
					double day = 0;
					double sumTranAmount = 0;
					for (int j = i; j <= endDayIndex; j++) {
						sumTranAmount = sumTranAmount + hisList.get(j).getNumberCount();
						day++;
					}
					double avgTranAmount = tranAmountAverageMap.get(code);
					double taRate = (avgTranAmount * day) / sumTranAmount;
					if (taRate > lowRate) {
						result.put(code, hisList);
						System.out.println("getDieTingAndLowTranAmountStockHisMap:" + code + " "
								+ hisList.get(i).getDate() + " " + taRate);

					}
				}
			}
		}
		return result;
	}

	public static HashMap<String, List<String>> getDieTingTimeMap(HashMap<String, List<HistoryDomain>> historyMap,
			int dayNum, int startRate, int endRate) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> hisList = historyMap.get(code);
			int listSize = hisList.size();
			for (int i = 0; i < listSize; i++) {
				double startDayClose = hisList.get(i).getNumberClose();
				int endDayIndex = i + dayNum;
				if (endDayIndex >= listSize) {
					break;
				}
				double endDayClose = hisList.get(endDayIndex).getNumberClose();
				double balance = endDayClose - startDayClose;
				double rate = (balance / startDayClose) * 100;
				String startDate = hisList.get(i).getDate();
				String endDate = hisList.get(endDayIndex).getDate();
				if (rate >= startRate && rate <= endRate) {
					List<String> list = result.get(code);
					if (list == null) {
						list = new ArrayList<String>();
					}
					list.add(startDate + "_" + endDate);
					result.put(code, list);
					i = endDayIndex;
					System.out.println("getDieTingStockHisMap:" + code + " " + startDate + "_" + endDate);
				}
			}
		}
		return result;
	}

	public static HashMap<String, String> getDieTingLastTimeMap(HashMap<String, List<HistoryDomain>> historyMap,
			int dayNum, int startRate, int endRate) {
		HashMap<String, String> result = new HashMap<String, String>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> hisList = historyMap.get(code);
			int listSize = hisList.size();
			for (int i = 0; i < listSize; i++) {
				double startDayClose = hisList.get(i).getNumberClose();
				int endDayIndex = i + dayNum;
				if (endDayIndex >= listSize) {
					break;
				}
				double endDayClose = hisList.get(endDayIndex).getNumberClose();
				double balance = endDayClose - startDayClose;
				double rate = (balance / startDayClose) * 100;
				String startDate = hisList.get(i).getDate();
				String endDate = hisList.get(endDayIndex).getDate();
				if (rate >= startRate && rate <= endRate) {
					result.put(code, endDate);
					System.out.println("getDieTingStockHisMap:" + code + " " + startDate + "_" + endDate);
				}
			}
		}
		return result;
	}
	
	public static HashMap<String, String> getDieTingFirstTimeMap(HashMap<String, List<HistoryDomain>> historyMap,
			int dayNum, int startRate, int endRate) {
		HashMap<String, String> result = new HashMap<String, String>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> hisList = historyMap.get(code);
			int listSize = hisList.size();
			for (int i = 0; i < listSize; i++) {
				double startDayClose = hisList.get(i).getNumberClose();
				int endDayIndex = i + dayNum;
				if (endDayIndex >= listSize) {
					break;
				}
				double endDayClose = hisList.get(endDayIndex).getNumberClose();
				double balance = endDayClose - startDayClose;
				double rate = (balance / startDayClose) * 100;
				String startDate = hisList.get(i).getDate();
				String endDate = hisList.get(endDayIndex).getDate();
				if (rate >= startRate && rate <= endRate) {
					result.put(code, endDate);
					System.out.println("getDieTingStockHisMap:" + code + " " + startDate + "_" + endDate);
					break;
				}
			}
		}
		return result;
	}

	public static HashMap<String, List<String>> getDieTingAndLowPriceTimeMap(
			HashMap<String, List<HistoryDomain>> historyMap, int dayNum, int startRate, int endRate, int startLowRate,
			int endLowRate, String endDate) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		HashMap<String, List<String>> dieTingTimeMap = getDieTingTimeMap(historyMap, dayNum, startRate, endRate);
		Set<String> codes = dieTingTimeMap.keySet();
		for (String code : codes) {
			List<String> list = dieTingTimeMap.get(code);
			for (String time : list) {
				String[] dates = time.split("_");
				String startDate = dates[1];
				// String endDate = "2017-09-22";
				List<HistoryDomain> hisList = historyMap.get(code);
				double startPrice = 0;
				double endPrice = 0;
				for (HistoryDomain hd : hisList) {
					if (hd.getDate().equals(startDate)) {
						startPrice = hd.getNumberClose();
					}
					if (hd.getDate().equals(endDate)) {
						endPrice = hd.getNumberClose();
					}
				}
				double rate = ((endPrice - startPrice) / startPrice) * 100;
				if (rate >= startLowRate && rate <= endLowRate) {
					result.put(code, list);
					break;
				}
			}

		}
		return result;
	}

	/**
	 * 
	 * @param historyMap
	 * @param endDate
	 *            当前日期
	 * @param dayNum
	 *            跌停日数
	 * @param startRate
	 *            跌停开始
	 * @param endRate
	 *            跌停结束
	 * @param bdStartCount
	 *            开始采集波动数
	 * @param bdEndCount
	 *            结束采集波动数
	 * @param bdBeginLimitRate
	 *            波动上升值
	 * @param bdEndLimitRate
	 *            波动下降值
	 * @param isBDHigh
	 *            是否波动底部上升
	 * @param isBDLow
	 *            是否波动底部下降
	 * @param startLowRate
	 *            当前比跌停上升幅度-开始
	 * @param endLowRate
	 *            当前比跌停上升幅度-结束
	 * @return
	 */
	public static HashMap<String, List<HistoryDomain>> getDieTingAndboDongHighStockMap(
			HashMap<String, List<HistoryDomain>> historyMap, String endDate, int dayNum, int startRate, int endRate,
			int bdStartCount, int bdEndCount, int bdBeginLimitRate, int bdEndLimitRate, boolean isBDHigh,
			boolean isBDLow, int startLowRate, int endLowRate) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		HashMap<String, List<String>> dieTingTimeMap = getDieTingAndLowPriceTimeMap(historyMap, dayNum, startRate,
				endRate, startLowRate, endLowRate, endDate);
		Set<String> codes = dieTingTimeMap.keySet();
		for (String code : codes) {
			List<String> list = dieTingTimeMap.get(code);
			for (String time : list) {
				String[] dates = time.split("_");
				String startDate = dates[1];
				// String endDate = "2017-09-22";
				List<HistoryDomain> twoDayMiddleList = HistoryAnalyse.getTwoDayMiddleList(historyMap.get(code),
						startDate, endDate);
				int boDongStockCount = getBoDongStockCount(code, twoDayMiddleList, bdBeginLimitRate, bdEndLimitRate,
						isBDHigh, isBDLow);
				if (boDongStockCount >= bdStartCount && boDongStockCount <= bdEndCount) {
					System.out.println(code + "_" + time + "==" + boDongStockCount + "_" + concept2Map.get(code));
					result.put(code, historyMap.get(code));
				}
			}
		}
		return result;
	}

	public static int getBoDongStockCount(String code, List<HistoryDomain> hisList, int bdBeginLimitRate,
			int bdEndLimitRate, boolean isBDHigh, boolean isBDLow) {
		int listSize = hisList.size();
		double lowestPrice = hisList.get(0).getNumberClose();
		double hightestPrice = hisList.get(0).getNumberClose();
		String status = "bdStart";
		int bdCount = 0;
		int bdAllCount = 0;
		String startDate = hisList.get(0).getDate();
		String endDate = hisList.get(0).getDate();
		double startLowestPrice = hisList.get(0).getNumberClose();
		for (int i = 1; i < listSize; i++) {
			String date = hisList.get(i).getDate();
			/*
			 * if(date.equals("2017-09-15")&&code.equals("300224")){
			 * System.out.println(); }
			 */
			double dayClose = hisList.get(i).getNumberClose();
			if (dayClose < lowestPrice) {
				lowestPrice = dayClose;
				if (status.equals("bdStart")) {
					hightestPrice = dayClose;
					startDate = hisList.get(i).getDate();
				}
			}
			if (dayClose > hightestPrice) {
				hightestPrice = dayClose;
				startLowestPrice = lowestPrice;
				if (status.equals("bdMid")) {
					lowestPrice = dayClose;
				}
			}
			double bdRate = ((hightestPrice - lowestPrice) / lowestPrice) * 100;
			if (status.equals("bdStart")) {
				if (bdRate > bdBeginLimitRate) {
					status = "bdMid";
					lowestPrice = hightestPrice;
					bdRate = 0;
				}
			}
			if (status.equals("bdMid")) {
				if (bdRate > bdEndLimitRate) {
					endDate = hisList.get(i).getDate();
					if (isBDHigh) {
						if (lowestPrice > startLowestPrice) {
							bdCount++;
						}
					} else if (isBDLow) {
						if (lowestPrice < startLowestPrice) {
							bdCount++;
						}
					} else {
						bdCount++;
					}
					status = "bdStart";
					lowestPrice = dayClose;
					hightestPrice = dayClose;
					bdAllCount++;
					// System.out.println("getBoDongStockbdAllCount:" + code +
					// "_" + startDate + "_" + endDate + "==" + bdAllCount);
					startDate = hisList.get(i).getDate();
				}
			}

		}
		return bdCount;
	}

	public static HashMap<String, List<HistoryDomain>> getPriceLowStcok(
			HashMap<String, List<HistoryDomain>> orderByDateMap, HashMap<String, BasicInfoDomain> stockBasicMap,
			double startRate, double endRate, boolean isSmallCompany) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> keys = orderByDateMap.keySet();
		for (String key : keys) {
			String code = key;
			List<HistoryDomain> list = orderByDateMap.get(key);
			String timeToMarket;
			try {
				if (stockBasicMap.get(code) == null) {
					continue;
				}
				timeToMarket = stockBasicMap.get(code).getTimeToMarket();
			} catch (Exception e) {
				System.out.println("err:" + code);
				e.printStackTrace();
				continue;
			}
			if ((timeToMarket.indexOf("2017")) > -1 || timeToMarket.equals("0")) {
				continue;
			}
			if (list.size() == 0) {
				continue;
			}
			int sum = 0;

			double lastTranPrice = 0;
			for (HistoryDomain hd : list) {
				double tranPrice = hd.getNumberClose();
				double priceBalance = tranPrice - lastTranPrice;
				if (priceBalance < 0) {
					sum++;
				}
				lastTranPrice = tranPrice;
			}
			double listSize = list.size();
			double needRate = (sum / (listSize - 1)) * 100;
			BasicInfoDomain bif = stockBasicMap.get(code);

			if (needRate > startRate && needRate < endRate) {
				if (isSmallCompany) {
					double firstClose = Double.parseDouble(list.get(0).getClose());
					double tranAmount = Double.parseDouble(bif.getOutstanding()) * firstClose;
					double pb = Double.parseDouble(bif.getPb());
					if (tranAmount > 5 && tranAmount < 100 && pb < 10) {
						result.put(code, list);
						System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-"
								+ stockBasicMap.get(code).getTimeToMarket() + "-" + needRate);
					}
				} else {
					result.put(code, list);
					System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-"
							+ stockBasicMap.get(code).getTimeToMarket() + "-" + needRate);
				}

			}
		}
		return result;
	}

	public static HashMap<String, BasicInfoDomain> getTwodayPriceHigh(HashMap<String, HistoryDomain> startdayMap,
			HashMap<String, HistoryDomain> enddayMap, HashMap<String, BasicInfoDomain> stockBasicMap, double highCount,
			double maxCount) {
		HashMap<String, BasicInfoDomain> result = new HashMap<String, BasicInfoDomain>();
		Set<String> codes = startdayMap.keySet();
		for (String code : codes) {
			if (!enddayMap.containsKey(code)) {
				System.out.println("!enddayMap.containsKey(code):" + code);
				continue;
			}
			double start = startdayMap.get(code).getNumberClose();
			double end = enddayMap.get(code).getNumberClose();
			double balance = end - start;
			double rate = (balance / end) * 100;
			// System.out.println(rate);
			if (rate > highCount && rate < maxCount) {
				stockBasicMap.get(code).setBackupDouble(rate);
				result.put(code, stockBasicMap.get(code));
			}

		}
		return result;
	}

	public static HashMap<String, Double> getTwoTimeAverageMap(HashMap<String, List<HistoryDomain>> historyMap,
			HashMap<String, BasicInfoDomain> twodayPriceHighMap) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = twodayPriceHighMap.keySet();
		for (String code : codes) {
			double average = getOneStockAverage(code, historyMap);
			result.put(code, average);
		}
		return result;
	}

	public static HashMap<String, Double> getAverageMap(HashMap<String, List<HistoryDomain>> historyMap) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			double average = getOneStockAverage(code, historyMap);
			result.put(code, average);
		}
		return result;
	}

	public static double getOneStockAverage(String code, HashMap<String, List<HistoryDomain>> historyMap) {
		List<HistoryDomain> list = historyMap.get(code);
		double sumClose = 0.0;
		double sumDay = 0.0;
		for (HistoryDomain hd : list) {
			double close = hd.getNumberClose();
			if (close > 0) {
				sumClose = sumClose + close;
				sumDay++;
			}
		}
		double average = sumClose / sumDay;
		return average;
	}

	public static HashMap<String, Double> getPriceRateMap(HashMap<String, HistoryDomain> startdayMap,
			HashMap<String, HistoryDomain> enddayMap, HashMap<String, List<HistoryDomain>> tranMap,
			HashMap<String, BasicInfoDomain> stockBasicMap, HashMap<String, List<String>> stockConceptMap) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = tranMap.keySet();
		double sumRate = 0;
		List<String> highRateList = new ArrayList<String>();
		List<String> profitRateList = new ArrayList<String>();
		List<String> noProfitRateList = new ArrayList<String>();
		for (String code : codes) {
			if (!startdayMap.containsKey(code) || !enddayMap.containsKey(code)) {
				System.out.println("!startdayMap.containsKey or !enddayMap.containsKey:" + code);
				System.out.println("startdayMap.size:" + startdayMap.size());
				continue;
			}
			Double start = startdayMap.get(code).getNumberClose();
			Double end = enddayMap.get(code).getNumberClose();
			Double balance = end - start;
			Double rate = (balance / end) * 100;
			result.put(code, rate);
			sumRate = sumRate + rate;
			if (rate > 2) {
				highRateList.add(code);
			}
			if (rate > 0) {
				profitRateList.add(code);
			} else {
				noProfitRateList.add(code);
			}
			System.out.println("getProfitRateList:" + code + "\t" + stockBasicMap.get(code).getName() + "\t" + balance
					+ "\t" + rate);
		}
		System.out.println("sumRate:" + sumRate);
		for (String code : highRateList) {
			String concept;
			if (stockConceptMap.get(code) == null) {
				concept = "无";
			} else {
				concept = stockConceptMap.get(code).toString();
			}
			System.out.println("highRateList:" + code + concept);
		}

		return result;
	}

	public static HashMap<String, Double> getHighestFlowPriceMap(HashMap<String, List<HistoryDomain>> historyMap) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> list = historyMap.get(code);
			if (list == null || list.size() == 0) {
				continue;
			}
			double lowestPrice = list.get(0).getNumberClose();
			double highestPrice = list.get(list.size() - 1).getNumberClose();
			double flowBalance = highestPrice - lowestPrice;
			for (int i = 1; i < list.size() - 1; i++) {
				HistoryDomain hd = list.get(i);
				if (hd.getNumberClose() < lowestPrice && hd.getNumberClose() < list.get(i + 1).getNumberClose()
						&& hd.getNumberClose() < list.get(i - 1).getNumberClose()) {
					double balance = highestPrice - lowestPrice;
					if (balance > flowBalance) {
						flowBalance = balance;
					}
					highestPrice = list.get(list.size() - 1).getNumberClose();
					lowestPrice = hd.getNumberClose();
				}
				if (hd.getNumberClose() > highestPrice && hd.getNumberClose() > list.get(i + 1).getNumberClose()
						&& hd.getNumberClose() > list.get(i - 1).getNumberClose()) {
					highestPrice = hd.getNumberClose();
				}
			}
			double balance = highestPrice - lowestPrice;
			if (balance > flowBalance) {
				flowBalance = balance;
			}
			// System.out.println(code + "\t" + lowestPrice + "\t" +
			// highestPrice);
			double flowRate = (flowBalance / lowestPrice) * 100;
			result.put(code, flowRate);
		}
		return result;
	}

	public static double getHighestFlowPriceRate(List<HistoryDomain> list) {
		if (list == null || list.size() == 0) {
			return -999;
		}
		double lowestPrice = list.get(0).getNumberClose();
		double highestPrice = list.get(list.size() - 1).getNumberClose();
		double flowBalance = highestPrice - lowestPrice;
		String startDate = list.get(0).getDate();
		String endDate = list.get(list.size() - 1).getDate();
		String tempStartDate = list.get(0).getDate();
		String tempEndDate = list.get(list.size() - 1).getDate();
		for (int i = 1; i < list.size() - 1; i++) {
			HistoryDomain hd = list.get(i);
			if (hd.getNumberClose() < lowestPrice && hd.getNumberClose() < list.get(i + 1).getNumberClose()
					&& hd.getNumberClose() < list.get(i - 1).getNumberClose()) {
				double balance = highestPrice - lowestPrice;
				if (balance > flowBalance) {
					flowBalance = balance;
					startDate = tempStartDate;
					endDate = tempEndDate;
				}
				lowestPrice = hd.getNumberClose();
				highestPrice = list.get(list.size() - 1).getNumberClose();
				tempStartDate = hd.getDate();
			}
			if (hd.getNumberClose() > highestPrice && hd.getNumberClose() > list.get(i + 1).getNumberClose()
					&& hd.getNumberClose() > list.get(i - 1).getNumberClose()) {
				highestPrice = hd.getNumberClose();
				tempEndDate = hd.getDate();
			}
		}
		double balance = highestPrice - lowestPrice;
		if (balance > flowBalance) {
			flowBalance = balance;
			startDate = tempStartDate;
			endDate = tempEndDate;
		}
		System.out.println(startDate + "\t" + endDate + "\t" + lowestPrice + "\t" + highestPrice);
		double flowRate = (flowBalance / lowestPrice) * 100;
		return flowRate;
	}
	
	public static double getLowestFlowPriceRate(List<HistoryDomain> list) {
		if (list == null || list.size() == 0) {
			return -999;
		}
		double lowestPrice = list.get(list.size() - 1).getNumberClose();
		double highestPrice = list.get(0).getNumberClose();
		double flowBalance = lowestPrice - highestPrice;
		String startDate = "";
		String endDate = "";
		String tempStartDate = "";
		String tempEndDate = "";
		for (int i = 1; i < list.size() - 1; i++) {
			HistoryDomain hd = list.get(i);
			if (hd.getNumberClose() < lowestPrice && hd.getNumberClose() < list.get(i + 1).getNumberClose()
					&& hd.getNumberClose() < list.get(i - 1).getNumberClose()) {
				lowestPrice = hd.getNumberClose();
				tempEndDate = hd.getDate();
			}
			if (hd.getNumberClose() > highestPrice && hd.getNumberClose() > list.get(i + 1).getNumberClose()
					&& hd.getNumberClose() > list.get(i - 1).getNumberClose()) {
				double balance = lowestPrice - highestPrice;
				if (balance < flowBalance) {
					flowBalance = balance;
					startDate = tempStartDate;
					endDate = tempEndDate;
				}
				lowestPrice = list.get(list.size() - 1).getNumberClose();
				highestPrice = hd.getNumberClose();
				tempStartDate = hd.getDate();
			}
		}
		double balance = lowestPrice - highestPrice;
		if (balance > flowBalance) {
			flowBalance = balance;
			startDate = tempStartDate;
			endDate = tempEndDate;
		}
		System.out.println(startDate + "\t" + endDate + "\t" + lowestPrice + "\t" + highestPrice);
		double flowRate = (flowBalance / lowestPrice) * 100;
		return flowRate;
	}

	public static HashMap<String, Double> getLowestPriceMap(HashMap<String, List<HistoryDomain>> historyMap) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> list = historyMap.get(code);
			if (list == null || list.size() == 0) {
				continue;
			}
			double lowestPrice = list.get(0).getNumberClose();
			for (HistoryDomain hd : list) {
				if (hd.getNumberClose() < lowestPrice) {
					lowestPrice = hd.getNumberClose();
				}
			}
			result.put(code, lowestPrice);
		}
		return result;
	}
	
	public static double getLowestPrice(List<HistoryDomain> list, String startDate, String endDate) {
		if (list == null || list.size() == 0) {
			return 0;
		}
		List<HistoryDomain> itorList = HistoryAnalyse.getTwoDayMiddleList(list, startDate, endDate);
		double lowestPrice = itorList.get(0).getNumberClose();
		for (HistoryDomain hd : itorList) {
			if (hd.getNumberClose() < lowestPrice) {
				lowestPrice = hd.getNumberClose();
			}
		}
		return lowestPrice;
	}
	
	public static double getHighestPrice(List<HistoryDomain> list, String startDate, String endDate) {
		if (list == null || list.size() == 0) {
			return 0;
		}
		List<HistoryDomain> itorList = HistoryAnalyse.getTwoDayMiddleList(list, startDate, endDate);
		if(itorList.size() == 0){
			return 0;
		}
		double hightestPrice = itorList.get(0).getNumberClose();
		for (HistoryDomain hd : itorList) {
			if (hd.getNumberClose() > hightestPrice) {
				hightestPrice = hd.getNumberClose();
			}
		}
		return hightestPrice;
	}

	public static HashMap<String, Double> getHighestPriceMap(HashMap<String, List<HistoryDomain>> historyMap) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = historyMap.keySet();
		for (String code : codes) {
			List<HistoryDomain> list = historyMap.get(code);
			if (list == null || list.size() == 0) {
				continue;
			}
			double highestPrice = list.get(0).getNumberClose();
			for (HistoryDomain hd : list) {
				if (hd.getNumberClose() > highestPrice) {
					highestPrice = hd.getNumberClose();
				}
			}
			result.put(code, highestPrice);
		}
		return result;
	}
}
