    public int update(BusinessObject o) throws DAOException {
        int update = 0;
        Bill bill = (Bill) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("UPDATE_BILL"));
            pst.setInt(1, bill.getId());
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
