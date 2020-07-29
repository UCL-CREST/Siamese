        private void delete(Connection conn, int itemId) throws SQLException {
            Statement statement = null;
            try {
                conn.setAutoCommit(false);
                deleteComponents(conn, itemId);
                statement = conn.createStatement();
                StringBuffer sqlBuff = new StringBuffer("DELETE FROM ");
                sqlBuff.append(m_dbItemName);
                sqlBuff.append(" WHERE ");
                sqlBuff.append(m_dbItemIdFieldColName);
                sqlBuff.append(" = ");
                sqlBuff.append(Integer.toString(itemId));
                String sql = sqlBuff.toString();
                statement.executeUpdate(sql);
                conn.commit();
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                throw ex;
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        }
