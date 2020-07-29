    protected void doBackupOrganizeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strSelQuery = "SELECT organize_id,organize_type_id,child_id,child_type_id,remark " + "FROM " + Common.ORGANIZE_RELATION_TABLE;
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_RELATION_B_TABLE + " " + "(version_no,organize_id,organize_type,child_id,child_type,remark) " + "VALUES (?,?,?,?,?,?)";
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
                    ps.setString(2, result.getString("organize_id"));
                    ps.setString(3, result.getString("organize_type_id"));
                    ps.setString(4, result.getString("child_id"));
                    ps.setString(5, result.getString("child_type_id"));
                    ps.setString(6, result.getString("remark"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_backup.doBackupOrganizeRelation(): ERROR Inserting data " + "in T_SYS_ORGANIZE_RELATION_B INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_backup.doBackupOrganizeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_backup.doBackupOrganizeRelation(): SQLException while committing or rollback");
        }
    }
