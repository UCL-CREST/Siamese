    private void saveCampaign() throws HeadlessException {
        try {
            dbConnection.setAutoCommit(false);
            dbConnection.setSavepoint();
            String sql = "UPDATE campaigns SET " + "queue = ? ," + "adjustRatioPeriod = ?, " + "asterisk = ?, " + "context = ?," + "extension = ?, " + "dialContext = ?, " + "dialPrefix = ?," + "dialTimeout = ?, " + "dialingMethod = ?," + "dialsPerFreeResourceRatio = ?, " + "maxIVRChannels = ?, " + "maxDialingThreads = ?," + "maxDialsPerFreeResourceRatio = ?," + "minDialsPerFreeResourceRatio = ?, " + "maxTries = ?, " + "firstRetryAfterMinutes = ?," + "secondRetryAfterMinutes = ?, " + "furtherRetryAfterMinutes = ?, " + "startDate = ?, " + "endDate = ?," + "popUpURL = ?, " + "contactBatchSize = ?, " + "retriesBatchPct = ?, " + "reschedulesBatchPct = ?, " + "allowReschedule = ?, " + "rescheduleToOnself = ?, " + "script = ?," + "agentsCanUpdateContacts = ?, " + "hideContactFields = ?, " + "afterCallWork = ?, " + "reserveAvailableAgents = ?, " + "useDNCList = ?, " + "enableAgentDNC = ?, " + "contactsFilter = ?, " + "DNCTo = ?," + "callRecordingPolicy = ?, " + "callRecordingPercent = ?, " + "callRecordingMaxAge = ?, " + "WHERE name = ?";
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            int i = 1;
            statement.setString(i++, txtQueue.getText());
            statement.setInt(i++, Integer.valueOf(txtAdjustRatio.getText()));
            statement.setString(i++, "");
            statement.setString(i++, txtContext.getText());
            statement.setString(i++, txtExtension.getText());
            statement.setString(i++, txtDialContext.getText());
            statement.setString(i++, txtDialPrefix.getText());
            statement.setInt(i++, 30000);
            statement.setInt(i++, cboDialingMethod.getSelectedIndex());
            statement.setFloat(i++, Float.valueOf(txtInitialDialingRatio.getText()));
            statement.setInt(i++, Integer.valueOf(txtMaxIVRChannels.getText()));
            statement.setInt(i++, Integer.valueOf(txtDialLimit.getText()));
            statement.setFloat(i++, Float.valueOf(txtMaxDialingRatio.getText()));
            statement.setFloat(i++, Float.valueOf(txtMinDialingRatio.getText()));
            statement.setInt(i++, Integer.valueOf(txtMaxRetries.getText()));
            statement.setInt(i++, Integer.valueOf(txtFirstRetry.getText()));
            statement.setInt(i++, Integer.valueOf(txtSecondRetry.getText()));
            statement.setInt(i++, Integer.valueOf(txtFurtherRetries.getText()));
            statement.setDate(i++, Date.valueOf(txtStartDate.getText()));
            statement.setDate(i++, Date.valueOf(txtEndDate.getText()));
            statement.setString(i++, txtURL.getText());
            statement.setInt(i++, Integer.valueOf(txtContactBatchSize.getText()));
            statement.setInt(i++, Integer.valueOf(txtRetryBatchPct.getText()));
            statement.setInt(i++, Integer.valueOf(txtRescheduleBatchPct.getText()));
            statement.setInt(i++, chkAgentCanReschedule.isSelected() ? 1 : 0);
            statement.setInt(i++, chkAgentCanRescheduleSelf.isSelected() ? 1 : 0);
            statement.setString(i++, txtScript.getText());
            statement.setInt(i++, chkAgentCanUpdateContacts.isSelected() ? 1 : 0);
            statement.setString(i++, "");
            statement.setInt(i++, Integer.valueOf(txtACW.getText()));
            statement.setInt(i++, Integer.valueOf(txtReserveAgents.getText()));
            statement.setInt(i++, cboDNCListPreference.getSelectedIndex());
            statement.setInt(i++, 1);
            statement.setString(i++, "");
            statement.setInt(i++, 0);
            statement.setInt(i++, cboRecordingPolicy.getSelectedIndex());
            statement.setInt(i++, Integer.valueOf(txtRecordingPct.getText()));
            statement.setInt(i++, Integer.valueOf(txtRecordingMaxAge.getText()));
            statement.setString(i++, campaign);
            statement.executeUpdate();
            dbConnection.commit();
        } catch (SQLException ex) {
            try {
                dbConnection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, null, ex1);
            }
            JOptionPane.showMessageDialog(this.getRootPane(), ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, null, ex);
        }
    }
