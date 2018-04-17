    public static void shutdown() {
        boolean goodExit = false;
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                goodExit = true;
            }
        }
        if (!goodExit) {
            System.err.println("Database did not shut down correctly.");
        }
    }
