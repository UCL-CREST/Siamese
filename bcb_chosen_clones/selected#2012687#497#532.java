    protected void doBackupOrganizeTypeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strSelQuery = "SELECT parent_organize_type,child_organize_type " + "FROM " + Common.ORGANIZE_TYPE_RELATION_TABLE;
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_TYPE_RELATION_B_TABLE + " " + "(version_no,parent_organize_type,child_organize_type) " + "VALUES (?,?,?)";
        DBOperation dbo = factory.createDBOperation(POOL_NAME);
        try {
            try {
                con = dbo.getConnection();
                con.setAutoCommit(false);
                ps = con.prepareStatement(strSelQuery);
                result = ps.executeQuery();
                ps = con.prepareStatement(strInsQuery);
                while (result.next()) {
                    ps.setInt(1, this.versionNO);
                    ps.setString(2, result.getString("parent_organize_type"));
                    ps.setString(3, result.getString("child_organize_type"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_backup.doBackupOrganizeTypeRelation(): ERROR Inserting data " + "in T_SYS_ORGANIZE_TYPE_RELATION_B INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_backup.doBackupOrganizeTypeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_backup.doBackupOrganizeTypeRelation(): SQLException while committing or rollback");
        }
    }
