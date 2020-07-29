    public void testIntegrityViolation() throws Exception {
        if (getDialect() instanceof MySQLMyISAMDialect) {
            reportSkip("MySQL (ISAM) does not support FK violation checking", "exception conversion");
            return;
        }
        SQLExceptionConverter converter = getDialect().buildSQLExceptionConverter();
        Session session = openSession();
        session.beginTransaction();
        Connection connection = session.connection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO T_MEMBERSHIP (user_id, group_id) VALUES (?, ?)");
            ps.setLong(1, 52134241);
            ps.setLong(2, 5342);
            ps.executeUpdate();
            fail("INSERT should have failed");
        } catch (SQLException sqle) {
            JDBCExceptionReporter.logExceptions(sqle, "Just output!!!!");
            JDBCException jdbcException = converter.convert(sqle, null, null);
            assertEquals("Bad conversion [" + sqle.getMessage() + "]", ConstraintViolationException.class, jdbcException.getClass());
            ConstraintViolationException ex = (ConstraintViolationException) jdbcException;
            System.out.println("Violated constraint name: " + ex.getConstraintName());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Throwable ignore) {
                }
            }
        }
        session.getTransaction().rollback();
        session.close();
    }
