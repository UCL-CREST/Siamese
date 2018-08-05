    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mysql", "root", "admin");
        new DataStorm().show(conn, "SELECT * FROM help_topic h;");
    }
