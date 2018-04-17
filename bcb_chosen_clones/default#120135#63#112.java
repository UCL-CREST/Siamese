    public static void main(String[] args) {
        final String dbdriver = "org.postgresql.Driver";
        final String dbconnect = "jdbc:postgresql://prologdev1:5432/wdb";
        final String dbuser = "guest2";
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
        String wdbInit = "SELECT wci.begin('" + dbuser + "')";
        String sql = " SELECT value, dataProviderName, placeName, placeGeometry," + "        referencetime, " + "        validFrom, validTo, " + "        valueParameterName, valueParameterUnit, " + "        levelParameterName, levelUnitName, levelFrom, levelTo, " + "        dataVersion, confidenceCode, storetime, valueid, valuetype " + " FROM wci.read( array['hirlam 10'], 'POINT( 10.0 59.0 )', " + "                ('2000-01-03 01:00:00', '2000-01-03 01:00:00', 'exact'), " + "                NULL, " + "                array['instant pressure of air', " + "                      'instant temperature of air', " + "                      'instant velocity of air (u vector)', " + "                      'instant velocity of air (v vector)'], " + "                ( 2, 2, 'distance above ground', 'exact' ), " + "                array[-1], " + "                NULL::wci.returnFloat )";
        final String colNames[] = { "value", "dataProviderName", "placeName", "referencetime", "validFrom", "validTo", "valueParameterName", "valueParameterUnit", "levelParameterName", "levelUnitName", "levelFrom", "levelTo", "dataVersion" };
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
