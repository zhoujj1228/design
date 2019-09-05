package Util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Util.domain.ExcelSheetDomain;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class FileUtil {
	public static void main(String[] args){
		FileUtil.createDIRbyPath("D:/Test/", "esb/classes/com/dc/esb/zjc/exception/");
	}
	
	public static void writeByFileAppendWithEncodeByList(List<String> list ,File file ,String encode){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			for(String s : list) {
				fos.write(s.getBytes(encode));
				fos.write("\n".getBytes(encode));
			}
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
	
	public static boolean deleteAllSubFile(File file){
		File[] listFiles = file.listFiles();
		for(int i = 0; i < listFiles.length; i++){
			if(listFiles[i].isFile()){
				listFiles[i].delete();
			}else{
				deleteAllSubFile(listFiles[i]);
				listFiles[i].delete();
			}
		}
		return true;
	}
	
	public static boolean deleteAllFromDir(File tempFileDir){
		File[] listFiles = tempFileDir.listFiles();
		for(int i = 0; i < listFiles.length; i++){
			if(listFiles[i].isFile()){
				listFiles[i].delete();
			}else{
				deleteAllFromDir(listFiles[i]);
			}
		}
		tempFileDir.delete();
		return true;
	}
	
	/**
	 * 读取文件内容
	 * @param file
	 * @return 文件内容（String）
	 */
	public static boolean copyFile(File fromFile, File toFile){
		try {
			FileInputStream input = new FileInputStream(fromFile);
			FileOutputStream output = new FileOutputStream(toFile);
			int in = input.read();
			while(in != -1){
				output.write(in);
				in = input.read();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String readByFile(File file){
		String result = null;
		FileInputStream is =null;
		BufferedReader bd =null;
		try {
			is = new FileInputStream(file);
			bd = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String s = bd.readLine();
			while(s != null){
				sb.append(s);
				s = bd.readLine();
			}
			result = sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(bd != null){
					bd.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据路径读取文件内容
	 * @param path
	 * @return
	 */
	public static String readByPath(String path){
		File file = new File(path);
		return readByFile(file);
	}
	
	public static boolean writeByFileWithNoEncoding(String data ,File file){
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(data);
			fw.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		} finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static boolean writeByMapWithEncoding(Map<String, List<String>> dataMap ,File file ,String encoding){
		createFile(file.getAbsolutePath());
		OutputStreamWriter fw = null;
		try {
			fw = new OutputStreamWriter(new FileOutputStream(file,true), encoding);
			Set<String> keySet = dataMap.keySet();
			for(String s : keySet){
				fw.write(s+"\t");
				List<String> list = dataMap.get(s);
				for(String s1 : list){
					fw.write(s1+"\t");
				}
				fw.write("\n");
			}
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static boolean writeByFileWithEncoding(String data ,File file ,String encoding){
		OutputStreamWriter fw = null;
		try {
			fw = new OutputStreamWriter(new FileOutputStream(file), encoding);
			fw.write(data);
			fw.flush();
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		} finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	/**
	 * 读取文件内容并将有标志的所在行包装为List返回
	 * @param file 文件
	 * @param checkFlag 检查字段
	 * @return 有标志的所有行list
	 */
	public static List<String> checkFileFlag(File file, String checkFlag){

		List<String> result = null;
		FileInputStream is =null;
		BufferedReader bd =null;
		try {
			is = new FileInputStream(file);
			bd = new BufferedReader(new InputStreamReader(is));
			String s = bd.readLine();
			result = new ArrayList<String>();
			while(s != null){
				if(s.indexOf(checkFlag) > 0){
					result.add(s);
				}
				s = bd.readLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(bd != null){
					bd.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	
	}
	/**
	 * 获得某个目录下所有文件
	 * @param file 文件
	 * @return 所有文件的集合list<FILE>
	 */
	public static List<File> getAllFileByFile(File file){
		List<File> list = new ArrayList<File>();
		if(file.isDirectory()){
			String[] fileList = file.list();
			String parentPath = file.getPath();
			for(int i = 0; i < fileList.length; i++){
				
				list.addAll(getAllFileByFile(new File(parentPath+"/"+fileList[i])));
			}
		}else if(file.isFile()){
			list.add(file);
		}
		return list;
	}
	
	/**
	 * 读取文件内容
	 * @param file 文件
	 * @return 文件内容（List）
	 * 
	 */
	public static List<String> readByFileToList(File file, String charSet){
		List<String> result = null;
		FileInputStream is =null;
		BufferedReader bd =null;
		try {
			is = new FileInputStream(file);
			if(charSet == null){
				bd = new BufferedReader(new InputStreamReader(is));
			}else{
				bd = new BufferedReader(new InputStreamReader(is, charSet));
			}
			
			List<String> list = new ArrayList<String>();
			String s = bd.readLine();
			while(s != null){
				list.add(s);
				s = bd.readLine();
			}
			result = list;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(bd != null){
					bd.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据excel文件路径得到一个可写的Workbook
	 * @param excelPath 文件路径
	 * @return 一个可写的Workbook
	 */
	public static WritableWorkbook getWriteExcel(String excelPath){
		InputStream is = null;
		Workbook readwb;
		WritableWorkbook writewb = null;
		try {
			is = new FileInputStream(excelPath);
			readwb = Workbook.getWorkbook(is);
			writewb = Workbook.createWorkbook(new File(excelPath), readwb);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		/*后续用法如下
		writewb = Workbook.createWorkbook(new File(excelPath));
		writewb.createSheet("接口清单", 0);
		WritableSheet sheet = writewb.getSheet(0);
		Label label = new Label(lie, hang, data);
		sheet.addCell(label);
		wwb.write();
		wwb.close();*/
		return writewb;
	}
	
	/**
	 * 根据excel文件路径得到一个可写的Workbook
	 * @param excelPath 文件路径
	 * @return 一个可写的Workbook
	 */
	public static boolean writeExcelByList(String excelPath, List<List<String>> lists){
		InputStream is = null;
		Workbook readwb;
		WritableWorkbook writewb = null;
		try {
			is = new FileInputStream(excelPath);
			readwb = Workbook.getWorkbook(is);
			writewb = Workbook.createWorkbook(new File(excelPath), readwb);
			writewb = Workbook.createWorkbook(new File(excelPath));
			writewb.createSheet("sheet1", 0);
			WritableSheet sheet = writewb.getSheet(0);
			for(int i = 0; i < lists.size(); i++){
				List<String> list = lists.get(i);
				for(int j = 0; j < list.size(); j++){
					Label label = new Label(j, i, list.get(j));
					sheet.addCell(label);
				}
			}
			writewb.write();
			writewb.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param is	输入流
	 * @param length	读取长度
	 * @return	读取字节数组
	 * @throws IOException
	 */
	public static byte[] readLenContent(InputStream is, int length)
			throws IOException {
		
		int count = 0;
		int offset = 0;

		byte[] retData = new byte[length];

		while ((count = is.read(retData, offset, length - offset)) != -1) {
			
			offset += count;
			if (offset == length)
				break;
		}

		return retData;
	}
	
	public static ArrayList<ArrayList<String>> readExcelAllData(File file) {
		String[][] excelAllData = null;
		ArrayList<ArrayList<String>> rowList = null;
		FileInputStream input = null;
		XSSFWorkbook wb = null;
		try {
			input = new FileInputStream(file);
			wb = new XSSFWorkbook(new BufferedInputStream(input));
			XSSFSheet sheet = wb.getSheetAt(0);
			rowList = new ArrayList<ArrayList<String>>();
			ArrayList<String> cellList = null;
			for(int i = 0; i < sheet.getLastRowNum(); i++){
				XSSFRow row = sheet.getRow(i);
				cellList = new ArrayList<String>();
				for(int j = 0; j < row.getLastCellNum(); j++){
					if(row != null){
						XSSFCell cell = row.getCell(j);
						if(cell != null && !"".equals(cell.getStringCellValue())){
							cellList.add(cell.getStringCellValue());
						}else{
							cellList.add("空");
						}
					}
					
				}
				rowList.add(cellList);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally{
			try {
				wb.close();
				input.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		
		
		/*for(ArrayList<String> list : rowList){
			System.out.println(list.size());
			for(String s : list){
				System.out.print("   "+s);
			}
		}*/
		return rowList;
	}
	/**
	 * 只返回有效的行列值（空值不返回），原版
	 * @param excelPath
	 * @param beginRow
	 * @param beginLie
	 * @return
	 */
	public static List<List<String>> getListBy03OR07ExcelPhysical(String excelPath, int beginRow, int beginLie) {
		org.apache.poi.ss.usermodel.Workbook wb = null;
		ArrayList<String> rowList = null;
		List<List<String>> allList = new ArrayList<List<String>>();
		try {
			File file = new File(excelPath);
			wb =  WorkbookFactory.create(new FileInputStream(file));
			int sheetCount = wb.getNumberOfSheets();
			System.out.println(sheetCount);
			for(int sheetNum = 0; sheetNum < sheetCount; sheetNum++){
				
				Sheet sheet = wb.getSheetAt(sheetNum);
				
				
				for(int i = beginRow; i < sheet.getPhysicalNumberOfRows(); i++){
					Row row = sheet.getRow(i);
					if(row == null){
						System.out.println("null页"+sheetNum+"行"+i);
						continue;
					}
					rowList = new ArrayList<String>();
					for(int j = beginLie ; j < row.getPhysicalNumberOfCells(); j++){
						Cell cell = row.getCell(j);
						if(cell==null){
							System.out.println("null页"+sheetNum+"行"+i+"列"+j);
							continue;
						}
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
							String s = cell.getNumericCellValue()+"";
							rowList.add(s);
						}else{
							String s = cell.getStringCellValue();
							rowList.add(s);
						}
						
					}
					allList.add(rowList);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(wb != null){
					wb.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		/*for(ArrayList<String> list : allList){
			System.out.println();
			for(String s : list){
				System.out.print(s+" ");
			}
		}*/
		
		
		return allList;
	}
	
	/**
	 * 返回包括空值的行列，更新版
	 * @param excelPath
	 * @param beginRow
	 * @param beginLie
	 * @return
	 */
	public static List<List<String>> getListBy03OR07Excel(String excelPath, int beginRow, int beginLie) {
		org.apache.poi.ss.usermodel.Workbook wb = null;
		ArrayList<String> rowList = null;
		List<List<String>> allList = new ArrayList<List<String>>();
		try {
			File file = new File(excelPath);
			wb =  WorkbookFactory.create(new FileInputStream(file));
			int sheetCount = wb.getNumberOfSheets();
			System.out.println(sheetCount);
			for(int sheetNum = 0; sheetNum < sheetCount; sheetNum++){
				
				Sheet sheet = wb.getSheetAt(sheetNum);
				
				
				for(int i = beginRow; i < sheet.getPhysicalNumberOfRows(); i++){
					Row row = sheet.getRow(i);
					if(row == null){
						System.out.println("null页"+sheetNum+"行"+i);
						continue;
					}
					rowList = new ArrayList<String>();
					for(int j = beginLie ; j < row.getLastCellNum(); j++){
						Cell cell = row.getCell(j);
						if(cell==null){
							System.out.println("null页"+sheetNum+"行"+i+"列"+j);
							rowList.add("");
							continue;
						}
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
							String s = cell.getNumericCellValue()+"";
							rowList.add(s);
						}else{
							String s = cell.getStringCellValue();
							rowList.add(s);
						}
						
					}
					allList.add(rowList);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(wb != null){
					wb.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		/*for(ArrayList<String> list : allList){
			System.out.println();
			for(String s : list){
				System.out.print(s+" ");
			}
		}*/
		
		
		return allList;
	}
	
	public static ArrayList<ArrayList<String>> getListBy07Excel(String excelPath, int beginRow, int beginLie, int sheetCount) {
		XSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		ArrayList<ArrayList<String>> allList = new ArrayList<ArrayList<String>>();
		try {
			wb =  new XSSFWorkbook(excelPath);
			for(int sheetNum = 0; sheetNum < sheetCount; sheetNum++){
				XSSFSheet sheet = wb.getSheetAt(sheetNum);
				rowList = new ArrayList<String>();
				
				for(int i = beginRow; i < sheet.getLastRowNum(); i++){
					XSSFRow row = sheet.getRow(i);
					for(int j = beginLie ; j < row.getLastCellNum(); j++){
						XSSFCell cell = row.getCell(j);
						String s = cell.getStringCellValue();
						rowList.add(s);
					}
					
				}
				allList.add(rowList);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(wb != null){
					wb.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		for(ArrayList<String> list : allList){
			System.out.println();
			for(String s : list){
				System.out.print(s + " ");
			}
		}
		
		
		return allList;
	}
	
	public static String readByFileWithEncoding(File file,String encoding){
		String result = null;
		FileInputStream is =null;
		BufferedReader bd =null;
		try {
			is = new FileInputStream(file);
			bd = new BufferedReader(new InputStreamReader(is,encoding));
			StringBuffer sb = new StringBuffer();
			String s = bd.readLine();
			while(s != null){
				sb.append(s);
				s = bd.readLine();
			}
			result = sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(bd != null){
					bd.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static List<String> compareTwoFileSimple(File createfile, File sourcefile){
		List<String> list1 = readByFileToList(createfile, null);
		List<String> list2 = readByFileToList(sourcefile, null);
		String fileName = createfile.getName();
		System.out.println("对比文件"+fileName);
		List<String> differentList = new ArrayList<String>();
		for(String s : list2){
			if(!list1.contains(s)){
				System.out.println("different原来文件多"+s);
				differentList.add(s);
			}
		}
		for(String s : list1){
			if(!list2.contains(s)){
				System.out.println("different生成文件多"+s);
				differentList.add(s);
			}
		}
		if(differentList.size() == 0){
			System.out.println("文件相同");
		}
		return null;
	}
	

	public static void writeFile(File file,String s){
		FileWriter fw1 = null;
		try {
			fw1 = new FileWriter(file);
			fw1.write(s);
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally{
			if(fw1 != null){
				try {
					fw1.close();
				} catch (IOException e) {
					 
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeFileAppendNoEncode(File file,String s){
		FileWriter fw2 = null;
		try {
			fw2 = new FileWriter(file,true);
			fw2.write(s);
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally{
			if(fw2 != null){
				try {
					fw2.close();
				} catch (IOException e) {
					 
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeFileAppendWithEncode(File file, String s, String encode){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			fos.write(s.getBytes(encode));
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
	
	/**
	 * 
	 * @param lists
	 * @return 交换了行列的List
	 */
	public static List<List<String>> tranHangAndLieList(List<List<String>> lists) {
		List<List<String>> results = new ArrayList<List<String>>();
		for(int i = 0; i < lists.size(); i++){
			List<String> list = lists.get(i);
			//System.out.println();
			for(int j = 0; j < list.size(); j++){
				//System.out.print(list.get(j));
				if(results.size() > j){
					results.get(j).add(list.get(j));
				}else{
					List<String> temp = new ArrayList<String>();
					temp.add(list.get(j));
					results.add(temp);
				}
			}
		}
		//System.out.println("--------------");
		return results;
	}
	
	public static List<ExcelSheetDomain> getSheetDomainBy03OR07Excel(String excelPath, int beginRow, int beginLie) {
		List<ExcelSheetDomain> result = new ArrayList<ExcelSheetDomain>();
		org.apache.poi.ss.usermodel.Workbook wb = null;
		ArrayList<String> rowList = null;
		List<List<String>> allList = null;
		try {
			File file = new File(excelPath);
			wb =  WorkbookFactory.create(new FileInputStream(file));
			int sheetCount = wb.getNumberOfSheets();
			//System.out.println(sheetCount);
			ExcelSheetDomain esd;
			for(int sheetNum = 0; sheetNum < sheetCount; sheetNum++){
				
				Sheet sheet = wb.getSheetAt(sheetNum);
				esd = new ExcelSheetDomain();
				esd.setSheetName(sheet.getSheetName());
				allList = new ArrayList<List<String>>();
				for(int i = beginRow; i < sheet.getPhysicalNumberOfRows(); i++){
					Row row = sheet.getRow(i);
					if(row == null){
						System.out.println("null页"+sheetNum+"行"+i);
						continue;
					}
					rowList = new ArrayList<String>();
					for(int j = beginLie ; j < row.getLastCellNum(); j++){
						Cell cell = row.getCell(j);
						if(cell==null){
							System.out.println("null页"+sheetNum+"行"+i+"列"+j);
							rowList.add("");
							continue;
						}
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
							String s = cell.getNumericCellValue()+"";
							rowList.add(s);
						}else{
							String s = cell.getStringCellValue();
							rowList.add(s);
						}
						
					}
					allList.add(rowList);
				}
				esd.setDataList(allList);
				result.add(esd);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(wb != null){
					wb.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		/*for(ArrayList<String> list : allList){
			System.out.println();
			for(String s : list){
				System.out.print(s+" ");
			}
		}*/
		
		
		return result;
	}
	
	public static File createFile(String path){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public static File createFileDeleteSource(String path){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	public static WritableWorkbook createNewExcel03(String excelPath) {
		WritableWorkbook writewb = null;
		try {
			writewb = Workbook.createWorkbook(new File(excelPath));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return writewb;
	}
	
	public static void readExcel07Limit(String excelPath, int rowAccessWindowSize, int beginRow, int beginLie){
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(excelPath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		SXSSFWorkbook writewb = new SXSSFWorkbook(workbook, rowAccessWindowSize);
		
		
	}
	public static String readByFileWithEncodingWithLineBreak(File file,String encoding){
		String result = null;
		FileInputStream is =null;
		BufferedReader bd =null;
		try {
			is = new FileInputStream(file);
			bd = new BufferedReader(new InputStreamReader(is,encoding));
			StringBuffer sb = new StringBuffer();
			String s = bd.readLine();
			while(s != null){
				sb.append(s + "\n");
				s = bd.readLine();
			}
			result = sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(bd != null){
					bd.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void createDIRbyPath(String rootPath, String dir){
		File file = new File(rootPath + dir);
		file.mkdirs();
	}
	
	public static byte[] readFileToByteByInt(String filePath) {
		InputStream fis;
		ByteArrayOutputStream bos;
		byte[] bytes = null;
		try {
			fis = new FileInputStream(filePath);
			bos = new ByteArrayOutputStream();
			int i = 0;
			while((i = fis.read()) != -1){
				bos.write(i);
			}
			bytes = bos.toByteArray();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static byte[] readFileToByteByByte(String filePath) {
		InputStream fis;
		ByteArrayOutputStream bos;
		byte[] bytes = null;
		try {
			fis = new FileInputStream(filePath);
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int i;
			while((i = fis.read(buffer)) != -1){
				bos.write(buffer, 0, i);
			}
			bytes = bos.toByteArray();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return bytes;
	}
	
	
}



