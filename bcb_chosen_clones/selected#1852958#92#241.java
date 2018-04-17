    public static String flushToCollector(AgentServer as, String IDVotazione) {
        VotazioneData v = (VotazioneData) as.af.votazioni.get(IDVotazione);
        if (Forwarder.enaChkTimeOnFlush) {
            int resultCheckTime = AgentFactory.checkTime(as.af.votazioni, IDVotazione);
            if (resultCheckTime != AgentFactory.C_TimeTooLate) {
                LOGGER.warn("Votazione " + IDVotazione + " non ancora chiusa");
                return "NOTCLOSED";
            }
        }
        String collectorCertBase64 = null;
        String urlCollector = null;
        Iterator<Role4VotazioneData> ia = v.agents.iterator();
        while (ia.hasNext()) {
            Role4VotazioneData r = (Role4VotazioneData) ia.next();
            if (r.role == Role4VotazioneData.C_COLLECTOR) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("FOUND:" + r.IDAgent + " " + r.role);
                AgentData ad = (AgentData) as.af.agents.get(r.IDAgent);
                collectorCertBase64 = ad.cert;
                urlCollector = ad.url;
                break;
            }
        }
        if (collectorCertBase64 == null) return "NOCERTFORCOLLECTOR";
        Certificate collectorCert = null;
        try {
            collectorCert = Crypto.loadCertBase64(collectorCertBase64);
        } catch (Exception e) {
            LOGGER.error("Unexpected exception", e);
            return "CERTCOLLECTORFAULT";
        }
        LOGGER.info("Load Agents");
        Connection conn = null;
        boolean autoCommitPresent = true;
        String ANSWER = "FAILURE";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sconn = as.config.getSconn();
            conn = DriverManager.getConnection(sconn);
            autoCommitPresent = conn.getAutoCommit();
            conn.setAutoCommit(false);
            String query = "" + " SELECT T1,signT1,envelope " + " FROM envelopes " + " WHERE IDVotazione=? AND flushed = 0 ";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, IDVotazione);
            ResultSet rs = pstmt.executeQuery();
            String sT1;
            byte[] T1, signT1, envelopeEnc;
            ByteArrayOutputStream stream_out = new ByteArrayOutputStream();
            ZipOutputStream zip_out = new ZipOutputStream(stream_out);
            zip_out.setLevel(Deflater.DEFAULT_COMPRESSION);
            ByteArrayOutputStream stream_singleVote = null;
            ZipOutputStream zipStream_singleVote = null;
            query = "" + " UPDATE envelopes SET flushed=1 " + " WHERE T1=?";
            PreparedStatement stmt_set_flushed = conn.prepareStatement(query);
            Vector<String> sentT1Buffer = new Vector();
            int k = 0;
            while (rs.next()) {
                sT1 = rs.getString("T1");
                T1 = Base64.decodeBase64(sT1.getBytes("utf-8"));
                signT1 = Base64.decodeBase64(rs.getString("signT1").getBytes("utf-8"));
                envelopeEnc = Base64.decodeBase64(rs.getString("envelope").getBytes("utf-8"));
                stream_singleVote = new ByteArrayOutputStream();
                zipStream_singleVote = new ZipOutputStream(stream_singleVote);
                zipStream_singleVote.setLevel(Deflater.NO_COMPRESSION);
                ZipEntry entry;
                entry = new ZipEntry("T1");
                zipStream_singleVote.putNextEntry(entry);
                zipStream_singleVote.write(T1);
                zipStream_singleVote.closeEntry();
                entry = new ZipEntry("signT1");
                zipStream_singleVote.putNextEntry(entry);
                zipStream_singleVote.write(signT1);
                zipStream_singleVote.closeEntry();
                entry = new ZipEntry("envelope");
                zipStream_singleVote.putNextEntry(entry);
                zipStream_singleVote.write(envelopeEnc);
                zipStream_singleVote.closeEntry();
                zipStream_singleVote.close();
                entry = new ZipEntry("vote_" + (k++));
                zip_out.putNextEntry(entry);
                zip_out.write(stream_singleVote.toByteArray());
                zip_out.closeEntry();
                sentT1Buffer.add(sT1);
            }
            rs.close();
            pstmt.close();
            if (k == 0) {
                try {
                    zip_out.close();
                } finally {
                    LOGGER.info("There aren't item to be flushed");
                    return "OK";
                }
            } else zip_out.close();
            LOGGER.info("Flushing");
            CollectorClient cc = new CollectorClient(urlCollector, collectorCert, as.af.aData.key);
            String response = cc.sendVote(IDVotazione, stream_out.toByteArray());
            if (response.startsWith("FAIL")) {
                LOGGER.error("Problem flushing: " + response);
                ANSWER = "FAILURE";
            } else {
                List<String> T1_received;
                if (response.equals("OK")) {
                    T1_received = new ArrayList();
                } else {
                    LOGGER.error("There are unflushed votes");
                    T1_received = Arrays.asList(response.split("\n"));
                }
                for (String tmpT1 : sentT1Buffer) {
                    if (!T1_received.contains(tmpT1)) {
                        stmt_set_flushed.setString(1, tmpT1);
                        stmt_set_flushed.addBatch();
                    } else {
                        LOGGER.error("Couldn't flush vote with T1: " + tmpT1);
                    }
                }
                stmt_set_flushed.executeBatch();
                conn.commit();
                LOGGER.info("Votes transferred to collector");
                ANSWER = "OK";
            }
        } catch (BatchUpdateException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            LOGGER.error("MySQL error", e);
            ANSWER = "MYSQLFAULT";
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            LOGGER.error("MySQL error", e);
            ANSWER = "MYSQLFAULT";
        } catch (ClassNotFoundException e) {
            LOGGER.error("Couldn't find jdbc driver", e);
            ANSWER = "FAILURE";
        } catch (IOException e) {
            LOGGER.error("IO exception", e);
            ANSWER = "CANNOTCREATEPACKETFAULT";
        } finally {
            try {
                conn.setAutoCommit(autoCommitPresent);
                conn.close();
            } catch (Exception ex) {
            }
            ;
        }
        return ANSWER;
    }
