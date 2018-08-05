        public boolean run() {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = getDataSource().getConnection();
                conn.setAutoCommit(false);
                conn.rollback();
                stmt = conn.createStatement();
                for (String task : tasks) {
                    if (task.length() == 0) continue;
                    LOGGER.info("Executing SQL migration: " + task);
                    stmt.executeUpdate(task);
                }
                conn.commit();
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (Throwable th) {
                }
                throw new SystemException("Cannot execute SQL migration", ex);
            } finally {
                try {
                    if (stmt != null) stmt.close();
                } catch (Throwable th) {
                    LOGGER.error(th);
                }
                try {
                    if (stmt != null) conn.close();
                } catch (Throwable th) {
                    LOGGER.error(th);
                }
            }
            return true;
        }
