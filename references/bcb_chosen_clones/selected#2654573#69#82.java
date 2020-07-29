    private static String[] findParameters(String sSql) {
        String sPrepared = sSql.replaceAll("('([^'])*')|(\"([^\"])*\")", "");
        ArrayList<String> arStr = new ArrayList<String>();
        Pattern pat = Pattern.compile(":(\\w*)");
        Matcher match = pat.matcher(sPrepared);
        while (match.find()) {
            for (int i = 1; i <= match.groupCount(); i++) {
                match.start(i);
                arStr.add(match.group(i));
                match.end(i);
            }
        }
        return arStr.toArray(new String[arStr.size()]);
    }
