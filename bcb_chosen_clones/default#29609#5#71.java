    public static void main(String[] args) {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dbName = "jdbcDemoDB";
        String connectionURL = "jdbc:derby:" + dbName + ";create=true";
        Connection conn = null;
        Statement s;
        PreparedStatement psInsert;
        ResultSet myWishes;
        String printLine = "  __________________________________________________";
        String createString = "CREATE TABLE WISH_LIST  " + "(WISH_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY " + "   CONSTRAINT WISH_PK PRIMARY KEY, " + " ENTRY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + " WISH_ITEM VARCHAR(32) NOT NULL) ";
        String answer;
        try {
            Class.forName(driver);
            System.out.println(driver + " loaded. ");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
            System.out.println("\n    >>> Please check your CLASSPATH variable   <<<\n");
        }
        try {
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Connected to database " + dbName);
            s = conn.createStatement();
            if (!WwdUtils.wwdChk4Table(conn)) {
                System.out.println(" . . . . creating table WISH_LIST");
                s.execute(createString);
            }
            psInsert = conn.prepareStatement("insert into WISH_LIST(WISH_ITEM) values (?)");
            do {
                answer = WwdUtils.getWishItem();
                if (!answer.equals("exit")) {
                    psInsert.setString(1, answer);
                    psInsert.executeUpdate();
                    myWishes = s.executeQuery("select ENTRY_DATE, WISH_ITEM from WISH_LIST order by ENTRY_DATE");
                    System.out.println(printLine);
                    while (myWishes.next()) {
                        System.out.println("On " + myWishes.getTimestamp(1) + " I wished for " + myWishes.getString(2));
                    }
                    System.out.println(printLine);
                    myWishes.close();
                }
            } while (!answer.equals("exit"));
            psInsert.close();
            s.close();
            conn.close();
            System.out.println("Closed connection");
            if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
                boolean gotSQLExc = false;
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                    if (se.getSQLState().equals("XJ015")) {
                        gotSQLExc = true;
                    }
                }
                if (!gotSQLExc) {
                    System.out.println("Database did not shut down normally");
                } else {
                    System.out.println("Database shut down normally");
                }
            }
        } catch (Throwable e) {
            System.out.println(" . . . exception thrown:");
            errorPrint(e);
        }
        System.out.println("Getting Started With Derby JDBC program ending.");
    }
