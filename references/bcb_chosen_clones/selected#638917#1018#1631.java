    public void appendMessage(MimeMessage oMsg) throws FolderClosedException, StoreClosedException, MessagingException {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBFolder.appendMessage()");
            DebugFile.incIdent();
        }
        final String EmptyString = "";
        if (!((DBStore) getStore()).isConnected()) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new StoreClosedException(getStore(), "Store is not connected");
        }
        if (0 == (iOpenMode & READ_WRITE)) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new javax.mail.FolderClosedException(this, "Folder is not open is READ_WRITE mode");
        }
        if ((0 == (iOpenMode & MODE_MBOX)) && (0 == (iOpenMode & MODE_BLOB))) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new javax.mail.FolderClosedException(this, "Folder is not open in MBOX nor BLOB mode");
        }
        String gu_mimemsg;
        if (oMsg.getClass().getName().equals("com.knowgate.hipermail.DBMimeMessage")) {
            gu_mimemsg = ((DBMimeMessage) oMsg).getMessageGuid();
            if (((DBMimeMessage) oMsg).getFolder() == null) ((DBMimeMessage) oMsg).setFolder(this);
        } else {
            gu_mimemsg = Gadgets.generateUUID();
        }
        String gu_workarea = ((DBStore) getStore()).getUser().getString(DB.gu_workarea);
        int iSize = oMsg.getSize();
        if (DebugFile.trace) DebugFile.writeln("MimeMessage.getSize() = " + String.valueOf(iSize));
        String sContentType, sContentID, sMessageID, sDisposition, sContentMD5, sDescription, sFileName, sEncoding, sSubject, sPriority, sMsgCharSeq;
        long lPosition = -1;
        try {
            sMessageID = oMsg.getMessageID();
            if (sMessageID == null || EmptyString.equals(sMessageID)) {
                try {
                    sMessageID = oMsg.getHeader("X-Qmail-Scanner-Message-ID", null);
                } catch (Exception ignore) {
                }
            }
            if (sMessageID != null) sMessageID = MimeUtility.decodeText(sMessageID);
            sContentType = oMsg.getContentType();
            if (sContentType != null) sContentType = MimeUtility.decodeText(sContentType);
            sContentID = oMsg.getContentID();
            if (sContentID != null) sContentID = MimeUtility.decodeText(sContentID);
            sDisposition = oMsg.getDisposition();
            if (sDisposition != null) sDisposition = MimeUtility.decodeText(sDisposition);
            sContentMD5 = oMsg.getContentMD5();
            if (sContentMD5 != null) sContentMD5 = MimeUtility.decodeText(sContentMD5);
            sDescription = oMsg.getDescription();
            if (sDescription != null) sDescription = MimeUtility.decodeText(sDescription);
            sFileName = oMsg.getFileName();
            if (sFileName != null) sFileName = MimeUtility.decodeText(sFileName);
            sEncoding = oMsg.getEncoding();
            if (sEncoding != null) sEncoding = MimeUtility.decodeText(sEncoding);
            sSubject = oMsg.getSubject();
            if (sSubject != null) sSubject = MimeUtility.decodeText(sSubject);
            sPriority = null;
            sMsgCharSeq = null;
        } catch (UnsupportedEncodingException uee) {
            throw new MessagingException(uee.getMessage(), uee);
        }
        BigDecimal dPgMessage = null;
        try {
            dPgMessage = getNextMessage();
        } catch (SQLException sqle) {
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        String sBoundary = getPartsBoundary(oMsg);
        if (DebugFile.trace) DebugFile.writeln("part boundary is \"" + (sBoundary == null ? "null" : sBoundary) + "\"");
        if (sMessageID == null) sMessageID = gu_mimemsg; else if (sMessageID.length() == 0) sMessageID = gu_mimemsg;
        Timestamp tsSent;
        if (oMsg.getSentDate() != null) tsSent = new Timestamp(oMsg.getSentDate().getTime()); else tsSent = null;
        Timestamp tsReceived;
        if (oMsg.getReceivedDate() != null) tsReceived = new Timestamp(oMsg.getReceivedDate().getTime()); else tsReceived = new Timestamp(new java.util.Date().getTime());
        try {
            String sXPriority = oMsg.getHeader("X-Priority", null);
            if (sXPriority == null) sPriority = null; else {
                sPriority = "";
                for (int x = 0; x < sXPriority.length(); x++) {
                    char cAt = sXPriority.charAt(x);
                    if (cAt >= (char) 48 || cAt <= (char) 57) sPriority += cAt;
                }
                sPriority = Gadgets.left(sPriority, 10);
            }
        } catch (MessagingException msge) {
            if (DebugFile.trace) DebugFile.writeln("MessagingException " + msge.getMessage());
        }
        boolean bIsSpam = false;
        try {
            String sXSpam = oMsg.getHeader("X-Spam-Flag", null);
            if (sXSpam != null) bIsSpam = (sXSpam.toUpperCase().indexOf("YES") >= 0 || sXSpam.toUpperCase().indexOf("TRUE") >= 0 || sXSpam.indexOf("1") >= 0);
        } catch (MessagingException msge) {
            if (DebugFile.trace) DebugFile.writeln("MessagingException " + msge.getMessage());
        }
        if (DebugFile.trace) DebugFile.writeln("MimeMessage.getFrom()");
        Address[] aFrom = null;
        try {
            aFrom = oMsg.getFrom();
        } catch (AddressException adre) {
            if (DebugFile.trace) DebugFile.writeln("From AddressException " + adre.getMessage());
        }
        InternetAddress oFrom;
        if (aFrom != null) {
            if (aFrom.length > 0) oFrom = (InternetAddress) aFrom[0]; else oFrom = null;
        } else oFrom = null;
        if (DebugFile.trace) DebugFile.writeln("MimeMessage.getReplyTo()");
        Address[] aReply = null;
        InternetAddress oReply;
        try {
            aReply = oMsg.getReplyTo();
        } catch (AddressException adre) {
            if (DebugFile.trace) DebugFile.writeln("Reply-To AddressException " + adre.getMessage());
        }
        if (aReply != null) {
            if (aReply.length > 0) oReply = (InternetAddress) aReply[0]; else oReply = null;
        } else {
            if (DebugFile.trace) DebugFile.writeln("no reply-to address found");
            oReply = null;
        }
        if (DebugFile.trace) DebugFile.writeln("MimeMessage.getRecipients()");
        Address[] oTo = null;
        Address[] oCC = null;
        Address[] oBCC = null;
        try {
            oTo = oMsg.getRecipients(MimeMessage.RecipientType.TO);
            oCC = oMsg.getRecipients(MimeMessage.RecipientType.CC);
            oBCC = oMsg.getRecipients(MimeMessage.RecipientType.BCC);
        } catch (AddressException adre) {
            if (DebugFile.trace) DebugFile.writeln("Recipient AddressException " + adre.getMessage());
        }
        Properties pFrom = new Properties(), pTo = new Properties(), pCC = new Properties(), pBCC = new Properties();
        if (DebugFile.trace) DebugFile.writeln("MimeMessage.getFlags()");
        Flags oFlgs = oMsg.getFlags();
        if (oFlgs == null) oFlgs = new Flags();
        MimePart oText = null;
        ByteArrayOutputStream byOutStrm = null;
        File oFile = null;
        MboxFile oMBox = null;
        if ((iOpenMode & MODE_MBOX) != 0) {
            try {
                if (DebugFile.trace) DebugFile.writeln("new File(" + Gadgets.chomp(sFolderDir, File.separator) + oCatg.getStringNull(DB.nm_category, "null") + ".mbox)");
                oFile = getFile();
                lPosition = oFile.length();
                if (DebugFile.trace) DebugFile.writeln("message position is " + String.valueOf(lPosition));
                oMBox = new MboxFile(oFile, MboxFile.READ_WRITE);
                if (DebugFile.trace) DebugFile.writeln("new ByteArrayOutputStream(" + String.valueOf(iSize > 0 ? iSize : 16000) + ")");
                byOutStrm = new ByteArrayOutputStream(iSize > 0 ? iSize : 16000);
                oMsg.writeTo(byOutStrm);
                sMsgCharSeq = byOutStrm.toString("ISO8859_1");
                byOutStrm.close();
            } catch (IOException ioe) {
                try {
                    if (oMBox != null) oMBox.close();
                } catch (Exception ignore) {
                }
                if (DebugFile.trace) DebugFile.decIdent();
                throw new MessagingException(ioe.getMessage(), ioe);
            }
        }
        try {
            if (oMsg.getClass().getName().equals("com.knowgate.hipermail.DBMimeMessage")) oText = ((DBMimeMessage) oMsg).getBody(); else {
                oText = new DBMimeMessage(oMsg).getBody();
            }
            if (DebugFile.trace) DebugFile.writeln("ByteArrayOutputStream byOutStrm = new ByteArrayOutputStream(" + oText.getSize() + ")");
            byOutStrm = new ByteArrayOutputStream(oText.getSize() > 0 ? oText.getSize() : 8192);
            oText.writeTo(byOutStrm);
            if (null == sContentMD5) {
                MD5 oMd5 = new MD5();
                oMd5.Init();
                oMd5.Update(byOutStrm.toByteArray());
                sContentMD5 = Gadgets.toHexString(oMd5.Final());
                oMd5 = null;
            }
        } catch (IOException ioe) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new MessagingException("IOException " + ioe.getMessage(), ioe);
        } catch (OutOfMemoryError oom) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new MessagingException("OutOfMemoryError " + oom.getMessage());
        }
        String sSQL = "INSERT INTO " + DB.k_mime_msgs + "(gu_mimemsg,gu_workarea,gu_category,id_type,id_content,id_message,id_disposition,len_mimemsg,tx_md5,de_mimemsg,file_name,tx_encoding,tx_subject,dt_sent,dt_received,tx_email_from,nm_from,tx_email_reply,nm_to,id_priority,bo_answered,bo_deleted,bo_draft,bo_flagged,bo_recent,bo_seen,bo_spam,pg_message,nu_position,by_content) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
        PreparedStatement oStmt = null;
        try {
            oStmt = oConn.prepareStatement(sSQL);
            oStmt.setString(1, gu_mimemsg);
            oStmt.setString(2, gu_workarea);
            if (oCatg.isNull(DB.gu_category)) oStmt.setNull(3, Types.CHAR); else oStmt.setString(3, oCatg.getString(DB.gu_category));
            oStmt.setString(4, Gadgets.left(sContentType, 254));
            oStmt.setString(5, Gadgets.left(sContentID, 254));
            oStmt.setString(6, Gadgets.left(sMessageID, 254));
            oStmt.setString(7, Gadgets.left(sDisposition, 100));
            if ((iOpenMode & MODE_MBOX) != 0) {
                iSize = sMsgCharSeq.length();
                oStmt.setInt(8, iSize);
            } else {
                if (iSize >= 0) oStmt.setInt(8, iSize); else oStmt.setNull(8, Types.INTEGER);
            }
            oStmt.setString(9, Gadgets.left(sContentMD5, 32));
            oStmt.setString(10, Gadgets.left(sDescription, 254));
            oStmt.setString(11, Gadgets.left(sFileName, 254));
            oStmt.setString(12, Gadgets.left(sEncoding, 16));
            oStmt.setString(13, Gadgets.left(sSubject, 254));
            oStmt.setTimestamp(14, tsSent);
            oStmt.setTimestamp(15, tsReceived);
            if (null == oFrom) {
                oStmt.setNull(16, Types.VARCHAR);
                oStmt.setNull(17, Types.VARCHAR);
            } else {
                oStmt.setString(16, Gadgets.left(oFrom.getAddress(), 254));
                oStmt.setString(17, Gadgets.left(oFrom.getPersonal(), 254));
            }
            if (null == oReply) oStmt.setNull(18, Types.VARCHAR); else oStmt.setString(18, Gadgets.left(oReply.getAddress(), 254));
            Address[] aRecipients;
            String sRecipientName;
            aRecipients = oMsg.getRecipients(MimeMessage.RecipientType.TO);
            if (null != aRecipients) if (aRecipients.length == 0) aRecipients = null;
            if (null != aRecipients) {
                sRecipientName = ((InternetAddress) aRecipients[0]).getPersonal();
                if (null == sRecipientName) sRecipientName = ((InternetAddress) aRecipients[0]).getAddress();
                oStmt.setString(19, Gadgets.left(sRecipientName, 254));
            } else {
                aRecipients = oMsg.getRecipients(MimeMessage.RecipientType.CC);
                if (null != aRecipients) {
                    if (aRecipients.length > 0) {
                        sRecipientName = ((InternetAddress) aRecipients[0]).getPersonal();
                        if (null == sRecipientName) sRecipientName = ((InternetAddress) aRecipients[0]).getAddress();
                        oStmt.setString(19, Gadgets.left(sRecipientName, 254));
                    } else oStmt.setNull(19, Types.VARCHAR);
                } else {
                    aRecipients = oMsg.getRecipients(MimeMessage.RecipientType.BCC);
                    if (null != aRecipients) {
                        if (aRecipients.length > 0) {
                            sRecipientName = ((InternetAddress) aRecipients[0]).getPersonal();
                            if (null == sRecipientName) sRecipientName = ((InternetAddress) aRecipients[0]).getAddress();
                            oStmt.setString(19, Gadgets.left(sRecipientName, 254));
                        } else oStmt.setNull(19, Types.VARCHAR);
                    } else {
                        oStmt.setNull(19, Types.VARCHAR);
                    }
                }
            }
            if (null == sPriority) oStmt.setNull(20, Types.VARCHAR); else oStmt.setString(20, sPriority);
            if (oConn.getDataBaseProduct() == JDCConnection.DBMS_ORACLE) {
                if (DebugFile.trace) DebugFile.writeln("PreparedStatement.setBigDecimal(21, ...)");
                oStmt.setBigDecimal(21, new BigDecimal(oFlgs.contains(Flags.Flag.ANSWERED) ? "1" : "0"));
                oStmt.setBigDecimal(22, new BigDecimal(oFlgs.contains(Flags.Flag.DELETED) ? "1" : "0"));
                oStmt.setBigDecimal(23, new BigDecimal(0));
                oStmt.setBigDecimal(24, new BigDecimal(oFlgs.contains(Flags.Flag.FLAGGED) ? "1" : "0"));
                oStmt.setBigDecimal(25, new BigDecimal(oFlgs.contains(Flags.Flag.RECENT) ? "1" : "0"));
                oStmt.setBigDecimal(26, new BigDecimal(oFlgs.contains(Flags.Flag.SEEN) ? "1" : "0"));
                oStmt.setBigDecimal(27, new BigDecimal(bIsSpam ? "1" : "0"));
                oStmt.setBigDecimal(28, dPgMessage);
                if ((iOpenMode & MODE_MBOX) != 0) oStmt.setBigDecimal(29, new BigDecimal(lPosition)); else oStmt.setNull(29, Types.NUMERIC);
                if (DebugFile.trace) DebugFile.writeln("PreparedStatement.setBinaryStream(30, new ByteArrayInputStream(" + String.valueOf(byOutStrm.size()) + "))");
                if (byOutStrm.size() > 0) oStmt.setBinaryStream(30, new ByteArrayInputStream(byOutStrm.toByteArray()), byOutStrm.size()); else oStmt.setNull(30, Types.LONGVARBINARY);
            } else {
                if (DebugFile.trace) DebugFile.writeln("PreparedStatement.setShort(21, ...)");
                oStmt.setShort(21, (short) (oFlgs.contains(Flags.Flag.ANSWERED) ? 1 : 0));
                oStmt.setShort(22, (short) (oFlgs.contains(Flags.Flag.DELETED) ? 1 : 0));
                oStmt.setShort(23, (short) (0));
                oStmt.setShort(24, (short) (oFlgs.contains(Flags.Flag.FLAGGED) ? 1 : 0));
                oStmt.setShort(25, (short) (oFlgs.contains(Flags.Flag.RECENT) ? 1 : 0));
                oStmt.setShort(26, (short) (oFlgs.contains(Flags.Flag.SEEN) ? 1 : 0));
                oStmt.setShort(27, (short) (bIsSpam ? 1 : 0));
                oStmt.setBigDecimal(28, dPgMessage);
                if ((iOpenMode & MODE_MBOX) != 0) oStmt.setBigDecimal(29, new BigDecimal(lPosition)); else oStmt.setNull(29, Types.NUMERIC);
                if (DebugFile.trace) DebugFile.writeln("PreparedStatement.setBinaryStream(30, new ByteArrayInputStream(" + String.valueOf(byOutStrm.size()) + "))");
                if (byOutStrm.size() > 0) oStmt.setBinaryStream(30, new ByteArrayInputStream(byOutStrm.toByteArray()), byOutStrm.size()); else oStmt.setNull(30, Types.LONGVARBINARY);
            }
            if (DebugFile.trace) DebugFile.writeln("Statement.executeUpdate()");
            oStmt.executeUpdate();
            oStmt.close();
            oStmt = null;
        } catch (SQLException sqle) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception ignore) {
            }
            try {
                if (null != oStmt) oStmt.close();
                oStmt = null;
            } catch (Exception ignore) {
            }
            try {
                if (null != oConn) oConn.rollback();
            } catch (Exception ignore) {
            }
            throw new MessagingException(DB.k_mime_msgs + " " + sqle.getMessage(), sqle);
        }
        if ((iOpenMode & MODE_BLOB) != 0) {
            try {
                byOutStrm.close();
            } catch (IOException ignore) {
            }
            byOutStrm = null;
        }
        try {
            Object oContent = oMsg.getContent();
            if (oContent instanceof MimeMultipart) {
                try {
                    saveMimeParts(oMsg, sMsgCharSeq, sBoundary, gu_mimemsg, sMessageID, dPgMessage.intValue(), 0);
                } catch (MessagingException msge) {
                    try {
                        if (oMBox != null) oMBox.close();
                    } catch (Exception ignore) {
                    }
                    try {
                        oConn.rollback();
                    } catch (Exception ignore) {
                    }
                    throw new MessagingException(msge.getMessage(), msge.getNextException());
                }
            }
        } catch (Exception xcpt) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception ignore) {
            }
            try {
                oConn.rollback();
            } catch (Exception ignore) {
            }
            throw new MessagingException("MimeMessage.getContent() " + xcpt.getMessage(), xcpt);
        }
        sSQL = "SELECT " + DB.gu_contact + "," + DB.gu_company + "," + DB.tx_name + "," + DB.tx_surname + "," + DB.tx_surname + " FROM " + DB.k_member_address + " WHERE " + DB.tx_email + "=? AND " + DB.gu_workarea + "=? UNION SELECT " + DB.gu_user + ",'****************************USER'," + DB.nm_user + "," + DB.tx_surname1 + "," + DB.tx_surname2 + " FROM " + DB.k_users + " WHERE (" + DB.tx_main_email + "=? OR " + DB.tx_alt_email + "=?) AND " + DB.gu_workarea + "=?";
        if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
        PreparedStatement oAddr = null;
        try {
            oAddr = oConn.prepareStatement(sSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet oRSet;
            InternetAddress oInetAdr;
            String sTxEmail, sGuCompany, sGuContact, sGuUser, sTxName, sTxSurname1, sTxSurname2, sTxPersonal;
            if (oFrom != null) {
                oAddr.setString(1, oFrom.getAddress());
                oAddr.setString(2, gu_workarea);
                oAddr.setString(3, oFrom.getAddress());
                oAddr.setString(4, oFrom.getAddress());
                oAddr.setString(5, gu_workarea);
                oRSet = oAddr.executeQuery();
                if (oRSet.next()) {
                    sGuContact = oRSet.getString(1);
                    if (oRSet.wasNull()) sGuContact = "null";
                    sGuCompany = oRSet.getString(2);
                    if (oRSet.wasNull()) sGuCompany = "null";
                    if (sGuCompany.equals("****************************USER")) {
                        sTxName = oRSet.getString(3);
                        if (oRSet.wasNull()) sTxName = "";
                        sTxSurname1 = oRSet.getString(4);
                        if (oRSet.wasNull()) sTxSurname1 = "";
                        sTxSurname2 = oRSet.getString(4);
                        if (oRSet.wasNull()) sTxSurname2 = "";
                        sTxPersonal = Gadgets.left(sTxName + " " + sTxSurname1 + " " + sTxSurname2, 254).replace(',', ' ').trim();
                    } else sTxPersonal = "null";
                    pFrom.put(oFrom.getAddress(), sGuContact + "," + sGuCompany + "," + sTxPersonal);
                } else pFrom.put(oFrom.getAddress(), "null,null,null");
                oRSet.close();
            }
            if (DebugFile.trace) DebugFile.writeln("from count = " + pFrom.size());
            if (oTo != null) {
                for (int t = 0; t < oTo.length; t++) {
                    oInetAdr = (InternetAddress) oTo[t];
                    sTxEmail = Gadgets.left(oInetAdr.getAddress(), 254);
                    oAddr.setString(1, sTxEmail);
                    oAddr.setString(2, gu_workarea);
                    oAddr.setString(3, sTxEmail);
                    oAddr.setString(4, sTxEmail);
                    oAddr.setString(5, gu_workarea);
                    oRSet = oAddr.executeQuery();
                    if (oRSet.next()) {
                        sGuContact = oRSet.getString(1);
                        if (oRSet.wasNull()) sGuContact = "null";
                        sGuCompany = oRSet.getString(2);
                        if (oRSet.wasNull()) sGuCompany = "null";
                        if (sGuCompany.equals("****************************USER")) {
                            sTxName = oRSet.getString(3);
                            if (oRSet.wasNull()) sTxName = "";
                            sTxSurname1 = oRSet.getString(4);
                            if (oRSet.wasNull()) sTxSurname1 = "";
                            sTxSurname2 = oRSet.getString(4);
                            if (oRSet.wasNull()) sTxSurname2 = "";
                            sTxPersonal = Gadgets.left(sTxName + " " + sTxSurname1 + " " + sTxSurname2, 254).replace(',', ' ').trim();
                        } else sTxPersonal = "null";
                        pTo.put(sTxEmail, sGuContact + "," + sGuCompany + "," + sTxPersonal);
                    } else pTo.put(sTxEmail, "null,null,null");
                    oRSet.close();
                }
            }
            if (DebugFile.trace) DebugFile.writeln("to count = " + pTo.size());
            if (oCC != null) {
                for (int c = 0; c < oCC.length; c++) {
                    oInetAdr = (InternetAddress) oCC[c];
                    sTxEmail = Gadgets.left(oInetAdr.getAddress(), 254);
                    oAddr.setString(1, sTxEmail);
                    oAddr.setString(2, gu_workarea);
                    oAddr.setString(3, sTxEmail);
                    oAddr.setString(4, sTxEmail);
                    oAddr.setString(5, gu_workarea);
                    oRSet = oAddr.executeQuery();
                    if (oRSet.next()) {
                        sGuContact = oRSet.getString(1);
                        if (oRSet.wasNull()) sGuContact = "null";
                        sGuCompany = oRSet.getString(2);
                        if (oRSet.wasNull()) sGuCompany = "null";
                        if (sGuCompany.equals("****************************USER")) {
                            sTxName = oRSet.getString(3);
                            if (oRSet.wasNull()) sTxName = "";
                            sTxSurname1 = oRSet.getString(4);
                            if (oRSet.wasNull()) sTxSurname1 = "";
                            sTxSurname2 = oRSet.getString(4);
                            if (oRSet.wasNull()) sTxSurname2 = "";
                            sTxPersonal = Gadgets.left(sTxName + " " + sTxSurname1 + " " + sTxSurname2, 254).replace(',', ' ').trim();
                        } else sTxPersonal = "null";
                        pCC.put(sTxEmail, sGuContact + "," + sGuCompany + "," + sTxPersonal);
                    } else pCC.put(sTxEmail, "null,null,null");
                    oRSet.close();
                }
            }
            if (DebugFile.trace) DebugFile.writeln("cc count = " + pCC.size());
            if (oBCC != null) {
                for (int b = 0; b < oBCC.length; b++) {
                    oInetAdr = (InternetAddress) oBCC[b];
                    sTxEmail = Gadgets.left(oInetAdr.getAddress(), 254);
                    oAddr.setString(1, sTxEmail);
                    oAddr.setString(2, gu_workarea);
                    oAddr.setString(3, sTxEmail);
                    oAddr.setString(4, sTxEmail);
                    oAddr.setString(5, gu_workarea);
                    oRSet = oAddr.executeQuery();
                    if (oRSet.next()) {
                        sGuContact = oRSet.getString(1);
                        if (oRSet.wasNull()) sGuContact = "null";
                        sGuCompany = oRSet.getString(2);
                        if (oRSet.wasNull()) sGuCompany = "null";
                        if (sGuCompany.equals("****************************USER")) {
                            sTxName = oRSet.getString(3);
                            if (oRSet.wasNull()) sTxName = "";
                            sTxSurname1 = oRSet.getString(4);
                            if (oRSet.wasNull()) sTxSurname1 = "";
                            sTxSurname2 = oRSet.getString(4);
                            if (oRSet.wasNull()) sTxSurname2 = "";
                            sTxPersonal = Gadgets.left(sTxName + " " + sTxSurname1 + " " + sTxSurname2, 254).replace(',', ' ').trim();
                        } else sTxPersonal = "null";
                        pBCC.put(sTxEmail, sGuContact + "," + sGuCompany);
                    } else pBCC.put(sTxEmail, "null,null,null");
                    oRSet.close();
                }
            }
            if (DebugFile.trace) DebugFile.writeln("bcc count = " + pBCC.size());
            oAddr.close();
            sSQL = "INSERT INTO " + DB.k_inet_addrs + " (gu_mimemsg,id_message,tx_email,tp_recipient,gu_user,gu_contact,gu_company,tx_personal) VALUES ('" + gu_mimemsg + "','" + sMessageID + "',?,?,?,?,?,?)";
            if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
            oStmt = oConn.prepareStatement(sSQL);
            java.util.Enumeration oMailEnum;
            String[] aRecipient;
            if (!pFrom.isEmpty()) {
                oMailEnum = pFrom.keys();
                while (oMailEnum.hasMoreElements()) {
                    sTxEmail = (String) oMailEnum.nextElement();
                    aRecipient = Gadgets.split(pFrom.getProperty(sTxEmail), ',');
                    oStmt.setString(1, sTxEmail);
                    oStmt.setString(2, "from");
                    if (aRecipient[0].equals("null") && aRecipient[1].equals("null")) {
                        oStmt.setNull(3, Types.CHAR);
                        oStmt.setNull(4, Types.CHAR);
                        oStmt.setNull(5, Types.CHAR);
                    } else if (aRecipient[1].equals("****************************USER")) {
                        oStmt.setString(3, aRecipient[0]);
                        oStmt.setNull(4, Types.CHAR);
                        oStmt.setNull(5, Types.CHAR);
                    } else {
                        oStmt.setNull(3, Types.CHAR);
                        oStmt.setString(4, aRecipient[0].equals("null") ? null : aRecipient[0]);
                        oStmt.setString(5, aRecipient[1].equals("null") ? null : aRecipient[1]);
                    }
                    if (aRecipient[2].equals("null")) oStmt.setNull(6, Types.VARCHAR); else oStmt.setString(6, aRecipient[2]);
                    if (DebugFile.trace) DebugFile.writeln("Statement.executeUpdate()");
                    oStmt.executeUpdate();
                }
            }
            if (!pTo.isEmpty()) {
                oMailEnum = pTo.keys();
                while (oMailEnum.hasMoreElements()) {
                    sTxEmail = (String) oMailEnum.nextElement();
                    aRecipient = Gadgets.split(pTo.getProperty(sTxEmail), ',');
                    oStmt.setString(1, sTxEmail);
                    oStmt.setString(2, "to");
                    if (aRecipient[0].equals("null") && aRecipient[1].equals("null")) {
                        oStmt.setNull(3, Types.CHAR);
                        oStmt.setNull(4, Types.CHAR);
                        oStmt.setNull(5, Types.CHAR);
                    } else if (aRecipient[1].equals("****************************USER")) {
                        oStmt.setString(3, aRecipient[0]);
                        oStmt.setNull(4, Types.CHAR);
                        oStmt.setNull(5, Types.CHAR);
                    } else {
                        oStmt.setNull(3, Types.CHAR);
                        oStmt.setString(4, aRecipient[0].equals("null") ? null : aRecipient[0]);
                        oStmt.setString(5, aRecipient[1].equals("null") ? null : aRecipient[1]);
                    }
                    if (aRecipient[2].equals("null")) oStmt.setNull(6, Types.VARCHAR); else oStmt.setString(6, aRecipient[2]);
                    if (DebugFile.trace) DebugFile.writeln("Statement.executeUpdate()");
                    oStmt.executeUpdate();
                }
            }
            if (!pCC.isEmpty()) {
                oMailEnum = pCC.keys();
                while (oMailEnum.hasMoreElements()) {
                    sTxEmail = (String) oMailEnum.nextElement();
                    aRecipient = Gadgets.split(pCC.getProperty(sTxEmail), ',');
                    oStmt.setString(1, sTxEmail);
                    oStmt.setString(2, "cc");
                    if (aRecipient[0].equals("null") && aRecipient[1].equals("null")) {
                        oStmt.setNull(3, Types.CHAR);
                        oStmt.setNull(4, Types.CHAR);
                        oStmt.setNull(5, Types.CHAR);
                    } else if (aRecipient[1].equals("****************************USER")) {
                        oStmt.setString(3, aRecipient[0]);
                        oStmt.setString(4, null);
                        oStmt.setString(5, null);
                    } else {
                        oStmt.setString(3, null);
                        oStmt.setString(4, aRecipient[0].equals("null") ? null : aRecipient[0]);
                        oStmt.setString(5, aRecipient[1].equals("null") ? null : aRecipient[1]);
                    }
                    if (aRecipient[2].equals("null")) oStmt.setNull(6, Types.VARCHAR); else oStmt.setString(6, aRecipient[2]);
                    if (DebugFile.trace) DebugFile.writeln("Statement.executeUpdate()");
                    oStmt.executeUpdate();
                }
            }
            if (!pBCC.isEmpty()) {
                oMailEnum = pBCC.keys();
                while (oMailEnum.hasMoreElements()) {
                    sTxEmail = (String) oMailEnum.nextElement();
                    aRecipient = Gadgets.split(pBCC.getProperty(sTxEmail), ',');
                    oStmt.setString(1, sTxEmail);
                    oStmt.setString(2, "bcc");
                    if (aRecipient[0].equals("null") && aRecipient[1].equals("null")) {
                        oStmt.setNull(3, Types.CHAR);
                        oStmt.setNull(4, Types.CHAR);
                        oStmt.setNull(5, Types.CHAR);
                    } else if (aRecipient[1].equals("****************************USER")) {
                        oStmt.setString(3, aRecipient[0]);
                        oStmt.setNull(4, Types.CHAR);
                        oStmt.setNull(5, Types.CHAR);
                    } else {
                        oStmt.setNull(3, Types.CHAR);
                        oStmt.setString(4, aRecipient[0].equals("null") ? null : aRecipient[0]);
                        oStmt.setString(5, aRecipient[1].equals("null") ? null : aRecipient[1]);
                    }
                    if (aRecipient[2].equals("null")) oStmt.setNull(6, Types.VARCHAR); else oStmt.setString(6, aRecipient[2]);
                    oStmt.executeUpdate();
                }
            }
            oStmt.close();
            oStmt = null;
            oStmt = oConn.prepareStatement("UPDATE " + DB.k_categories + " SET " + DB.len_size + "=" + DB.len_size + "+" + String.valueOf(iSize) + " WHERE " + DB.gu_category + "=?");
            oStmt.setString(1, getCategory().getString(DB.gu_category));
            oStmt.executeUpdate();
            oStmt.close();
            oStmt = null;
            if ((iOpenMode & MODE_MBOX) != 0) {
                if (DebugFile.trace) DebugFile.writeln("MboxFile.appendMessage(" + (oMsg.getContentID() != null ? oMsg.getContentID() : "") + ")");
                oMBox.appendMessage(sMsgCharSeq);
                oMBox.close();
                oMBox = null;
            }
            if (DebugFile.trace) DebugFile.writeln("Connection.commit()");
            oConn.commit();
        } catch (SQLException sqle) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception ignore) {
            }
            try {
                if (null != oStmt) oStmt.close();
                oStmt = null;
            } catch (Exception ignore) {
            }
            try {
                if (null != oAddr) oAddr.close();
                oAddr = null;
            } catch (Exception ignore) {
            }
            try {
                if (null != oConn) oConn.rollback();
            } catch (Exception ignore) {
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        } catch (IOException ioe) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception ignore) {
            }
            try {
                if (null != oStmt) oStmt.close();
                oStmt = null;
            } catch (Exception ignore) {
            }
            try {
                if (null != oAddr) oAddr.close();
                oAddr = null;
            } catch (Exception ignore) {
            }
            try {
                if (null != oConn) oConn.rollback();
            } catch (Exception ignore) {
            }
            throw new MessagingException(ioe.getMessage(), ioe);
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DBFolder.appendMessage() : " + gu_mimemsg);
        }
    }
