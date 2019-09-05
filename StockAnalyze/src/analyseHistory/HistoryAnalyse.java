package analyseHistory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Util.AnalyseUtil;
import Util.Constant;
import Util.FileUtil;
import domain.HistoryDomain;

public class HistoryAnalyse {
	public static CountDownLatch completeCount;
	public static ExecutorService ex = Executors.newFixedThreadPool(50);
	public static HashMap<String, List<HistoryDomain>> historyMap;
	public static HashMap<String, List<HistoryDomain>> historyOrderMap;
	public static Hashtable<String, List<HistoryDomain>> tempInitMap = new Hashtable<String, List<HistoryDomain>>();
	public static void main(String[] args) {
		historyOrderMap = getHistoryOrderListMapByDirWithThread(Constant.historyFile, "2017-08-01", "2017-09-22");
		System.out.println(historyMap.size());
	}
	

	public static HashMap<String, List<HistoryDomain>> initHistoryMap(String startDate, String endDate){
		historyMap = getHistoryListMapByDirWithThread(Constant.historyFile, startDate, endDate);
		return historyMap;
	}
	
	public static HashMap<String, List<HistoryDomain>> initHistoryOrderMap(String startDate, String endDate){
		historyOrderMap = getHistoryOrderListMapByDirWithThread(Constant.historyFile, startDate, endDate);
		return historyOrderMap;
	}
	
	public static HashMap<String, List<HistoryDomain>> getHisMapByStartAndEndDateMap(HashMap<String, List<HistoryDomain>> historyMap, HashMap<String, String> startDateMap, HashMap<String, String> endDateMap){
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		for(String code : historyMap.keySet()){
			String startDate = startDateMap.get(code);
			String endDate = endDateMap.get(code);
			List<HistoryDomain> list = historyMap.get(code);
			List<HistoryDomain> needList = new ArrayList<HistoryDomain>();
			for(HistoryDomain hd : list){
				String date = hd.getDate();
				boolean isNeedData = getIsTwoTimeMiddle(date, startDate, endDate, "yyyy-MM-dd");
				if(isNeedData){
					needList.add(hd);
				}
			}
			System.out.println("getHisMapByStartAndEndDateMap:" + code + "\t" + startDate + "\t" +  endDate);
			result.put(code, needList);
		}
		return result;
	}
	
	
	
	public static HashMap<String, List<HistoryDomain>> getCodeHisMap(HashMap<String, List<HistoryDomain>> historyMap, Collection<String> con){
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		Set<String> keySet = historyMap.keySet();
		for(String code : keySet){
			if(con.contains(code)){
				result.put(code, historyMap.get(code));
			}
		}
		return result;
	}
	
	public static HashMap<String, List<HistoryDomain>> getHistoryListMapByDir(File historyFile, String startDateStr, String endDateStr) {
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		List<File> files = FileUtil.getAllFileByFile(historyFile);
		for(File file : files){
			String fileName = file.getName();
			String[] ss = fileName.split("\\$");
			String code = ss[0];
			List<HistoryDomain> list = getHistoryByFileAndTime(file, startDateStr, endDateStr);
			if(result.containsKey(code)){
				System.out.println("historyMap already have code");
			}
			//System.out.println("CodeSize:" + code + "--" + list.size());
			result.put(code, list);
		}
		return result;
	}
	
	private static HashMap<String, List<HistoryDomain>> getHistoryListMapByDirWithThread(File historyFile, String startDateStr, String endDateStr) {
		HashMap<String, List<HistoryDomain>> result = null;
		List<File> files = FileUtil.getAllFileByFile(historyFile);
		completeCount = new CountDownLatch(files.size());
		for(File file : files){
			HistoryInitThread hit = new HistoryInitThread(file, startDateStr, endDateStr);
			ex.execute(hit);
		}
		while(true){
			if(completeCount.getCount() == 0){
				ex.shutdown();
				result = new HashMap<String, List<HistoryDomain>>(tempInitMap);
				break;
			}
		}
		return result;
	}
	
	private static HashMap<String, List<HistoryDomain>> getHistoryOrderListMapByDirWithThread(File historyFile, String startDateStr, String endDateStr) {
		HashMap<String, List<HistoryDomain>> result = null;
		List<File> files = FileUtil.getAllFileByFile(historyFile);
		completeCount = new CountDownLatch(files.size());
		for(File file : files){
			HistoryOrderInitThread hit = new HistoryOrderInitThread(file, startDateStr, endDateStr);
			ex.execute(hit);
		}
		while(true){
			if(completeCount.getCount() == 0){
				ex.shutdown();
				result = new HashMap<String, List<HistoryDomain>>(tempInitMap);
				break;
			}
		}
		return result;
	}
	
