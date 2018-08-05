    private static List<String> insertWhitespace(List<String> lines) {
        List<String> returnList = new ArrayList<String>();
        StringBuilder bui = new StringBuilder();
        Pattern blankBeforeAndAfter = Pattern.compile("&&|\\|\\||(\\!|<|>|\\||&|\\+|-)?\\={1,3}|\\?|:|\\+|-|\\*|\\{|\\,|!");
        for (String line : lines) {
            if (line.startsWith(LITERAL_PREFIX)) {
                returnList.add(line);
                continue;
            }
            Matcher m = blankBeforeAndAfter.matcher(line);
            if (!m.find()) {
                returnList.add(line);
                continue;
            }
            bui.setLength(0);
            int from = 0;
            do {
                bui.append(line.substring(from, m.start()));
                bui.append(' ');
                bui.append(m.group());
                bui.append(' ');
                from = m.end();
            } while (m.find());
            bui.append(line.substring(from, line.length()));
            returnList.add(bui.toString());
        }
        return returnList;
    }
