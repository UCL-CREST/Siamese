    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (doAuth(request, response)) {
            Connection conn = null;
            try {
                int UID = icsm.getIntChatSession(request).getUID();
                conn = getJDBCConnection(icsm.getHeavyDatabaseConnectionPool(), request, response, HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                if (conn == null) return;
                ResultSet rs = IntChatDatabaseOperations.executeQuery(conn, "SELECT id FROM ic_messagetypes WHERE templatename='" + IntChatConstants.MessageTemplates.IC_FILES + "' LIMIT 1");
                if (rs.next()) {
                    int fileTypeID = rs.getInt("id");
                    String recipients = request.getHeader(IntChatConstants.HEADER_FILERECIPIENTS);
                    rs.getStatement().close();
                    rs = null;
                    if (recipients != null) {
                        HashMap<String, String> hm = Tools.parseMultiparamLine(request.getHeader("Content-Disposition"));
                        String fileName = URLDecoder.decode(hm.get("filename"), IntChatServerDefaults.ENCODING);
                        long fileLength = (request.getHeader("Content-Length") != null ? Long.parseLong(request.getHeader("Content-Length")) : -1);
                        fileLength = (request.getHeader(IntChatConstants.HEADER_FILELENGTH) != null ? Long.parseLong(request.getHeader(IntChatConstants.HEADER_FILELENGTH)) : fileLength);
                        long maxFileSize = RuntimeParameters.getIntValue(ParameterNames.MAX_FILE_SIZE) * 1048576;
                        if (maxFileSize > 0 && fileLength > maxFileSize) {
                            request.getInputStream().close();
                            response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                            return;
                        }
                        long now = System.currentTimeMillis();
                        long nextid = ic_messages_id_seq.nextval();
                        IntChatServletInputStream in = new IntChatServletInputStream(request);
                        IntChatMessage icm = null;
                        conn.setAutoCommit(false);
                        try {
                            PreparedStatement ps = conn.prepareStatement("INSERT INTO ic_messages (id, tid, mhead, mbody, mdate, sid) VALUES (?, ?, ?, ?, ?, ?)");
                            ps.setLong(1, nextid);
                            ps.setInt(2, fileTypeID);
                            ps.setString(3, fileName);
                            ps.setString(4, Long.toString(fileLength));
                            ps.setLong(5, now);
                            ps.setInt(6, UID);
                            ps.executeUpdate();
                            ps.close();
                            if (!insertBLOB(conn, in, fileLength, nextid, maxFileSize)) {
                                conn.rollback();
                                return;
                            }
                            icm = new IntChatMessage(false, fileTypeID, null, null);
                            String[] id = recipients.split(",");
                            int id1;
                            for (int i = 0; i < id.length; i++) {
                                id1 = Integer.parseInt(id[i].trim());
                                IntChatDatabaseOperations.executeUpdate(conn, "INSERT INTO ic_recipients (mid, rid) VALUES ('" + nextid + "', '" + id1 + "')");
                                icm.addTo(id1);
                            }
                            conn.commit();
                        } catch (Exception e) {
                            conn.rollback();
                            throw e;
                        } finally {
                            conn.setAutoCommit(true);
                        }
                        if (icm != null) {
                            icm.setID(nextid);
                            icm.setDate(new Timestamp(now - TimeZone.getDefault().getOffset(now)));
                            icm.setFrom(UID);
                            icm.setHeadText(fileName);
                            icm.setBodyText(Long.toString(fileLength));
                            icsm.onClientSentMessage(icm);
                        }
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                if (rs != null) {
                    rs.getStatement().close();
                    rs = null;
                }
            } catch (RetryRequest rr) {
                throw rr;
            } catch (Exception e) {
                Tools.makeErrorResponse(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
            } finally {
                try {
                    if (conn != null) icsm.getHeavyDatabaseConnectionPool().releaseConnection(conn);
                } catch (Exception e) {
                }
            }
        }
    }
