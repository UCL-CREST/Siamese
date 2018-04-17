    public void close() {
        boolean goodExit = false;
        if (!pending()) commit();
        try {
            DriverManager.getConnection("jdbc:derby:" + dbName + ";shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                goodExit = true;
            }
        }
        if (!goodExit) {
            System.err.println("Database " + dbName + " did not shut down correctly.");
        }
    }
