    public void connect() throws SQLException {
        if (this.isValid()) return;
        sqlConnection = DriverManager.getConnection("jdbc:postgresql://newton.math.uni-mannheim.de" + ":5432/bugflow", "bugflow", "bugflow");
        connectionOpened = true;
    }
