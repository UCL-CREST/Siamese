    public void importCSV(InputStream csvfile) throws Exception {
        try {
            String[] qmarks = new String[columns.length];
            for (int i = 0; i < qmarks.length; i++) {
                qmarks[i] = "?";
            }
            if (cleartable) {
                String delsql = "delete from " + table;
                Statement delstm = conn.createStatement();
                delstm.executeUpdate(delsql);
            }
            String sql = "insert into " + table + " (" + StringUtils.join(columns, ", ") + ") values (" + StringUtils.join(qmarks, ", ") + ")";
            log.debug("SQL: " + sql);
            PreparedStatement stm = conn.prepareStatement(sql);
            int datatypes[] = new int[columns.length];
            for (int i = 0; i < columns.length; i++) {
                Table tbl = project.getTableByName(table);
                if (tbl == null) throw new OntopiaRuntimeException("Unknown table: " + table);
                Column col = tbl.getColumnByName(columns[i]);
                if (col == null) throw new OntopiaRuntimeException("Unknown table column: " + columns[i]);
                if (col.getType() == null) throw new OntopiaRuntimeException("Column type is null: " + col.getType());
                DataType datatype = project.getDataTypeByName(col.getType(), "generic");
                if (datatype == null) throw new OntopiaRuntimeException("Unknown column type: " + col.getType());
                String dtype = datatype.getType();
                if ("varchar".equals(dtype)) datatypes[i] = Types.VARCHAR; else if ("integer".equals(dtype)) datatypes[i] = Types.INTEGER; else throw new OntopiaRuntimeException("Unknown datatype: " + dtype);
            }
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(csvfile));
            for (int i = 0; i < ignorelines; i++) {
                String line = reader.readLine();
                if (line == null) break;
            }
            log.debug("[" + StringUtils.join(columns, ", ") + "]");
            int lineno = 0;
            while (true) {
                lineno++;
                String line = reader.readLine();
                if (line == null) break;
                try {
                    String[] cols = StringUtils.split(line, separator);
                    if (cols.length > columns.length && !ignorecolumns) log.debug("Ignoring columns: " + (columns.length + 1) + "-" + cols.length + " '" + line + "'");
                    log.debug("CVALUES: " + (columns.length + 1) + "-" + cols.length + " '" + line + "'");
                    String dmesg = "(";
                    for (int i = 0; i < columns.length; i++) {
                        String col = cols[i];
                        if (stripquotes) {
                            int len = col.length();
                            if (len > 1 && ((col.charAt(0) == '"' && col.charAt(len - 1) == '"') || (col.charAt(0) == '\'' && col.charAt(len - 1) == '\''))) col = col.substring(1, len - 1);
                        }
                        if (col != null && col.equals("")) col = null;
                        dmesg = dmesg + col;
                        if (i < columns.length - 1) dmesg = dmesg + ", ";
                        stm.setObject(i + 1, col, datatypes[i]);
                    }
                    dmesg = dmesg + ")";
                    log.debug(dmesg);
                    stm.execute();
                } catch (Exception e) {
                    conn.rollback();
                    throw new OntopiaRuntimeException("Cannot read line " + lineno + ": '" + line + "'", e);
                }
            }
            conn.commit();
        } finally {
            if (conn != null) conn.close();
        }
    }
