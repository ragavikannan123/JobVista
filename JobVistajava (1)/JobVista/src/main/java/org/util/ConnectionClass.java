package org.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
	private Connection conn;
	private static ConnectionClass currConnection;

	private ConnectionClass() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/JobVista2","Abi","Abi");

		} catch (SQLException e) {
			System.out.println("Problem in sql Connection");
		} catch (ClassNotFoundException e) {
			System.out.println("Problem in Sql jar");
		}

	}

	public static ConnectionClass CreateCon() {
//		Problem in Connection
		if (currConnection instanceof ConnectionClass) {
			return currConnection;
		} else {
			return new ConnectionClass();
		}

	}

	public Connection getConnection() {
		return conn;
	}

}
