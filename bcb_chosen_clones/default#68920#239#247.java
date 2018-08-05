    public void StatementCheck() {
        if (settings.get("sql", "usemysql").equalsIgnoreCase("true")) {
            try {
                if (sql.isClosed()) sql = DriverManager.getConnection("jdbc:mysql://" + settings.get("sql", "ip") + ":" + settings.get("sql", "port") + "/" + settings.get("sql", "database"), settings.get("sql", "user"), settings.get("sql", "password")).createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
