        private void saveMessage(String server, Message message, byte[] bytes) throws Exception {
            ConnectionProvider cp = null;
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                SessionFactoryImplementor impl = (SessionFactoryImplementor) getPortalDao().getSessionFactory();
                cp = impl.getConnectionProvider();
                conn = cp.getConnection();
                conn.setAutoCommit(false);
                long orgId = 0;
                String className = "";
                long classId = 0;
                if (message.getBody() instanceof Entity) {
                    Entity entity = (Entity) message.getBody();
                    orgId = entity.getOrgId();
                    className = entity.getClass().getName();
                    classId = entity.getId();
                }
                ps = conn.prepareStatement("insert into light_replication_message (orgId,server,event,className,classId,message,createDate) values(?,?,?,?,?,?,?);");
                ps.setLong(1, orgId);
                ps.setString(2, server);
                ps.setString(3, message.getEvent().toString());
                ps.setString(4, className);
                ps.setLong(5, classId);
                ps.setBytes(6, bytes);
                ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                ps.executeUpdate();
                conn.commit();
                ps.close();
                conn.close();
            } catch (Exception e) {
                conn.rollback();
                ps.close();
                conn.close();
                e.printStackTrace();
                throw new Exception(e);
            }
        }
