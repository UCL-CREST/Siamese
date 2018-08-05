    public void testLocalUserAccountLocalRemoteRoles() throws SQLException {
        Statement st = null;
        Connection authedCon = null;
        try {
            saSt.executeUpdate("CREATE USER tlualrr PASSWORD 'wontuse'");
            saSt.executeUpdate("GRANT role3 TO tlualrr");
            saSt.executeUpdate("GRANT role4 TO tlualrr");
            saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + getClass().getName() + ".twoRolesFn'");
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "TLUALRR", "unusedPassword");
            } catch (SQLException se) {
                fail("Access with 'twoRolesFn' failed");
            }
            st = authedCon.createStatement();
            assertFalse("Positive test #1 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t1 VALUES(1)"));
            assertFalse("Positive test #2 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t2 VALUES(2)"));
            assertTrue("Negative test #3 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t3 VALUES(3)"));
            assertTrue("Negative test #4 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t4 VALUES(4)"));
            assertEquals(twoRolesSet, AuthUtils.getEnabledRoles(authedCon));
        } finally {
            if (st != null) try {
                st.close();
            } catch (SQLException se) {
                logger.error("Close of Statement failed:" + se);
            } finally {
                st = null;
            }
            if (authedCon != null) try {
                authedCon.rollback();
                authedCon.close();
            } catch (SQLException se) {
                logger.error("Close of Authed Conn. failed:" + se);
            } finally {
                authedCon = null;
            }
        }
    }
