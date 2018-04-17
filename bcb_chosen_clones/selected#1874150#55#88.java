    public int create(BusinessObject o) throws DAOException {
        int insert = 0;
        int id = 0;
        Item item = (Item) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("INSERT_ITEM"));
            pst.setString(1, item.getDescription());
            pst.setDouble(2, item.getUnit_price());
            pst.setInt(3, item.getQuantity());
            pst.setDouble(4, item.getVat());
            pst.setInt(5, item.getIdProject());
            pst.setInt(6, item.getIdCurrency());
            insert = pst.executeUpdate();
            if (insert <= 0) {
                connection.rollback();
                throw new DAOException("Number of rows <= 0");
            } else if (insert > 1) {
                connection.rollback();
                throw new DAOException("Number of rows > 1");
            }
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select max(id_item) from item");
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
