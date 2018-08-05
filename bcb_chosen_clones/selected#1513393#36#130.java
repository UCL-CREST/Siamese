    public synchronized void insertMessage(FrostUnsentMessageObject mo) throws SQLException {
        AttachmentList files = mo.getAttachmentsOfType(Attachment.FILE);
        AttachmentList boards = mo.getAttachmentsOfType(Attachment.BOARD);
        Connection conn = AppLayerDatabase.getInstance().getPooledConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO UNSENDMESSAGES (" + "primkey,messageid,inreplyto,board,sendafter,idlinepos,idlinelen,fromname,subject,recipient,msgcontent," + "hasfileattachment,hasboardattachment,timeAdded" + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            Long identity = null;
            Statement stmt = AppLayerDatabase.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery("select UNIQUEKEY('UNSENDMESSAGES')");
            if (rs.next()) {
                identity = new Long(rs.getLong(1));
            } else {
                logger.log(Level.SEVERE, "Could not retrieve a new unique key!");
            }
            rs.close();
            stmt.close();
            mo.setMsgIdentity(identity.longValue());
            int i = 1;
            ps.setLong(i++, mo.getMsgIdentity());
            ps.setString(i++, mo.getMessageId());
            ps.setString(i++, mo.getInReplyTo());
            ps.setInt(i++, mo.getBoard().getPrimaryKey().intValue());
            ps.setLong(i++, 0);
            ps.setInt(i++, mo.getIdLinePos());
            ps.setInt(i++, mo.getIdLineLen());
            ps.setString(i++, mo.getFromName());
            ps.setString(i++, mo.getSubject());
            ps.setString(i++, mo.getRecipientName());
            ps.setString(i++, mo.getContent());
            ps.setBoolean(i++, (files.size() > 0));
            ps.setBoolean(i++, (boards.size() > 0));
            ps.setLong(i++, mo.getTimeAdded());
            int inserted = 0;
            try {
                inserted = ps.executeUpdate();
            } finally {
                ps.close();
            }
            if (inserted == 0) {
                logger.log(Level.SEVERE, "message insert returned 0 !!!");
                return;
            }
            mo.setMsgIdentity(identity.longValue());
            if (files.size() > 0) {
                PreparedStatement p = conn.prepareStatement("INSERT INTO UNSENDFILEATTACHMENTS" + " (msgref,filename,filesize,filekey)" + " VALUES (?,?,?,?)");
                for (Iterator it = files.iterator(); it.hasNext(); ) {
                    FileAttachment fa = (FileAttachment) it.next();
                    int ix = 1;
                    p.setLong(ix++, mo.getMsgIdentity());
                    p.setString(ix++, fa.getInternalFile().getPath());
                    p.setLong(ix++, fa.getFileSize());
                    p.setString(ix++, fa.getKey());
                    int ins = p.executeUpdate();
                    if (ins == 0) {
                        logger.log(Level.SEVERE, "fileattachment insert returned 0 !!!");
                    }
                }
                p.close();
            }
            if (boards.size() > 0) {
                PreparedStatement p = conn.prepareStatement("INSERT INTO UNSENDBOARDATTACHMENTS" + " (msgref,boardname,boardpublickey,boardprivatekey,boarddescription)" + " VALUES (?,?,?,?,?)");
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
                    }
                }
                p.close();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Exception during insert of unsent message", t);
            try {
                conn.rollback();
            } catch (Throwable t1) {
                logger.log(Level.SEVERE, "Exception during rollback", t1);
            }
            try {
                conn.setAutoCommit(true);
            } catch (Throwable t1) {
            }
        } finally {
            AppLayerDatabase.getInstance().givePooledConnection(conn);
        }
    }
