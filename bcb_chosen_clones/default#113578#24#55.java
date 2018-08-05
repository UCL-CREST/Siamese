    public void testNull(String readerClass) {
        String jdbcClassName = "org.aarboard.jdbc.xls.XlsDriver";
        String jdbcURL = "jdbc:aarboard:xls:C:/Develop/Sourceforge/xlsjdbc/test/testdata/";
        String jdbcUsername = "";
        String jdbcPassword = "";
        String jdbcTableName = "nulltest1";
        int nCount = 1093;
        try {
            Class.forName(jdbcClassName);
            java.util.Properties info = new java.util.Properties();
            info.setProperty(org.aarboard.jdbc.xls.XlsDriver.XLS_READER_CLASS, readerClass);
            java.sql.Connection conn = java.sql.DriverManager.getConnection(jdbcURL, info);
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet results = stmt.executeQuery("SELECT * FROM " + jdbcTableName);
            int rCount = 0;
            while (results.next()) {
                String thisCountry = results.getString("Land");
                java.sql.Date thisDate = results.getDate("AnmeldedatumDat");
                java.sql.Date thisDate2 = results.getDate("Zahlungsdatum");
                rCount++;
                if (rCount == 1092) {
                    System.out.println("Current row: " + rCount);
                }
            }
            assertTrue("Did not find expected " + nCount + " rows, but " + rCount, rCount == nCount);
            results.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            assertFalse("Exception e: " + e.getMessage(), true);
        }
    }
