    @Override
    public void backup() {
        Connection connection = null;
        PreparedStatement prestm = null;
        try {
            if (logger.isInfoEnabled()) logger.info("backup table " + getOrigin() + " start...");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            String tableExistsResult = "";
            prestm = connection.prepareStatement("show tables from " + schema + " like '" + getDestination() + "';");
            ResultSet rs = prestm.executeQuery();
            if (rs.next()) tableExistsResult = rs.getString(1);
            rs.close();
            prestm.close();
            if (StringUtils.isBlank(tableExistsResult)) {
                String createTableSql = "";
                prestm = connection.prepareStatement("show create table " + getOrigin() + ";");
                rs = prestm.executeQuery();
                if (rs.next()) createTableSql = rs.getString(2);
                rs.close();
                prestm.close();
                createTableSql = createTableSql.replaceAll("`" + getOrigin() + "`", "`" + getDestination() + "`");
                createTableSql = createTableSql.replaceAll("auto_increment", "");
                createTableSql = createTableSql.replaceAll("AUTO_INCREMENT", "");
                Matcher matcher = stripRelationTablePattern.matcher(createTableSql);
                if (matcher.find()) createTableSql = matcher.replaceAll("");
                matcher = normalizePattern.matcher(createTableSql);
                if (matcher.find()) createTableSql = matcher.replaceAll("\n )");
                Statement stm = connection.createStatement();
                stm.execute(createTableSql);
                if (logger.isDebugEnabled()) logger.debug("table '" + getDestination() + "' created!");
            } else if (logger.isDebugEnabled()) logger.debug("table '" + getDestination() + "' already exists");
            Date date = new Date();
            date.setTime(TimeUtil.addHours(date, -getHours()).getTimeInMillis());
            date.setTime(TimeUtil.getTodayAtMidnight().getTimeInMillis());
            if (logger.isInfoEnabled()) logger.info("backuping records before: " + date);
            long currentRows = 0L;
            prestm = connection.prepareStatement("select count(*) from " + getOrigin() + " where " + getCondition() + "");
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            prestm.setDate(1, sqlDate);
            rs = prestm.executeQuery();
            if (rs.next()) currentRows = rs.getLong(1);
            rs.close();
            prestm.close();
            if (currentRows > 0) {
                connection.setAutoCommit(false);
                prestm = connection.prepareStatement("INSERT INTO " + getDestination() + " SELECT * FROM " + getOrigin() + " WHERE " + getCondition());
                prestm.setDate(1, sqlDate);
                int rows = prestm.executeUpdate();
                prestm.close();
                if (logger.isInfoEnabled()) logger.info(rows + " rows backupped");
                prestm = connection.prepareStatement("DELETE FROM " + getOrigin() + " WHERE " + getCondition());
                prestm.setDate(1, sqlDate);
                rows = prestm.executeUpdate();
                prestm.close();
                connection.commit();
                if (logger.isInfoEnabled()) logger.info(rows + " rows deleted");
            } else if (logger.isInfoEnabled()) logger.info("no backup need");
            if (logger.isInfoEnabled()) logger.info("backup table " + getOrigin() + " end");
        } catch (SQLException e) {
            logger.error(e, e);
            if (applicationContext != null) applicationContext.publishEvent(new TrapEvent(this, "dbcon", "Errore SQL durante il backup dei dati della tabella " + getOrigin(), e));
            try {
                connection.rollback();
            } catch (SQLException e1) {
            }
        } catch (Throwable e) {
            logger.error(e, e);
            if (applicationContext != null) applicationContext.publishEvent(new TrapEvent(this, "generic", "Errore generico durante il backup dei dati della tabella " + getOrigin(), e));
            try {
                connection.rollback();
            } catch (SQLException e1) {
            }
        } finally {
            try {
                if (prestm != null) prestm.close();
            } catch (SQLException e) {
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
