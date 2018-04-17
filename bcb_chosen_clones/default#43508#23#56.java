    public static void main(String args[]) {
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + DB_NAME;
            Connection con = DriverManager.getConnection(url);
            System.out.println("URL: " + url);
            System.out.println("Connection: " + con);
            stmt = con.createStatement();
            System.out.print("Create table : ");
            int result = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS \"people\" (" + "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT," + "\"name\" VARCHAR(45) NULL," + "\"size\" FLOAT NULL," + "\"age\" INT NULL);");
            System.out.print(result + "\n");
            System.out.print("Deleting a record : ");
            result = stmt.executeUpdate("DELETE FROM people WHERE id > 0;");
            System.out.print(result + "\n");
            System.out.print("Adding a record : ");
            result = stmt.executeUpdate("INSERT INTO people (name,size,age) VALUES ('David',3.15,28);");
            result = stmt.executeUpdate("INSERT INTO people (name,size,age) VALUES ('David',3.15,28);");
            result = stmt.executeUpdate("INSERT INTO people (name,size,age) VALUES ('David',3.15,28);");
            result = stmt.executeUpdate("INSERT INTO people (name,size,age) VALUES ('David',3.15,28);");
            result = stmt.executeUpdate("INSERT INTO people (name,size,age) VALUES ('David',3.15,28);");
            System.out.print(result + "\n");
            System.out.println("* List of people:");
            ResultSet results = stmt.executeQuery("SELECT * FROM people");
            while (results.next()) {
                System.out.print("\t" + results.getString(2));
                System.out.println("");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
