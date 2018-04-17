    protected static void test01() throws InterruptedException, MalformedURLException, IOException {
        CharSequence content = HttpToolkit.getHTMLString(new URL("http://java.sun.com/javase/6/docs/api/allclasses-frame.html"));
        Pattern pattern = Pattern.compile("A HREF=\"[\\p{Alpha}\\p{Punct}]+\"");
        Matcher matcher = pattern.matcher(content);
        List<CharSequence> urlStringList = new ArrayList<CharSequence>();
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            urlStringList.add(content.subSequence(startIndex + 8, endIndex - 1));
        }
        test01_1(urlStringList);
    }
