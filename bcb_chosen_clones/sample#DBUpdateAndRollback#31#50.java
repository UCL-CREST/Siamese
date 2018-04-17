	public static void Sample2(String myField, String condition1, String condition2) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/test", "user", "password");
		connection.setAutoCommit(false);
		
		Statement st = connection.createStatement();
		
		String sql = "UPDATE myTable SET myField = '" + myField + "' WHERE myOtherField1 = '" + condition1 + "' AND myOtherField2 = '" + condition2 + "'";
		
		int numChanged = st.executeUpdate(sql);
		
		// If more than 10 entries change, panic and rollback
		if(numChanged > 10) {
			connection.rollback();
		} else {
			connection.commit();
		}
		
		st.close();
		connection.close();
	}
