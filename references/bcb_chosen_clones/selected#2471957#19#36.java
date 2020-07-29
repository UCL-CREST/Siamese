    public String filter(String text) {
        String result = text;
        String regex = "\\[\\[.+?\\]\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        int addedChars = 0;
        while (m.find()) {
            String plainLink = m.group();
            WikiLink link = new WikiLink(plainLink.substring(2, plainLink.length() - 2), defNamespace);
            String formattedLink = link.toHtml();
            int linkStart = m.start() + addedChars;
            String first = result.substring(0, linkStart);
            String end = result.substring(m.end() + addedChars);
            addedChars += formattedLink.length() - plainLink.length();
            result = first + formattedLink + end;
        }
        return result;
    }
