    protected int executeUpdates(List<UpdateStatement> statements, OlVersionCheck olVersionCheck) throws DaoException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start executeUpdates");
        }
        PreparedStatement stmt = null;
        Connection conn = null;
        int rowsAffected = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            conn.rollback();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            if (olVersionCheck != null) {
                stmt = conn.prepareStatement(olVersionCheck.getQuery());
                stmt.setObject(1, olVersionCheck.getId());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                Number olVersion = (Number) rs.getObject("olVersion");
                stmt.close();
                stmt = null;
                if (olVersion.intValue() != olVersionCheck.getOlVersionToCheck().intValue()) {
                    rowsAffected = -1;
                }
            }
            if (rowsAffected >= 0) {
                for (UpdateStatement query : statements) {
                    stmt = conn.prepareStatement(query.getQuery());
                    if (query.getParams() != null) {
                        for (int parameterIndex = 1; parameterIndex <= query.getParams().length; parameterIndex++) {
                            Object object = query.getParams()[parameterIndex - 1];
                            stmt.setObject(parameterIndex, object);
                        }
                    }
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(" **** Sending statement:\n" + query.getQuery());
                    }
                    rowsAffected += stmt.executeUpdate();
                    stmt.close();
                    stmt = null;
                }
            }
            conn.commit();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                LOGGER.info("Integrity constraint violation", e);
                throw new UniqueConstaintException();
            }
            throw new DaoException("error.databaseError", e);
        } finally {
            try {
                if (stmt != null) {
                    LOGGER.debug("closing open statement!");
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new DaoException("error.databaseError", e);
            } finally {
                stmt = null;
            }
            try {
                if (conn != null) {
                    LOGGER.debug("rolling back open connection!");
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                throw new DaoException("error.databaseError", e);
            } finally {
                conn = null;
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("finish executeUpdates");
        }
        return rowsAffected;
    }
