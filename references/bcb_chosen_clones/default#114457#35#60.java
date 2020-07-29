    public static GameBattlePlanTmplList load() {
        GameBattlePlanTmplList gameBattlePlanTmplList = new GameBattlePlanTmplList();
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
            String sql = "select * from battleplan";
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                BattlePlanTmpl battlePlan = (BattlePlanTmpl) res.getObject("battleplan");
                gameBattlePlanTmplList.addBattlePlanTmpl(battlePlan);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return gameBattlePlanTmplList;
    }
