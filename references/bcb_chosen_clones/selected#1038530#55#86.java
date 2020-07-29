    public int create(BusinessObject o) throws DAOException {
        int insert = 0;
        int id = 0;
        Project project = (Project) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("INSERT_PROJECT"));
            pst.setString(1, project.getName());
            pst.setString(2, project.getDescription());
            pst.setInt(3, project.getIdAccount());
            pst.setInt(4, project.getIdContact());
            insert = pst.executeUpdate();
            if (insert <= 0) {
                connection.rollback();
                throw new DAOException("Number of rows <= 0");
            } else if (insert > 1) {
                connection.rollback();
                throw new DAOException("Number of rows > 1");
            }
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select max(id_project) from project");
            rs.next();
            id = rs.getInt(1);
            connection.commit();
        } catch (SQLException e) {
            Log.write(e.getMessage());
            throw new DAOException("A SQLException has occured");
        } catch (NullPointerException npe) {
            Log.write(npe.getMessage());
            throw new DAOException("Connection null");
        }
        return id;
    }
