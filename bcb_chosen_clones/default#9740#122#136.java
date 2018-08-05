    public static boolean storeInformation(int gridid, float value, int variableid, int rotation, int assist) {
        try {
            String url = "jdbc:postgresql://155.206.19.246/ODM-Gamma";
            Class.forName("org.postgresql.Driver");
            Connection db = DriverManager.getConnection(url, "postgis", "");
            Statement st = db.createStatement();
            st.close();
            db.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Caught ClassNotFoundException: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Caught SQLException: " + e.getMessage());
        }
        return true;
    }
