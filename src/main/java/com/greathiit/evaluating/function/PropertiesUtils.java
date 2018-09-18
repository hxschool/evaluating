package com.greathiit.evaluating.function;

import java.io.*;
import java.util.Properties;

public class PropertiesUtils {
	// 产生一个操作配置文件的对象
	static Properties prop = new Properties();

	/**
	 * *
	 * 
	 * @param fileName	需要加载的properties文件，文件需要放在WEB-INF/conf根目录下
	 * @return 是否加载成功
	 */
	public static boolean loadFile(String fileName) {
		String cpath = PropertiesUtils.class.getResource("/").toString();    
		if(cpath.indexOf("file:")>-1) {
			cpath = cpath.substring("file:".length());
		}
		
		FileInputStream fin = null;
		try {
			String s = cpath.concat("conf/dbconfig.properties");//"C:\\Users\\Team\\git\\evaluating\\target\\classes\\conf\\dbconfig.properties";
			fin = new FileInputStream(s);
			InputStream in = new BufferedInputStream(fin);
			prop.load(in);
			//prop.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据KEY取回相应的value
	 * 
	 * @param key
	 * @return
	 */
	public static String getPropertyValue(String key) {
		return prop.getProperty(key);
	}
	
	public static void main(String[] args) {
		String cpath = "file:/alidata/server/tomcat9080/webapps/evaluating/WEB-INF/classes/dbconfig.properties";
		if(cpath.indexOf("file:")>-1) {
			cpath = cpath.substring("file:".length());
		}
		System.out.println(cpath);
	}
}