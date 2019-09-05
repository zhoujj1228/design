package analyseHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import domain.BasicInfoDomain;
import domain.HolderHistoryDomain;
import analyseBasicInfo.BasicAnalyse;

public class GetHolderByEast {
	public static Hashtable<String, List<HolderHistoryDomain>> holderMap = new Hashtable<String, List<HolderHistoryDomain>>();
	static String rootPath = "E:\\test\\basic\\";
	ExecutorService ex;
	int sum =0;
	public static void main(String[] args) throws Exception {
		GetHolderByEast ahe = new GetHolderByEast();
		ahe.call();
	}

	private void call() throws Exception {
		ex = Executors.newFixedThreadPool(100);
		//String holdersFileDir = rootPath + "holders\\";
		HashMap<String, BasicInfoDomain> stockBasicMap = BasicAnalyse.initStockBasicMap();
		Set<String> codes = stockBasicMap.keySet();
		List<String> codeList = new ArrayList<String>();
		for(String code : codes) {
			codeList.add(code);
		}
		//codeList.add("600251");
		putHolerMap(codeList, stockBasicMap);
		System.out.println(sum);
		ex.shutdown();
	}

	

	private void putHolerMap(List<String> codeList, HashMap<String, BasicInfoDomain> stockBasicMap) {
		for(String code : codeList) {
			putOneStockHolder(code, holderMap, stockBasicMap);
		}
	}

	private void putOneStockHolder(String code,
			Hashtable<String, List<HolderHistoryDomain>> holderMap, HashMap<String, BasicInfoDomain> stockBasicMap) {
		BasicInfoDomain bd = stockBasicMap.get(code);
		List<String> afterMarketDateList = getAfterMarketDateList(bd.getTimeToMarket(), "yyyyMMdd");
		//List<HolderHistoryDomain> hdList = new ArrayList<HolderHistoryDomain>();
		if(afterMarketDateList.size() == 0) {
			sum++;
		}
		//System.out.println("codeSizeAndListSize:" + code + " " + afterMarketDateList.size());
		GetHolderInfoThread git = new GetHolderInfoThread(code, afterMarketDateList, holderMap);
		ex.execute(git);
	}

	private List<String> getAfterMarketDateList(String timeToMarket, String pattern) {
		List<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = sdf.parse(timeToMarket);
		} catch (ParseException e) {
			e.printStackTrace();
			try {
				date = sdf2.parse("2011-01-10");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		String marketDateWithSplit = sdf2.format(date);
		String[] marketDateStrs = marketDateWithSplit.split("-");
		String marketYear = marketDateStrs[0];
		String marketMonth = marketDateStrs[1];
		//String marketDay = marketDateStrs[2];
		int iYear = Integer.parseInt(marketYear);
		int iMonth = Integer.parseInt(marketMonth);
		//int iDay = Integer.parseInt(marketDay);
		/*Calendar cl = Calendar.getInstance();
		cl.setTime(new Date());*/
		String[] itorMonthAndDate = {"/3/31","/6/30","/9/30","/12/31"};
		int index = 0;
		if(iMonth > 9){
			index = 3;
		}else if(iMonth > 6){
			index = 2;
		}else if(iMonth > 3){
			index = 1;
		}
		long todayTamp = new Date().getTime();
		long itorDateTamp = 0;
		out:while(true){
			for(; index < itorMonthAndDate.length; index++){
				String itorDateStr = iYear + itorMonthAndDate[index];
				Date itorDate = null;
				try {
					itorDate = sdf3.parse(itorDateStr);
				} catch (ParseException e) {
					e.printStackTrace();
					break out;
				}
				itorDateTamp = itorDate.getTime();
				if(itorDateTamp > todayTamp){
					break out;
				}
				result.add(itorDateStr);
			}
			index = 0;
			iYear++;
		}
		
		return result;
	}

	

	

}


