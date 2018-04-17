    public void add(AddInterceptorChain chain, Entry entry, LDAPConstraints constraints) throws LDAPException {
        Connection con = (Connection) chain.getRequest().get(JdbcInsert.MYVD_DB_CON + this.dbInsertName);
        if (con == null) {
            throw new LDAPException("Operations Error", LDAPException.OPERATIONS_ERROR, "No Database Connection");
        }
        try {
            con.setAutoCommit(false);
            HashMap<String, String> db2ldap = (HashMap<String, String>) chain.getRequest().get(JdbcInsert.MYVD_DB_DB2LDAP + this.dbInsertName);
            String uid = ((RDN) (new DN(entry.getEntry().getDN())).getRDNs().get(0)).getValue();
            PreparedStatement ps = con.prepareStatement(this.insertSQL);
            for (int i = 0; i < this.fields.size(); i++) {
                String field = this.fields.get(i);
                if (field.equals(this.rdnField)) {
                    ps.setString(i + 1, uid);
                } else {
                    ps.setString(i + 1, entry.getEntry().getAttribute(db2ldap.get(field)).getStringValue());
                }
            }
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
