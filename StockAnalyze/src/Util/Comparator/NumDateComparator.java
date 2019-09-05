package Util.Comparator;

import java.util.Comparator;

public class NumDateComparator implements Comparator<String>{

	@Override
	public int compare(String date1, String date2) {
		String[] date1s = date1.split("/");
		String[] date2s = date2.split("/");
		if(Integer.parseInt(date1s[0]) > Integer.parseInt(date2s[0])){
			return 1;
		}else if(Integer.parseInt(date1s[0]) < Integer.parseInt(date2s[0])){
			return -1;
		}
		if(Integer.parseInt(date1s[1]) > Integer.parseInt(date2s[1])){
			return 1;
		}else if(Integer.parseInt(date1s[1]) < Integer.parseInt(date2s[1])){
			return -1;
		}
		if(Integer.parseInt(date1s[2]) > Integer.parseInt(date2s[2])){
			return 1;
		}else if(Integer.parseInt(date1s[2]) < Integer.parseInt(date2s[2])){
			return -1;
		}
		return 0;
	}


}
