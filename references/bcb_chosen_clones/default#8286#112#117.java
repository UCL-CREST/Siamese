    public MapGraph() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/postgis";
        _con = DriverManager.getConnection(url, "postgres", "ibjk87");
        _graph = new IncidenceListGraph();
    }
