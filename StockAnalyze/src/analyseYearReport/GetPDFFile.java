package analyseYearReport;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Util.Constant;
import Util.FileUtil;
import Util.PDFUtil;
import analyseBasicInfo.BasicAnalyse;

public class GetPDFFile {

	public static void main(String[] args) {
		GetPDFFile g = new GetPDFFile();
		BasicAnalyse.initStockBasicMap();
		g.call();
	}

	private void call() {
		ExecutorService es = Executors.newFixedThreadPool(50);
		for(final String code : BasicAnalyse.stockBasicMap.keySet()) {
			
			es.execute(new Runnable() {
				
				public void run() {
					System.out.println(code);
					String createFilePath = Constant.yearReportTxtPath + code + File.separator;
					FileUtil.createDIRbyPath(Constant.yearReportTxtPath, code);
					String sourcePath = Constant.yearReportPath + code + File.separator;
					List<File> allFile = FileUtil.getAllFileByFile(new File(sourcePath));
					for(File file : allFile) {
						try {
							//System.out.println(file.getName());
							String pdfString = PDFUtil.getPDFString(file.getAbsolutePath());
							//System.out.println(pdfString);
							File createFileDeleteSource = FileUtil.createFileDeleteSource(createFilePath + file.getName() + ".txt");
							FileUtil.writeByFileWithEncoding(pdfString, createFileDeleteSource, "GBK");
						} catch (Exception e) {
							e.printStackTrace();
						}
						//System.out.println(file.getName());
					}
				}
			});
		}
		es.shutdown();
	}

}
