    public static void main(String[] args) {
        Connection con = null;
        String url = "jdbc:mysql://192.168.15.110:3306/";
        String dbName = "os";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "vkmohan123";
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + dbName, userName, password);
            Statement st = con.createStatement();
            String sql123 = "update os.LoadRequestResponse set processId=" + args[0] + " where requestId=" + args[1];
            int update = st.executeUpdate(sql123);
            System.out.println("after update" + args[0] + " req:" + args[1]);
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
