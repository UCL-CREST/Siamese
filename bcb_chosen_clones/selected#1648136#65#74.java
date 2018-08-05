    public void testNonGreedyStrong() {
        String page = "Stuff you say, it is<strong>Yea</strong>I find it interesting";
        Pattern p = Pattern.compile("<strong>.*?</strong>");
        Matcher m = p.matcher(page);
        while (m.find()) {
            System.out.println(" ***** : " + m.group());
            System.out.println(" ***** + at " + m.start());
            System.out.println(" ***** - to " + m.end());
        }
    }
