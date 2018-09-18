package com.greathiit.evaluating.dBassiant;

import java.sql.Connection;

import com.greathiit.evaluating.function.*;


public class DBHelper {
	private Connection con=null;
	
	public DBHelper()
	{
		
	}
	
	
	public Connection getConnetion() throws Exception
	{
		if(con==null)
		{
			con=ConnUtils.getConnection();
			return con;
		}
		return con;
	}
}
