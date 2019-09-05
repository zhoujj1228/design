package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.FileUtil;
import domain.HolderDomain;
import domain.HolderHistoryDomain;

public class HolderDateStockInitThread implements Runnable{
	static Object lockObj = new Object();
	File file;
	HolderDateStockInitThread(File file){
		this.file = file;
	}

	public void run() {
		try {
			List<String> list = FileUtil.readByFileToList(file, "GBK");
			String code = file.getName().split("\\.")[0];
			HolderHistoryDomain hhd;
			String lastStr = "";
			for(String s : list){
				s = lastStr + s;
				String[] ss = s.split("\t");
				if(ss.length < 6){
					//System.out.println("ss.length < 6:" + code + " " + s);
					lastStr = s;
					continue;
				}
				if(!lastStr.equals("")){
					//System.out.println("lastStr to new Str:" + s);
				}
				String date = ss[0];
				String name = ss[1];
				String amount = ss[2];
				String rate = ss[3];
				String upDown = ss[4];
				String change = ss[5];
				synchronized(lockObj){
					HashMap<String, List<HolderDomain>> dateStockMap;;
					if(HolderAnalyse.holderDateStockMap.containsKey(name)){
						dateStockMap =  HolderAnalyse.holderDateStockMap.get(name);
					}else{
						dateStockMap = new HashMap<String, List<HolderDomain>>();
					}
					List<HolderDomain> holderData;
					if(dateStockMap.containsKey(date)){
						holderData = dateStockMap.get(date);
					}else{
						holderData = new ArrayList<HolderDomain>();
					}
					HolderDomain hd = new HolderDomain(code, date, name, amount, rate, upDown, change);
					holderData.add(hd);
					dateStockMap.put(date,holderData);
					HolderAnalyse.holderDateStockMap.put(name, dateStockMap);
				}
				lastStr = "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			HolderAnalyse.completeCount.countDown();
		}
	}
}
