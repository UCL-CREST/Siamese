    public void addUser(String name, String unit, String organizeName, int userId, int orgId, String email) {
        Connection connection = null;
        PreparedStatement ps = null;
        DBOperation dbo = factory.createDBOperation(POOL_NAME);
        try {
            connection = dbo.getConnection();
            ps = connection.prepareStatement(INSERT_USER);
            ps.setInt(1, AddrslistMainDao.getNewID());
            ps.setInt(2, -100);
            ps.setString(3, name.substring(0, 1));
            ps.setString(4, name.substring(1));
            ps.setString(5, unit);
            ps.setString(6, organizeName);
            ps.setString(7, "");
            ps.setString(8, email);
            ps.setString(9, "");
            ps.setString(10, "");
            ps.setString(11, "");
            ps.setString(12, "");
            ps.setString(13, "");
            ps.setString(14, "");
            ps.setString(15, "");
            ps.setString(16, "");
            ps.setString(17, "");
            ps.setString(18, "");
            ps.setInt(19, userId);
            ps.setInt(20, orgId);
            ps.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
            }
        } finally {
            try {
                ps.close();
                connection.close();
                dbo.close();
            } catch (Exception e) {
            }
        }
    }
