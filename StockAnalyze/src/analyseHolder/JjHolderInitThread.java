package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.FileUtil;
import domain.HolderDomain;
import domain.HolderHistoryDomain;

public class JjHolderInitThread implements Runnable{
	File file;
	JjHolderInitThread(File file){
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
				if(ss.length < 3){
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
				HolderDomain hd = new HolderDomain(date, name, amount, rate, "", "");
				hd.setCode(code);
				if(dateHolderMap.containsKey(date)){
					hhd = dateHolderMap.get(date);
				}else{
					hhd = new HolderHistoryDomain();
				}
				hhd.getJjHolders().add(hd);
				dateHolderMap.put(date, hhd);
				lastStr = "";
			}
			HolderAnalyse.jjHolderMap.put(code, dateHolderMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			HolderAnalyse.completeCount.countDown();
		}
	}
}
