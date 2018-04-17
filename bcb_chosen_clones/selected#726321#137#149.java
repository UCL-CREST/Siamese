    public void gotoSite(JAXXContext rootContext) {
        EpisodesManagerConfig config = rootContext.getContextValue(EpisodesManagerConfig.class);
        URL siteURL = config.getOptionAsURL("application.site.url");
        log.info("goto " + siteURL);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(siteURL.toURI());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                ErrorDialogUI.showError(ex);
            }
        }
    }
