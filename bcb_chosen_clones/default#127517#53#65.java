    private static boolean Reconnect() {
        try {
            cn.close();
        } catch (SQLException e) {
        }
        try {
            cn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db, username, passwd);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
