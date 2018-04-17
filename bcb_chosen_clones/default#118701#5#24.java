    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("usage: java SimpleConnect " + "hostname username password");
            return;
        }
        String databaseHost = args[0];
        String username = args[1];
        String password = args[2];
        Class.forName("org.storrow.jdbc4d.jdbc.FourDDriver");
        Connection connect = DriverManager.getConnection("jdbc:4d:storrow:@" + databaseHost, username, password);
        ResultSet tables = connect.getMetaData().getTables(null, null, null, null);
        while (tables.next()) {
            System.out.println("TABLE: " + tables.getString("TABLE_NAME"));
            ResultSet columns = connect.getMetaData().getColumns(null, null, tables.getString("TABLE_NAME"), null);
            while (columns.next()) {
                System.out.println("  Column name: " + columns.getString("COLUMN_NAME") + " (" + columns.getString("DATA_TYPE") + ")");
            }
        }
        System.exit(0);
    }
