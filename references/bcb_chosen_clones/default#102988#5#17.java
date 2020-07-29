    public HandlerPersistencia() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql:\\localhost\nombreDB?user= x&password = x");
            Statement consulta = conectar.createStatement();
            ResultSet respuesta = consulta.executeQuery("Select * From base Where x = y ");
            while (respuesta.next()) {
                respuesta.getString(2);
            }
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
    }
