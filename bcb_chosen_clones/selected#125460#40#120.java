    public synchronized long nextValue(final Session session) {
        if (sequence < seqLimit) {
            return ++sequence;
        } else {
            final MetaDatabase db = MetaTable.DATABASE.of(table);
            Connection connection = null;
            ResultSet res = null;
            String sql = null;
            PreparedStatement statement = null;
            StringBuilder out = new StringBuilder(64);
            try {
                connection = session.getSeqConnection(db);
                String tableName = db.getDialect().printFullTableName(getTable(), true, out).toString();
                out.setLength(0);
                out.setLength(0);
                sql = db.getDialect().printSequenceNextValue(this, out).toString();
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, sql + "; [" + tableName + ']');
                }
                statement = connection.prepareStatement(sql);
                statement.setString(1, tableName);
                int i = statement.executeUpdate();
                if (i == 0) {
                    out.setLength(0);
                    sql = db.getDialect().printSequenceInit(this, out).toString();
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, sql + "; [" + tableName + ']');
                    }
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, tableName);
                    statement.executeUpdate();
                }
                out.setLength(0);
                sql = db.getDialect().printSequenceCurrentValue(this, out).toString();
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, sql + "; [" + tableName + ']');
                }
                statement = connection.prepareStatement(sql);
                statement.setString(1, tableName);
                res = statement.executeQuery();
                res.next();
                seqLimit = res.getLong(1);
                int step = res.getInt(2);
                maxValue = res.getLong(3);
                sequence = (seqLimit - step) + 1;
                if (maxValue != 0L) {
                    if (seqLimit > maxValue) {
                        seqLimit = maxValue;
                        if (sequence > maxValue) {
                            String msg = "The sequence '" + tableName + "' needs to raise the maximum value: " + maxValue;
                            throw new IllegalStateException(msg);
                        }
                        statement.close();
                        sql = db.getDialect().printSetMaxSequence(this, out).toString();
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(Level.INFO, sql + "; [" + tableName + ']');
                        }
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, tableName);
                        statement.execute();
                    }
                    if (maxValue > Long.MAX_VALUE - step) {
                        String msg = "The sequence attribute '" + tableName + ".maxValue' is too hight," + " the recommended maximal value is: " + (Long.MAX_VALUE - step) + " (Long.MAX_VALUE-step)";
                        LOGGER.log(Level.WARNING, msg);
                    }
                }
                connection.commit();
            } catch (Throwable e) {
                if (connection != null) try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Rollback fails");
                }
                IllegalStateException exception = e instanceof IllegalStateException ? (IllegalStateException) e : new IllegalStateException("ILLEGAL SQL: " + sql, e);
                throw exception;
            } finally {
                MetaDatabase.close(null, statement, res, true);
            }
            return sequence;
        }
    }
