    public void moveRuleUp(String language, String tag, int row) throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = fido.util.FidoDataSource.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                int max = findMaxRank(stmt, language, tag);
                if ((row < 2) || (row > max)) throw new IllegalArgumentException("Row number (" + row + ") was not between 2 and " + max);
                stmt.executeUpdate("update LanguageMorphologies set Rank = -1 " + "where Rank = " + row + " and MorphologyTag = '" + tag + "' and " + "      LanguageName = '" + language + "'");
                stmt.executeUpdate("update LanguageMorphologies set Rank = " + row + "where Rank = " + (row - 1) + " and MorphologyTag = '" + tag + "' and " + "      LanguageName = '" + language + "'");
                stmt.executeUpdate("update LanguageMorphologies set Rank = " + (row - 1) + "where Rank = -1 and MorphologyTag = '" + tag + "' and " + "      LanguageName = '" + language + "'");
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
