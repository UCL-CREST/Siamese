    public void delete(DeleteInterceptorChain chain, DistinguishedName dn, LDAPConstraints constraints) throws LDAPException {
        Connection con = (Connection) chain.getRequest().get(JdbcInsert.MYVD_DB_CON + this.dbInsertName);
        if (con == null) {
            throw new LDAPException("Operations Error", LDAPException.OPERATIONS_ERROR, "No Database Connection");
        }
        try {
            con.setAutoCommit(false);
            String uid = ((RDN) dn.getDN().getRDNs().get(0)).getValue();
            PreparedStatement ps = con.prepareStatement(this.deleteSQL);
            ps.setString(1, uid);
            ps.executeUpdate();
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
