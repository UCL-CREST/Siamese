    public void adicionaCliente(ClienteBean cliente) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "insert into cliente(nome,cpf,telefone,cursoCargo,bloqueado,ativo,tipo) values(?,?,?,?,?,?,?)";
        try {
            pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getCPF());
            pstmt.setString(3, cliente.getTelefone());
            pstmt.setString(4, cliente.getCursoCargo());
            pstmt.setString(5, cliente.getBloqueado());
            pstmt.setString(6, cliente.getAtivo());
            pstmt.setString(7, cliente.getTipo());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                cliente.setIdCliente(rs.getLong(1));
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                throw new RuntimeException("Erro ao inserir cliente.", ex1);
            }
            throw new RuntimeException("Erro ao inserir cliente.", ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                throw new RuntimeException("Ocorreu um erro no banco de dados.", ex);
            }
        }
    }
