package Util;

import java.io.File;

public class Constant {
	public static String rootPath = "C:\\Users\\Administrator\\Desktop\\周家炬\\股票\\";
	//public static String rootPath = "E:\\test\\";
	public static File basicInfoFile = new File(rootPath + "basic\\basic.txt");
	public static File concept1File = new File(rootPath + "basic\\concept1.txt");
	public static File concept2File = new File(rootPath + "basic\\concept2.txt");
	public static File historyFile = new File(rootPath + "history\\");
	public static String updateFilePath = rootPath + "updateFiles\\";
	public static String holdersFileDir = rootPath + "holders\\";
	public static File sdltHoldersDataFile = new File(rootPath + "sdltHolders\\holders\\");
	public static File sdltHoldersConnectFile = new File(rootPath + "sdltHolders\\sdltHoldersConnect.txt");
	public static String sdltHoldersConnectFilePath = rootPath + "sdltHolders\\connect\\";
	public static String sdltHoldersPath = rootPath + "sdltHolders\\";
	public static String historyDatePattern = "yyyy-MM-dd";
	public static String encode = "GBK";
	public static String today = "2017/8/30";
	public static String oldDay = "2011/8/30";
	public static File profitFile = new File(rootPath + "profit\\");
	public static String concept3FilePath = rootPath + "basic\\concept3.txt";
	public static String jjHoldersFileDir = rootPath + "jjholders\\";
	public static File jjHoldersDataFile = new File(jjHoldersFileDir);
	public static String businessManagePath = rootPath + "business\\";
	public static String concept4FilePath = rootPath + "basic\\concept4\\";
	public static String yearReportTxtPath = rootPath + "yearReportTxt\\";
	public static String yearReportPath = rootPath + "yearReport\\";
	public static String patentPath = rootPath + "patent\\";
	public static String gdNumFileDir = rootPath + "gdNum\\";
	public static String goodStockFilePath = rootPath + "basic\\优质股.txt";
	public static String profitStockFilePath = rootPath + "basic\\已知优质股.txt";
}
