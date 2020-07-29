    public static void browse(URL url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException e) {
                logfailure();
            } catch (URISyntaxException e) {
                logger.warn("Error occured while converting URL to URI");
            }
        } else if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            try {
                new ProcessBuilder("xdg-open", url.toString()).start();
            } catch (IOException e) {
                logfailure();
            }
        } else {
            logger.warn("unsupported operating system: {}", System.getProperty("os.name"));
            logfailure();
        }
    }
