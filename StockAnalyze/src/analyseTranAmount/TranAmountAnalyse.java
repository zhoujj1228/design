package analyseTranAmount;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import Util.AnalyseUtil;
import domain.BasicInfoDomain;
import domain.HistoryDomain;

public class TranAmountAnalyse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static HashMap<String, List<HistoryDomain>> getTranAmountSlowDownStcok(
			HashMap<String, List<HistoryDomain>> orderByDateMap, HashMap<String, BasicInfoDomain> stockBasicMap) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> keys = orderByDateMap.keySet();
		for(String key : keys){
			String code = key;
			List<HistoryDomain> list = orderByDateMap.get(key);
			String timeToMarket;
			try {
				if(stockBasicMap.get(code) == null){
					continue;
				}
				timeToMarket = stockBasicMap.get(code).getTimeToMarket();
			} catch (Exception e) {
				System.out.println("err:" + code);
				e.printStackTrace();
				continue;
			}
			/*if(code.indexOf("002383") > -1){
				BasicInfoDomain bif = stockBasicMap.get(code);
				System.out.println(bif.getTimeToMarket());
			}*/
			if((timeToMarket.indexOf("2017")) > -1 || timeToMarket.equals("0")){
				continue;
			}
			if(list.size() == 0){
				continue;
			}
			//int sum = 0;
			int sumTranAmount = 0;
			
			double lastTranAmount = 0;
			for(HistoryDomain hd : list){
				//double allTranAmount = Double.parseDouble(stockBasicMap.get(code).getOutstanding()) * 100000000;
				double tranAmount = Double.parseDouble(hd.getCount());
				//double tranRate = (tranAmount/allTranAmount) * 100 * 100;
				double banlance = tranAmount - lastTranAmount;
				if(banlance < 0){
					sumTranAmount++;
				}
				/*if(tranRate > 5 && tranRate < 15){
					sum++;
				}*/
				lastTranAmount = tranAmount;
			}
			double listSize = list.size();
			double needTranAmountRate = (sumTranAmount/(listSize-1)) * 100;
			//double needRate = (sum/listSize) * 100;
			BasicInfoDomain bif = stockBasicMap.get(code);
			/*if(needRate > 80){
				if(Double.parseDouble(list.get(0).getClose()) > Double.parseDouble(list.get(list.size()-1).getClose())){
					if(!(code.indexOf("300") > -1)){
						double firstClose = Double.parseDouble(list.get(0).getClose());
						double tranAmount = Double.parseDouble(bif.getOutstanding()) * firstClose;
						double pb = Double.parseDouble(bif.getPb());
						if(tranAmount > 5 && tranAmount < 100 && pb < 10){
							result.put(code, list);
							System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket());
						}
					}
				}
			}*/
			/*if(code.equals("002383")){
				System.out.println(code + needTranAmountRate);
			}*/
			if(needTranAmountRate > 70){
				double firstClose = Double.parseDouble(list.get(0).getClose());
				double tranAmount = Double.parseDouble(bif.getOutstanding()) * firstClose;
				double pb = Double.parseDouble(bif.getPb());
				if(tranAmount > 5 && tranAmount < 100 && pb < 10){
					if(Double.parseDouble(list.get(0).getClose()) > Double.parseDouble(list.get(list.size()-1).getClose())){
						result.put(code, list);
						System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket() + "-" + needTranAmountRate);
					}
					
				}
			}
		}
		return result;
	}
	

	public static double getLastDayToAvgTranAmountRate(List<HistoryDomain> list) {
		double sumAmount = 0;
		double count = 0;
		for(int i = list.size() - 2; i > 0; i--){
			sumAmount = sumAmount + list.get(i).getNumberCount();
			count++;
		}
		double avgTranAmount = sumAmount/count;
		double amountRate = AnalyseUtil.getRate(avgTranAmount, list.get(list.size() - 1).getNumberCount());
		return amountRate;
	}
	
	public static HashMap<String, Double> getTranAmountAverage(HashMap<String, List<HistoryDomain>> historyMap){
		HashMap<String, Double> result = new HashMap<String, Double>();
		Set<String> codes = historyMap.keySet();
		for(String code : codes){
			double sumAmount = 0;
			double sumNum = 0;
			List<HistoryDomain> list = historyMap.get(code);
			for(HistoryDomain hd : list){
				sumAmount = sumAmount + hd.getNumberCount();
				sumNum++;
			}
			double average = sumAmount/sumNum;
			result.put(code, average);
		}
		return result;
	}
	
	public static double getOneStockAvgTranAmount(HashMap<String, List<HistoryDomain>> historyMap, String code){
		double sumAmount = 0;
		double sumNum = 0;
		List<HistoryDomain> list = historyMap.get(code);
		for(HistoryDomain hd : list){
			sumAmount = sumAmount + hd.getNumberCount();
			sumNum++;
		}
		double average = sumAmount/sumNum;
		return average;
	}
	
	/**
	 * 得到当前时间段的交易量和以前平均值交易量的比率在范围内的数据
	 * @param historyMap
	 * @param startRate
	 * @param endRate
	 * @param averageMap
	 * @return
	 */
	public static HashMap<String, List<HistoryDomain>> getTranAmountAverageInRate(HashMap<String, List<HistoryDomain>> historyMap, 
			double startRate, double endRate, HashMap<String, Double> averageMap){
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> codes = historyMap.keySet();
		for(String code : codes){
			double sumAmount = 0;
			double sumNum = 0;
			List<HistoryDomain> list = historyMap.get(code);
			for(HistoryDomain hd : list){
				sumAmount = sumAmount + hd.getNumberCount();
				sumNum++;
			}
			double avg = sumAmount/sumNum;
			double beforeAvg = averageMap.get(code);
			double rate = (avg/beforeAvg) * 100;
			if(rate > startRate && rate < endRate){
				result.put(code, list);
				System.out.println("getTranAmountAverageInRate:" + code + " " + rate);
			}
		}
		return null;
	}
	
	public static HashMap<String, List<HistoryDomain>> getTranRateHighStcok(
			HashMap<String, List<HistoryDomain>> orderByDateMap, HashMap<String, BasicInfoDomain> stockBasicMap) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> keys = orderByDateMap.keySet();
		for(String key : keys){
			String code = key;
			List<HistoryDomain> list = orderByDateMap.get(key);
			String timeToMarket;
			try {
				if(stockBasicMap.get(code) == null){
					continue;
				}
				timeToMarket = stockBasicMap.get(code).getTimeToMarket();
			} catch (Exception e) {
				System.out.println("err:" + code);
				e.printStackTrace();
				continue;
			}
			/*if(code.indexOf("002383") > -1){
				BasicInfoDomain bif = stockBasicMap.get(code);
				System.out.println(bif.getTimeToMarket());
			}*/
			if((timeToMarket.indexOf("2017")) > -1 || timeToMarket.equals("0")){
				continue;
			}
			if(list.size() == 0){
				continue;
			}
			int sum = 0;
			//int sumTranAmount = 0;
			
			//double lastTranAmount = 0;
			for(HistoryDomain hd : list){
				double allTranAmount = Double.parseDouble(stockBasicMap.get(code).getOutstanding()) * 100000000;
				double tranAmount = Double.parseDouble(hd.getCount());
				double tranRate = (tranAmount/allTranAmount) * 100 * 100;
				//double banlance = tranAmount - lastTranAmount;
				/*if(banlance < 0){
					sumTranAmount++;
				}*/
				if(tranRate > 5 && tranRate < 15){
					sum++;
				}
				//lastTranAmount = tranAmount;
			}
			double listSize = list.size();
			//double needTranAmountRate = (sumTranAmount/(listSize-1)) * 100;
			double needRate = (sum/listSize) * 100;
			BasicInfoDomain bif = stockBasicMap.get(code);
			if(needRate > 80){
				if(Double.parseDouble(list.get(0).getClose()) > Double.parseDouble(list.get(list.size()-1).getClose())){
					if(!(code.indexOf("300") > -1)){
						double firstClose = Double.parseDouble(list.get(0).getClose());
						double tranAmount = Double.parseDouble(bif.getOutstanding()) * firstClose;
						double pb = Double.parseDouble(bif.getPb());
						if(tranAmount > 5 && tranAmount < 100 && pb < 10){
							result.put(code, list);
							System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket());
						}
					}
				}
			}
			/*if(code.equals("002383")){
				System.out.println(code + needTranAmountRate);
			}*/
			/*if(needTranAmountRate > 70){
				double firstClose = Double.parseDouble(list.get(0).getClose());
				double tranAmount = Double.parseDouble(bif.getOutstanding()) * firstClose;
				double pb = Double.parseDouble(bif.getPb());
				if(tranAmount > 5 && tranAmount < 100 && pb < 10){
					if(Double.parseDouble(list.get(0).getClose()) > Double.parseDouble(list.get(list.size()-1).getClose())){
						result.put(code, list);
						System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket() + "-" + needTranAmountRate);
					}
					
				}
			}*/
		}
		return result;
	}
	
	
	
	public static HashMap<String, List<HistoryDomain>> getTranAmountHighPriceLowStcok(
			HashMap<String, List<HistoryDomain>> orderByDateMap, HashMap<String, BasicInfoDomain> stockBasicMap, double startRate, double endRate, boolean isSmallCompany) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> keys = orderByDateMap.keySet();
		for(String key : keys){
			String code = key;
			List<HistoryDomain> list = orderByDateMap.get(key);
			String timeToMarket;
			try {
				if(stockBasicMap.get(code) == null){
					continue;
				}
				timeToMarket = stockBasicMap.get(code).getTimeToMarket();
			} catch (Exception e) {
				System.out.println("err:" + code);
				e.printStackTrace();
				continue;
			}
			/*if(code.indexOf("002383") > -1){
				BasicInfoDomain bif = stockBasicMap.get(code);
				System.out.println(bif.getTimeToMarket());
			}*/
			if((timeToMarket.indexOf("2017")) > -1 || timeToMarket.equals("0")){
				continue;
			}
			if(list.size() == 0){
				continue;
			}
			int sumTranAmount = 0;
			
			double lastTranPrice = 0;
			double lastTranAmount = 0;
			for(HistoryDomain hd : list){
				double tranAmount = Double.parseDouble(hd.getCount());
				double amountBalance = tranAmount - lastTranAmount;
				double tranPrice = hd.getNumberClose();
				double priceBalance = tranPrice - lastTranPrice;
				if(amountBalance > 0 && priceBalance < 0){
					sumTranAmount++;
				}
				lastTranAmount = tranAmount;
				lastTranPrice = tranPrice;
			}
			double listSize = list.size();
			double needRate = (sumTranAmount/(listSize-1)) * 100;
			BasicInfoDomain bif = stockBasicMap.get(code);
			
			if(needRate > startRate && needRate < endRate){
				if(isSmallCompany){
					double firstClose = Double.parseDouble(list.get(0).getClose());
					double tranAmount = Double.parseDouble(bif.getOutstanding()) * firstClose;
					double pb = Double.parseDouble(bif.getPb());
					if(tranAmount > 5 && tranAmount < 100 && pb < 10){
						result.put(code, list);
						System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket() + "-" + needRate);
					}
				}else{
					result.put(code, list);
					System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket() + "-" + needRate);
				}
				
			}
		}
		return result;
	}
	public static HashMap<String, List<HistoryDomain>> getTranAmountHighStcok(
			HashMap<String, List<HistoryDomain>> orderByDateMap, HashMap<String, BasicInfoDomain> stockBasicMap, double startRate, double endRate, boolean isSmallCompany) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> keys = orderByDateMap.keySet();
		for(String key : keys){
			String code = key;
			List<HistoryDomain> list = orderByDateMap.get(key);
			String timeToMarket;
			try {
				if(stockBasicMap.get(code) == null){
					continue;
				}
				timeToMarket = stockBasicMap.get(code).getTimeToMarket();
			} catch (Exception e) {
				System.out.println("err:" + code);
				e.printStackTrace();
				continue;
			}
			/*if(code.indexOf("002383") > -1){
				BasicInfoDomain bif = stockBasicMap.get(code);
				System.out.println(bif.getTimeToMarket());
			}*/
			if((timeToMarket.indexOf("2017")) > -1 || timeToMarket.equals("0")){
				continue;
			}
			if(list.size() == 0){
				continue;
			}
			int sumTranAmount = 0;
			
			double lastTranAmount = 0;
			for(HistoryDomain hd : list){
				double tranAmount = Double.parseDouble(hd.getCount());
				double amountBalance = tranAmount - lastTranAmount;
				if(amountBalance > 0){
					sumTranAmount++;
				}
				lastTranAmount = tranAmount;
			}
			double listSize = list.size();
			double needRate = (sumTranAmount/(listSize-1)) * 100;
			BasicInfoDomain bif = stockBasicMap.get(code);
			
			if(needRate > startRate && needRate < endRate){
				if(isSmallCompany){
					double firstClose = Double.parseDouble(list.get(0).getClose());
					double tranAmount = Double.parseDouble(bif.getOutstanding()) * firstClose;
					double pb = Double.parseDouble(bif.getPb());
					if(tranAmount > 5 && tranAmount < 100 && pb < 10){
						result.put(code, list);
						System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket() + "-" + needRate);
					}
				}else{
					result.put(code, list);
					System.out.println("codeAndTime:" + code + "-" + stockBasicMap.get(code).getName() + "-" + stockBasicMap.get(code).getTimeToMarket() + "-" + needRate);
				}
				
			}
		}
		return result;
	}
}
