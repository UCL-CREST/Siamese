    @Override
    public final boolean save() throws RecordException, RecordValidationException, RecordValidationSyntax {
        if (frozen) {
            throw new RecordException("The object is frozen.");
        }
        boolean toReturn = false;
        Class<? extends Record> actualClass = this.getClass();
        HashMap<String, Integer> columns = getColumns(TableNameResolver.getTableName(actualClass));
        Connection conn = ConnectionManager.getConnection();
        LoggableStatement pStat = null;
        try {
            if (exists()) {
                doValidations(true);
                StatementBuilder builder = new StatementBuilder("update " + TableNameResolver.getTableName(actualClass) + " set");
                String updates = "";
                for (String key : columns.keySet()) {
                    if (!key.equals("id")) {
                        Field f = null;
                        try {
                            f = FieldHandler.findField(actualClass, key);
                        } catch (FieldOrMethodNotFoundException e) {
                            throw new RecordException("Database column name >" + key + "< not found in class " + actualClass.getCanonicalName());
                        }
                        updates += key + " = :" + key + ", ";
                        builder.set(key, FieldHandler.getValue(f, this));
                    }
                }
                builder.append(updates.substring(0, updates.length() - 2));
                builder.append("where id = :id");
                builder.set(":id", FieldHandler.getValue(FieldHandler.findField(actualClass, "id"), this));
                pStat = builder.getPreparedStatement(conn);
                log.log(pStat.getQueryString());
                int i = pStat.executeUpdate();
                toReturn = i == 1;
            } else {
                doValidations(false);
                StatementBuilder builder = new StatementBuilder("insert into " + TableNameResolver.getTableName(actualClass) + " ");
                String names = "";
                String values = "";
                for (String key : columns.keySet()) {
                    Field f = null;
                    try {
                        f = FieldHandler.findField(actualClass, key);
                    } catch (FieldOrMethodNotFoundException e) {
                        throw new RecordException("Database column name >" + key + "< not found in class " + actualClass.getCanonicalName());
                    }
                    if (key.equals("id") && (Integer) FieldHandler.getValue(f, this) == 0) {
                        continue;
                    }
                    names += key + ", ";
                    values += ":" + key + ", ";
                    builder.set(key, f.get(this));
                }
                names = names.substring(0, names.length() - 2);
                values = values.substring(0, values.length() - 2);
                builder.append("(" + names + ")");
                builder.append("values");
                builder.append("(" + values + ")");
                pStat = builder.getPreparedStatement(conn);
                log.log(pStat.getQueryString());
                int i = pStat.executeUpdate();
                toReturn = i == 1;
            }
            if (childList != null) {
                if (childObjects == null) {
                    childObjects = new HashMap<Class<? extends Record>, Record>();
                }
                for (Class<? extends Record> c : childList.keySet()) {
                    if (childObjects.get(c) != null) {
                        childObjects.get(c).save();
                    }
                }
            }
            if (childrenList != null) {
                if (childrenObjects == null) {
                    childrenObjects = new HashMap<Class<? extends Record>, List<? extends Record>>();
                }
                for (Class<? extends Record> c : childrenList.keySet()) {
                    if (childrenObjects.get(c) != null) {
                        for (Record r : childrenObjects.get(c)) {
                            r.save();
                        }
                    }
                }
            }
            if (relatedList != null) {
                if (childrenObjects == null) {
                    childrenObjects = new HashMap<Class<? extends Record>, List<? extends Record>>();
                }
                for (Class<? extends Record> c : relatedList.keySet()) {
                    if (childrenObjects.get(c) != null) {
                        for (Record r : childrenObjects.get(c)) {
                            r.save();
                        }
                    }
                }
            }
            return toReturn;
        } catch (Exception e) {
            if (e instanceof RecordValidationException) {
                throw (RecordValidationException) e;
            }
            if (e instanceof RecordValidationSyntax) {
                throw (RecordValidationSyntax) e;
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback");
            }
            throw new RecordException(e);
        } finally {
            try {
                if (pStat != null) {
                    pStat.close();
                }
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                throw new RecordException("Error closing connection");
            }
        }
    }
