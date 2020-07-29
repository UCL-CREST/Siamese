    public void guiViewGroup(ApplicationType app, GroupType group) {
        try {
            File outputHtml = this.applyViewGroupXsl(app, group);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                URI uri = outputHtml.toURI();
                logger.debug("Displaying via browser: " + uri.toASCIIString());
                desktop.browse(uri);
            } else {
                logger.debug("Displaying via dialog");
                String title = "View Group: " + group.getTitle();
                ViewHtmlDialog recDialog = new ViewHtmlDialog(MetaManager.getMainWindow(), true, title, outputHtml);
                recDialog.setVisible(true);
            }
        } catch (Exception ex) {
            logger.warn(ex);
        }
    }
