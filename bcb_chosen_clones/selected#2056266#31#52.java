    public void highlightText(String str) {
        String EMailRegex = "([a-zA-Z0-9]+([_+\\.-][a-zA-Z0-9]+)*@([a-zA-Z0-9]+([\\.-][a-zA-Z0-9]+)*)+\\.[a-zA-Z]{2,4})";
        String URLRegex = "(\\b((\\w*(:\\S*)?@)?(http|https|ftp)://[\\S]+)(?=\\s|$))";
        String regex = EMailRegex + "|" + URLRegex;
        Pattern EMailPat = Pattern.compile(regex);
        Matcher EMailMatcher = EMailPat.matcher(str);
        SimpleAttributeSet standard = new SimpleAttributeSet();
        SimpleAttributeSet highlighted = new SimpleAttributeSet();
        StyleConstants.setForeground(highlighted, Color.BLUE);
        StyleConstants.setUnderline(highlighted, true);
        int begin = 0;
        int end;
        while (EMailMatcher.find()) {
            end = EMailMatcher.start();
            if (end > 1) setCharacterAttributes(begin, end - begin, standard, true);
            begin = end;
            end = EMailMatcher.end();
            setCharacterAttributes(begin, end - begin, highlighted, true);
            begin = end;
        }
        setCharacterAttributes(begin, str.length(), standard, true);
    }
