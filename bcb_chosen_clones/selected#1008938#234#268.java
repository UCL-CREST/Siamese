    private void modifyEntry(ModifyInterceptorChain chain, DistinguishedName dn, ArrayList<LDAPModification> mods, Connection con) throws LDAPException {
        try {
            con.setAutoCommit(false);
            HashMap<String, String> ldap2db = (HashMap<String, String>) chain.getRequest().get(JdbcInsert.MYVD_DB_LDAP2DB + this.dbInsertName);
            Iterator<LDAPModification> it = mods.iterator();
            String sql = "UPDATE " + this.tableName + " SET ";
            while (it.hasNext()) {
                LDAPModification mod = it.next();
                if (mod.getOp() != LDAPModification.REPLACE) {
                    throw new LDAPException("Only modify replace allowed", LDAPException.OBJECT_CLASS_VIOLATION, "");
                }
                sql += ldap2db.get(mod.getAttribute().getName()) + "=? ";
            }
            sql += " WHERE " + this.rdnField + "=?";
            PreparedStatement ps = con.prepareStatement(sql);
            it = mods.iterator();
            int i = 1;
            while (it.hasNext()) {
                LDAPModification mod = it.next();
                ps.setString(i, mod.getAttribute().getStringValue());
                i++;
            }
            String uid = ((RDN) dn.getDN().getRDNs().get(0)).getValue();
            ps.setString(i, uid);
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
