    public FormatGetter(String format, List<String> colNames) {
        Pattern p = Pattern.compile("%\\{([^}]+)\\}");
        pieces = new ArrayList<ColumnGetter>();
        Matcher m = p.matcher(format);
        int end = 0;
        while (m.find()) {
            if (m.start() > end) pieces.add(new ConstantGetter(format.substring(end, m.start())));
            int mg1 = colNames.indexOf(m.group(1));
            if (mg1 < 0) throw new DataException(String.format("Column not found: %s", m.group(1)));
            pieces.add(new CleanGetter(mg1));
            end = m.end();
        }
        if (end < format.length()) pieces.add(new ConstantGetter(format.substring(end)));
    }
