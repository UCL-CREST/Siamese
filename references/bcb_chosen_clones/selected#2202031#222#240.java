    public void removeGames(List<Game> games) throws SQLException {
        Connection conn = ConnectionManager.getManager().getConnection();
        PreparedStatement stm = null;
        conn.setAutoCommit(false);
        try {
            for (Game game : games) {
                stm = conn.prepareStatement(Statements.DELETE_GAME);
                stm.setInt(1, game.getGameID());
                stm.executeUpdate();
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (stm != null) stm.close();
        }
        conn.commit();
        conn.setAutoCommit(true);
    }
