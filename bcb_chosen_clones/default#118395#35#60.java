    public static GameRoleList load() {
        GameRoleList gameRoleList = new GameRoleList();
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
            String sql = "select * from role";
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                Role role = (Role) res.getObject("role");
                gameRoleList.addRole(role);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return gameRoleList;
    }
