    public User createUser(Map userData) throws HamboFatalException {
        DBConnection con = null;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            String userId = (String) userData.get(HamboUser.USER_ID);
            String sql = "insert into user_UserAccount " + "(userid,firstname,lastname,street,zipcode,city," + "province,country,email,cellph,gender,password," + "language,timezn,birthday,datecreated,lastlogin," + "disabled,wapsigned,ldapInSync,offerings,firstcb) " + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, (String) userData.get(HamboUser.FIRST_NAME));
            ps.setString(3, (String) userData.get(HamboUser.LAST_NAME));
            ps.setString(4, (String) userData.get(HamboUser.STREET_ADDRESS));
            ps.setString(5, (String) userData.get(HamboUser.ZIP_CODE));
            ps.setString(6, (String) userData.get(HamboUser.CITY));
            ps.setString(7, (String) userData.get(HamboUser.STATE));
            ps.setString(8, (String) userData.get(HamboUser.COUNTRY));
            ps.setString(9, (String) userData.get(HamboUser.EXTERNAL_EMAIL_ADDRESS));
            ps.setString(10, (String) userData.get(HamboUser.MOBILE_NUMBER));
            ps.setString(11, (String) userData.get(HamboUser.GENDER));
            ps.setString(12, (String) userData.get(HamboUser.PASSWORD));
            ps.setString(13, (String) userData.get(HamboUser.LANGUAGE));
            ps.setString(14, (String) userData.get(HamboUser.TIME_ZONE));
            java.sql.Date date = (java.sql.Date) userData.get(HamboUser.BIRTHDAY);
            if (date != null) ps.setDate(15, date); else ps.setNull(15, Types.DATE);
            date = (java.sql.Date) userData.get(HamboUser.CREATED);
            if (date != null) ps.setDate(16, date); else ps.setNull(16, Types.DATE);
            date = (java.sql.Date) userData.get(HamboUser.LAST_LOGIN);
            if (date != null) ps.setDate(17, date); else ps.setNull(17, Types.DATE);
            Boolean bool = (Boolean) userData.get(HamboUser.DISABLED);
            if (bool != null) ps.setBoolean(18, bool.booleanValue()); else ps.setBoolean(18, UserAccountInfo.DEFAULT_DISABLED);
            bool = (Boolean) userData.get(HamboUser.WAP_ACCOUNT);
            if (bool != null) ps.setBoolean(19, bool.booleanValue()); else ps.setBoolean(19, UserAccountInfo.DEFAULT_WAP_ACCOUNT);
            bool = (Boolean) userData.get(HamboUser.LDAP_IN_SYNC);
            if (bool != null) ps.setBoolean(20, bool.booleanValue()); else ps.setBoolean(20, UserAccountInfo.DEFAULT_LDAP_IN_SYNC);
            bool = (Boolean) userData.get(HamboUser.OFFERINGS);
            if (bool != null) ps.setBoolean(21, bool.booleanValue()); else ps.setBoolean(21, UserAccountInfo.DEFAULT_OFFERINGS);
            ps.setString(22, (String) userData.get(HamboUser.COBRANDING_ID));
            con.executeUpdate(ps, null);
            ps = con.prepareStatement(DBUtil.getQueryCurrentOID(con, "user_UserAccount", "newoid"));
            ResultSet rs = con.executeQuery(ps, null);
            if (rs.next()) {
                OID newOID = new OID(rs.getBigDecimal("newoid").doubleValue());
                userData.put(HamboUser.OID, newOID);
            }
            con.commit();
        } catch (Exception ex) {
            if (con != null) try {
                con.rollback();
            } catch (SQLException sqlex) {
            }
            throw new HamboFatalException(MSG_INSERT_FAILED, ex);
        } finally {
            if (con != null) try {
                con.reset();
            } catch (SQLException ex) {
            }
            if (con != null) con.release();
        }
        return buildUser(userData);
    }
