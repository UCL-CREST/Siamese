    private void parseBasic(String uri_path) throws SQLException, InvalidCollectionException {
        if (uri_path != null) {
            String path_content = uri_path.substring(1);
            final TableInfo tableInfo = db.getTableInfo();
            final Pattern patternjoin = Pattern.compile("\\+?\\/\\+?");
            final Matcher matcher = patternjoin.matcher(path_content);
            int table_name_statrt_pos = 0;
            while (matcher.find()) {
                if (matcher.start() != table_name_statrt_pos) {
                    final String table_name = path_content.substring(table_name_statrt_pos, matcher.start());
                    this.parseTableName(table_name, table_name_statrt_pos, tableInfo);
                    switch(matcher.group().length()) {
                        case 1:
                            {
                                joinCollection.add(JoinEnum.INNER);
                                break;
                            }
                        case 2:
                            {
                                if (matcher.group().equals("/+")) {
                                    joinCollection.add(JoinEnum.LEFT);
                                } else {
                                    joinCollection.add(JoinEnum.RIGHT);
                                }
                                break;
                            }
                        case 3:
                            {
                                joinCollection.add(JoinEnum.OUTER);
                                break;
                            }
                    }
                } else {
                    throw new InvalidCollectionException("Symbol " + matcher.group() + " is either set at the beggining of the collection or followed by another join symbol instead of a table.");
                }
                table_name_statrt_pos = matcher.end();
            }
            if (table_name_statrt_pos != path_content.length()) {
                parseTableName(path_content, table_name_statrt_pos, tableInfo);
            } else {
                throw new InvalidCollectionException("Symbol " + matcher.group() + " is located at the end of the collection string.");
            }
        } else {
            throw new InvalidCollectionException("Collection string is null.");
        }
    }
