    public void modify(ModifyInterceptorChain chain, DistinguishedName dn, ArrayList<LDAPModification> mods, LDAPConstraints constraints) throws LDAPException {
        Connection con = (Connection) chain.getRequest().get(JdbcInsert.MYVD_DB_CON + "LDAPBaseServer");
        if (con == null) {
            throw new LDAPException("Operations Error", LDAPException.OPERATIONS_ERROR, "No Database Connection");
        }
        try {
            con.setAutoCommit(false);
            HashMap<String, String> db2ldap = (HashMap<String, String>) chain.getRequest().get(JdbcInsert.MYVD_DB_DB2LDAP + "LDAPBaseServer");
            Iterator<LDAPModification> it = mods.iterator();
            String uid = ((RDN) dn.getDN().getRDNs().get(0)).getValue();
            int id = this.getId(dn, con);
            while (it.hasNext()) {
                LDAPModification mod = it.next();
                if (mod.getOp() == LDAPModification.REPLACE) {
                    String attributeName = mod.getAttribute().getName();
                    if (attributeName.equals(db2ldap.get("first")) || attributeName.equals(db2ldap.get("last"))) {
                        PreparedStatement ps = con.prepareStatement("UPDATE USERS SET " + (attributeName.equals(db2ldap.get("first")) ? "first" : "last") + "=? WHERE username=?");
                        ps.setString(1, mod.getAttribute().getStringValue());
                        ps.setString(2, uid);
                        ps.executeUpdate();
                        ps.close();
                    } else if (attributeName.equals(db2ldap.get("username"))) {
                        throw new LDAPException("Can not modify the rdn", LDAPException.NOT_ALLOWED_ON_RDN, "Can not perform modify");
                    } else if (attributeName.equals(db2ldap.get("name"))) {
                        PreparedStatement ps = con.prepareStatement("DELETE FROM locationmap WHERE person=?");
                        ps.setInt(1, id);
                        ps.executeUpdate();
                        ps.close();
                        ps = con.prepareStatement("INSERT INTO locationmap (person,location) VALUES (?,?)");
                        PreparedStatement pssel = con.prepareStatement("SELECT id FROM LOCATIONS WHERE name=?");
                        String[] vals = mod.getAttribute().getStringValueArray();
                        for (int i = 0; i < vals.length; i++) {
                            pssel.setString(1, vals[i]);
                            ResultSet rs = pssel.executeQuery();
                            if (!rs.next()) {
                                con.rollback();
                                throw new LDAPException("Location " + vals[i] + " does not exist", LDAPException.OBJECT_CLASS_VIOLATION, "Location " + vals[i] + " does not exist");
                            }
                            int lid = rs.getInt("id");
                            ps.setInt(1, id);
                            ps.setInt(2, lid);
                            ps.executeUpdate();
                        }
                        ps.close();
                        pssel.close();
                    }
                } else if (mod.getOp() == LDAPModification.DELETE) {
                    if (mod.getAttribute().getName().equals(db2ldap.get("name"))) {
                        String[] vals = mod.getAttribute().getStringValueArray();
                        if (vals.length == 0) {
                            PreparedStatement ps = con.prepareStatement("DELETE FROM locationmap WHERE person=?");
                            ps.setInt(1, id);
                            ps.executeUpdate();
                            ps.close();
                        } else {
                            PreparedStatement ps = con.prepareStatement("DELETE FROM locationmap WHERE person=? and location=?");
                            PreparedStatement pssel = con.prepareStatement("SELECT id FROM LOCATIONS WHERE name=?");
                            for (int i = 0; i < vals.length; i++) {
                                pssel.setString(1, vals[i]);
                                ResultSet rs = pssel.executeQuery();
                                if (!rs.next()) {
                                    con.rollback();
                                    throw new LDAPException("Location " + vals[i] + " does not exist", LDAPException.OBJECT_CLASS_VIOLATION, "Location " + vals[i] + " does not exist");
                                }
                                int lid = rs.getInt("id");
                                ps.setInt(1, id);
                                ps.setInt(2, lid);
                                ps.executeUpdate();
                            }
                            ps.close();
                            pssel.close();
                        }
                    } else {
                        throw new LDAPException("Can not delete attribute " + mod.getAttribute().getName(), LDAPException.INVALID_ATTRIBUTE_SYNTAX, "");
                    }
                } else if (mod.getOp() == LDAPModification.ADD) {
                    if (mod.getAttribute().getName().equals(db2ldap.get("name"))) {
                        String[] vals = mod.getAttribute().getStringValueArray();
                        PreparedStatement ps = con.prepareStatement("INSERT INTO locationmap (person,location) VALUES (?,?)");
                        PreparedStatement pssel = con.prepareStatement("SELECT id FROM LOCATIONS WHERE name=?");
                        for (int i = 0; i < vals.length; i++) {
                            pssel.setString(1, vals[i]);
                            ResultSet rs = pssel.executeQuery();
                            if (!rs.next()) {
                                con.rollback();
                                throw new LDAPException("Location " + vals[i] + " does not exist", LDAPException.OBJECT_CLASS_VIOLATION, "Location " + vals[i] + " does not exist");
                            }
                            int lid = rs.getInt("id");
                            ps.setInt(1, id);
                            ps.setInt(2, lid);
                            ps.executeUpdate();
                        }
                        ps.close();
                        pssel.close();
                    } else {
                        throw new LDAPException("Can not delete attribute " + mod.getAttribute().getName(), LDAPException.INVALID_ATTRIBUTE_SYNTAX, "");
                    }
                }
            }
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
