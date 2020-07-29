    public static void openDefaultBrowser(String url) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = null;
                try {
                    uri = new URI(url);
                    desktop.browse(uri);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "", e);
                } catch (URISyntaxException e) {
                    logger.log(Level.SEVERE, "", e);
                }
                logger.info(LanguageResource.getLanguage().getString("info.browser.open"));
                LoggingDesktopController.printInfo(MessageFormat.format(LanguageResource.getLanguage().getString("info.browser.open"), url));
            } else {
                logger.severe(LanguageResource.getLanguage().getString("error.no_desktop_support"));
                LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_desktop_support"));
            }
        } else {
            logger.severe(LanguageResource.getLanguage().getString("error.no_desktop_support"));
            LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_desktop_support"));
        }
    }
