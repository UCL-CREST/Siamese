    public void atualizarLivro(LivroBean livro) {
        PreparedStatement pstmt = null;
        String sql = "update livro " + "set " + "isbn = ?, " + "autor = ?, " + "editora = ?, " + "edicao = ?, " + "titulo = ? " + "where " + "isbn = ?";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, livro.getISBN());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getEditora());
            pstmt.setString(4, livro.getEdicao());
            pstmt.setString(5, livro.getTitulo());
            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                throw new RuntimeException("Erro ao tentar atualizar livro.", ex1);
            }
            throw new RuntimeException("Erro ao tentar atualizar livro.", ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao tentar atualizar livro.", ex);
            }
        }
    }
