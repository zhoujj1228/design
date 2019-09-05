package analyseYearReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import Util.Constant;
import Util.FileUtil;
import Util.HTTPDownloadUtil;
import Util.HttpClientHelper;
import Util.PatternUtil;
import analyseBasicInfo.BasicAnalyse;
import domain.BasicInfoDomain;

public class GetYearReport {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		GetYearReport gyr = new GetYearReport();
		BasicAnalyse.initStockBasicMap();
		removeProxy();
		gyr.call2();
		removeProxy();
		
		
	}

	private void call() {
		for(final String code : BasicAnalyse.stockBasicMap.keySet()) {
			ExecutorService es = Executors.newFixedThreadPool(1);
			es.execute(new Runnable() {
				
				@SuppressWarnings("static-access")
				@Override
				public void run() {
					int i = 1;
					while(true) {
						//FileUtil.createDIRbyPath(Constant.yearReportPath, code);
						String url = "http://data.eastmoney.com/notices/getdata.ashx?StockCode="+code+"&CodeType=0&PageIndex="+i+"&PageSize=50&jsObj=hBUeaQhE&SecNodeType=1&FirstNodeType=1&rt=50283720";
						String sendGet = null;
						while(true) {
							try {
								sendGet = sendGet(url, null, "GBK");
								break;
							} catch (Exception e) {
								//e.printStackTrace();
								System.out.println(e.getMessage());
								try {
									Thread.currentThread().sleep(1000);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
						String jsonStr = sendGet.substring(sendGet.indexOf("{"), sendGet.length() - 1);
						//System.out.println(jsonStr);
						JSONArray jsonArray = JSONObject.parseObject(jsonStr).getJSONArray("data");
						if(jsonArray == null) {
							break;
						}
						int size = jsonArray.size();
						if(size == 0) {
							break;
						}
						for(int j = 0; j < size; j++) {
							JSONObject jsonObject = jsonArray.getJSONObject(j);
							String reportUrl = jsonObject.getString("Url");
							//System.out.println(reportUrl);
							String reportHTML;
							while(true) {
								try {
									reportHTML = sendGet(reportUrl, null, "GBK");
									break;
								} catch (Exception e) {
									// TODO 自动生成的 catch 块
									//e.printStackTrace();
									System.out.println(e.getMessage());
									try {
										Thread.currentThread().sleep(1000);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
								}
							}
							//System.out.println(reportHTML);
							String fileName =jsonObject.getString("NOTICETITLE");
							List<List<String>> patternList = PatternUtil.getPatternList(reportHTML, "^.*(http://pdf.dfcfw.com/pdf/.*?pdf).*$", 1);
							String pdfUrl = patternList.get(0).get(1);
							fileName = fileName.replaceAll(":", "");
							//System.out.println(fileName);
							while(true) {
								try {
									HTTPDownloadUtil.downloadFile(pdfUrl, Constant.yearReportPath, code + "_" + fileName + ".pdf");
									break;
								} catch (Throwable e) {
									//e.printStackTrace();
									System.out.println(e.getMessage());
									try {
										Thread.currentThread().sleep(1000);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
						i++;
						
					}
					
				}
			});
			
			es.shutdown();
		}
	}
	
	@SuppressWarnings("static-access")
	private void call2() {
		/*System.getProperties().setProperty("http.proxyHost", "182.141.44.173");  
	    System.getProperties().setProperty("http.proxyPort", "9000"); */
		String needPattern = "^.*201[7-9].*$";
		String noNeedPattern = "^.*摘要.*$";
		File completeFile = new File(Constant.yearReportPath + "complete.txt");
		List<String> completeList = FileUtil.readByFileToList(completeFile, "GBK");
		for(final String code : BasicAnalyse.stockBasicMap.keySet()) {
			if(completeList.contains(code)) {
				continue;
			}
			int i = 1;
			while(true) {
				FileUtil.createDIRbyPath(Constant.yearReportPath, code);
				String url = "http://data.eastmoney.com/notices/getdata.ashx?StockCode="+code+"&CodeType=0&PageIndex="+i+"&PageSize=50&jsObj=hBUeaQhE&SecNodeType=1&FirstNodeType=1&rt=50283720";
				String sendGet = null;
				while(true) {
					try {
						sendGet = sendGet(url, null, "GBK");
						break;
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println(e.getMessage());
						try {
							Thread.currentThread().sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				String jsonStr = sendGet.substring(sendGet.indexOf("{"), sendGet.length() - 1);
				//System.out.println(jsonStr);
				JSONArray jsonArray = null;
				try {
					jsonArray = JSONObject.parseObject(jsonStr).getJSONArray("data");
				} catch (Exception e2) {
					e2.printStackTrace();
					System.out.println("unclosed.str:" + sendGet);
				}
				if(jsonArray == null) {
					break;
				}
				int size = jsonArray.size();
				if(size == 0) {
					break;
				}
				for(int j = 0; j < size; j++) {
					JSONObject jsonObject = jsonArray.getJSONObject(j);
					String reportUrl = jsonObject.getString("Url");
					//System.out.println(reportUrl);
					//System.out.println(reportHTML);
					String fileName =jsonObject.getString("NOTICETITLE");
					fileName = fileName.replaceAll(":", "");
					fileName = fileName.replaceAll("\\*", "");
					fileName = fileName.replaceAll("\"", "");
					fileName = fileName.replaceAll("\\|", "");
					fileName = fileName.replaceAll("\\\\", "");
					fileName = fileName.replaceAll("/", "");
					fileName = fileName.replaceAll("<", "");
					fileName = fileName.replaceAll(">", "");
					//System.out.println(fileName);
					if(!fileName.matches(needPattern)) {
						System.out.println("!fileName.matches(needPattern):" + fileName);
						continue;
					}
					if(fileName.matches(noNeedPattern)) {
						System.out.println("fileName.matches(noNeedPattern):" + fileName);
						continue;
					}
					String reportHTML;
					while(true) {
						try {
							reportHTML = sendGet(reportUrl, null, "GBK");
							break;
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							//e.printStackTrace();
							System.out.println(e.getMessage());
							try {
								Thread.currentThread().sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
					List<List<String>> patternList = PatternUtil.getPatternList(reportHTML, "^.*(http://pdf.dfcfw.com/pdf/.*?pdf).*$", 1);
					if(patternList == null || patternList.size() == 0 || patternList.get(0) == null || patternList.get(0).size() == 0) {
						System.out.println("patternList error : " + patternList);
						continue;
					}                   
					String pdfUrl = patternList.get(0).get(1);
					while(true) {
						try {
							downloadFile(pdfUrl, Constant.yearReportPath + code + "/", code + "_" + fileName + ".pdf");
							break;
						} catch (Throwable e) {
							//e.printStackTrace();
							System.out.println(e.getMessage());
							try {
								Thread.currentThread().sleep(3000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				i++;
			}
			FileUtil.writeFileAppendWithEncode(completeFile, code+"\n", "GBK");
		
		}
	}
	
	public static String downloadFile(String src_file, String dest_file, String createName) throws Throwable {

		String fileName = getFileName(src_file);
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpGet httpget = new HttpGet(src_file);
			httpget.setConfig(RequestConfig.custom() //
					.setConnectionRequestTimeout(3000) //
					.setConnectTimeout(3000) //
					.setSocketTimeout(3000) //
					.build());
			//httpget.setHeader("X-Forwarded-For", "1.1.1.1, 2.2.2.2, 3.3.3.3");
			try (CloseableHttpResponse response = httpclient.execute(httpget)) {
				org.apache.http.HttpEntity entity = response.getEntity();
				if (createName != null && !createName.equals("")) {
					fileName = createName;
				}
				File desc = new File(dest_file + File.separator + fileName);
				File folder = desc.getParentFile();
				folder.mkdirs();
				try (InputStream is = entity.getContent(); //
						OutputStream os = new FileOutputStream(desc)) {
					copy(is, os);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				throw new Throwable("文件下载失败......", e);
			}
		}
		return dest_file + File.separator + fileName;
	}
	
	private static void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			boolean isFile = false;
			while ((len = is.read(buffer)) > -1) {
				isFile = true;
				os.write(buffer, 0, len);
				os.flush();
			}
			// System.out.println(isFile);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	private static String getFileName(String src_file) {
		// TODO 自动生成的方法存根
		return src_file.substring(src_file.indexOf("/"));
	}
	
	
	public static String sendGet(String urlParam, Map<String, Object> params, String charset) {
		StringBuffer resultBuffer = null;
		// 构建请求参数
		StringBuffer sbParams = new StringBuffer();
		if (params != null && params.size() > 0) {
			for (Entry<String, Object> entry : params.entrySet()) {
				sbParams.append(entry.getKey());
				sbParams.append("=");
				sbParams.append(entry.getValue());
				sbParams.append("&");
			}
		}
		HttpURLConnection con = null;
		BufferedReader br = null;
		try {
			URL url = null;
			if (sbParams != null && sbParams.length() > 0) {
				url = new URL(urlParam + "?" + sbParams.substring(0, sbParams.length() - 1));
			} else {
				url = new URL(urlParam);
			}
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.connect();
			resultBuffer = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
			String temp;
			while ((temp = br.readLine()) != null) {
				resultBuffer.append(temp);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
					throw new RuntimeException(e);
				} finally {
					if (con != null) {
						con.disconnect();
						con = null;
					}
				}
			}
		}
		return resultBuffer.toString();
	}
	
	public static void removeProxy() {
		System.getProperties().remove("http.proxyHost");
		System.getProperties().remove("http.proxyPort");
		System.getProperties().remove("https.proxyHost");
		System.getProperties().remove("https.proxyPort");
	}
}
