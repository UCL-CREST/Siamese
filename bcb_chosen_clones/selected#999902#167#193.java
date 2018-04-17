    public int update(BusinessObject o) throws DAOException {
        int update = 0;
        Currency curr = (Currency) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("UPDATE_CURRENCY"));
            pst.setString(1, curr.getName());
            pst.setInt(2, curr.getIdBase());
            pst.setDouble(3, curr.getValue());
            pst.setInt(4, curr.getId());
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
