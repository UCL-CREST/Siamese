    protected void syncMessages(Message message) {
        if (message.getEvent() == Event.CONNECT || message.getEvent() == Event.SYNC_DONE) return;
        logger.info(String.format("remove stale replication messages: %s", message.toString()));
        String className = "";
        long classId = 0;
        if (message.getBody() instanceof Entity) {
            Entity entity = (Entity) message.getBody();
            className = entity.getClass().getName();
            classId = entity.getId();
        }
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            SessionFactoryImplementor impl = (SessionFactoryImplementor) portalDao.getSessionFactory();
            cp = impl.getConnectionProvider();
            conn = cp.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("delete from light_replication_message where event=? and className=? and classId=?");
            ps.setString(1, message.getEvent().toString());
            ps.setString(2, className);
            ps.setLong(3, classId);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            conn.close();
        } catch (Exception e) {
            try {
                conn.rollback();
                ps.close();
                conn.close();
            } catch (Exception se) {
            }
        }
    }
