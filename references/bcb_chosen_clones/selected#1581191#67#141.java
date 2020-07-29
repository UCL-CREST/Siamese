    public void removeBodyPart(int iPart) throws MessagingException, ArrayIndexOutOfBoundsException {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBMimeMultipart.removeBodyPart(" + String.valueOf(iPart) + ")");
            DebugFile.incIdent();
        }
        DBMimeMessage oMsg = (DBMimeMessage) getParent();
        DBFolder oFldr = ((DBFolder) oMsg.getFolder());
        Statement oStmt = null;
        ResultSet oRSet = null;
        String sDisposition = null, sFileName = null;
        boolean bFound;
        try {
            oStmt = oFldr.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(SELECT " + DB.id_disposition + "," + DB.file_name + " FROM " + DB.k_mime_parts + " WHERE " + DB.gu_mimemsg + "='" + oMsg.getMessageGuid() + "' AND " + DB.id_part + "=" + String.valueOf(iPart) + ")");
            oRSet = oStmt.executeQuery("SELECT " + DB.id_disposition + "," + DB.file_name + " FROM " + DB.k_mime_parts + " WHERE " + DB.gu_mimemsg + "='" + oMsg.getMessageGuid() + "' AND " + DB.id_part + "=" + String.valueOf(iPart));
            bFound = oRSet.next();
            if (bFound) {
                sDisposition = oRSet.getString(1);
                if (oRSet.wasNull()) sDisposition = "inline";
                sFileName = oRSet.getString(2);
            }
            oRSet.close();
            oRSet = null;
            oStmt.close();
            oStmt = null;
            if (!bFound) {
                if (DebugFile.trace) DebugFile.decIdent();
                throw new MessagingException("Part not found");
            }
            if (!sDisposition.equals("reference") && !sDisposition.equals("pointer")) {
                if (DebugFile.trace) DebugFile.decIdent();
                throw new MessagingException("Only parts with reference or pointer disposition can be removed from a message");
            } else {
                if (sDisposition.equals("reference")) {
                    try {
                        File oRef = new File(sFileName);
                        if (oRef.exists()) oRef.delete();
                    } catch (SecurityException se) {
                        if (DebugFile.trace) DebugFile.writeln("SecurityException " + sFileName + " " + se.getMessage());
                        if (DebugFile.trace) DebugFile.decIdent();
                        throw new MessagingException("SecurityException " + sFileName + " " + se.getMessage(), se);
                    }
                }
                oStmt = oFldr.getConnection().createStatement();
                if (DebugFile.trace) DebugFile.writeln("Statement.executeUpdate(DELETE FROM " + DB.k_mime_parts + " WHERE " + DB.gu_mimemsg + "='" + oMsg.getMessageGuid() + "' AND " + DB.id_part + "=" + String.valueOf(iPart) + ")");
                oStmt.executeUpdate("DELETE FROM " + DB.k_mime_parts + " WHERE " + DB.gu_mimemsg + "='" + oMsg.getMessageGuid() + "' AND " + DB.id_part + "=" + String.valueOf(iPart));
                oStmt.close();
                oStmt = null;
                oFldr.getConnection().commit();
            }
        } catch (SQLException sqle) {
            if (oRSet != null) {
                try {
                    oRSet.close();
                } catch (Exception ignore) {
                }
            }
            if (oStmt != null) {
                try {
                    oStmt.close();
                } catch (Exception ignore) {
                }
            }
            try {
                oFldr.getConnection().rollback();
            } catch (Exception ignore) {
            }
            if (DebugFile.trace) DebugFile.decIdent();
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DBMimeMultipart.removeBodyPart()");
        }
    }
