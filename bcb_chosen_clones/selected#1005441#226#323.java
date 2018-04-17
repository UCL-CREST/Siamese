            }
        }
    }

    private void recvMessage(String from, String to) throws Exception {
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl();
        Receiver receiver = null;
        ProviderConnection connection = factory.createConnection(from, to);
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            receiver = Receiver.createReceiver(connection);
            receiver.open();
            EXTSSPMessage message = (EXTSSPMessage) receiver.receiveEX();
            if (message == null) {
                System.out.println("no message");
            } else {
                conn.setAutoCommit(false);
                EXTSSPHeader header = message.getEXHeader();
                UUIDHexGenerator u = new UUIDHexGenerator();
                String id = u.generate().toString();
                pstmt = conn.prepareStatement(drawOutRecvSql(header, id));
                pstmt.executeUpdate();
                String xml = "";
                TSSPBody body = message.getBody();
                xml = body.getDomAsString();
                xml = xml.replaceAll("ns1:", "");
                saveClobMessage(pstmt, conn, rs, xml, id);
                String notify_id = "";
                Iterator iter = message.getAttachments();
                while (iter.hasNext()) {
                    AttachmentPart a = (AttachmentPart) iter.next();
                    String contentId = a.getContentId();
                    if (contentId.startsWith(Constant.PREFIX_PERSON)) {
                        DataHandler dh = a.getDataHandler();
                        InputStream is = dh.getInputStream();
                        byte[] temp = FileCopyUtils.copyToByteArray(is);
                        String content = new String(temp);
                        RecvDto recv = (RecvDto) XStreamConvert.xmlToBean(content);
                        if (recv == null) throw new Exception("接收方信息对象转换错误！请检查存入的信息对象xml字符串是否正确:" + content);
                        if (notify_id.equals("")) {
                            notify_id = u.generate().toString();
                            header.setType(Constant.MESSAGETYPE_NOTIFY);
                            pstmt = conn.prepareStatement(drawOutRecvSql(header, notify_id));
                            pstmt.executeUpdate();
                            String notify_content = header.getNotifyContent();
                            if (notify_content == null) notify_content = "接收到新的esb消息，但未定义通知消息内容!";
                            saveClobMessage(pstmt, conn, rs, notify_content, notify_id);
                        }
                        savePersonInfo(pstmt, conn, recv, notify_id);
                    } else {
                        DataHandler dh = a.getDataHandler();
                        InputStream is = dh.getInputStream();
                        String attid = u.generate().toString();
                        sql = "insert into message_recv_attachment(ATTACHMENTID," + "VERSION,MRECVID,BUSS_ID,ATTACHMENT) values('" + attid + "',0,'" + id + "','" + contentId + "',empty_blob())";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.executeUpdate();
                        sql = "select attachment from message_recv_attachment" + " where attachmentid = '" + attid + "' for update";
                        pstmt = conn.prepareStatement(sql);
                        rs = pstmt.executeQuery();
                        rs.next();
                        Blob blob = rs.getBlob(1);
                        OutputStream blobOutputStream = ((oracle.sql.BLOB) blob).getBinaryOutputStream();
                        FileCopyUtils.copy(is, blobOutputStream);
                        is.close();
                        blobOutputStream.close();
                    }
                }
                conn.commit();
                conn.setAutoCommit(true);
            }
            receiver.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("received message, rollback");
                if (receiver != null) {
                    receiver.rollback();
                }
            } catch (JAXMException e1) {
                e1.printStackTrace();
            }
        } finally {
            DBUtil.close(rs, pstmt, conn);
            if (receiver != null) {
                try {
                    receiver.close();
                } catch (JAXMException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JAXMException e) {
                    e.printStackTrace();
