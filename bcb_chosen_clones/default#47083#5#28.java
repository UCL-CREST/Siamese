    public static void main(String[] args) {
        System.out.println("MySQL Connect Example.");
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "temp";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "saloni";
        String table1 = "Create table Connections(id INT UNSIGNED NOT NULL AUTO_INCREMENT,PRIMARY KEY (id),personA varchar(12),personB varchar(12))";
        String table2 = "Create table Follow(id INT UNSIGNED NOT NULL AUTO_INCREMENT,PRIMARY KEY (id),personA varchar(12),personB varchar(12))";
        String table3 = "Create table Location(id INT UNSIGNED NOT NULL AUTO_INCREMENT,PRIMARY KEY (id),person varchar(12),place varchar(30))";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(table1);
            stmt.executeUpdate(table2);
            stmt.executeUpdate(table3);
            conn.close();
            System.out.println("Disconnected from database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
