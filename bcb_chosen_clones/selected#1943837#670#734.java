    protected Object insertSingle(Object name, Object fact) throws SQLException {
        DFAgentDescription dfd = (DFAgentDescription) fact;
        AID agentAID = dfd.getName();
        String agentName = agentAID.getName();
        DFAgentDescription dfdToReturn = null;
        String batchErrMsg = "";
        Connection conn = getConnectionWrapper().getConnection();
        PreparedStatements pss = getPreparedStatements();
        try {
            dfdToReturn = (DFAgentDescription) removeSingle(dfd.getName());
            Date leaseTime = dfd.getLeaseTime();
            long lt = (leaseTime != null ? leaseTime.getTime() : -1);
            String descrId = getGUID();
            pss.stm_insAgentDescr.setString(1, descrId);
            pss.stm_insAgentDescr.setString(2, agentName);
            pss.stm_insAgentDescr.setString(3, String.valueOf(lt));
            pss.stm_insAgentDescr.executeUpdate();
            saveAID(agentAID);
            Iterator iter = dfd.getAllLanguages();
            if (iter.hasNext()) {
                pss.stm_insLanguage.clearBatch();
                while (iter.hasNext()) {
                    pss.stm_insLanguage.setString(1, descrId);
                    pss.stm_insLanguage.setString(2, (String) iter.next());
                    pss.stm_insLanguage.addBatch();
                }
                pss.stm_insLanguage.executeBatch();
            }
            iter = dfd.getAllOntologies();
            if (iter.hasNext()) {
                pss.stm_insOntology.clearBatch();
                while (iter.hasNext()) {
                    pss.stm_insOntology.setString(1, descrId);
                    pss.stm_insOntology.setString(2, (String) iter.next());
                    pss.stm_insOntology.addBatch();
                }
                pss.stm_insOntology.executeBatch();
            }
            iter = dfd.getAllProtocols();
            if (iter.hasNext()) {
                pss.stm_insProtocol.clearBatch();
                while (iter.hasNext()) {
                    pss.stm_insProtocol.setString(1, descrId);
                    pss.stm_insProtocol.setString(2, (String) iter.next());
                    pss.stm_insProtocol.addBatch();
                }
                pss.stm_insProtocol.executeBatch();
            }
            saveServices(descrId, dfd.getAllServices());
            regsCnt++;
            if (regsCnt > MAX_REGISTER_WITHOUT_CLEAN) {
                regsCnt = 0;
                clean();
            }
            conn.commit();
        } catch (SQLException sqle) {
            try {
                conn.rollback();
            } catch (SQLException se) {
                logger.log(Logger.SEVERE, "Rollback for incomplete insertion of DFD for agent " + dfd.getName() + " failed.", se);
            }
            throw sqle;
        }
        return dfdToReturn;
    }
