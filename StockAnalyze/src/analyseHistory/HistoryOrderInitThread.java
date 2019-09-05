package analyseHistory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Util.FileUtil;
import analyseHolder.HolderAnalyse;
import domain.HistoryDomain;

public class HistoryOrderInitThread implements Runnable{
	File file;
	String startDateStr;
	String endDateStr;
	HistoryOrderInitThread(File file, String startDateStr, String endDateStr){
		this.file = file;
		this.startDateStr = startDateStr;
		this.endDateStr = endDateStr;
	}
	public void run() {
		try {
			String fileName = file.getName();
			String[] ss = fileName.split("\\$");
			String code = ss[0];
			List<HistoryDomain> list = getHistoryByFileAndTime(file, startDateStr, endDateStr);
			if(HistoryAnalyse.tempInitMap.containsKey(code)){
				System.out.println("historyMap already have code");
			}
			//System.out.println("CodeSize:" + code + "--" + list.size());
			HistoryAnalyse.tempInitMap.put(code, list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			HistoryAnalyse.completeCount.countDown();
		}
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
				addToSortList(result, hd);
			}
		}
		return result;
	}
	private static void addToSortList(List<HistoryDomain> result, HistoryDomain hd) {
		int insertIndex = 0;
		for(int i = 0; i < result.size(); i++){
			HistoryDomain itorHd = result.get(i);
			if(itorHd.getDate().compareTo(hd.getDate()) >= 0){
				break;
			}
			insertIndex++;
		}
		result.add(insertIndex, hd);
		/*for(HistoryDomain temp : result){
			System.out.println(temp.getDate());
		}*/
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
}
