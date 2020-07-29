    public void removeStadium(String name, String city) throws StadiumException {
        Connection conn = ConnectionManager.getManager().getConnection();
        int id = findStadiumBy_N_C(name, city);
        if (id == -1) throw new StadiumException("No such stadium");
        try {
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement(Statements.SELECT_STAD_TRIBUNE);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            TribuneLogic logic = TribuneLogic.getInstance();
            while (rs.next()) {
                logic.removeTribune(rs.getInt("tribuneID"));
            }
            stm = conn.prepareStatement(Statements.DELETE_STADIUM);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new StadiumException("Removing stadium failed", e);
        }
        try {
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
