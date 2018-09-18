package com.greathiit.evaluating.function;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnUtils {
	private static Connection conn = null;
	private static String driver = null;

	private static String username = null;
	private static String password = null;
	private static String url = null;
	public static String SYSTEM_URL = null;
	public static String SYSTEM_PUBLICKEY = null;
	public static String APPID = null;
	public static String PUBLICKEY = null;
	public static String PRIVATEKEY = null;
	static {
		PropertiesUtils.loadFile("dbconfig.properties");
		driver = PropertiesUtils.getPropertyValue("jdbc.driver");
		url = PropertiesUtils.getPropertyValue("jdbc.url");
		username = PropertiesUtils.getPropertyValue("jdbc.username");
		password = PropertiesUtils.getPropertyValue("jdbc.password");
		
		SYSTEM_URL = PropertiesUtils.getPropertyValue("greathiit.url");
		SYSTEM_PUBLICKEY = PropertiesUtils.getPropertyValue("greathiit.otherPublicKey");
		APPID = PropertiesUtils.getPropertyValue("evaluating.appid");
		PUBLICKEY = PropertiesUtils.getPropertyValue("evaluating.publicKey");
		PRIVATEKEY = PropertiesUtils.getPropertyValue("evaluating.privateKey");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 取得连接的工具方法
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if(conn!=null) {
			return conn;
		}
		conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

}
