    public static void main(String args[]) {
        String url = "jdbc:mySubprotocol:myDataSource";
        Connection con;
        Statement stmt;
        try {
            Class.forName("myDriver.ClassName");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, "myLogin", "myPassword");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM COFFEES");
            uprs.moveToInsertRow();
            uprs.updateString("COF_NAME", "Kona");
            uprs.updateInt("SUP_ID", 150);
            uprs.updateFloat("PRICE", 10.99f);
            uprs.updateInt("SALES", 0);
            uprs.updateInt("TOTAL", 0);
            uprs.insertRow();
            uprs.updateString("COF_NAME", "Kona_Decaf");
            uprs.updateInt("SUP_ID", 150);
            uprs.updateFloat("PRICE", 11.99f);
            uprs.updateInt("SALES", 0);
            uprs.updateInt("TOTAL", 0);
            uprs.insertRow();
            uprs.beforeFirst();
            System.out.println("Table COFFEES after insertion:");
            while (uprs.next()) {
                String name = uprs.getString("COF_NAME");
                int id = uprs.getInt("SUP_ID");
                float price = uprs.getFloat("PRICE");
                int sales = uprs.getInt("SALES");
                int total = uprs.getInt("TOTAL");
                System.out.print(name + "   " + id + "   " + price);
                System.out.println("   " + sales + "   " + total);
            }
            uprs.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }
