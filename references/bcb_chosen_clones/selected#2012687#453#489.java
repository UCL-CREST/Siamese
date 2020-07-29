    protected void doBackupOrganizeType() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strSelQuery = "SELECT organize_type_id,organize_type_name,width " + "FROM " + Common.ORGANIZE_TYPE_TABLE;
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_TYPE_B_TABLE + " " + "(version_no,organize_type_id,organize_type_name,width) " + "VALUES (?,?,?,?)";
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
                    ps.setString(2, result.getString("organize_type_id"));
                    ps.setString(3, result.getString("organize_type_name"));
                    ps.setInt(4, result.getInt("width"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_backup.doBackupOrganizeType(): ERROR Inserting data " + "in T_SYS_ORGANIZE_B_TYPE INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_backup.doBackupOrganizeType(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_backup.doBackupOrganizeType(): SQLException while committing or rollback");
        }
    }
