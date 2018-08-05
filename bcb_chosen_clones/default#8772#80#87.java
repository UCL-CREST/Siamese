    public void connectToDB() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/bcmed?user=root&password=bentes123");
        } catch (SQLException e) {
            System.out.println("Unable to connect to database");
            System.exit(1);
        }
    }
