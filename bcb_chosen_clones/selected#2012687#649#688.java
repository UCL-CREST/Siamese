    protected void doRestoreOrganizeType() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strDelQuery = "DELETE FROM " + Common.ORGANIZE_TYPE_TABLE;
        String strSelQuery = "SELECT organize_type_id,organize_type_name,width " + "FROM " + Common.ORGANIZE_TYPE_B_TABLE + " " + "WHERE version_no = ?";
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_TYPE_TABLE + " " + "(organize_type_id,organize_type_name,width) " + "VALUES (?,?,?)";
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
                    ps.setString(1, result.getString("organize_type_id"));
                    ps.setString(2, result.getString("organize_type_name"));
                    ps.setInt(3, result.getInt("width"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_backup.doRestoreOrganizeType(): ERROR Inserting data " + "in T_SYS_ORGANIZE_TYPE INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_backup.doRestoreOrganizeType(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_backup.doRestoreOrganizeType(): SQLException while committing or rollback");
        }
    }
