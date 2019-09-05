package analyseConcept;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import Util.Constant;
import Util.FileUtil;
import Util.HttpClientHelper;

public class GetConceptByEast {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		getConceptList();
	}
	public static void getConceptList() {
		String rsp = HttpClientHelper.sendGet("http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=C._BKGN&sty=FPGBKI&st=c&sr=-1&p=1&ps=5000&cb=&js=var%20BKCache=[(x)]&token=7bc05d0d4c3c22ef9fca8c2a912d779c&v=0.892006215673572",
				null, "UTF-8");
		//rsp = rsp.replaceAll("var BKCache=", "");
		JSONArray conceptArray = JSONObject.parseArray(rsp);
		File file = FileUtil.createFileDeleteSource(Constant.concept3FilePath);
		for(int i = 0; i < conceptArray.size(); i++) {
			String conceptData = conceptArray.getString(i);
			//System.out.println(conceptData);
			String[] split = conceptData.split(",");
			String code = split[1];
			String name = split[2];
			//System.out.println(code + name);
			List<String> list = new ArrayList<String>();
			list.add(name);
			try {
				list.addAll(getConceptStocksList(code));
				writeByFileAppendWithEncodeByList(list, file, "GBK");
			} catch (Exception e) {
				e.printStackTrace();
				i--;
			}
		}
	}
	
	private static List<String> getConceptStocksList(String cCode) {
		List<String> list = new ArrayList<String>();
		String rsp = HttpClientHelper.sendGet("http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=C."+cCode+"1&sty=FCOIATA&sortType=C&sortRule=-1&page=1&pageSize=200&js=var%20quote_123={rank:[(x)],pages:(pc)}&token=7bc05d0d4c3c22ef9fca8c2a912d779c&jsName=quote_123&_g=0.8089710815089575",
				null, "UTF-8");
		rsp = rsp.replaceAll("var quote_123=", "");
		JSONObject data = JSONObject.parseObject(rsp);
		JSONArray stockArray = data.getJSONArray("rank");
		for(int i = 0; i < stockArray.size(); i++) {
			String conceptData = stockArray.getString(i);
			//System.out.println(conceptData);
			String[] split = conceptData.split(",");
			String code = split[1];
			//String name = split[2];
			//System.out.println(code + name);
			list.add(code);
		}
		return list;
	}
	public static void writeByFileAppendWithEncodeByList(List<String> list ,File file ,String encode){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			for(String s : list) {
				s = s + "\t";
				fos.write(s.getBytes(encode));
			}
			fos.write("\n".getBytes(encode));
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
