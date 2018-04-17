    @Override
    public DataUpdateResult<Record> updateRecord(String authToken, Record record, Filter filter, Field sourceField, InputModel inputmodel) throws DataOperationException {
        validateUserIsSignedOn(authToken);
        DataUpdateResult<Record> recordUpdateResult = new DataUpdateResult<Record>();
        HttpSession session = getSession();
        if (record != null) {
            Connection connection = null;
            boolean updated = false;
            try {
                connection = DatabaseConnector.getConnection();
                connection.setAutoCommit(false);
                recordUpdateResult.setMessage(messages.server_record_update_success(""));
                recordUpdateResult.setSuccessful(true);
                long userId = getSignedOnUser(authToken).getUserId();
                AuditTrailManager.updateAuditTrail(connection, AuditTrailManager.createAuditTrailEvent(record, userId, AuditableEvent.EVENTYPE_UPDATE), authToken, session);
                if (record.isTopicsChanged()) {
                    ArrayList<Topic> currentTopics = TopicRetrievalServiceImpl.getTopics(record.getRecordid(), getSession(), authToken);
                    TopicUpdateServiceImpl.removeRecordTopics(connection, currentTopics, record.getRecordid());
                    TopicUpdateServiceImpl.insertRecordTopics(connection, record.getTopics(), record.getRecordid());
                }
                ArrayList<RecordAttribute> recordAttributes = record.getRecordattributes();
                if (recordAttributes != null && recordAttributes.size() > 0) {
                    Iterator<RecordAttribute> rItr = recordAttributes.iterator();
                    while (rItr.hasNext()) {
                        RecordAttribute r = rItr.next();
                        if (r.getRecordattributeid() > 0) {
                            if (r.getArchivedtimestamp() == null) {
                                String rAtSql = "update tms.recordattributes set chardata = ? " + "where recordattributeid = ?";
                                PreparedStatement updateRecordAttribute = connection.prepareStatement(rAtSql);
                                updateRecordAttribute.setString(1, r.getChardata());
                                updateRecordAttribute.setLong(2, r.getRecordattributeid());
                                updateRecordAttribute.executeUpdate();
                                AuditTrailManager.updateAuditTrail(connection, AuditTrailManager.createAuditTrailEvent(r, userId, AuditableEvent.EVENTYPE_UPDATE), authToken, session);
                            } else {
                                String rAtSql = "update tms.recordattributes set archivedtimestamp = now() where  recordattributeid = ?";
                                PreparedStatement updateRecordAttribute = connection.prepareStatement(rAtSql);
                                updateRecordAttribute.setLong(1, r.getRecordattributeid());
                                updateRecordAttribute.executeUpdate();
                                AuditTrailManager.updateAuditTrail(connection, AuditTrailManager.createAuditTrailEvent(r, userId, AuditableEvent.EVENTYPE_DELETE), authToken, session);
                            }
                        } else {
                            String rAtSql = "insert into tms.recordattributes " + "(inputmodelfieldid, chardata, recordid) " + "values (?, ?, ?) returning recordattributeid";
                            PreparedStatement insertRecordAttribute = connection.prepareStatement(rAtSql);
                            insertRecordAttribute.setLong(1, r.getInputmodelfieldid());
                            insertRecordAttribute.setString(2, r.getChardata());
                            insertRecordAttribute.setLong(3, record.getRecordid());
                            ResultSet result = insertRecordAttribute.executeQuery();
                            if (result.next()) {
                                long recordattributeid = result.getLong("recordattributeid");
                                r.setRecordattributeid(recordattributeid);
                                AuditTrailManager.updateAuditTrail(connection, AuditTrailManager.createAuditTrailEvent(r, userId, AuditableEvent.EVENTYPE_CREATE), authToken, session);
                            }
                        }
                    }
                }
                ArrayList<Term> terms = record.getTerms();
                Iterator<Term> termsItr = terms.iterator();
                while (termsItr.hasNext()) {
                    Term term = termsItr.next();
                    if (term.getTermid() != -1) TermUpdater.updateTerm(connection, term, userId, authToken, getSession()); else {
                        TermAdditionServiceImpl termAdder = new TermAdditionServiceImpl();
                        termAdder.addTerm(connection, term, userId, authToken, session);
                    }
                }
                connection.commit();
                updated = true;
                if (filter != null) RecordIdTracker.refreshRecordIdsInSessionByFilter(session, connection, true, filter, sourceField, authToken); else RecordIdTracker.refreshRecordIdsInSession(session, connection, false, authToken);
                RecordRetrievalServiceImpl retriever = new RecordRetrievalServiceImpl();
                Record updatedRecord = retriever.retrieveRecordByRecordId(initSignedOnUser(authToken), record.getRecordid(), session, false, inputmodel, authToken);
                recordUpdateResult.setResult(updatedRecord);
            } catch (Exception e) {
                if (!updated && connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException e1) {
                        LogUtility.log(Level.SEVERE, session, messages.log_db_rollback(""), e1, authToken);
                        e1.printStackTrace();
                    }
                }
                recordUpdateResult.setFailed(true);
                if (updated) {
                    recordUpdateResult.setMessage(messages.server_record_update_retrieve(""));
                    recordUpdateResult.setException(e);
                    LogUtility.log(Level.SEVERE, session, messages.server_record_update_retrieve(""), e, authToken);
                } else {
                    recordUpdateResult.setMessage(messages.server_record_update_fail(""));
                    recordUpdateResult.setException(new PersistenceException(e));
                    LogUtility.log(Level.SEVERE, session, messages.server_record_update_fail(""), e, authToken);
                }
                GWT.log(recordUpdateResult.getMessage(), e);
            } finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (Exception e) {
                    LogUtility.log(Level.SEVERE, session, messages.log_db_close(""), e, authToken);
                }
            }
        }
        return recordUpdateResult;
    }
