    @Test
    public void test30_passwordAging() throws Exception {
        Db db = DbConnection.defaultCieDbRW();
        try {
            db.begin();
            Config.setProperty(db, "com.entelience.esis.security.passwordAge", "5", 1);
            PreparedStatement pst = db.prepareStatement("UPDATE e_people SET last_passwd_change = '2006-07-01' WHERE user_name = ?");
            pst.setString(1, "esis");
            db.executeUpdate(pst);
            db.commit();
            p_logout();
            t30login1();
            assertTrue(isPasswordExpired());
            PeopleInfoLine me = getCurrentPeople();
            assertNotNull(me.getPasswordExpirationDate());
            assertTrue(me.getPasswordExpirationDate().before(DateHelper.now()));
            t30chgpasswd();
            assertFalse(isPasswordExpired());
            me = getCurrentPeople();
            assertNotNull(me.getPasswordExpirationDate());
            assertTrue(me.getPasswordExpirationDate().after(DateHelper.now()));
            p_logout();
            t30login2();
            assertFalse(isPasswordExpired());
            t30chgpasswd2();
            db.begin();
            Config.setProperty(db, "com.entelience.esis.security.passwordAge", "0", 1);
            db.commit();
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
        } finally {
            db.safeClose();
        }
    }
