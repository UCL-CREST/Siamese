    @Override
    public DataUpdateResult<Record> archiveRecord(String authToken, Record record, Filter filter, Field sourceField, InputModel inputmodel) throws DataOperationException {
        validateUserIsSignedOn(authToken);
        validateUserHasAdminRights(authToken);
        DataUpdateResult<Record> recordUpdateResult = new DataUpdateResult<Record>();
        if (record != null) {
            Connection connection = null;
            boolean archived = false;
            try {
                long userId = getSignedOnUser(authToken).getUserId();
                connection = DatabaseConnector.getConnection();
                connection.setAutoCommit(false);
                recordUpdateResult.setMessage(messages.server_record_delete_success(""));
                recordUpdateResult.setSuccessful(true);
                String sql = "update tms.records set archivedtimestamp = now() where recordid = ?";
                PreparedStatement updateRecord = connection.prepareStatement(sql);
                updateRecord.setLong(1, record.getRecordid());
                int recordArchived = 0;
                recordArchived = updateRecord.executeUpdate();
                if (recordArchived > 0) AuditTrailManager.updateAuditTrail(connection, AuditTrailManager.createAuditTrailEvent(record, userId, AuditableEvent.EVENTYPE_DELETE), authToken, getSession());
                TopicUpdateServiceImpl.archiveRecordTopics(connection, record.getTopics(), record.getRecordid());
                ArrayList<RecordAttribute> recordAttributes = record.getRecordattributes();
                if (recordAttributes != null && recordAttributes.size() > 0) {
                    Iterator<RecordAttribute> rItr = recordAttributes.iterator();
                    while (rItr.hasNext()) {
                        RecordAttribute r = rItr.next();
                        String rAtSql = "update tms.recordattributes set archivedtimestamp = now() where recordattributeid = ?";
                        PreparedStatement updateRecordAttribute = connection.prepareStatement(rAtSql);
                        updateRecordAttribute.setLong(1, r.getRecordattributeid());
                        int recordAttribArchived = 0;
                        recordAttribArchived = updateRecordAttribute.executeUpdate();
                        if (recordAttribArchived > 0) AuditTrailManager.updateAuditTrail(connection, AuditTrailManager.createAuditTrailEvent(r, userId, AuditableEvent.EVENTYPE_DELETE), authToken, getSession());
                    }
                }
                ArrayList<Term> terms = record.getTerms();
                Iterator<Term> termsItr = terms.iterator();
                while (termsItr.hasNext()) {
                    Term term = termsItr.next();
                    TermUpdater.archiveTerm(connection, term, userId, authToken, getSession());
                }
                connection.commit();
                archived = true;
                if (filter != null) RecordIdTracker.refreshRecordIdsInSessionByFilter(this.getThreadLocalRequest().getSession(), connection, true, filter, sourceField, authToken); else RecordIdTracker.refreshRecordIdsInSession(this.getThreadLocalRequest().getSession(), connection, false, authToken);
                RecordRetrievalServiceImpl retriever = new RecordRetrievalServiceImpl();
                RecordIdTracker.refreshRecordIdsInSession(this.getThreadLocalRequest().getSession(), connection, false, authToken);
                Record updatedRecord = retriever.retrieveRecordByRecordId(initSignedOnUser(authToken), record.getRecordid(), this.getThreadLocalRequest().getSession(), false, inputmodel, authToken);
                recordUpdateResult.setResult(updatedRecord);
            } catch (Exception e) {
                if (!archived && connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException e1) {
                        LogUtility.log(Level.SEVERE, getSession(), messages.log_db_rollback(""), e1, authToken);
                        e1.printStackTrace();
                    }
                }
                recordUpdateResult.setFailed(true);
                if (archived) {
                    recordUpdateResult.setMessage(messages.server_record_delete_retrieve(""));
                    recordUpdateResult.setException(e);
                    LogUtility.log(Level.SEVERE, getSession(), messages.server_record_delete_retrieve(""), e, authToken);
                } else {
                    recordUpdateResult.setMessage(messages.server_record_delete_fail(""));
                    recordUpdateResult.setException(new PersistenceException(e));
                    LogUtility.log(Level.SEVERE, getSession(), messages.server_record_delete_fail(""), e, authToken);
                }
                GWT.log(recordUpdateResult.getMessage(), e);
            } finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (Exception e) {
                    LogUtility.log(Level.SEVERE, getSession(), messages.log_db_close(""), e, authToken);
                }
            }
        }
        return recordUpdateResult;
    }
