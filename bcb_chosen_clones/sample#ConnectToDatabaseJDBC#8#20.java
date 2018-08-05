	public static Connection getConnection1(String serverName, int port, String database, String driver, String username, String password) throws SQLException {
		Connection conn = null;
		
		Properties props = new Properties();
		props.put("user", username);
		props.put("password", password);
		
		String url = "jdbc:" + driver + "://" + serverName + ":" + port + "/" + database;
		
		conn = DriverManager.getConnection(url, props);
		
		return conn;
	}
