package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.FileUtil;
import domain.HolderDomain;
import domain.HolderHistoryDomain;

public class HolderInitThread implements Runnable{
	File file;
	HolderInitThread(File file){
		this.file = file;
	}

	public void run() {
		try {
			HashMap<String, HolderHistoryDomain> dateHolderMap = new HashMap<String, HolderHistoryDomain>();
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
				HolderDomain hd = new HolderDomain(name, amount, rate, upDown, change);
				if(dateHolderMap.containsKey(date)){
					hhd = dateHolderMap.get(date);
				}else{
					hhd = new HolderHistoryDomain();
				}
				hhd.getLtgdList().add(hd);
				dateHolderMap.put(date, hhd);
				lastStr = "";
			}
			HolderAnalyse.holderMap.put(code, dateHolderMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			HolderAnalyse.completeCount.countDown();
		}
	}
}
