    public static Connection getDBConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Driver MySQL = (Driver) Class.forName("org.gjt.mm.mysql.Driver").newInstance();
        Connection Conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozymandias", "ozymandias", "desertsands");
        return Conn;
    }
