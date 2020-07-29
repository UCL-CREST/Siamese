    private void insert() throws SQLException, NamingException {
        Logger logger = getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("enter - " + getClass().getName() + ".insert()");
        }
        try {
            if (logger.isInfoEnabled()) {
                logger.info("insert(): Create new sequencer record for " + getName());
            }
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup(dataSourceName);
                conn = ds.getConnection();
                conn.setReadOnly(false);
                stmt = conn.prepareStatement(INSERT_SEQ);
                stmt.setString(INS_NAME, getName());
                stmt.setLong(INS_NEXT_KEY, defaultInterval * 2);
                stmt.setLong(INS_INTERVAL, defaultInterval);
                stmt.setLong(INS_UPDATE, System.currentTimeMillis());
                try {
                    if (stmt.executeUpdate() != 1) {
                        nextId = -1L;
                        logger.warn("insert(): Failed to create sequencer entry for " + getName() + " (no error message)");
                    } else if (logger.isInfoEnabled()) {
                        nextId = defaultInterval;
                        nextSeed = defaultInterval * 2;
                        interval = defaultInterval;
                        logger.info("insert(): First ID will be " + nextId);
                    }
                } catch (SQLException e) {
                    logger.warn("insert(): Error inserting row into database, possible concurrency issue: " + e.getMessage());
                    if (logger.isDebugEnabled()) {
                        e.printStackTrace();
                    }
                    nextId = -1L;
                }
                if (!conn.getAutoCommit()) {
                    conn.commit();
                }
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ignore) {
                    }
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ignore) {
                    }
                }
                if (conn != null) {
                    if (!conn.getAutoCommit()) {
                        try {
                            conn.rollback();
                        } catch (SQLException ignore) {
                        }
                    }
                    try {
                        conn.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("exit - " + getClass().getName() + ".insert()");
            }
        }
    }
