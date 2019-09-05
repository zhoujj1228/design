package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFUtil {

    public static void main(String[] args){
    	getPDFString("E:\\test\\pdf\\H2_AN201710200966321652_1.pdf");
    }
    
    public static String getPDFString(String path) {


        File pdfFile = new File(path);
        PDDocument document = null;
		String content = null;
        try
        {
            // 方式一：
            /**
            InputStream input = null;
            input = new FileInputStream( pdfFile );
            //加载 pdf 文档
            PDFParser parser = new PDFParser(new RandomAccessBuffer(input));
            parser.parse();
            document = parser.getPDDocument();
            **/

            // 方式二：
            document=PDDocument.load(pdfFile);

            // 获取页码
            int pages = document.getNumberOfPages();

            // 读文本内容
            PDFTextStripper stripper=new PDFTextStripper();
            // 设置按顺序输出
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            content = stripper.getText(document);
            //System.out.println(content);     
        }
        catch(Exception e)
        {
            System.out.println(e);
        }finally {
        	try {
				document.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return content;

    
    }
    


}
