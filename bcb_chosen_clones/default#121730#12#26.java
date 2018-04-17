    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        String url = "jdbc:oracle:thin:@10.0.13.62:1521:dev";
        String password = "cciclife";
        String user = "cciclife";
        Class.forName("com.p6spy.engine.spy.P6SpyDriver");
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * from prpjpayreftask");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
        connection.close();
    }
