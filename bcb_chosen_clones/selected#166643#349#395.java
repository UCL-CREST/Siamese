        void execute(Connection conn, Component parent, String context, final ProgressMonitor progressMonitor, ProgressWrapper progressWrapper) throws Exception {
            int noOfComponents = m_components.length;
            Statement statement = null;
            StringBuffer pmNoteBuf = new StringBuffer(m_update ? "Updating " : "Creating ");
            pmNoteBuf.append(m_itemNameAbbrev);
            pmNoteBuf.append(" ");
            pmNoteBuf.append(m_itemNameValue);
            final String pmNote = pmNoteBuf.toString();
            progressMonitor.setNote(pmNote);
            try {
                conn.setAutoCommit(false);
                int id = -1;
                if (m_update) {
                    statement = conn.createStatement();
                    String sql = getUpdateSql(noOfComponents, m_id);
                    statement.executeUpdate(sql);
                    id = m_id;
                    if (m_indexesChanged) deleteComponents(conn, id);
                } else {
                    PreparedStatement pStmt = getInsertPrepStmt(conn, noOfComponents);
                    pStmt.executeUpdate();
                    Integer res = DbCommon.getAutoGenId(parent, context, pStmt);
                    if (res == null) return;
                    id = res.intValue();
                }
                if (!m_update || m_indexesChanged) {
                    PreparedStatement insertCompPrepStmt = conn.prepareStatement(getInsertComponentPrepStmtSql());
                    for (int i = 0; i < noOfComponents; i++) {
                        createComponent(progressMonitor, m_components, pmNote, id, i, insertCompPrepStmt);
                    }
                }
                conn.commit();
                m_itemTable.getPrimaryId().setVal(m_item, id);
                m_itemCache.updateCache(m_item, id);
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                throw ex;
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        }
