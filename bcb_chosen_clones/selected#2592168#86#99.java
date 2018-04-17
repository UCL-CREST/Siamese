    public static void browseToURI(final String strURI) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(strURI));
            } catch (IOException exp) {
                log.error(exp.toString());
            } catch (URISyntaxException exp) {
                log.error(exp.toString());
            }
        } else {
            log.fatal("Java Desktop API is not supported on this platform.");
        }
    }
