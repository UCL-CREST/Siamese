    public void deleteInstance(int instanceId) throws FidoDatabaseException, ObjectNotFoundException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = fido.util.FidoDataSource.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                if (contains(stmt, instanceId) == false) throw new ObjectNotFoundException(instanceId);
                ObjectLinkTable objectLinkList = new ObjectLinkTable();
                ObjectAttributeTable objectAttributeList = new ObjectAttributeTable();
                objectLinkList.deleteObject(stmt, instanceId);
                objectAttributeList.deleteObject(stmt, instanceId);
                stmt.executeUpdate("delete from Objects where ObjectId = " + instanceId);
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
