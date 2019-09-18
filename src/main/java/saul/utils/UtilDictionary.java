package saul.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UtilDictionary {
	public static void closeInputStream(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void closeOutputStream(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static boolean checkIp(String ip) {
		if ("localhost".equalsIgnoreCase(ip)) return true;

		if(ip.length()<7 || ip.length() >15) return false;
		
		String[] arr = ip.split("\\.");	
		if (arr.length != 4) return false;
		int temp = 0;
		for(String num : arr) {
			if(!checkIntNumber(num)) return false;
			temp = Integer.parseInt(num);
			if (temp < 0 || temp > 255) return false; 
		}
		return true;
	}
	
	public static boolean checkIntNumber(String num) {
		char temp = ' ';
		for(int i = 0; i < num.length(); i++) {
			temp = num.charAt(i);
			if ( temp > '9' || temp < '0') 
				return false;	
		}
		return true;
	}
	
	public static boolean checkFilePath(String path) {
		if (path.endsWith(".properties")) return true;
		return false;
			
		
	}

}