	private static List<HistoryDomain> getHistoryByFileAndTime(File file, String startDateStr, String endDateStr) {
		List<HistoryDomain> result = new ArrayList<HistoryDomain>();
		List<String> allHistory = FileUtil.readByFileToList(file, "UTF-8");
		for(String s : allHistory){
			String[] ss = s.split("	");
			if(ss.length < 2){
				System.out.println("ss.length < 2" + file.getName() + s);
				continue;
			}
			//String code = ss[0];
			String date = ss[1];
			String open = ss[2];
			String close = ss[3];
			String high = ss[4];
			String low = ss[5];
			String count = ss[6];
			boolean isNeedData = getIsTwoTimeMiddle(date, startDateStr, endDateStr, "yyyy-MM-dd");
			if(isNeedData){
				HistoryDomain hd = new HistoryDomain(date, open, close, high, low, count);
				result.add(hd);
			}
		}
		return result;
	}
	
	public static HashMap<String, List<HistoryDomain>> getOrderDateMap(HashMap<String, List<HistoryDomain>> historyMap, String startDateStr, String endDateStr) {
		Set<String> keys = historyMap.keySet();
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		for(String key : keys){
			/*if(key.indexOf("002383") > -1){
				System.out.println(key);
			}*/
			List<HistoryDomain> list = historyMap.get(key);
			List<HistoryDomain> sourceList = new ArrayList<HistoryDomain>();
			for(HistoryDomain hd : list){
				String date = hd.getDate();
				boolean isNeedData = getIsTwoTimeMiddle(date, startDateStr, endDateStr, "yyyy-MM-dd");
				if(isNeedData){
					sourceList.add(hd);
				}
			}
			sourceList = sortWithQuickByDate(sourceList);
			result.put(key, sourceList);
		}
		/*for(HistoryDomain hd : historyMap.get("603999")){
			System.out.println(hd.getDate());
		}*/
		return result;
	}
	
	public static HashMap<String, List<HistoryDomain>> getOrderDateMap(HashMap<String, List<HistoryDomain>> historyMap) {
		Set<String> keys = historyMap.keySet();
		System.out.println("getOrderDateMap:" + AnalyseUtil.getTime());
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		for(String key : keys){
			/*if(key.indexOf("002383") > -1){
				System.out.println(key);
			}*/
			List<HistoryDomain> list = historyMap.get(key);
			List<HistoryDomain> sourceList = new ArrayList<HistoryDomain>();
			for(HistoryDomain hd : list){
				sourceList.add(hd);
			}
			sourceList = sortWithQuickByDate(sourceList);
			result.put(key, sourceList);
		}
		/*for(HistoryDomain hd : historyMap.get("603999")){
			System.out.println(hd.getDate());
		}*/
		System.out.println("getOrderDateMap:" + AnalyseUtil.getTime());
		return result;
	}
	
