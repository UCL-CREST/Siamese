    void go(String[] args) {
        parseArguments(args);
        System.out.println("SimpleApp starting in " + framework + " mode");
        loadDriver();
        Connection conn = null;
        ArrayList statements = new ArrayList();
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        Statement s = null;
        ResultSet rs = null;
        try {
            Properties props = new Properties();
            props.put("user", "user1");
            props.put("password", "user1");
            String dbName = "derbyDB";
            conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
            System.out.println("Connected to and created database " + dbName);
            conn.setAutoCommit(false);
            s = conn.createStatement();
            statements.add(s);
            s.execute("create table location(num int, addr varchar(40))");
            System.out.println("Created table location");
            psInsert = conn.prepareStatement("insert into location values (?, ?)");
            statements.add(psInsert);
            psInsert.setInt(1, 1956);
            psInsert.setString(2, "Webster St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1956 Webster");
            psInsert.setInt(1, 1910);
            psInsert.setString(2, "Union St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1910 Union");
            psUpdate = conn.prepareStatement("update location set num=?, addr=? where num=?");
            statements.add(psUpdate);
            psUpdate.setInt(1, 180);
            psUpdate.setString(2, "Grand Ave.");
            psUpdate.setInt(3, 1956);
            psUpdate.executeUpdate();
            System.out.println("Updated 1956 Webster to 180 Grand");
            psUpdate.setInt(1, 300);
            psUpdate.setString(2, "Lakeshore Ave.");
            psUpdate.setInt(3, 180);
            psUpdate.executeUpdate();
            System.out.println("Updated 180 Grand to 300 Lakeshore");
            rs = s.executeQuery("SELECT num, addr FROM location ORDER BY num");
            int number;
            boolean failure = false;
            if (!rs.next()) {
                failure = true;
                reportFailure("No rows in ResultSet");
            }
            if ((number = rs.getInt(1)) != 300) {
                failure = true;
                reportFailure("Wrong row returned, expected num=300, got " + number);
            }
            if (!rs.next()) {
                failure = true;
                reportFailure("Too few rows");
            }
            if ((number = rs.getInt(1)) != 1910) {
                failure = true;
                reportFailure("Wrong row returned, expected num=1910, got " + number);
            }
            if (rs.next()) {
                failure = true;
                reportFailure("Too many rows");
            }
            if (!failure) {
                System.out.println("Verified the rows");
            }
            s.execute("drop table location");
            System.out.println("Dropped table location");
            conn.commit();
            System.out.println("Committed the transaction");
            if (framework.equals("embedded")) {
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                    if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState())))) {
                        System.out.println("Derby shut down normally");
                    } else {
                        System.err.println("Derby did not shut down normally");
                        printSQLException(se);
                    }
                }
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }
            int i = 0;
            while (!statements.isEmpty()) {
                Statement st = (Statement) statements.remove(i);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException sqle) {
                    printSQLException(sqle);
                }
            }
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }
        }
    }
