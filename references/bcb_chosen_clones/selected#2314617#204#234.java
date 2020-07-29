    protected void fixupCategoryAncestry(Context context) throws DataStoreException {
        Connection db = null;
        Statement s = null;
        try {
            db = context.getConnection();
            db.setAutoCommit(false);
            s = db.createStatement();
            s.executeUpdate("delete from category_ancestry");
            walkTreeFixing(db, CATEGORYROOT);
            db.commit();
            context.put(Form.ACTIONEXECUTEDTOKEN, "Category Ancestry regenerated");
        } catch (SQLException sex) {
            try {
                db.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DataStoreException("Failed to refresh category ancestry");
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (db != null) {
                context.releaseConnection(db);
            }
        }
    }
