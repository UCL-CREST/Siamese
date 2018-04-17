    public void save() {
        Connection conn = null;
        try {
            String dbURL = "jdbc:hsqldb:/tmp/yacht";
            Class.forName("org.hsqldb.jdbcDriver");
            conn = DriverManager.getConnection(dbURL, "sa", "");
        } catch (Exception e) {
            System.out.println("ERROR: Could not connect to database");
            System.out.print(e);
        }
        try {
            String delsql = "delete from role";
            Statement delstmt = conn.createStatement();
            delstmt.executeQuery(delsql);
            delstmt.close();
            int counter = 0;
            while (counter < _gameRoleList.size()) {
                Object role = _gameRoleList.elementAt(counter);
                PreparedStatement stmt = conn.prepareStatement("insert into role (role) values (?)");
                stmt.setObject(1, role);
                stmt.executeUpdate();
                stmt.close();
                counter++;
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
