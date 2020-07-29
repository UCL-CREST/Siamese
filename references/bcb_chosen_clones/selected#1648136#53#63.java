    public void testHref() {
        String page = "Stuff you say, it is<a href=\"coolsite.htm\">Yea</a>I find it interesting";
        Pattern p = Pattern.compile("(<a.*?href=.*?>(.*?)</a>)");
        Matcher m = p.matcher(page);
        while (m.find()) {
            System.out.println(" ***** : " + m.groupCount());
            System.out.println(" ***** : " + m.group());
            System.out.println(" ***** + at " + m.start());
            System.out.println(" ***** - to " + m.end());
        }
    }
