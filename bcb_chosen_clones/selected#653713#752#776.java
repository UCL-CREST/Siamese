    public void rename(String virtualWiki, String oldTopicName, String newTopicName) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        try {
            boolean commit = false;
            conn.setAutoCommit(false);
            try {
                PreparedStatement pstm = conn.prepareStatement(STATEMENT_RENAME);
                try {
                    pstm.setString(1, newTopicName);
                    pstm.setString(2, oldTopicName);
                    pstm.setString(3, virtualWiki);
                    if (pstm.executeUpdate() == 0) throw new SQLException("Unable to rename topic " + oldTopicName + " on wiki " + virtualWiki);
                } finally {
                    pstm.close();
                }
                doUnlockTopic(conn, virtualWiki, oldTopicName);
                doRenameAllVersions(conn, virtualWiki, oldTopicName, newTopicName);
                commit = true;
            } finally {
                if (commit) conn.commit(); else conn.rollback();
            }
        } finally {
            conn.close();
        }
    }
