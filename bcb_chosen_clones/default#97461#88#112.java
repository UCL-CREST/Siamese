    private void queryDB(String q) {
        rows = null;
        rows = new Vector();
        ResultSet rs;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stat = conn.createStatement();
            rs = stat.executeQuery(q);
            while (rs.next()) {
                Vector cols = new Vector();
                cols.add(0, rs.getString("FileName"));
                cols.add(1, rs.getString("baseDir"));
                cols.add(2, rs.getBoolean("Seen"));
                cols.add(3, rs.getString("md5"));
                cols.add(4, rs.getInt("id"));
                rows.add(cols);
            }
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fireTableDataChanged();
    }
