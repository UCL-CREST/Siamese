    public static void doIt(String action) {
        int f = -1;
        Statement s = null;
        Connection connection = null;
        try {
            init();
            log.info("<<< Looking up UserTransaction >>>");
            UserTransaction usertransaction = (UserTransaction) context.lookup("java:comp/UserTransaction");
            log.info("<<< beginning the transaction >>>");
            usertransaction.begin();
            log.info("<<< Connecting to xadatasource >>>");
            connection = xadatasource.getConnection();
            log.info("<<< Connected >>>");
            s = connection.createStatement();
            s.executeUpdate("update testdata set foo=foo + 1 where id=1");
            if ((action != null) && action.equals("commit")) {
                log.info("<<< committing the transaction >>>");
                usertransaction.commit();
            } else {
                log.info("<<< rolling back the transaction >>>");
                usertransaction.rollback();
            }
            log.info("<<< transaction complete >>>");
        } catch (Exception e) {
            log.error("doIt", e);
        } finally {
            try {
                s.close();
                connection.close();
            } catch (Exception x) {
                log.error("problem closing statement/connection", x);
            }
        }
    }
