package Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HTTPDownloadUtil {

	public static void main(String[] args) throws Throwable {
		// TODO 自动生成的方法存根
		downloadFile("http://pdf.dfcfw.com/pdf/H2_AN201710200966321652_1.pdf", "E:/test", "a.pdf");
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
}
