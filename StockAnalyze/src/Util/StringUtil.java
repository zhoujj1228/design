package Util;

public class StringUtil {
	/**
	 * unicode转换字符串
	 */
	public static String unicodeAndStr2String(String unicodeAndStr) {
		StringBuffer string = new StringBuffer();
		String[] hex = unicodeAndStr.split("\\\\u");
		string.append(hex[0]);
		for (int i = 1; i < hex.length; i++) {
			// 转换出每一个代码点
			String s1 = hex[i].substring(0,4);
			String s2 = "";
			if(hex[i].length() > 4){
				s2 = hex[i].substring(4,hex[i].length());
			}
			int data = Integer.parseInt(s1, 16);
			// 追加成string
			string.append((char) data);
			string.append(s2);
		}
		return string.toString();
	}
	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {
	    StringBuffer unicode = new StringBuffer();
	    for (int i = 0; i < string.length(); i++) {
	        // 取出每一个字符
	        char c = string.charAt(i);
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	    return unicode.toString();
	}
	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {
	    StringBuffer string = new StringBuffer();
	    String[] hex = unicode.split("\\\\u");
	    for (int i = 1; i < hex.length; i++) {
	        // 转换出每一个代码点
	        int data = Integer.parseInt(hex[i], 16);
	        // 追加成string
	        string.append((char) data);
	    }
	    return string.toString();
	}
}
