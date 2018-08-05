    public void updateUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        log.info("Start update auth");
        PreparedStatement ps = null;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            String sql = "update WM_AUTH_USER " + "set " + "ID_FIRM=?, IS_USE_CURRENT_FIRM=?, " + "ID_HOLDING=?, IS_HOLDING=? " + "WHERE  ID_AUTH_USER=? ";
            ps = db.prepareStatement(sql);
            if (infoAuth.getAuthInfo().getCompanyId() == null) {
                ps.setNull(1, Types.INTEGER);
                ps.setInt(2, 0);
            } else {
                ps.setLong(1, infoAuth.getAuthInfo().getCompanyId());
                ps.setInt(2, infoAuth.getAuthInfo().isCompany() ? 1 : 0);
            }
            if (infoAuth.getAuthInfo().getHoldingId() == null) {
                ps.setNull(3, Types.INTEGER);
                ps.setInt(4, 0);
            } else {
                ps.setLong(3, infoAuth.getAuthInfo().getHoldingId());
                ps.setInt(4, infoAuth.getAuthInfo().isHolding() ? 1 : 0);
            }
            ps.setLong(5, infoAuth.getAuthInfo().getAuthUserId());
            ps.executeUpdate();
            processDeletedRoles(db, infoAuth);
            processNewRoles(db, infoAuth.getRoles(), infoAuth.getAuthInfo().getAuthUserId());
            db.commit();
        } catch (Throwable e) {
            try {
                if (db != null) db.rollback();
            } catch (Exception e001) {
            }
            final String es = "Error add user auth";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(db, ps);
            ps = null;
            db = null;
            log.info("End update auth");
        }
    }
