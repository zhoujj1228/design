package jUnitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Util.Constant;
import analyseBasicInfo.BasicAnalyse;
import analyseHistory.HistoryAnalyse;
import analyseHolder.HolderAnalyse;

public class HolderAnalyseTest {

	@Before
	public void setUp() throws Exception {
		HolderAnalyse.initJjHolderMap(Constant.jjHoldersDataFile);
		HolderAnalyse.initJjDateCodeMap(HolderAnalyse.jjHolderMap);
		HistoryAnalyse.initHistoryMap("2016-09-01", "2017-09-30");
		BasicAnalyse.initStockBasicMap();
	}

	@Test
	public void testGetHolderStockBestProfitRate() {
		double holderStockBestProfitRate = HolderAnalyse.getHolderStockHighestProfitRate("’‘ºŒ‹∞", "600433", "2016/9/30", "2017/3/31", HistoryAnalyse.historyMap);
		System.out.println(holderStockBestProfitRate);
	}

}
