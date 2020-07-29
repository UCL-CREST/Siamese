    protected void doRestoreOrganizeTypeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strDelQuery = "DELETE FROM " + Common.ORGANIZE_TYPE_RELATION_TABLE;
        String strSelQuery = "SELECT parent_organize_type,child_organize_type " + "FROM " + Common.ORGANIZE_TYPE_RELATION_B_TABLE + " " + "WHERE version_no = ?";
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_TYPE_RELATION_TABLE + " " + "(parent_organize_type,child_organize_type) " + "VALUES (?,?)";
        DBOperation dbo = factory.createDBOperation(POOL_NAME);
        try {
            try {
                con = dbo.getConnection();
                con.setAutoCommit(false);
                ps = con.prepareStatement(strDelQuery);
                ps.executeUpdate();
                ps = con.prepareStatement(strSelQuery);
                ps.setInt(1, this.versionNO);
                result = ps.executeQuery();
                ps = con.prepareStatement(strInsQuery);
                while (result.next()) {
                    ps.setString(1, result.getString("parent_organize_type"));
                    ps.setString(2, result.getString("child_organize_type"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_backup.doRestoreOrganizeTypeRelation(): ERROR Inserting data " + "in T_SYS_ORGANIZE_TYPE_RELATION INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_backup.doRestoreOrganizeTypeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_backup.doRestoreOrganizeTypeRelation(): SQLException while committing or rollback");
        }
    }
