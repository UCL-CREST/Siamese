    private void setTablesJoinsAndBoundaries(String collection) throws SQLException, QueryException {
        if (collection != null) {
            final Pattern pattern = Pattern.compile("!?\\+!?");
            final Matcher matcher = pattern.matcher(collection);
            int pos = 0;
            while (matcher.find()) {
                if (matcher.start() != pos) {
                    final String[] s = collection.substring(pos, matcher.start()).split("\\?", 2);
                    tables.add(s[0]);
                    if (s.length > 1) {
                        QueryString qs = new QueryString(s[1]);
                        setBoundedColumns(s[0], qs);
                        setFilterBoundaries(s[0], qs);
                    }
                    switch(matcher.group().length()) {
                        case 1:
                            {
                                joinOperations.add(Join.INNER);
                                break;
                            }
                        case 2:
                            {
                                if (matcher.group().equals("|+")) {
                                    joinOperations.add(Join.LEFT);
                                } else {
                                    joinOperations.add(Join.RIGHT);
                                }
                                break;
                            }
                        case 3:
                            {
                                joinOperations.add(Join.OUTER);
                                break;
                            }
                    }
                } else {
                    throw new QueryException(QueryExceptionState.INVALID_COLLECTION, "Symbol " + matcher.group() + " is either set at the beggining of the collection or followed by another join symbol instead of a table.");
                }
                pos = matcher.end();
            }
            if (pos != collection.length()) {
                final String[] s = collection.substring(pos).split("\\?", 2);
                tables.add(s[0]);
                if (s.length > 1) {
                    QueryString qs = new QueryString(s[1]);
                    setBoundedColumns(s[0], qs);
                    setFilterBoundaries(s[0], qs);
                }
            } else {
                throw new QueryException(QueryExceptionState.INVALID_COLLECTION, "Symbol " + matcher.group() + " is located at the end of the collection string.");
            }
        } else {
            throw new QueryException(QueryExceptionState.INVALID_COLLECTION, "Collection string is null.");
        }
    }
