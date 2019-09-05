package analysePatent;

import java.util.List;

import Util.Constant;
import Util.HTTPDownloadUtil;
import Util.HttpClientHelper;
import Util.PatternUtil;

public class GetPatent {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		GetPatent gp = new GetPatent();
		gp.call();
	}

	private void call() {
		String url = "http://www.sipo.gov.cn/tjxx/zltjjb/";
		String sendGet = HttpClientHelper.sendGet(url, null, "GBK");
		List<List<String>> patternList = PatternUtil.getPatternList(sendGet, "<a href=\"\\.(/[0-9]{6}/t[0-9]{8}_.*?\\.html)\" target=\"_blank\">(.*?)</a>", 2);
		for (List<String> list : patternList) {
			String dataUrl = "http://www.sipo.gov.cn/tjxx/zltjjb" + list.get(1);
			String pdfHTML = HttpClientHelper.sendGet(dataUrl, null, "GBK");
			String pdfHTMLUrlPrefix = dataUrl.substring(0, dataUrl.lastIndexOf("/"));
			List<List<String>> pdfList = PatternUtil.getPatternList(pdfHTML, "<a href=\"\\.(/.*?.pdf)\">(.*?)</a>", 2);
			String name = pdfList.get(0).get(2) + ".pdf";
			name = name.replaceAll(" ", "");
			name = name.replaceAll(":", "");
			name = name.replaceAll("\\*", "");
			name = name.replaceAll("\"", "");
			name = name.replaceAll("\\|", "");
			name = name.replaceAll("\\\\", "");
			name = name.replaceAll("/", "");
			name = name.replaceAll("<", "");
			name = name.replaceAll(">", "");
			String pdfFileUrl = pdfHTMLUrlPrefix + pdfList.get(0).get(1);
			// System.out.println(pdfFileUrl + name);
			while (true) {
				try {
					HTTPDownloadUtil.downloadFile(pdfFileUrl, Constant.patentPath, name);
					break;
				} catch (Throwable e) {
					// TODO 自动生成的 catch 块
					// e.printStackTrace();
				}
			}
		}
		
		for (int i = 1; i < 6; i++) {
			String urlNext = "http://www.sipo.gov.cn/tjxx/zltjjb/index_" + i + ".html";
			String sendGet1 = HttpClientHelper.sendGet(urlNext, null, "GBK");
			List<List<String>> patternList1 = PatternUtil.getPatternList(sendGet1,
					"<a href=\"\\.(/[0-9]{6}/t[0-9]{8}_.*?\\.html)\" target=\"_blank\">(.*?)</a>", 2);
			for (List<String> list : patternList1) {
				String dataUrl = "http://www.sipo.gov.cn/tjxx/zltjjb" + list.get(1);
				String pdfHTML = HttpClientHelper.sendGet(dataUrl, null, "GBK");
				String pdfHTMLUrlPrefix = dataUrl.substring(0, dataUrl.lastIndexOf("/"));
				List<List<String>> pdfList = PatternUtil.getPatternList(pdfHTML, "<a href=\"\\.(/.*?.pdf)\">(.*?)</a>",2);
				if (pdfList == null || pdfList.size() == 0 || pdfList.get(0) == null || pdfList.get(0).size() == 0) {
					System.out.println("patternList error : " + pdfList);
					continue;
				}
				String pdfFileUrl = pdfHTMLUrlPrefix + pdfList.get(0).get(1);
				String name = pdfList.get(0).get(2) + ".pdf";
				name = name.replaceAll(" ", "");
				name = name.replaceAll(":", "");
				name = name.replaceAll("\\*", "");
				name = name.replaceAll("\"", "");
				name = name.replaceAll("\\|", "");
				name = name.replaceAll("\\\\", "");
				name = name.replaceAll("/", "");
				name = name.replaceAll("<", "");
				name = name.replaceAll(">", "");
				// System.out.println(pdfFileUrl + name);
				while (true) {
					try {
						HTTPDownloadUtil.downloadFile(pdfFileUrl, Constant.patentPath, name);
						break;
					} catch (Throwable e) {
						// TODO 自动生成的 catch 块
						// e.printStackTrace();
					}
				}
			}

		}
		
	}

}
