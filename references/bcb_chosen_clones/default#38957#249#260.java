    public void getRubeConnection() {
        tryClassForName();
        try {
            System.out.println("test10.java, 395: " + "Trying:  jdbc:mysql://" + server + "/" + rube + "," + user_name + "," + password);
            conn = DriverManager.getConnection("jdbc:mysql://" + server + "/" + rube, user_name, password);
            System.out.println("test10.java, 399: " + "connection good");
        } catch (SQLException sex) {
            System.out.println("test10.java, 403: " + "SQLException: " + sex.getMessage());
            System.out.println("test10.java, 404: " + "SQLState: " + sex.getSQLState());
            System.out.println("test10.java, 405: " + "VendorError: " + sex.getErrorCode());
        }
    }
