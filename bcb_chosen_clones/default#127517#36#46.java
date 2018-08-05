    public static void Initialize() throws DBError {
        if (!initialized) {
            try {
                cn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db, username, passwd);
                initialized = true;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                throw new DBError(e);
            }
        }
    }
