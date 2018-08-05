    public static TypTabelle3<Integer, Integer, String> suche(String text, String regex) {
        TypTabelle3<Integer, Integer, String> erg = TypTabelle3.neu(Integer.class, Integer.class, String.class);
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(text);
        while (matcher.find()) erg.plus(matcher.start(), matcher.end(), matcher.group());
        return erg;
    }
