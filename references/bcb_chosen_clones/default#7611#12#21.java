    public static Connection getCon() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433; DatabaseName=XuRi", "sa", "sa");
            System.out.println("调用连接方法");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
