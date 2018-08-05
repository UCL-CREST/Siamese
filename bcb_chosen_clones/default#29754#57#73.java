    public void init() throws ServletException {
        PathDB = getInitParameter("PathDB");
        UsernameDB = getInitParameter("UserDB");
        PasswordDB = getInitParameter("PassDB");
        String jdbcDriver = "org.gjt.mm.mysql.Driver";
        String dbURL = "jdbc:mysql://" + PathDB + "?user=" + UsernameDB + "&password=" + PasswordDB;
        try {
            Class.forName(jdbcDriver).newInstance();
            connectDB = DriverManager.getConnection(dbURL);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("JDBC driver not found: " + jdbcDriver);
        } catch (SQLException e) {
            throw new UnavailableException("Unable to connect to: " + dbURL);
        } catch (Exception e) {
            throw new UnavailableException("Error: " + e);
        }
    }
