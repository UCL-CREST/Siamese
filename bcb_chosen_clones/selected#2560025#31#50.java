    private static void showBrowser(String tokenKey) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported (fatal)");
            System.exit(1);
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (desktop == null || !desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            System.err.println("Desktop doesn't support the browse action (fatal)");
            System.exit(1);
        }
        try {
            desktop.browse(new URI("http://api.t.sina.com.cn/oauth/authorize?oauth_token=" + tokenKey));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
