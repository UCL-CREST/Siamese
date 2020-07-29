    @Algorithm(name = "EXT")
    public void execute() {
        Connection conn = null;
        try {
            Class.forName(jdbcDriver).newInstance();
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            conn.setAutoCommit(false);
            l.debug("Connected to the database");
            Statement stmt = conn.createStatement();
            l.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            List<Map<String, String>> res = DbUtil.listFromRS(rs);
            if (null != res && !res.isEmpty()) {
                docs = new ArrayList<Doc>();
                List<String> keys = new ArrayList<String>();
                for (Map<String, String> map : res) {
                    docs.add(convert(map));
                    String key = map.get(pk);
                    keys.add(key);
                }
                String sql2 = updateSQL + " where " + pk + " in (" + CollectionUtil.toString(keys) + ")";
                l.debug(sql2);
                stmt.executeUpdate(sql2);
                conn.commit();
            }
        } catch (Exception e) {
            l.error(e.getMessage(), e);
            if (null != conn) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    l.error(ex.getMessage(), ex);
                }
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                    l.debug("Disconnected from database");
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            }
        }
        if (null != docs && !docs.isEmpty()) {
            triggerEvent("EO");
        } else {
            triggerEvent("EMPTY");
        }
    }
