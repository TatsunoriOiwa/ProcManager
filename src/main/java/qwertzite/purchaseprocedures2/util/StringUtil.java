package qwertzite.purchaseprocedures2.util;

import java.util.Map;

import qwertzite.purchaseprocedures2.procedure.variable.Variable;

public class StringUtil {
	
	
	public static String zeroFIll(int val) {
		return String.format("%2s", String.valueOf(val)).replace(" ", "0");
	}
	
	public static String zeroFIll4(int val) {
		return String.format("%4s", String.valueOf(val)).replace(" ", "0");
	}
	
	public static String substitute(String str, Map<String, Variable> map) {
//		System.out.println("\n " + str + " ->");
		StringBuilder sb = new StringBuilder();
		sb.append(str);
		substituteRecursive(0, sb, map, 0);
//		System.out.println("result: " + sb.toString());
		return sb.toString();
	}
	
	private static final String OPEN = "{";
	private static final String CLOSE = "}";
	private static int substituteRecursive(int beginIndex, StringBuilder sb, Map<String, Variable> map, int d) {
		int cursor = beginIndex;
		int open;
		int close;
		while(true) {
			open = sb.indexOf(OPEN, cursor);
			close = sb.indexOf(CLOSE, cursor);
//			System.out.print(d);
//			printPos(new int[] {beginIndex, cursor, open, close}, new String[] { "|", "^", "{", "}" });
			if (open >= 0 && close > open) { // recursive
				cursor = substituteRecursive(open+1, sb, map, d+1);
			} else if (close > 0 && beginIndex > 0) { // replace
				String str = sb.substring(beginIndex, close);
//				System.out.println("replace " + str);
				if (map.containsKey(str)) {
					String replace = map.get(str).getDisplayString();
					sb.replace(beginIndex-1, close+1, replace);
//					System.out.println(" " + sb);
					return beginIndex + replace.length()-1;
				}
//				System.out.println(" " + sb);
				return close;
			} else { // none
				return cursor;
			}
		}
	}
}
