    public int executeDelete(String sql) {
        int num = 0;
        try {
            conn = DriverManager.getConnection("jdbc:odbc:ClassDB", "limq", "123");
            Statement stmt = conn.createStatement();
            num = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.err.println("ִ��ɾ���д���:" + ex.getMessage());
            System.out.print("ִ��ɾ���д���:" + ex.getMessage());
        }
        CloseDataBase();
        return num;
    }
