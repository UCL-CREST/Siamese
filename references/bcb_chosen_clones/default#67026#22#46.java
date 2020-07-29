    public void doTestNRows(int nCount, String readerClass) {
        String jdbcClassName = "org.aarboard.jdbc.xls.XlsDriver";
        String jdbcURL = "jdbc:aarboard:xls:C:/Develop/Sourceforge/xlsjdbc/test/testdata/";
        String jdbcUsername = "";
        String jdbcPassword = "";
        String jdbcTableName = "" + nCount + "rows";
        try {
            java.util.Properties info = new java.util.Properties();
            info.setProperty(org.aarboard.jdbc.xls.XlsDriver.XLS_READER_CLASS, readerClass);
            Class.forName(jdbcClassName);
            java.sql.Connection conn = java.sql.DriverManager.getConnection(jdbcURL, info);
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet results = stmt.executeQuery("SELECT * FROM " + jdbcTableName);
            int rCount = 0;
            while (results.next()) {
                rCount++;
            }
            assertTrue("Did not find expected " + nCount + " rows, but " + rCount, rCount == nCount);
            results.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            assertFalse("Exception e: " + e.getMessage(), true);
        }
    }
