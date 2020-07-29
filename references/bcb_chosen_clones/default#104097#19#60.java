    public static void main(String args[]) {
        try {
            boolean createDB = true;
            r.setSeed(System.currentTimeMillis());
            Class.forName("com.mysql.jdbc.Driver");
            String url = MYSQL_CONNECTSTRING;
            Connection con = DriverManager.getConnection(url, MYSQL_LOGIN, MYSQL_PASSWORD);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "SHOW DATABASES";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("Database").equals(MYSQL_DATABASE_NAME)) {
                    createDB = false;
                    break;
                }
            }
            if (createDB == true) {
                System.out.println("Creating database " + MYSQL_DATABASE_NAME);
                sql = "CREATE Database " + MYSQL_DATABASE_NAME;
                stmt.executeUpdate(sql);
                System.out.println("Creating table " + MYSQL_TABLE_NAME);
                sql = "CREATE TABLE ";
                sql += MYSQL_DATABASE_NAME + "." + MYSQL_TABLE_NAME;
                sql += "(col1 varchar(16), col2 varchar(16), col3 int)";
                stmt.executeUpdate(sql);
            }
            for (int i = 0; i < 1000; i++) {
                FavoriteKeys.genNextKey();
                sql = "INSERT INTO " + MYSQL_DATABASE_NAME + "." + MYSQL_TABLE_NAME;
                sql += "(col1,col2,col3) ";
                sql += "VALUES('" + FavoriteKeys.key1 + "','";
                sql += FavoriteKeys.key2 + "',";
                sql += FavoriteKeys.value + ")";
                stmt.executeUpdate(sql);
            }
            displayDistributionReport();
            con.close();
        } catch (Exception e) {
            System.out.println("exception: " + e);
        }
    }
