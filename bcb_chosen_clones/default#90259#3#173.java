    public static void main(String args[]) throws Exception {
        String databaseURL = "jdbc:firebirdsql:local:c:/database/dialect1.fdb?sql_dialect=1";
        String user = "sysdba";
        String password = "masterkey";
        String driverName = "org.firebirdsql.jdbc.FBDriver";
        java.sql.Driver d = null;
        java.sql.Connection c = null;
        java.sql.Statement s = null;
        java.sql.ResultSet rs = null;
        try {
            int registrationAlternative = 1;
            switch(registrationAlternative) {
                case 1:
                    try {
                        Class.forName("org.firebirdsql.jdbc.FBDriver");
                    } catch (java.lang.ClassNotFoundException e) {
                        System.out.println("Firebird JCA-JDBC driver not found in class path");
                        System.out.println(e.getMessage());
                        return;
                    }
                    break;
                case 2:
                    try {
                        java.sql.DriverManager.registerDriver((java.sql.Driver) Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance());
                    } catch (java.lang.ClassNotFoundException e) {
                        System.out.println("Driver not found in class path");
                        System.out.println(e.getMessage());
                        return;
                    } catch (java.lang.IllegalAccessException e) {
                        System.out.println("Unable to access driver constructor, this shouldn't happen!");
                        System.out.println(e.getMessage());
                        return;
                    } catch (java.lang.InstantiationException e) {
                        System.out.println("Unable to create an instance of driver class, this shouldn't happen!");
                        System.out.println(e.getMessage());
                        return;
                    } catch (java.sql.SQLException e) {
                        System.out.println("Driver manager failed to register driver");
                        showSQLException(e);
                        return;
                    }
                    break;
                case 3:
                    java.util.Properties sysProps = System.getProperties();
                    StringBuffer drivers = new StringBuffer("org.firebirdsql.jdbc.FBDriver");
                    String oldDrivers = sysProps.getProperty("jdbc.drivers");
                    if (oldDrivers != null) drivers.append(":" + oldDrivers);
                    sysProps.put("jdbc.drivers", drivers.toString());
                    System.setProperties(sysProps);
                    break;
                case 4:
                    d = new org.firebirdsql.jdbc.FBDriver();
            }
            try {
                d = java.sql.DriverManager.getDriver(databaseURL);
                System.out.println("Firebird JCA-JDBC driver version " + d.getMajorVersion() + "." + d.getMinorVersion() + " registered with driver manager.");
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to find Firebird JCA-JDBC driver among the registered drivers.");
                showSQLException(e);
                return;
            }
            int connectionAlternative = 1;
            switch(connectionAlternative) {
                case 1:
                    try {
                        c = java.sql.DriverManager.getConnection(databaseURL, user, password);
                        System.out.println("Connection established.");
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                        System.out.println("Unable to establish a connection through the driver manager.");
                        showSQLException(e);
                        return;
                    }
                    break;
                case 2:
                    try {
                        java.util.Properties connectionProperties = new java.util.Properties();
                        connectionProperties.put("user", user);
                        connectionProperties.put("password", password);
                        connectionProperties.put("lc_ctype", "WIN1251");
                        c = d.connect(databaseURL, connectionProperties);
                        System.out.println("Connection established.");
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                        System.out.println("Unable to establish a connection through the driver.");
                        showSQLException(e);
                        return;
                    }
                    break;
            }
            try {
                c.setAutoCommit(false);
                System.out.println("Auto-commit is disabled.");
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to disable autocommit.");
                showSQLException(e);
                return;
            }
            try {
                java.sql.DatabaseMetaData dbMetaData = c.getMetaData();
                if (dbMetaData.supportsTransactions()) System.out.println("Transactions are supported."); else System.out.println("Transactions are not supported.");
                java.sql.ResultSet tables = dbMetaData.getTables(null, null, "%", new String[] { "VIEW" });
                while (tables.next()) {
                    System.out.println(tables.getString("TABLE_NAME") + " is a view.");
                }
                tables.close();
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to extract database meta data.");
                showSQLException(e);
            }
            try {
                s = c.createStatement();
                s.executeQuery("select cast('????' as varchar(30) character set win1251) from rdb$database order by 1 collate pxw_cyrl");
                s.executeUpdate("update employee set salary = salary + 10000");
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                System.out.println("Unable to increase everyone's salary.");
                showSQLException(e);
            }
            try {
                rs = s.executeQuery("select full_name from employee where salary < 50000");
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to submit a static SQL query.");
                showSQLException(e);
                return;
            }
            try {
                java.sql.ResultSetMetaData rsMetaData = rs.getMetaData();
                System.out.println("The query executed has " + rsMetaData.getColumnCount() + " result columns.");
                System.out.println("Here are the columns: ");
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    System.out.println(rsMetaData.getColumnName(i) + " of type " + rsMetaData.getColumnTypeName(i));
                }
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to extract result set meta data.");
                showSQLException(e);
            }
            try {
                System.out.println("Here are the employee's whose salary < $50,000");
                while (rs.next()) {
                    System.out.println(rs.getString("full_name"));
                }
            } catch (java.sql.SQLException e) {
                System.out.println("Unable to step thru results of query");
                showSQLException(e);
                return;
            }
        } finally {
            System.out.println("Closing database resources and rolling back any changes we made to the database.");
            try {
                if (rs != null) rs.close();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }
            try {
                if (s != null) s.close();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }
            try {
                if (c != null) c.rollback();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }
            try {
                if (c != null) c.close();
            } catch (java.sql.SQLException e) {
                showSQLException(e);
            }
        }
    }
