    public static void reset() throws Exception {
        Session session = DataStaticService.getHibernateSessionFactory().openSession();
        try {
            Connection connection = session.connection();
            try {
                Statement statement = connection.createStatement();
                try {
                    statement.executeUpdate("delete from Tag");
                    connection.commit();
                } finally {
                    statement.close();
                }
            } catch (HibernateException e) {
                connection.rollback();
                throw new Exception(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new Exception(e);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            session.close();
        }
    }
