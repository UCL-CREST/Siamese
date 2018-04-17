    public void doNew(Vector userId, String shareFlag, String folderId) throws AddrException {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rset = null;
        String sql = "insert into " + SHARE_TABLE + " values(?,?,?)";
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            for (int i = 0; i < userId.size(); i++) {
                String user = (String) userId.elementAt(i);
                ps = connection.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(folderId));
                ps.setInt(2, Integer.parseInt(user));
                ps.setString(3, shareFlag);
                int resultCount = ps.executeUpdate();
                if (resultCount != 1) {
                    throw new Exception("error");
                }
            }
            connection.commit();
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            logger.error("���������ļ�����Ϣʧ��, " + ex.getMessage());
            throw new AddrException("���������ļ�����Ϣʧ��,һ�������ļ���ֻ�ܹ����ͬһ�û�һ��!");
        } finally {
            close(rset, null, ps, connection, dbo);
        }
    }
