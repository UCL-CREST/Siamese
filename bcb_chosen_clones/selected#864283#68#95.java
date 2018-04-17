    @Override
    public boolean setupDatabaseSchema() {
        Configuration cfg = Configuration.getInstance();
        Connection con = getConnection();
        if (null == con) return false;
        try {
            String sql = FileTool.readFile(cfg.getProperty("database.sql.rootdir") + System.getProperty("file.separator") + cfg.getProperty("database.sql.mysql.setupschema"));
            sql = sql.replaceAll(MYSQL_SQL_SCHEMA_REPLACEMENT, StateSaver.getInstance().getDatabaseSettings().getSchema());
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            String[] sqlParts = sql.split(";");
            for (String sqlPart : sqlParts) {
                if (sqlPart.trim().length() > 0) stmt.executeUpdate(sqlPart);
            }
            con.commit();
            JOptionPane.showMessageDialog(null, language.getProperty("database.messages.executionsuccess"), language.getProperty("dialog.information.title"), JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException e) {
            Logger.logException(e);
        }
        try {
            if (con != null) con.rollback();
        } catch (SQLException e) {
            Logger.logException(e);
        }
        JOptionPane.showMessageDialog(null, language.getProperty("database.messages.executionerror"), language.getProperty("dialog.error.title"), JOptionPane.ERROR_MESSAGE);
        return false;
    }
