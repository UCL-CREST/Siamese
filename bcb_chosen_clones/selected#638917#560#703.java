    public Message[] expunge() throws MessagingException {
        Statement oStmt = null;
        CallableStatement oCall = null;
        PreparedStatement oUpdt = null;
        ResultSet oRSet;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBFolder.expunge()");
            DebugFile.incIdent();
        }
        if (0 == (iOpenMode & READ_WRITE)) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new javax.mail.FolderClosedException(this, "Folder is not open is READ_WRITE mode");
        }
        if ((0 == (iOpenMode & MODE_MBOX)) && (0 == (iOpenMode & MODE_BLOB))) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new javax.mail.FolderClosedException(this, "Folder is not open in MBOX nor BLOB mode");
        }
        MboxFile oMBox = null;
        DBSubset oDeleted = new DBSubset(DB.k_mime_msgs, DB.gu_mimemsg + "," + DB.pg_message, DB.bo_deleted + "=1 AND " + DB.gu_category + "='" + oCatg.getString(DB.gu_category) + "'", 100);
        try {
            int iDeleted = oDeleted.load(getConnection());
            File oFile = getFile();
            if (oFile.exists() && iDeleted > 0) {
                oMBox = new MboxFile(oFile, MboxFile.READ_WRITE);
                int[] msgnums = new int[iDeleted];
                for (int m = 0; m < iDeleted; m++) msgnums[m] = oDeleted.getInt(1, m);
                oMBox.purge(msgnums);
                oMBox.close();
            }
            oStmt = oConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            oRSet = oStmt.executeQuery("SELECT p." + DB.file_name + " FROM " + DB.k_mime_parts + " p," + DB.k_mime_msgs + " m WHERE p." + DB.gu_mimemsg + "=m." + DB.gu_mimemsg + " AND m." + DB.id_disposition + "='reference' AND m." + DB.bo_deleted + "=1 AND m." + DB.gu_category + "='" + oCatg.getString(DB.gu_category) + "'");
            while (oRSet.next()) {
                String sFileName = oRSet.getString(1);
                if (!oRSet.wasNull()) {
                    try {
                        File oRef = new File(sFileName);
                        oRef.delete();
                    } catch (SecurityException se) {
                        if (DebugFile.trace) DebugFile.writeln("SecurityException " + sFileName + " " + se.getMessage());
                    }
                }
            }
            oRSet.close();
            oRSet = null;
            oStmt.close();
            oStmt = null;
            oFile = getFile();
            oStmt = oConn.createStatement();
            oStmt.executeUpdate("UPDATE " + DB.k_categories + " SET " + DB.len_size + "=" + String.valueOf(oFile.length()) + " WHERE " + DB.gu_category + "='" + getCategory().getString(DB.gu_category) + "'");
            oStmt.close();
            oStmt = null;
            if (oConn.getDataBaseProduct() == JDCConnection.DBMS_POSTGRESQL) {
                oStmt = oConn.createStatement();
                for (int d = 0; d < iDeleted; d++) oStmt.executeQuery("SELECT k_sp_del_mime_msg('" + oDeleted.getString(0, d) + "')");
                oStmt.close();
                oStmt = null;
            } else {
                oCall = oConn.prepareCall("{ call k_sp_del_mime_msg(?) }");
                for (int d = 0; d < iDeleted; d++) {
                    oCall.setString(1, oDeleted.getString(0, d));
                    oCall.execute();
                }
                oCall.close();
                oCall = null;
            }
            if (oFile.exists() && iDeleted > 0) {
                BigDecimal oUnit = new BigDecimal(1);
                oStmt = oConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                oRSet = oStmt.executeQuery("SELECT MAX(" + DB.pg_message + ") FROM " + DB.k_mime_msgs + " WHERE " + DB.gu_category + "='getCategory().getString(DB.gu_category)'");
                oRSet.next();
                BigDecimal oMaxPg = oRSet.getBigDecimal(1);
                if (oRSet.wasNull()) oMaxPg = new BigDecimal(0);
                oRSet.close();
                oRSet = null;
                oStmt.close();
                oStmt = null;
                oMaxPg = oMaxPg.add(oUnit);
                oStmt = oConn.createStatement();
                oStmt.executeUpdate("UPDATE " + DB.k_mime_msgs + " SET " + DB.pg_message + "=" + DB.pg_message + "+" + oMaxPg.toString() + " WHERE " + DB.gu_category + "='" + getCategory().getString(DB.gu_category) + "'");
                oStmt.close();
                oStmt = null;
                DBSubset oMsgSet = new DBSubset(DB.k_mime_msgs, DB.gu_mimemsg + "," + DB.pg_message, DB.gu_category + "='" + getCategory().getString(DB.gu_category) + "' ORDER BY " + DB.pg_message, 1000);
                int iMsgCount = oMsgSet.load(oConn);
                oMBox = new MboxFile(oFile, MboxFile.READ_ONLY);
                long[] aPositions = oMBox.getMessagePositions();
                oMBox.close();
                if (iMsgCount != aPositions.length) {
                    throw new IOException("DBFolder.expunge() Message count of " + String.valueOf(aPositions.length) + " at MBOX file " + oFile.getName() + " does not match message count at database index of " + String.valueOf(iMsgCount));
                }
                oMaxPg = new BigDecimal(0);
                oUpdt = oConn.prepareStatement("UPDATE " + DB.k_mime_msgs + " SET " + DB.pg_message + "=?," + DB.nu_position + "=? WHERE " + DB.gu_mimemsg + "=?");
                for (int m = 0; m < iMsgCount; m++) {
                    oUpdt.setBigDecimal(1, oMaxPg);
                    oUpdt.setBigDecimal(2, new BigDecimal(aPositions[m]));
                    oUpdt.setString(3, oMsgSet.getString(0, m));
                    oUpdt.executeUpdate();
                    oMaxPg = oMaxPg.add(oUnit);
                }
                oUpdt.close();
            }
            oConn.commit();
        } catch (SQLException sqle) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception e) {
            }
            try {
                if (oStmt != null) oStmt.close();
            } catch (Exception e) {
            }
            try {
                if (oCall != null) oCall.close();
            } catch (Exception e) {
            }
            try {
                if (oConn != null) oConn.rollback();
            } catch (Exception e) {
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        } catch (IOException sqle) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception e) {
            }
            try {
                if (oStmt != null) oStmt.close();
            } catch (Exception e) {
            }
            try {
                if (oCall != null) oCall.close();
            } catch (Exception e) {
            }
            try {
                if (oConn != null) oConn.rollback();
            } catch (Exception e) {
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DBFolder.expunge()");
        }
        return null;
    }
