package analyseBasicInfo;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Util.Constant;
import Util.FileUtil;

public class UpdateHistory {

	public static void main(String[] args) throws ParseException {
		UpdateHistory uh = new UpdateHistory();
		uh.call();
	}

	private void call() throws ParseException {
		HashMap<String, File> codeFileHisMap = new HashMap<String, File>();
		List<File> historyFileList = FileUtil.getAllFileByFile(Constant.historyFile);
		List<File> updateFileList = FileUtil.getAllFileByFile(new File(Constant.updateFilePath));
		for(File file : historyFileList){
			String name = file.getName().split("\\.")[0];
			String[] names = name.split("\\$");
			codeFileHisMap.put(names[0], file);
		}
		List<File> tempList = new ArrayList<File>();
		//顺序排序
		for(File file : updateFileList){
			String name = file.getName().split("\\.")[0];
			double time = Double.parseDouble(name);
			if(tempList.size() == 0){
				tempList.add(file);
			}else{
				int index = 0;
				for(int i = 0; i < tempList.size(); i++){
					File tempFile = tempList.get(i);
					String tempName = tempFile.getName().split("\\.")[0];
					double tempTime = Double.parseDouble(tempName);
					if(time > tempTime){
						index = i + 1;
					}
				}
				tempList.add(index, file);
			}
		}
		updateFileList = tempList;
		for(File file : updateFileList){
			String fileName = file.getName();
			SimpleDateFormat sdfNoSplit = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdfWithSplit = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdfNoSplit.parse(fileName);
			String dateStrWithSplit = sdfWithSplit.format(date);
			List<String> updateDataList = FileUtil.readByFileToList(file, "GBK");
			for(String oneData : updateDataList){
				String[] datas = oneData.split("\t");
				String code = datas[0];
				//String name = datas[1];
				String close = datas[3];
				String open = datas[4];
				String high = datas[5];
				String low = datas[6];
				String count = datas[8];
				Double doubleCountShou = Double.parseDouble(count)/100;
				
				if(codeFileHisMap.containsKey(code)){
					File historyFile = codeFileHisMap.get(code);
					String dayDataList = FileUtil.readByFileWithEncoding(historyFile, "GBK");
					if(dayDataList.indexOf(dateStrWithSplit) < 0){
						String insertStr = code + "\t" + dateStrWithSplit + "\t" + open + "\t" + close + "\t" +
									high + "\t" + low + "\t" + doubleCountShou + "\r\n";
						FileUtil.writeFileAppendWithEncode(historyFile, insertStr, "GBK");
					}
				}else{
					System.out.println("历史数据无:" + code);
				}
			}
			
		}
	}

}
