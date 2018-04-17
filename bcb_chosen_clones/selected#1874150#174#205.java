    public int update(BusinessObject o) throws DAOException {
        int update = 0;
        Item item = (Item) o;
        try {
            PreparedStatement pst = connection.prepareStatement(XMLGetQuery.getQuery("UPDATE_ITEM"));
            pst.setString(1, item.getDescription());
            pst.setDouble(2, item.getUnit_price());
            pst.setInt(3, item.getQuantity());
            pst.setDouble(4, item.getVat());
            pst.setInt(5, item.getIdProject());
            if (item.getIdBill() == 0) pst.setNull(6, java.sql.Types.INTEGER); else pst.setInt(6, item.getIdBill());
            pst.setInt(7, item.getIdCurrency());
            pst.setInt(8, item.getId());
            System.out.println("item => " + item.getDescription() + " " + item.getUnit_price() + " " + item.getQuantity() + " " + item.getVat() + " " + item.getIdProject() + " " + item.getIdBill() + " " + item.getIdCurrency() + " " + item.getId());
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
