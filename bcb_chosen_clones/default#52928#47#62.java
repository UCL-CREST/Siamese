    public Connection getconnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Class Found");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established");
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return conn;
    }
