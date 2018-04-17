    public boolean find(String strText, String strPattern) {
        Pattern findPattern = Pattern.compile("[a-z]*");
        Matcher matcher = findPattern.matcher(strText);
        int finder = 0;
        while (matcher.find()) {
            finder++;
            System.out.println("Found " + strText.substring(matcher.start(), matcher.end()));
        }
        System.out.println("Found " + finder + " matches in " + strText);
        if (finder > 0) return true;
        return false;
    }
