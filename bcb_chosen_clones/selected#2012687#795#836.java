    protected void doRestoreOrganizeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strDelQuery = "DELETE FROM " + Common.ORGANIZE_RELATION_TABLE;
        String strSelQuery = "SELECT organize_id,organize_type_id,child_id,child_type_id,remark " + "FROM " + Common.ORGANIZE_RELATION_B_TABLE + " " + "WHERE version_no = ?";
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_RELATION_TABLE + " " + "(organize_id,organize_type,child_id,child_type,remark) " + "VALUES (?,?,?,?,?)";
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
                    ps.setString(1, result.getString("organize_id"));
                    ps.setString(2, result.getString("organize_type_id"));
                    ps.setString(3, result.getString("child_id"));
                    ps.setString(4, result.getString("child_type_id"));
                    ps.setString(5, result.getString("remark"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_backup.doRestoreOrganizeRelation(): ERROR Inserting data " + "in T_SYS_ORGANIZE_RELATION INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_backup.doRestoreOrganizeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_backup.doRestoreOrganizeRelation(): SQLException while committing or rollback");
        }
    }
