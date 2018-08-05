    public void create(Session session) {
        Connection conn = session.getConnection(this, true);
        Statement stat = null;
        StringBuilder out = new StringBuilder(256);
        Appendable sql = out;
        List<MetaTable> tables = new ArrayList<MetaTable>();
        List<MetaColumn> newColumns = new ArrayList<MetaColumn>();
        List<MetaColumn> foreignColumns = new ArrayList<MetaColumn>();
        List<MetaIndex> indexes = new ArrayList<MetaIndex>();
        boolean createSequenceTable = false;
        int tableTotalCount = getTableTotalCount();
        boolean anyChange = false;
        try {
            stat = conn.createStatement();
            if (isSequenceTableRequired()) {
                PreparedStatement ps = null;
                ResultSet rs = null;
                Throwable exception = null;
                String logMsg = "";
                try {
                    sql = getDialect().printSequenceCurrentValue(findFirstSequencer(), out);
                    ps = conn.prepareStatement(sql.toString());
                    ps.setString(1, "-");
                    rs = ps.executeQuery();
                } catch (Throwable e) {
                    exception = e;
                }
                if (exception != null) {
                    switch(ORM2DLL_POLICY.of(this)) {
                        case VALIDATE:
                        case WARNING:
                            throw new IllegalStateException(logMsg, exception);
                        case CREATE_DDL:
                        case CREATE_OR_UPDATE_DDL:
                        case INHERITED:
                            createSequenceTable = true;
                    }
                }
                if (LOGGER.isLoggable(Level.INFO)) {
                    logMsg = "Table ''{0}'' {1} available on the database ''{2}''.";
                    logMsg = MessageFormat.format(logMsg, getDialect().getSeqTableModel().getTableName(), exception != null ? "is not" : "is", getId());
                    LOGGER.log(Level.INFO, logMsg);
                }
                try {
                    if (exception != null) {
                        conn.rollback();
                    }
                } finally {
                    close(null, ps, rs, false);
                }
            }
            boolean ddlOnly = false;
            switch(ORM2DLL_POLICY.of(this)) {
                case CREATE_DDL:
                    ddlOnly = true;
                case CREATE_OR_UPDATE_DDL:
                case VALIDATE:
                case WARNING:
                case INHERITED:
                    boolean change = isModelChanged(conn, tables, newColumns, indexes);
                    if (change && ddlOnly) {
                        if (tables.size() < tableTotalCount) {
                            return;
                        }
                    }
                    break;
                case DO_NOTHING:
                default:
                    return;
            }
            switch(MetaParams.CHECK_KEYWORDS.of(getParams())) {
                case WARNING:
                case EXCEPTION:
                    Set<String> keywords = getDialect().getKeywordSet(conn);
                    for (MetaTable table : tables) {
                        if (table.isTable()) {
                            checkKeyWord(MetaTable.NAME.of(table), table, keywords);
                            for (MetaColumn column : MetaTable.COLUMNS.of(table)) {
                                checkKeyWord(MetaColumn.NAME.of(column), table, keywords);
                            }
                        }
                    }
                    for (MetaColumn column : newColumns) {
                        checkKeyWord(MetaColumn.NAME.of(column), column.getTable(), keywords);
                    }
                    for (MetaIndex index : indexes) {
                        checkKeyWord(MetaIndex.NAME.of(index), MetaIndex.TABLE.of(index), keywords);
                    }
            }
            if (tableTotalCount == tables.size()) for (String schema : getSchemas(tables)) {
                out.setLength(0);
                sql = getDialect().printCreateSchema(schema, out);
                if (isFilled(sql)) {
                    try {
                        stat.executeUpdate(sql.toString());
                    } catch (SQLException e) {
                        LOGGER.log(Level.INFO, "{0}: {1}; {2}", new Object[] { e.getClass().getName(), sql.toString(), e.getMessage() });
                        conn.rollback();
                    }
                }
            }
            int tableCount = 0;
            for (MetaTable table : tables) {
                if (table.isTable()) {
                    tableCount++;
                    out.setLength(0);
                    sql = getDialect().printTable(table, out);
                    executeUpdate(sql, stat, table);
                    foreignColumns.addAll(table.getForeignColumns());
                    anyChange = true;
                }
            }
            for (MetaColumn column : newColumns) {
                out.setLength(0);
                sql = getDialect().printAlterTable(column, out);
                executeUpdate(sql, stat, column.getTable());
                anyChange = true;
                if (column.isForeignKey()) {
                    foreignColumns.add(column);
                }
            }
            for (MetaIndex index : indexes) {
                out.setLength(0);
                sql = getDialect().printIndex(index, out);
                executeUpdate(sql, stat, MetaIndex.TABLE.of(index));
                anyChange = true;
            }
            for (MetaColumn column : foreignColumns) {
                if (column.isForeignKey()) {
                    out.setLength(0);
                    MetaTable table = MetaColumn.TABLE.of(column);
                    sql = getDialect().printForeignKey(column, table, out);
                    executeUpdate(sql, stat, column.getTable());
                    anyChange = true;
                }
            }
            if (createSequenceTable) {
                out.setLength(0);
                sql = getDialect().printSequenceTable(this, out);
                final MetaTable table = new MetaTable();
                MetaTable.ORM2DLL_POLICY.setValue(table, MetaParams.ORM2DLL_POLICY.getDefault());
                executeUpdate(sql, stat, table);
            }
            @SuppressWarnings("unchecked") final List<MetaTable> cTables;
            switch(MetaParams.COMMENT_POLICY.of(ormHandler.getParameters())) {
                case FOR_NEW_OBJECT:
                    cTables = tables;
                    break;
                case ALWAYS:
                    cTables = TABLES.getList(this);
                    break;
                case ON_ANY_CHANGE:
                    cTables = anyChange ? TABLES.getList(this) : (List) Collections.emptyList();
                    break;
                case NEVER:
                    cTables = Collections.emptyList();
                    break;
                default:
                    throw new IllegalStateException("Unsupported parameter");
            }
            if (!cTables.isEmpty()) {
                sql = out;
                createTableComments(cTables, stat, out);
            }
            conn.commit();
        } catch (Throwable e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Can't rollback DB" + getId(), ex);
            }
            throw new IllegalArgumentException(Session.SQL_ILLEGAL + sql, e);
        }
    }