	/**
	 * 由于计算密集,多线程反而更慢
	 * @param historyMap
	 * @return
	 */
	public static HashMap<String, List<HistoryDomain>> getOrderDateWithThreadMap(final HashMap<String, List<HistoryDomain>> historyMap) {
		Set<String> keys = historyMap.keySet();
		final HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		ExecutorService hisEX = Executors.newFixedThreadPool(50);
		final CountDownLatch completeHisInitCount = new CountDownLatch(keys.size());
		System.out.println("getOrderDateWithThreadMap" + AnalyseUtil.getTime());
		for(final String key : keys){
			hisEX.execute(new Runnable() {
				
				public void run() {

					List<HistoryDomain> list = historyMap.get(key);
					List<HistoryDomain> sourceList = new ArrayList<HistoryDomain>();
					for(HistoryDomain hd : list){
						sourceList.add(hd);
					}
					sourceList = sortWithQuickByDate(sourceList);
					result.put(key, sourceList);
					completeHisInitCount.countDown();
				}
			});
			/*if(key.indexOf("002383") > -1){
				System.out.println(key);
			}*/
		}
		while(true){
			if(completeHisInitCount.getCount() == 0){
				hisEX.shutdown();
				break;
			}
		}
		System.out.println("getOrderDateWithThreadMap" + AnalyseUtil.getTime());
		/*for(HistoryDomain hd : historyMap.get("603999")){
			System.out.println(hd.getDate());
		}*/
		return result;
	}
	
	
	/**
	 * 
	 * @param historyMap
	 * @return
	 */
	public static HashMap<String, List<HistoryDomain>> getOrderDateWith2ThreadMap(final HashMap<String, List<HistoryDomain>> historyMap) {
		Set<String> keys = historyMap.keySet();
		System.out.println("getOrderDateMap:" + AnalyseUtil.getTime());
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		HistoryOrderThread hot1 = new HistoryOrderThread(result, historyMap);
		HistoryOrderThread hot2 = new HistoryOrderThread(result, historyMap);
		for(String key : keys){
			/*if(key.indexOf("002383") > -1){
				System.out.println(key);
			}*/
			if(key.startsWith("6")){
				hot1.codes.add(key);
			}else{
				hot2.codes.add(key);
			}
		}
		Thread t1 = new Thread(hot1);
		Thread t2 = new Thread(hot2);
		t1.start();
		t2.start();
		/*for(HistoryDomain hd : historyMap.get("603999")){
			System.out.println(hd.getDate());
		}*/
		while(t1.getState() != Thread.State.TERMINATED || t1.getState() != Thread.State.TERMINATED){
			//System.out.println(t1.getState() + " " + t2.getState());
		}
		System.out.println("getOrderDateMap:" + result.size() + AnalyseUtil.getTime());
		return result;
	}
	
	public static HashMap<String, List<HistoryDomain>> getTwoDayMiddleMap(HashMap<String, List<HistoryDomain>> historyMap, String startDateStr, String endDateStr) {
		Set<String> keys = historyMap.keySet();
		HashMap<String, List<HistoryDomain>> result = new HashMap<String, List<HistoryDomain>>();
		for(String key : keys){
			/*if(key.indexOf("002383") > -1){
				System.out.println(key);
			}*/
			List<HistoryDomain> list = historyMap.get(key);
			List<HistoryDomain> sourceList = new ArrayList<HistoryDomain>();
			for(HistoryDomain hd : list){
				String date = hd.getDate();
				boolean isNeedData = getIsTwoTimeMiddle(date, startDateStr, endDateStr, "yyyy-MM-dd");
				if(isNeedData){
					sourceList.add(hd);
				}
			}
			//sourceList = sortWithQuickByDate(sourceList);
			result.put(key, sourceList);
		}
		/*for(HistoryDomain hd : historyMap.get("603999")){
			System.out.println(hd.getDate());
		}*/
		return result;
	}
	
	public static List<HistoryDomain> getTwoDayMiddleList(List<HistoryDomain> list, String startDateStr,
			String endDateStr) {
		List<HistoryDomain> result = new ArrayList<HistoryDomain>();
		for (HistoryDomain hd : list) {
			String date = hd.getDate();
			boolean isNeedData = getIsTwoTimeMiddle(date, startDateStr, endDateStr, "yyyy-MM-dd");
			if (isNeedData) {
				result.add(hd);
			}
		}
		return result;
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
	public static List<HistoryDomain> sortWithQuickByDate(List<HistoryDomain> sourceList){
		List<HistoryDomain> result = new ArrayList<HistoryDomain>();
		List<HistoryDomain> headList = new ArrayList<HistoryDomain>();
		List<HistoryDomain> tailList = new ArrayList<HistoryDomain>();
		if(sourceList.size() < 2){
			return sourceList;
		}
		HistoryDomain first = sourceList.get(0);
		long firstDateTamp = AnalyseUtil.parseDate(first.getDate(), "yyyy-MM-dd").getTime();
		for(int i = 1; i < sourceList.size(); i++){
			HistoryDomain hd = sourceList.get(i);
			long dateTamp = AnalyseUtil.parseDate(hd.getDate(), "yyyy-MM-dd").getTime();
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
	
	
	
	public static HashMap<String, HistoryDomain> getSomeDayMap(HashMap<String, List<HistoryDomain>> historyMap, String date) {
		HashMap<String, HistoryDomain> result = new HashMap<String, HistoryDomain>();
		Set<String> keys = historyMap.keySet();
		for(String key : keys){
			List<HistoryDomain> list = historyMap.get(key);
			for(HistoryDomain hd : list){
				if(date.equals(hd.getDate())){
					result.put(key, hd);
				}
			}
			
		}
		return result;
	}

}
