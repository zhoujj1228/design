package Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
	public static List<List<String>> getPatternList(String sourceStr, String pattern, int patternNum){
		List<List<String>> result = new ArrayList<List<String>>();
		Pattern p = Pattern.compile(pattern);// Æ¥ÅäµÄÄ£Ê½
		Matcher matcher = p.matcher(sourceStr);
		while (matcher.find()) {
			List<String> temp = new ArrayList<String>();
			for(int i = 0; i < patternNum + 1; i++){
				String s = matcher.group(i);
				temp.add(s);
			}
			result.add(temp);
		}
		return result;
	}
}
