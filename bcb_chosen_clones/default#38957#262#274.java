    private void getAdminConnection() {
        String orig_db = rube;
        tryClassForName();
        try {
            System.out.println("test10.java, 416: " + "Trying:  jdbc:mysql://" + server + "/OpenRolapAdmin " + user_name + "," + password);
            conn = DriverManager.getConnection("jdbc:mysql://" + server + "/OpenRolapAdmin", user_name, password);
            System.out.println("test10.java, 420: " + "connection good");
        } catch (SQLException sex) {
            System.out.println("test10.java, 424: " + "SQLException: " + sex.getMessage());
            System.out.println("test10.java, 425: " + "SQLState: " + sex.getSQLState());
            System.out.println("test10.java, 426: " + "VendorError: " + sex.getErrorCode());
        }
    }
