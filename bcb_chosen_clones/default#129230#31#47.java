    public void connect() {
        DatabaseMetaData dma;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            try {
                this.conn = DriverManager.getConnection(this.dbc);
                dma = conn.getMetaData();
                this.setUrl(dma.getURL());
                this.setDriverName(dma.getDriverName());
                this.setDriverVersion(dma.getDriverVersion());
            } catch (SQLException e) {
                System.out.println(" DriverManager.getConnection() Fail");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class.ForName() Fail");
        }
    }
