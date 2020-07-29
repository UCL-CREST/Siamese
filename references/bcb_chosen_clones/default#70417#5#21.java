    public static void main(String[] args) {
        System.out.println("SQL Req v0.0");
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://172.16.0.1:3306/tango?user=root&passwd=");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            while (rs.next()) System.out.println(rs.getString("Tables_in_tango"));
            rs = stmt.executeQuery("DESCRIBE device");
            while (rs.next()) System.out.println(rs.getString("Field"));
        } catch (ClassNotFoundException e1) {
            System.out.println(e1);
        } catch (SQLException e2) {
            System.out.println(e2);
        }
        System.out.println("end");
    }
