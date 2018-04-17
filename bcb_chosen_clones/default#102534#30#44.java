    private Connection openSqlConnection() {
        try {
            String server = readFromConsole("SQL Server host:");
            String username = readFromConsole("SQL username:");
            String password = readFromConsole("password:");
            String database = readFromConsole("Database:");
            String url = "jdbc:mysql://" + server + "/" + database + "?user=" + username + "&password=" + password;
            Connection con = DriverManager.getConnection(url);
            return con;
        } catch (Exception e) {
            System.out.println("Could not open SQL connection!");
            System.exit(1);
            return null;
        }
    }
