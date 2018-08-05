    public void add(AddInterceptorChain chain, Entry entry, LDAPConstraints constraints) throws LDAPException {
        Connection con = (Connection) chain.getRequest().get(JdbcInsert.MYVD_DB_CON + "LDAPBaseServer");
        if (con == null) {
            throw new LDAPException("Operations Error", LDAPException.OPERATIONS_ERROR, "No Database Connection");
        }
        try {
            con.setAutoCommit(false);
            HashMap<String, String> db2ldap = (HashMap<String, String>) chain.getRequest().get(JdbcInsert.MYVD_DB_DB2LDAP + "LDAPBaseServer");
            PreparedStatement ps = con.prepareStatement("INSERT INTO USERS (id,firstname,lastname,username) VALUES (?,?,?,?)");
            ps.setInt(1, 5);
            ps.setString(2, entry.getEntry().getAttribute(db2ldap.get("firstname")).getStringValue());
            ps.setString(3, entry.getEntry().getAttribute(db2ldap.get("lastname")).getStringValue());
            ps.setString(4, entry.getEntry().getAttribute(db2ldap.get("username")).getStringValue());
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("SELECT id FROM LOCATIONS WHERE name=?");
            PreparedStatement inst = con.prepareStatement("INSERT INTO LOCATIONMAP (person,location) VALUES (?,?)");
            LDAPAttribute l = entry.getEntry().getAttribute(db2ldap.get("name"));
            if (l == null) {
                con.rollback();
                throw new LDAPException("Location is required", LDAPException.OBJECT_CLASS_VIOLATION, "Location is required");
            }
            String[] vals = l.getStringValueArray();
            for (int i = 0; i < vals.length; i++) {
                ps.setString(1, vals[i]);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    con.rollback();
                    throw new LDAPException("Location " + vals[i] + " does not exist", LDAPException.OBJECT_CLASS_VIOLATION, "Location " + vals[i] + " does not exist");
                }
                inst.setInt(1, 5);
                inst.setInt(2, rs.getInt("id"));
                inst.executeUpdate();
            }
            ps.close();
            inst.close();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                throw new LDAPException("Could not add entry or rollback transaction", LDAPException.OPERATIONS_ERROR, e.toString(), e);
            }
            throw new LDAPException("Could not add entry", LDAPException.OPERATIONS_ERROR, e.toString(), e);
        }
    }
