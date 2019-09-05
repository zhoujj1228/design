package analyseHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.HistoryDomain;

public class HistoryOrderThread implements Runnable{
	List<String> codes = new ArrayList<String>();
	HashMap<String, List<HistoryDomain>> result;
	HashMap<String, List<HistoryDomain>> historyMap;
	boolean isComplete = false;
	HistoryOrderThread(HashMap<String, List<HistoryDomain>> result, HashMap<String, List<HistoryDomain>> historyMap){
		this.result = result;
		this.historyMap = historyMap;
	}
	public void run() {
		for(String key : codes){
			/*if(key.indexOf("002383") > -1){
				System.out.println(key);
			}*/
			List<HistoryDomain> list = historyMap.get(key);
			List<HistoryDomain> sourceList = new ArrayList<HistoryDomain>();
			for(HistoryDomain hd : list){
				sourceList.add(hd);
			}
			sourceList = HistoryAnalyse.sortWithQuickByDate(sourceList);
			result.put(key, sourceList);
		}
		isComplete = true;
	}

}
