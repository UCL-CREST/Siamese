    public int update(BusinessObject o) throws DAOException {
        int update = 0;
        Account acc = (Account) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("UPDATE_ACCOUNT"));
            pst.setString(1, acc.getName());
            pst.setString(2, acc.getAddress());
            pst.setInt(3, acc.getCurrency());
            pst.setInt(4, acc.getMainContact());
            pst.setBoolean(5, acc.isArchived());
            pst.setInt(6, acc.getId());
            update = pst.executeUpdate();
            if (update <= 0) {
                connection.rollback();
                throw new DAOException("Number of rows <= 0");
            } else if (update > 1) {
                connection.rollback();
                throw new DAOException("Number of rows > 1");
            }
            connection.commit();
        } catch (SQLException e) {
            Log.write(e.getMessage());
            throw new DAOException("A SQLException has occured");
        } catch (NullPointerException npe) {
            Log.write(npe.getMessage());
            throw new DAOException("Connection null");
        }
        return update;
    }
