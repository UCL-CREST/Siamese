        @Override
        void execute(Connection conn, Component parent, String context, ProgressMonitor progressBar, ProgressWrapper progressWrapper) throws Exception {
            Statement statement = null;
            try {
                conn.setAutoCommit(false);
                statement = conn.createStatement();
                String deleteSql = getDeleteSql(m_compositionId);
                statement.executeUpdate(deleteSql);
                conn.commit();
                s_compostionCache.delete(new Integer(m_compositionId));
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
