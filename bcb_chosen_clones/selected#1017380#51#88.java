    public void saveHiddenNames(HashSet names) throws SQLException {
        Connection conn = AppLayerDatabase.getInstance().getPooledConnection();
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM HIDDENBOARDNAMES");
            stmt.close();
            stmt = null;
            ps = conn.prepareStatement("INSERT INTO HIDDENBOARDNAMES (boardname) VALUES (?)");
            for (Iterator i = names.iterator(); i.hasNext(); ) {
                String bName = (String) i.next();
                ps.setString(1, bName);
                ps.executeUpdate();
            }
            ps.close();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Exception during save", t);
            try {
                conn.rollback();
            } catch (Throwable t1) {
                logger.log(Level.SEVERE, "Exception during rollback", t1);
            }
            try {
                conn.setAutoCommit(true);
            } catch (Throwable t1) {
            }
        } finally {
            AppLayerDatabase.getInstance().givePooledConnection(conn);
            try {
                if (ps != null) ps.close();
            } catch (Throwable t1) {
            }
            ;
        }
    }
