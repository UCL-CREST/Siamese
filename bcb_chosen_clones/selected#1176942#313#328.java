    private ArrayList<String> pathName2ElementsWithAttributes(String pathName) {
        ArrayList<String> result = new ArrayList<String>();
        if (pathName.charAt(0) == '/') pathName = pathName.substring(1, pathName.length());
        String regExpr = "[a-zA-Z0-9:]+?\\[.+?\\]/" + "|" + "[a-zA-Z0-9:]+?/" + "|" + "[a-zA-Z0-9:]+?\\[.+\\]$" + "|" + "[a-zA-Z0-9:]+?$";
        Pattern p = Pattern.compile(regExpr, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher m = p.matcher(pathName);
        while (m.find()) {
            int msBeginPos = m.start();
            int msEndPos = m.end();
            String elementName = pathName.substring(msBeginPos, msEndPos);
            int elemNameSize = elementName.length();
            if (elemNameSize > 0 && elementName.charAt(elemNameSize - 1) == '/') elementName = elementName.substring(0, elemNameSize - 1);
            result.add(elementName);
        }
        return result;
    }
