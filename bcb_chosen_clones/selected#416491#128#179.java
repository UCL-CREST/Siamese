    public void update(Site site) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String exp = site.getExtendParent();
        String path = site.getPath();
        try {
            String sqlStr = "update t_ip_site set id=?,name=?,description=?,ascii_name=?,remark_number=?,increment_index=?,use_status=?,appserver_id=? where id=?";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            String[] selfDefinePath = getSelfDefinePath(path, exp, connection, preparedStatement, resultSet);
            selfDefineDelete(selfDefinePath, connection, preparedStatement);
            selfDefineAdd(selfDefinePath, site, connection, preparedStatement);
            preparedStatement = connection.prepareStatement(sqlStr);
            preparedStatement.setInt(1, site.getSiteID());
            preparedStatement.setString(2, site.getName());
            preparedStatement.setString(3, site.getDescription());
            preparedStatement.setString(4, site.getAsciiName());
            preparedStatement.setInt(5, site.getRemarkNumber());
            preparedStatement.setString(6, site.getIncrementIndex().trim());
            preparedStatement.setString(7, String.valueOf(site.getUseStatus()));
            preparedStatement.setString(8, String.valueOf(site.getAppserverID()));
            preparedStatement.setInt(9, site.getSiteID());
            preparedStatement.executeUpdate();
            connection.commit();
            int resID = site.getSiteID() + Const.SITE_TYPE_RES;
            StructResource sr = new StructResource();
            sr.setResourceID(Integer.toString(resID));
            sr.setOperateID(Integer.toString(1));
            sr.setOperateTypeID(Const.OPERATE_TYPE_ID);
            sr.setTypeID(Const.RES_TYPE_ID);
            StructAuth sa = new AuthorityManager().getExternalAuthority(sr);
            int authID = sa.getAuthID();
            if (authID == 0) {
                String resName = site.getName();
                int resTypeID = Const.RES_TYPE_ID;
                int operateTypeID = Const.OPERATE_TYPE_ID;
                String remark = "";
                AuthorityManager am = new AuthorityManager();
                am.createExtResource(Integer.toString(resID), resName, resTypeID, operateTypeID, remark);
            }
            site.wirteFile();
        } catch (SQLException ex) {
            connection.rollback();
            log.error("����վ������ʧ��!", ex);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
    }
