    public void delete(String name) throws FidoDatabaseException, CannotDeleteSystemLinkException, ClassLinkTypeNotFoundException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = fido.util.FidoDataSource.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                if (isSystemLink(stmt, name) == true) throw new CannotDeleteSystemLinkException(name);
                AdjectivePrepositionTable prepTable = new AdjectivePrepositionTable();
                prepTable.deleteLinkType(stmt, name);
                ObjectLinkTable objectLinkTable = new ObjectLinkTable();
                objectLinkTable.deleteLinkType(stmt, name);
                String sql = "delete from ClassLinkTypes where LinkName = '" + name + "'";
                stmt.executeUpdate(sql);
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
