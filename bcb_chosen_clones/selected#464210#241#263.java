    public void delete(String language, String tag, int row) throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                String sql = "delete from LanguageMorphologies " + "where LanguageName = '" + language + "' and MorphologyTag = '" + tag + "' and " + "      Rank = " + row;
                conn = fido.util.FidoDataSource.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                bumpAllRowsUp(stmt, language, tag, row);
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw e;
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }
