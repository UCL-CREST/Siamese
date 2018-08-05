    private void saveMimeParts(MimeMessage oMsg, String sMsgCharSeq, String sBoundary, String sMsgGuid, String sMsgId, int iPgMessage, int iOffset) throws MessagingException, OutOfMemoryError {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBFolder.saveMimeParts([Connection], [MimeMessage], " + sBoundary + ", " + sMsgGuid + "," + sMsgId + ", " + String.valueOf(iPgMessage) + ", " + String.valueOf(iOffset) + ", [Properties])");
            DebugFile.incIdent();
        }
        PreparedStatement oStmt = null;
        Blob oContentTxt;
        ByteArrayOutputStream byOutPart;
        int iPrevPart = 0, iThisPart = 0, iNextPart = 0, iPartStart = 0;
        try {
            MimeMultipart oParts = (MimeMultipart) oMsg.getContent();
            final int iParts = oParts.getCount();
            if (DebugFile.trace) DebugFile.writeln("message has " + String.valueOf(iParts) + " parts");
            if (iParts > 0) {
                if (sMsgCharSeq != null && sBoundary != null && ((iOpenMode & MODE_MBOX) != 0)) {
                    iPrevPart = sMsgCharSeq.indexOf(sBoundary, iPrevPart);
                    if (iPrevPart > 0) {
                        iPrevPart += sBoundary.length();
                        if (DebugFile.trace) DebugFile.writeln("found message boundary token at " + String.valueOf(iPrevPart));
                    }
                }
                String sSQL = "INSERT INTO " + DB.k_mime_parts + "(gu_mimemsg,id_message,pg_message,nu_offset,id_part,id_content,id_type,id_disposition,len_part,de_part,tx_md5,file_name,by_content) VALUES ('" + sMsgGuid + "',?,?,?,?,?,?,?,?,?,NULL,?,?)";
                if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
                oStmt = oConn.prepareStatement(sSQL);
                for (int p = 0; p < iParts; p++) {
                    if (DebugFile.trace) DebugFile.writeln("processing part " + String.valueOf(p));
                    BodyPart oPart = oParts.getBodyPart(p);
                    byOutPart = new ByteArrayOutputStream(oPart.getSize() > 0 ? oPart.getSize() : 131072);
                    oPart.writeTo(byOutPart);
                    if (sMsgCharSeq != null && sBoundary != null && iPrevPart > 0) {
                        iThisPart = sMsgCharSeq.indexOf(sBoundary, iPrevPart);
                        if (iThisPart > 0) {
                            if (DebugFile.trace) DebugFile.writeln("found part " + String.valueOf(p + iOffset) + " boundary at " + String.valueOf(iThisPart));
                            iPartStart = iThisPart + sBoundary.length();
                            while (iPartStart < sMsgCharSeq.length()) {
                                if (sMsgCharSeq.charAt(iPartStart) != ' ' && sMsgCharSeq.charAt(iPartStart) != '\r' && sMsgCharSeq.charAt(iPartStart) != '\n' && sMsgCharSeq.charAt(iPartStart) != '\t') break; else iPartStart++;
                            }
                        }
                        iNextPart = sMsgCharSeq.indexOf(sBoundary, iPartStart);
                        if (iNextPart < 0) {
                            if (DebugFile.trace) DebugFile.writeln("no next part found");
                            iNextPart = sMsgCharSeq.length();
                        } else {
                            if (DebugFile.trace) DebugFile.writeln("next part boundary found at " + String.valueOf(iNextPart));
                        }
                    }
                    String sContentType = oPart.getContentType();
                    if (sContentType != null) sContentType = MimeUtility.decodeText(sContentType);
                    boolean bForwardedAttachment = false;
                    if ((null != sContentType) && (null != ((DBStore) getStore()).getSession())) {
                        if (DebugFile.trace) DebugFile.writeln("Part Content-Type: " + sContentType.replace('\r', ' ').replace('\n', ' '));
                        if (sContentType.toUpperCase().startsWith("MULTIPART/ALTERNATIVE") || sContentType.toUpperCase().startsWith("MULTIPART/RELATED") || sContentType.toUpperCase().startsWith("MULTIPART/SIGNED")) {
                            try {
                                ByteArrayInputStream byInStrm = new ByteArrayInputStream(byOutPart.toByteArray());
                                MimeMessage oForwarded = new MimeMessage(((DBStore) getStore()).getSession(), byInStrm);
                                saveMimeParts(oForwarded, sMsgCharSeq, getPartsBoundary(oForwarded), sMsgGuid, sMsgId, iPgMessage, iOffset + iParts);
                                byInStrm.close();
                                byInStrm = null;
                                bForwardedAttachment = true;
                            } catch (Exception e) {
                                if (DebugFile.trace) DebugFile.writeln(e.getClass().getName() + " " + e.getMessage());
                            }
                        }
                    }
                    if (!bForwardedAttachment) {
                        oStmt.setString(1, sMsgId);
                        oStmt.setBigDecimal(2, new BigDecimal(iPgMessage));
                        if ((iPartStart > 0) && ((iOpenMode & MODE_MBOX) != 0)) oStmt.setBigDecimal(3, new BigDecimal(iPartStart)); else oStmt.setNull(3, oConn.getDataBaseProduct() == JDCConnection.DBMS_ORACLE ? Types.NUMERIC : Types.DECIMAL);
                        oStmt.setInt(4, p + iOffset);
                        oStmt.setString(5, ((javax.mail.internet.MimeBodyPart) oPart).getContentID());
                        oStmt.setString(6, Gadgets.left(sContentType, 254));
                        oStmt.setString(7, Gadgets.left(oPart.getDisposition(), 100));
                        if ((iOpenMode & MODE_MBOX) != 0) oStmt.setInt(8, iNextPart - iPartStart); else oStmt.setInt(8, oPart.getSize() > 0 ? oPart.getSize() : byOutPart.size());
                        if (oPart.getDescription() != null) oStmt.setString(9, Gadgets.left(MimeUtility.decodeText(oPart.getDescription()), 254)); else oStmt.setNull(9, Types.VARCHAR);
                        if (DebugFile.trace) DebugFile.writeln("file name is " + oPart.getFileName());
                        if (oPart.getFileName() != null) oStmt.setString(10, Gadgets.left(MimeUtility.decodeText(oPart.getFileName()), 254)); else oStmt.setNull(10, Types.VARCHAR);
                        if ((iOpenMode & MODE_BLOB) != 0) oStmt.setBinaryStream(11, new ByteArrayInputStream(byOutPart.toByteArray()), byOutPart.size()); else oStmt.setNull(11, Types.LONGVARBINARY);
                        if (DebugFile.trace) DebugFile.writeln("PreparedStatement.executeUpdate()");
                        oStmt.executeUpdate();
                    }
                    byOutPart.close();
                    byOutPart = null;
                    oContentTxt = null;
                    if ((iOpenMode & MODE_MBOX) != 0) iPrevPart = iNextPart;
                }
                if (DebugFile.trace) DebugFile.writeln("PreparedStatement.close()");
                oStmt.close();
            }
        } catch (SQLException e) {
            if (DebugFile.trace) {
                DebugFile.writeln("SQLException " + e.getMessage());
                DebugFile.decIdent();
            }
            if (null != oStmt) {
                try {
                    oStmt.close();
                } catch (Exception ignore) {
                }
            }
            try {
                if (null != oConn) oConn.rollback();
            } catch (Exception ignore) {
            }
            throw new MessagingException(e.getMessage(), e);
        } catch (IOException e) {
            if (DebugFile.trace) {
                DebugFile.writeln("IOException " + e.getMessage());
                DebugFile.decIdent();
            }
            if (null != oStmt) {
                try {
                    oStmt.close();
                } catch (Exception ignore) {
                }
            }
            throw new MessagingException(e.getMessage(), e);
        } catch (Exception e) {
            if (DebugFile.trace) {
                DebugFile.writeln(e.getClass().getName() + " " + e.getMessage());
                DebugFile.decIdent();
            }
            if (null != oStmt) {
                try {
                    oStmt.close();
                } catch (Exception ignore) {
                }
            }
            throw new MessagingException(e.getMessage(), e);
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DBFolder.saveMimeParts()");
        }
    }
