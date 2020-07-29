    public int update(BusinessObject o) throws DAOException {
        int update = 0;
        Project project = (Project) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("UPDATE_PROJECT"));
            pst.setString(1, project.getName());
            pst.setString(2, project.getDescription());
            pst.setInt(3, project.getIdAccount());
            pst.setInt(4, project.getIdContact());
            pst.setBoolean(5, project.isArchived());
            pst.setInt(6, project.getId());
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
