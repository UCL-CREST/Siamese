    public static List<String> getPlaceHolderList(String sqlBody) {
        ArrayList<String> resultList = new ArrayList();
        Pattern pat = Pattern.compile("[^\\\\]\\$\\{?[0-9a-zA-Z_]+\\}?", Pattern.CASE_INSENSITIVE);
        Matcher match = pat.matcher(sqlBody);
        while (match.find()) {
            String matchedChar = sqlBody.substring(match.start() + 1, match.end());
            if (matchedChar.length() > 3 && (matchedChar.charAt(0) == '{') && (matchedChar.charAt(matchedChar.length() - 1) == '}')) {
                matchedChar = matchedChar.substring(2, matchedChar.length() - 1);
            } else {
                matchedChar = matchedChar.substring(1, matchedChar.length());
            }
            if (!resultList.contains(matchedChar)) {
                resultList.add(matchedChar);
            }
        }
        return resultList;
    }
