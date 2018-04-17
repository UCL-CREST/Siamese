    public ConfigureIconsTool() throws SQLException, ClassNotFoundException {
        Class.forName("org.gjt.mm.mysql.Driver");
        con = DriverManager.getConnection("jdbc:mysql:///laboratorio", "root", "");
    }
