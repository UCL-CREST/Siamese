            public Integer execute(Connection con) throws SQLException {
                int updateCount = 0;
                boolean oldAutoCommitSetting = con.getAutoCommit();
                Statement stmt = null;
                try {
                    con.setAutoCommit(autoCommit);
                    stmt = con.createStatement();
                    int statementCount = 0;
                    for (String statement : sql) {
                        try {
                            updateCount += stmt.executeUpdate(statement);
                            statementCount++;
                            if (statementCount % commitRate == 0 && !autoCommit) {
                                con.commit();
                            }
                        } catch (SQLException ex) {
                            if (!failOnError) {
                                log.log(LogLevel.WARN, "%s.  Failed to execute: %s.", ex.getMessage(), sql);
                            } else {
                                throw translate(statement, ex);
                            }
                        }
                    }
                    if (!autoCommit) {
                        con.commit();
                    }
                    return updateCount;
                } catch (SQLException ex) {
                    if (!autoCommit) {
                        con.rollback();
                    }
                    throw ex;
                } finally {
                    close(stmt);
                    con.setAutoCommit(oldAutoCommitSetting);
                }
            }
