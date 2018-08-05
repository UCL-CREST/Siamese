    public synchronized int insertMessage(FrostMessageObject mo) {
        AttachmentList files = mo.getAttachmentsOfType(Attachment.FILE);
        AttachmentList boards = mo.getAttachmentsOfType(Attachment.BOARD);
        Connection conn = AppLayerDatabase.getInstance().getPooledConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO " + getMessageTableName() + " (" + "primkey,messageid,inreplyto,isvalid,invalidreason,msgdatetime,msgindex,board,fromname,subject,recipient,signature," + "signaturestatus,publickey,isdeleted,isnew,isreplied,isjunk,isflagged,isstarred,hasfileattachment,hasboardattachment,idlinepos,idlinelen" + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            Long identity = null;
            Statement stmt = AppLayerDatabase.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery("select UNIQUEKEY('" + getMessageTableName() + "')");
            if (rs.next()) {
                identity = new Long(rs.getLong(1));
            } else {
                logger.log(Level.SEVERE, "Could not retrieve a new unique key!");
            }
            rs.close();
            stmt.close();
            int i = 1;
            ps.setLong(i++, identity.longValue());
            ps.setString(i++, mo.getMessageId());
            ps.setString(i++, mo.getInReplyTo());
            ps.setBoolean(i++, mo.isValid());
            ps.setString(i++, mo.getInvalidReason());
            ps.setLong(i++, mo.getDateAndTime().getMillis());
            ps.setInt(i++, mo.getIndex());
            ps.setInt(i++, mo.getBoard().getPrimaryKey().intValue());
            ps.setString(i++, mo.getFromName());
            ps.setString(i++, mo.getSubject());
            ps.setString(i++, ((mo.getRecipientName() != null && mo.getRecipientName().length() == 0) ? null : mo.getRecipientName()));
            if (mo.getSignatureV2() == null || mo.getSignatureV2().length() == 0) {
                ps.setString(i++, mo.getSignatureV1());
            } else {
                ps.setString(i++, mo.getSignatureV2());
            }
            ps.setInt(i++, mo.getSignatureStatus());
            ps.setString(i++, mo.getPublicKey());
            ps.setBoolean(i++, mo.isDeleted());
            ps.setBoolean(i++, mo.isNew());
            ps.setBoolean(i++, mo.isReplied());
            ps.setBoolean(i++, mo.isJunk());
            ps.setBoolean(i++, mo.isFlagged());
            ps.setBoolean(i++, mo.isStarred());
            ps.setBoolean(i++, (files.size() > 0));
            ps.setBoolean(i++, (boards.size() > 0));
            ps.setInt(i++, mo.getIdLinePos());
            ps.setInt(i++, mo.getIdLineLen());
            int inserted;
            try {
                inserted = ps.executeUpdate();
            } finally {
                ps.close();
            }
            if (inserted == 0) {
                logger.log(Level.SEVERE, "message insert returned 0 !!!");
                throw new Exception("message insert returned 0 !!!");
            }
            mo.setMsgIdentity(identity.longValue());
            PreparedStatement pc = conn.prepareStatement("INSERT INTO " + getContentTableName() + " (msgref,msgcontent) VALUES (?,?)");
            pc.setLong(1, mo.getMsgIdentity());
            pc.setString(2, mo.getContent());
            try {
                inserted = pc.executeUpdate();
            } finally {
                pc.close();
            }
            if (inserted == 0) {
                logger.log(Level.SEVERE, "message content insert returned 0 !!!");
                throw new Exception("message content insert returned 0 !!!");
            }
            if (files.size() > 0) {
                PreparedStatement p = conn.prepareStatement("INSERT INTO " + getFileAttachmentsTableName() + " (msgref,filename,filesize,filekey)" + " VALUES (?,?,?,?)");
                try {
                    for (Iterator it = files.iterator(); it.hasNext(); ) {
                        FileAttachment fa = (FileAttachment) it.next();
                        int ix = 1;
                        p.setLong(ix++, mo.getMsgIdentity());
                        p.setString(ix++, fa.getFilename());
                        p.setLong(ix++, fa.getFileSize());
                        p.setString(ix++, fa.getKey());
                        int ins = p.executeUpdate();
                        if (ins == 0) {
                            logger.log(Level.SEVERE, "fileattachment insert returned 0 !!!");
                            throw new Exception("fileattachment insert returned 0 !!!");
                        }
                    }
                } finally {
                    p.close();
                }
            }
            if (boards.size() > 0) {
                PreparedStatement p = conn.prepareStatement("INSERT INTO " + getBoardAttachmentsTableName() + " (msgref,boardname,boardpublickey,boardprivatekey,boarddescription)" + " VALUES (?,?,?,?,?)");
                try {
                    for (Iterator it = boards.iterator(); it.hasNext(); ) {
                        BoardAttachment ba = (BoardAttachment) it.next();
                        Board b = ba.getBoardObj();
                        int ix = 1;
                        p.setLong(ix++, mo.getMsgIdentity());
                        p.setString(ix++, b.getNameLowerCase());
                        p.setString(ix++, b.getPublicKey());
                        p.setString(ix++, b.getPrivateKey());
                        p.setString(ix++, b.getDescription());
                        int ins = p.executeUpdate();
                        if (ins == 0) {
                            logger.log(Level.SEVERE, "boardattachment insert returned 0 !!!");
                            throw new Exception("boardattachment insert returned 0 !!!");
                        }
                    }
                } finally {
                    p.close();
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
            return INSERT_OK;
        } catch (Throwable t) {
            boolean isDuplicate;
            if (t.getMessage().indexOf("constraint violation") > 0 && t.getMessage().indexOf("MSG_ID_UNIQUE_ONLY") > 0) {
                isDuplicate = true;
                logger.warning("Duplicate message id, not added to database table: msgid='" + mo.getMessageId() + "', board=" + mo.getBoard().getName() + ", date='" + mo.getDateAndTimeString() + "', index=" + mo.getIndex());
            } else if (t.getMessage().indexOf("constraint violation") > 0 && t.getMessage().indexOf("MSG_UNIQUE_ONLY") > 0) {
                isDuplicate = true;
                logger.warning("Duplicate msgdatetime,index,board, not added to database table: msgid='" + mo.getMessageId() + "', board=" + mo.getBoard().getName() + ", date='" + mo.getDateAndTimeString() + "', index=" + mo.getIndex());
            } else {
                isDuplicate = false;
                logger.log(Level.SEVERE, "Exception during insert of message: msgid='" + mo.getMessageId() + "', board=" + mo.getBoard().getName() + ", date='" + mo.getDateAndTimeString() + "', index=" + mo.getIndex(), t);
                try {
                    conn.rollback();
                } catch (Throwable t1) {
                    logger.log(Level.SEVERE, "Exception during rollback", t1);
                }
            }
            try {
                conn.setAutoCommit(true);
            } catch (Throwable t1) {
            }
            if (isDuplicate) {
                return INSERT_DUPLICATE;
            } else {
                return INSERT_ERROR;
            }
        } finally {
            AppLayerDatabase.getInstance().givePooledConnection(conn);
        }
    }
