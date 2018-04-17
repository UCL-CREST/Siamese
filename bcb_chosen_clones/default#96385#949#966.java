    static void conectaBanco() {
        try {
            String driverName = "org.gjt.mm.mysql.Driver";
            Class.forName(driverName);
            String serverName = "localhost";
            String mydatabase = "IETS_PHP";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
            String username = "iets";
            String password = "bg5ad3";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("O driver expecificado não foi encontrado.");
            resultado = "O driver expecificado não foi encontrado.";
        } catch (SQLException e) {
            System.out.println("N�o foi poss�vel conectar ao Banco de Dados");
            resultado = "N�o foi poss�vel conectar ao Banco de Dados";
        }
    }
