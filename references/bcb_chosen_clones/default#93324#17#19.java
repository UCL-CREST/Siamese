    private static void testBrowser(Desktop desktop) throws IOException, URISyntaxException {
        if (desktop.isDesktopSupported()) desktop.browse(new URI("http://sports.sina.com.cn")); else System.out.println("not support desktop!");
    }
