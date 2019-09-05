package analyseHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Util.Constant;
import Util.FileUtil;

public class CountNameTest {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		List<String> readByFileToList = FileUtil.readByFileToList(Constant.sdltHoldersConnectFile, "GBK");
		for(String s : readByFileToList){
			String[] split = s.split("\\$\\$");
			if(!list.contains(split[0])){
				list.add(split[0]);
				System.out.println(list.size() + split[0]);
			}
		}
		
		System.out.println(list.size());
	}

}
