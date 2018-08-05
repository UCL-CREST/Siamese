    public int delete(BusinessObject o) throws DAOException {
        int delete = 0;
        Contact contact = (Contact) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("DELETE_CONTACT"));
            pst.setInt(1, contact.getId());
            delete = pst.executeUpdate();
            if (delete <= 0) {
                connection.rollback();
                throw new DAOException("Number of rows <= 0");
            } else if (delete > 1) {
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
        return delete;
    }
