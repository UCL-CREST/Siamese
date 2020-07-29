    public void Inicia() {
        Statement st = null;
        nombre = null;
        buque = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost", "sa", "");
            System.out.println("Conectado con HSQLDB, OK");
        } catch (SQLException ex) {
            Logger.getLogger(BasesBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BasesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
