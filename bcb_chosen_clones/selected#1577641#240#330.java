    public void testDoubleNaN() {
        double value = 0;
        boolean wasEqual = false;
        String message = "DB operation completed";
        String ddl1 = "DROP TABLE t1 IF EXISTS;" + "CREATE TABLE t1 ( d DECIMAL, f DOUBLE, l BIGINT, i INTEGER, s SMALLINT, t TINYINT, " + "dt DATE DEFAULT CURRENT_DATE, ti TIME DEFAULT CURRENT_TIME, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP );";
        try {
            stmnt.execute(ddl1);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO t1 (d,f,l,i,s,t,dt,ti,ts) VALUES (?,?,?,?,?,?,?,?,?)");
            ps.setString(1, "0.2");
            ps.setDouble(2, 0.2);
            ps.setLong(3, java.lang.Long.MAX_VALUE);
            ps.setInt(4, Integer.MAX_VALUE);
            ps.setInt(5, Short.MAX_VALUE);
            ps.setInt(6, 0);
            ps.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            ps.setTime(8, new java.sql.Time(System.currentTimeMillis()));
            ps.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.execute();
            ps.setInt(1, 0);
            ps.setDouble(2, java.lang.Double.NaN);
            ps.setLong(3, java.lang.Long.MIN_VALUE);
            ps.setInt(4, Integer.MIN_VALUE);
            ps.setInt(5, Short.MIN_VALUE);
            ps.setInt(6, 0);
            ps.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis() + 1));
            ps.setTime(8, new java.sql.Time(System.currentTimeMillis() + 1));
            ps.setDate(9, new java.sql.Date(System.currentTimeMillis() + 1));
            ps.execute();
            ps.setInt(1, 0);
            ps.setDouble(2, java.lang.Double.POSITIVE_INFINITY);
            ps.setInt(4, Integer.MIN_VALUE);
            ps.setObject(5, Boolean.TRUE);
            ps.setBoolean(5, true);
            ps.setObject(5, new Short((short) 2), Types.SMALLINT);
            ps.setObject(6, new Integer(2), Types.TINYINT);
            ps.setObject(7, new java.sql.Date(System.currentTimeMillis() + 2));
            ps.setObject(8, new java.sql.Time(System.currentTimeMillis() + 2));
            ps.setObject(9, new java.sql.Timestamp(System.currentTimeMillis() + 2));
            ps.execute();
            ps.setObject(1, new Float(0), Types.INTEGER);
            ps.setObject(4, new Float(1), Types.INTEGER);
            ps.setDouble(2, java.lang.Double.NEGATIVE_INFINITY);
            ps.execute();
            ResultSet rs = stmnt.executeQuery("SELECT d, f, l, i, s*2, t FROM t1");
            boolean result = rs.next();
            value = rs.getDouble(2);
            int integerValue = rs.getInt(4);
            if (rs.next()) {
                value = rs.getDouble(2);
                wasEqual = Double.isNaN(value);
                integerValue = rs.getInt(4);
                integerValue = rs.getInt(1);
            }
            if (rs.next()) {
                value = rs.getDouble(2);
                wasEqual = wasEqual && value == Double.POSITIVE_INFINITY;
            }
            if (rs.next()) {
                value = rs.getDouble(2);
                wasEqual = wasEqual && value == Double.NEGATIVE_INFINITY;
            }
            rs = stmnt.executeQuery("SELECT MAX(i) FROM t1");
            if (rs.next()) {
                int max = rs.getInt(1);
                System.out.println("Max value for i: " + max);
            }
            {
                stmnt.execute("drop table CDTYPE if exists");
                rs = stmnt.executeQuery("CREATE TABLE cdType (ID INTEGER NOT NULL, name VARCHAR(50), PRIMARY KEY(ID))");
                rs = stmnt.executeQuery("SELECT MAX(ID) FROM cdType");
                if (rs.next()) {
                    int max = rs.getInt(1);
                    System.out.println("Max value for ID: " + max);
                } else {
                    System.out.println("Max value for ID not returned");
                }
                stmnt.executeUpdate("INSERT INTO cdType VALUES (10,'Test String');");
                stmnt.executeQuery("CALL IDENTITY();");
                try {
                    stmnt.executeUpdate("INSERT INTO cdType VALUES (10,'Test String');");
                } catch (SQLException e1) {
                    stmnt.execute("ROLLBACK");
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        System.out.println("testDoubleNaN complete");
        assertEquals(true, wasEqual);
    }
