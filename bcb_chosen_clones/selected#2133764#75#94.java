    public void addPropertyColumns(WCAChannel destination, Set<Property> properties) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Connection con = session.connection();
        try {
            createPropertyTable(destination);
            extendPropertyList(destination, properties);
            Statement statement = con.createStatement();
            for (Property property : properties) {
                String propertyName = removeBadChars(property.getName());
                statement.executeUpdate(alterTable.format(new Object[] { getTableName(destination), propertyName, property.getDBColumnType() }));
            }
            con.commit();
            con.close();
            session.close();
        } catch (SQLException e) {
            con.rollback();
            session.close();
            throw e;
        }
    }
