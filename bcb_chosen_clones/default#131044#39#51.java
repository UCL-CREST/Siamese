    public int executeInsert(String sql) {
        int num = 0;
        try {
            conn = DriverManager.getConnection("jdbc:odbc:ClassDB", "limq", "123");
            Statement stmt = conn.createStatement();
            num = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.err.println("ִ�в����д���:" + ex.getMessage());
            System.out.print("ִ�в����д���:" + ex.getMessage());
        }
        CloseDataBase();
        return num;
    }
