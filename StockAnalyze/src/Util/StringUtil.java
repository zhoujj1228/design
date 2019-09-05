package Util;

public class StringUtil {
	/**
	 * unicodeת���ַ���
	 */
	public static String unicodeAndStr2String(String unicodeAndStr) {
		StringBuffer string = new StringBuffer();
		String[] hex = unicodeAndStr.split("\\\\u");
		string.append(hex[0]);
		for (int i = 1; i < hex.length; i++) {
			// ת����ÿһ�������
			String s1 = hex[i].substring(0,4);
			String s2 = "";
			if(hex[i].length() > 4){
				s2 = hex[i].substring(4,hex[i].length());
			}
			int data = Integer.parseInt(s1, 16);
			// ׷�ӳ�string
			string.append((char) data);
			string.append(s2);
		}
		return string.toString();
	}
	/**
	 * �ַ���ת��unicode
	 */
	public static String string2Unicode(String string) {
	    StringBuffer unicode = new StringBuffer();
	    for (int i = 0; i < string.length(); i++) {
	        // ȡ��ÿһ���ַ�
	        char c = string.charAt(i);
	        // ת��Ϊunicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	    return unicode.toString();
	}
	/**
	 * unicode ת�ַ���
	 */
	public static String unicode2String(String unicode) {
	    StringBuffer string = new StringBuffer();
	    String[] hex = unicode.split("\\\\u");
	    for (int i = 1; i < hex.length; i++) {
	        // ת����ÿһ�������
	        int data = Integer.parseInt(hex[i], 16);
	        // ׷�ӳ�string
	        string.append((char) data);
	    }
	    return string.toString();
	}
}
