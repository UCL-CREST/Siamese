    public static int getNextID(int AD_Client_ID, String TableName, String trxName) {
        if (TableName == null || TableName.length() == 0) throw new IllegalArgumentException("TableName missing");
        int retValue = -1;
        boolean adempiereSys = Ini.isPropertyBool(Ini.P_ADEMPIERESYS);
        if (adempiereSys && AD_Client_ID > 11) adempiereSys = false;
        if (CLogMgt.isLevel(LOGLEVEL)) s_log.log(LOGLEVEL, TableName + " - AdempiereSys=" + adempiereSys + " [" + trxName + "]");
        String selectSQL = null;
        if (DB.isPostgreSQL()) {
            selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_Sequence_ID " + "FROM AD_Sequence " + "WHERE Name=?" + " AND IsActive='Y' AND IsTableID='Y' AND IsAutoSequence='Y' " + " FOR UPDATE OF AD_Sequence ";
            USE_PROCEDURE = false;
        } else if (DB.isOracle()) {
            selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_Sequence_ID " + "FROM AD_Sequence " + "WHERE Name=?" + " AND IsActive='Y' AND IsTableID='Y' AND IsAutoSequence='Y' " + "FOR UPDATE";
            USE_PROCEDURE = true;
        } else {
            selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_Sequence_ID " + "FROM AD_Sequence " + "WHERE Name=?" + " AND IsActive='Y' AND IsTableID='Y' AND IsAutoSequence='Y' ";
            USE_PROCEDURE = false;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        for (int i = 0; i < 3; i++) {
            try {
                conn = DB.getConnectionID();
                if (conn == null) return -1;
                pstmt = conn.prepareStatement(selectSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                pstmt.setString(1, TableName);
                if (!USE_PROCEDURE && DB.getDatabase().isQueryTimeoutSupported()) pstmt.setQueryTimeout(QUERY_TIME_OUT);
                rs = pstmt.executeQuery();
                if (CLogMgt.isLevelFinest()) s_log.finest("AC=" + conn.getAutoCommit() + ", RO=" + conn.isReadOnly() + " - Isolation=" + conn.getTransactionIsolation() + "(" + Connection.TRANSACTION_READ_COMMITTED + ") - RSType=" + pstmt.getResultSetType() + "(" + ResultSet.TYPE_SCROLL_SENSITIVE + "), RSConcur=" + pstmt.getResultSetConcurrency() + "(" + ResultSet.CONCUR_UPDATABLE + ")");
                if (rs.next()) {
                    MTable table = MTable.get(Env.getCtx(), TableName);
                    int AD_Sequence_ID = rs.getInt(4);
                    boolean gotFromHTTP = false;
                    if (adempiereSys) {
                        String isUseCentralizedID = MSysConfig.getValue("DICTIONARY_ID_USE_CENTRALIZED_ID", "Y");
                        if ((!isUseCentralizedID.equals("N")) && (!isExceptionCentralized(TableName))) {
                            retValue = getNextOfficialID_HTTP(TableName);
                            if (retValue > 0) {
                                PreparedStatement updateSQL;
                                updateSQL = conn.prepareStatement("UPDATE AD_Sequence SET CurrentNextSys = ? + 1 WHERE AD_Sequence_ID = ?");
                                try {
                                    updateSQL.setInt(1, retValue);
                                    updateSQL.setInt(2, AD_Sequence_ID);
                                    updateSQL.executeUpdate();
                                } finally {
                                    updateSQL.close();
                                }
                            }
                            gotFromHTTP = true;
                        }
                    }
                    boolean queryProjectServer = false;
                    if (table.getColumn("EntityType") != null) queryProjectServer = true;
                    if (!queryProjectServer && MSequence.Table_Name.equalsIgnoreCase(TableName)) queryProjectServer = true;
                    if (queryProjectServer && (adempiereSys) && (!isExceptionCentralized(TableName))) {
                        String isUseProjectCentralizedID = MSysConfig.getValue("PROJECT_ID_USE_CENTRALIZED_ID", "N");
                        if (isUseProjectCentralizedID.equals("Y")) {
                            retValue = getNextProjectID_HTTP(TableName);
                            if (retValue > 0) {
                                PreparedStatement updateSQL;
                                updateSQL = conn.prepareStatement("UPDATE AD_Sequence SET CurrentNext = GREATEST(CurrentNext, ? + 1) WHERE AD_Sequence_ID = ?");
                                try {
                                    updateSQL.setInt(1, retValue);
                                    updateSQL.setInt(2, AD_Sequence_ID);
                                    updateSQL.executeUpdate();
                                } finally {
                                    updateSQL.close();
                                }
                            }
                            gotFromHTTP = true;
                        }
                    }
                    if (!gotFromHTTP) {
                        if (USE_PROCEDURE) {
                            retValue = nextID(conn, AD_Sequence_ID, adempiereSys);
                        } else {
                            PreparedStatement updateSQL;
                            int incrementNo = rs.getInt(3);
                            if (adempiereSys) {
                                updateSQL = conn.prepareStatement("UPDATE AD_Sequence SET CurrentNextSys = CurrentNextSys + ? WHERE AD_Sequence_ID = ?");
                                retValue = rs.getInt(2);
                            } else {
                                updateSQL = conn.prepareStatement("UPDATE AD_Sequence SET CurrentNext = CurrentNext + ? WHERE AD_Sequence_ID = ?");
                                retValue = rs.getInt(1);
                            }
                            try {
                                updateSQL.setInt(1, incrementNo);
                                updateSQL.setInt(2, AD_Sequence_ID);
                                updateSQL.executeUpdate();
                            } finally {
                                updateSQL.close();
                            }
                        }
                    }
                    conn.commit();
                } else s_log.severe("No record found - " + TableName);
                break;
            } catch (Exception e) {
                s_log.log(Level.SEVERE, TableName + " - " + e.getMessage(), e);
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException e1) {
                }
            } finally {
                DB.close(rs, pstmt);
                pstmt = null;
                rs = null;
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                    conn = null;
                }
            }
            Thread.yield();
        }
        return retValue;
    }
