    @Override
    public List<String> getAllLinks(String string) {
        Pattern p = Pattern.compile(RAPIDSHARE_REGEX);
        Matcher m = p.matcher(string);
        Set<String> links = new LinkedHashSet<String>();
        while (m.find()) {
            String strLink = string.substring(m.start(), m.end());
            links.add(strLink);
        }
        List<String> linksArray = new ArrayList<String>();
        for (String s : links) {
            linksArray.add(s);
        }
        return linksArray;
    }
