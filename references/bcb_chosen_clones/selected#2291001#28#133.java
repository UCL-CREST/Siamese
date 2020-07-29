    public String getNextSequence(Integer id) throws ApplicationException {
        java.sql.PreparedStatement preStat = null;
        java.sql.ResultSet rs = null;
        boolean noRecordMatch = false;
        String prefix = "";
        String suffix = "";
        Long startID = null;
        Integer length = null;
        Long currID = null;
        Integer increment = null;
        int nextID;
        String formReferenceID = null;
        synchronized (lock) {
            synchronized (dbConn) {
                try {
                    preStat = dbConn.prepareStatement("SELECT PREFIX,SUFFIX,START_NO,LENGTH,CURRENT_NO,INCREMENT FROM FORM_RECORD WHERE ID=?");
                    setPrepareStatement(preStat, 1, id);
                    rs = preStat.executeQuery();
                    if (rs.next()) {
                        prefix = rs.getString(1);
                        suffix = rs.getString(2);
                        startID = new Long(rs.getLong(3));
                        length = new Integer(rs.getInt(4));
                        currID = new Long(rs.getLong(5));
                        increment = new Integer(rs.getInt(6));
                        if (Utility.isEmpty(startID) || Utility.isEmpty(length) || Utility.isEmpty(currID) || Utility.isEmpty(increment) || startID.intValue() < 0 || length.intValue() < startID.toString().length() || currID.intValue() < startID.intValue() || increment.intValue() < 1 || new Integer(increment.intValue() + currID.intValue()).toString().length() > length.intValue()) {
                            noRecordMatch = true;
                        } else {
                            if (!Utility.isEmpty(prefix)) {
                                formReferenceID = prefix;
                            }
                            String strCurrID = currID.toString();
                            for (int i = 0; i < length.intValue() - strCurrID.length(); i++) {
                                formReferenceID += "0";
                            }
                            formReferenceID += strCurrID;
                            if (!Utility.isEmpty(suffix)) {
                                formReferenceID += suffix;
                            }
                        }
                    } else {
                        noRecordMatch = true;
                    }
                } catch (Exception e) {
                    log.error(e, e);
                    try {
                        dbConn.close();
                    } catch (Exception ignore) {
                    } finally {
                        dbConn = null;
                    }
                    throw new ApplicationException("errors.framework.get_next_seq", e);
                } finally {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    } finally {
                        rs = null;
                    }
                    try {
                        preStat.close();
                    } catch (Exception ignore) {
                    } finally {
                        preStat = null;
                    }
                }
                if (!noRecordMatch && formReferenceID != null) {
                    try {
                        int updateCnt = 0;
                        nextID = currID.intValue() + increment.intValue();
                        do {
                            preStat = dbConn.prepareStatement("UPDATE FORM_RECORD SET CURRENT_NO=? WHERE ID=?");
                            setPrepareStatement(preStat, 1, new Integer(nextID));
                            setPrepareStatement(preStat, 2, id);
                            updateCnt = preStat.executeUpdate();
                            if (updateCnt == 0) {
                                Thread.sleep(50);
                            }
                        } while (updateCnt == 0);
                        dbConn.commit();
                    } catch (Exception e) {
                        log.error(e, e);
                        try {
                            dbConn.rollback();
                        } catch (Exception ignore) {
                        }
                        throw new ApplicationException("errors.framework.get_next_seq", e);
                    } finally {
                        try {
                            preStat.close();
                        } catch (Exception ignore) {
                        } finally {
                            preStat = null;
                        }
                        try {
                            dbConn.close();
                        } catch (Exception ignore) {
                        } finally {
                            dbConn = null;
                        }
                    }
                }
                return formReferenceID;
            }
        }
    }
