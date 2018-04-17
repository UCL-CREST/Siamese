        return saveDB(con, id, false);
    }

    public long saveDB(Connection con, long id, boolean commit) throws SQLException {
        StringBuffer SQL = null;
        Statement statement = null;
        ResultSet result_set = null;
        try {
            statement = con.createStatement();
            if (id < 0) {
                id = QueryUtils.sequenceGetNextID(con, "PATTERN_OUTLINE");
            } else {
                deleteDB(con, id);
            }
            SQL = new StringBuffer("insert into ");
            SQL.append("PATTERN_OUTLINE values (");
            SQL.append(id);
            SQL.append(",");
            SQL.append(XColor.toInt(pattern.getPatternColor()));
            SQL.append(",");
            SQL.append(pattern.getPatternStyle());
            SQL.append(",");
            SQL.append(pattern.getPatternDensity());
            SQL.append(",");
            SQL.append(XColor.toInt(pattern.getBackgroundColor()));
            SQL.append(",");
            SQL.append(XColor.toInt(outline.getColor()));
            SQL.append(",");
            SQL.append(outline.getStyle());
            SQL.append(",");
            SQL.append(outline.getWidth());
            SQL.append(")");
            statement.executeUpdate(new String(SQL));
            SQL = null;
            if (commit) {
                con.commit();
            }
        } catch (SQLException e) {
            System.err.println(getClass().getName() + ":" + e + " SQL:=" + SQL);
            if (commit) {
                con.rollback();
            }
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
