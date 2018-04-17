    public CreateDB() {
        try {
            loadJDBCDriver();
            conn = getConnection("F:/databases/sunpress");
            stmt = conn.createStatement();
            createTables(stmt);
            populateTables(stmt);
            stmt.close();
            conn.close();
            DriverManager.getConnection("jdbc:cloudscape:;shutdown=true");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
