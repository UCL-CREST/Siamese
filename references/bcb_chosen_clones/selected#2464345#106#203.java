    public void executeUpdate(Native nativeResource) throws Exception {
        Connection con = null;
        boolean autoCommit = true;
        PreparedStatement st = null;
        try {
            HrRecord hr = getRepository();
            ManagedConnection mc = returnConnection();
            con = mc.getJdbcConnection();
            autoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            String sql = "";
            boolean isUpdate = false;
            String sUuid = "";
            boolean finableBeforeUpdate = false;
            if (UuidUtil.isUuid(hr.getUuid())) {
                sUuid = hr.getUuid();
                finableBeforeUpdate = queryFindable(con);
                sql = createUpdateSQL();
                st = con.prepareStatement(sql);
                isUpdate = true;
            } else {
                sUuid = UuidUtil.makeUuid(true);
                finableBeforeUpdate = hr.getFindable();
                sql = createInsertSQL();
                st = con.prepareStatement(sql);
            }
            if (hr.getOwnerId() < 0) {
                hr.setOwnerId(getOwner().getLocalID());
            }
            int n = 1;
            st.setInt(n++, hr.getOwnerId());
            st.setTimestamp(n++, makeTimestamp(hr.getInputDate()));
            st.setTimestamp(n++, makeTimestamp(hr.getUpdateDate()));
            st.setString(n++, hr.getName());
            st.setString(n++, hr.getHostUrl());
            st.setString(n++, hr.getHarvestFrequency().toString());
            st.setString(n++, Boolean.toString(hr.getSendNotification()));
            st.setString(n++, hr.getProtocol().getKind().toLowerCase());
            st.setString(n++, ProtocolSerializer.toXmlString(hr.getProtocol()));
            st.setString(n++, PublicationMethod.registration.name());
            if (!isUpdate) {
                if (getRequestContext().getApplicationConfiguration().getHarvesterConfiguration().getResourceAutoApprove()) {
                    st.setString(n++, ApprovalStatus.approved.name());
                } else {
                    st.setString(n++, ApprovalStatus.posted.name());
                }
            }
            st.setString(n++, Boolean.toString(hr.getSearchable()));
            st.setString(n++, Boolean.toString(hr.getSynchronizable()));
            st.setString(n++, sUuid);
            logExpression(sql);
            int nRowCount = st.executeUpdate();
            getActionResult().setNumberOfRecordsModified(nRowCount);
            if (!isUpdate && nRowCount == 1) {
                closeStatement(st);
                st = con.prepareStatement("SELECT ID FROM " + getHarvestingTableName() + " WHERE UPPER(DOCUUID)=?");
                st.setString(1, sUuid.toUpperCase());
                ResultSet genKeys = st.executeQuery();
                genKeys.next();
                int nLocalId = genKeys.getInt(1);
                hr.setLocalId(nLocalId);
                hr.setUuid(sUuid);
                closeResultSet(genKeys);
            }
            con.commit();
            if (nativeResource != null || (isUpdate && finableBeforeUpdate != hr.getFindable())) {
                try {
                    if (nativeResource == null && isUpdate) {
                        nativeResource = queryNative(con);
                    }
                    if (nativeResource != null) {
                        String content = nativeResource.getContent();
                        String sourceUri = nativeResource.getSourceUri().asString();
                        Publisher publisher = createPublisherOfRepository();
                        PublicationRequest publicationRequest = createPublicationRequest(publisher, content, sourceUri);
                        publicationRequest.publish();
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.INFO, "Unable to create resource definition.", ex);
                }
            }
            Harvester harvestEngine = getRequestContext().getApplicationContext().getHarvestingEngine();
            if (_repository.getIsHarvestDue()) {
                harvestEngine.submit(getRequestContext(), _repository, null, _repository.getLastSyncDate());
            }
            harvestEngine.reselect();
        } catch (Exception ex) {
            if (con != null) {
                con.rollback();
            }
            throw ex;
        } finally {
            closeStatement(st);
            if (con != null) {
                con.setAutoCommit(autoCommit);
            }
        }
    }
