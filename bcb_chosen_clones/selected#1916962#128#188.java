    public boolean updateCalculatedHand(CalculateTransferObject query, BasicStartingHandTransferObject[] hands) throws CalculateDAOException {
        boolean retval = false;
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep = null;
        ResultSet result = null;
        StringBuffer sql = new StringBuffer(SELECT_ID_SQL);
        sql.append(appendQuery(query));
        try {
            connection = getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            result = statement.executeQuery(sql.toString());
            if (result.first()) {
                String id = result.getString("id");
                prep = connection.prepareStatement(UPDATE_HANDS_SQL);
                for (int i = 0; i < hands.length; i++) {
                    prep.setInt(1, hands[i].getWins());
                    prep.setInt(2, hands[i].getLoses());
                    prep.setInt(3, hands[i].getDraws());
                    prep.setString(4, id);
                    prep.setString(5, hands[i].getHand());
                    if (prep.executeUpdate() != 1) {
                        throw new SQLException("updated too many records in calculatehands, " + id + "-" + hands[i].getHand());
                    }
                }
                connection.commit();
            }
        } catch (SQLException sqle) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.setNextException(sqle);
                throw new CalculateDAOException(e);
            }
            throw new CalculateDAOException(sqle);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    throw new CalculateDAOException(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new CalculateDAOException(e);
                }
            }
            if (prep != null) {
                try {
                    prep.close();
                } catch (SQLException e) {
                    throw new CalculateDAOException(e);
                }
            }
        }
        return retval;
    }
