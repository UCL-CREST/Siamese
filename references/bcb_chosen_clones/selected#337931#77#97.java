    @Override
    public void makeRead(final String user, final long databaseID, final long time) throws SQLException {
        final String query = "insert into fs.read_post (post, user, read_date) values (?, ?, ?)";
        ensureConnection();
        final PreparedStatement statement = m_connection.prepareStatement(query);
        try {
            statement.setLong(1, databaseID);
            statement.setString(2, user);
            statement.setTimestamp(3, new Timestamp(time));
            final int count = statement.executeUpdate();
            if (0 == count) {
                throw new SQLException("Nothing updated.");
            }
            m_connection.commit();
        } catch (final SQLException e) {
            m_connection.rollback();
            throw e;
        } finally {
            statement.close();
        }
    }
