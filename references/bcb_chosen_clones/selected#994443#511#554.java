    @Override
    public User updateUser(User bean) throws SitoolsException {
        checkUser();
        Connection cx = null;
        try {
            cx = ds.getConnection();
            cx.setAutoCommit(false);
            PreparedStatement st;
            int i = 1;
            if (bean.getSecret() != null && !"".equals(bean.getSecret())) {
                st = cx.prepareStatement(jdbcStoreResource.UPDATE_USER_WITH_PW);
                st.setString(i++, bean.getFirstName());
                st.setString(i++, bean.getLastName());
                st.setString(i++, bean.getSecret());
                st.setString(i++, bean.getEmail());
                st.setString(i++, bean.getIdentifier());
            } else {
                st = cx.prepareStatement(jdbcStoreResource.UPDATE_USER_WITHOUT_PW);
                st.setString(i++, bean.getFirstName());
                st.setString(i++, bean.getLastName());
                st.setString(i++, bean.getEmail());
                st.setString(i++, bean.getIdentifier());
            }
            st.executeUpdate();
            st.close();
            if (bean.getProperties() != null) {
                deleteProperties(bean.getIdentifier(), cx);
                createProperties(bean, cx);
            }
            if (!cx.getAutoCommit()) {
                cx.commit();
            }
        } catch (SQLException e) {
            try {
                cx.rollback();
            } catch (SQLException e1) {
                throw new SitoolsException("UPDATE_USER ROLLBACK" + e1.getMessage(), e1);
            }
            throw new SitoolsException("UPDATE_USER " + e.getMessage(), e);
        } finally {
            closeConnection(cx);
        }
        return getUserById(bean.getIdentifier());
    }
