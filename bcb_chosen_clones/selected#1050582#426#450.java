    public void doViewRule() {
        try {
            ApplicationType app = MetaManager.getMainWindow().getApplicationBar().getApplication();
            MutableTreeNode selectedNode = getSelectedNode();
            if (selectedNode instanceof RecommendationMutableTreeNode) {
                RecommendationReferenceType recRef = ((RecommendationMutableTreeNode) selectedNode).getRecommendation();
                RecommendationType rec = RecommendationHelper.getRecommendationTypeForRecommendation(app, recRef.getStringValue());
                File outputHtml = RecommendationHelper.applyViewRecommendationXsl(currentApplication, rec);
                if (Desktop.isDesktopSupported()) {
                    logger.debug("Displaying via browser");
                    Desktop desktop = Desktop.getDesktop();
                    String path = "file:///" + outputHtml.getCanonicalPath();
                    URL uri = new URL(path);
                    desktop.browse(uri.toURI());
                } else {
                    logger.debug("Displaying via dialog");
                    String title = "View Rule: " + RecommendationHelper.getUserFriendlyId(app, rec);
                    ViewHtmlDialog recDialog = new ViewHtmlDialog(MetaManager.getMainWindow(), true, title, outputHtml);
                    recDialog.setVisible(true);
                }
            }
        } catch (Exception ex) {
            logger.fatal("Unhandled exception while viewing a Rule.", ex);
        }
    }
