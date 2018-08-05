    private Vendor createVendor() throws SQLException, IOException {
        Connection conn = null;
        Statement st = null;
        String query = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            query = "insert into " + DB.Tbl.vend + "(" + col.title + "," + col.addDate + "," + col.authorId + ") values('" + title + "',now()," + user.getId() + ")";
            st.executeUpdate(query, new String[] { col.id });
            rs = st.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Не удается получить generated key 'id' в таблице vendors.");
            }
            int genId = rs.getInt(1);
            rs.close();
            saveDescr(genId);
            conn.commit();
            Vendor v = new Vendor();
            v.setId(genId);
            v.setTitle(title);
            v.setDescr(descr);
            VendorViewer.getInstance().vendorListChanged();
            return v;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                st.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }
