    public static void main(String[] args) {
        Console c = System.console();
        assert (c != null) : "console cannot be null";
        String matcherStr = c.readLine("%s", "Matcher: ");
        String patternStr = c.readLine("%s", "Pattern: ");
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(matcherStr);
        System.out.println("Pattern is " + m.pattern());
        while (m.find()) {
            System.out.println(m.start() + " " + m.group() + " " + m.end());
        }
    }
