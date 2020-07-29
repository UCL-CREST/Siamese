    public String transformString(String orig) {
        StringBuffer ret = new StringBuffer(orig);
        pattern = Pattern.compile("\\b(\\w{" + minCharacters + "})");
        Matcher matcher = pattern.matcher(orig);
        while (matcher.find()) {
            String substring = orig.substring(matcher.start(1), matcher.end(1));
            ret.setCharAt(matcher.start(1), substring.toUpperCase().charAt(0));
        }
        return ret.toString();
    }
