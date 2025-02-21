package com.boot.util.wiscom;

import java.util.regex.Pattern;

public class RegexConfirmUtils {

	public static String plateRegex = "^(([\\u4e00-\\u9fa5][a-zA-Z]|[\\u4e00-\\u9fa5]{2}\\d{2}|[\\u4e00-\\u9fa5]{2}[a-zA-Z])[-]?|([wW][Jj][\\u4e00-\\u9fa5]{1}[-]?)|([a-zA-Z]{2}))([A-Za-z0-9]{5}|[DdFf][A-HJ-NP-Za-hj-np-z0-9][0-9]{4}|[0-9]{5}[DdFf])$";
	public static Pattern platePattern = Pattern.compile(plateRegex);

	public static boolean judgeIsPlate(String plate_no) {
		return platePattern.matcher(plate_no).find();
	}


	public static void main(String [] args) {
		String plate_no = "无号牌";
		System.err.println(RegexConfirmUtils.judgeIsPlate(plate_no));
	}
}
