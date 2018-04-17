    public void insert(IIDGenerator idGenerators, AIDADocument item) throws SQLException {
        AIDAActivityObjectDB.getManager(token).insert(idGenerators, item);
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(DOC_INSERT);
            ps.setLong(1, item.getId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getRelativeLink());
            ps.executeUpdate();
            ps.close();
            insertDescriptions(con, item);
        } catch (SQLException sqlEx) {
            con.rollback();
            throw sqlEx;
        } finally {
            con.close();
        }
        return;
    }
