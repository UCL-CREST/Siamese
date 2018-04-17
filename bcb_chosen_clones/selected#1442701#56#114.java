    private Integer getInt(String sequence) throws NoSuchSequenceException {
        Connection conn = null;
        PreparedStatement read = null;
        PreparedStatement write = null;
        boolean success = false;
        try {
            conn = ds.getConnection();
            conn.setTransactionIsolation(conn.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);
            read = conn.prepareStatement(SELECT_SQL);
            read.setString(1, sequence);
            ResultSet readRs = read.executeQuery();
            if (!readRs.next()) {
                throw new NoSuchSequenceException();
            }
            int currentSequenceId = readRs.getInt(1);
            int currentSequenceValue = readRs.getInt(2);
            Integer currentSequenceValueInteger = new Integer(currentSequenceValue);
            write = conn.prepareStatement(UPDATE_SQL);
            write.setInt(1, currentSequenceValue + 1);
            write.setInt(2, currentSequenceId);
            int rowsAffected = write.executeUpdate();
            if (rowsAffected == 1) {
                success = true;
                return currentSequenceValueInteger;
            } else {
                logger.error("Something strange has happened.  The row count was not 1, but was " + rowsAffected);
                return currentSequenceValueInteger;
            }
        } catch (SQLException sqle) {
            logger.error("Table based id generation failed : ");
            logger.error(sqle.getMessage());
            return new Integer(0);
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (Exception e) {
                }
            }
            if (write != null) {
                try {
                    write.close();
                } catch (Exception e) {
                }
            }
            if (conn != null) {
                try {
                    if (success) {
                        conn.commit();
                    } else {
                        conn.rollback();
                    }
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
    }
