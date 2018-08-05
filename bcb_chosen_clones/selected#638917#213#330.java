    public void moveMessage(DBMimeMessage oSrcMsg) throws MessagingException {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBFolder.moveMessage()");
            DebugFile.incIdent();
        }
        JDCConnection oConn = null;
        PreparedStatement oStmt = null;
        ResultSet oRSet = null;
        BigDecimal oPg = null;
        BigDecimal oPos = null;
        int iLen = 0;
        try {
            oConn = ((DBStore) getStore()).getConnection();
            oStmt = oConn.prepareStatement("SELECT " + DB.pg_message + "," + DB.nu_position + "," + DB.len_mimemsg + " FROM " + DB.k_mime_msgs + " WHERE " + DB.gu_mimemsg + "=?");
            oStmt.setString(1, oSrcMsg.getMessageGuid());
            oRSet = oStmt.executeQuery();
            if (oRSet.next()) {
                oPg = oRSet.getBigDecimal(1);
                oPos = oRSet.getBigDecimal(2);
                iLen = oRSet.getInt(3);
            }
            oRSet.close();
            oRSet = null;
            oStmt.close();
            oStmt = null;
            oConn.setAutoCommit(false);
            oStmt = oConn.prepareStatement("UPDATE " + DB.k_categories + " SET " + DB.len_size + "=" + DB.len_size + "-" + String.valueOf(iLen) + " WHERE " + DB.gu_category + "=?");
            oStmt.setString(1, ((DBFolder) (oSrcMsg.getFolder())).getCategory().getString(DB.gu_category));
            oStmt.executeUpdate();
            oStmt.close();
            oStmt = null;
            oStmt = oConn.prepareStatement("UPDATE " + DB.k_categories + " SET " + DB.len_size + "=" + DB.len_size + "+" + String.valueOf(iLen) + " WHERE " + DB.gu_category + "=?");
            oStmt.setString(1, getCategory().getString(DB.gu_category));
            oStmt.executeUpdate();
            oStmt.close();
            oStmt = null;
            oConn.commit();
        } catch (SQLException sqle) {
            if (null != oRSet) {
                try {
                    oRSet.close();
                } catch (Exception ignore) {
                }
            }
            if (null != oStmt) {
                try {
                    oStmt.close();
                } catch (Exception ignore) {
                }
            }
            if (null != oConn) {
                try {
                    oConn.rollback();
                } catch (Exception ignore) {
                }
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        if (null == oPg) throw new MessagingException("Source message not found");
        if (null == oPos) throw new MessagingException("Source message position is not valid");
        DBFolder oSrcFldr = (DBFolder) oSrcMsg.getFolder();
        MboxFile oMboxSrc = null, oMboxThis = null;
        try {
            oMboxSrc = new MboxFile(oSrcFldr.getFile(), MboxFile.READ_WRITE);
            oMboxThis = new MboxFile(oSrcFldr.getFile(), MboxFile.READ_WRITE);
            oMboxThis.appendMessage(oMboxSrc, oPos.longValue(), iLen);
            oMboxThis.close();
            oMboxThis = null;
            oMboxSrc.purge(new int[] { oPg.intValue() });
            oMboxSrc.close();
            oMboxSrc = null;
        } catch (Exception e) {
            if (oMboxThis != null) {
                try {
                    oMboxThis.close();
                } catch (Exception ignore) {
                }
            }
            if (oMboxSrc != null) {
                try {
                    oMboxSrc.close();
                } catch (Exception ignore) {
                }
            }
            throw new MessagingException(e.getMessage(), e);
        }
        try {
            oConn = ((DBStore) getStore()).getConnection();
            BigDecimal dNext = getNextMessage();
            String sCatGuid = getCategory().getString(DB.gu_category);
            oStmt = oConn.prepareStatement("UPDATE " + DB.k_mime_msgs + " SET " + DB.gu_category + "=?," + DB.pg_message + "=? WHERE " + DB.gu_mimemsg + "=?");
            oStmt.setString(1, sCatGuid);
            oStmt.setBigDecimal(2, dNext);
            oStmt.setString(3, oSrcMsg.getMessageGuid());
            oStmt.executeUpdate();
            oStmt.close();
            oStmt = null;
            oConn.commit();
        } catch (SQLException sqle) {
            if (null != oStmt) {
                try {
                    oStmt.close();
                } catch (Exception ignore) {
                }
            }
            if (null != oConn) {
                try {
                    oConn.rollback();
                } catch (Exception ignore) {
                }
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DBFolder.moveMessage()");
        }
    }
