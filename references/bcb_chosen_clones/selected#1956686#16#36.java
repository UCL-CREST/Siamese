    public NamedParameterProcessor(String sql) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Pattern pattern = Pattern.compile(":[a-zA-Z0-9]+");
        final Matcher matcher = pattern.matcher(sql);
        int paramNum = 0;
        int pos = 0;
        while (matcher.find(pos)) {
            final String prefix = sql.substring(pos, matcher.start());
            final String paramName = sql.substring(matcher.start() + 1, matcher.end());
            List<Integer> integerList = _paramPos.get(paramName);
            if (integerList == null) {
                integerList = new ArrayList<Integer>();
                _paramPos.put(paramName, integerList);
            }
            integerList.add(++paramNum);
            stringBuilder.append(prefix).append("?");
            pos = matcher.end();
        }
        stringBuilder.append(sql.substring(pos));
        _jdbcSql = stringBuilder.toString();
    }
