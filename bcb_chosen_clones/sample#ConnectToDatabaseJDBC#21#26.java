	public static Connection getConnection2(String serverName, int port, String database, String driver, String username, String password) throws SQLException {
		Connection conn = null;
		String url = "jdbc:" + driver + "://" + serverName + ":" + port + "/" + database;	
		conn = DriverManager.getConnection(url, username, password);
		return conn;
	}
