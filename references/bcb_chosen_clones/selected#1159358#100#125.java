    public void delete(DeleteInterceptorChain chain, DistinguishedName dn, LDAPConstraints constraints) throws LDAPException {
        Connection con = (Connection) chain.getRequest().get(JdbcInsert.MYVD_DB_CON + "LDAPBaseServer");
        if (con == null) {
            throw new LDAPException("Operations Error", LDAPException.OPERATIONS_ERROR, "No Database Connection");
        }
        try {
            con.setAutoCommit(false);
            int id = getId(dn, con);
            HashMap<String, String> db2ldap = (HashMap<String, String>) chain.getRequest().get(JdbcInsert.MYVD_DB_DB2LDAP + "LDAPBaseServer");
            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps = con.prepareStatement("DELETE FROM locationmap WHERE person=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                throw new LDAPException("Could not delete entry or rollback transaction", LDAPException.OPERATIONS_ERROR, e.toString(), e);
            }
            throw new LDAPException("Could not delete entry", LDAPException.OPERATIONS_ERROR, e.toString(), e);
        }
    }
