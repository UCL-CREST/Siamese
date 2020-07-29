    public ResultSet executeQuery(String sql) {
        rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:odbc:ClassDB", "limq", "123");
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            System.err.println("ִ�в�ѯ�д���:" + ex.getMessage());
            System.out.print("ִ�в�ѯ�д���:" + ex.getMessage());
        }
        return rs;
    }
