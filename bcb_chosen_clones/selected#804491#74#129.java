    public void update(String channelPath, String dataField, String fatherDocId) {
        String sqlInitial = "select uri from t_ip_doc_res where doc_id = '" + fatherDocId + "' and type=" + " '" + ces.platform.infoplat.core.DocResource.DOC_MAGAZINE_TYPE + "' ";
        String sqlsortURL = "update t_ip_doc_res set uri = ? where doc_id = '" + fatherDocId + "' " + " and type = '" + ces.platform.infoplat.core.DocResource.DOC_MAGAZINE_TYPE + "' ";
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            dbo = (ERDBOperation) createDBOperation();
            String url = "";
            boolean flag = true;
            StringTokenizer st = null;
            conn = dbo.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sqlInitial);
            rs = ps.executeQuery();
            if (rs.next()) url = rs.getString(1);
            if (!url.equals("")) {
                st = new StringTokenizer(url, ",");
                String sortDocId = "";
                while (st.hasMoreTokens()) {
                    if (flag) {
                        sortDocId = "'" + st.nextToken() + "'";
                        flag = false;
                    } else {
                        sortDocId = sortDocId + "," + "'" + st.nextToken() + "'";
                    }
                }
                String sqlsort = "select id from t_ip_doc where id in (" + sortDocId + ") order by " + dataField;
                ps = conn.prepareStatement(sqlsort);
                rs = ps.executeQuery();
                String sortURL = "";
                boolean sortflag = true;
                while (rs.next()) {
                    if (sortflag) {
                        sortURL = rs.getString(1);
                        sortflag = false;
                    } else {
                        sortURL = sortURL + "," + rs.getString(1);
                    }
                }
                ps = conn.prepareStatement(sqlsortURL);
                ps.setString(1, sortURL);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            close(rs, null, ps, conn, dbo);
        }
    }
