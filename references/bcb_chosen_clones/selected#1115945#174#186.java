    private void update(String statement, SyrupConnection con, boolean do_log) throws Exception {
        Statement s = null;
        try {
            s = con.createStatement();
            s.executeUpdate(statement);
            con.commit();
        } catch (Throwable e) {
            if (do_log) {
                logger.log(Level.INFO, "Update failed. Transaction is rolled back", e);
            }
            con.rollback();
        }
    }
