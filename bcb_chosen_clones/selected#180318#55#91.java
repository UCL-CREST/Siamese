    public int create(BusinessObject o) throws DAOException {
        int insert = 0;
        int id = 0;
        Contact contact = (Contact) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("INSERT_CONTACT"));
            pst.setString(1, contact.getName());
            pst.setString(2, contact.getFirstname());
            pst.setString(3, contact.getPhone());
            pst.setString(4, contact.getEmail());
            if (contact.getAccount() == 0) {
                pst.setNull(5, java.sql.Types.INTEGER);
            } else {
                pst.setInt(5, contact.getAccount());
            }
            insert = pst.executeUpdate();
            if (insert <= 0) {
                connection.rollback();
                throw new DAOException("Number of rows <= 0");
            } else if (insert > 1) {
                connection.rollback();
                throw new DAOException("Number of rows > 1");
            }
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from contact");
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
