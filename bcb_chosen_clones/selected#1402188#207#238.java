    public static void help(String s) {
        String url = "http://www.jcpsim.org/" + s + ".html";
        logger.info("HELP: " + url);
        BasicService basicService = null;
        try {
            basicService = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
        } catch (UnavailableServiceException use) {
            logger.info("Java Web Start Services are unavailable.");
        }
        if (basicService != null) {
            logger.info(basicService.getCodeBase().toString());
            try {
                basicService.showDocument(new java.net.URL(url));
            } catch (java.net.MalformedURLException e) {
                logger.info("Malformed URL: " + e.toString());
            }
        } else {
            if ((System.getProperty("java.version").startsWith("1.6"))) {
                if (Desktop.isDesktopSupported()) {
                    if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new java.net.URI(url));
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } catch (URISyntaxException use) {
                            use.printStackTrace();
                        }
                    }
                }
            }
        }
    }
