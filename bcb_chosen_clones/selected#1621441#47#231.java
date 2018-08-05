    public static synchronized int registerVote(String IDVotazione, byte[] T1, byte[] sbT2, byte[] envelope, Config config) {
        if (IDVotazione == null) {
            LOGGER.error("registerVote::IDV null");
            return C_addVote_BOH;
        }
        if (T1 == null) {
            LOGGER.error("registerVote::T1 null");
            return C_addVote_BOH;
        }
        if (envelope == null) {
            LOGGER.error("registerVote::envelope null");
            return C_addVote_BOH;
        }
        LOGGER.info("registering vote started");
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean autoCommitPresent = true;
        int ANSWER = C_addVote_BOH;
        try {
            ByteArrayInputStream tmpXMLStream = new ByteArrayInputStream(envelope);
            SAXReader tmpXMLReader = new SAXReader();
            Document doc = tmpXMLReader.read(tmpXMLStream);
            if (LOGGER.isTraceEnabled()) LOGGER.trace(doc.asXML());
            String sT1 = new String(Base64.encodeBase64(T1), "utf-8");
            String ssbT2 = new String(Base64.encodeBase64(sbT2), "utf-8");
            String sEnvelope = new String(Base64.encodeBase64(envelope), "utf-8");
            LOGGER.trace("loading jdbc driver ...");
            Class.forName("com.mysql.jdbc.Driver");
            LOGGER.trace("... loaded");
            conn = DriverManager.getConnection(config.getSconn());
            autoCommitPresent = conn.getAutoCommit();
            conn.setAutoCommit(false);
            String query = "" + " INSERT INTO votes(IDVotazione, T1, signByT2 , envelope) " + " VALUES           (?          , ? , ?        , ?       ) ";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, IDVotazione);
            stmt.setString(2, sT1);
            stmt.setString(3, ssbT2);
            stmt.setString(4, sEnvelope);
            stmt.executeUpdate();
            stmt.close();
            LOGGER.debug("vote saved for references, now start the parsing");
            query = "" + " INSERT INTO risposte (IDVotazione, T1, IDquestion , myrisposta,freetext) " + " VALUES               (?          , ? , ?          , ?         ,?)        ";
            stmt = conn.prepareStatement(query);
            Element question, itemsElem, rispostaElem;
            List<Element> rispList;
            String id, rispostaText, risposta, freeText, questionType;
            Iterator<Element> questionIterator = doc.selectNodes("/poll/manifest/question").iterator();
            while (questionIterator.hasNext()) {
                question = (Element) questionIterator.next();
                risposta = freeText = "";
                id = question.attributeValue("id");
                itemsElem = question.element("items");
                questionType = itemsElem == null ? "" : itemsElem.attributeValue("type");
                rispostaElem = question.element("myrisposta");
                rispostaText = rispostaElem == null ? "" : rispostaElem.getText();
                if (rispostaText.equals(Votazione.C_TAG_WHITE_XML)) {
                    risposta = C_TAG_WHITE;
                } else if (rispostaText.equals(Votazione.C_TAG_NULL_XML)) {
                    risposta = C_TAG_NULL;
                } else {
                    if (!rispostaText.equals("") && LOGGER.isDebugEnabled()) LOGGER.warn("Risposta text should be empty!: " + rispostaText);
                    risposta = C_TAG_BUG;
                    if (questionType.equals("selection")) {
                        Element rispItem = rispostaElem.element("item");
                        String tmpRisposta = rispItem.attributeValue("index");
                        if (tmpRisposta != null) {
                            risposta = tmpRisposta;
                            if (risposta.equals("0")) freeText = rispItem.getText();
                        }
                    } else if (questionType.equals("borda")) {
                        rispList = rispostaElem.elements("item");
                        if (rispList != null) {
                            risposta = "";
                            String index, tokens;
                            for (Element rispItem : rispList) {
                                index = rispItem.attributeValue("index");
                                tokens = rispItem.attributeValue("tokens");
                                if (index.equals("0")) freeText = rispItem.getText();
                                if (risposta.length() > 0) risposta += ",";
                                risposta += index + ":" + tokens;
                            }
                        }
                    } else if (questionType.equals("ordering")) {
                        rispList = rispostaElem.elements("item");
                        if (rispList != null) {
                            risposta = "";
                            String index, order;
                            for (Element rispItem : rispList) {
                                index = rispItem.attributeValue("index");
                                order = rispItem.attributeValue("order");
                                if (index == null) {
                                    continue;
                                }
                                if (index.equals("0")) freeText = rispItem.getText();
                                if (risposta.length() > 0) risposta += ",";
                                risposta += index + ":" + order;
                            }
                        }
                    } else if (questionType.equals("multiple")) {
                        rispList = rispostaElem.elements("item");
                        if (rispList != null) {
                            risposta = "";
                            String index;
                            for (Element rispItem : rispList) {
                                index = rispItem.attributeValue("index");
                                if (index.equals("0")) freeText = rispItem.getText();
                                if (risposta.length() > 0) risposta += ",";
                                risposta += index;
                            }
                        }
                    } else if (questionType.equals("free")) {
                        freeText = rispostaElem.element("item").getText();
                        risposta = "";
                    }
                }
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("ID_QUESTION: " + id);
                    LOGGER.trace("question type: " + questionType);
                    LOGGER.trace("risposta: " + risposta);
                    LOGGER.trace("freetext: " + freeText);
                }
                if (risposta.equals(C_TAG_BUG)) {
                    LOGGER.error("Invalid answer");
                    LOGGER.error("T1: " + sT1);
                    LOGGER.error("ID_QUESTION: " + id);
                    LOGGER.error("question type: " + questionType);
                }
                stmt.setString(1, IDVotazione);
                stmt.setString(2, sT1);
                stmt.setString(3, id);
                stmt.setString(4, risposta);
                stmt.setString(5, freeText);
                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
            conn.commit();
            ANSWER = C_addVote_OK;
            LOGGER.info("registering vote end successfully");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            if (e.getErrorCode() == 1062) {
                ANSWER = C_addVote_DUPLICATE;
                LOGGER.error("error while registering vote (duplication)");
            } else {
                ANSWER = C_addVote_BOH;
                LOGGER.error("error while registering vote", e);
            }
        } catch (UnsupportedEncodingException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            LOGGER.error("encoding error", e);
            ANSWER = C_addVote_BOH;
        } catch (DocumentException e) {
            LOGGER.error("DocumentException", e);
            ANSWER = C_addVote_BOH;
        } catch (ClassNotFoundException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            LOGGER.error("error while registering vote", e);
            ANSWER = C_addVote_BOH;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            LOGGER.error("Unexpected exception while registering vote", e);
            ANSWER = C_addVote_BOH;
        } finally {
            try {
                conn.setAutoCommit(autoCommitPresent);
                conn.close();
            } catch (Exception e) {
            }
            ;
        }
        return ANSWER;
    }
