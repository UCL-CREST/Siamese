    public Transaction() throws Exception {
        Connection Conn = null;
        Statement Stmt = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            Conn = DriverManager.getConnection(DBUrl);
            Conn.setAutoCommit(true);
            Stmt = Conn.createStatement();
            try {
                Stmt.executeUpdate("DROP TABLE trans_test");
            } catch (SQLException sqlEx) {
            }
            Stmt.executeUpdate("CREATE TABLE trans_test (id int not null primary key, decdata double) type=BDB");
            Conn.setAutoCommit(false);
            Stmt.executeUpdate("INSERT INTO trans_test (id, decdata) VALUES (1, 21.0)");
            Stmt.executeUpdate("INSERT INTO trans_test (id, decdata) VALUES (2, 23.485115)");
            Conn.rollback();
            System.out.println("Roll Ok");
            ResultSet RS = Stmt.executeQuery("SELECT * from trans_test");
            if (!RS.next()) {
                System.out.println("Ok");
            } else {
                System.out.println("Rollback failed");
            }
            Stmt.executeUpdate("INSERT INTO trans_test (id, decdata) VALUES (2, 23.485115)");
            Stmt.executeUpdate("INSERT INTO trans_test (id, decdata) VALUES (1, 21.485115)");
            Conn.commit();
            RS = Stmt.executeQuery("SELECT * from trans_test where id=2");
            if (RS.next()) {
                System.out.println(RS.getDouble(2));
                System.out.println("Ok");
            } else {
                System.out.println("Rollback failed");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (Stmt != null) {
                try {
                    Stmt.close();
                } catch (SQLException SQLEx) {
                }
            }
            if (Conn != null) {
                try {
                    Conn.close();
                } catch (SQLException SQLEx) {
                }
            }
        }
    }
