	public static void Sample1(String myField, String condition1, String condition2) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/test", "user", "password");
		connection.setAutoCommit(false);
		
		PreparedStatement ps = connection.prepareStatement("UPDATE myTable SET myField = ? WHERE myOtherField1 = ? AND myOtherField2 = ?");
		ps.setString(1, myField);
		ps.setString(2, condition1);
		ps.setString(3, condition2);
		
		// If more than 10 entries change, panic and rollback
		int numChanged = ps.executeUpdate();
		if(numChanged > 10) {
			connection.rollback();
		} else {
			connection.commit();
		}
		
		ps.close();
		connection.close();
	}
