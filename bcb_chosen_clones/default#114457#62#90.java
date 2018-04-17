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
            String delsql = "delete from battleplan";
            Statement delstmt = conn.createStatement();
            delstmt.executeQuery(delsql);
            delstmt.close();
            int counter = 0;
            while (counter < _gameBattlePlanTmplList.size()) {
                Object battlePlan = _gameBattlePlanTmplList.elementAt(counter);
                PreparedStatement stmt = conn.prepareStatement("insert into battleplan (battleplan) values (?)");
                stmt.setObject(1, battlePlan);
                stmt.executeUpdate();
                stmt.close();
                counter++;
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
