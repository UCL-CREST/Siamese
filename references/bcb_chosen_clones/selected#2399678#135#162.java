    @Test
    public void testSQLite() {
        log("trying SQLite..");
        for (int i = 0; i < 10; i++) {
            Connection c = null;
            try {
                Class.forName("SQLite.JDBCDriver");
                c = DriverManager.getConnection("jdbc:sqlite:/C:/Source/SRFSurvey/app/src/org/speakright/srfsurvey/results.db", "", "");
                c.setAutoCommit(false);
                Statement st = c.createStatement();
                int rc = st.executeUpdate("INSERT INTO tblAnswers(fQID,fQNAME) VALUES('q1','zoo')");
                st.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(1);
                try {
                    if (c != null && !c.isClosed()) {
                        c.rollback();
                        c.close();
                    }
                } catch (SQLException sql) {
                }
            }
        }
        log("end");
    }
