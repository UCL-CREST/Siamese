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
        String wdbInit = "SELECT wci.begin('" + dbuser + "')";
        String sql = " SELECT * " + " FROM wci.read( array['test wci 5'], NULL, " + "                '2009-11-13 00:00:00+00', " + "                NULL, " + "                array['air temperature', " + "                      'air pressure'], " + "                NULL, " + "                array[-1], " + "                NULL::wci.returngid )";
        final String colNames[] = { "valueParameterName", "validTimeFrom" };
        ResultSet rs = null;
        try {
            statement.execute(wdbInit);
            rs = statement.executeQuery(sql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            String data;
            boolean first;
            while (rs.next()) {
                Statement getGridStatement = con.createStatement();
                first = true;
                long gridId = rs.getLong("value");
                ResultSet blobResult = getGridStatement.executeQuery("SELECT * FROM wci.fetch(" + gridId + ", NULL::wci.grid)");
                while (blobResult.next()) {
                    int size = blobResult.getInt("numberX") + blobResult.getInt("numberX");
                    InputStream blob = blobResult.getBinaryStream("grid");
                    DataInputStream blobReader = new DataInputStream(blob);
                    int floatToRead = 42;
                    blobReader.skip(floatToRead * 4);
                    float value = blobReader.readFloat();
                    System.out.print(value + "\t");
                    for (String name : colNames) {
                        data = rs.getString(name);
                        if (first) {
                            first = false;
                        } else {
                            System.out.print(", ");
                        }
                        if (data != null) System.out.print(data); else System.out.print("\\N");
                    }
                    System.out.print("  (BLOB size: " + size + ")");
                    System.out.println();
                }
                blobResult.close();
            }
        } catch (Exception ex) {
            System.out.println("SELECT error: " + ex);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException ex) {
                System.out.println("EXCEPTION: rs.close: " + ex);
            }
        }
    }
