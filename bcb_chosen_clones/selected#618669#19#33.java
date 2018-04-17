    public String processNode(String sContent) {
        loadSymbolsTable();
        int index = 0;
        Pattern pattern = Pattern.compile("@.[^\",]*");
        Matcher matcher = pattern.matcher(sContent);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(sContent.substring(index, matcher.start()));
            String name = sContent.substring(matcher.start() + 1, matcher.end());
            sb.append(symbolsTable.get(name));
            index = matcher.end();
        }
        sb.append(sContent.substring(index));
        return sb.toString();
    }
