    public void setValueAt(Object value, int row, int column) {
        System.out.println(value);
        if (column == 2) {
            int id = (Integer) ((Vector) rows.get(row)).get(4);
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:test.db");
                stat = conn.createStatement();
                int v = (Boolean) value ? 0 : 1;
                System.out.println(v);
                System.out.println("update EpisodeFile set seen=" + ((Boolean) value ? 1 : 0) + " where id=" + id + ";");
                stat.executeUpdate("update EpisodeFile set seen=" + ((Boolean) value ? 1 : 0) + " where id=" + id + ";");
                ((Vector) (rows.get(row))).set(2, value);
                fireTableCellUpdated(row, column);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
