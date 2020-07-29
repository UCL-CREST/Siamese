    public void delete(Channel channel) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            String[] selfDefinePath = getSelfDefinePath(channel.getPath(), "1", connection, preparedStatement, resultSet);
            selfDefineDelete(selfDefinePath, connection, preparedStatement);
            String sqlStr = "delete from t_ip_channel where channel_path=?";
            preparedStatement = connection.prepareStatement(sqlStr);
            preparedStatement.setString(1, channel.getPath());
            preparedStatement.executeUpdate();
            sqlStr = "delete from t_ip_channel_order where channel_order_site = ?";
            preparedStatement.setString(1, channel.getPath());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            log.error("ɾ��Ƶ��ʧ�ܣ�channelPath=" + channel.getPath(), ex);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
    }
