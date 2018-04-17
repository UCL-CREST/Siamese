    public void guiViewRecommendation(ApplicationType app, RecommendationType rec) {
        try {
            File outputHtml = this.applyViewRecommendationXsl(app, rec);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                URI uri = outputHtml.toURI();
                logger.debug("Displaying via browser: " + uri.toASCIIString());
                desktop.browse(uri);
            } else {
                logger.debug("Displaying via dialog");
                String title = "View Rule: " + super.getUserFriendlyId(app, rec);
                ViewHtmlDialog recDialog = new ViewHtmlDialog(MetaManager.getMainWindow(), true, title, outputHtml);
                recDialog.setVisible(true);
            }
        } catch (Exception ex) {
            logger.warn("", ex);
        }
    }
