    public static void main(String[] args) {
        final String dbdriver = "org.postgresql.Driver";
        final String dbconnect = "jdbc:postgresql://localhost:5432/wdb";
        final String dbuser = "wcitest";
        final String dbpasswd = "";
        Connection con = null;
        Statement statement = null;
        try {
            Class.forName(dbdriver);
            con = DriverManager.getConnection(dbconnect, dbuser, dbpasswd);
            statement = con.createStatement();
        } catch (Exception e) {
            System.out.println("FATAL: cant load the database driver <" + dbdriver + ">!");
            System.exit(1);
        }
        String wdbInit = "SELECT wci.begin('" + dbuser + "', 999, 999, 999 )";
        String sql = " SELECT * " + " FROM wci.read( array['test wci 5'], 'POINT( 10.0 59.0 )', " + "                '2009-11-13 00:00:00+00', " + "                NULL, " + "                array['air temperature', " + "                      'air pressure'], " + "                NULL, " + "                array[-1], " + "                NULL::wci.returnFloat )";
        final String colNames[] = { "value", "valueParameterName", "validTimeFrom" };
        ResultSet rs = null;
        try {
            statement.execute(wdbInit);
            rs = statement.executeQuery(sql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            String data;
            boolean first;
            while (rs.next()) {
                first = true;
                for (String name : colNames) {
                    data = rs.getString(name);
                    if (first) {
                        first = false;
                    } else {
                        System.out.print(", ");
                    }
                    if (data != null) System.out.print(data); else System.out.print("\\N");
                }
                System.out.println();
            }
        } catch (Exception ex) {
            System.out.println("SELECT error: " + ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("EXCEPTION: rs.close: " + ex);
                }
            }
        }
    }
