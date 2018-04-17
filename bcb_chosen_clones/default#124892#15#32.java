    public static void main(String[] args) {
        try {
            String url = "jdbc:oracle:thin:@" + args[0];
            String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);
            Connection conn = DriverManager.getConnection(url, args[1], args[2]);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM Resorts");
            System.out.println("jdbc connection made!");
            if (r.next()) System.out.println("database:" + url + " is ok!"); else System.out.println("database:" + url + " is empty!");
            r.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
    }
